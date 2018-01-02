package com.ce.ems.models;

import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.FormSectionType;
import com.ce.ems.base.classes.Gender;
import com.kylantis.eaa.core.forms.CompositeEntry;
import com.kylantis.eaa.core.forms.InputType;
import com.kylantis.eaa.core.forms.SimpleEntry;
import com.kylantis.eaa.core.fusion.EndpointRepository;
import com.kylantis.eaa.core.users.RoleRealm;

public class FormFieldRepository {

	private static final String FORM_FIELD_MAPPING_KEY_PREFIX = "FORM_FIELD_MAPPING_";
	
	public static void createDefaultFields() {
		
		FormModel.newSection("Profile Information", FormSectionType.APPLICATION_FORM).forEach((k, v) -> {

			saveFieldId(k, FieldType.FIRST_NAME, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.TEXT, "First Name").setSortOrder(1).setIsDefault(true)));

			saveFieldId(k, FieldType.MIDDLE_NAME, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.TEXT, "Middle Name").setSortOrder(2).setIsDefault(true).setIsRequired(false)));

			saveFieldId(k, FieldType.LAST_NAME, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.TEXT, "Last Name").setSortOrder(3).setIsDefault(true)));

			saveFieldId(k, FieldType.GENDER,
					FormModel.newCompositeField(v,
							(CompositeEntry) new CompositeEntry("Gender").withItem("Male", Gender.MALE.getValue())
									.withItem("Female", Gender.FEMALE.getValue()).setSortOrder(4).setIsDefault(true)));
			
			saveFieldId(k, FieldType.DATE_OF_BIRTH, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.DATE_OF_BIRTH, "Date Of Birth").setSortOrder(5).setIsDefault(true)));
			
			saveFieldId(k, FieldType.EMAIL, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.EMAIL, "Email").setSortOrder(6).setIsDefault(true)));
		});

		
		FormModel.newSection("Contact Information", FormSectionType.APPLICATION_FORM).forEach((k, v) -> {

			saveFieldId(k, FieldType.PHONE_NUMBER, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.PHONE, "Phone number").setSortOrder(1).setIsDefault(true)));

			saveFieldId(k, FieldType.ADDRESS, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.TEXT, "Address").setSortOrder(2).setIsDefault(true)));

			String countryField = FormModel.newCompositeField(v, (CompositeEntry) new CompositeEntry("Country")
					.setItemsSource(EndpointRepository.GET_COUNTRY_NAMES).setSortOrder(3).setIsDefault(true));
			saveFieldId(k, FieldType.COUNTRY, countryField);

			
			String stateField = FormModel.newCompositeField(v,
					(CompositeEntry) new CompositeEntry("State").setItemsSource(EndpointRepository.GET_STATE_NAMES)
							.setContext(countryField).setSortOrder(4).setIsDefault(true));
			saveFieldId(k, FieldType.STATE, stateField);
			
			String cityField = FormModel.newCompositeField(v,
					(CompositeEntry) new CompositeEntry("City").setItemsSource(EndpointRepository.GET_CITY_NAMES)
							.setContext(stateField).setSortOrder(5).setIsDefault(true));
			saveFieldId(k, FieldType.CITY, cityField);
	
		});

		
		FormModel.newSection("Institution Profile", FormSectionType.APPLICATION_FORM).forEach((k, v) -> {

			switch (k) {
			
			case ADMIN:
				
				
				break;
			case EXAM_OFFICER:
				
				
				break;
			case HEAD_OF_DEPARTMENT:
				
				String facultyField = FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(EndpointRepository.GET_FACULTY_NAMES).setSortOrder(1).setIsDefault(true));
				saveFieldId(k, FieldType.FACULTY, facultyField);

				saveFieldId(k, FieldType.DEPARTMENT, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Country")
						.setItemsSource(EndpointRepository.GET_DEPARTMENT_NAMES).setSortOrder(2).setIsDefault(true)
						.setContext(facultyField)));

				break;
			case DEAN:
				
				saveFieldId(k, FieldType.FACULTY, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(EndpointRepository.GET_FACULTY_NAMES).setSortOrder(1).setIsDefault(true)));

				break;
			case LECTURER:
				

				String _facultyField = FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(EndpointRepository.GET_FACULTY_NAMES).setSortOrder(1).setIsDefault(true));
				saveFieldId(k, FieldType.FACULTY, _facultyField);

				saveFieldId(k, FieldType.DEPARTMENT, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Country")
						.setItemsSource(EndpointRepository.GET_DEPARTMENT_NAMES).setSortOrder(2).setIsDefault(true)
						.setContext(_facultyField)));
				
				break;
				
			case STUDENT:
				
				saveFieldId(k, FieldType.MATRIC_NUMBER, FormModel.newSimpleField(v,
						(SimpleEntry) new SimpleEntry(InputType.TEXT, "Matric Number").setSortOrder(1).setIsDefault(true)));

				saveFieldId(k, FieldType.LEVEL, FormModel.newSimpleField(v,
						(SimpleEntry) new SimpleEntry(InputType.TEXT, "Level").setSortOrder(2).setIsDefault(true)));

				saveFieldId(k, FieldType.JAMB_REG_NO, FormModel.newSimpleField(v,
						(SimpleEntry) new SimpleEntry(InputType.TEXT, "Jamb Reg No.").setSortOrder(3).setIsDefault(true)));

				String __facultyField = FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(EndpointRepository.GET_FACULTY_NAMES).setSortOrder(4).setIsDefault(true));
				saveFieldId(k, FieldType.FACULTY, __facultyField);

				saveFieldId(k, FieldType.DEPARTMENT, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Country")
						.setItemsSource(EndpointRepository.GET_DEPARTMENT_NAMES).setSortOrder(5).setIsDefault(true)
						.setContext(__facultyField)));

				break;
			}

		});

	}

	public static Map<FieldType, String> getFieldIds(RoleRealm realm) {
		
		FluentHashMap<FieldType, String> result = new FluentHashMap<FieldType, String>()
				.with(FieldType.FIRST_NAME, FormFieldRepository.getFieldId(realm, FieldType.FIRST_NAME))
				.with(FieldType.LAST_NAME, FormFieldRepository.getFieldId(realm, FieldType.LAST_NAME))
				.with(FieldType.MIDDLE_NAME, FormFieldRepository.getFieldId(realm, FieldType.MIDDLE_NAME))
				.with(FieldType.GENDER, FormFieldRepository.getFieldId(realm, FieldType.GENDER))
				.with(FieldType.DATE_OF_BIRTH, FormFieldRepository.getFieldId(realm, FieldType.DATE_OF_BIRTH))
				.with(FieldType.EMAIL, FormFieldRepository.getFieldId(realm, FieldType.EMAIL))
				
				.with(FieldType.PHONE_NUMBER, FormFieldRepository.getFieldId(realm, FieldType.PHONE_NUMBER))
				.with(FieldType.ADDRESS, FormFieldRepository.getFieldId(realm, FieldType.ADDRESS))
				.with(FieldType.CITY, FormFieldRepository.getFieldId(realm, FieldType.CITY))
				.with(FieldType.STATE, FormFieldRepository.getFieldId(realm, FieldType.STATE))
				.with(FieldType.COUNTRY, FormFieldRepository.getFieldId(realm, FieldType.COUNTRY));
	
		switch (realm) {
		
		  case ADMIN:
			  break;
			  
		  case EXAM_OFFICER:
			  break;
			  
		  case HEAD_OF_DEPARTMENT:
			  result
			  	.with(FieldType.FACULTY, FormFieldRepository.getFieldId(realm, FieldType.FACULTY))
				.with(FieldType.DEPARTMENT, FormFieldRepository.getFieldId(realm, FieldType.DEPARTMENT));
			  break;
			 
		  case DEAN:
			  result
			  	.with(FieldType.FACULTY, FormFieldRepository.getFieldId(realm, FieldType.FACULTY));
			  break;
			  
		  case LECTURER:
			  result
			  	.with(FieldType.FACULTY, FormFieldRepository.getFieldId(realm, FieldType.FACULTY))
				.with(FieldType.DEPARTMENT, FormFieldRepository.getFieldId(realm, FieldType.DEPARTMENT));
			  break;
			  
		  case STUDENT:
			  result
			  	.with(FieldType.FACULTY, FormFieldRepository.getFieldId(realm, FieldType.FACULTY))
				.with(FieldType.DEPARTMENT, FormFieldRepository.getFieldId(realm, FieldType.DEPARTMENT))
			  	.with(FieldType.JAMB_REG_NO, FormFieldRepository.getFieldId(realm, FieldType.JAMB_REG_NO))
				.with(FieldType.LEVEL, FormFieldRepository.getFieldId(realm, FieldType.LEVEL))
				.with(FieldType.MATRIC_NUMBER, FormFieldRepository.getFieldId(realm, FieldType.MATRIC_NUMBER));
			  break;
		
		default:
			break;
		}
		
		return result;
	}

	private static void saveFieldId(RoleRealm realm, FieldType fieldType, String Id) {
		String key = FORM_FIELD_MAPPING_KEY_PREFIX + realm.name() + "_" + fieldType;
		ConfigModel.put(key, Id);
	}

	public static String getFieldId(RoleRealm realm, FieldType fieldType) {
		String key = FORM_FIELD_MAPPING_KEY_PREFIX + realm.name() + "_" + fieldType;
		Object value = ConfigModel.get(key);
		return value != null ? value.toString() : null;
	}

	public enum FieldType {
		FIRST_NAME, MIDDLE_NAME, LAST_NAME, DATE_OF_BIRTH, GENDER, EMAIL, PHONE_NUMBER, ADDRESS, CITY, STATE, COUNTRY, POSTAL_CODE, MATRIC_NUMBER, LEVEL, JAMB_REG_NO, FACULTY, DEPARTMENT
	}

}
