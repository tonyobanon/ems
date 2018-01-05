package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.EntityUtils;
import com.ce.ems.base.classes.QueryFilter;
import com.ce.ems.base.classes.spec.ActivitySpec;
import com.ce.ems.entites.ActivityEntity;

public class ActivityModel extends BaseModel {

	@Override
	public String path() {
		return "core/activity_stream";
	}

	public static void newActivity(ActivitySpec spec) {
		ofy().save().entity(EntityHelper.fromObjectModel(spec));
	}

	public static void newActivity(String text) {
		ofy().save().entity(new ActivityEntity().setImage(null).setText(text).setDate(new Date()));
	}

	public static List<ActivitySpec> list(Date date) {

		List<ActivitySpec> result = new ArrayList<>();
		EntityUtils.query(ActivityEntity.class, QueryFilter.get("date <= ", date)).forEach(e -> {
			result.add(EntityHelper.toObjectModel(e));
		});
		return result;
	}
}
