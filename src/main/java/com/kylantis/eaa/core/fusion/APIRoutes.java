package com.kylantis.eaa.core.fusion;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.ce.ems.base.classes.ObjectWrapper;
import com.ce.ems.base.core.AppUtils;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ClassIdentityType;
import com.ce.ems.base.core.ClasspathScanner;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.ResourceException;
import com.ce.ems.utils.Utils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.kylantis.eaa.core.keys.AppConfigKey;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

@BlockerTodo("Create optionalRequestParams setting, Validate request params in fusion. Do in main ctx handler")
public class APIRoutes {
	

	public static Pattern endpointClassUriPattern = Pattern.compile("\\A\\Q/\\E[a-zA-Z]+[-]*[a-zA-Z]+(\\Q/\\E[a-zA-Z]+[-]*[a-zA-Z]+)*\\z");

	public static Pattern endpointMethodUriPattern = Pattern.compile("\\A\\Q/\\E[a-zA-Z-]+[-]*[a-zA-Z]+\\z");


	private static Multimap<String, RouteHandler> routeKeys = LinkedHashMultimap.create();
	private static Multimap<Route, RouteHandler> routes = LinkedHashMultimap.create();

	protected static Map<Object, Integer> routesMappings = new HashMap<>();

	protected static Map<Integer, List<String>> functionalityToRoutesMappings = new HashMap<>();
	
	protected static final String USER_ID_PARAM_NAME = "x_uid";
	public static final String BASE_PATH = "/api";

	/**
	 * This discovers fusion services by scanning the classpath
	 */
	private static void scanServices(Consumer<FusionServiceContext> consumer) {

		Logger.debug("Scanning for services");

		String ext = AppUtils.getConfig(AppConfigKey.CLASSES_SERVICES_EXT);

		for (Class<? extends BaseService> service : new ClasspathScanner<>(ext, BaseService.class,
				ClassIdentityType.SUPER_CLASS).scanClasses()) {

			BaseService _serviceInstance = null;
			try {
				_serviceInstance = service.newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				Exceptions.throwRuntime(ex);
			}
			final BaseService serviceInstance = _serviceInstance;

			// Silently ignore
			if (!service.isAnnotationPresent(EndpointClass.class)) {
				continue;
			}

			EndpointClass classAnnotation = service.getAnnotation(EndpointClass.class);

			if (!endpointClassUriPattern.matcher(classAnnotation.uri()).matches()) {
				throw new RuntimeException("Improper URI format for " + service.getName());
			}

			Method[] methods = service.getDeclaredMethods();

			for (int i = 0; i < methods.length; i++) {

				Method method = methods[i];

				if (!method.isAnnotationPresent(EndpointMethod.class)) {
					// Silently ignore
					continue;
				}

				EndpointMethod methodAnnotation = method.getAnnotation(EndpointMethod.class);

				if (!endpointMethodUriPattern.matcher(methodAnnotation.uri()).matches()) {
					throw new RuntimeException("Improper URI format for " + service.getName() + "/" + method.getName());
				}
				
				consumer.accept(new FusionServiceContext(classAnnotation, methodAnnotation, serviceInstance, method,
						i == methods.length - 1));
			}
		}
	}

	private static void saveServiceClient(String buffer, BaseService service) {

		String name = service.getClass().getSimpleName();

		String path = "/tmp/ems/fusion-service-clients/" + name.replace("Service", "").toLowerCase() + ".js";

		File clientStubFile = new File(path);
		
		clientStubFile.mkdirs();
		try {
			if (clientStubFile.exists()) {
				clientStubFile.delete();
			}

			clientStubFile.createNewFile();

			Logger.trace("Saving service client for " + name + " to " + clientStubFile.getAbsolutePath());

			Utils.saveString(buffer, Files.newOutputStream(clientStubFile.toPath()));

		} catch (IOException e) {
			Exceptions.throwRuntime(e);
		}
	}

