package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ce.ems.base.classes.ApplicationStatus;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.FormSectionType;
import com.ce.ems.base.classes.Gender;
import com.ce.ems.base.classes.IndexedNameSpec;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.spec.DepartmentalHeadSpec;
import com.ce.ems.base.classes.spec.FacultyDeanSpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.base.core.Exceptions;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Model;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.ResourceException;
import com.ce.ems.entites.ApplicationEntity;
import com.ce.ems.entites.ApplicationFormValueEntity;
import com.ce.ems.entites.DeclinedApplicationEntity;
import com.ce.ems.models.FormFieldRepository.FieldType;
import com.ce.ems.utils.FrontendObjectMarshaller;
import com.ce.ems.utils.Utils;
import com.googlecode.objectify.Key;
import com.kylantis.eaa.core.keys.ConfigKeys;
import com.kylantis.eaa.core.pdf.FormFactory;
import com.kylantis.eaa.core.pdf.PDFForm;
import com.kylantis.eaa.core.pdf.SizeSpec;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;
import com.kylantis.eaa.core.users.UserProfileSpec;

@Model(dependencies = { FormModel.class, PlatformModel.class })
public class ApplicationModel extends BaseModel {

	@Override
	public String path() {
		return "core/application";
	}

	@Override
	public void preInstall() {
	}

	@Override
	public void install(InstallOptions options) {

		Logger.info("Generating application questionnaires for all role realms");

		for (RoleRealm realm : RoleRealm.values()) {

			String blobId = generatePDFQuestionnaire(realm);
			ConfigModel.put(ConfigKeys.$REALM_APPLICATION_FORM_BLOB_ID.replace("$REALM", realm.name()), blobId);

			Logger.info("Generated Questionnaire for " + realm.toString() + " with blob-id: " + blobId);
		}
	}

	@ModelMethod(functionality = Functionality.CREATE_APPLICATION)
	public static Long newApplication(String role) {

		// Create application with status of CREATED

		Long applicationId = ofy().save().entity(new ApplicationEntity().setRole(role)
				.setStatus(ApplicationStatus.CREATED.getValue()).setDateCreated(new Date())).now().getId();

		
		// Update cached list index

		SearchModel.addCachedListKey(IndexedNameType.APPLICATION,
				FluentHashMap.forValueMap().with("status", ApplicationStatus.CREATED.getValue()),
				applicationId);

		return applicationId;
	}

	@ModelMethod(functionality = Functionality.UPDATE_APPLICATION)
	public static void updateApplication(Long applicationId, Map<String, String> values) {

		ApplicationStatus status = getApplicationStatus(applicationId);

		if (!(status.equals(ApplicationStatus.CREATED) || status.equals(ApplicationStatus.OPEN))) {
			throw new ResourceException(ResourceException.UPDATE_NOT_ALLOWED);
		}

		validateApplicationValues(applicationId, values);

		// Delete old values
		deleteFieldValuesForApplication(applicationId);

		// Save values
		List<ApplicationFormValueEntity> entries = new FluentArrayList<>();
		values.forEach((k, v) -> {
			entries.add(new ApplicationFormValueEntity().setApplicationId(applicationId).setFieldId(k)
					.setValue(v.toString()).setDateUpdated(new Date()));
		});

		ofy().save().entities(entries).now();

		if(status.equals(ApplicationStatus.CREATED)) {

			// Update status
			updateApplicationStatus(applicationId, ApplicationStatus.OPEN);
			
			// Update cached list index

			SearchModel.removeCachedListKey(IndexedNameType.APPLICATION,
					FluentHashMap.forValueMap().with("status", ApplicationStatus.CREATED.getValue()),
					applicationId);
			
			SearchModel.addCachedListKey(IndexedNameType.APPLICATION,
					FluentHashMap.forValueMap().with("status", ApplicationStatus.OPEN.getValue()),
					applicationId);
		}
	}

