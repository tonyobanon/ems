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
import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.ce.ems.base.core.Listable;
import com.ce.ems.entites.directory.AcademicSemesterEntity;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;

public class AcademicSemesterList extends Listable<AcademicSemesterSpec>{

	@Override
	public IndexedNameType type() {
		return IndexedNameType.ACADEMIC_SEMESTER;
	}

	@Override
	public boolean authenticate(Long userId, List<ListingFilter> listingFilters) {
		return RoleModel.isAccessAllowed(BaseUserModel.getRole(userId), Functionality.VIEW_ALL_SEMESTER_COURSES);
	}

	@Override
	public Class<AcademicSemesterEntity> entityType() {
		return AcademicSemesterEntity.class;
	}
	
	@Override
	public boolean searchable() {
		return false;
	}

	@Override
	public Map<Long, AcademicSemesterSpec> getAll(List<String> keys) {
		
		Map<Long, AcademicSemesterSpec> result = new FluentHashMap<>();
		
		List<Long> longKeys = new ArrayList<>(keys.size());
		
		//Convert to Long keys
		keys.forEach(k -> {
			longKeys.add(Long.parseLong(k));
		});
		
		ofy().load().type(AcademicSemesterEntity.class).ids(longKeys).forEach((k,v) -> {
			result.put(k, EntityHelper.toObjectModel(v));
		});

		return result;
	}
	
	@Override
	public SearchableUISpec searchableUiSpec() {
		return null;
	}

}
