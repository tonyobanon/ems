package com.kylantis.eaa.core.fusion;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;

import com.ce.ems.base.core.AppUtils;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Todo;
import com.ce.ems.base.core.UsesMock;
import com.ce.ems.models.PlatformModel;
import com.ce.ems.utils.Utils;
import com.google.gson.Gson;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@UsesMock
public class WebRoutes {

	//@DEV
	protected static Path webFolderURI = AppUtils.getPath("web/public_html");

	public static String DEFAULT_INDEX_URI = "/";
	public static String DEFAULT_SETUP_URI = "/setup/one";

	public static String DEFAULT_CONSOLE_URI = "/dashboard";

	public static String DEFAULT_LOGIN_URI = "/user-login";

	private static Pattern webpagePattern = Pattern
			.compile("\\A[a-zA-Z0-9]+([_]*[-]*[a-zA-Z0-9]+)*\\Q.\\E(html|htm|xhtml){1}\\z");
	// Let's be lenient a little with the fileName
	// .compile("\\A[a-zA-Z]+(\\Q_\\E[a-zA-Z]+)*\\Q.\\E[a-zA-Z]+\\z");

	public static Tika TIKA_INSTANCE = new Tika();

	static Map<Object, WebRouteSpec> routesMappings = new HashMap<>();
	private static Map<String, String> routes = new HashMap<>();

	public static final String USER_ID_PARAM_NAME = "x_uid";

	@Todo("Create util for wallking file tree")
	private static void scanRoutes() throws IOException {

		//Logger.info("Scanning for Web routes");

		// Get Route mappings

		JsonObject o = new JsonObject(
				Utils.getString(Files.newInputStream(webFolderURI.resolve("route_mapping.json"))));

		Gson gson = GsonFactory.newInstance();

		o.forEach(e -> {
			routesMappings.put(e.getKey(), gson.fromJson(e.getValue().toString(), WebRouteSpec.class));
		});

		// Walk tree inorder to expose all files in the web folder

		Files.walkFileTree(webFolderURI, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				Path path = webFolderURI.relativize(file);

				String uri = path.toString().replaceAll("\\\\", "/");

				if (webpagePattern.matcher(uri).matches()) {
					uri = toCanonicalURI(uri);
				}

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

	private static boolean handleAuth(String uri, RoutingContext ctx, Integer functionalityId) {

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

	@BlockerTodo("Stop parsing content type with TIKA on each request")
	public static final void get(RoutingContext ctx) {

		String uri = ctx.request().path();

		if(uri.equals("/")) {
			if(!PlatformModel.isInstalled()) {
				uri = DEFAULT_SETUP_URI;
			}
		}
		
		if (!routes.containsKey(uri)) {
			ctx.response().setStatusCode(HttpServletResponse.SC_FOUND).putHeader("Location", "/404").end();
			return;
		}

		// Improve security
		ctx.response()
				// allow proxies to cache the data
				// .putHeader("Cache-Control", "public,
				// max-age=31536000")

				// @DEV
				.putHeader("Cache-Control", "no-cache")
				
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

		WebRouteSpec spec = routesMappings.get(uri);

		if (spec != null) {
			for (Integer f : spec.getMin()) {
				if (!handleAuth(uri, ctx, f)) {
					ctx.response().setStatusCode(HttpServletResponse.SC_FOUND)
							.putHeader("Location", DEFAULT_LOGIN_URI + "?returnUrl=" + uri).end();
					return;
				}
			}
		}

		if ((uri.equals(DEFAULT_LOGIN_URI)) && handleAuth(DEFAULT_LOGIN_URI, ctx, 0)) {
			String returnUrl = ctx.request().getParam("returnUrl");
			if (returnUrl == null) {
				returnUrl = DEFAULT_INDEX_URI;
			}
			ctx.response().setStatusCode(HttpServletResponse.SC_FOUND).putHeader("Location", returnUrl).end();
			return;
		}

		ctx.request().params().forEach(e -> {
			ctx.addCookie(new CookieImpl(e.getKey(), e.getValue()).setPath(ctx.request().path()));
		});

		String file = routes.get(uri);
		Path p = webFolderURI.resolve(file);

		try {
			ctx.response().putHeader("content-type", TIKA_INSTANCE.detect(file))
					.write(Buffer.buffer(IOUtils.toByteArray(p.toUri())));
		} catch (IOException e1) {
			Exceptions.throwRuntime(e1);
		}
	}

	static {
		try {
			scanRoutes();
		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
	}
}
