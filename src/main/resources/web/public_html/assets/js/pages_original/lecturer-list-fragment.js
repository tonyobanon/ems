
var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    var departmentId = getCookieOnce("departmentid");

    init_lecturers_list_table(departmentId).then(() => {
        $(window).trigger('ce-load-page-end');
    });

}

function init_lecturers_list_table(departmentId) {

    return getLecturers(departmentId).then(data => {

        var table = $('#lecturers-list-table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Id')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Name')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Courses_taught')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Start_date')));

        var rows = table.find(' > tbody');

        for (var k in data) {

            var v = data[k];

            var row = $('<tr></tr>').attr('data-lecturer-id', v['id']);

            row.append($('<td></td>').text(v['id']));
            row.append($('<td></td>').html(v['name']));
            
            //row.append($('<td></td>').html('<b>' + v['courses'].toString().replace(',', ',&nbsp;&nbsp;&nbsp;').replace('[', '').replace('[', '') + '</b>'));
            row.append($('<td></td>').text(v['courses'].length));
            
            var dateStarted = moment(v['startDate']);
            row.append($('<td></td>').text(dateStarted.format('ddd, h:mm A') + " (" + dateStarted.fromNow() + ")"));

            rows.append(row);
        }

        //Initialize Table
        var dtable = table.DataTable({
            "pagingType": "full_numbers_icon",
            order: [1, 'desc'],
            responsive: true
        });

        table.on('click', '> tbody > tr', function () { 
            var lecturerId = $(this).attr('data-lecturer-id');
            if (RETURN_URL) {
                window.location = RETURN_URL + "?id=" + lecturerId;
            } else {
                //Do nothing at this point
            }
        });

    });

}


 