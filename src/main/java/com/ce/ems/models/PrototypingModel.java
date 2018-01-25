
package com.ce.ems.models;

import java.util.Date;
import java.util.Random;

import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.spec.CourseSpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.classes.spec.DepartmentalHeadSpec;
import com.ce.ems.base.classes.spec.FacultyDeanSpec;
import com.ce.ems.base.classes.spec.FacultySpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Model;
import com.ce.ems.base.core.RequestThreadAttributes;
import com.ce.ems.models.helpers.MockHelper;
import com.kylantis.eaa.core.users.RoleRealm;

@Model(dependencies = { ActivityStreamModel.class, ApplicationModel.class, BaseUserModel.class, BlobStoreModel.class,
		CalculationModel.class, ConfigModel.class, ConfigurationModel.class, DModel.class, EmailingModel.class,
		FormModel.class, LocaleModel.class, LocationModel.class, MetricsModel.class, PlatformModel.class,
		RoleModel.class, SearchModel.class, SystemMetricsModel.class })
public class PrototypingModel extends BaseModel {

	private static final boolean IS_ENABLED = true;

	@Override
	public String path() {
		return "tmp/prototyping";
	}

	@Override
	public void preInstall() {
	}

	@Override
	public void install(InstallOptions options) {

	}

	public static void addMocks() {

		if (!IS_ENABLED) {
			return;
		}

		Logger.debug("Populating mock data, used for prototyping..");

		Logger.debug("manually adding RequestThreadAttributes, since execution is outside a fusion request context");

		RequestThreadAttributes.set(LocaleModel.USER_DEFAULT_LOCALE, "en-NG");

		Long principal = -1l;

		Random randomInstance = new Random();

		Logger.debug("Creating faculties and departments, Hod and Dean to be appointed in later step");

		Long facultyOfScience = DModel.createFaculty(principal, new FacultySpec().setName("Sciences"));
		Long cscDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(3)
				.setFaculty(facultyOfScience).setIsAccredited(true).setName("Computer Science"));
		Long mcbDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(2)
				.setFaculty(facultyOfScience).setIsAccredited(true).setName("Microbiology"));
		Long psbDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(5)
				.setFaculty(facultyOfScience).setIsAccredited(false).setName("Plant and Science Biology"));
		Long aebDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(4)
				.setFaculty(facultyOfScience).setIsAccredited(true).setName("Animal and Environment"));

