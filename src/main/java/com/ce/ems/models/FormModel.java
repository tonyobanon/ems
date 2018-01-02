package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.EntityUtils;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.FormSectionType;
import com.ce.ems.base.classes.QueryFilter;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.PlatformInternal;
import com.ce.ems.base.core.ResourceException;
import com.ce.ems.entites.FormCompositeFieldEntity;
import com.ce.ems.entites.FormSectionEntity;
import com.ce.ems.entites.FormSimpleFieldEntity;
import com.googlecode.objectify.Key;
import com.kylantis.eaa.core.forms.CompositeEntry;
import com.kylantis.eaa.core.forms.Question;
import com.kylantis.eaa.core.forms.SimpleEntry;
import com.kylantis.eaa.core.pdf.Section;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;


public class FormModel extends BaseModel {

	@Override
	public String path() {
		return "core/form";
	}
	
	@Override
	public void preInstall() {
		FormFieldRepository.createDefaultFields();
	}

	public static void install() {
	}

	@PlatformInternal
	protected static Map<RoleRealm, Long> newSection(String name, FormSectionType type) {

		Map<RoleRealm, Long> result = new FluentHashMap<>();
		for(RoleRealm realm : RoleRealm.values()) {
			result.put(realm, newSection(name, type, realm));
		}
		return result;
	}
	
	/**
	 * This creates a new section, for the given realm
	*/
	@ModelMethod(functionality = {Functionality.MANAGE_APPLICATION_FORMS, Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM})
	public static Long newSection(String name, FormSectionType type, RoleRealm realm) {
		FormSectionEntity e = new FormSectionEntity()
				.setName(name)
				.setType(type.getValue())
				.setRealm(realm != null ? realm.getValue() : null);
		ofy().save().entity(e).now();
		return e.getId();
	}
	
	private static Integer getSectionType(Long sectionId) {
		FormSectionEntity e = ofy().load().type(FormSectionEntity.class).id(sectionId).safe();
		return e.getType();
	}

	/**
	 * This lists all sections for the given realm
	 */
	@ModelMethod(functionality = {Functionality.VIEW_APPLICATION_FORM, Functionality.VIEW_SYSTEM_CONFIGURATION})
	public static List<Section> listSections(FormSectionType type, RoleRealm realm) {

		List<QueryFilter> filters = new FluentArrayList<>();
		filters.add(QueryFilter.get("type = ", type.getValue()));
		
		if(realm != null) {
			filters.add(QueryFilter.get("realm = ", realm.getValue()));
		}
		
		QueryFilter[] filtersArray = filters.toArray(new QueryFilter[filters.size()]);
		
		List<Section> result = new FluentArrayList<>();

		EntityUtils.query(FormSectionEntity.class, filtersArray).forEach(e -> {
			result.add(new Section().setId(e.getId().toString()).setTitle(e.getName()).setSummary(e.getDescription()));
		});

		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public static void deleteSection(Long sectionId, FormSectionType type) {
		
		FormSectionEntity e = ofy().load().type(FormSectionEntity.class).id(sectionId).safe();
		
		if(!e.getType().equals(type.getValue())) {
			throw new ResourceException(ResourceException.DELETE_NOT_ALLOWED);
		}
		
		deleteSection(sectionId);
	}
	
	/**
	 * This deletes a section
	 */
	protected static void deleteSection(Long sectionId) {

		// Delete fields

		listFieldKeys(sectionId).forEach(k -> {

			ofy().delete().key(k);
		});

		// Delete entity
		
		ofy().delete().key(Key.create(FormSectionEntity.class, sectionId)).now();
	}

	/**
	 * This creates a new simple field
	 */
	protected static String newSimpleField(FormSectionType type, Long sectionId, SimpleEntry spec, Boolean isDefault) {
		
		if(!getSectionType(sectionId).equals(type.getValue())) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}
		
		// Create field

		FormSimpleFieldEntity e = EntityHelper.fromObjectModel(sectionId, isDefault, spec);

		ofy().save().entity(e).now();

		// Ensure key uniqueness in FormCompositeFieldEntity

		if (ofy().load().type(FormCompositeFieldEntity.class).id(e.getId()).now() != null) {

			Logger.warn("Duplicate key was created while creating simple form field. Recreating ..");

			ofy().delete().key(Key.create(FormSimpleFieldEntity.class, e.getId())).now();
			return newSimpleField(type, sectionId, spec, isDefault);
		}

		return e.getId();
	}

	/**
	 * This creates a new simple field
	 */
	@ModelMethod(functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public static String newSimpleField(FormSectionType type, Long sectionId, SimpleEntry spec) {
		return newSimpleField(type, sectionId, spec, false);
	}

	// Created for FormFieldRepository
	protected static String newSimpleField(Long sectionId, SimpleEntry spec) {
		return newSimpleField(FormSectionType.APPLICATION_FORM, sectionId, spec);
	}
	
	/**
	 * This lists all simple simple fields that matches the specified query filter
	 */
	private static List<SimpleEntry> listSimpleFields(QueryFilter... filters) {

		List<SimpleEntry> entries = new FluentArrayList<>();

		EntityUtils.lazyQuery(FormSimpleFieldEntity.class, filters).forEach(e -> {
			entries.add(EntityHelper.toObjectModel(e));
		});

		return entries;
	}

	/**
	 * This creates a new composite field
	 */
	protected static String newCompositeField(FormSectionType type, Long sectionId, CompositeEntry spec, Boolean isDefault) {


		if(!getSectionType(sectionId).equals(type.getValue())) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}
		
		// Create field

		FormCompositeFieldEntity e = EntityHelper.fromObjectModel(sectionId, isDefault, spec);

		ofy().save().entity(e).now();

		// Ensure key uniqueness in FormSimpleFieldEntity

		if (ofy().load().key(Key.create(FormSimpleFieldEntity.class, e.getId())).now() != null) {

			Logger.warn("Duplicate key was created while creating composite form field. Recreating ..");

			ofy().delete().key(Key.create(FormCompositeFieldEntity.class, e.getId())).now();

			return newCompositeField(type, sectionId, spec, isDefault);
		}

		return e.getId();
	}

