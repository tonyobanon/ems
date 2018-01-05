package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.AssessmentTotalType;
import com.ce.ems.base.classes.BooleanWrapper;
import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.EntityUtils;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.IntegerWrapper;
import com.ce.ems.base.classes.QueryFilter;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.SystemErrorCodes;
import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.ScoreSheet;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.Model;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.SystemValidationException;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.calculation.AcademicSemesterCourseEntity;
import com.ce.ems.entites.calculation.ResultRecordSheetEntity;
import com.ce.ems.entites.directory.AssessmentTotalEntity;
import com.ce.ems.entites.directory.LevelSemesterEntity;
import com.google.common.collect.Lists;
import com.kylantis.eaa.core.fusion.Unexposed;
import com.kylantis.eaa.core.keys.ConfigKeys;
import com.kylantis.eaa.core.users.Functionality;

@BlockerTodo("Create configuration that allows the admin to specify grade for scores, and from this"
		+ " derive analytics metrics just before result score sheet becomes final")

@Todo("Create setting to create global assessment total")

@Model(dependencies = {DirectoryModel.class})
public class CalculationModel extends BaseModel {

	@Override
	public String path() {
		return "core/calculation";
	}
	
	@Override
	public void preInstall() {
		
		ConfigModel.put(ConfigKeys.MAX_CARRY_OVER_UNIT_LOAD, 10);
	}

	@Override
	public void install(InstallOptions options) {
	}

	

