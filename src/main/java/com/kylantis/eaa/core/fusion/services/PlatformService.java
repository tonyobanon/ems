package com.kylantis.eaa.core.fusion.services;

import javax.servlet.http.HttpServletResponse;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.PlatformModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.WebRoutes;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/platform/tools")
public class PlatformService extends BaseService {

	@EndpointMethod(uri = "/setup", bodyParams = {
			"payload" }, method = HttpMethod.POST, isBlocking = true, functionality = Functionality.PLATFORM_INSTALLATION)
	public void doSetup(RoutingContext context) {

		try {

			if (PlatformModel.isInstalled()) {
				return;
			}

			JsonObject body = context.getBodyAsJson();

			InstallOptions spec = GsonFactory.newInstance().fromJson(body.getJsonObject("payload").encode(),
					InstallOptions.class);

			// Perform installation
			PlatformModel.doInstall(spec);

			// Go to console
			context.response().putHeader("X-Location", WebRoutes.DEFAULT_CONSOLE_URI)
					.setStatusCode(HttpServletResponse.SC_FOUND);

		} catch (Exception e) {
			Exceptions.throwRuntime(e);
		}
	}

}
