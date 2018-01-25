
var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    var departmentLevelId = getCookieOnce("departmentlevelid");

    init_students_list_table(departmentLevelId).then(() => {
        $(window).trigger('ce-load-page-end');
    });
    
}

function init_students_list_table(departmentLevelId) {

    return getStudents(departmentLevelId).then(data => {

        var table = $('#students-list-table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Id')));
        thParent.append($('<th></th>').html(get_ce_translate_node('matric_number')));
        thParent.append($('<th></th>').html(get_ce_translate_node('student_name')));
        thParent.append($('<th></th>').html(get_ce_translate_node('jamb_reg_no')));
        thParent.append($('<th></th>').html(get_ce_translate_node('current_cgpa')));

        var rows = table.find(' > tbody');

        for (var k in data) {

            var v = data[k];

            var row = $('<tr></tr>').attr('data-student-id', v['id']);

            row.append($('<td></td>').text(v['id']));
            row.append($('<td></td>').text(v['matricNumber']));
            row.append($('<td></td>').html(v['name']));
            row.append($('<td></td>').text(v['jambRegNo']));
            row.append($('<td></td>').text(v['cgpa']));

            rows.append(row);
        }

        //Initialize Table
        var dtable = table.DataTable({
            "pagingType": "full_numbers_icon",
            order: [1, 'desc'],
            responsive: true
        });

        table.on('click', '> tbody > tr', function () { 
            var studentId = $(this).attr('data-student-id');
            if (RETURN_URL) {
                window.location = RETURN_URL + "?id=" + studentId;
            } else {
                //Do nothing at this point
            }
        });

    });

}


 