	@ModelMethod(functionality = Functionality.SUBMIT_APPLICATION)
	public static Long submitApplication(Long applicationId) {

		String role = getApplicationRole(applicationId);
		RoleRealm realm = RoleModel.getRealm(role);

		Map<String, String> values = getFieldValues(applicationId);

		validateApplicationValues(applicationId, values);

		updateApplicationStatus(applicationId, ApplicationStatus.PENDING);

		// Update status
		updateApplicationStatus(applicationId, ApplicationStatus.OPEN);
		
		
		// Update cached list index

		SearchModel.removeCachedListKey(IndexedNameType.APPLICATION,
				FluentHashMap.forValueMap().with("status", ApplicationStatus.OPEN.getValue()),
				applicationId);
		
		SearchModel.addCachedListKey(IndexedNameType.APPLICATION,
				FluentHashMap.forValueMap().with("status", ApplicationStatus.PENDING.getValue()),
				applicationId);

		
		// Add to search index
		
		IndexedNameSpec nameSpec = getNameSpec(applicationId, realm, values);
		SearchModel.addIndexedName(nameSpec, IndexedNameType.APPLICATION);
		
		return applicationId;
	}

	private static void validateApplicationValues(Long applicationId, Map<String, String> values) {

		RoleRealm realm = RoleModel.getRealm(getApplicationRole(applicationId));

		System.out.println(realm);

		// Scan for relevant fields, verify that proper values are provided for them

		for (Entry<String, Boolean> e : FormModel.listAllFieldKeys(FormSectionType.APPLICATION_FORM, realm)
				.entrySet()) {

			if (!e.getValue()) {
				continue;
			}

			String value = values.get(e.getKey());

			if (value == null || value.trim().equals("")) {
				throw new ResourceException(ResourceException.FAILED_VALIDATION);
			}
		}
		;
	}

	@ModelMethod(functionality = Functionality.DOWNLOAD_QUESTIONNAIRE)
	public static String getPDFQuestionnaire(String role) {

		RoleRealm realm = RoleModel.getRealm(role);

		String blobId = (String) ConfigModel
				.get(ConfigKeys.$REALM_APPLICATION_FORM_BLOB_ID.replace("$REALM", realm.toString()));

		if (blobId == null) {
			return generatePDFQuestionnaire(realm);
		}

		return blobId;
	}

	private static String generatePDFQuestionnaire(RoleRealm realm) {

		PDFForm form = new PDFForm().setLogoURL(ConfigModel.get(ConfigKeys.ORGANIZATION_LOGO).toString())
				.setSubtitleLeft(Utils.prettify(realm.name().toLowerCase())).setTitle("Registration")
				.setSubtitleRight(ConfigModel.get(ConfigKeys.ORGANIZATION_NAME).toString());

		FormModel.listSections(FormSectionType.APPLICATION_FORM, realm).forEach((section) -> {

			section.withEntries(FormModel.getFields(FormSectionType.APPLICATION_FORM, Long.parseLong(section.getId())));

			form.withSection(section);
		});

		File tmp = FormFactory.toPDF(new SizeSpec(12), new SizeSpec(14), form);

		try {

			String blobId = BlobStoreModel.save(new FileInputStream(tmp));

			ConfigModel.put(ConfigKeys.$REALM_APPLICATION_FORM_BLOB_ID.replace("$REALM", realm.toString()), blobId);

			return blobId;

		} catch (IOException e) {
			Exceptions.throwRuntime(e);
			return null;
		}
	}

	protected static Map<String, String> getFieldValues(Long applicationId, Collection<String> fieldIds) {

		Map<String, String> result = new FluentHashMap<>();

		fieldIds.forEach(fieldId -> {

			ApplicationFormValueEntity e = ofy().load().type(ApplicationFormValueEntity.class)
					.filter("fieldId = ", fieldId).filter("applicationId = ", applicationId.toString()).first().now();

			result.put(fieldId, e != null ? e.getValue() : null);
		});
		return result;
	}

	public static String getApplicationRole(Long id) {
		ApplicationEntity e = ofy().load().type(ApplicationEntity.class).id(id).safe();
		return e.getRole();
	}

	private static ApplicationStatus getApplicationStatus(Long id) {
		ApplicationEntity e = ofy().load().type(ApplicationEntity.class).id(id).safe();
		return ApplicationStatus.from(e.getStatus());
	}

	@ModelMethod(functionality = Functionality.UPDATE_APPLICATION)
	public static Map<String, String> getFieldValues(Long applicationId) {

		Map<String, String> result = new FluentHashMap<>();

		RoleRealm realm = RoleModel.getRealm(getApplicationRole(applicationId));

		Collection<String> fieldIds = FormModel.listAllFieldKeys(FormSectionType.APPLICATION_FORM, realm).keySet();

		fieldIds.forEach(fieldId -> {

			ApplicationFormValueEntity e = ofy().load().type(ApplicationFormValueEntity.class)
					.filter("fieldId = ", fieldId).filter("applicationId = ", applicationId.toString()).first().now();

			result.put(fieldId, e != null ? e.getValue() : null);
		});
		return result;
	}

