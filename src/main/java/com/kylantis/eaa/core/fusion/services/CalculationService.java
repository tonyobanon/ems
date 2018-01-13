package com.kylantis.eaa.core.fusion.services;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.ScoreSheet;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.CalculationModel;
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

	@EndpointMethod(uri = "/get-academic-semester-courses", bodyParams = {"academicSemesterId", "courseCodes"}, method = HttpMethod.POST,
			functionality = Functionality.VIEW_SEMESTER_COURSE_RESULT)
	public void getAcademicSemesterCourses (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long academicSemesterId = body.getLong("academicSemesterId");
		List<String> courseCodes = body.getJsonArray("courseCodes").getList();
		
		Map<String, AcademicSemesterCourseSpec> result = CalculationModel.getAcademicSemesterCourses(academicSemesterId, courseCodes);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/is-semester-course-sheet-created", requestParams = {"courseCode"},
			functionality = Functionality.MANAGE_COURSE_SCORE_SHEET)
	public void isSemesterCourseSheetCreated (RoutingContext ctx) {
		
		Long principal = FusionHelper.getUserId(ctx.request());
		String courseCode = ctx.request().getParam("courseCode");
		
		Boolean result = CalculationModel.isSemesterCourseSheetCreated(principal, courseCode);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/create-score-sheet", bodyParams = {"academicSemesterCourseId"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_COURSE_SCORE_SHEET)
	public void createScoreSheet (RoutingContext ctx) { 
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		Long academicSemesterCourseId = Long.parseLong(body.getString("academicSemesterCourseId"));
		
		Long result = CalculationModel.createScoreSheet(principal, academicSemesterCourseId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/get-score-sheet", requestParams = {"courseCode", "academicSemesterId"},
			functionality = Functionality.VIEW_COURSE_SCORE_SHEET)
	public void getScoreSheet(RoutingContext ctx) {
		 
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		String courseCode = ctx.request().getParam("courseCode");
		String academicSemesterId = ctx.request().getParam("academicSemesterId");
	
		ScoreSheet result = CalculationModel.getScoreSheet(principal, courseCode, !academicSemesterId.equals("undefined")
				? Long.parseLong(academicSemesterId) : null);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-score-sheet-with-id", requestParams = {"academicSemesterCourseId"},
			functionality = Functionality.VIEW_COURSE_SCORE_SHEET)
	public void getScoreSheetWithId(RoutingContext ctx) { 
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		Long academicSemesterCourseId = Long.parseLong(ctx.request().getParam("academicSemesterCourseId"));
	
		ScoreSheet result = CalculationModel.getScoreSheet(principal, academicSemesterCourseId, null);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	} 
	 
	@EndpointMethod(uri = "/update-score-sheet", bodyParams = {"academicSemesterCourseId", "updates"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_COURSE_SCORE_SHEET)
	public void updateScoreSheet (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		Long academicSemesterCourseId = body.getLong("academicSemesterCourseId");
		Map<String, Object> updates = body.getJsonObject("updates").getMap();
		
		Map<Long, List<Integer>> scores = new FluentHashMap<>();
		updates.forEach((k,v) -> {
			scores.put(Long.parseLong(k), (List<Integer>) v);
		});
		
		List<Long> result = CalculationModel.updateScoreSheet(principal, academicSemesterCourseId, scores);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/submit-score-sheet", bodyParams = {"academicSemesterCourseId"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_COURSE_SCORE_SHEET)
	public void submitScoreSheet (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		
		Long academicSemesterCourseId = Long.parseLong(body.getString("academicSemesterCourseId"));
		
		CalculationModel.submitScoreSheet(principal, academicSemesterCourseId);
	}
	
	@EndpointMethod(uri = "/get-level-semester", requestParams = {"departmentLevelId", "semester"},
			functionality = Functionality.LIST_LEVEL_SEMESTERS)
	public void getLevelSemester(RoutingContext ctx) {
		
		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));
		Semester semester = Semester.from(Integer.parseInt(ctx.request().getParam("semester")));
		
		Long result = CalculationModel.getLevelSemester(departmentLevelId, semester);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-level-assessment-totals", requestParams = {"departmentLevelId", "levelSemesterId"},
			functionality = Functionality.VIEW_ASSESSMENT_TOTALS)
	public void getLevelAssessmentTotals(RoutingContext ctx) {
		
		Long departmentLevelId = Long.parseLong(ctx.request().getParam("departmentLevelId"));
		Long levelSemesterId = Long.parseLong(ctx.request().getParam("levelSemesterId"));
		
		List<AssessmentTotalSpec> result = CalculationModel.getAssessmentTotalsForLevel(departmentLevelId, levelSemesterId);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/new-assessment-total", bodyParams = {"spec"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public void newAssessmentTotal (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		AssessmentTotalSpec spec = GsonFactory.newInstance().fromJson(body.getString("spec"), AssessmentTotalSpec.class);
		
		Long result = CalculationModel.newAssessmentTotal(principal, spec);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/delete-assessment-total", bodyParams = {"totalId"}, method = HttpMethod.POST,
			functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public void deleteAssessmentTotal (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		Long totalId = body.getLong("totalId");
		
		CalculationModel.deleteAssessmentTotal(principal, totalId);
	}

}
