
var RETURN_URL;

function initPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    init_course_list().then(() => {
        $(window).trigger('ce-load-page-end');
    });

}

function init_course_list() {

    var departmentLevelId = getCookieOnce("departmentlevelid");
    var academicSemesterId = getCookieOnce("academicsemesterid");

    var promise;

    if (academicSemesterId && departmentLevelId) {

        //View Result Sheet
        promise = getSemesterCourses(departmentLevelId, academicSemesterId);

    } else if (departmentLevelId) {

        //Directory
        promise = getLevelCourses(departmentLevelId);

    } else {

        //Manage Result Sheet
        promise = getAvailableCourses();
    }

    return promise.then(data => {

        canUserManageCourses().then(b => {
            if (b) {
                $('#create-course-btn-container').show();
                $('#create-course-btn-container a').attr('href', "?create&departmentLevelId=" + departmentLevelId);
            }
        });

        var table = $('#courses-list-table');

        var count = 0;

        // Check result size
        for (var semester in data) {
            for (var i in data[semester]) {
                count++;
            }
        }

        if (count === 0) {
            table.parent().html('<h2 class="ui header" style="color: rgba(0,0,0,.4);"> No courses are currently available. </h2>');
        }

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Course_code')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Course_name')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Semester')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Unit_load')));

        var rows = table.find(' > tbody');

        for (var semester in data) {

            var obj = data[semester];

            for (var i in obj) {

                var v = obj[i];

                var row = $('<tr></tr>');

                if (v['semesterCourseId']) {
                    row.attr('data-semester-course-id', v['semesterCourseId']);
                    row.attr('data-semester-course-sheet-created', v['isSheetCreated']);
                    row.attr('data-semester-course-sheet-final', v['isSheetFinal']);
                } else {
                    row.attr('data-course-code', v['code']);
                }

                row.append($('<td></td>').text(v['code']));
                row.append($('<td></td>').html(v['name']));
                row.append($('<td></td>').html(semester));
                row.append($('<td></td>').html(v['point']));

                rows.append(row);
            }
        }

        //Initialize Table
        var dtable = table.DataTable({
            "pagingType": "full_numbers_icon",
            order: [1, 'desc'],
            responsive: true
        });

        table.on('click', '> tbody > tr', function () {

            if (!RETURN_URL) {
                return;
            }

            var semesterCourseId = $(this).attr('data-semester-course-id');
            var courseCode = $(this).attr('data-course-code');

            var reqParams = semesterCourseId ?
                    ("?academicSemesterCourseId=" + semesterCourseId) :
                    ("?courseCode=" + courseCode);


            if (courseCode) {

                window.location = RETURN_URL + reqParams;

            } else if (semesterCourseId) {

                // Check if the score has been created for this course
                var isSheetCreated = $(this).attr('data-semester-course-sheet-created') === "true";
                var isSheetFinal = $(this).attr('data-semester-course-sheet-final') === "true";

                if (isSheetCreated) {
                    window.location = RETURN_URL + reqParams;
                    return;
                }

                //Verify that user can manage result sheets 
                canManageResultSheet(semesterCourseId).then((isAllowed) => {

                    if (isAllowed) {

                        //Attempt to create result sheet. Note this may fail still, so catch
                        createScoreSheet(semesterCourseId).then(function (data) {

                            window.location = RETURN_URL + reqParams;
                            return;

                        }).catch(() => {
                            //This lecturer probably does not own this course
                        });
                    } else {
                        alert("The result sheet has not yet been created for this course");
                    }
                });

            }

        });

    });

}


 