
var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    init_faculty_list_table().then(() => {
        $(window).trigger('ce-load-page-end');
    });

}

function init_faculty_list_table() {

    return listFaculties().then((results) => {

        canUserManageFaculties().then(b => {
            if (b) {
                $('#create-faculty-btn-container a').attr('href', "?create");
                $('#create-faculty-btn-container').show();                        
            }
        });

        var table = $('#faculties-table');

        if (Object.keys(results).length === 0) {
            table.parent().html('<h2 class="ui header" style="color: rgba(0,0,0,.4);"> No faculties are currently available. </h2>');
        }

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Name')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Dean')));

        var rows = table.find(' > tbody');

        for (var k in results) {

            var v = results[k];

            var row = $('<tr></tr>').attr('data-faculty-id', v['id']);

            row.append($('<td></td>').text(v['name']));
            row.append($('<td></td>').html(v['deanName'] ? v['deanName'] : ''));

            rows.append(row);

        }

        //Initialize Table
        var dtable = table.DataTable({
            "pagingType": "full_numbers_icon",
            order: [1, 'desc'],
            responsive: true
        });

        table.on('click', '> tbody > tr', function () {

            var facultyId = $(this).attr('data-faculty-id');

            if (RETURN_URL) {
                window.location = RETURN_URL + "?facultyId=" + facultyId;
            } else {
                //Display Faculty page                    
            }
        });


    });
}



