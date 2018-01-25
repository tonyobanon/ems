package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.ce.ems.base.api.event_streams.CustomPredicate;
import com.ce.ems.base.api.event_streams.ObjectEntity;
import com.ce.ems.base.api.event_streams.ObjectType;
import com.ce.ems.base.api.event_streams.Preposition;
import com.ce.ems.base.api.event_streams.Sentence;
import com.ce.ems.base.api.event_streams.SubjectEntity;
import com.ce.ems.base.api.event_streams.SubjectType;
import com.ce.ems.base.classes.AssessmentTotalType;
import com.ce.ems.base.classes.ClientResources.ClientRBRef;
import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.EntityUtils;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.FluentHashMap;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.IntegerWrapper;
import com.ce.ems.base.classes.ObjectWrapper;
import com.ce.ems.base.classes.QueryFilter;
import com.ce.ems.base.classes.ScoreGrade;
import com.ce.ems.base.classes.Semester;
import com.ce.ems.base.classes.SystemErrorCodes;
import com.ce.ems.base.classes.spec.AcademicSemesterCourseSpec;
import com.ce.ems.base.classes.spec.AssessmentTotalSpec;
import com.ce.ems.base.classes.spec.DepartmentLevelSpec;
import com.ce.ems.base.classes.spec.ResultRecordSheetSpec;
import com.ce.ems.base.classes.spec.ScoreGradeSpec;
import com.ce.ems.base.classes.spec.ScoreSheet;
import com.ce.ems.base.classes.spec.TotalSpec;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.Logger;
import com.ce.ems.base.core.Model;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.ResourceException;
import com.ce.ems.base.core.SystemValidationException;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.ScoreGradeEntity;
import com.ce.ems.entites.calculation.AcademicSemesterCourseEntity;
import com.ce.ems.entites.calculation.ResultRecordSheetEntity;
import com.ce.ems.entites.calculation.StudentSemesterCoursesEntity;
import com.ce.ems.entites.directory.AssessmentTotalEntity;
import com.ce.ems.entites.directory.CourseEntity;
import com.ce.ems.entites.directory.DepartmentalLevelEntity;
import com.ce.ems.entites.directory.LevelSemesterEntity;
import com.ce.ems.entites.directory.StudentEntity;
import com.google.common.collect.Lists;
import com.kylantis.eaa.core.keys.ConfigKeys;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;

@BlockerTodo("Create configuration that allows the admin to specify grade for scores, and from this"
		+ " derive analytics metrics just before result score sheet becomes final")

@Todo("Create setting to create global assessment total")

@Model(dependencies = { DModel.class })
public class CalculationModel extends BaseModel {

	@Override
	public String path() {
		return "core/calculation";
	}

	@Override
	@BlockerTodo("Create configuration for this")
	public void preInstall() {
		
		ConfigModel.put(ConfigKeys.MAX_CARRY_OVER_UNIT_LOAD, 10);

		Logger.debug("Creating score grades");

		// Create score grades

		List<ScoreGradeSpec> grades = new FluentArrayList<ScoreGradeSpec>()
				.with(new ScoreGradeSpec(ScoreGrade.A.name(), 70, 100))
				.with(new ScoreGradeSpec(ScoreGrade.B.name(), 65, 69))
				.with(new ScoreGradeSpec(ScoreGrade.C.name(), 60, 64))
				.with(new ScoreGradeSpec(ScoreGrade.D.name(), 55, 59))
				.with(new ScoreGradeSpec(ScoreGrade.E.name(), 50, 54))
				.with(new ScoreGradeSpec(ScoreGrade.F.name(), 0, 49));

		CalculationModel.updateScoreGrades(-1l, grades);
	}

	@Override
	public void install(InstallOptions options) {

	}