//		Long facultyOfHumanities = DModel.createFaculty(principal, new FacultySpec().setName("Humanities"));
//
//		Long psDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(3)
//				.setFaculty(facultyOfHumanities).setIsAccredited(false).setName("Political Science"));
//		Long hisDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(6)
//				.setFaculty(facultyOfHumanities).setIsAccredited(true).setName("History"));
//		Long geoDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(4)
//				.setFaculty(facultyOfHumanities).setIsAccredited(false).setName("Geology"));
//		Long engDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(2)
//				.setFaculty(facultyOfHumanities).setIsAccredited(true).setName("English"));
//
//		Long facultyOfEngineering = DModel.createFaculty(principal, new FacultySpec().setName("Engineering"));
//		Long meDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(3).setFaculty(facultyOfEngineering).setIsAccredited(true).setName("Mechanical Engineering"));
//		Long ceDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(6).setFaculty(facultyOfEngineering).setIsAccredited(true).setName("Civil Engineering"));
//		Long eeDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(4).setFaculty(facultyOfEngineering).setIsAccredited(true).setName("Electrical Engineering"));
//		Long pceDpt = DModel.createDepartment(principal, new DepartmentSpec().setDuration(2).setFaculty(facultyOfEngineering).setIsAccredited(false).setName("Petro-chemical Engineering"));

		Logger.debug("Creating courses for department: " + cscDpt);

		Long _100level = DModel.getDepartmentLevel(cscDpt, 100).getId();

		DModel.createCourse(principal, new CourseSpec().setCode("BBA 113").setName("Financial Accounting")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BBA 115").setName("Business Administration")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));
		DModel.createCourse(principal,
				new CourseSpec().setCode("COM 121").setName("Computer Architecture and Organization")
						.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_100level))
						.setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("COM 124").setName("Linear Programming")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));

		Long _200level = DModel.getDepartmentLevel(cscDpt, 200).getId();

		DModel.createCourse(principal, new CourseSpec().setCode("BIT 212").setName("System Analysis and Design")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
		DModel.createCourse(principal,
				new CourseSpec().setCode("COM 211").setName("Object oriented system development with Java")
						.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level))
						.setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("COM 212").setName("Data Structure and Algorism")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
		DModel.createCourse(principal,
				new CourseSpec().setCode("BIT 211").setName("Database Development and Administration")
						.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level))
						.setPoint(3));

		DModel.createCourse(principal, new CourseSpec().setCode("COM 224").setName("Application Development with VB")
				.setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("COM 221").setName("Operating System Principles")
				.setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("COM 222").setName("Software Engineering")
				.setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("RSC 001").setName("Research Methodology in Computing")
				.setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
		DModel.createCourse(principal,
				new CourseSpec().setCode("BIM 290").setName("Entrepreneurship and small business management")
						.setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level))
						.setPoint(3));

		Long _300level = DModel.getDepartmentLevel(cscDpt, 300).getId();

		DModel.createCourse(principal, new CourseSpec().setCode("COM 311").setName("Statistical tools for analysis")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 311").setName("Management I.T Project")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 313").setName("Communication System")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("COM 312").setName("Artificial Intelligence")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal,
				new CourseSpec().setCode("BIT 314").setName("Network Configuration and Management")
						.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level))
						.setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 312").setName("Computer Graphics")
				.setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));

		DModel.createCourse(principal, new CourseSpec().setCode("COM 321").setName("Simulation and Modeling")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 322").setName("Distributed System")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 321").setName("Professional Issues in IT")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 324").setName("Information Security")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
		DModel.createCourse(principal, new CourseSpec().setCode("BIT 325").setName("EE-Commerce")
				.setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));

		Logger.debug("Creating users");

		String adminRole = RoleModel.getDefaultRole(RoleRealm.ADMIN);
		BaseUserModel.registerUser(MockHelper.createMockAdmin(), adminRole, principal);

		String examOfficerRole = RoleModel.getDefaultRole(RoleRealm.EXAM_OFFICER);
		BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), examOfficerRole, principal);

		String deanRole = RoleModel.getDefaultRole(RoleRealm.DEAN);

		Logger.debug("Creating and appointing faculty deans");

		Long deanUserId1 = BaseUserModel.registerUser(MockHelper.createMockDean(), deanRole, principal);
		DModel.createFacultyDean(deanUserId1,
				new FacultyDeanSpec().setFaculty(facultyOfScience).setStartDate(new Date()));

//		Long deanUserId2 = BaseUserModel.registerUser(MockHelper.createMockDean(), deanRole, principal);
//		DModel.createFacultyDean(deanUserId2,
//				new FacultyDeanSpec().setFaculty(facultyOfHumanities).setStartDate(new Date()));
//
//		Long deanUserId3 = BaseUserModel.registerUser(MockHelper.createMockDean(), deanRole, principal);
//		DModel.createFacultyDean(deanUserId3,
//				new FacultyDeanSpec().setFaculty(facultyOfEngineering).setStartDate(new Date()));

		Logger.debug("Creating and appointing Hod for: " + cscDpt);

		String hodRole = RoleModel.getDefaultRole(RoleRealm.HEAD_OF_DEPARTMENT);

		Long hodUserId1 = BaseUserModel.registerUser(MockHelper.createMockHod(), hodRole, principal);
		DModel.createDepartmentalHead(hodUserId1,
				new DepartmentalHeadSpec().setDepartment(cscDpt).setStartDate(new Date()));

		Long hodUserId2 = BaseUserModel.registerUser(MockHelper.createMockAdmin(), hodRole, principal);
		DModel.createDepartmentalHead(hodUserId2,
				new DepartmentalHeadSpec().setDepartment(psbDpt).setStartDate(new Date()));

		Long hodUserId3 = BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), hodRole, principal);
		DModel.createDepartmentalHead(hodUserId3,
				new DepartmentalHeadSpec().setDepartment(aebDpt).setStartDate(new Date()));

