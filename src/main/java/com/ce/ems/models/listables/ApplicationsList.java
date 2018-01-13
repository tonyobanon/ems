package com.ce.ems.models.listables;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.ApplicationStatus;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameSpec;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.spec.BaseApplicationSpec;
import com.ce.ems.base.core.Listable;
import com.ce.ems.entites.ApplicationEntity;
import com.ce.ems.models.ApplicationModel;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;

public class ApplicationsList extends Listable<BaseApplicationSpec> {

	@Override
	public IndexedNameType type() {
		return IndexedNameType.APPLICATION;
	}

	@Override
	public Map<String, BaseApplicationSpec> getAll(List<String> keys) {

		Map<String, BaseApplicationSpec> result = new FluentHashMap<>();
		
		keys.forEach(k -> {
			
			Long applicationId = Long.parseLong(k);

			String role = ApplicationModel.getApplicationRole(applicationId);
			RoleRealm realm = RoleModel.getRealm(role);
			
			IndexedNameSpec nameSpec = ApplicationModel.getNameSpec(applicationId, realm);
			
			ApplicationEntity e = ofy().load().type(ApplicationEntity.class).id(applicationId).safe();
			
			BaseApplicationSpec spec = new BaseApplicationSpec()
					.setId(applicationId)
					.setRole(e.getRole())
					.setStatus(ApplicationStatus.from(e.getStatus()))
					.setNameSpec(nameSpec)
					.setDateCreated(e.getDateCreated())
					.setDateUpdated(e.getDateUpdated());
			
			 result.put(k, spec);
			
		});
		
		return result;
	}

	@Override
	public boolean authenticate(Long userId, Map<String, Object> filters) {
		return RoleModel.isAccessAllowed(BaseUserModel.getRole(userId), Functionality.VIEW_APPLICATIONS);
	}

	@Override
	public Class<ApplicationEntity> entityType() {
		return ApplicationEntity.class;
	}
	
	@Override
	public boolean searchable() {
		return true;
	}

	@Override
	public SearchableUISpec searchableUiSpec() {
		return new SearchableUISpec().setName("applications").setListingPageUrl("/applications-search");
	}
}
