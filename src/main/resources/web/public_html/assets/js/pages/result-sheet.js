
var COURSE_CODE;
var ACADEMIC_SEMESTER;

var CURRENT_ACADEMIC_SEMESTER_COURSE;

var SCORE_SHEET_UPDATES = {};


function initPage() {

    //We need to determine what to display to the user

    var academicSemesterCourseid = getCookie("academicsemestercourseid");
    var courseCode = getCookie("coursecode");
    var academicSemesterId = getCookie("academicsemesterid");

    removeCookieWithPath('academicsemestercourseid', '/result-sheet');
    removeCookieWithPath('coursecode', '/result-sheet');
    removeCookieWithPath('academicsemesterid', '/result-sheet');

    if (!courseCode && !academicSemesterCourseid) {

        //Display available courses for the user to select from
        init_available_courses_table();

    } else {

        //Display the result sheet for the course
        init_score_sheet(academicSemesterCourseid, JSON.parse(courseCode), academicSemesterId);
    }
}

function init_score_sheet(academicSemesterCourseId, courseCode, academicSemesterId) {

    if (!academicSemesterId) {
        academicSemesterId = undefined;
    }

    var scoreSheet = academicSemesterCourseId ? getScoreSheetWithId(academicSemesterCourseId) : getScoreSheet(courseCode, academicSemesterId);

    scoreSheet.then(function (data) {

        CURRENT_ACADEMIC_SEMESTER_COURSE = data['academicSemesterCourseId'];

        console.log("Initializing course result sheet with for semester course: " + CURRENT_ACADEMIC_SEMESTER_COURSE);

        //Update DOM
        var table = $('#course-result-sheet table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('student_name')));
        thParent.append($('<th></th>').html(get_ce_translate_node('matric_number')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Department')));
        thParent.append($('<th></th>').html(get_ce_translate_node('level')));

        var totals = data['totals'];

        for (var i in totals) {

            var total = totals[i];

            var th = $('<th></th>').attr('id', total['id']).html(total['name'] + "  (" + total['value'] + " <span class='ce-translate'>marks</span>)");
            thParent.append(th);
        }

        thParent.append($('<th></th>').html(get_ce_translate_node('total')));
        thParent.append($('<th></th>').html(get_ce_translate_node('last_updated')));

        var trParent = table.find('> tbody');

        var records = data['resultRecord'];

        for (var i in records) {

            var record = records[i];

            var tr = $('<tr></tr>')
                    .attr('data-student-id', record['studentId'])
                    .append('<td>' + record['studentName'] + '</td>')
                    .append('<td>' + record['studentMatricNumber'] + '</td>')
                    .append('<td>' + record['departmentName'] + '</td>')
                    .append($('<td></td>').html(get_ce_translate_node(record['level']['value'])));

            var scores = record['scores'];

            for (var i in scores) {
                tr.append($('<td>' + scores[i] + '</td>').attr('data-total-index', i));
            }

            tr.append($('<td></td>').addClass('total').text(record['total']));

            if (record['lastUpdated']) {
                tr.append($('<td></td>').html(moment(record['lastUpdated']).fromNow() + ' by ' + record['lastUpdatedBy']));
            } else {
                tr.append('<td></td>');
            }

            trParent.append(tr);
        }

        //Listen for changes to scores
        trParent.find('> tr > td').on('change', function (e, newValue) {

            newValue = parseInt(newValue);
            var elem = $(this);

            if (!elem.attr('data-total-index')) {
                //Not in the student score column
                return false;
            }

            if (isNaN(newValue)) {
                //Not a number
                return false;
            }

            var maxScore = totals[parseInt(elem.attr('data-total-index'))]['value'];

            if (newValue < 0 || newValue > maxScore) {
                //Value is out of range
                return false;
            }

            var parent = elem.parent();

            var studentId = parseInt(parent.attr('data-student-id'));
            var studentScores = [];

            var totalScore = 0;

            parent.find("td[data-total-index]").each(function (i, el) {
                var score = parseInt($(el).text());
                studentScores.push(score);

                totalScore += score;
            });

            //Update total score column
            parent.find("td.total").text(totalScore);

            //Save updates
            SCORE_SHEET_UPDATES[studentId] = studentScores;

            // Mark this row as updated
            parent.attr('data-prev-class', parent.attr('class'));
            parent.removeAttr('class');
            parent.attr('style', 'background-color: aliceblue;');

            $('#save-course-result-sheet').show();

            return true;
        });


        if (data['isFinal'] === false) {
            table.editableTableWidget();
        }

        table.DataTable();

        show_result_sheet_container();
    });
}


function save_result_sheet_updates() {

    updateScoreSheet(CURRENT_ACADEMIC_SEMESTER_COURSE, SCORE_SHEET_UPDATES).then((data) => {

        for (var i in data) {
            var k = parseInt(data[i]);

            //remove from SCORE_SHEET_UPDATES
            delete SCORE_SHEET_UPDATES[k];

            var e = $("#course-result-sheet table > tbody > [data-student-id^='" + k + "']");
            
            e.removeAttr('style');
            e.attr('class', e.attr('data-prev-class'));;
        }

        $('#save-course-result-sheet').hide();

    });

}



function init_available_courses_table() {

    getAvailableSemesterCourses().then(data => {

        var rows = $('#available-courses table > tbody');

        for (var k in data) {

            var v = data[k];

            var row = $('<tr></tr>').attr('id', v['id']);

            row.append($('<td></td>').text(v['courseCode']));
            row.append($('<td></td>').text(v['courseTitle']));
            row.append($('<td></td>').text(v['studentCount']));

            var dateUpdated = moment(v['dateUpdated']);
            row.append($('<td></td>').text(dateUpdated.format('ddd, h:mm A') + " (" + dateUpdated.fromNow() + ")"));

            row
                    .attr('course-code', k)
                    .attr('data-is-sheet-created', v['isSheetCreated'])
                    .attr('data-is-sheet-final', v['isSheetFinal']);

            rows.append(row);
        }

        //Initialize Table
        var dtable = $('#available-courses table').DataTable({
            "pagingType": "full_numbers_icon",
            order: [1, 'desc'],
            responsive: true
        });

        show_available_courses_container();

        $('#available-courses table > tbody > tr').on('click', function () {

            var isSheetCreated = $(this).attr('data-is-sheet-created') === 'true';
            var isSheetFinal = $(this).attr('data-is-sheet-final') === 'true';

            if (!isSheetCreated) {
                create_result_sheet($(this).attr('id'), $(this).attr('course-code'));
            } else if (!isSheetFinal) {
                update_result_sheet($(this).attr('id'));
            }

        });


        // initialize table context Menu
        $
                .contextMenu({
                    selector: '#available-courses table > tbody > tr',
                    items: {

                        create_result_sheet: {
                            name: "Create Result sheet",
                            icon: 'create',
                            callback: function (key, opt) {
                                create_result_sheet($(this).attr('id'));
                            },
                            visible: function () {
                                return $(this).attr('data-is-sheet-created') === 'false';
                            }
                        },
                        update_result_sheet: {
                            name: "Update Result sheet",
                            icon: 'update',
                            callback: function (key, opt) {
                                update_result_sheet($(this).attr('id'));
                            },
                            visible: function () {
                                return $(this).attr('data-is-sheet-created') === 'true'
                                        && $(this).attr('data-is-sheet-final') === 'false';
                            }
                        },
                        submit_result_sheet: {
                            name: "Submit Result sheet",
                            icon: 'submit',
                            callback: function (key, opt) {
                                submit_result_sheet($(this).attr('id'));
                            },
                            visible: function () {
                                return $(this).attr('data-is-sheet-created') === 'true'
                                        && $(this).attr('data-is-sheet-final') === 'false';
                            }
                        },
                        download_result_sheet: {
                            name: "Download Result sheet",
                            icon: 'download',
                            callback: function (key, opt) {
                                download_result_sheet($(this).attr('id'));
                            },
                            visible: function () {
                                return $(this).attr('data-is-sheet-final') === 'true';
                            }
                        }
                    },

                    events: {
                        show: function (opt) {

                            //Todo:
                            //$(this).css('background', 'rgba(0,0,0,.05)!important');
                        },
                        hide: function (opt) {
                        }
                    }

                });

    });

}

function create_result_sheet(academicSemesterCourseId) {

    createScoreSheet(academicSemesterCourseId).then(function (data) {
        window.location = window.location + "?academicsemestercourseid=" + academicSemesterCourseId;
    });
}

function update_result_sheet(academicSemesterCourseId) {
    window.location = window.location + "?academicsemestercourseid=" + academicSemesterCourseId;
}

function submit_result_sheet(academicSemesterCourseId) {
    submitScoreSheet(academicSemesterCourseId).then(() => {
        window.location = window.location;
    });
}

function download_result_sheet(courseCode) {
    console.log(courseCode);
}

function show_available_courses_container() {
    $('#available-courses').css('display', 'flex');

    $('#course-result-sheet').css('display', 'none');
    $('#course-result-sheet').empty();
}

function show_result_sheet_container() {
    $('#course-result-sheet').css('display', 'flex');

    $('#available-courses').css('display', 'none');
    $('#available-courses').empty();
}

