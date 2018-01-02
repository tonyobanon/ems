package com.kylantis.eaa.core.fusion.services;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
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
			functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void isSemesterCourseSheetCreated (RoutingContext ctx) {
		
		Long principal = FusionHelper.getUserId(ctx.request());
		String courseCode = ctx.request().getParam("courseCode");
		
		Boolean result = CalculationModel.isSemesterCourseSheetCreated(principal, courseCode);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/create-course-sheet", bodyParams = {"courseCode"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void createScoreSheet (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		String courseCode = body.getString("courseCode");
		
		Long result = CalculationModel.createScoreSheet(principal, courseCode);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}

	@EndpointMethod(uri = "/update-course-sheet", bodyParams = {"academicSemesterCourseId", "updates"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public void updateScoreSheet (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long principal = FusionHelper.getUserId(ctx.request());	
		Long academicSemesterCourseId = Long.parseLong(body.getString("academicSemesterCourseId"));
		Map<String, Object> updates = body.getJsonObject("updates").getMap();
		
		Map<Long, List<Short>> scores = new FluentHashMap<>();
		updates.forEach((k,v) -> {
			scores.put(Long.parseLong(k), (List<Short>) v);
		});
		
		List<Long> result = CalculationModel.updateScoreSheet(principal, academicSemesterCourseId, scores);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/get-level-assessment-totals", requestParams = {"departmentLevel"},
			functionality = Functionality.VIEW_ASSESSMENT_TOTALS)
	public void getLevelAssessmentTotals(RoutingContext ctx) {
		
		Long departmentLevel = Long.parseLong(ctx.request().getParam("departmentLevel"));
		
		Map<Semester, List<AssessmentTotalSpec>> result = CalculationModel.getAssessmentTotalsForLevel(departmentLevel);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/new-assessment-total", bodyParams = {"spec"}, method = HttpMethod.PUT,
			functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public void newAssessmentTotal (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		AssessmentTotalSpec spec = GsonFactory.newInstance().fromJson(body.getString("spec"), AssessmentTotalSpec.class);
		
		Long result = CalculationModel.newAssessmentTotal(spec);
		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true).end();
	}
	
	@EndpointMethod(uri = "/delete-assessment-total", bodyParams = {"totalId"}, method = HttpMethod.POST,
			functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public void deleteAssessmentTotal (RoutingContext ctx) {
		
		JsonObject body = ctx.getBodyAsJson();
		
		Long totalId = body.getLong("totalId");
		
		CalculationModel.deleteAssessmentTotal(totalId);
	}

}
