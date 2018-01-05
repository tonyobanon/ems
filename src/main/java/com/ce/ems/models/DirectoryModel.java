package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.CourseSpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.classes.spec.DepartmentalHeadSpec;
import com.ce.ems.base.classes.spec.FacultyDeanSpec;
import com.ce.ems.base.classes.spec.FacultySpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.SystemValidationException;
import com.ce.ems.entites.calculation.AcademicSemesterCourseEntity;
import com.ce.ems.entites.calculation.StudentSemesterCoursesEntity;
import com.ce.ems.entites.directory.AcademicSemesterEntity;
import com.ce.ems.entites.directory.CourseEntity;
import com.ce.ems.entites.directory.DepartmentEntity;
import com.ce.ems.entites.directory.DepartmentalHeadEntity;
import com.ce.ems.entites.directory.DepartmentalLevelEntity;
import com.ce.ems.entites.directory.FacultyDeanEntity;
import com.ce.ems.entites.directory.FacultyEntity;
import com.ce.ems.entites.directory.LecturerEntity;
import com.ce.ems.entites.directory.LevelSemesterEntity;
import com.ce.ems.entites.directory.StudentEntity;
import com.kylantis.eaa.core.fusion.Unexposed;
import com.kylantis.eaa.core.keys.ConfigKeys;
import com.kylantis.eaa.core.users.Functionality;

public class DirectoryModel extends BaseModel {

	@Override
	public String path() {
		return "core/directory";
	}
	
	@Override
	public void preInstall() {

		// create faculties

		//Logger.info("Creating faculties");

		// long facultyOfScience = createFaculty(new FacultySpec().setName("Faculty of
		// Science"));
		// long facultyOfHumanity = createFaculty(new FacultySpec().setName("Faculty of
		// Humanity"));

		// create department
		//Logger.info("Creating departments");
		//createDepartment(new DepartmentSpec());

		// create courses
		//createCourse(new CourseSpec());
	}

	@Override
	public void install(InstallOptions options) {

		// Create new academic semester
		newAcademicSemester(options.getAcademicSemester());

	}

	@BlockerTodo("On starting new semester, we need to scan all student data, and "
			+ "update their levels if applicable")

	@ModelMethod(functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public static Long newAcademicSemester(AcademicSemesterSpec spec) {

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
			
			AcademicSemesterCourseEntity o = new AcademicSemesterCourseEntity().setAcademicSemesterId(academicSemesterId)
					.setCourseCode(course).setIsSheetCreated(false).setIsSheetFinal(false).setTotals(null)
					.setStudents(new ArrayList<>());
			
			entities.add(o);
		});
		
