package com.kylantis.eaa.core.fusion;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ce.ems.base.classes.spec.BlobSpec;
import com.ce.ems.base.core.AppUtils;
import com.ce.ems.base.core.Application;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Todo;
import com.ce.ems.base.core.UsesMock;
import com.ce.ems.models.PlatformModel;
import com.ce.ems.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kylantis.eaa.core.keys.CacheValues;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@UsesMock
@Todo("Add sexy configuration params")
public class WebRoutes {

	// @DEV
	public static ExecutorService fileWatcherPool = null;
	// @DEV
	public static WatchService watchService = null;
	// @DEV
	private static final Path PROJECT_RESOURCES_FOLDER = Paths
			.get("/Users/anthony.anyanwu/workspace/ems/src/main/resources");

	protected static final String webFolder = "web/public_html";

	// @DEV
	protected static Path webFolderURI = AppUtils.getPath(webFolder);

	public static String DEFAULT_INDEX_URI = "/";

	public static String NOT_FOUND_URI = "/404";

	public static String DEFAULT_SETUP_URI = "/setup/one";

	public static String DEFAULT_CONSOLE_URI = "/console";

	public static String DEFAULT_LOGIN_URI = "/user-login";

	private static Pattern webpagePattern = Pattern
			.compile("\\A[a-zA-Z0-9]+([_]*[-]*[a-zA-Z0-9]+)*\\Q.\\E(html|htm|xhtml){1}\\z");
	// Let's be lenient a little with the fileName
	// .compile("\\A[a-zA-Z]+(\\Q_\\E[a-zA-Z]+)*\\Q.\\E[a-zA-Z]+\\z");

	public static Pattern webpageUriPattern = Pattern
			.compile("\\A\\Q/\\E[a-zA-Z]+[-]*[a-zA-Z]+(\\Q/\\E[a-zA-Z]+[-]*[a-zA-Z]+)*\\z");

	public static Tika TIKA_INSTANCE = new Tika();

	protected static final Map<String, List<String>> routeParams = new HashMap<>();

	protected static final Map<Integer, String> functionalityToRoutesMappings = new HashMap<>();

	static final Map<String, WebRouteSpec> routeFunctionalities = new HashMap<>();

	private static final Map<String, PageSpec> resourceMappings = new LinkedHashMap<>();

	private static final Map<String, BlobSpec> routesData = new HashMap<>();

	private static final Map<String, String> routes = new HashMap<>();

	public static final String USER_ID_PARAM_NAME = "x_uid";

	@Todo("Create util for wallking file tree")
	public static void scanRoutes() throws IOException {

		Logger.debug("Scanning for web routes");

		Gson gson = GsonFactory.newInstance();

		// Get Route functionalities

		JsonObject route_functionalities = new JsonObject(
				Utils.getString(Files.newInputStream(webFolderURI.resolve("route_functionalities.json"))));

		route_functionalities.forEach(e -> {

			// Map functionality to route. Note: only some routes have this mapping

			WebRouteSpec spec = gson.fromJson(e.getValue().toString(), WebRouteSpec.class).setUri(e.getKey());

			spec.getMin().forEach(i -> {
				functionalityToRoutesMappings.put(i, spec.getUri());
			});

			spec.getMax().forEach(i -> {
				functionalityToRoutesMappings.put(i, spec.getUri());
			});

			routeFunctionalities.put(e.getKey(), spec);
		});

		// Get Route params

		JsonObject route_params = new JsonObject(
				Utils.getString(Files.newInputStream(webFolderURI.resolve("route_params.json"))));

		route_params.forEach(e -> {

			List<String> params = gson.fromJson(e.getValue().toString(), new TypeToken<List<String>>() {
			}.getType());
			routeParams.put(e.getKey(), params);
		});

		// Walk tree inorder to expose all files in the <webFolderURI>

		Files.walkFileTree(webFolderURI, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				Path path = webFolderURI.relativize(file);

				String uri = path.toString().replaceAll("\\\\", "/");

				if (webpagePattern.matcher(uri).matches()) {
					uri = toCanonicalURI(uri);
				} else {
					uri = "/" + uri;
				}

				Logger.trace(uri + " => " + file.toFile());
				routes.put(uri, path.toString());

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
				if (e == null) {
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}
		});

		routes.put("/", "index.html");

		// Get web resource mapping

		JsonObject resource_mapping = new JsonObject(
				Utils.getString(Files.newInputStream(webFolderURI.resolve("resource_mapping.json")))); 

		resource_mapping.forEach(e -> { 

			PageSpec spec = gson.fromJson(e.getValue().toString(), PageSpec.class); 

			resourceMappings.put(e.getKey(), spec); 

		});  

		// For all entries, put data in <routesData>. 

