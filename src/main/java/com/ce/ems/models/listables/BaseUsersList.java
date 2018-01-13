package com.ce.ems.models.listables;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameSpec;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.spec.BaseUserSpec;
import com.ce.ems.base.core.Listable;
import com.ce.ems.entites.BaseUserEntity;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;

public class BaseUsersList extends Listable<BaseUserSpec>{

	@Override
	public IndexedNameType type() {
		return IndexedNameType.USER;
	}

	@Override
	public boolean authenticate(Long userId, Map<String, Object> filters) {
		return RoleModel.isAccessAllowed(BaseUserModel.getRole(userId), Functionality.GET_USER_PROFILE);
	}

	@Override
	public Class<BaseUserEntity> entityType() {
		return BaseUserEntity.class;
	}
	
	@Override
	public boolean searchable() {
		return true;
	}

	@Override
	public Map<String, BaseUserSpec> getAll(List<String> keys) {
		
		Map<String, BaseUserSpec> result = new FluentHashMap<>();
		
		keys.forEach(k -> {
			Long userId = Long.parseLong(k);
			
			BaseUserEntity e = ofy().load().type(BaseUserEntity.class).id(userId).safe();
			
			IndexedNameSpec nameSpec = new IndexedNameSpec()
						.setKey(userId.toString())
						.setX(e.getFirstName())
						.setY(e.getLastName())
						.setZ(e.getMiddleName());
			
			BaseUserSpec spec = new BaseUserSpec()
					.setId(userId)
					.setRole(e.getRole())
					.setNameSpec(nameSpec)
					.setDateCreated(e.getDateCreated())
					.setDateUpdated(e.getDateUpdated());	
			
			result.put(k, spec);
		});
		
		return result;
	}
	
	@Override
	public SearchableUISpec searchableUiSpec() {
		return new SearchableUISpec().setName("users").setListingPageUrl("/users-search");
	}

}