	@ModelMethod(functionality = Functionality.VIEW_SEMESTER_COURSE_RESULT)
	public static Map<String, AcademicSemesterCourseSpec> getAcademicSemesterCourses(Long academicSemesterId, List<String> courseCodes) {

		Map<String, AcademicSemesterCourseSpec> result = new FluentHashMap<>();
		
		courseCodes.forEach(courseCode -> {

			AcademicSemesterCourseEntity e = EntityUtils.lazyQuery(AcademicSemesterCourseEntity.class,
					QueryFilter.get("academicSemesterId", academicSemesterId.toString()),
					QueryFilter.get("courseCode", courseCode)).first().safe();
			
			result.put(courseCode, EntityHelper.toObjectModel(e));
			
		});
		
		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static Boolean isSemesterCourseSheetCreated(Long principal, String courseCode) {

		if (principal != -1 && !DirectoryModel.isCourseLecturer(principal, courseCode)) {
			throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
		}

		return ofy().load().type(AcademicSemesterCourseEntity.class)
				.filter("academicSemesterId = ", DirectoryModel.currentSemesterId().toString()).filter("courseCode = ", courseCode)
				.first().safe().getIsSheetCreated();
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static Long createScoreSheet(Long principal, String courseCode) {

		if (!DirectoryModel.isCourseLecturer(principal, courseCode)) {
			throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
		}

		Long currentSemesterId = DirectoryModel.currentSemesterId();

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.filter("academicSemesterId = ", currentSemesterId.toString()).filter("courseCode = ", courseCode).first().safe();

		if (e.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_ALREADY_CREATED_FOR_SEMESTER_COURSE);
		}

		// add totals
		List<Long> totals = validateTotals(DirectoryModel.getCourseLevelSemester(e.getCourseCode()));

		e.setTotals(totals);

		List<Short> defaultScores = new ArrayList<>();
		e.getTotals().forEach(total -> {
			defaultScores.add((short) 0);
		});

		
		List<ResultRecordSheetEntity> sheetEntries = new ArrayList<>();
		
		e.getStudents().forEach(s -> {

			ResultRecordSheetEntity re = new ResultRecordSheetEntity().setAcademicSemesterCourseId(e.getId())
					.setStudentId(s).setScores(defaultScores).setTotal((short) 0);
			
			sheetEntries.add(re);
		});
		
		ofy().save().entities(sheetEntries).now();
		

		e.setIsSheetCreated(true);

		ofy().save().entity(e).now();

		return e.getId();
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static ScoreSheet getScoreSheet(Long principal, String courseCode) {

		if (!DirectoryModel.isCourseLecturer(principal, courseCode)) {
			throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
		}

		ScoreSheet result = new ScoreSheet();

		Long currentSemesterId = DirectoryModel.currentSemesterId();

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.filter("academicSemesterId = ", currentSemesterId).filter("courseCode = ", courseCode).first().safe();

		if (!e.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_NOT_CREATED_FOR_SEMESTER_COURSE);
		}

		result.setAcademicSemesterCourseId(e.getId());

		result.setTotals(getAcademicSemesterCourseTotalValues(e.getId()));

		ofy().load().type(ResultRecordSheetEntity.class).filter("academicSemesterCourseId = ", e.getId())
				.forEach(se -> {
					result.addRecord(EntityHelper.toObjectModel(se));
				});

		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static List<Long> updateScoreSheet(Long principal, Long academicSemesterCourseId,
			Map<Long, List<Short>> scores) {

		// Get associated course code
		String courseCode = ofy().load().type(AcademicSemesterCourseEntity.class).id(academicSemesterCourseId).safe()
				.getCourseCode();

		if (!DirectoryModel.isCourseLecturer(principal, courseCode)) {
			throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
		}

		// This represents the score sheet ids whose scores were successfully updated
		List<Long> result = new FluentArrayList<>();

		List<Integer> totals = getAcademicSemesterCourseTotalValues(academicSemesterCourseId);

		scores.forEach((k, v) -> {

			// validate scores against totals

			for (int i = 0; i < totals.size(); i++) {
				if (v.get(i) > totals.get(i)) {
					throw new SystemValidationException(SystemErrorCodes.STUDENT_SCORE_EXCEEDS_RESULT_SHEET_TOTAL,
							k.toString());
				}
			}

			ResultRecordSheetEntity e = ofy().load().type(ResultRecordSheetEntity.class).id(k).safe();
			e.setScores(v);

			short total = 0;
			for (Short s : v) {
				total += s;
			}

			e.setTotal(total);

			ofy().save().entity(e).now();

			result.add(k);
		});

		return result;
	}
	

	@Unexposed
	@BlockerTodo
	@ModelMethod(functionality = Functionality.VIEW_SEMESTER_COURSE_RESULT)
	public static Long getSemesterCourseReport(Long academicSemesterCourseId) {

		// AcademicSemesterCourseEntity e;

		return null;
	}

	@Unexposed
	@BlockerTodo
	@ModelMethod(functionality = Functionality.VIEW_SEMESTER_STUDENT_RESULT)
	public static Long getStudentSemesterReport(Long studentId) {

		// StudentSemesterCoursesEntity e;

		return null;
	}
	
	protected static Long getLevelSemester(Long departmentLevel, Semester s) {

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevel.toString()).filter("semester = ", s.getValue()).first().safe();

		return ls.getId();
	}

	@ModelMethod(functionality = Functionality.VIEW_ASSESSMENT_TOTALS)
	public static Map<Semester, List<AssessmentTotalSpec>> getAssessmentTotalsForLevel(Long departmentLevel) {

		Map<Semester, List<AssessmentTotalSpec>> result = new FluentHashMap<>();

		for (Semester s : Semester.values()) {

			List<AssessmentTotalSpec> totals = new FluentArrayList<>();

			ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", getLevelSemester(departmentLevel, s).toString()).forEach(e -> {
				totals.add(EntityHelper.toObjectModel(e));
			});

			result.put(s, totals);
		}
		return result;
	}

	protected static List<AssessmentTotalEntity> getAssessmentTotals(Long levelSemester) {

		List<AssessmentTotalEntity> totals = new FluentArrayList<>();

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemester.toString()).forEach(e -> {
			totals.add(e);
		});

		return totals;
	}

	protected static List<Integer> getAcademicSemesterCourseTotalValues(Long academicSemesterCourseId) {

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		if (!e.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.COURSE_RESULT_SHEET_DOES_NOT_EXIST);
		}

		List<Long> totalIds = EntityHelper.toObjectModel(e).getTotals();

		return Lists.newArrayList(getAssessmentTotalValues(totalIds).values());
	}

	protected static Map<Long, Integer> getAssessmentTotalValues(List<Long> ids) {

		Map<Long, Integer> totals = new FluentHashMap<>();

		ofy().load().type(AssessmentTotalEntity.class).ids(ids).forEach((k, v) -> {
			totals.put(k, EntityHelper.toObjectModel(v).getPercentile());
		});

		return totals;
	}

	protected static List<Long> getAssessmentTotalKeys(Long levelSemester) {

		List<Long> totals = new FluentArrayList<>();

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemester.toString()).forEach(e -> {
			totals.add(e.getId());
		});

		return totals;
	}

	@ModelMethod(functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public static Long newAssessmentTotal(AssessmentTotalSpec spec) {

		if (spec.getPercentile() < 1) {
			throw new SystemValidationException(SystemErrorCodes.INVALID_PERCENTILE_ENTERED);
		}

		// verify that all percentiles don't exceed 100

		IntegerWrapper totalPercentile = new IntegerWrapper();

		CalculationModel.getAssessmentTotals(spec.getLevelSemester()).forEach(e -> {
			totalPercentile.add(e.getPercentile());
		});

		totalPercentile.add(spec.getPercentile());

		if (totalPercentile.getValue() > 100) {
			throw new SystemValidationException(SystemErrorCodes.TOTAL_PERCENTILES_SUM_MORE_THAN_100);
		}
		
		AssessmentTotalEntity e = EntityHelper.fromObjectModel(spec);

		ofy().save().entity(e).now();
		
		return e.getId();
	}

	private static List<Long> validateTotals(Long levelSemester) {

		List<Long> result = new FluentArrayList<>();

		List<AssessmentTotalEntity> entities = new FluentArrayList<>();

		IntegerWrapper totalPercentile = new IntegerWrapper();
		BooleanWrapper isExamTotal = new BooleanWrapper();

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemester.toString()).forEach(e -> {

			entities.add(e);
			result.add(e.getLevelSemester());

			totalPercentile.add(e.getPercentile());

			if (e.getType().equals(AssessmentTotalType.EXAM.getValue()) && isExamTotal.getValue()) {
				throw new SystemValidationException(SystemErrorCodes.MULTIPLE_EXAM_TYPED_ASSESSMENT_TOTAL);
			}

			isExamTotal.setValue(e.getType().equals(AssessmentTotalType.EXAM.getValue()));
		});

		// at least one AssessmentTotal must be of type exam
		if (!isExamTotal.getValue()) {
			throw new SystemValidationException(SystemErrorCodes.NO_EXAM_TYPED_ASSESSMENT_TOTAL);
		}

		// verify that all percentiles are exactly 100

		if (totalPercentile.getValue() != 100) {
			throw new SystemValidationException(
					totalPercentile.getValue() < 100 ? SystemErrorCodes.TOTAL_PERCENTILES_SUM_LESS_THAN_100
							: SystemErrorCodes.TOTAL_PERCENTILES_SUM_MORE_THAN_100);
		}

		// Now that validation has passed, mark these totals as validated to prevent
		// deletion in the future

		entities.forEach(e -> {
			if (!e.getIsValidated()) {
				ofy().save().entity(e.setIsValidated(true)).now();
			}
		});

		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public static void deleteAssessmentTotal(Long id) {
		AssessmentTotalEntity e = ofy().load().type(AssessmentTotalEntity.class).id(id).safe();

		if (e.getIsValidated()) {
			throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_ALREADY_CREATED_FOR_ASSESSMENT_TOTAL);
		}

		ofy().delete().type(AssessmentTotalEntity.class).id(id).now();
	}

}