	/**
	 * This creates a new composite field
	 */
	@ModelMethod(functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public static String newCompositeField(FormSectionType type, Long sectionId, CompositeEntry spec) {
		return newCompositeField(type, sectionId, spec, false);
	}
	
	// Created for FormFieldRepository
	protected static String newCompositeField(Long sectionId, CompositeEntry spec) {
		return newCompositeField(FormSectionType.APPLICATION_FORM, sectionId, spec);
	}

	/**
	 * This lists all composite fields that matches the specified query filter
	 */
	private static List<CompositeEntry> listCompositeFields(QueryFilter... filters) {

		List<CompositeEntry> entries = new FluentArrayList<>();

		EntityUtils.lazyQuery(FormCompositeFieldEntity.class, filters)
		.forEach(e -> {
			entries.add(EntityHelper.toObjectModel(e));
		});

		return entries;
	}
	
	protected static Map<String, Boolean> listAllFieldKeys(FormSectionType type, RoleRealm realm) {
		
		Map<String, Boolean> keys = new FluentHashMap<>();
		
		listSections(type, realm).forEach(section -> {
			
			System.out.println("Section : " + section.getId());
			
			Map<String, Boolean> o = new FluentHashMap<>();
			
			
			EntityUtils.lazyQuery(FormSimpleFieldEntity.class, QueryFilter.get("section =", section.getId())).forEach(e -> {

				System.out.println(e.getId() + ": " + e.getName());
				o.put(e.getId(), e.getIsRequired());
			});
			
			EntityUtils.lazyQuery(FormCompositeFieldEntity.class, QueryFilter.get("section =", section.getId())).forEach(e -> {

				System.out.println(e.getId() + ": " + e.getName());
				o.put(e.getId(), e.getIsRequired());
			});
					
			keys.putAll(o);
		});
		
		return keys;
	}

	/**
	 * This lists the keys for all simple and composite fields that exists in a section
	 */
	private static List<Key<?>> listFieldKeys(long sectionId) {

		List<Key<?>> keys = new FluentArrayList<>();

		ofy().load().type(FormSimpleFieldEntity.class).filter("section = ", sectionId).forEach(e -> {
			keys.add(Key.create(FormSimpleFieldEntity.class, e.getId()));
		});

		ofy().load().type(FormCompositeFieldEntity.class).filter("section = ", sectionId).forEach(e -> {
			keys.add(Key.create(FormCompositeFieldEntity.class, e.getId()));
		});

		return keys;
	}

	/**
	 * This gets fields available in the given section
	 */
	@ModelMethod(functionality = {Functionality.VIEW_APPLICATION_FORM, Functionality.VIEW_SYSTEM_CONFIGURATION})
	public static List<Question> getFields(FormSectionType type, long sectionId) {

		FormSectionEntity e = ofy().load().type(FormSectionEntity.class).id(sectionId).safe();
		
		if(!e.getType().equals(type.getValue())) {
			throw new ResourceException(ResourceException.ACCESS_NOT_ALLOWED);
		}
		
		List<Question> fields = new FluentArrayList<>();

		QueryFilter filter = QueryFilter.get("section =", sectionId);

		// Add simple fields
		fields.addAll(listSimpleFields(filter));

		// Add composite fields
		fields.addAll(listCompositeFields(filter));

		return fields;
	}

	/**
	 * This gets all fields available in the given section(s)
	 */
	@ModelMethod(functionality = {Functionality.VIEW_APPLICATION_FORM, Functionality.VIEW_SYSTEM_CONFIGURATION})
	public static Map<Long, List<Question>> getAllFields(FormSectionType type, List<Long> sectionIds) {

		Map<Long, List<Question>> result = new FluentHashMap<>();

		sectionIds.forEach(sectionId -> {

			result.put(sectionId, getFields(type, sectionId));
		});

		return result;
	}

	/**
	 * This deletes a given field
	 */
	@ModelMethod(functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public static void deleteField(FormSectionType type, String id) {

		boolean isDeleted = false;
		
		// Delete field entity

		FormSimpleFieldEntity se = ofy().load().key(Key.create(FormSimpleFieldEntity.class, id)).now();
		if (se != null && !se.getIsDefault()) {
			
			if(!getSectionType(se.getSection()).equals(type.getValue())) {
				throw new ResourceException(ResourceException.DELETE_NOT_ALLOWED);
			}
			
			ofy().delete().key(Key.create(FormSimpleFieldEntity.class, id)).now();
			isDeleted = true;
		}

		if (se == null) {
			FormCompositeFieldEntity ce = ofy().load().key(Key.create(FormCompositeFieldEntity.class, id)).now();
			if(ce != null && !ce.getIsDefault()) {
				
				if(!getSectionType(ce.getSection()).equals(type.getValue())) {
					throw new ResourceException(ResourceException.DELETE_NOT_ALLOWED);
				}
				
				ofy().delete().key(Key.create(FormCompositeFieldEntity.class, id)).now();
				isDeleted = true;
			}
		}

		// Then, delete delete saved values
		if(isDeleted) {
			if(type.equals(FormSectionType.APPLICATION_FORM)) {
				BaseUserModel.deleteFieldValues(id);
				ApplicationModel.deleteFieldValues(id);
			}
		} else {
			//<id> may be a default field
			throw new ResourceException(ResourceException.DELETE_NOT_ALLOWED);
		}
	}

}