	private static boolean validateGrades(List<ScoreGradeSpec> grades) {

		boolean b = true;

		// First verify length
		b = ScoreGrade.values().length == grades.size();
		if (!b) {
			throw new ResourceException(ResourceException.FAILED_VALIDATION,
					"A validation error occured: Invalid length");
		}

		// Then, verify that all scores are tentatively between 0 and 100
		b = grades.get(0).getUpperBound() == 100 && grades.get(grades.size() - 1).getLowerBound() == 0;
		if (!b) {
			throw new ResourceException(ResourceException.FAILED_VALIDATION,
					"A validation error occured: All values not within 0 .. 100");
		}

		// Then, verify the edges are numerically successive
		for (int i = 0; i < grades.size() - 1; i++) {
			b = grades.get(i).getLowerBound() == grades.get(i + 1).getUpperBound() + 1;
			if (!b) {
				throw new ResourceException(ResourceException.FAILED_VALIDATION,
						"A validation error occured. Edges are not numerically successive.");
			}
		}

		// Then verify that from n + 1 [bounds] < .... < n [bounds]
		List<Integer> vs = new ArrayList<Integer>(grades.size() * 2);
		for (int i = 0; i < grades.size(); i++) {
			ScoreGradeSpec v = grades.get(i);
			vs.add(v.getUpperBound());
			vs.add(v.getLowerBound());
		}
		int c = 101;
		for (int i : vs) {
			if (c > i) {
				c = i;
			} else {
				throw new ResourceException(ResourceException.FAILED_VALIDATION,
						"A validation error occured while verifying n + 1 [bounds] < .... < n [bounds]");
			}
		}

		return b;
	}

	@ModelMethod(functionality = Functionality.VIEW_SCORE_GRADES)
	public static List<ScoreGradeSpec> getScoreGrades() {

		List<ScoreGradeSpec> result = new ArrayList<>();

		ofy().load().type(ScoreGradeEntity.class).list().forEach(e -> {
			result.add(new ScoreGradeSpec(e.getGrade(), e.getLowerBound(), e.getUpperBound()));
		});

		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_SCORE_GRADES)
	public static void updateScoreGrades(Long principal, List<ScoreGradeSpec> grades) {

		validateGrades(grades);

		List<ScoreGradeEntity> entities = new ArrayList<>();

		grades.forEach(e -> {
			entities.add(new ScoreGradeEntity().setGrade(e.getGrade()).setLowerBound(e.getLowerBound())
					.setUpperBound(e.getUpperBound()));
		});

		ofy().save().entities(entities);
		
		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.UPDATED)
				.setObject(ObjectEntity.get(ObjectType.SCORE_GRADES)
						);

