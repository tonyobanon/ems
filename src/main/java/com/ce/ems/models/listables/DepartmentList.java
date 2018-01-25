package com.ce.ems.models.listables;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.ListingFilter;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.core.Listable;
import com.ce.ems.entites.directory.DepartmentEntity;
import com.ce.ems.entites.directory.FacultyEntity;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;

public class DepartmentList extends Listable<DepartmentSpec>{

	@Override
	public IndexedNameType type() {
		return IndexedNameType.DEPARTMENT;
	}

	@Override
	public boolean authenticate(Long userId, List<ListingFilter> listingFilters) {
		return RoleModel.isAccessAllowed(BaseUserModel.getRole(userId), Functionality.VIEW_DEPARTMENT_PROFILES);
	}

	@Override
	public Class<DepartmentEntity> entityType() {
		return DepartmentEntity.class;
	}
	
	@Override
	public boolean searchable() {
		return false;
	}

	@Override
	public Map<Long, DepartmentSpec> getAll(List<String> keys) {
		
		Map<Long, DepartmentSpec> result = new FluentHashMap<>();
		
		List<Long> longKeys = new ArrayList<>(keys.size());
		
		//Convert to Long keys
		keys.forEach(k -> {
			longKeys.add(Long.parseLong(k));
		});
		
		ofy().load().type(DepartmentEntity.class).ids(longKeys).forEach((k,v) -> {
			
			String facultyName = ofy().load().type(FacultyEntity.class).id(v.getFaculty()).safe().getName();
				
			DepartmentSpec o = EntityHelper.toObjectModel(v)
					.setFacultyName(facultyName);
			
			if(v.getHeadOfDepartment() != null) {
				o.setHeadOfDepartmentName(BaseUserModel.getPersonName(v.getHeadOfDepartment(), true).toString());
			}
			
			result.put(k, o);
		});

		return result;
	}
	
	@Override
	public SearchableUISpec searchableUiSpec() {
		return null;
	}

}
