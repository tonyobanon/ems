package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.List;

import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.entites.MetricEntity;
import com.googlecode.objectify.Key;

public class MetricsModel extends BaseModel {

	public static final String TIMESERIES_SUFFIX = "_TIMESERIES";
	public static final String CURRENT_SUFFIX = "_CURRENT";

	@Override
	public String path() {
		return "core/metrics";
	}
	
	@Override
	public void install(InstallOptions options) {

	}

	@Override
	public void start() {
		
		// Start a socket server, that gets written to when metric is updated
	}

	private static void doIncrement(String key, String parentKey) {
		put(key, parentKey, get(key) + 1);
	}

	public static void increment(String key) {

			Calendar currentTime = Calendar.getInstance();

			int yy = currentTime.get(Calendar.YEAR);
			int mm = currentTime.get(Calendar.MONTH);
			int dd = currentTime.get(Calendar.DAY_OF_MONTH);
			int hh = currentTime.get(Calendar.HOUR_OF_DAY);
			int mn = currentTime.get(Calendar.MINUTE);
			int ss = currentTime.get(Calendar.SECOND);

			doIncrement(key + "_" + yy, key);
			doIncrement(key + "_" + yy + "_" + mm, key);
			doIncrement(key + "_" + yy + "_" + mm + "_" + dd, key);
			doIncrement(key + "_" + yy + "_" + mm + "_" + dd + "_" + hh, key);
			doIncrement(key + "_" + yy + "_" + mm + "_" + dd + "_" + hh + "_" + mn, key);
			doIncrement(key + "_" + yy + "_" + mm + "_" + dd + "_" + hh + "_" + mn + "_" + ss, key);

	}

	private static Integer get(String key) {
		MetricEntity e = ((MetricEntity) ofy().load().type(MetricEntity.class).id(key).now());
		return e != null ? e.getValue() : 0;
	}

	private static void put(String key, String parentKey, Integer value) {
		ofy().save().entity(new MetricEntity().setKey(key).setParentKey(parentKey).setValue(value)).now();
	}

	public static void clear(String parentKey) {

		List<String> keys = new FluentArrayList<>();

		ofy().load().type(MetricEntity.class).filter("parentKey = ", parentKey).forEach((e) -> {
			keys.add(e.getKey());
		});

		delete(keys.toArray(new String[keys.size()]));
	}

	private static void delete(String... keys) {
		List<Key<MetricEntity>> keysO = new FluentArrayList<>();

		for (String k : keys) {
			keysO.add(Key.create(MetricEntity.class, k));
		}

		ofy().delete().keys(keysO).now();
	}

}
