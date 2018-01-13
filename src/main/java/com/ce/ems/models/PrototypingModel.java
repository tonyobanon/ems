
package com.ce.ems.models;

import java.util.Date;

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
import com.ce.ems.base.core.Application;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Model;
import com.ce.ems.base.core.RequestThreadAttributes;
import com.ce.ems.models.helpers.MockHelper;
import com.kylantis.eaa.core.users.RoleRealm;

@Model(dependencies = {ActivityStreamModel.class, ApplicationModel.class, BaseUserModel.class, BlobStoreModel.class, CacheModel.class,
		CalculationModel.class, ConfigModel.class, ConfigurationModel.class, DirectoryModel.class, EmailingModel.class, FormModel.class, LocaleModel.class
		,LocationModel.class, MetricsModel.class, PlatformModel.class, RoleModel.class, SearchModel.class, SystemMetricsModel.class})
public class PrototypingModel extends BaseModel{

	@Override
	public String path() {
		return "tmp/prototyping";
	}
	
	@Override
	public void preInstall() {
	}
	
	@Override
	public void install(InstallOptions options) {
		
		if(Application.isProduction()) {
			return;
		}
		
		
		Logger.debug("Populating mock data, used for prototyping..");
		
		
		Logger.debug("manually adding RequestThreadAttributes, since execution is outside a fusion request context");
		
		RequestThreadAttributes.set(LocaleModel.USER_DEFAULT_LOCALE, "en-NG");	
		
		
		Long principal = -1l;		
		
		
		Logger.debug("Creating faculties and departments, Hod and Dean to be appointed in later step");
		
		Long facultyOfScience = DirectoryModel.createFaculty(principal, new FacultySpec().setName("Sciences"));			
		Long cscDpt = DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(3).setFaculty(facultyOfScience).setIsAccredited(true).setName("Computer Science"));
		    DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(2).setFaculty(facultyOfScience).setIsAccredited(true).setName("Microbiology"));
		    DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(5).setFaculty(facultyOfScience).setIsAccredited(false).setName("Plant and Science Biology"));
		    DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(4).setFaculty(facultyOfScience).setIsAccredited(true).setName("Animal and Environment"));
		
		
		Long facultyOfHumanities = DirectoryModel.createFaculty(principal, new FacultySpec().setName("Humanities"));
		
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(3).setFaculty(facultyOfHumanities).setIsAccredited(false).setName("Political Science"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(6).setFaculty(facultyOfHumanities).setIsAccredited(true).setName("History"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(4).setFaculty(facultyOfHumanities).setIsAccredited(false).setName("Geology"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(2).setFaculty(facultyOfHumanities).setIsAccredited(true).setName("English"));

	    
		Long facultyOfEngineering = DirectoryModel.createFaculty(principal, new FacultySpec().setName("Engineering"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(3).setFaculty(facultyOfEngineering).setIsAccredited(true).setName("Mechanical Engineering"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(6).setFaculty(facultyOfEngineering).setIsAccredited(true).setName("Civil Engineering"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(4).setFaculty(facultyOfEngineering).setIsAccredited(true).setName("Electrical Engineering"));
			DirectoryModel.createDepartment(principal, new DepartmentSpec().setDuration(2).setFaculty(facultyOfEngineering).setIsAccredited(false).setName("Petro-chemical Engineering"));

		
			

			Logger.debug("Creating courses for department: " + cscDpt);
		
			Long _100level = DirectoryModel.getDepartmentLevel(cscDpt, 100);
		
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BBA 113").setName("Financial Accounting").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BBA 115").setName("Business Administration").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 121").setName("Computer Architecture and Organization").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 124").setName("Linear Programming").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_100level)).setPoint(3));
		
			
			
			Long _200level = DirectoryModel.getDepartmentLevel(cscDpt, 200);
			
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 212").setName("System Analysis and Design").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 211").setName("Object oriented system development with Java").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 212").setName("Data Structure and Algorism").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 211").setName("Database Development and Administration").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 224").setName("Application Development with VB").setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 221").setName("Operating System Principles").setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 222").setName("Software Engineering").setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("RSC 001").setName("Research Methodology in Computing").setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIM 290").setName("Entrepreneurship and small business management").setSemester(Semester.THIRD).setDepartmentLevels(FluentArrayList.asList(_200level)).setPoint(3));

			
			
			Long _300level = DirectoryModel.getDepartmentLevel(cscDpt, 300);
			
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 311").setName("Statistical tools for analysis").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 311").setName("Management I.T Project").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 313").setName("Communication System").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 312").setName("Artificial Intelligence").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 314").setName("Network Configuration and Management").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 312").setName("Computer Graphics").setSemester(Semester.FIRST).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));

			DirectoryModel.createCourse(principal, new CourseSpec().setCode("COM 321").setName("Simulation and Modeling").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 322").setName("Distributed System").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 321").setName("Professional Issues in IT").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 324").setName("Information Security").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));
			DirectoryModel.createCourse(principal, new CourseSpec().setCode("BIT 321").setName("EE-Commerce").setSemester(Semester.SECOND).setDepartmentLevels(FluentArrayList.asList(_300level)).setPoint(3));

		
			
			Logger.debug("Creating users");
			
			String adminRole = RoleModel.getDefaultRole(RoleRealm.ADMIN);
			BaseUserModel.registerUser(MockHelper.createMockAdmin(), adminRole, principal);
						
			String examOfficerRole = RoleModel.getDefaultRole(RoleRealm.EXAM_OFFICER);
			BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), examOfficerRole, principal);
			
			
			
			String deanRole = RoleModel.getDefaultRole(RoleRealm.DEAN);
			
			Logger.debug("Creating and appointing faculty deans");
			
			Long deanUserId1 = BaseUserModel.registerUser(MockHelper.createMockDean(), deanRole, principal);
			DirectoryModel.createFacultyDean(deanUserId1, new FacultyDeanSpec().setFaculty(facultyOfScience).setStartDate(new Date()));
			
			Long deanUserId2 = BaseUserModel.registerUser(MockHelper.createMockDean(), deanRole, principal);
			DirectoryModel.createFacultyDean(deanUserId2, new FacultyDeanSpec().setFaculty(facultyOfHumanities).setStartDate(new Date()));
			
			Long deanUserId3 = BaseUserModel.registerUser(MockHelper.createMockDean(), deanRole, principal);
			DirectoryModel.createFacultyDean(deanUserId3, new FacultyDeanSpec().setFaculty(facultyOfEngineering).setStartDate(new Date()));
			
			
			
			Logger.debug("Creating and appointing Hod for: " + cscDpt);
			
			String hodRole = RoleModel.getDefaultRole(RoleRealm.HEAD_OF_DEPARTMENT);
			Long hodUserId = BaseUserModel.registerUser(MockHelper.createMockHod(), hodRole, principal);
			DirectoryModel.createDepartmentalHead(hodUserId, new DepartmentalHeadSpec().setDepartment(cscDpt).setStartDate(new Date()));
			
			
			
			Logger.debug("creating lecturers, and assigning them to courses");
			
			String lecturerRole = RoleModel.getDefaultRole(RoleRealm.LECTURER);
			
			Long lecturerUserId1 = BaseUserModel.registerUser(MockHelper.createMockAdmin(), lecturerRole, principal);
			DirectoryModel.createLecturer(lecturerUserId1, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
			DirectoryModel.addLecturerCourses(hodUserId, lecturerUserId1, new FluentArrayList<String>().with("BBA 113").with("BBA 115").with("COM 121").with("COM 124"));
			
			
			Long lecturerUserId2 = BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), lecturerRole, principal);
			DirectoryModel.createLecturer(lecturerUserId2, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
			DirectoryModel.addLecturerCourses(hodUserId, lecturerUserId2, new FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM 212").with("BIT 211"));
			
			
			Long lecturerUserId3 = BaseUserModel.registerUser(MockHelper.createMockDean(), lecturerRole, principal);
			DirectoryModel.createLecturer(lecturerUserId3, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
			DirectoryModel.addLecturerCourses(hodUserId, lecturerUserId3, new FluentArrayList<String>().with("COM 224").with("COM 221").with("COM 222").with("RSC 001").with("BIM 290"));
			
			
			Long lecturerUserId4 = BaseUserModel.registerUser(MockHelper.createMockHod(), lecturerRole, principal);
			DirectoryModel.createLecturer(lecturerUserId4, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
			DirectoryModel.addLecturerCourses(hodUserId, lecturerUserId4, new FluentArrayList<String>().with("COM 311").with("BIT 311").with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312"));
			
			
			Long lecturerUserId5 = BaseUserModel.registerUser(MockHelper.createMockLecturer(), lecturerRole, principal);
			DirectoryModel.createLecturer(lecturerUserId5, new LecturerSpec().setDepartment(cscDpt).setStartDate(new Date()));
			DirectoryModel.addLecturerCourses(hodUserId, lecturerUserId5, new FluentArrayList<String>().with("COM 321").with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321"));
			
			
			
			Logger.debug("creating students, and registering their courses for the current semester");
			
			String studentRole = RoleModel.getDefaultRole(RoleRealm.STUDENT);
			
			Long studentUserId1 = BaseUserModel.registerUser(MockHelper.createMockDean(), studentRole, principal);
			DirectoryModel.createStudent(studentUserId1, new StudentSpec().setJambRegNo("126836822FR").setMatricNumber("17/45672").setDepartmentLevel(_100level));
			DirectoryModel.registerStudentCourses(studentUserId1, new FluentArrayList<String>().with("BBA 113").with("BBA 115"));
			
			Long studentUserId2 = BaseUserModel.registerUser(MockHelper.createMockExamOfficer(), studentRole, principal);
			DirectoryModel.createStudent(studentUserId2, new StudentSpec().setJambRegNo("12686768FR").setMatricNumber("17/468872").setDepartmentLevel(_100level));
			DirectoryModel.registerStudentCourses(studentUserId2, new FluentArrayList<String>().with("BBA 113").with("BBA 115"));
			
			Long studentUserId3 = BaseUserModel.registerUser(MockHelper.createMockDean(), studentRole, principal);
			DirectoryModel.createStudent(studentUserId3, new StudentSpec().setJambRegNo("4712122FR").setMatricNumber("16/48892").setDepartmentLevel(_200level));
			//These are second and third semester courses, not first
			//DirectoryModel.registerStudentCourses(studentUserId3, new FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM 212").with("BIT 211").with("COM 224").with("COM 221").with("COM 222").with("RSC 001").with("BIM 290"));
			
			Long studentUserId4 = BaseUserModel.registerUser(MockHelper.createMockHod(), studentRole, principal);
			DirectoryModel.createStudent(studentUserId4, new StudentSpec().setJambRegNo("4723542FR").setMatricNumber("16/412372").setDepartmentLevel(_200level));
			//These are second and third semester courses, not first
			//DirectoryModel.registerStudentCourses(studentUserId4, new FluentArrayList<String>().with("BIT 212").with("COM 211").with("COM 212").with("BIT 211").with("COM 224").with("COM 221").with("COM 222").with("RSC 001").with("BIM 290"));

			
			Long studentUserId5 = BaseUserModel.registerUser(MockHelper.createMockLecturer(), studentRole, principal);
			DirectoryModel.createStudent(studentUserId5, new StudentSpec().setJambRegNo("640939FR").setMatricNumber("15/42001").setDepartmentLevel(_300level));
			DirectoryModel.registerStudentCourses(studentUserId5, new FluentArrayList<String>().with("COM 311").with("BIT 311").with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312")
					// These are second semester courses, not first
					//.with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321")
					);
			
			Long studentUserId6 = BaseUserModel.registerUser(MockHelper.createMockStudent(), studentRole, principal);
			DirectoryModel.createStudent(studentUserId6, new StudentSpec().setJambRegNo("123222FR").setMatricNumber("15/19902").setDepartmentLevel(_300level));
			DirectoryModel.registerStudentCourses(studentUserId6, new FluentArrayList<String>().with("COM 311").with("BIT 311").with("BIT 313").with("COM 312").with("BIT 314").with("BIT 312")
					// These are second semester courses, not first
					//.with("BIT 322").with("BIT 321").with("BIT 324").with("BIT 321")
					);

	}
	
}
