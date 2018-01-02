package com.ce.ems.requests;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ce.ems.base.classes.FluentArrayList;
import com.kylantis.eaa.core.fusion.APIRoutes;
import com.kylantis.eaa.core.fusion.RPCFactory;
import com.kylantis.eaa.core.fusion.Route;
import com.kylantis.eaa.core.fusion.RouteHandler;
import com.kylantis.eaa.core.gaefusion.ErrorHelper;
import com.kylantis.eaa.core.gaefusion.GAERouteContext;

import io.vertx.core.http.HttpMethod;

@WebServlet(urlPatterns = APIRoutes.BASE_PATH + "/*", loadOnStartup = 10)
public class BaseApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Mock RoutingContext from HttpServletRequest

		GAERouteContext ctx = new GAERouteContext(req);

		String path = ctx.request().path().replace(APIRoutes.BASE_PATH, "");

		HttpMethod method = ctx.request().method();

		// Find all matching handlers

		List<RouteHandler> handlers = new FluentArrayList<>();

		// Matching all paths and methods
		handlers.addAll(APIRoutes.getRouteHandler(new Route()));

		// Matching only current method
		handlers.addAll(APIRoutes.getRouteHandler(new Route().setMethod(method)));

		// Matching only current path
		handlers.addAll(APIRoutes.getRouteHandler(new Route().setUri(path)));

		// Matching current path and method
		handlers.addAll(APIRoutes.getRouteHandler(new Route().setMethod(method).setUri(path)));

		
		
		// Recursively call each handler

		for (RouteHandler handler : handlers) {

			// Create exception catching mechanism

			try {
				handler.getHandler().handle(ctx);
			} catch (Exception e) {
				e.printStackTrace();
				ctx.response().end(com.kylantis.eaa.core.fusion.Utils.toResponse(ErrorHelper.getError(e)));
				break;
			}

			// Check if response is ended

			if (ctx.response().ended()) {
				break;
			}
		}

		if (!ctx.response().ended()) {
			if (ctx.response().bytesWritten() == 0) {
				ctx.response().write(com.kylantis.eaa.core.fusion.Utils.toResponse(ctx.response().getStatusCode()));
			}
			ctx.response().end();
		}

		ctx.response().transform(resp);
		resp.flushBuffer();
	}

	static {
		RPCFactory.setPrependDomainVariableToUrl(false);
	}
}
