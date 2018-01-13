package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.api.event_streams.Article;
import com.ce.ems.base.api.event_streams.CustomPredicate;
import com.ce.ems.base.api.event_streams.ObjectEntity;
import com.ce.ems.base.api.event_streams.ObjectType;
import com.ce.ems.base.api.event_streams.Preposition;
import com.ce.ems.base.api.event_streams.Sentence;
import com.ce.ems.base.api.event_streams.SubjectEntity;
import com.ce.ems.base.api.event_streams.SubjectType;
import com.ce.ems.base.classes.AssessmentTotalType;
import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.IndexedNameSpec;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.IntegerWrapper;
import com.ce.ems.base.classes.ListUpdate;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.SystemErrorCodes;
import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.CourseSpec;
import com.ce.ems.base.classes.spec.DepartmentLevelSpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.classes.spec.DepartmentalHeadSpec;
import com.ce.ems.base.classes.spec.FacultyDeanSpec;
import com.ce.ems.base.classes.spec.FacultySpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.base.core.BlockerBlockerTodo;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.SystemValidationException;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.calculation.AcademicSemesterCourseEntity;
import com.ce.ems.entites.calculation.StudentSemesterCoursesEntity;
import com.ce.ems.entites.directory.AcademicSemesterEntity;
import com.ce.ems.entites.directory.CourseEntity;
import com.ce.ems.entites.directory.DepartmentEntity;
import com.ce.ems.entites.directory.DepartmentalHeadEntity;
import com.ce.ems.entites.directory.DepartmentalLevelEntity;
import com.ce.ems.entites.directory.DepartmentalLevelStudentsEntity;
import com.ce.ems.entites.directory.FacultyDeanEntity;
import com.ce.ems.entites.directory.FacultyEntity;
import com.ce.ems.entites.directory.LecturerEntity;
import com.ce.ems.entites.directory.LevelSemesterEntity;
import com.ce.ems.entites.directory.StudentEntity;
import com.kylantis.eaa.core.fusion.Unexposed;
import com.kylantis.eaa.core.keys.ConfigKeys;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;

@BlockerTodo("Add functionality that enable principals to update user department levels. It happens frequently in real life")
public class DirectoryModel extends BaseModel {

	@Override
	public String path() {
		return "core/directory";
	}

	@Override
	public void preInstall() {
	}

	@Override
	public void install(InstallOptions options) {

		// Create new academic semester
		newAcademicSemester(-1l, options.getAcademicSemester());
	}