	protected static void deleteFieldValues(String fieldId) {

		List<Key<ApplicationFormValueEntity>> keys = new FluentArrayList<>();

		ofy().load().type(ApplicationFormValueEntity.class).filter("fieldId = ", fieldId).forEach(e -> {
			keys.add(Key.create(ApplicationFormValueEntity.class, e.getId()));
		});

		ofy().delete().keys(keys).now();
	}

	private static void deleteFieldValuesForApplication(Long applicationId) {

		// Delete form values

		List<Key<?>> keys = new FluentArrayList<>();

		ofy().load().type(ApplicationFormValueEntity.class).filter("applicationId = ", applicationId.toString())
				.forEach(e -> {
					keys.add(Key.create(ApplicationFormValueEntity.class, e.getId()));
				});

		ofy().delete().keys(keys).now();
	}

	private static void updateApplicationStatus(Long applicationId, ApplicationStatus status) {
		ApplicationEntity e = ofy().load().type(ApplicationEntity.class).id(applicationId).safe();

		e.setStatus(status.getValue());
		e.setDateUpdated(new Date());

		ofy().save().entity(e);
	}

	@ModelMethod(functionality = Functionality.REVIEW_APPLICATION)
	public static Long acceptApplication(Long principal, Long applicationId) {

		// Consolidate application
		Long userId = consolidateApplication(principal, applicationId);

		// Update application status
		updateApplicationStatus(applicationId, ApplicationStatus.ACCEPTED);

		// Delete field values for application
		// deleteFieldValuesForApplication(applicationId);

		
		// Update cached list index

		SearchModel.removeCachedListKey(IndexedNameType.APPLICATION,
					FluentHashMap.forValueMap().with("status", ApplicationStatus.PENDING.getValue()),
					applicationId);
				
		SearchModel.addCachedListKey(IndexedNameType.APPLICATION,
					FluentHashMap.forValueMap().with("status", ApplicationStatus.ACCEPTED.getValue()),
					applicationId);
		
		
		//Update type and entity Id of indexed name
		
		SearchModel.updateIndexedNameType(applicationId, userId, IndexedNameType.APPLICATION, IndexedNameType.USER);

		return userId;
	}

	@ModelMethod(functionality = Functionality.REVIEW_APPLICATION)
	public static void declineApplication(Long applicationId, Long staffId, String reason) {

		// Update application status
		updateApplicationStatus(applicationId, ApplicationStatus.DECLINED);

		// Create new declined application

		DeclinedApplicationEntity de = new DeclinedApplicationEntity().setApplicationId(applicationId)
				.setStaffId(staffId).setReason(reason).setDateCreated(new Date());
		ofy().save().entity(de);

		// Delete field values for application
		// deleteFieldValuesForApplication(applicationId);


		// Update cached list index

		SearchModel.removeCachedListKey(IndexedNameType.APPLICATION,
					FluentHashMap.forValueMap().with("status", ApplicationStatus.PENDING.getValue()),
					applicationId);
				
		SearchModel.addCachedListKey(IndexedNameType.APPLICATION,
					FluentHashMap.forValueMap().with("status", ApplicationStatus.DECLINED.getValue()),
					applicationId);
		
		
		//remove indexed name
		
		SearchModel.removeIndexedName(applicationId.toString(), IndexedNameType.APPLICATION);
	}

	private static Long consolidateApplication(Long principal, Long applicationId) {

		String role = getApplicationRole(applicationId);
		RoleRealm realm = RoleModel.getRealm(role);

		Map<FieldType, String> keys = FormFieldRepository.getFieldIds(realm);
		Map<String, String> values = getFieldValues(applicationId);

		UserProfileSpec user = getConsolidatedUser(keys, values);
		Long userId = BaseUserModel.registerUser(user, role, principal);

		switch (realm) {
		case DEAN:
			DirectoryModel.createFacultyDean(userId, getConsolidatedDean(keys, values));
			break;
		case HEAD_OF_DEPARTMENT:
			DirectoryModel.createDepartmentalHead(userId, getConsolidatedHod(keys, values));
			break;
		case LECTURER:
			DirectoryModel.createLecturer(userId, getConsolidatedLecturer(keys, values));
			break;
		case STUDENT:
			DirectoryModel.createStudent(userId, getConsolidatedStudent(keys, values));
			break;
		case ADMIN:
			break;
		case EXAM_OFFICER:
			break;
		default:
			break;
		}

		// Notify user of new account creation

		return userId;
	}

