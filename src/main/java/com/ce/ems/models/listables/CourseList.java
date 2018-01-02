package com.ce.ems.models.listables;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.classes.spec.BaseCourseSpec;
import com.ce.ems.base.core.Listable;
import com.ce.ems.entites.directory.CourseEntity;
import com.ce.ems.models.BaseUserModel;
import com.ce.ems.models.RoleModel;
import com.kylantis.eaa.core.users.Functionality;

public class CourseList extends Listable<BaseCourseSpec>{

	@Override
	public IndexedNameType type() {
		return IndexedNameType.COURSE;
	}

	@Override
	public boolean authenticate(Long userId, Map<String, Object> filters) {
		return RoleModel.isAccessAllowed(BaseUserModel.getRole(userId), Functionality.SEARCH_COURSES);
	}

	@Override
	public Class<?> entityClass() {
		return CourseEntity.class;
	}
	
	@Override
	public boolean searchable() {
		return true;
	}

	@Override
	public Map<String, BaseCourseSpec> getAll(List<String> keys) {
		
		Map<String, BaseCourseSpec> result = new FluentHashMap<>();
		
		ofy().load().type(CourseEntity.class).ids(keys).forEach((k,v) -> {
			BaseCourseSpec o = new BaseCourseSpec()
					.setCode(v.getCode())
					.setName(v.getName())
					.setPoint(v.getPoint());
			
			result.put(k, o);
		});

		return result;
	}
	
	@Override
	public SearchableUISpec searchableUiSpec() {
		return new SearchableUISpec().setName("Courses").setListingPageUrl("/course-search");
	}

}
