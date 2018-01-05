package com.ce.ems.base.classes;

import java.util.Date;

import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.ce.ems.base.classes.spec.ActivitySpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.BlobSpec;
import com.ce.ems.base.classes.spec.CourseResultReportSpec;
import com.ce.ems.base.classes.spec.CourseSpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.classes.spec.DepartmentalHeadSpec;
import com.ce.ems.base.classes.spec.FacultyDeanSpec;
import com.ce.ems.base.classes.spec.FacultySpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.LevelSemesterSpec;
import com.ce.ems.base.classes.spec.ResultRecordSheetSpec;
import com.ce.ems.base.classes.spec.StudentResultReportSpec;
import com.ce.ems.base.classes.spec.StudentSemesterCoursesSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.entites.ActivityEntity;
import com.ce.ems.entites.BaseUserEntity;
import com.ce.ems.entites.BlobEntity;
import com.ce.ems.entites.FormCompositeFieldEntity;
import com.ce.ems.entites.FormSimpleFieldEntity;
import com.ce.ems.entites.IndexedNameEntity;
import com.ce.ems.entites.calculation.AcademicSemesterCourseEntity;
import com.ce.ems.entites.calculation.CourseResultReportEntity;
import com.ce.ems.entites.calculation.ResultRecordSheetEntity;
import com.ce.ems.entites.calculation.StudentResultReportEntity;
import com.ce.ems.entites.calculation.StudentSemesterCoursesEntity;
import com.ce.ems.entites.directory.AcademicSemesterEntity;
import com.ce.ems.entites.directory.AssessmentTotalEntity;
import com.ce.ems.entites.directory.CourseEntity;
import com.ce.ems.entites.directory.DepartmentEntity;
import com.ce.ems.entites.directory.DepartmentalHeadEntity;
import com.ce.ems.entites.directory.FacultyDeanEntity;
import com.ce.ems.entites.directory.FacultyEntity;
import com.ce.ems.entites.directory.LecturerEntity;
import com.ce.ems.entites.directory.LevelSemesterEntity;
import com.ce.ems.entites.directory.StudentEntity;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.forms.CompositeEntry;
import com.kylantis.eaa.core.forms.InputType;
import com.kylantis.eaa.core.forms.SimpleEntry;
import com.kylantis.eaa.core.users.UserProfileSpec;

public class EntityHelper {
	
	
	/////////////////	FormCompositeFieldEntity   ///////////////////
	
	public static CompositeEntry toObjectModel(FormCompositeFieldEntity entity) {

		CompositeEntry o = new CompositeEntry(entity.getId(), entity.getName())
				
				.withItems(entity.getOptions())
				.setItemsSource(entity.getItemsSource())
				.setDefaultSelections(entity.getDefaultSelections())
				.setAllowMultipleChoice(entity.getAllowMultipleChoice());
				
				o.setSortOrder(entity.getSortOrder());
				o.setIsRequired(entity.getIsRequired());
				o.setIsVisible(entity.getIsVisible());
				o.setIsDefault(entity.getIsDefault());
		
		return o;
	}

	public static FormCompositeFieldEntity fromObjectModel(String sectionId, Boolean isInternal, CompositeEntry spec) {

		FormCompositeFieldEntity o = new FormCompositeFieldEntity()
				
				.setId(spec.getId() != null ? (String) spec.getId() : Utils.newRandom())
				.setName(spec.getTitle())
				.setSection(sectionId)
				
				.setSortOrder(spec.getSortOrder())
				.setIsRequired(spec.getIsRequired())
				.setIsVisible(spec.getIsVisible())
				.setIsDefault(spec.getIsDefault() ? true : null)
				
				.setItemsSource(spec.getItemsSource())
				.setOptions(spec.getItems())
				.setDefaultSelections(spec.getDefaultSelections())
				.setAllowMultipleChoice(spec.isAllowMultipleChoice())
				
				.setDateCreated(new Date());
		
		return o;
	}

	/////////////////	FormCompositeFieldEntity   ///////////////////
	
	
	
	
	
	
	
	/////////////////	FormSimpleFieldEntity   ///////////////////
	
	public static SimpleEntry toObjectModel(FormSimpleFieldEntity entity) {

		SimpleEntry o = new SimpleEntry(entity.getId(), InputType.valueOf(entity.getInputType()), entity.getName())
				
				.setDefaultValue(entity.getDefaultValue());
				
				o.setSortOrder(entity.getSortOrder());
				o.setIsRequired(entity.getIsRequired());
				o.setIsVisible(entity.getIsVisible());
				o.setIsDefault(entity.getIsDefault());
		
		return o;
	}

