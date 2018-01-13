package com.kylantis.eaa.core.fusion.services;

import com.ce.ems.base.classes.ActivitityStreamTimeline;
import com.ce.ems.models.ActivityStreamModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/activity-stream")
public class ActivityStreamService extends BaseService {
  
	@EndpointMethod(uri = "/set-timeline", requestParams = { "timeline" }, method = HttpMethod.POST,
			functionality = Functionality.MANAGE_ACTIVITY_STREAM)
	public void setActivityStreamTimeline(RoutingContext ctx) {
		ActivitityStreamTimeline timeline = ActivitityStreamTimeline.from(Integer.parseInt(ctx.request().getParam("timeline")));
		ActivityStreamModel.setActivityTimeline(timeline);
	}
	        
	@EndpointMethod(uri = "/get-timeline",
			functionality = Functionality.MANAGE_ACTIVITY_STREAM)
	public void getActivityStreamTimeline(RoutingContext ctx) {
		ActivitityStreamTimeline timeline = ActivityStreamModel.getActivityTimeline();
		ctx.response().setChunked(true).write(new JsonObject().put("timeline", timeline.getValue()).encode()); 
	}  
	         
	@EndpointMethod(uri = "/is-enabled", 
			functionality = Functionality.MANAGE_ACTIVITY_STREAM)
	public void isActivityStreamEnabled(RoutingContext ctx) {
		Boolean isEnabled = ActivityStreamModel.isEnabled();
		ctx.response().setChunked(true).write(new JsonObject().put("isEnabled", isEnabled).encode());
	}    
	 
	@EndpointMethod(uri = "/disable", method = HttpMethod.POST, 
			functionality = Functionality.MANAGE_ACTIVITY_STREAM)
	public void disableActivityStream(RoutingContext ctx) {
		ActivityStreamModel.disable(); 
	}
	
	@EndpointMethod(uri = "/enable", method = HttpMethod.POST,
			functionality = Functionality.MANAGE_ACTIVITY_STREAM)
	public void enableActivityStream(RoutingContext ctx) {
		ActivityStreamModel.enable();
	}
	
}
