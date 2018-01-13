package com.kylantis.eaa.core.fusion.services;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.ce.ems.base.classes.spec.CourseSpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.classes.spec.FacultySpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.DirectoryModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/directory")
public class DirectoryService extends BaseService {

	@EndpointMethod(uri = "/new-academic-semester", bodyParams = {"spec"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public void newAcademicSemester(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());	
		
		JsonObject body = ctx.getBodyAsJson();
		AcademicSemesterSpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), AcademicSemesterSpec.class);
		
		Long id = DirectoryModel.newAcademicSemester(principal, spec);
		
		ctx.response().write(id.toString()).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/end-current-semester",
			functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public void endCurrentSemester(RoutingContext ctx) {
		DirectoryModel.endSemester();
	}
	
	@EndpointMethod(uri = "/get-current-semester",
			functionality = Functionality.GET_CURRENT_SEMESTER)
	public void getCurrentSemester(RoutingContext ctx) {
		AcademicSemesterSpec spec = DirectoryModel.getCurrentAcademicSemester();
		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}
	

	@EndpointMethod(uri = "/list-academic-semester",
			functionality = Functionality.LIST_ACADEMIC_SEMESTER)
	public void listAcademicSemester(RoutingContext ctx) {
		List<AcademicSemesterSpec> result = DirectoryModel.listAcademicSemester();
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/create-department", bodyParams = {"spec"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_DEPARTMENTS)
	public void createDepartment(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());	
		
		JsonObject body = ctx.getBodyAsJson();
		DepartmentSpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), DepartmentSpec.class);
		
		Long id = DirectoryModel.createDepartment(principal, spec);
		
		ctx.response().write(id.toString()).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-department", requestParams = {"departmentId"},
			functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void getDepartment(RoutingContext ctx) {
		
		Long departmentId = Long.parseLong(ctx.request().getParam("departmentId"));
		
		DepartmentSpec spec = DirectoryModel.getDepartment(departmentId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/list-department-levels", requestParams = {"departmentId"},
			functionality = Functionality.LIST_DEPARTMENT_LEVELS)
	public void listDepartmentLevels(RoutingContext ctx) {
		
		Long departmentId = Long.parseLong(ctx.request().getParam("departmentId"));
		
		 Map<Long, String> result = DirectoryModel.listDepartmentLevels(departmentId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/list-department-names", requestParams = {"facultyId"},
			functionality = Functionality.LIST_DEPARTMENT_NAMES)
	public void listDepartmentNames(RoutingContext ctx) {
		
		String facultyId = ctx.request().getParam("facultyId");
		
		Map<Long, String> result = facultyId != null ? DirectoryModel.listDepartmentNames(Long.parseLong(facultyId)) 
														: DirectoryModel.listDepartmentNames();
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	
	@EndpointMethod(uri = "/list-departments", requestParams = {"facultyId"},
			functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void listDepartments(RoutingContext ctx) {
		
		Long facultyId = Long.parseLong(ctx.request().getParam("facultyId"));
		
		List<DepartmentSpec> result = DirectoryModel.listDepartments(facultyId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/create-faculty", bodyParams = {"spec"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_FACULTIES)
	public void createFaculty(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());	
		
		JsonObject body = ctx.getBodyAsJson();
		FacultySpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), FacultySpec.class);
		
		Long id = DirectoryModel.createFaculty(principal, spec);
		
		ctx.response().write(id.toString()).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-faculty", requestParams = {"facultyId"},
			functionality = Functionality.VIEW_FACULTY_PROFILES)
	public void getFaculty(RoutingContext ctx) {
		
		Long facultyId = Long.parseLong(ctx.request().getParam("facultyId"));
		
		FacultySpec spec = DirectoryModel.getFaculty(facultyId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/list-faculties",
			functionality = Functionality.VIEW_FACULTY_PROFILES)
	public void listFaculties(RoutingContext ctx) {
		
		List<FacultySpec> result = DirectoryModel.listFaculties();
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/list-faculty-names",
			functionality = Functionality.LIST_FACULTY_NAMES)
	public void listFacultyNames(RoutingContext ctx) {
		
		Map<Long, String> result = DirectoryModel.listFacultyNames();
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-student", requestParams = {"studentId"},
			functionality = Functionality.VIEW_STUDENT_PROFILES)
	public void getStudent(RoutingContext ctx) {
		
		Long studentId = Long.parseLong(ctx.request().getParam("studentId"));
		
		StudentSpec spec = DirectoryModel.getStudent(studentId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-students", requestParams = {"departmentLevelId"},
			functionality = Functionality.VIEW_STUDENT_PROFILES)
	public void getStudents (RoutingContext ctx) {
		
		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));
		
		List<StudentSpec> result = DirectoryModel.getStudents(departmentLevelId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-lecturer", requestParams = {"lecturerId"},
			functionality = Functionality.VIEW_LECTURER_PROFILES)
	public void getLecturer (RoutingContext ctx) {
		
		Long lecturerId = Long.parseLong(ctx.request().getParam("lecturerId"));
		
		LecturerSpec result = DirectoryModel.getLecturer(lecturerId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-lecturers", requestParams = {"departmentId"},
			functionality = Functionality.VIEW_LECTURER_PROFILES)
	public void getLecturers (RoutingContext ctx) {
		
		Long departmentId = Long.parseLong(ctx.request().getParam("departmentId"));
		
		List<Long> result = DirectoryModel.getLecturers(departmentId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/create-course", bodyParams = {"spec"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_COURSES)
	public void createCourse(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());	
		
		JsonObject body = ctx.getBodyAsJson();
		CourseSpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), CourseSpec.class);
		
		DirectoryModel.createCourse(principal, spec);
	}
	
	@EndpointMethod(uri = "/get-course", requestParams = {"courseCode"},
			functionality = Functionality.VIEW_COURSES)
	public void getCourse(RoutingContext ctx) {
		
		String courseCode = ctx.request().getParam("courseCode");
		
		CourseSpec spec = DirectoryModel.getCourse(courseCode);
		
		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}
	
	
	@EndpointMethod(uri = "/list-semster-level-courses", requestParams = {"departmentLevel", "semester"},
			functionality = Functionality.VIEW_COURSES)
	public void listCoursesForSemesterLevel (RoutingContext ctx) {
		
		Long departmentLevel = Long.parseLong(ctx.request().getParam("departmentLevel"));
		String semester = ctx.request().getParam("semester");
		
		Map<Semester, List<CourseSpec>> result = semester != null ?
									DirectoryModel.listCoursesForSemesterLevel(departmentLevel, Semester.from(Integer.parseInt(semester)))
									:
									DirectoryModel.listAllCoursesForLevel(departmentLevel);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/list-student-courses", requestParams = {"studentId"},
			functionality = Functionality.REGISTER_STUDENT_COURSES)
	public void listStudentCourses (RoutingContext ctx) {
		
		Long studentId = Long.parseLong(ctx.request().getParam("studentId"));
		
		List<CourseSpec> result = DirectoryModel.listStudentCourses(studentId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/register-student-courses", bodyParams = {"studentId", "courses"}, method = HttpMethod.PUT,
			functionality = Functionality.REGISTER_STUDENT_COURSES)
	public void registerStudentCourses (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long studentId = body.getLong("studentId");
		List<String> courses = body.getJsonArray("courses").getList();
		
		DirectoryModel.registerStudentCourses(studentId, courses);
	} 
	
	@EndpointMethod(uri = "/get-available-semester-courses",
			functionality = Functionality.MANAGE_COURSE_SCORE_SHEET)
	public void getAvailableSemesterCourses (RoutingContext ctx) {
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		Map<String, AcademicSemesterCourseSpec> result = DirectoryModel.getAcademicSemesterCourses(principal);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end(); 
	}
	
	@EndpointMethod(uri = "/add-lecturer-courses", bodyParams = {"lecturerId", "courses"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public void addLecturerCourses (RoutingContext ctx) {
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		JsonObject body = ctx.getBodyAsJson();  
		
		Long lecturerId = body.getLong("lecturerId");
		List<String> courses = body.getJsonArray("courses").getList();
		
		DirectoryModel.addLecturerCourses(principal, lecturerId, courses);
	}
	
	@EndpointMethod(uri = "/remove-lecturer-course", bodyParams = {"lecturerId", "course"}, method = HttpMethod.DELETE,
			functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public void removeLecturerCourse (RoutingContext ctx) {
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long lecturerId = body.getLong("lecturerId");
		String course = body.getString("courses");
		
		DirectoryModel.removeCourses(principal, lecturerId, course);
	}
	
}