//		Long hodUserId4 = BaseUserModel.registerUser(MockHelper.createMockDean(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId4,
//				new DepartmentalHeadSpec().setDepartment(psDpt).setStartDate(new Date()));
//
//		Long hodUserId5 = BaseUserModel.registerUser(MockHelper.createMockLecturer(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId5,
//				new DepartmentalHeadSpec().setDepartment(hisDpt).setStartDate(new Date()));
//
//		Long hodUserId6 = BaseUserModel.registerUser(MockHelper.createMockStudent(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId6,
//				new DepartmentalHeadSpec().setDepartment(geoDpt).setStartDate(new Date()));
//
//		Long hodUserId7 = BaseUserModel.registerUser(MockHelper.createMockAdmin(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId7,
//				new DepartmentalHeadSpec().setDepartment(engDpt).setStartDate(new Date()));
//
//		Long hodUserId8 = BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId8,
//				new DepartmentalHeadSpec().setDepartment(meDpt).setStartDate(new Date()));
//
//		Long hodUserId9 = BaseUserModel.registerUser(MockHelper.createMockDean(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId9,
//				new DepartmentalHeadSpec().setDepartment(ceDpt).setStartDate(new Date()));
//
//		Long hodUserId10 = BaseUserModel.registerUser(MockHelper.createMockLecturer(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId10,
//				new DepartmentalHeadSpec().setDepartment(eeDpt).setStartDate(new Date()));
//
//		Long hodUserId11 = BaseUserModel.registerUser(MockHelper.createMockStudent(), hodRole, principal);
//		DModel.createDepartmentalHead(hodUserId11,
//				new DepartmentalHeadSpec().setDepartment(pceDpt).setStartDate(new Date()));

		Long hodUserId12 = BaseUserModel.registerUser(MockHelper.createMockHod(), hodRole, principal);
		DModel.createDepartmentalHead(hodUserId12,
				new DepartmentalHeadSpec().setDepartment(mcbDpt).setStartDate(new Date()));

		Logger.debug("creating lecturers, and assigning them to courses");

		String lecturerRole = RoleModel.getDefaultRole(RoleRealm.LECTURER);

		Long lecturerUserId1 = BaseUserModel.registerUser(MockHelper.createMockAdmin(), lecturerRole, principal);
		DModel.createLecturer(lecturerUserId1, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
		DModel.addLecturerCourses(hodUserId1, lecturerUserId1,
				new FluentArrayList<String>().with("BBA 113").with("BBA 115").with("COM 121").with("COM 124"));

		Long lecturerUserId2 = BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), lecturerRole, principal);
		DModel.createLecturer(lecturerUserId2, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
		DModel.addLecturerCourses(hodUserId1, lecturerUserId2,
				new FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM 212").with("BIT 211"));

		Long lecturerUserId3 = BaseUserModel.registerUser(MockHelper.createMockDean(), lecturerRole, principal);
		DModel.createLecturer(lecturerUserId3, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
		DModel.addLecturerCourses(hodUserId1, lecturerUserId3, new FluentArrayList<String>().with("COM 224")
				.with("COM 221").with("COM 222").with("RSC 001").with("BIM 290"));

		Long lecturerUserId4 = BaseUserModel.registerUser(MockHelper.createMockHod(), lecturerRole, principal);
		DModel.createLecturer(lecturerUserId4, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
		DModel.addLecturerCourses(hodUserId1, lecturerUserId4, new FluentArrayList<String>().with("COM 311")
				.with("BIT 311").with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312"));

		Long lecturerUserId5 = BaseUserModel.registerUser(MockHelper.createMockLecturer(), lecturerRole, principal);
		DModel.createLecturer(lecturerUserId5, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
		DModel.addLecturerCourses(hodUserId1, lecturerUserId5, new FluentArrayList<String>().with("COM 321")
				.with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321"));

		Logger.debug("creating students, and registering their courses for the current semester");

		String studentRole = RoleModel.getDefaultRole(RoleRealm.STUDENT);

		// Add CSC 100 level students

		for (Integer i = 0; i < 3; i++) {

			Long studentUserIdX = BaseUserModel.registerUser(MockHelper.nextMockUser(), studentRole, principal);
			DModel.createStudent(studentUserIdX, new StudentSpec().setJambRegNo(randomInstance.nextInt(5000) + "3442FR")
					.setMatricNumber("17/" + randomInstance.nextInt(5000)).setDepartmentLevel(_100level));
			DModel.registerStudentCourses(studentUserIdX,
					new FluentArrayList<String>().with("BBA 113").with("BBA 115"));

		}

		Long studentUserId1 = BaseUserModel.registerUser(MockHelper.createMockDean(), studentRole, principal);
		DModel.createStudent(studentUserId1, new StudentSpec().setJambRegNo("126836822FR").setMatricNumber("17/45672")
				.setDepartmentLevel(_100level));
		DModel.registerStudentCourses(studentUserId1, new FluentArrayList<String>().with("BBA 113").with("BBA 115"));

		Long studentUserId2 = BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), studentRole, principal);
		DModel.createStudent(studentUserId2, new StudentSpec().setJambRegNo("12686768FR").setMatricNumber("17/468872")
				.setDepartmentLevel(_100level));
		DModel.registerStudentCourses(studentUserId2, new FluentArrayList<String>().with("BBA 113").with("BBA 115"));

		// Add CSC 200 level students

		for (Integer i = 0; i < 3; i++) {

			Long studentUserIdX = BaseUserModel.registerUser(MockHelper.nextMockUser(), studentRole, principal);
			DModel.createStudent(studentUserIdX, new StudentSpec().setJambRegNo(randomInstance.nextInt(5000) + "322GE")
					.setMatricNumber("16/" + randomInstance.nextInt(5000)).setDepartmentLevel(_200level));
			// These are second and third semester courses, not first
			// DirectoryModel.registerStudentCourses(studentUserIdX, new
			// FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM
			// 212").with("BIT 211").with("COM 224").with("COM 221").with("COM
			// 222").with("RSC 001").with("BIM 290"));

		}

		Long studentUserId3 = BaseUserModel.registerUser(MockHelper.createMockDean(), studentRole, principal);
		DModel.createStudent(studentUserId3,
				new StudentSpec().setJambRegNo("4712122FR").setMatricNumber("16/48892").setDepartmentLevel(_200level));
		// These are second and third semester courses, not first
		// DirectoryModel.registerStudentCourses(studentUserId3, new
		// FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM
		// 212").with("BIT 211").with("COM 224").with("COM 221").with("COM
		// 222").with("RSC 001").with("BIM 290"));

		Long studentUserId4 = BaseUserModel.registerUser(MockHelper.createMockHod(), studentRole, principal);
		DModel.createStudent(studentUserId4,
				new StudentSpec().setJambRegNo("4723542FR").setMatricNumber("16/412372").setDepartmentLevel(_200level));
		// These are second and third semester courses, not first
		// DirectoryModel.registerStudentCourses(studentUserId4, new
		// FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM
		// 212").with("BIT 211").with("COM 224").with("COM 221").with("COM
		// 222").with("RSC 001").with("BIM 290"));

		// Add CSC 300 level students

		for (Integer i = 0; i < 5; i++) {

			Long studentUserIdX = BaseUserModel.registerUser(MockHelper.nextMockUser(), studentRole, principal);
			DModel.createStudent(studentUserIdX, new StudentSpec().setJambRegNo(randomInstance.nextInt(5000) + "362FX")
					.setMatricNumber("15/" + randomInstance.nextInt(5000)).setDepartmentLevel(_300level));
			DModel.registerStudentCourses(studentUserIdX, new FluentArrayList<String>().with("COM 311").with("BIT 311")
					.with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312")
			// These are second semester courses, not first
			// .with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321")
			);
		}

		Long studentUserId5 = BaseUserModel.registerUser(MockHelper.createMockLecturer(), studentRole, principal);
		DModel.createStudent(studentUserId5,
				new StudentSpec().setJambRegNo("640939FR").setMatricNumber("15/42001").setDepartmentLevel(_300level));
		DModel.registerStudentCourses(studentUserId5, new FluentArrayList<String>().with("COM 311").with("BIT 311")
				.with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312")
		// These are second semester courses, not first
		// .with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321")
		);

		Long studentUserId6 = BaseUserModel.registerUser(MockHelper.createMockStudent(), studentRole, principal);
		DModel.createStudent(studentUserId6,
				new StudentSpec().setJambRegNo("123222FR").setMatricNumber("15/19902").setDepartmentLevel(_300level));
		DModel.registerStudentCourses(studentUserId6, new FluentArrayList<String>().with("COM 311").with("BIT 311")
				.with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312")
		// These are second semester courses, not first
		// .with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321")
		);

	}

}
