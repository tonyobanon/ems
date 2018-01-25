package com.kylantis.eaa.core.fusion.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.ScoreGradeSpec;
import com.ce.ems.base.classes.spec.ScoreSheet;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.CalculationModel;
import com.google.gson.reflect.TypeToken;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/calculation")
public class CalculationService extends BaseService {

	@EndpointMethod(uri = "/can-manage-score-grades", requestParams = {}, functionality = Functionality.VIEW_SCORE_GRADES)
	public void canUserManageScoreGrades(RoutingContext ctx) {
		isAccessAllowed(ctx, Functionality.MANAGE_SCORE_GRADES);
	}
	
	@EndpointMethod(uri = "/get-score-grades", functionality = Functionality.VIEW_SCORE_GRADES)
	public void getScoreGrades(RoutingContext ctx) {
		List<ScoreGradeSpec> result = CalculationModel.getScoreGrades();
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/update-score-grades", bodyParams = {"grades"}, method = HttpMethod.PUT, functionality = Functionality.MANAGE_SCORE_GRADES)
	public void updateScoreGrades(RoutingContext ctx) {
		
		Long principal = FusionHelper.getUserId(ctx.request());
		JsonObject body = ctx.getBodyAsJson();
		
		String gradesJson = body.getJsonObject("grades").encode();
		
		Map<String, List<Integer>> grades = GsonFactory.newInstance().fromJson(gradesJson, new TypeToken<Map<String, List<Integer>>>() {}.getType());
		
		List<ScoreGradeSpec> spec = new ArrayList<>(grades.size());
		
		grades.forEach((k, v) -> {
			spec.add(new ScoreGradeSpec(k, v.get(0), v.get(1)));
		});
		
		CalculationModel.updateScoreGrades(principal, spec);
		
		ctx.response().write(GsonFactory.newInstance().toJson(grades.keySet())).setChunked(true).end();
	}

	@EndpointMethod(uri = "/is-semester-course-sheet-created", requestParams = {
			"courseCode" }, functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void isSemesterCourseSheetCreated(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());
		String courseCode = ctx.request().getParam("courseCode");

		Boolean result = CalculationModel.isSemesterCourseSheetCreated(principal, courseCode);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/can-manage-score-sheet", requestParams = {
			"academicSemesterCourseId" }, functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public void canManageResultSheet(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		Long academicSemesterCourseId = Long.parseLong(ctx.request().getParam("academicSemesterCourseId"));

		Boolean hasAccess = CalculationModel.canManageResultSheet(principal, academicSemesterCourseId);
		ctx.response().write(GsonFactory.newInstance().toJson(hasAccess)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/create-score-sheet", bodyParams = {
			"academicSemesterCourseId" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void createScoreSheet(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());
		Long academicSemesterCourseId = Long.parseLong(body.getString("academicSemesterCourseId"));

		Long result = CalculationModel.createScoreSheet(principal, academicSemesterCourseId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-score-sheet", requestParams = { "courseCode",
			"academicSemesterId" }, functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public void getScoreSheet(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		String courseCode = ctx.request().getParam("courseCode");
		String academicSemesterId = ctx.request().getParam("academicSemesterId");

		ScoreSheet result = CalculationModel.getScoreSheet(principal, courseCode,
				!academicSemesterId.equals("undefined") ? Long.parseLong(academicSemesterId) : null);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-score-sheet-with-id", requestParams = {
			"academicSemesterCourseId" }, functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public void getScoreSheetWithId(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());

		Long academicSemesterCourseId = Long.parseLong(ctx.request().getParam("academicSemesterCourseId"));

		ScoreSheet result = CalculationModel.getScoreSheet(principal, academicSemesterCourseId, null);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/update-score-sheet", bodyParams = { "academicSemesterCourseId",
			"updates" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void updateScoreSheet(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long academicSemesterCourseId = body.getLong("academicSemesterCourseId");
		Map<String, Object> updates = body.getJsonObject("updates").getMap();

		Map<Long, List<Integer>> scores = new FluentHashMap<>();
		updates.forEach((k, v) -> {
			scores.put(Long.parseLong(k), (List<Integer>) v);
		});

		List<Long> result = CalculationModel.updateScoreSheet(principal, academicSemesterCourseId, scores);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/submit-score-sheet", bodyParams = {
			"academicSemesterCourseId" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void submitScoreSheet(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());

		Long academicSemesterCourseId = body.getLong("academicSemesterCourseId");

		CalculationModel.submitScoreSheet(principal, academicSemesterCourseId);
	}

	@EndpointMethod(uri = "/get-level-semester", requestParams = { "departmentLevelId",
			"semester" }, functionality = Functionality.LIST_LEVEL_SEMESTERS)
	public void getLevelSemester(RoutingContext ctx) {

		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));
		Semester semester = Semester.from(Integer.parseInt(ctx.request().getParam("semester")));

		Long result = CalculationModel.getLevelSemester(departmentLevelId, semester);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-assessment-totals", requestParams = {"levelSemesterId" }, functionality = Functionality.VIEW_ASSESSMENT_TOTALS)
	public void getAssessmentTotals(RoutingContext ctx) {

		Long principal = FusionHelper.getUserId(ctx.request());
		Long levelSemesterId = Long.parseLong(ctx.request().getParam("levelSemesterId"));

		List<AssessmentTotalSpec> result = CalculationModel.getAssessmentTotalsForLevel(principal,
				levelSemesterId);
		
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/new-assessment-total", bodyParams = {
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public void newAssessmentTotal(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());
		AssessmentTotalSpec spec = GsonFactory.newInstance().fromJson(body.getString("spec"),
				AssessmentTotalSpec.class);

		Long result = CalculationModel.newAssessmentTotal(principal, spec);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/delete-assessment-total", bodyParams = {
			"totalId" }, method = HttpMethod.POST, functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public void deleteAssessmentTotal(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long principal = FusionHelper.getUserId(ctx.request());
		Long totalId = body.getLong("totalId");

		CalculationModel.deleteAssessmentTotal(principal, totalId);
	}

}