	public static FormSimpleFieldEntity fromObjectModel(String sectionId, Boolean isDefault, SimpleEntry spec) {

		FormSimpleFieldEntity o = new FormSimpleFieldEntity()
				
				.setId(spec.getId() != null ? (String) spec.getId() : Utils.newRandom())
				
				.setName(spec.getTitle())
				.setSection(sectionId)
				.setInputType(spec.getInputType().toString())
				
				.setSortOrder(spec.getSortOrder())
				.setDefaultValue(spec.getDefaultValue())
				.setIsRequired(spec.getIsRequired())
				.setIsVisible(spec.getIsVisible())
				.setIsDefault(isDefault ? true : null)
				
				.setDateCreated(new Date());
		
		return o;
	}
	
	/////////////////	FormSimpleFieldEntity   ///////////////////
	
	
	
	
	
	
	/////////////////	BaseUserEntity   ///////////////////
	
	public static UserProfileSpec toObjectModel(BaseUserEntity entity) {
	
		UserProfileSpec o = new UserProfileSpec()
				
				.setEmail(entity.getEmail())
				 //.setPassword(entity.getPassword())
				.setFirstName(entity.getFirstName())
				.setMiddleName(entity.getMiddleName())
				.setLastName(entity.getLastName())
				.setImage(entity.getImage())
				.setPhone(entity.getPhone())
				.setDateOfBirth(entity.getDateOfBirth())
				.setGender(Gender.from(entity.getGender()))
				.setAddress(entity.getAddress())
				.setCity(entity.getCity())
				.setTerritory(entity.getTerritory())
				.setCountry(entity.getCountry());
		
		return o;
	}
	
	public static BaseUserEntity fromObjectModel(String role, Long principal, UserProfileSpec spec) {

		BaseUserEntity o = new BaseUserEntity()
				
				.setEmail(spec.getEmail())
				.setPassword(spec.getPassword())
				.setFirstName(spec.getFirstName())
				.setMiddleName(spec.getMiddleName())
				.setLastName(spec.getLastName())
				.setImage(spec.getImage())
				.setPhone(spec.getPhone())
				.setDateOfBirth(spec.getDateOfBirth())
				.setGender(spec.getGender().getValue())
				.setAddress(spec.getAddress())
				.setCity(spec.getCity())
				.setTerritory(spec.getTerritory())
				.setCountry(spec.getCountry())
				.setRole(role)
				.setPrincipal(principal)
				.setDateCreated(new Date());
		
		return o;
		
	}
	
	/////////////////	BaseUserEntity   ///////////////////
	
	
	
	
	
	/////////////////	DepartmentEntity   ///////////////////
	
	public static DepartmentSpec toObjectModel(DepartmentEntity entity) {
	
		DepartmentSpec o = new DepartmentSpec()
				
				.setName(entity.getName())
				.setFaculty(entity.getFaculty())
				.setHeadOfDepartment(entity.getHeadOfDepartment())
				.setDuration(entity.getDuration())
				.setIsAccredited(entity.getIsAccredited());
		
		return o;
	}
	
	public static DepartmentEntity fromObjectModel(DepartmentSpec spec) {

		DepartmentEntity o = new DepartmentEntity()
				
				.setName(spec.getName())
				.setFaculty(spec.getFaculty())
				.setHeadOfDepartment(spec.getHeadOfDepartment())
				.setDuration(spec.getDuration())
				.setIsAccredited(spec.getIsAccredited());
		
		return o;
	}
	
	/////////////////	DepartmentEntity   ///////////////////
	
	
	
	
	
	
	/////////////////	FacultyEntity   ///////////////////
	
	public static FacultySpec toObjectModel(FacultyEntity entity) {
	
		FacultySpec o = new FacultySpec()
				.setName(entity.getName());
		
		return o;
	}
	
	public static FacultyEntity fromObjectModel(FacultySpec spec) {

		FacultyEntity o = new FacultyEntity()	
				.setName(spec.getName());
		
		return o;
	}
	
	/////////////////	FacultyEntity   ///////////////////
	
	
	
	
	
	
	/////////////////	CourseEntity   ///////////////////
	
	public static CourseSpec toObjectModel(CourseEntity entity) {
	
		CourseSpec o = new CourseSpec()
				.setCode(entity.getCode())
				.setName(entity.getName())
				.setPoint(entity.getPoint())
				.setSemester(Semester.from(entity.getSemester()))
				.setDepartmentLevels(entity.getDepartmentLevels())
				.setLecturers(entity.getLecturers());
		
		return o;
	}
	
