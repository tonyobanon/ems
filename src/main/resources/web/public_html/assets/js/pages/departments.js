
var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user

    var returnUrl = getCookie("returnurl");

    removeCookieWithPath('returnurl', '/departments');

    if (returnUrl) {
        RETURN_URL = returnUrl;
    }

    init_departments_list_table();
}


function init_departments_list_table() {

    newListContext(6, {}, -1).then((data) => {

        var contextKey = data['contextKey'];
        nextListingResults(2, contextKey).then((results) => {

            var table = $('#departments-table');

            var thParent = table.find('> thead > tr');

            thParent.append($('<th></th>').html(get_ce_translate_node('name')));
            thParent.append($('<th></th>').html(get_ce_translate_node('faculty')));
            thParent.append($('<th></th>').html(get_ce_translate_node('duration')));
            thParent.append($('<th></th>').html(get_ce_translate_node('accreditation')));

            var rows = table.find(' > tbody');

            for (var k in results) {

                var v = results[k];

                var row = $('<tr></tr>').attr('data-department-id', v['id']).attr('data-faculty-id', v['faculty']);

                row.append($('<td></td>').text(v['name']));
                row.append($('<td></td>').text(v['facultyName']));
                row.append($('<td></td>').html(v['duration'] + '&#32;' + get_ce_translate_node('years')));
                row.append($('<td></td>').html(v['isAccredited'] ? '<i class="checkmark icon green large"></i>' : ''));

                rows.append(row);
                
            }

            //Initialize Table
            var dtable = table.DataTable({
                "pagingType": "full_numbers_icon",
                order: [1, 'desc'],
                responsive: true
            });

            show_departments_table_container();

            $('#departments-table > tbody > tr').on('click', function () {

                var departmentId = $(this).attr('data-department-id');

                if(RETURN_URL){
                    window.location = RETURN_URL + "?departmentId=" + departmentId;
                } else {
                    
                    //Display department page
                    
                }

            });




        });
    });
}
function show_departments_table_container() {
    $('#departments-table-container').css('display', 'flex');
}


