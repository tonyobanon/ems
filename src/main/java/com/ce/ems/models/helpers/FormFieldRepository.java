package com.ce.ems.models.helpers;

import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.FormSectionType;
import com.ce.ems.base.classes.Gender;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.models.ConfigModel;
import com.ce.ems.models.FormModel;
import com.kylantis.eaa.core.forms.CompositeEntry;
import com.kylantis.eaa.core.forms.InputType;
import com.kylantis.eaa.core.forms.SimpleEntry;
import com.kylantis.eaa.core.fusion.APIRoutes;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;

@BlockerTodo("Add proper sort order for form section and questions")
public class FormFieldRepository {

	private static final String FORM_FIELD_MAPPING_KEY_PREFIX = "FORM_FIELD_MAPPING_";
	
	public static void createDefaultFields() {
		
		FormModel.newSection("Profile Information", FormSectionType.APPLICATION_FORM).forEach((k, v) -> {

			saveFieldId(k, FieldType.IMAGE, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.IMAGE, "Passport").setSortOrder(1).setIsDefault(true)));
			
			saveFieldId(k, FieldType.FIRST_NAME, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.PLAIN, "First Name").setSortOrder(2).setIsDefault(true)));

			saveFieldId(k, FieldType.MIDDLE_NAME, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.PLAIN, "Middle Name").setSortOrder(3).setIsDefault(true).setIsRequired(false)));

			saveFieldId(k, FieldType.LAST_NAME, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.PLAIN, "Last Name").setSortOrder(4).setIsDefault(true)));

			saveFieldId(k, FieldType.GENDER,
					FormModel.newCompositeField(v,
							(CompositeEntry) new CompositeEntry("Gender").withItem("Male", Gender.MALE.getValue())
									.withItem("Female", Gender.FEMALE.getValue()).setSortOrder(5).setIsDefault(true)));
			
			saveFieldId(k, FieldType.DATE_OF_BIRTH, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.DATE_OF_BIRTH, "Date Of Birth").setSortOrder(6).setIsDefault(true)));
			
			saveFieldId(k, FieldType.EMAIL, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.EMAIL, "Email").setSortOrder(7).setIsDefault(true)));
		});

		
		FormModel.newSection("Contact Information", FormSectionType.APPLICATION_FORM).forEach((k, v) -> {

			saveFieldId(k, FieldType.PHONE_NUMBER, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.PHONE, "Phone number").setSortOrder(1).setIsDefault(true)));

			saveFieldId(k, FieldType.ADDRESS, FormModel.newSimpleField(v,
					(SimpleEntry) new SimpleEntry(InputType.PLAIN, "Address").setSortOrder(2).setIsDefault(true)));

			String countryField = FormModel.newCompositeField(v, (CompositeEntry) new CompositeEntry("Country")
					.setItemsSource(APIRoutes.getUri(Functionality.GET_COUNTRY_NAMES).get(0)).setSortOrder(3).setIsDefault(true));
			saveFieldId(k, FieldType.COUNTRY, countryField);

			
			String stateField = FormModel.newCompositeField(v,
					(CompositeEntry) new CompositeEntry("State").setItemsSource(APIRoutes.getUri(Functionality.GET_TERRITORY_NAMES).get(0))
							.setContext(countryField).setSortOrder(4).setIsDefault(true));
			saveFieldId(k, FieldType.STATE, stateField);
			
			String cityField = FormModel.newCompositeField(v,
					(CompositeEntry) new CompositeEntry("City").setItemsSource(APIRoutes.getUri(Functionality.GET_CITY_NAMES).get(0))
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
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_FACULTY_NAMES).get(0)).setSortOrder(1).setIsDefault(true));
				saveFieldId(k, FieldType.FACULTY, facultyField);

				saveFieldId(k, FieldType.DEPARTMENT, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Department")
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_DEPARTMENT_NAMES).get(0)).setSortOrder(2).setIsDefault(true)
						.setContext(facultyField)));

				break;
			case DEAN:
				
				saveFieldId(k, FieldType.FACULTY, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_FACULTY_NAMES).get(0)).setSortOrder(1).setIsDefault(true)));

				break;
			case LECTURER:
				

				String _facultyField = FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_FACULTY_NAMES).get(0)).setSortOrder(1).setIsDefault(true));
				saveFieldId(k, FieldType.FACULTY, _facultyField);

				saveFieldId(k, FieldType.DEPARTMENT, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Department")
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_DEPARTMENT_NAMES).get(0)).setSortOrder(2).setIsDefault(true)
						.setContext(_facultyField)));
				
				break;
				
			case STUDENT:
				
				saveFieldId(k, FieldType.MATRIC_NUMBER, FormModel.newSimpleField(v,
						(SimpleEntry) new SimpleEntry(InputType.PLAIN, "Matric Number").setSortOrder(1).setIsDefault(true)));

				saveFieldId(k, FieldType.LEVEL, FormModel.newSimpleField(v,
						(SimpleEntry) new SimpleEntry(InputType.NUMBER_4L, "Level").setSortOrder(2).setIsDefault(true)));

				saveFieldId(k, FieldType.JAMB_REG_NO, FormModel.newSimpleField(v,
						(SimpleEntry) new SimpleEntry(InputType.PLAIN, "Jamb Reg No.").setSortOrder(3).setIsDefault(true)));

				String __facultyField = FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Faculty")
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_FACULTY_NAMES).get(0)).setSortOrder(4).setIsDefault(true));
				saveFieldId(k, FieldType.FACULTY, __facultyField);

				saveFieldId(k, FieldType.DEPARTMENT, FormModel.newCompositeField(v,
						(CompositeEntry) new CompositeEntry("Department")
						.setItemsSource(APIRoutes.getUri(Functionality.LIST_DEPARTMENT_NAMES).get(0)).setSortOrder(5).setIsDefault(true)
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
		IMAGE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DATE_OF_BIRTH, GENDER, EMAIL, PHONE_NUMBER, ADDRESS, CITY, STATE, COUNTRY, POSTAL_CODE, MATRIC_NUMBER, LEVEL, JAMB_REG_NO, FACULTY, DEPARTMENT
	}

}
