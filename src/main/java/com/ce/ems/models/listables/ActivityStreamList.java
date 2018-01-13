package com.ce.ems.models.listables;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.spec.ActivitySpec;
import com.ce.ems.base.core.Listable;
import com.ce.ems.entites.ActivityStreamEntity;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;

public class ActivityStreamList extends Listable<ActivitySpec>{

	@Override
	public IndexedNameType type() {
		return IndexedNameType.ACTIVITY_STREAM;
	}

	@Override
	public boolean authenticate(Long userId, Map<String, Object> filters) {
		return RoleModel.isAccessAllowed(BaseUserModel.getRole(userId), Functionality.MANAGE_ACTIVITY_STREAM);
	}

	@Override
	public Class<ActivityStreamEntity> entityType() {
		return ActivityStreamEntity.class;
	}
	
	@Override
	public boolean searchable() {
		return false;
	}

	@Override
	public Map<Long, ActivitySpec> getAll(List<String> keys) {
		
		Map<Long, ActivitySpec> result = new FluentHashMap<>();
		
		List<Long> longKeys = new ArrayList<>(keys.size());
		
		//Convert to Long keys
		keys.forEach(k -> {
			longKeys.add(Long.parseLong(k));
		});
		
		ofy().load().type(ActivityStreamEntity.class).ids(longKeys).forEach((k,v) -> {
			result.put(k, EntityHelper.toObjectModel(v));
		});

		return result;
	}
	
	@Override
	public SearchableUISpec searchableUiSpec() {
		return null;
	}

}
