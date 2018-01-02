package com.kylantis.eaa.core.fusion.services;

import java.util.Map;

import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.ApplicationModel;
import com.ce.ems.utils.ObjectUtils;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/user-applications")
public class ApplicationService extends BaseService {

	@EndpointMethod(uri = "/create-application", bodyParams = { "roleName"}, method = HttpMethod.PUT,
			functionality = Functionality.CREATE_APPLICATION)
	public void createApplication(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String roleName = body.getString("roleName");
		
		Long applicationId = ApplicationModel.newApplication(roleName);
		
		ctx.response().write(applicationId.toString()).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/update-application", bodyParams = { "applicationId", "values"}, method = HttpMethod.POST,
			functionality = Functionality.UPDATE_APPLICATION)
	public void updateApplication(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long applicationId = body.getLong("applicationId");
		Map<String, Object> values = body.getJsonObject("values").getMap();
		
		ApplicationModel.updateApplication(applicationId, ObjectUtils.toStringMap(values));
	}
	
	@EndpointMethod(uri = "/submit-application", bodyParams = { "applicationId"}, method = HttpMethod.PUT,
			functionality = Functionality.SUBMIT_APPLICATION)
	public void submitApplication(RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();

		Long applicationId = body.getLong("applicationId");
		
		ApplicationModel.submitApplication(applicationId);
	}
	
	@EndpointMethod(uri = "/get-pdf-questionnaire",requestParams = {"roleName"},
			functionality = Functionality.DOWNLOAD_QUESTIONNAIRE)
	public void getPDFQuestionnaire(RoutingContext ctx) {

		String roleName = ctx.request().getParam("roleName");
		String blobId = ApplicationModel.getPDFQuestionnaire(roleName);
		
		ctx.response().write(blobId).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-field-values", requestParams = { "applicationId" },
			functionality = Functionality.UPDATE_APPLICATION)
	public void getApplicationFieldValues(RoutingContext ctx) {
		
		Long applicationId = Long.parseLong(ctx.request().getParam("applicationId"));
		
		Map<String, String> result = ApplicationModel.getFieldValues(applicationId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/accept-application", bodyParams = { "applicationId" }, method = HttpMethod.POST,
			functionality = Functionality.REVIEW_APPLICATION)
	public void acceptApplication(RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());
		
		Long applicationId = body.getLong("applicationId");
		
		Long id = ApplicationModel.acceptApplication(principal, applicationId);
		ctx.response().write(id.toString());
	}
	
	@EndpointMethod(uri = "/decline-application", bodyParams = { "applicationId", "reason" }, method = HttpMethod.PUT,
			functionality = Functionality.REVIEW_APPLICATION)
	public void declineApplication(RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();

		Long applicationId = body.getLong("applicationId");		
		Long principal = FusionHelper.getUserId(ctx.request());
		String reason = body.getString("reason");
		
		ApplicationModel.declineApplication(applicationId, principal, reason);
	}

}