	public static CourseEntity fromObjectModel(CourseSpec spec) {

		CourseEntity o = new CourseEntity()	
				.setCode(spec.getCode())
				.setName(spec.getName())
				.setPoint(spec.getPoint())
				.setSemester(spec.getSemester().getValue())
				.setDepartmentLevels(spec.getDepartmentLevels())
				.setLecturers(spec.getLecturers());
		
		return o;
	}
	
	/////////////////	CourseEntity   ///////////////////
	

	
	
	
	
	/////////////////	AcademicSemesterEntity   ///////////////////
	
	public static AcademicSemesterSpec toObjectModel(AcademicSemesterEntity entity) {
	
		AcademicSemesterSpec o = new AcademicSemesterSpec()
				.setLowerYearBound(entity.getLowerYearBound())
				.setUpperYearBound(entity.getUpperYearBound())
				.setValue(Semester.from(entity.getValue()))
				.setStartDate(entity.getStartDate())
				.setEndDate(entity.getEndDate());
		
		return o;
	}
	
	public static AcademicSemesterEntity fromObjectModel(AcademicSemesterSpec spec) {

		AcademicSemesterEntity o = new AcademicSemesterEntity()
				
				.setLowerYearBound(spec.getLowerYearBound())
				.setUpperYearBound(spec.getUpperYearBound())
				.setValue(spec.getValue().getValue())
				.setStartDate(spec.getStartDate())
				.setEndDate(spec.getEndDate());
		
		return o;
	}
	
	/////////////////	AcademicSemesterEntity   ///////////////////
	
	
	
	
	
	
	
	/////////////////	LevelSemesterEntity   ///////////////////
	
	public static LevelSemesterSpec toObjectModel(LevelSemesterEntity entity) {
	
		LevelSemesterSpec o = new LevelSemesterSpec()
				.setId(entity.getId())
				.setDepartmentLevel(entity.getDepartmentLevel())
				.setSemester(Semester.from(entity.getSemester()))
				.setCourses(entity.getCourses());
		return o;
	}
	
	public static LevelSemesterEntity fromObjectModel(LevelSemesterSpec spec) {

		LevelSemesterEntity o = new LevelSemesterEntity()
				.setDepartmentLevel(spec.getDepartmentLevel())
				.setSemester(spec.getSemester().getValue())
				.setCourses(spec.getCourses())
				.setDateCreated(new Date());
		
		return o;
	}
	
	/////////////////	LevelSemesterEntity   ///////////////////
	
	
	
	
	

	
	/////////////////	AssessmentTotalEntity   ///////////////////
	
	public static AssessmentTotalSpec toObjectModel(AssessmentTotalEntity entity) {
	
		AssessmentTotalSpec o = new AssessmentTotalSpec()
				.setName(entity.getName())
				.setPercentile(entity.getPercentile())
				.setType(AssessmentTotalType.from(entity.getType()))
				.setIsValidated(entity.getIsValidated());
		
		return o;
	}
	
	public static AssessmentTotalEntity fromObjectModel(AssessmentTotalSpec spec) {

		AssessmentTotalEntity o = new AssessmentTotalEntity()
				.setName(spec.getName())
				.setType(spec.getType().getValue())
				.setPercentile(spec.getPercentile())
				.setIsValidated(spec.getIsValidated())
				.setDateCreated(new Date());
		
		return o;
	}
	
	/////////////////	AssessmentTotalEntity   ///////////////////
	
	
	
	
	
	
	
	
	/////////////////	StudentEntity   ///////////////////
	
	public static StudentSpec toObjectModel(StudentEntity entity) {
	
		StudentSpec o = new StudentSpec()
				.setId(entity.getId())
				.setMatricNumber(entity.getMatricNumber())
				.setJambRegNo(entity.getJambRegNo())
				.setDepartmentLevel(entity.getDepartmentLevel());
		return o;
	}
	
	public static StudentEntity fromObjectModel(StudentSpec spec) {

		StudentEntity o = new StudentEntity()
				.setMatricNumber(spec.getMatricNumber())
				.setJambRegNo(spec.getJambRegNo())
				.setDepartmentLevel(spec.getDepartmentLevel());
		return o;
	}
	
	/////////////////	StudentEntity   ///////////////////
	
	
	
	
	
	/////////////////	LecturerEntity   ///////////////////
	
	public static LecturerSpec toObjectModel(LecturerEntity entity) {
	
		LecturerSpec o = new LecturerSpec()
							.setId(entity.getId())
							.setDepartment(entity.getDepartment())
							.setCourses(entity.getCourses())
							.setStartDate(entity.getStartDate());
		return o;
	}
	