	private static FacultyDeanSpec getConsolidatedDean(Map<FieldType, String> keys, Map<String, String> values) {
		return new FacultyDeanSpec().setFaculty(Long.parseLong(values.get(keys.get(FieldType.FACULTY))))
				.setStartDate(new Date());
	}

	private static DepartmentalHeadSpec getConsolidatedHod(Map<FieldType, String> keys, Map<String, String> values) {
		return new DepartmentalHeadSpec().setDepartment(Long.parseLong(values.get(keys.get(FieldType.DEPARTMENT))))
				.setStartDate(new Date());
	}

	private static LecturerSpec getConsolidatedLecturer(Map<FieldType, String> keys, Map<String, String> values) {
		return new LecturerSpec().setCourses(new ArrayList<>())
				.setDepartment(Long.parseLong(values.get(keys.get(FieldType.DEPARTMENT)))).setStartDate(new Date());
	}

	private static StudentSpec getConsolidatedStudent(Map<FieldType, String> keys, Map<String, String> values) {

		Long department = Long.parseLong(values.get(keys.get(FieldType.DEPARTMENT)));
		Integer level = Integer.parseInt(values.get(keys.get(FieldType.LEVEL)));

		Long departmentLevelId = DirectoryModel.getDepartmentLevel(department, level);

		return new StudentSpec().setJambRegNo(values.get(keys.get(FieldType.JAMB_REG_NO)))
				.setMatricNumber(values.get(keys.get(FieldType.MATRIC_NUMBER))).setDepartmentLevel(departmentLevelId);
	}

	private static UserProfileSpec getConsolidatedUser(Map<FieldType, String> keys, Map<String, String> values) {

		// Generate new password
		String password = Utils.newRandom().substring(0, 6);

		return new UserProfileSpec()

				.setFirstName(values.get(keys.get(FieldType.FIRST_NAME)))
				.setLastName(values.get(keys.get(FieldType.LAST_NAME)))
				.setMiddleName(values.get(keys.get(FieldType.MIDDLE_NAME)))
				.setDateOfBirth(FrontendObjectMarshaller.unmarshalDate(values.get(keys.get(FieldType.DATE_OF_BIRTH))))
				.setGender(Gender.from(Integer.parseInt(values.get(keys.get(FieldType.GENDER)))))
				.setEmail(values.get(keys.get(FieldType.EMAIL)).toString()).setPassword(password)

				.setAddress(values.get(keys.get(FieldType.ADDRESS)))
				.setPhone(Long.parseLong(values.get(keys.get(FieldType.PHONE_NUMBER))))
				.setCity(Integer.parseInt(values.get(keys.get(FieldType.CITY))))
				.setTerritory(values.get(keys.get(FieldType.STATE)))
				.setCountry(values.get(keys.get(FieldType.COUNTRY)));
	}

	public static IndexedNameSpec getNameSpec(Long applicationId, RoleRealm realm) {
		return getNameSpec(applicationId, realm, null);
	}

	private static IndexedNameSpec getNameSpec(Long applicationId, RoleRealm realm, Map<String, String> fieldValues) {

		IndexedNameSpec spec = new IndexedNameSpec();

		String firstNameField = FormFieldRepository.getFieldId(realm, FormFieldRepository.FieldType.FIRST_NAME);
		String middleNameField = FormFieldRepository.getFieldId(realm, FormFieldRepository.FieldType.MIDDLE_NAME);
		String lastNameField = FormFieldRepository.getFieldId(realm, FormFieldRepository.FieldType.LAST_NAME);

		if (fieldValues == null) {
			fieldValues = getFieldValues(applicationId,
					new FluentArrayList<String>().with(firstNameField).with(middleNameField).with(lastNameField));
		}

		spec.setKey(applicationId.toString()).setX(fieldValues.get(firstNameField))
				.setY(fieldValues.get(lastNameField)).setZ(fieldValues.get(middleNameField));

		return spec;
	}

}
