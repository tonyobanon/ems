
var CURRENT_ACADEMIC_SEMESTER_COURSE;

var SCORE_SHEET_UPDATES = {};

var RETURN_URL;
var IS_TABLE_EDITABLE;

function initPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    } 
    
    var academicSemesterCourseid = getCookieOnce("academicsemestercourseid");

    init_score_sheet(academicSemesterCourseid).then(() => {
        $(window).trigger('ce-load-page-end');
    });
}

//The promise thenable is too large, please chunk it of possible
function init_score_sheet(academicSemesterCourseId) {

    var scoreSheet = getScoreSheetWithId(academicSemesterCourseId);

    return scoreSheet.then(function (data) {

        CURRENT_ACADEMIC_SEMESTER_COURSE = data['academicSemesterCourseId'];

        console.log("Initializing course result sheet with for semester course: " + data['academicSemesterCourseId']);

        //Update DOM
        var table = $('#course-result-sheet-table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Id')));
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
                    .append('<td>' + record['studentId'] + '</td>')
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

        //Listen for row clicks if the table is not editable
        table.on('click', '> tbody > tr', function () { 
            if (!IS_TABLE_EDITABLE && RETURN_URL) {
                var studentId = parseInt($(this).attr('data-student-id'));
                window.location = RETURN_URL + "?id=" + studentId;
            }
        });

        //Listen for changes to scores, if the table is editable
        trParent.find('> tr > td').on('change', function (e, newValue) {

            if (!IS_TABLE_EDITABLE) {
                return;
            }

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

            //Verify that user can update 
             canManageResultSheet(CURRENT_ACADEMIC_SEMESTER_COURSE).then((isAllowed) => {

                if (isAllowed) {

                    //Make sheet editable
                    table.editableTableWidget();

                    //Show submit button
                    $('#submit-course-result-sheet').show();

                    IS_TABLE_EDITABLE = true;
                }

                table.DataTable();
            });
        } else {
            table.DataTable();
        }
    });
}


function save_result_sheet_updates() {

    return updateScoreSheet(CURRENT_ACADEMIC_SEMESTER_COURSE, SCORE_SHEET_UPDATES).then((data) => {

        for (var i in data) {
            var k = parseInt(data[i]);

            //remove from SCORE_SHEET_UPDATES
            delete SCORE_SHEET_UPDATES[k];

            var e = $("#course-result-sheet-table > tbody > [data-student-id^='" + k + "']");

            e.removeAttr('style');
            e.attr('class', e.attr('data-prev-class'));

        }

        $('#save-course-result-sheet').hide();

    }).catch((e) => {
        alert(e.responseJSON.message);
    });

}

function submit_result_sheet() {

    if (confirm("Once submitted this result sheet can no longer be modified. Are you sure ?")) {
        submitScoreSheet(CURRENT_ACADEMIC_SEMESTER_COURSE).then(() => {
            window.location = window.location;
        });
    }
    ;
}