	/**
	 * This returns a set of request handlers available for this app by scanning for
	 * fusion services. It also includes an auth handler to authenticate requests.
	 * <br>
	 * In the course of execution, this methods populates
	 * {@code APIRoutes.routesMappings}, and also generates javascript clients for
	 * all fusion endpoints.
	 * 
	 */
	public static void scanRoutes() {

		Logger.debug("Scanning for API routes");

		routes = ArrayListMultimap.create();

		// First, add auth handler

		registerRoute(new Route(), new RouteHandler(Handlers::APIAuthHandler, false));

		// Then, add fusion services found in classpath

		ObjectWrapper<StringBuilder> serviceCientBuffer = new ObjectWrapper<StringBuilder>().set(new StringBuilder());

		// This is used to avoid duplicate service methods, since they all exists in a
		// global client context
		Map<String, String> methodNames = new HashMap<>();

		scanServices(context -> {

			String className = context.getServiceInstance().getClass().getSimpleName();
			String methodName = context.getMethod().getName();

			if (methodNames.containsKey(methodName)) {

				String msg = "Method name: " + methodName + "(..) in " + className + " already exists in "
						+ methodNames.get(methodName);
				throw new ResourceException(ResourceException.RESOURCE_ALREADY_EXISTS, msg);
			}

			methodNames.put(methodName, className);

			Functionality functionality = context.getEndpointMethod().functionality();
			
			String uri = context.getEndpointClass().uri() + context.getEndpointMethod().uri();
			HttpMethod httpMethod = context.getEndpointMethod().method();

			Route route = new Route(uri, httpMethod);

			Logger.trace("Mapping route: " + uri + " (" + httpMethod + ") to functionality: "
					+ functionality);

			routesMappings.put(route.toString(), context.getEndpointMethod().functionality().getId());

			
			if(!functionalityToRoutesMappings.containsKey(functionality.getId())) {
				functionalityToRoutesMappings.put(functionality.getId(), new ArrayList<>());
			}
			
			functionalityToRoutesMappings.get(functionality.getId()).add(uri);			
			
			if (context.getEndpointMethod().createXhrClient()) {
				// Generate XHR clients
				serviceCientBuffer.get().append(RPCFactory.generateXHRClient(context.getEndpointClass(),
						context.getMethod(), context.getEndpointMethod(), route));
			}

			// Add Handler

			RouteHandler handler = new RouteHandler(((ctx) -> {

				// Verify Scheme
				if (context.getEndpointMethod().requireSSL()) {
					if (!ctx.request().isSSL()) {
						ctx.response().setStatusCode(HttpServletResponse.SC_NOT_ACCEPTABLE).end();
					}
				}

				try {
					context.getMethod().invoke(context.getServiceInstance(), ctx);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Exceptions.throwRuntime(e);
				}

			}), context.getEndpointMethod().isBlocking());

			registerRoute(route, handler);

			
			
			// Generate Javascript client

			if (context.isClassEnd()) {
				saveServiceClient(serviceCientBuffer.get().toString(), context.getServiceInstance());
				serviceCientBuffer.set(new StringBuilder());
			}

		});
	}

	protected static Router get() {

		final Router router = Router.router(WebServer.vertX);

		getRoutes().forEach((route, handler) -> {
			addRoute(router, route, handler);
		});

		router.route().handler(ctx -> {
			if (!ctx.response().ended()) {

				if (ctx.response().bytesWritten() == 0) {
					ctx.response().write(com.kylantis.eaa.core.fusion.Utils.toResponse(ctx.response().getStatusCode()));
				}
				ctx.response().end();
			}
		});
		return router;
	}

	private static final void addRoute(Router router, Route route, RouteHandler handler) {

		io.vertx.ext.web.Route r =

				// Match all paths and methods
				route.getMethod() == null && route.getUri() == null ? router.route() :
				// Match by method only
						route.getMethod() != null && route.getUri() == null
								? router.routeWithRegex(route.getMethod(), "/*")
								:
								// Match by path only
								route.getUri() != null && route.getMethod() == null ? router.route(route.getUri()) :
								// Match by method and path
										router.route(route.getMethod(), route.getUri());

		if (handler.isBlocking()) {
			r.blockingHandler(handler.getHandler());
		} else {
			r.handler(handler.getHandler());
		}

	}

	public static void clear() {
		routes = null;
	}

	public static Collection<RouteHandler> getRouteHandler(Route route) {
		return routeKeys.get(route.toString());
	}

	private static final void registerRoute(Route route, RouteHandler handler) {

		if (!routeKeys.containsKey(route.toString())) {
			routeKeys.put(route.toString(), handler);
			routes.put(route, handler);
		} else {
			throw new ResourceException(ResourceException.RESOURCE_ALREADY_EXISTS,
					"Route: " + route + " already exists");
		}
	}

	/**
	 * Note: All routes paths returned are not prefixed with
	 * {@code APIRoutes.BASE_PATH}. All callers should consolidate this when setting
	 * up their respective containers. <br>
	 * Also, note that exceptions will possibly be thrown during the execution of
	 * these handlers, and callers should create proper exception catching mechanism
	 * on their containers. <br>
	 * Callers should create mechanisms to properly end the response after all
	 * handlers, have finished execution
	 */
	public static Multimap<Route, RouteHandler> getRoutes() {
		if (routes == null) {
			scanRoutes();
		}
		return routes;
	}
	
	public static List<String> getUri(Functionality functionality){
		return functionalityToRoutesMappings.get(functionality.getId());
	}

	static {
			
	}
}
