package com.kylantis.eaa.core.fusion;

import javax.servlet.http.HttpServletResponse;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

public class Handlers {

	public static void APIAuthHandler(RoutingContext ctx) {

		// set json response type
		ctx.response().putHeader("Content-Type", "application/json");

		// do not allow proxies to cache the data
		ctx.response().putHeader("Cache-Control", "no-store, no-cache");

		
		String uri = ctx.request().path().replace(APIRoutes.BASE_PATH, "");
		HttpMethod method = ctx.request().method();
		
		Route route = new Route(uri, method);
		
		Integer functionalityId = APIRoutes.routesMappings.get(route.toString());

		if (functionalityId == null) {
			functionalityId = -1;
		}

		if (functionalityId < 0) {
			ctx.next();
			return;
		}

		boolean hasAccess = false;

		String sessionToken = ctx.request().getHeader(FusionHelper.sessionTokenName());
		Long userId = FusionHelper.getUserIdFromToken(sessionToken);

		if (userId != null) {
			hasAccess = true;
		}

		if (functionalityId > 0 && hasAccess == true) {
			hasAccess = false;
			for (String roleName : FusionHelper.getRoles(userId)) {

				// Check that this role has the right to access this Uri
				if(FusionHelper.isAccessAllowed(roleName, functionalityId)){
					hasAccess = true;
					break;
				}
			}
		}

		if (hasAccess) {
			FusionHelper.setUserId(ctx.request(), userId);
			ctx.next();
		} else {
			
			ctx.response()
				.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED)
				.write(com.kylantis.eaa.core.fusion.Utils.toResponse(HttpServletResponse.SC_UNAUTHORIZED))
				.end();
		}

	}

}
