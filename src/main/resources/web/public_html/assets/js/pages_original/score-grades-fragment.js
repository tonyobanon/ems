
var SCORE_GRADES = {};

function initPage() {

    init_score_grades().then(() => {
        $(window).trigger('ce-load-page-end');
    });
}

//The promise thenable is too large, please chunk it of possible
function init_score_grades() {

    return getScoreGrades().then(function (data) {

        //Update DOM
        var table = $('#score-grades-table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Grade')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Lower_bound')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Upper_bound')));

        var trParent = table.find('> tbody');

        for (var i in data) {

            var grade = data[i];

            var tr = $('<tr></tr>')
                    .attr('data-grade', grade['grade'])
                    .append('<td>' + grade['grade'] + '</td>')
                    .append('<td data-grade-bound="true">' + grade['lowerBound'] + '</td>')
                    .append('<td data-grade-bound="true">' + grade['upperBound'] + '</td>');

            trParent.append(tr);

            SCORE_GRADES[grade['grade']] = [grade['lowerBound'], grade['upperBound']];
        }


        //Listen for changes to scores, if the table is editable
        trParent.find('> tr > td').on('change', function (e, newValue) {

            newValue = parseInt(newValue);

            var elem = $(this);

            if (isNaN(newValue)) {
                //Not a number
                return false;
            }

            var parent = elem.parent();

            var grade = parent.attr('data-grade');

            var gradeBounds = [];

            parent.find("td[data-grade-bound]").each(function (i, el) {
                var bound = parseInt($(el).text());
                gradeBounds.push(bound);
            });

            //Save updates
            SCORE_GRADES[grade] = gradeBounds;

            // Mark this row as updated
            parent.attr('data-prev-class', parent.attr('class'));
            parent.removeAttr('class');
            parent.attr('style', 'background-color: aliceblue;');

            $('#save-score-grades').show();

            return true;
        });

        //Verify that user can update 
         canUserManageScoreGrades().then((isAllowed) => {

            if (isAllowed) {
                //Make sheet editable
                table.editableTableWidget();
            }

            table.DataTable();
        });

    });
}


function save_score_grades_updates() {

    return updateScoreGrades(SCORE_GRADES).then((data) => {

        for (var i in data) {
            var k = data[i];

            var e = $("#score-grades-table > tbody > [data-grade^='" + k + "']");

            e.removeAttr('style');
            e.attr('class', e.attr('data-prev-class'));

        }

        $('#save-score-grades').hide();

    }).catch((e) => {
       alert(e.responseJSON.message);
    });

}