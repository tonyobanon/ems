package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.ce.ems.base.api.event_streams.Sentence;
import com.ce.ems.base.classes.ActivitityStreamTimeline;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.ActivityStreamEntity;
import com.ce.ems.utils.BackendObjectMarshaller;
import com.ce.ems.utils.Dates;
import com.kylantis.eaa.core.keys.ConfigKeys;


@BlockerTodo("Create sentence serializers and deserializer, since they will be stored as Json very soon")
@Todo("Maintaining activity streams are extremely expensive, and as such, find a workaround")
//Create a configuration, that allows admins to set the default page size when retrieving activity streams on the client
public class ActivityStreamModel extends BaseModel {

	@Override
	public String path() {
		return "core/activity_stream";
	}

	@Override 
	public void preInstall() {
		enable();
		ConfigModel.put(ConfigKeys.DEFAULT_ACTIVITY_STREAM_TIMELINE, ActivitityStreamTimeline.DAILY.getValue());
	} 

	public static void newActivity(Sentence activity) {
		newActivity(null, activity);
	} 

	public static ActivitityStreamTimeline getActivityTimeline() {
		return ActivitityStreamTimeline
				.from(Integer.parseInt(ConfigModel.get(ConfigKeys.DEFAULT_ACTIVITY_STREAM_TIMELINE)));
	}
 
	public static void setActivityTimeline(ActivitityStreamTimeline timeline) {
		ConfigModel.put(ConfigKeys.DEFAULT_ACTIVITY_STREAM_TIMELINE, timeline.getValue());
	}
	 
	public static void disable() {
		ConfigModel.put(ConfigKeys.ACTIVITY_STREAM_ENABLED, BackendObjectMarshaller.marshal(false));
	}
	
	public static void enable() {
		ConfigModel.put(ConfigKeys.ACTIVITY_STREAM_ENABLED, BackendObjectMarshaller.marshal(true));
	}
	
	public static Boolean isEnabled() {
		return BackendObjectMarshaller.unmarshalBool(ConfigModel.get(ConfigKeys.ACTIVITY_STREAM_ENABLED));
	}

	public static void newActivity(String image, Sentence activity) {

		if(!isEnabled()) {
			Logger.debug("Could not save activity beacuse activity streams are currently disabled");
			return;
		}
		
		 //Try to stringify activity
		 //activity.toString();

		// then, save
		 //GsonFactory.newInstance().toJson(activity)
		ofy().save().entity(new ActivityStreamEntity().setActivity(activity.toString())
				.setImage(image).setDate(Dates.now())).now();
	}
}