	public static LecturerEntity fromObjectModel(LecturerSpec spec) {

		LecturerEntity o = new LecturerEntity()
				.setId(spec.getId())
				.setDepartment(spec.getDepartment())
				.setCourses(spec.getCourses())
				.setStartDate(spec.getStartDate());
		return o;
	}
	
	/////////////////	LecturerEntity   ///////////////////
	
	
	
	
	
	/////////////////	DepartmentalHeadEntity   ///////////////////
	
	public static DepartmentalHeadSpec toObjectModel(DepartmentalHeadEntity entity) {
	
		DepartmentalHeadSpec o = new DepartmentalHeadSpec()
							.setId(entity.getId())
							.setDepartment(entity.getDepartment())
							.setStartDate(entity.getStartDate());
		return o;
	}
	
	public static DepartmentalHeadEntity fromObjectModel(DepartmentalHeadSpec spec) {

		DepartmentalHeadEntity o = new DepartmentalHeadEntity()
				.setId(spec.getId())
				.setDepartment(spec.getDepartment())
				.setStartDate(spec.getStartDate());
		return o;
	}
	
	/////////////////	DepartmentalHeadEntity   ///////////////////
	
	
	
	
	/////////////////	FacultyDeanEntity   ///////////////////
	
	public static FacultyDeanSpec toObjectModel(FacultyDeanEntity entity) {
	
		FacultyDeanSpec o = new FacultyDeanSpec()
							.setId(entity.getId())
							.setFaculty(entity.getFaculty())
							.setStartDate(entity.getStartDate());
		return o;
	}
	
	public static FacultyDeanEntity fromObjectModel(FacultyDeanSpec spec) {

		FacultyDeanEntity o = new FacultyDeanEntity()
				.setId(spec.getId())
				.setFaculty(spec.getFaculty())
				.setStartDate(spec.getStartDate());
		return o;
	}
	
	/////////////////	FacultyDeanEntity   ///////////////////
	
	
	
	
	
	
	
	
	
	/////////////////	StudentSemesterCoursesEntity   ///////////////////
	
	public static StudentSemesterCoursesSpec toObjectModel(StudentSemesterCoursesEntity entity) {
	
		StudentSemesterCoursesSpec o = new StudentSemesterCoursesSpec()
										.setId(entity.getId())
										.setStudentId(entity.getStudentId())
										.setAcademicSemesterId(entity.getAcademicSemesterId())
										.setCourses(entity.getCourses())
										.setDateCreated(entity.getDateCreated());
		return o;
	}
	
	public static StudentSemesterCoursesEntity fromObjectModel(StudentSemesterCoursesSpec spec) {

		StudentSemesterCoursesEntity o = new StudentSemesterCoursesEntity()
				.setStudentId(spec.getStudentId())
				.setAcademicSemesterId(spec.getAcademicSemesterId())
				.setCourses(spec.getCourses())
				.setDateCreated(new Date());
		return o;
	}
	
	/////////////////	StudentSemesterCoursesEntity   ///////////////////
	
	
	
	/////////////////	AcademicSemesterCourseEntity   ///////////////////
	
	public static AcademicSemesterCourseSpec toObjectModel(AcademicSemesterCourseEntity entity) {
	
		AcademicSemesterCourseSpec o = new AcademicSemesterCourseSpec()
				.setId(entity.getId())
				.setAcademicSemesterId(entity.getAcademicSemesterId())
				.setCourseCode(entity.getCourseCode())
				.setIsSheetCreated(entity.getIsSheetCreated())
				.setDateSheetCreated(entity.getDateSheetCreated())
				.setIsSheetFinal(entity.getIsSheetFinal())
				.setDateSheetFinal(entity.getDateSheetFinal())
				.setTotals(entity.getTotals())
				.setStudents(entity.getStudents());
										
		return o;
	}
	
	public static AcademicSemesterCourseEntity fromObjectModel(AcademicSemesterCourseSpec spec) {

		AcademicSemesterCourseEntity o = new AcademicSemesterCourseEntity()
				.setAcademicSemesterId(spec.getAcademicSemesterId())
				.setCourseCode(spec.getCourseCode())
				.setIsSheetCreated(spec.getIsSheetCreated())
				.setDateSheetCreated(spec.getDateSheetCreated())
				.setIsSheetFinal(spec.getIsSheetFinal())
				.setDateSheetFinal(spec.getDateSheetFinal())
				.setTotals(spec.getTotals())
				.setStudents(spec.getStudents());
		return o;
	}
	
	/////////////////	AcademicSemesterCourseEntity   ///////////////////
	
	
	

	

