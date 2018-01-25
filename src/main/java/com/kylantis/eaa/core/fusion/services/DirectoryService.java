package com.kylantis.eaa.core.fusion.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.ClientResources.ClientRBRef;
import com.ce.ems.base.classes.NumberBound;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.spec.AcademicSemesterSpec;
import com.ce.ems.base.classes.spec.BaseCourseSpec;
import com.ce.ems.base.classes.spec.CourseSpec;
import com.ce.ems.base.classes.spec.DepartmentSpec;
import com.ce.ems.base.classes.spec.FacultySpec;
import com.ce.ems.base.classes.spec.LecturerSpec;
import com.ce.ems.base.classes.spec.StudentSpec;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.DModel;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.CacheAdapter;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.keys.CacheKeys;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/directory")
public class DirectoryService extends BaseService {

	@EndpointMethod(uri = "/new-academic-semester", bodyParams = {
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public void newAcademicSemester(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		AcademicSemesterSpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(),
				AcademicSemesterSpec.class);

		Long id = DModel.newAcademicSemester(principal, spec);

		ctx.response().write(id.toString()).setChunked(true).end();
	}

	@EndpointMethod(uri = "/end-current-semester", functionality = Functionality.MANAGE_SEMESTER_TIMELINE)
	public void endCurrentSemester(RoutingContext ctx) {
		DModel.endSemester();
	}

	@EndpointMethod(uri = "/get-current-semester", functionality = Functionality.GET_CURRENT_SEMESTER)
	public void getCurrentSemester(RoutingContext ctx) {
		AcademicSemesterSpec spec = DModel.getCurrentAcademicSemester();
		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/list-academic-semester", functionality = Functionality.LIST_ACADEMIC_SEMESTER)
	public void listAcademicSemester(RoutingContext ctx) {
		List<AcademicSemesterSpec> result = DModel.listAcademicSemester();
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/create-department", bodyParams = {
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_DEPARTMENTS)
	public void createDepartment(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		DepartmentSpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(),
				DepartmentSpec.class);

		Long id = DModel.createDepartment(principal, spec);

		ctx.response().write(id.toString()).setChunked(true);
		
		CacheAdapter.del(CacheKeys.DEPARTMENT_LIST_$FACULTY.replace("$FACULTY", spec.getFaculty().toString()));
		CacheAdapter.del(CacheKeys.DEPARTMENT_LIST);
	}

	@EndpointMethod(uri = "/can-manage-departments", requestParams = {}, functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void canUserManageDepartments(RoutingContext ctx) {
		isAccessAllowed(ctx, Functionality.MANAGE_DEPARTMENTS);
	}

	@EndpointMethod(uri = "/get-department", requestParams = {
			"departmentId" }, functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void getDepartment(RoutingContext ctx) {

		Long departmentId = Long.parseLong(ctx.request().getParam("departmentId"));

		DepartmentSpec spec = DModel.getDepartment(departmentId);

		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-all-semesters", requestParams = {}, method = HttpMethod.GET, functionality = Functionality.GET_ALL_SEMESTERS)
	public void getAllSemesters(RoutingContext ctx) {

		Semester[] semesters = Semester.values();
		Map<Integer, String> result = new HashMap<>(semesters.length);

		for (Semester s : semesters) {
			result.put(s.getValue(), ClientRBRef.get(Utils.prettify(s.toString())).toString());
		}

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/get-department-for-level", requestParams = {
			"departmentLevelId" }, functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void getDepartmentForLevel(RoutingContext ctx) {

		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));

		Long department = DModel.getDepartmentForLevel(departmentLevelId);

		ctx.response().write(GsonFactory.newInstance().toJson(department)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-associated-department-levels", requestParams = {
			"departmentLevelId" }, functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void getAssociatedDepartmentLevels(RoutingContext ctx) {

		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));

		Map<Long, String> result = DModel.getAssociatedDepartmentLevels(departmentLevelId);

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/list-department-levels", requestParams = {
			"departmentId" }, functionality = Functionality.LIST_DEPARTMENT_LEVELS)
	public void listDepartmentLevels(RoutingContext ctx) {

		Long departmentId = Long.parseLong(ctx.request().getParam("departmentId"));

		Map<Long, String> result = DModel.listDepartmentLevels(departmentId);

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/list-department-names", requestParams = {
			"facultyId" }, functionality = Functionality.LIST_DEPARTMENT_NAMES)
	public void listDepartmentNames(RoutingContext ctx) {

		String facultyId = ctx.request().getParam("facultyId");

		Map<Long, String> result = facultyId != null ? DModel.listDepartmentNames(Long.parseLong(facultyId))
				: DModel.listDepartmentNames();

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/list-departments", requestParams = {
			"facultyId" }, functionality = Functionality.VIEW_DEPARTMENT_PROFILES)
	public void listDepartments(RoutingContext ctx) {

		String facultyId = ctx.request().getParam("facultyId");

		String cacheKey = !facultyId.equals("undefined")
				? CacheKeys.DEPARTMENT_LIST_$FACULTY.replace("$FACULTY", facultyId)
				: CacheKeys.DEPARTMENT_LIST;

		String json = (String) CacheAdapter.get(cacheKey);
		
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			
			List<DepartmentSpec> result = !facultyId.equals("undefined")
					? DModel.listDepartments(Long.parseLong(facultyId))
					: DModel.listDepartments();
			json = GsonFactory.newInstance().toJson(result);
			CacheAdapter.put(cacheKey, json);
			ctx.response().write(json).setChunked(true);
		}
	}

	@EndpointMethod(uri = "/create-faculty", bodyParams = {
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_FACULTIES)
	public void createFaculty(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		FacultySpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), FacultySpec.class);

		Long id = DModel.createFaculty(principal, spec);

		ctx.response().write(id.toString()).setChunked(true).end();
	}

	@EndpointMethod(uri = "/can-manage-faculties", requestParams = {}, functionality = Functionality.VIEW_FACULTY_PROFILES)
	public void canUserManageFaculties(RoutingContext ctx) {
		isAccessAllowed(ctx, Functionality.MANAGE_FACULTIES);
	}

	@EndpointMethod(uri = "/get-faculty", requestParams = {
			"facultyId" }, functionality = Functionality.VIEW_FACULTY_PROFILES)
	public void getFaculty(RoutingContext ctx) {

		Long facultyId = Long.parseLong(ctx.request().getParam("facultyId"));

		FacultySpec spec = DModel.getFaculty(facultyId);

		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/list-faculties", functionality = Functionality.VIEW_FACULTY_PROFILES)
	public void listFaculties(RoutingContext ctx) {

		List<FacultySpec> result = DModel.listFaculties();

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/list-faculty-names", functionality = Functionality.LIST_FACULTY_NAMES)
	public void listFacultyNames(RoutingContext ctx) {

		Map<Long, String> result = DModel.listFacultyNames();

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-student", requestParams = {
			"studentId" }, functionality = Functionality.VIEW_STUDENT_PROFILES)
	public void getStudent(RoutingContext ctx) {

		Long studentId = Long.parseLong(ctx.request().getParam("studentId"));

		StudentSpec spec = DModel.getStudent(studentId);

		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-students", requestParams = {
			"departmentLevelId" }, functionality = Functionality.VIEW_STUDENT_PROFILES)
	public void getStudents(RoutingContext ctx) {

		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));

		List<StudentSpec> result = DModel.getStudents(departmentLevelId);

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-lecturer", requestParams = {
			"lecturerId" }, functionality = Functionality.VIEW_LECTURER_PROFILES)
	public void getLecturer(RoutingContext ctx) {

		Long lecturerId = Long.parseLong(ctx.request().getParam("lecturerId"));

		LecturerSpec result = DModel.getLecturer(lecturerId);

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-lecturers", requestParams = { "departmentId",
			"departmentLevelId" }, functionality = Functionality.VIEW_LECTURER_PROFILES)
	public void getLecturers(RoutingContext ctx) {

		String departmentId = ctx.request().getParam("departmentId");
		String departmentLevelId = ctx.request().getParam("departmentLevelId");

		List<LecturerSpec> result = null;

		if (!departmentId.equals("undefined")) {
			result = DModel.getLecturers(Long.parseLong(departmentId));
		} else {
			result = DModel.getLecturers(DModel.getDepartmentForLevel(Long.parseLong(departmentLevelId)));
		}

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/create-course", bodyParams = {
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_COURSES)
	public void createCourse(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();
		CourseSpec spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), CourseSpec.class);

		DModel.createCourse(principal, spec);
	}

	@EndpointMethod(uri = "/can-manage-courses", requestParams = {}, functionality = Functionality.VIEW_COURSES)
	public void canUserManageCourses(RoutingContext ctx) {
		isAccessAllowed(ctx, Functionality.MANAGE_COURSES);
	}

	@EndpointMethod(uri = "/get-course", requestParams = { "courseCode" }, functionality = Functionality.VIEW_COURSES)
	public void getCourse(RoutingContext ctx) {

		String courseCode = ctx.request().getParam("courseCode");

		CourseSpec spec = DModel.getCourse(courseCode);

		ctx.response().write(GsonFactory.newInstance().toJson(spec)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-semester-courses", requestParams = { "departmentLevel",
			"academicSemesterId" }, functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public void getSemesterCourses(RoutingContext ctx) {

		String departmentLevel = ctx.request().getParam("departmentLevel");
		String academicSemesterId = ctx.request().getParam("academicSemesterId");

		Map<Semester, List<BaseCourseSpec>> result = DModel.listCoursesForSemesterLevel(Long.parseLong(departmentLevel),
				Long.parseLong(academicSemesterId));

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/get-level-courses", requestParams = {
			"departmentLevel" }, functionality = Functionality.VIEW_COURSES)
	public void getLevelCourses(RoutingContext ctx) {

		String departmentLevel = ctx.request().getParam("departmentLevel");

		Map<Semester, List<BaseCourseSpec>> result = DModel.listAllCoursesForLevel(Long.parseLong(departmentLevel));

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/get-available-courses", functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void getAvailableCourses(RoutingContext ctx) {

		Map<Semester, List<BaseCourseSpec>> result = DModel
				.getAcademicSemesterCourses(FusionHelper.getUserId(ctx.request()));

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/list-student-courses", requestParams = {
			"studentId" }, functionality = Functionality.REGISTER_STUDENT_COURSES)
	public void listStudentCourses(RoutingContext ctx) {

		Long studentId = Long.parseLong(ctx.request().getParam("studentId"));

		Map<Semester, List<BaseCourseSpec>> result = DModel.listStudentCourses(studentId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/register-student-courses", bodyParams = { "studentId",
			"courses" }, method = HttpMethod.PUT, functionality = Functionality.REGISTER_STUDENT_COURSES)
	public void registerStudentCourses(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long studentId = body.getLong("studentId");
		List<String> courses = body.getJsonArray("courses").getList();

		DModel.registerStudentCourses(studentId, courses);
	}

	@EndpointMethod(uri = "/get-student-semester-courses", requestParams = {
			"studentId" }, method = HttpMethod.GET, functionality = Functionality.VIEW_STUDENT_PROFILES)
	public void getStudentSemesterCourses(RoutingContext ctx) {

		Long studentId = Long.parseLong(ctx.request().getParam("studentId"));

		List<BaseCourseSpec> result = DModel.getStudentSemesterCourses(studentId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/get-student-semester-performance", requestParams = {
			"studentId" }, method = HttpMethod.GET, functionality = Functionality.VIEW_STUDENT_PROFILES)
	public void getStudentSemesterPerformance(RoutingContext ctx) {

		Long studentId = Long.parseLong(ctx.request().getParam("studentId"));

		Map<String, NumberBound> result = DModel.getStudentSemesterPerformance(studentId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/add-lecturer-courses", bodyParams = { "lecturerId",
			"courses" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public void addLecturerCourses(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();

		Long lecturerId = body.getLong("lecturerId");
		List<String> courses = body.getJsonArray("courses").getList();

		DModel.addLecturerCourses(principal, lecturerId, courses);
	}

	@EndpointMethod(uri = "/remove-lecturer-course", bodyParams = { "lecturerId",
			"course" }, method = HttpMethod.DELETE, functionality = Functionality.MANAGE_LECTURER_PROFILES)
	public void removeLecturerCourse(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		JsonObject body = ctx.getBodyAsJson();

		Long lecturerId = body.getLong("lecturerId");
		String course = body.getString("courses");

		DModel.removeCourses(principal, lecturerId, course);
	}

}