		ActivityStreamModel.newActivity(activity);
	}

	protected static Map<String, Integer> getAcademicSemesterCourseHighScore(Long academicSemesterId,
			Collection<String> courseCodes) {

		Map<String, Integer> result = new FluentHashMap<>();

		courseCodes.forEach(courseCode -> {

			AcademicSemesterCourseEntity e = EntityUtils.lazyQuery(AcademicSemesterCourseEntity.class,
					QueryFilter.get("academicSemesterId", academicSemesterId.toString()),
					QueryFilter.get("courseCode", courseCode)).first().safe();

			result.put(courseCode, e.getHighestScore());
		});
		return result;
	}

	protected static Map<String, AcademicSemesterCourseSpec> getAcademicSemesterCoursesHtghScores(
			Long academicSemesterId) {

		Map<String, AcademicSemesterCourseSpec> result = new HashMap<>();

		EntityUtils.lazyQuery(AcademicSemesterCourseEntity.class,
				QueryFilter.get("academicSemesterId = ", academicSemesterId.toString())).list().forEach(e -> {
					AcademicSemesterCourseSpec spec = EntityHelper.toObjectModel(e)
							.setCourseTitle(DModel.getCourseName(e.getCourseCode()));
					result.put(spec.getCourseCode(), spec);
				});

		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static Boolean isSemesterCourseSheetCreated(Long principal, String courseCode) {

		if (principal != -1 && !DModel.isCourseLecturer(principal, courseCode)) {
			throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
		}

		return ofy().load().type(AcademicSemesterCourseEntity.class)
				.filter("academicSemesterId = ", DModel.currentSemesterId().toString())
				.filter("courseCode = ", courseCode).first().safe().getIsSheetCreated();
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static Long createScoreSheet(Long principal, Long academicSemesterCourseId) {

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		if (e.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_ALREADY_CREATED_FOR_SEMESTER_COURSE);
		}

		String role = BaseUserModel.getRole(principal);
		RoleRealm realm = RoleModel.getRealm(role);

		if (realm.equals(RoleRealm.LECTURER)) {
			if (!DModel.isCourseLecturer(principal, e.getCourseCode())) {
				throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
			}
		}

		// Because a course may be associated with multiple department levels, the entry
		// @ index 0 is used as the default
		Long departmentLevel = DModel.getCourse(e.getCourseCode()).getDepartmentLevels().get(0);
		Long levelSemester = CalculationModel.getLevelSemester(departmentLevel,
				Semester.from(DModel.getAcademicSemesterValue(e.getAcademicSemesterId())));

		// add totals
		List<Long> totals = validateTotals(levelSemester);

		e.setTotals(totals);

		List<Integer> defaultScores = new ArrayList<>();
		e.getTotals().forEach(total -> {
			defaultScores.add(0);
		});

		List<ResultRecordSheetEntity> sheetEntries = new ArrayList<>();

		e.getStudents().forEach(s -> {

			ResultRecordSheetEntity re = new ResultRecordSheetEntity().setAcademicSemesterCourseId(e.getId())
					.setStudentId(s).setScores(defaultScores).setTotal(0);

			sheetEntries.add(re);
		});

		ofy().save().entities(sheetEntries).now();

		e.setIsSheetCreated(true);

		ofy().save().entity(e).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.CREATED)
				.setObject(ObjectEntity.get(ObjectType.COURSE_RESULT_SHEET).addIdentifier(e.getId()))
				.withPreposition(Preposition.FOR,
						ObjectEntity.get(ObjectType.COURSE).setName(e.getCourseCode()).addIdentifier(departmentLevel)
		// .addIdentifier(e.getCourseCode())
		);

		ActivityStreamModel.newActivity(activity);

		return e.getId();
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public static ScoreSheet getScoreSheet(Long principal, Long academicSemesterCourseId,
			@Nullable AcademicSemesterCourseEntity e) {

		if (e == null) {
			e = ofy().load().type(AcademicSemesterCourseEntity.class).id(academicSemesterCourseId).safe();
			if (!e.getIsSheetCreated()) {
				throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_NOT_CREATED_FOR_SEMESTER_COURSE);
			}
		}

		ScoreSheet result = new ScoreSheet();

		result.setAcademicSemesterCourseId(e.getId());

		result.setTotals(getAcademicSemesterCourseTotals(e.getId()));

		ofy().load().type(ResultRecordSheetEntity.class).filter("academicSemesterCourseId = ", e.getId().toString())
				.forEach(se -> {

					ResultRecordSheetSpec entry = EntityHelper.toObjectModel(se);

					Long departmentLevelId = ofy().load().type(StudentEntity.class).id(se.getStudentId()).safe()
							.getDepartmentLevel();

					DepartmentLevelSpec departmentLevel = EntityHelper.toObjectModel(
							ofy().load().type(DepartmentalLevelEntity.class).id(departmentLevelId).safe());

					result.addRecord(
							entry.setStudentName(BaseUserModel.getPersonName(se.getStudentId(), true).toString())
									.setStudentMatricNumber(DModel.getStudentMatricNumber(se.getStudentId()))
									.setDepartmentName(DModel.getDepartmentName(departmentLevel.getDepartment()))
									.setLevel(ClientRBRef.get(departmentLevel.toString()))
									.setLastUpdatedBy(se.getLastUpdatedBy() != null
											? BaseUserModel.getPersonName(se.getLastUpdatedBy(), false).toString()
											: null));
				});

		result.setIsFinal(e.getIsSheetFinal());

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.VIEWED)
				.setObject(ObjectEntity.get(ObjectType.COURSE_RESULT_SHEET).addIdentifier(e.getId()))
				.withPreposition(Preposition.FOR,
						ObjectEntity.get(ObjectType.COURSE).setName(e.getCourseCode()).addIdentifier(ofy().load()
								.type(CourseEntity.class).id(e.getCourseCode()).safe().getDepartmentLevels().get(0))
		// .addIdentifier(e.getCourseCode())
		);

		ActivityStreamModel.newActivity(activity);

		return result;
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public static ScoreSheet getScoreSheet(Long principal, String courseCode, Long academicSemesterId) {

		if (academicSemesterId == null) {
			academicSemesterId = DModel.currentSemesterId();
		}

		String role = BaseUserModel.getRole(principal);
		RoleRealm realm = RoleModel.getRealm(role);

		if (realm.equals(RoleRealm.LECTURER)) {
			if (!DModel.isCourseLecturer(principal, courseCode)) {
				throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
			}
		}

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.filter("academicSemesterId = ", academicSemesterId.toString()).filter("courseCode = ", courseCode)
				.first().safe();

		if (!e.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_NOT_CREATED_FOR_SEMESTER_COURSE);
		}

		return getScoreSheet(principal, e.getId(), e);
	}

	@ModelMethod(functionality = Functionality.VIEW_COURSE_RESULT_SHEET)
	public static Boolean canManageResultSheet(Long principal, Long academicSemesterCourseId) {

		AcademicSemesterCourseEntity entity = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		// Get associated course code
		String courseCode = entity.getCourseCode();

		String role = BaseUserModel.getRole(principal);
		RoleRealm realm = RoleModel.getRealm(role);

		boolean b = RoleModel.isAccessAllowed(role, Functionality.MANAGE_COURSE_RESULT_SHEET);

		if (b && realm.equals(RoleRealm.LECTURER)) {
			b = DModel.isCourseLecturer(principal, courseCode);
		}

		return b;
	}

	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static List<Long> updateScoreSheet(Long principal, Long academicSemesterCourseId,
			Map<Long, List<Integer>> scores) {

		AcademicSemesterCourseEntity entity = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		ObjectWrapper<Integer> highScore = new ObjectWrapper<Integer>().set(entity.getHighestScore());

		Long currentSemesterId = DModel.currentSemesterId();
		if (!entity.getAcademicSemesterId().equals(currentSemesterId)) {
			throw new ResourceException(ResourceException.UPDATE_NOT_ALLOWED);
		}

		// Get associated course code
		String courseCode = entity.getCourseCode();

		String role = BaseUserModel.getRole(principal);
		RoleRealm realm = RoleModel.getRealm(role);

		if (realm.equals(RoleRealm.LECTURER)) {
			if (!DModel.isCourseLecturer(principal, courseCode)) {
				throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
			}
		}

		// Get totals

		if (!entity.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.COURSE_RESULT_SHEET_DOES_NOT_EXIST);
		}

		List<Long> totalIds = entity.getTotals();
		List<Integer> totals = Lists.newArrayList(getAssessmentTotalValues(totalIds).values());

		// This represents the score sheet ids whose scores were successfully updated
		List<Long> result = new FluentArrayList<>();

		List<Object> entities = new ArrayList<>();

		scores.forEach((k, v) -> {

			// validate scores against totals

//			for (int i = 0; i < totals.size(); i++) {
//				if (v.get(i) > totals.get(i)) {
//					throw new SystemValidationException(SystemErrorCodes.STUDENT_SCORE_EXCEEDS_RESULT_SHEET_TOTAL,
//							k.toString());
//				}
//			}

			ResultRecordSheetEntity e = EntityUtils.query(ResultRecordSheetEntity.class,
					QueryFilter.get("academicSemesterCourseId =", academicSemesterCourseId.toString()),
					QueryFilter.get("studentId =", k.toString())).get(0);

			e.setScores(v);
			e.setLastUpdated(new Date());
			e.setLastUpdatedBy(principal);

			int total = 0;
			for (int s : v) {
				total += s;
			}

			e.setTotal(total);

			entities.add(e);

			if (total > highScore.get()) {
				highScore.set(total);
			}

			StudentSemesterCoursesEntity sse = EntityUtils.query(StudentSemesterCoursesEntity.class,
					QueryFilter.get("academicSemesterId =", currentSemesterId.toString()),
					QueryFilter.get("studentId =", k.toString())).get(0);

			sse.putCourse(courseCode, total);

			entities.add(sse);

			result.add(k);

		});

		if (highScore.get() > entity.getHighestScore()) {
			entity.setHighestScore(highScore.get());
		}
		entity.setDateUpdated(new Date());

		entities.add(entity);

		ofy().save().entities(entities).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).addIdentifier(principal))
				.setPredicate(CustomPredicate.UPDATED)
				.setObject(ObjectEntity.get(ObjectType.COURSE_RESULT_SHEET)
						.setIdentifiers(FluentArrayList.asList(entity.getId())))
				.withPreposition(Preposition.FOR,
						ObjectEntity.get(ObjectType.COURSE).setName(courseCode)
								.addIdentifier(ofy().load().type(CourseEntity.class).id(entity.getCourseCode()).safe()
										.getDepartmentLevels().get(0))
		// .setIdentifiers(FluentArrayList.asList(courseCode))
		);

		ActivityStreamModel.newActivity(activity);

		return result;
	}

	@BlockerTodo("Add code for compiling results, and  generating reports")
	@ModelMethod(functionality = Functionality.MANAGE_COURSE_RESULT_SHEET)
	public static void submitScoreSheet(Long principal, Long academicSemesterCourseId) {

		AcademicSemesterCourseEntity entity = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		if (entity.getIsSheetFinal()) {
			throw new SystemValidationException(SystemErrorCodes.COURSE_RESULT_SHEET_ALREADY_SUBMITTED);
		}

		String role = BaseUserModel.getRole(principal);
		RoleRealm realm = RoleModel.getRealm(role);

		if (realm.equals(RoleRealm.LECTURER)) {
			if (!DModel.isCourseLecturer(principal, entity.getCourseCode())) {
				throw new SystemValidationException(SystemErrorCodes.INSUFFIECIENT_PERMISSION_FOR_COURSE_RESULT_SHEET);
			}
		}

		entity.setIsSheetFinal(true);
		entity.setDateUpdated(new Date());

		ofy().save().entity(entity).now();

		// Add to activity stream

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).addIdentifier(principal))
				.setPredicate(CustomPredicate.SUBMITTED)
				.setObject(ObjectEntity.get(ObjectType.COURSE_RESULT_SHEET).addIdentifier(entity.getCourseCode())
						.addIdentifier(entity.getAcademicSemesterId()))
				.withPreposition(Preposition.FOR, ObjectEntity.get(ObjectType.COURSE).setName(entity.getCourseCode())
						.addIdentifier(entity.getCourseCode()));

		ActivityStreamModel.newActivity(activity);

	}

	/**
	 * @Unexposed @BlockerTodo("Implement this ASAP") //@ModelMethod(functionality =
	 *            Functionality.VIEW_SEMESTER_COURSE_RESULT) public static Long
	 *            getSemesterCourseReport(Long principal, Long
	 *            academicSemesterCourseId) {
	 * 
	 *            AcademicSemesterCourseEntity entity =
	 *            ofy().load().type(AcademicSemesterCourseEntity.class)
	 *            .id(academicSemesterCourseId).safe();
	 * 
	 *            String courseCode = entity.getCourseCode(); Long
	 *            academicSemesterId = entity.getAcademicSemesterId();
	 * 
	 *            // Generate PDF document, if not generated. This should have been
	 *            done // immediately the sheet becomes final
	 * 
	 *            // Add to activity stream
	 * 
	 *            Sentence activity = Sentence.newInstance()
	 *            .setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
	 *            .setPredicate(CustomPredicate.DOWNLOADED)
	 *            .setObject(ObjectEntity.get(ObjectType.COURSE_RESULT_SHEET)
	 *            .setIdentifiers(FluentArrayList.asList((Object)
	 *            courseCode).with(academicSemesterId)))
	 *            .withPreposition(Preposition.FOR,
	 *            ObjectEntity.get(ObjectType.COURSE).setName(courseCode).addIdentifier(courseCode));
	 * 
	 *            ActivityStreamModel.newActivity(activity);
	 * 
	 *            return null; }
	 * 
	 * @Unexposed
	 * @BlockerTodo //@ModelMethod(functionality =
	 *              Functionality.VIEW_STUDENT_SEMESTER_RESULT) public static Long
	 *              getStudentSemesterReport(Long principal, Long studentId, Long
	 *              academicSemesterId) {
	 * 
	 *              StudentSemesterCoursesEntity e;
	 * 
	 *              // Generate PDF document, if not generated. This should have
	 *              been done // immediately the semester ends
	 * 
	 *              // To generate it, we first query ResultRecordSheetEntity, for
	 *              each of the // student's registered courses, then update //
	 *              StudentSemesterCoursesEntity.courses Map
	 * 
	 *              // Add to activity stream
	 * 
	 *              Sentence activity = Sentence.newInstance()
	 *              .setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
	 *              .setPredicate(CustomPredicate.DOWNLOADED)
	 *              .setObject(ObjectEntity.get(ObjectType.STUDENT_RESULT)
	 *              .setIdentifiers(FluentArrayList.asList((Object)
	 *              studentId).with(academicSemesterId)))
	 *              .withPreposition(Preposition.FOR,
	 *              SubjectEntity.get(SubjectType.STUDENT).setIdentifiers(FluentArrayList.asList(studentId)));
	 * 
	 *              ActivityStreamModel.newActivity(activity);
	 * 
	 *              return null; }
	 * 
	 **/

	@ModelMethod(functionality = Functionality.LIST_DEPARTMENT_LEVELS)
	public static Long getLevelSemester(Long departmentLevelId, Semester s) {

		LevelSemesterEntity ls = ofy().load().type(LevelSemesterEntity.class)
				.filter("departmentLevel = ", departmentLevelId.toString()).filter("semester = ", s.getValue()).first()
				.safe();

		return ls.getId();
	}

	@ModelMethod(functionality = Functionality.VIEW_ASSESSMENT_TOTALS)
	public static List<AssessmentTotalSpec> getAssessmentTotalsForLevel(Long principal, Long levelSemesterId) {

		List<AssessmentTotalSpec> totals = new FluentArrayList<>();

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemesterId.toString())
				.forEach(e -> {
					totals.add(EntityHelper.toObjectModel(e));
				});

		// Add to activity stream

		DepartmentLevelSpec levelSpec = DModel.getDepartmentLevel(levelSemesterId);

		Sentence activity = Sentence.newInstance()
				.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
				.setPredicate(CustomPredicate.VIEWED)
				.setObject(ObjectEntity.get(ObjectType.ASSESSMENT_TOTAL)
						.setIdentifiers(FluentArrayList.asList(levelSemesterId)))

				.withPreposition(Preposition.FOR,
						ObjectEntity.get(ObjectType.DEPARTMENT)
								.setIdentifiers(FluentArrayList.asList(levelSpec.getDepartment()))
								.setName(DModel.getDepartmentName(levelSpec.getDepartment()))
								.addQualifier(ClientRBRef.get(levelSpec))
								.addQualifier(ClientRBRef.get(DModel.getSemester(levelSemesterId))));

		ActivityStreamModel.newActivity(activity);

		return totals;
	}

	protected static List<AssessmentTotalEntity> getAssessmentTotals(Long levelSemester) {

		List<AssessmentTotalEntity> totals = new FluentArrayList<>();

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemester.toString())
				.forEach(e -> {
					totals.add(e);
				});

		return totals;
	}

	private static Long getAcademicSemesterId(Long academicSemesterCourseId) {

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		return e.getAcademicSemesterId();
	}

	@BlockerTodo("Create algorithm to sort based on their percentiles")
	protected static List<TotalSpec> getAcademicSemesterCourseTotals(Long academicSemesterCourseId) {

		AcademicSemesterCourseEntity e = ofy().load().type(AcademicSemesterCourseEntity.class)
				.id(academicSemesterCourseId).safe();

		if (!e.getIsSheetCreated()) {
			throw new SystemValidationException(SystemErrorCodes.COURSE_RESULT_SHEET_DOES_NOT_EXIST);
		}

		List<Long> totalIds = e.getTotals();

		return getAssessmentTotals(totalIds);
	}

	protected static List<TotalSpec> getAssessmentTotals(List<Long> ids) {

		List<TotalSpec> totals = new ArrayList<>();

		ofy().load().type(AssessmentTotalEntity.class).ids(ids).forEach((k, v) -> {
			totals.add(new TotalSpec().setId(v.getId()).setName(v.getName()).setValue(v.getPercentile()));
		});

		return totals;
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

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemester.toString())
				.forEach(e -> {
					totals.add(e.getId());
				});

		return totals;
	}

	@ModelMethod(functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public static Long newAssessmentTotal(Long principal, AssessmentTotalSpec spec) {

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

		AssessmentTotalEntity e = EntityHelper.fromObjectModel(spec).setIsValidated(false);

		ofy().save().entity(e).now();

		// Add to activity stream

		DepartmentLevelSpec levelSpec = DModel.getDepartmentLevel(spec.getLevelSemester());

		boolean b = false;

		if (b) {

			Sentence activity = Sentence.newInstance()
					.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
					.setPredicate(CustomPredicate.UPDATED)
					.setObject(ObjectEntity.get(ObjectType.ASSESSMENT_TOTAL)
							.setIdentifiers(FluentArrayList.asList(spec.getLevelSemester())))

					.withPreposition(Preposition.FOR,
							ObjectEntity.get(ObjectType.DEPARTMENT)
									.setIdentifiers(FluentArrayList.asList(levelSpec.getDepartment()))
									.setName(DModel.getDepartmentName(levelSpec.getDepartment()))
									.addQualifier(ClientRBRef.get(levelSpec))
									.addQualifier(ClientRBRef.get(DModel.getSemester(e.getLevelSemester()))));

			ActivityStreamModel.newActivity(activity);
		}

		return e.getId();
	}

	private static List<Long> validateTotals(Long levelSemester) {

		List<Long> result = new FluentArrayList<>();

		List<AssessmentTotalEntity> entities = new ArrayList<>();

		IntegerWrapper totalPercentile = new IntegerWrapper();
		ObjectWrapper<Boolean> isExamTotal = new ObjectWrapper<Boolean>().set(false);

		ofy().load().type(AssessmentTotalEntity.class).filter("levelSemester =", levelSemester.toString())
				.forEach(e -> {

					entities.add(e);

					result.add(e.getId());

					totalPercentile.add(e.getPercentile());

					if (e.getType().equals(AssessmentTotalType.EXAM.getValue()) && isExamTotal.get()) {
						throw new SystemValidationException(SystemErrorCodes.MULTIPLE_EXAM_TYPED_ASSESSMENT_TOTAL);
					}

					if (e.getType().equals(AssessmentTotalType.EXAM.getValue())) {
						isExamTotal.set(true);
					}

				});

		// at least one AssessmentTotal must be of type exam
		if (!isExamTotal.get()) {
			throw new SystemValidationException(SystemErrorCodes.NO_EXAM_TYPED_ASSESSMENT_TOTAL);
		}

		// verify that all percentiles are exactly 100

		if (totalPercentile.getValue() != 100) {
			throw new SystemValidationException(
					totalPercentile.getValue() < 100 ? SystemErrorCodes.TOTAL_PERCENTILES_SUM_LESS_THAN_100
							: SystemErrorCodes.TOTAL_PERCENTILES_SUM_MORE_THAN_100);
		}

		// Now that validation has passed, mark these totals as validated to prevent
		// deletion in the future, this semester

		entities.forEach(e -> {
			if (!e.getIsValidated()) {
				ofy().save().entity(e.setIsValidated(true)).now();
			}
		});

		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_ASSESSMENT_TOTALS)
	public static void deleteAssessmentTotal(Long principal, Long id) {
		AssessmentTotalEntity e = ofy().load().type(AssessmentTotalEntity.class).id(id).safe();

		if (e.getIsValidated()) {
			throw new SystemValidationException(SystemErrorCodes.RESULT_SHEET_ALREADY_CREATED_FOR_ASSESSMENT_TOTAL);
		}

		ofy().delete().type(AssessmentTotalEntity.class).id(id).now();

		// Add to activity stream

		DepartmentLevelSpec levelSpec = DModel.getDepartmentLevel(e.getLevelSemester());

		boolean b = false;

		if (b) {

			Sentence activity = Sentence.newInstance()
					.setSubject(SubjectEntity.get(SubjectType.USER).setIdentifiers(FluentArrayList.asList(principal)))
					.setPredicate(CustomPredicate.UPDATED)
					.setObject(ObjectEntity.get(ObjectType.ASSESSMENT_TOTAL)
							.setIdentifiers(FluentArrayList.asList(e.getLevelSemester())))

					.withPreposition(Preposition.FOR,
							ObjectEntity.get(ObjectType.DEPARTMENT)
									.setIdentifiers(FluentArrayList.asList(levelSpec.getDepartment()))
									.setName(DModel.getDepartmentName(levelSpec.getDepartment()))
									.addQualifier(ClientRBRef.get(levelSpec))
									.addQualifier(ClientRBRef.get(DModel.getSemester(e.getLevelSemester()))));

			ActivityStreamModel.newActivity(activity);

		}
	}

}