		ofy().save().entities(entities).now();
	}

	protected static Long currentSemesterId() {
		return Long.parseLong(ConfigModel.get(ConfigKeys.CURRENT_SEMESTER));
	}

	@BlockerTodo
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
	public static Long createDepartment(DepartmentSpec spec) {

		DepartmentEntity entity = EntityHelper.fromObjectModel(spec);
		ofy().save().entity(entity).now();

		// Save department levels

		List<Long> levels = new FluentArrayList<>();

		for (int i = 100; i < ((spec.getDuration() * 100) + 1); i += 100) {

			DepartmentalLevelEntity e = new DepartmentalLevelEntity().setDepartment(entity.getId()).setLevel(i)
					.setStudents(new FluentArrayList<>());
			ofy().save().entity(e).now();

			levels.add(e.getId());

			// Save level semesters

			for (Semester semester : Semester.values()) {

				LevelSemesterEntity se = new LevelSemesterEntity().setDepartmentLevel(e.getId())
						.setSemester(semester.getValue()).setDateCreated(new Date());

				ofy().save().entity(se);

				// Save default assessment totals

				CalculationModel.newAssessmentTotal(new AssessmentTotalSpec().setName("CA 1").setPercentile(10)
						.setType(AssessmentTotalType.ASSESSMENT).setLevelSemester(se.getId()));

				CalculationModel.newAssessmentTotal(new AssessmentTotalSpec().setName("CA 2").setPercentile(10)
						.setType(AssessmentTotalType.ASSESSMENT).setLevelSemester(se.getId()));

				CalculationModel.newAssessmentTotal(new AssessmentTotalSpec().setName("Exam").setPercentile(80)
						.setType(AssessmentTotalType.EXAM).setLevelSemester(se.getId()));

			}
		}

		entity.setLevels(levels);
		ofy().save().entity(entity).now();
		
		return entity.getId();
	}

	@ModelMethod(functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public static DepartmentSpec getDepartment(Long id) {
		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(id).safe();
		return EntityHelper.toObjectModel(e);
	}

	@ModelMethod(functionality = Functionality.LIST_DEPARTMENT_LEVELS)
	public static Map<Long, Integer> listDepartmentLevels(Long departmentId) {

		Map<Long, Integer> levels = new FluentHashMap<>();

		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(departmentId).safe();
		e.getLevels().forEach(l -> {
			DepartmentalLevelEntity le = ofy().load().type(DepartmentalLevelEntity.class).id(l).safe();
			levels.put(le.getId(), le.getLevel());
		});
		return levels;
	}
	

	protected static Long getDepartmentLevel(Long departmentId, Integer level) {

		DepartmentEntity e = ofy().load().type(DepartmentEntity.class).id(departmentId).safe();
		
		for(Long l : e.getLevels()){
			DepartmentalLevelEntity le = ofy().load().type(DepartmentalLevelEntity.class).id(l).safe();
			if(le.getLevel().equals(level)) {
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

		DepartmentalLevelEntity levelEntity = ofy().load().type(DepartmentalLevelEntity.class).id(departmentLevelId)
				.safe();

		if (u.equals(ListUpdate.ADD)) {
			levelEntity.addStudent(studentId);
		} else {
			levelEntity.removeStudent(studentId);
		}

		ofy().save().entity(levelEntity).now();
	}

	@ModelMethod(functionality = Functionality.MANAGE_FACULTIES)
	public static Long createFaculty(FacultySpec spec) {
		FacultyEntity e = new FacultyEntity().setName(spec.getName());
		ofy().save().entity(e).now();
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

	protected static void createStudent(Long id, StudentSpec spec) {

		StudentEntity e = EntityHelper.fromObjectModel(spec).setId(id);
		ofy().save().entity(e).now();

		// increase student count for current level
		addStudentToDepartmentLevel(ListUpdate.ADD, spec.getDepartmentLevel(), e.getId());
	}

	@Unexposed
	@BlockerTodo
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

	@ModelMethod(functionality = Functionality.VIEW_STUDENT_PROFILES)
	public static List<Long> getStudents(Long departmentLevelId) {
		DepartmentalLevelEntity e = ofy().load().type(DepartmentalLevelEntity.class).id(departmentLevelId).safe();
		return e.getStudents();
	}

	protected static void createLecturer(Long id, LecturerSpec spec) {

		LecturerEntity e = EntityHelper.fromObjectModel(spec).setId(id);
		ofy().save().entity(e).now();

		// add to lecturers field in Department
		DepartmentEntity dpt = ofy().load().type(DepartmentEntity.class).id(spec.getDepartment()).safe()
				.addLecturer(spec.getId());
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
	
	@ModelMethod(functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public static void addLecturerCourses(long lecturerId, List<String> courses) {
		LecturerEntity e = ofy().load().type(LecturerEntity.class).id(lecturerId).safe();
		e.addCourses(courses);
		ofy().save().entity(e);
	}
	
	@ModelMethod(functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public static void removeCourses(long lecturerId, String course) {
		LecturerEntity e = ofy().load().type(LecturerEntity.class).id(lecturerId).safe();
		e.removeCourse(course);
		ofy().save().entity(e);
	}

	@Unexposed
	@ModelMethod(functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public static void deleteLecturer(long id) {

		long departmentId = ofy().load().type(LecturerEntity.class).id(id).safe().getDepartment();

		ofy().delete().type(LecturerEntity.class).id(id).now();

		// delete from lecturers field in Department
		DepartmentEntity dpt = ofy().load().type(DepartmentEntity.class).id(departmentId).safe().removeLecturer(id);
		ofy().save().entity(dpt).now();

		// Consolidate associated records

	}

	protected static void createDepartmentalHead(Long id, DepartmentalHeadSpec spec) {

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

	protected static void createFacultyDean(Long id, FacultyDeanSpec spec) {

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
	public static void createCourse(CourseSpec spec) {

		CourseEntity e = EntityHelper.fromObjectModel(spec);
		ofy().save().entity(e).now();

		e.getDepartmentLevels().forEach(l -> {

			LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class).filter("departmentLevel = ", l.toString())
					.filter("semester = ", e.getSemester()).first().safe();

			ls.addCourse(e.getCode());

			ofy().save().entity(ls);
		});
		
		Long currentSemesterId = currentSemesterId();
		
		createAcademicSemesterCourse(currentSemesterId, new FluentArrayList<String>().with(spec.getCode()));
		
		// Add to search index
		SearchModel.addIndexedName(new IndexedNameSpec(e.getCode(), e.getCode(), e.getName()), IndexedNameType.COURSE);
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static CourseSpec getCourse(String courseCode) {
		return EntityHelper.toObjectModel(ofy().load().type(CourseEntity.class).id(courseCode).safe());
	}

	protected static Short getCourseUnitLoad(String courseCode) {
		return EntityHelper.toObjectModel(ofy().load().type(CourseEntity.class).id(courseCode).safe()).getPoint();
	}

	protected static Long getCourseLevelSemester(String courseCode) {
		// Because a course may be associated with multiple department levels, the entry
		// @ index 0 is used as the default
		return getCourse(courseCode).getDepartmentLevels().get(0);
	}

	public static Boolean isCourseLecturer(Long principal, String courseCode) {
		return getCourse(courseCode).getLecturers().contains(principal);
	}

	protected static List<String> listCourseKeys(Semester semester) {
		List<String> result = new FluentArrayList<>();

		ofy().load().type(LevelSemesterEntity.class).filter("semester = ", semester.getValue()).forEach(ls -> {
			ls.getCourses().forEach(c -> {
				result.add(DirectoryModel.getCourse(c).getCode());
			});
		});
		return result;
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static Map<Semester, List<CourseSpec>> listCoursesForSemesterLevel(Long departmentLevel, Semester semester) {
		List<CourseSpec> result = new FluentArrayList<>();

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", semester.getValue()).first().safe();

		ls.getCourses().forEach(c -> {
			result.add(DirectoryModel.getCourse(c));
		});

		return new FluentHashMap<Semester, List<CourseSpec>>()
				.with(semester, result);
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static Map<Semester, List<CourseSpec>> listAllCoursesForLevel(Long departmentLevel) {

		Map<Semester, List<CourseSpec>> result = new FluentHashMap<>();

		for (Semester s : Semester.values()) {

			List<CourseSpec> courses = new FluentArrayList<>();

			LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
					.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", s.getValue()).first().safe();

			ls.getCourses().forEach(c -> {
				courses.add(DirectoryModel.getCourse(c));
			});

			result.put(s, courses);
		}

		return result;
	}

	//Not really needed
	@Unexposed
	@ModelMethod(functionality = Functionality.VIEW_COURSES)
	public static List<CourseSpec> listSemesterCoursesForLevel(Long departmentLevel, Long academicSemesterId) {

		List<CourseSpec> courses = new FluentArrayList<>();

		Integer semester = getAcademicSemesterValue(academicSemesterId);

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", semester).first().safe();

		ls.getCourses().forEach(c -> {
			courses.add(DirectoryModel.getCourse(c));
		});

		return courses;
	}

	protected static List<String> listCourseKeys(Long departmentLevel, long academicSemesterId) {
		List<String> result = new FluentArrayList<>();

		Integer currentSemester = DirectoryModel.getAcademicSemesterValue(academicSemesterId);

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", currentSemester).first().safe();

		ls.getCourses().forEach(c -> {
			result.add(c);
		});

		return result;
	}

	@Unexposed
	@BlockerTodo()
	@ModelMethod(functionality = Functionality.MANAGE_COURSES)
	public static void deleteCourse(String courseCode) {

		// Consolidate

		ofy().delete().type(CourseEntity.class).id(courseCode).now();
		
		SearchModel.removeIndexedName(courseCode, IndexedNameType.COURSE);
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
	public static void registerStudentCourses(Long studentId, List<String> courses) {

		Long currentSemesterId = DirectoryModel.currentSemesterId();
		long departmentLevel = ofy().load().type(StudentEntity.class).id(studentId).safe().getDepartmentLevel();

		List<String> levelCourses = listCourseKeys(departmentLevel, currentSemesterId);

		List<String> carryoverCourses = courses;
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

			// verify that score sheets has not yet been created for these courses

			if (CalculationModel.isSemesterCourseSheetCreated((long) -1, c)) {
				throw new SystemValidationException(SystemErrorCodes.COURSE_RESULT_SHEET_ALREADY_EXISTS, c);
			}
		});

		courses.forEach(c -> {

			AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
					.filter("academicSemesterId = ", currentSemesterId().toString()).filter("courseCode = ", c).first().safe();

			e.addStudent(studentId);

			ofy().save().entity(e);

		});

		StudentSemesterCoursesEntity e = new StudentSemesterCoursesEntity().setStudentId(studentId)
				.setAcademicSemesterId(currentSemesterId()).setCourses(courses).setDateCreated(new Date());

		ofy().save().entity(e);
	}

	public static String getCourseCode(Long academicSemesterCourseId) {
		return ofy().load().type(AcademicSemesterCourseEntity.class).id(academicSemesterCourseId.toString()).safe().getCourseCode();
	}
}