	/////////////////	CourseResultReportEntity   ///////////////////
	
	public static CourseResultReportSpec toObjectModel(CourseResultReportEntity entity) {
	
		CourseResultReportSpec o = new CourseResultReportSpec()
				.setAcademicSemesterCourseId(entity.getAcademicSemesterCourseId())
				.setBlobId(entity.getBlobId())
				.setCreatedBy(entity.getCreatedBy())
				.setDateCreated(entity.getDateCreated());
		return o;
	}
	
	public static CourseResultReportEntity fromObjectModel(CourseResultReportSpec spec) {

		CourseResultReportEntity o = new CourseResultReportEntity()
				.setAcademicSemesterCourseId(spec.getAcademicSemesterCourseId())
				.setBlobId(spec.getBlobId())
				.setCreatedBy(spec.getCreatedBy())
				.setDateCreated(spec.getDateCreated());
		return o;
	}
	
	/////////////////	CourseResultReportEntity   ///////////////////
	
	
	
	/////////////////	StudentResultReportEntity   ///////////////////
	
	public static StudentResultReportSpec toObjectModel(StudentResultReportEntity entity) {
	
		StudentResultReportSpec o = new StudentResultReportSpec()
				.setId(entity.getId())
				.setStudentId(entity.getStudentId())
				.setAcademicSemesterId(entity.getAcademicSemesterId())
				.setBlobId(entity.getBlobId())
				.setDateCreated(entity.getDateCreated());
		
		return o;
	}
	
	public static StudentResultReportEntity fromObjectModel(StudentResultReportSpec spec) {

		StudentResultReportEntity o = new StudentResultReportEntity()
				.setStudentId(spec.getStudentId())
				.setAcademicSemesterId(spec.getAcademicSemesterId())
				.setBlobId(spec.getBlobId())
				.setDateCreated(new Date());
		return o;
	}
	
	/////////////////	StudentResultReportEntity   ///////////////////
	
	
	
	/////////////////	BlobEntity   ///////////////////
	
	public static BlobSpec toObjectModel(BlobEntity entity) {
		
		BlobSpec o = new BlobSpec()
				.setId(entity.getId())
				.setData(entity.getData())
				.setSize(entity.getSize())
				.setMimeType(entity.getMimeType())
				.setDateCreated(entity.getDateCreated());
		
		return o;
	}
	
	public static BlobEntity fromObjectModel(BlobSpec spec) {

		BlobEntity o = new BlobEntity()
				.setId(spec.getId())
				.setData(spec.getData())
				.setSize(spec.getSize())
				.setMimeType(spec.getMimeType())
				.setDateCreated(spec.getDateCreated());
		
		return o;
	}
	
	/////////////////	BlobEntity   ///////////////////	
	
	
	
	
	
	
	
	/////////////////	PersonNameEntity   ///////////////////
	
	public static IndexedNameSpec toObjectModel(IndexedNameEntity entity) {
		
		IndexedNameSpec o = new IndexedNameSpec()
				.setKey(entity.getEntityId())
				.setX(entity.getX())
				.setY(entity.getY())
				.setZ(entity.getZ());
		
		return o;
	}

	/////////////////	PersonNameEntity   ///////////////////	

	
	
	
	/////////////////	ResultRecordSheetEntity   ///////////////////	
	
	public static ResultRecordSheetSpec toObjectModel(ResultRecordSheetEntity v) {
	
		ResultRecordSheetSpec spec = new ResultRecordSheetSpec()
			.setId(v.getId())
			.setStudentId(v.getStudentId())
			.setScores(v.getScores())
			.setTotal(v.getTotal())
			.setLastUpdated(v.getLastUpdated())
			.setLastUpdatedBy(v.getLastUpdatedBy());
	
		return spec;
	}
	
	/////////////////	ResultRecordSheetEntity   ///////////////////	
	
	
	
	
	
	

	/////////////////	ActivityEntity   ///////////////////
	
	public static ActivitySpec toObjectModel(ActivityEntity entity) {
		
		ActivitySpec o = new ActivitySpec()
				.setId(entity.getId())
				.setText(entity.getText())
				.setImage(entity.getImage())
				.setDate(entity.getDate());
		
		return o;
	}
	
	public static ActivityEntity fromObjectModel(ActivitySpec spec) {

		ActivityEntity o = new ActivityEntity()
				.setId(spec.getId())
				.setText(spec.getText())
				.setImage(spec.getImage())
				.setDate(new Date());
		
		return o;
	}
	
	/////////////////	ActivityEntity   ///////////////////	
	
	
}