	@BlockerTodo("Add activity stream. On starting new semester, we need to scan all student data, and "
			+ "update their levels if applicable")
	@BlockerBlockerTodo("On starting  new semster, all assessment totals need to be updated, i.e isValidated = true")
	@ModelMethod(functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public static Long newAcademicSemester(Long principal, AcademicSemesterSpec spec) {

		// create entity

		AcademicSemesterEntity e = EntityHelper.fromObjectModel(spec);
		ofy().save().entity(e).now();

		if (spec.getValue().equals(Semester.FIRST)) {
			// DepartmentalLevelEntity
			// StudentEntity
			// we need to scan all student data, and update their levels if applicable
		}

		createAcademicSemesterCourse(e.getId(), listCourseKeys(spec.getValue()));

		ConfigModel.put(ConfigKeys.CURRENT_SEMESTER, e.getId());

		return e.getId();
	}

	protected static void createAcademicSemesterCourse(Long academicSemesterId, List<String> courses) {

		List<AcademicSemesterCourseEntity> entities = new ArrayList<AcademicSemesterCourseEntity>();

		courses.forEach(course -> {

			AcademicSemesterCourseEntity o = new AcademicSemesterCourseEntity()
					.setAcademicSemesterId(academicSemesterId).setCourseCode(course).setIsSheetCreated(false)
					.setIsSheetFinal(false).setTotals(null).setStudents(new ArrayList<>()).setDateUpdated(new Date());

			entities.add(o);
		});

		ofy().save().entities(entities).now();
	}

	protected static Long currentSemesterId() {
		return Long.parseLong(ConfigModel.get(ConfigKeys.CURRENT_SEMESTER));
	}

	@BlockerTodo("Add activity stream")
	@ModelMethod(functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public static void endSemester() {

		// set end date for current_semester

		// set current_semester config to null

		// compute metrics

		// Save
	}

	@ModelMethod(functionality = Functionality.GET_CURRENT_SEMESTER)
	public static AcademicSemesterSpec getCurrentAcademicSemester() {

		Long id = currentSemesterId();
		AcademicSemesterEntity e = ofy().load().type(AcademicSemesterEntity.class).id(id).safe();

		return EntityHelper.toObjectModel(e);
	}

	@ModelMethod(functionality = Functionality.LIST_ACADEMIC_SEMESTER)
	public static List<AcademicSemesterSpec> listAcademicSemester() {

		List<AcademicSemesterSpec> result = new FluentArrayList<>();

		ofy().load().type(AcademicSemesterEntity.class).forEach(e -> {
			result.add(EntityHelper.toObjectModel(e));
		});

		return result;
	}

	protected static Integer getAcademicSemesterValue(Long id) {
		AcademicSemesterEntity e = ofy().load().type(AcademicSemesterEntity.class).id(id).safe();
		return e.getValue();
	}

	@ModelMethod(functionality = Functionality.MANAGE_DEPARTMENTS)
	@Todo("Use batch saves in this method. Recursively calling newAssessmentTotal(..) is too expensive")
	public static Long createDepartment(Long principal, DepartmentSpec spec) {

		DepartmentEntity entity = EntityHelper.fromObjectModel(spec);
		ofy().save().entity(entity).now();

		// Save department levels

		List<Long> levels = new FluentArrayList<>();

		for (int i = 100; i < ((spec.getDuration() * 100) + 1); i += 100) {

			DepartmentalLevelEntity e = new DepartmentalLevelEntity().setDepartment(entity.getId()).setLevel(i);
			ofy().save().entity(e).now();

			levels.add(e.getId());

			DepartmentalLevelStudentsEntity ls = new DepartmentalLevelStudentsEntity().setId(e.getId())
					.setStudents(new ArrayList<>());
			ofy().save().entity(ls).now();

			// Save level semesters

			for (Semester semester : Semester.values()) {

				LevelSemesterEntity se = new LevelSemesterEntity().setDepartmentLevel(e.getId())
						.setSemester(semester.getValue()).setCourses(new ArrayList<>()).setDateCreated(new Date());

				ofy().save().entity(se).now();

				// Save default assessment totals

				CalculationModel.newAssessmentTotal(principal, new AssessmentTotalSpec().setName("CA 1")
						.setPercentile(10).setType(AssessmentTotalType.ASSESSMENT).setLevelSemester(se.getId()));

				CalculationModel.newAssessmentTotal(principal, new AssessmentTotalSpec().setName("CA 2")
						.setPercentile(10).setType(AssessmentTotalType.ASSESSMENT).setLevelSemester(se.getId()));

				CalculationModel.newAssessmentTotal(principal, new AssessmentTotalSpec().setName("Exam")
						.setPercentile(80).setType(AssessmentTotalType.EXAM).setLevelSemester(se.getId()));

			}
		}

		entity.setLevels(levels);
		ofy().save().entity(entity).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.CREATED).setObject(ObjectEntity.get(ObjectType.DEPARTMENT)
						.setIdentifiers(FluentArrayList.asList(entity.getId())).setName(spec.getName()).addQualifier());
		ActivityStreamModel.newActivity(BaseUserModel.getAvatar(principal), activity);

		return entity.getId();
	}

	@ModelMethod(functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public static DepartmentSpec getDepartment(Long id) {
		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(id).safe();
		return EntityHelper.toObjectModel(e);
	}

	protected static String getDepartmentName(Long id) {
		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(id).safe();
		return e.getName();
	}

	@ModelMethod(functionality = Functionality.LIST_DEPARTMENT_LEVELS)
	public static Map<Long, String> listDepartmentLevels(Long departmentId) {

		Map<Long, String> result = new HashMap<>();

		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(departmentId).safe();
		
		ofy().load().type(DepartmentalLevelEntity.class).ids(e.getLevels()).forEach((k,v) -> {
			DepartmentLevelSpec spec = EntityHelper.toObjectModel(v);
			result.put(k, spec.toString());
		});
		
		return result;
	}

	protected static Semester getSemester(Long levelSemesterId) {
		return Semester.from(ofy().load().type(LevelSemesterEntity.class).id(levelSemesterId).safe().getSemester());
	}

	protected static DepartmentLevelSpec getDepartmentLevel(Long levelSemesterId) {
		Long departmentLevelId = ofy().load().type(LevelSemesterEntity.class).id(levelSemesterId).safe()
				.getDepartmentLevel();
		DepartmentalLevelEntity departmentLevel = ofy().load().type(DepartmentalLevelEntity.class).id(departmentLevelId)
				.safe();
		return EntityHelper.toObjectModel(departmentLevel);
	}

	protected static Long getDepartmentLevel(Long departmentId, Integer level) {

		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(departmentId).safe();

		for (Long l : e.getLevels()) {
			DepartmentalLevelEntity le = ofy().load().type(DepartmentalLevelEntity.class).id(l).safe();
			if (le.getLevel().equals(level)) {
				return le.getId();
			}
		}
		return null;
	}

	@ModelMethod(functionality = Functionality.LIST_DEPARTMENT_NAMES)
	public static Map<Long, String> listDepartmentNames(Long facultyId) {
		Map<Long, String> result = new FluentHashMap<>();
		ofy().load().type(DepartmentEntity.class).filter("faculty =", facultyId.toString()).forEach(e -> {
			result.put(e.getId(), e.getName());
		});
		return result;
	}

	@ModelMethod(functionality = Functionality.LIST_DEPARTMENT_NAMES)
	public static Map<Long, String> listDepartmentNames() {
		Map<Long, String> result = new FluentHashMap<>();
		ofy().load().type(DepartmentEntity.class).forEach(e -> {
			result.put(e.getId(), e.getName());
		});
		return result;
	}

	@ModelMethod(functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public static List<DepartmentSpec> listDepartments(Long facultyId) {
		List<DepartmentSpec> result = new FluentArrayList<>();
		ofy().load().type(DepartmentEntity.class).filter("faculty =", facultyId.toString()).forEach(e -> {
			result.add(EntityHelper.toObjectModel(e));
		});
		return result;
	}

	protected static void addStudentToDepartmentLevel(ListUpdate u, Long departmentLevelId, Long studentId) {

		DepartmentalLevelStudentsEntity e = ofy().load().type(DepartmentalLevelStudentsEntity.class)
				.id(departmentLevelId).safe();

		if (u.equals(ListUpdate.ADD)) {
			e.addStudent(studentId);
		} else {
			e.removeStudent(studentId);
		}

		ofy().save().entity(e).now();
	}

	@ModelMethod(functionality = Functionality.MANAGE_FACULTIES)
	public static Long createFaculty(Long principal, FacultySpec spec) {

		FacultyEntity e = new FacultyEntity().setName(spec.getName());
		ofy().save().entity(e).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.CREATED).setObject(ObjectEntity.get(ObjectType.FACULTY)
						.setIdentifiers(FluentArrayList.asList(e.getId())).setName(spec.getName()).addQualifier());
		ActivityStreamModel.newActivity(BaseUserModel.getAvatar(principal), activity);

		return e.getId();
	}

	@ModelMethod(functionality = Functionality.VIEW_FACULTY_PROFILES)
	public static FacultySpec getFaculty(Long id) {
		FacultyEntity e = ofy().load().type(FacultyEntity.class).id(id).safe();
		return EntityHelper.toObjectModel(e);
	}
	
	@ModelMethod(functionality = Functionality.VIEW_FACULTY_PROFILES)
	public static List<FacultySpec> listFaculties() {
		List<FacultySpec> result = new FluentArrayList<>();
		ofy().load().type(FacultyEntity.class).forEach(e -> {
			result.add(EntityHelper.toObjectModel(e));
		});
		return result;
	}

	@ModelMethod(functionality = Functionality.LIST_FACULTY_NAMES)
	public static Map<Long, String> listFacultyNames() {
		Map<Long, String> result = new FluentHashMap<>();
		ofy().load().type(FacultyEntity.class).forEach(e -> {
			result.put(e.getId(), e.getName());
		});
		return result;
	}

	@Todo("make method protected")
	public static void createStudent(Long id, StudentSpec spec) {

		StudentEntity e = EntityHelper.fromObjectModel(spec).setId(id).setCgpa(0.0d);
		ofy().save().entity(e).now();

		// increase student count for current level
		addStudentToDepartmentLevel(ListUpdate.ADD, spec.getDepartmentLevel(), e.getId());
	}

	@Unexposed
	@BlockerTodo("Impl, add to act. stream")
	@ModelMethod(functionality = Functionality.MANAGE_STUDENT_PROFILES)
	public static void deleteStudent(Long id) {

		// Go through student records and consolidate ..

		ofy().delete().type(StudentEntity.class).id(id).now();

		BaseUserModel.deleteUser(id);
	}

	@ModelMethod(functionality = Functionality.VIEW_STUDENT_PROFILES)
	public static StudentSpec getStudent(Long id) {
		StudentEntity e = ofy().load().type(StudentEntity.class).id(id).safe();
		return EntityHelper.toObjectModel(e);
	}

	protected static String getStudentMatricNumber(Long id) {
		StudentEntity e = ofy().load().type(StudentEntity.class).id(id).safe();
		return e.getMatricNumber();
	}

	@ModelMethod(functionality = Functionality.VIEW_STUDENT_PROFILES)
	public static List<StudentSpec> getStudents(Long departmentLevelId) {

		DepartmentalLevelStudentsEntity e = ofy().load().type(DepartmentalLevelStudentsEntity.class)
				.id(departmentLevelId).safe();

		List<StudentSpec> result = new ArrayList<>();

		ofy().load().type(StudentEntity.class).ids(e.getStudents()).forEach((k, v) -> {
			StudentSpec spec = EntityHelper.toObjectModel(v).setName(BaseUserModel.getPersonName(k, true).toString());
			result.add(spec);
		});

		return result;
	}

	@Todo("make method protected")
	public static void createLecturer(Long id, LecturerSpec spec) {

		LecturerEntity e = EntityHelper.fromObjectModel(spec).setId(id).setCourses(new ArrayList<>());
		ofy().save().entity(e).now();

		// add to lecturers field in Department
		DepartmentEntity dpt = ofy().load().type(DepartmentEntity.class).id(spec.getDepartment()).safe()
				.addLecturer(e.getId());
		ofy().save().entity(dpt).now();

		e.getCourses().forEach(c -> {
			CourseEntity ce = ofy().load().type(CourseEntity.class).id(c).safe();
			ce.addLecturer(e.getId());
			ofy().save().entity(ce);
		});
	}

	@ModelMethod(functionality = Functionality.VIEW_LECTURER_PROFILES)
	public static LecturerSpec getLecturer(long id) {
		LecturerEntity e = ofy().load().type(LecturerEntity.class).id(id).safe();
		return EntityHelper.toObjectModel(e);
	}

	@ModelMethod(functionality = Functionality.VIEW_LECTURER_PROFILES)
	public static List<Long> getLecturers(long departmentId) {
		return ofy().load().type(DepartmentEntity.class).id(departmentId).safe().getLecturers();
	}

	/**
	 * This gets the courses that are assigned to the lecturer, applicable to the
	 * current semester
	 */
	@BlockerTodo("See comments in method")
	@ModelMethod(functionality = Functionality.MANAGE_COURSE_SCORE_SHEET)
	public static Map<String, AcademicSemesterCourseSpec> getAcademicSemesterCourses(long principal) {

		// boolean canAccessAll =
		// RoleModel.isAccessAllowed(BaseUserModel.getRole(principal),
		// Functionality.VIEW_ALL_SEMESTER_COURSES);

		boolean isAdmin = RoleModel.getRealm(BaseUserModel.getRole(principal)).equals(RoleRealm.ADMIN);

		if (isAdmin) {
			// @BlockerTodo
			// This should not happen normally. In this case, AcademicSemesterList listable
			// should be used on the client
			// This is extremely inappropriate
			return CalculationModel.getAcademicSemesterCourses(currentSemesterId());
		} else {
			LecturerEntity e = ofy().load().type(LecturerEntity.class).id(principal).safe();
			return CalculationModel.getAcademicSemesterCourses(currentSemesterId(), e.getCourses());
		}
	}

	@ModelMethod(functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public static void addLecturerCourses(Long principal, Long lecturerId, List<String> courses) {
		LecturerEntity e = ofy().load().type(LecturerEntity.class).id(lecturerId).safe();
		e.addCourses(courses);
		ofy().save().entity(e).now();

		// Add to activity stream

		courses.forEach(course -> {

			Sentence activity = Sentence.newInstance()
					.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
					.setPredicate(CustomPredicate.ASSIGNED)

					.setObject(ObjectEntity.get(ObjectType.COURSE).setIdentifiers(FluentArrayList.asList(course))
							.setName(course))
					.withPreposition(Preposition.TO,
							SubjectEntity.get(SubjectType.LECTURER).setIdentifiers(FluentArrayList.asList(lecturerId)));

			ActivityStreamModel.newActivity(BaseUserModel.getAvatar(principal), activity);
		});

	}

	@ModelMethod(functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public static void removeCourses(Long principal, Long lecturerId, String course) {

		LecturerEntity e = ofy().load().type(LecturerEntity.class).id(lecturerId).safe();
		e.removeCourse(course);
		ofy().save().entity(e).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.UNASSIGNED)

				.setObject(ObjectEntity.get(ObjectType.COURSE).setIdentifiers(FluentArrayList.asList(course))
						.setName(course))
				.withPreposition(Preposition.FROM,
						SubjectEntity.get(SubjectType.LECTURER).setIdentifiers(FluentArrayList.asList(lecturerId)));

		ActivityStreamModel.newActivity(BaseUserModel.getAvatar(principal), activity);
	}

	@Unexposed
	@Todo("Add act. stream")
	@ModelMethod(functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public static void deleteLecturer(long id) {

		long departmentId = ofy().load().type(LecturerEntity.class).id(id).safe().getDepartment();

		ofy().delete().type(LecturerEntity.class).id(id).now();

		// delete from lecturers field in Department
		DepartmentEntity dpt = ofy().load().type(DepartmentEntity.class).id(departmentId).safe().removeLecturer(id);
		ofy().save().entity(dpt).now();

		// Consolidate associated records

	}

	@Todo("make method protected")
	public static void createDepartmentalHead(Long id, DepartmentalHeadSpec spec) {

		DepartmentEntity dpt = ofy().load().type(DepartmentEntity.class).id(spec.getDepartment()).safe();

		if (dpt.getHeadOfDepartment() != null) {
			throw new SystemValidationException(SystemErrorCodes.DEPARTMENTAL_HEAD_ALREADY_EXISTS);
		}

		DepartmentalHeadEntity e = EntityHelper.fromObjectModel(spec).setId(id);
		ofy().save().entity(e).now();

		ofy().save().entity(dpt.setHeadOfDepartment(e.getId())).now();
	}

	// @ModelMethod(functionality = Functionality.MANAGE_DEPARTMENTAL_HEAD_PROFILES)
	protected static void appointDepartmentalHead(Long userId, long departmentId) {
	}

	@Todo("make method protected")
	public static void createFacultyDean(Long id, FacultyDeanSpec spec) {

		FacultyEntity dpt = ofy().load().type(FacultyEntity.class).id(spec.getFaculty()).safe();

		if (dpt.getDean() != null) {
			throw new SystemValidationException(SystemErrorCodes.FACULTY_DEAN_ALREADY_EXISTS);
		}

		FacultyDeanEntity e = EntityHelper.fromObjectModel(spec).setId(id);
		ofy().save().entity(e).now();

		ofy().save().entity(dpt.setDean(e.getId())).now();
	}

	// @ModelMethod(functionality = Functionality.MANAGE_FACULTY_DEAN_PROFILES)
	protected static void appointFacultyDean(Long userId, long facultyId) {
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSES)
	public static void createCourse(Long principal, CourseSpec spec) {

		CourseEntity e = EntityHelper.fromObjectModel(spec);
		ofy().save().entity(e).now();

		List<LevelSemesterEntity> lse = new ArrayList<>();

		e.getDepartmentLevels().forEach(l -> {

			LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
					.filter("departmentLevel = ", l.toString()).filter("semester = ", e.getSemester()).first().safe();

			ls.addCourse(e.getCode());

			lse.add(ls);
		});

		ofy().save().entities(lse).now();

		Long currentSemesterId = currentSemesterId();

		// Create semester course, if the semester of this course falls under current
		// academic semester
		if (getAcademicSemesterValue(currentSemesterId).equals(spec.getSemester().getValue())) {
			createAcademicSemesterCourse(currentSemesterId, new FluentArrayList<String>().with(spec.getCode()));
		}

		// Add to search index
		SearchModel.addIndexedName(new IndexedNameSpec(e.getCode(), e.getCode(), e.getName()), IndexedNameType.COURSE);

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.CREATED)

				.setObject(ObjectEntity.get(ObjectType.COURSE).setIdentifiers(FluentArrayList.asList(e.getCode()))
						.setName(spec.getName()).addQualifier());

		ActivityStreamModel.newActivity(BaseUserModel.getAvatar(principal), activity);

	}

	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static CourseSpec getCourse(String courseCode) {
		return EntityHelper.toObjectModel(ofy().load().type(CourseEntity.class).id(courseCode).safe());
	}

	public static String getCourseName(String courseCode) {
		return EntityHelper.toObjectModel(ofy().load().type(CourseEntity.class).id(courseCode).safe()).getName();
	}

	protected static Short getCourseUnitLoad(String courseCode) {
		return EntityHelper.toObjectModel(ofy().load().type(CourseEntity.class).id(courseCode).safe()).getPoint();
	}

	public static Boolean isCourseLecturer(Long principal, String courseCode) {
		return getCourse(courseCode).getLecturers().contains(principal);
	}

	protected static List<String> listCourseKeys(Semester semester) {
		List<String> result = new FluentArrayList<>();

		ofy().load().type(LevelSemesterEntity.class).filter("semester = ", semester.getValue()).forEach(ls -> {
			ls.getCourses().forEach(c -> {
				result.add(c);
			});
		});

		return result;
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static Map<Semester, List<CourseSpec>> listCoursesForSemesterLevel(Long departmentLevel, Semester semester) {
		List<CourseSpec> result = new FluentArrayList<>();

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", semester.getValue())
				.first().safe();

		ls.getCourses().forEach(c -> {
			result.add(DirectoryModel.getCourse(c));
		});

		return new FluentHashMap<Semester, List<CourseSpec>>().with(semester, result);
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static Map<Semester, List<CourseSpec>> listAllCoursesForLevel(Long departmentLevel) {

		Map<Semester, List<CourseSpec>> result = new FluentHashMap<>();

		for (Semester s : Semester.values()) {

			List<CourseSpec> courses = new FluentArrayList<>();

			LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
					.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", s.getValue())
					.first().safe();

			ls.getCourses().forEach(c -> {
				courses.add(DirectoryModel.getCourse(c));
			});

			result.put(s, courses);
		}

		return result;
	}

	// Not really needed
	@Unexposed
	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static List<CourseSpec> listSemesterCoursesForLevel(Long departmentLevel, Long academicSemesterId) {

		List<CourseSpec> courses = new FluentArrayList<>();

		Integer semester = getAcademicSemesterValue(academicSemesterId);

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", semester).first()
				.safe();

		ls.getCourses().forEach(c -> {
			courses.add(DirectoryModel.getCourse(c));
		});

		return courses;
	}

	protected static List<String> listCourseKeys(Long departmentLevel, Long academicSemesterId) {
		List<String> result = new FluentArrayList<>();

		Integer currentSemester = DirectoryModel.getAcademicSemesterValue(academicSemesterId);

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", currentSemester).first()
				.safe();

		ls.getCourses().forEach(c -> {
			result.add(c);
		});

		return result;
	}

	@Unexposed
	@BlockerTodo()
	@ModelMethod(functionality = Functionality.MANAGE_COURSES)
	public static void deleteCourse(Long principal, String courseCode) {

		// Consolidate

		ofy().delete().type(CourseEntity.class).id(courseCode).now();

		SearchModel.removeIndexedName(courseCode, IndexedNameType.COURSE);

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.DELETED)

				.setObject(ObjectEntity.get(ObjectType.COURSE).setIdentifiers(FluentArrayList.asList(courseCode))
						.setName(getCourseName(courseCode)));

		ActivityStreamModel.newActivity(BaseUserModel.getAvatar(principal), activity);
	}

	/**
	 * Courses that are available for registration by a student
	 */
	@ModelMethod(functionality = Functionality.REGISTER_STUDENT_COURSES)
	public static List<CourseSpec> listStudentCourses(Long studentId) {

		Long currentSemesterId = DirectoryModel.currentSemesterId();
		Semester currentSemester = Semester.from(DirectoryModel.getAcademicSemesterValue(currentSemesterId));

		long departmentLevel = ofy().load().type(StudentEntity.class).id(studentId).safe().getDepartmentLevel();

		return listCoursesForSemesterLevel(departmentLevel, currentSemester).get(currentSemester);
	}

	@ModelMethod(functionality = Functionality.REGISTER_STUDENT_COURSES)
	@BlockerTodo("Here, Verify that for course in <courses>, it is valid for user's department")
	public static void registerStudentCourses(Long studentId, List<String> courses) {

		// First, verify that student has not registered his semester courses yet
		if (ofy().load().type(StudentSemesterCoursesEntity.class).filter("studentId", studentId.toString()).chunkAll()
				.count() > 0) {
			throw new SystemValidationException(SystemErrorCodes.STUDENT_ALREADY_REGISTERED_COURSES_FOR_THIS_SEMESTER);
		}

		// Then, Verify that for course in <courses>, it is valid for user's department

		Long currentSemesterId = DirectoryModel.currentSemesterId();

		long departmentLevel = ofy().load().type(StudentEntity.class).id(studentId).safe().getDepartmentLevel();

		List<String> levelCourses = listCourseKeys(departmentLevel, currentSemesterId);

		List<String> carryoverCourses = new ArrayList<>(courses);
		carryoverCourses.removeIf(s -> {
			return levelCourses.contains(s);
		});

		// verify that unit load of carry-over courses do not exceed the stipulated
		// maximums
		if (!carryoverCourses.isEmpty()) {

			IntegerWrapper totalPoints = new IntegerWrapper();

			carryoverCourses.forEach(c -> {
				totalPoints.add(getCourseUnitLoad(c));
			});

			Integer maxCarryOverUnits = Integer.parseInt(ConfigModel.get(ConfigKeys.MAX_CARRY_OVER_UNIT_LOAD));

			if (totalPoints.getValue() > maxCarryOverUnits) {
				throw new SystemValidationException(SystemErrorCodes.MAX_CARRY_OVER_UNITS_EXCEEDED);
			}
		}

		courses.forEach(c -> {

			// verify that score sheets has not yet been created for this course

			if (CalculationModel.isSemesterCourseSheetCreated((long) -1, c)) {
				throw new SystemValidationException(SystemErrorCodes.COURSE_RESULT_SHEET_ALREADY_EXISTS, c);
			}
		});

		List<AcademicSemesterCourseEntity> se = new ArrayList<>();

		courses.forEach(c -> {

			AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
					.filter("academicSemesterId = ", currentSemesterId().toString()).filter("courseCode = ", c).first()
					.safe();

			e.addStudent(studentId);
			se.add(e);
		});

		ofy().save().entities(se).now();

		Map<String, Short> finalCourses = new HashMap<>();

		courses.forEach(c -> {
			finalCourses.put(c, (short) 0);
		});

		StudentSemesterCoursesEntity e = new StudentSemesterCoursesEntity().setStudentId(studentId)
				.setAcademicSemesterId(currentSemesterId()).setCourses(finalCourses).setDateCreated(new Date());

		ofy().save().entity(e).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.STUDENT).setIdentifiers(FluentArrayList.asList(studentId)))
				.setPredicate(CustomPredicate.REGISTERED).setObject(ObjectEntity.get(ObjectType.SEMESTER_COURSES)
						.setArticle(BaseUserModel.isMaleUser(studentId) ? Article.HIS : Article.HER));

		ActivityStreamModel.newActivity(BaseUserModel.getAvatar(studentId), activity);
	}

	public static String getCourseCode(Long academicSemesterCourseId) {
		return ofy().load().type(AcademicSemesterCourseEntity.class).id(academicSemesterCourseId.toString()).safe()
				.getCourseCode();
	}
}