		resourceMappings.forEach((uri, spec) -> {

			String fileContent = null;
			try { 
				fileContent = Utils.getString(Files.newInputStream(webFolderURI.resolve(Paths.get(routes.get(uri)))));

				if (spec.getParent() != null) {

					// The general contract is that parents should exist in resource_mapping.json,
					// before children

					fileContent = mergeHtml(uri, fileContent, new String(routesData.get(spec.getParent()).getData()));
				}

			} catch (IOException ex) {
				Exceptions.throwRuntime(ex);
			}

			byte[] bytes = fileContent.getBytes();
			BlobSpec bs = new BlobSpec().setMimeType(TIKA_INSTANCE.detect(routes.get(uri))).setData(bytes);

			routesData.put(uri, bs);
		});

		// @Dev Watch files in <routesData>:

		if (!Application.isProduction()) {

			startWatchService();

			List<String> watchedFolders = new ArrayList<>();

			routesData.keySet().forEach(k -> {

				Path folder = PROJECT_RESOURCES_FOLDER.resolve(webFolder).resolve(Paths.get(routes.get(k))).getParent();

				if (!watchedFolders.contains(folder.toString())) {
					try {
						folder.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
						watchedFolders.add(folder.toString());

					} catch (IOException ex) {
						Exceptions.throwRuntime(ex);
					}
				}
			});

			Logger.trace("Watching the following folders for changes: ");
			watchedFolders.forEach(s -> {
				Logger.trace("* " + s);
			});

			fileWatcherPool.execute(new Runnable() {

				@Override
				public void run() {
					WatchKey key;
					try {
						while ((key = watchService.take()) != null) {
							for (WatchEvent<?> event : key.pollEvents()) {

								WatchEvent.Kind<?> kind = event.kind();
								if (kind == StandardWatchEventKinds.OVERFLOW) {
									continue;
								}

								WatchEvent<Path> ev = (WatchEvent<Path>) event;
								Path file = Paths.get(key.watchable().toString()).resolve(ev.context());

								if (file.toFile().isDirectory()) {
									continue;
								}

								String uri = PROJECT_RESOURCES_FOLDER.resolve(webFolder).relativize(file).toString()
										.replaceAll("\\\\", "/");

								if (webpagePattern.matcher(uri).matches()) {
									uri = toCanonicalURI(uri);
								} else {
									uri = "/" + uri;
								}

								String fileContent = null;
								try {
									fileContent = Utils.getString(Files.newInputStream(file));

								} catch (IOException ex) {
									Exceptions.throwRuntime(ex);
								}

								onFileChange(uri, fileContent);
							}
							key.reset();
						}
					} catch (InterruptedException e) {
						// Its totally fine. This file watcher thread was destroyed probably due to
						// application shutdown
					}
				}
			});

		}
 
	}

	private static final void onFileChange(String uri, String fileContent) {   
 
		PageSpec spec = resourceMappings.get(uri);

		if (spec != null && spec.getParent() != null) {
			fileContent = mergeHtml(uri, fileContent, new String(routesData.get(spec.getParent()).getData()));
		}

		byte[] bytes = fileContent.getBytes();
		BlobSpec bs = new BlobSpec().setMimeType(TIKA_INSTANCE.detect(routes.get(uri))).setData(bytes);

		routesData.put(uri, bs);

		// Check if this is a parent, and update all children, if necessary
		resourceMappings.forEach((k, v) -> {
			if (v.getParent() != null && v.getParent().equals(uri)) {
				
				String childContents = null;
				try {
					childContents = Utils.getString(Files.newInputStream(webFolderURI.resolve(Paths.get(routes.get(k)))));
				} catch (IOException ex) {
					Exceptions.throwRuntime(ex);
				}
				
				onFileChange(k, childContents);
			}
		});
	}

	private static String mergeHtml(String uri, String childContents, String parentContents) {

		// Read files
		Document childPage = Jsoup.parse(childContents);
		Document parentPage = Jsoup.parse(parentContents);

		Element childHead = childPage.select("head").first();
		Element childMainContent = childPage.select("#ce-page-main-container").first();
		Element childScripts = childPage.select("#ce-page-scripts").first();

		parentPage.select("head").first().append(childHead.html());
		parentPage.select("#ce-page-main-container").html(childMainContent.html());
		parentPage.select("body").first().append(childScripts.html());

		return parentPage.toString();
	}

	private static String toCanonicalURI(String fileName) {
		return "/"

				// replace underscores
				+ fileName.replaceAll("_", "/")
						// remove file extensions
						.split(Pattern.quote("."))[0];
	}

	protected static Router get() {
		final Router router = Router.router(WebServer.vertX);
		router.get().handler(ctx -> {
			get(ctx);
		});
		return router;
	}

	private static boolean handleAuth(RoutingContext ctx, List<Integer> functionalityIds) {
		for (Integer f : functionalityIds) {
			if (!handleAuth(ctx, f)) {
				return false;
			}
		}
		return true;
	}

	private static boolean handleAuth(RoutingContext ctx, Integer functionalityId) {

		if (functionalityId < 0) {
			return true;
		}

		// Login is required, at the barest minimum
		// Get sessionToken from either a cookie or request header
		String sessionToken;
		try {
			sessionToken = ctx.getCookie(FusionHelper.sessionTokenName()).getValue();
		} catch (NullPointerException e) {
			sessionToken = ctx.request().getHeader(FusionHelper.sessionTokenName());
		}

		if (sessionToken == null) {
			return false;
		}

		// Verify that sessionToken is valid
		Long userId = FusionHelper.getUserIdFromToken(sessionToken);

		if (userId == null) {
			return false;
		} else if (functionalityId == 0) {
			return true;
		}

		if (functionalityId > 0) {

			for (String roleName : FusionHelper.getRoles(userId)) {
				if (FusionHelper.isAccessAllowed(roleName, functionalityId)) {
					return true;
				}
			}
		}

		return false;
	}

	@BlockerTodo("Stop parsing content type with TIKA on each request."
			+ " On line 446, select a suitable functionality, instead of using [0], or investigate")
	public static final void get(RoutingContext ctx) {

		String uri = ctx.request().path();

		if (uri.equals("/")) {
			if (!PlatformModel.isInstalled()) {
				uri = DEFAULT_SETUP_URI;
			}
		}

		if (!routes.containsKey(uri) && !uri.equals(DEFAULT_CONSOLE_URI)) {

			if (webpageUriPattern.matcher(uri).matches()) {
				ctx.response().setStatusCode(HttpServletResponse.SC_FOUND).putHeader("Location", NOT_FOUND_URI).end();
			} else {
				ctx.response().setStatusCode(HttpServletResponse.SC_NOT_FOUND);
			}
			return;
		}

		// Improve security
		ctx.response()

				// allow proxies to cache the data
				// .putHeader("Cache-Control", "public,
				// max-age=31536000") //change to 600 secs

				// @DEV
				.putHeader("Cache-Control", "no-cache, no-store, must-revalidate")

				// prevents Internet Explorer from MIME -
				// sniffing a
				// response away from the declared content-type
				.putHeader("X-Content-Type-Options", "nosniff")
				// Strict HTTPS (for about ~6Months)
				.putHeader("Strict-Transport-Security", "max-age=" + 15768000)
				// IE8+ do not allow opening of attachments in
				// the context
				// of this resource
				.putHeader("X-Download-Options", "noopen")
				// enable XSS for IE
				.putHeader("X-XSS-Protection", "1; mode=block")
				// deny frames
				.putHeader("X-FRAME-OPTIONS", "DENY");

		WebRouteSpec spec = routeFunctionalities.get(uri);
		 
		if (spec != null) {
			if (!handleAuth(ctx, spec.getMin())) {
				ctx.response().setStatusCode(HttpServletResponse.SC_FOUND).putHeader("Location", DEFAULT_LOGIN_URI
						+ "?returnUrl=" + uri + (ctx.request().query() != null ? "?" + ctx.request().query() : ""))
						.end();
				return;
			}
		}

		if ((uri.equals(DEFAULT_LOGIN_URI))) {

			String returnUriString = ctx.request().getParam("returnUrl");
			String returnUri = returnUriString != null ? URI.create(returnUriString).getPath() : null;

			boolean canAccess = false;

			if (returnUri != null) {
				// Verify that user has the right to view returnUrl
				canAccess = handleAuth( ctx, routeFunctionalities.get(returnUri).getMin());
			}

			if(!canAccess && handleAuth(ctx, 0)) {
				// If a user is logged in, then fallback to a page he has access to view
				List<Integer> functionalities = FusionHelper.functionalities(ctx);
				returnUriString = returnUri = functionalityToRoutesMappings.get(functionalities.get(0));
				canAccess = true;
			}
			
			if(canAccess) {
				ctx.response().setStatusCode(HttpServletResponse.SC_FOUND)
						.putHeader("Location", returnUriString).end();
				return;
			}
		}

		ctx.request().params().forEach(e -> {
			ctx.addCookie(new CookieImpl(e.getKey(), e.getValue()).setPath(ctx.request().path())
					.setMaxAge(CacheValues.REQUEST_PARAM_COOKIE_EXPIRY_IN_SECS));
		});

		if (uri.equals(DEFAULT_CONSOLE_URI)) {
			List<Integer> functionalities = FusionHelper.functionalities(ctx);
			ctx.reroute(functionalityToRoutesMappings.get(functionalities.get(0)));
			return;
		}
		
		if (routesData.containsKey(uri)) {

			BlobSpec data = routesData.get(uri);
			ctx.response().putHeader("content-type", data.getMimeType()).write(Buffer.buffer(data.getData()));

		} else {

			String file = routes.get(uri);
			Path p = webFolderURI.resolve(file);

			try {
				ctx.response().putHeader("content-type", TIKA_INSTANCE.detect(file))
						.write(Buffer.buffer(IOUtils.toByteArray(p.toUri())));
			} catch (IOException e1) {
				Exceptions.throwRuntime(e1);
			}
		}
	}

	public static List<String> getUriParams(String uri) {
		return routeParams.get(uri);
	}

	public static String getUri(Functionality f) {
		return functionalityToRoutesMappings.get(f.getId());
	}

	private static void startWatchService() {
		try {
			watchService = FileSystems.getDefault().newWatchService();
			fileWatcherPool = Executors.newFixedThreadPool(1);
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
	}

	static {
	}
}
