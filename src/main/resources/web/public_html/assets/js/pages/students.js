
var CURRENT_STUDENT_ID;

function initPage() {

    //We need to determine what to display to the user

    var studentId = getCookie("studentid");
    var departmentLevelId = getCookie("departmentlevelid");
    var departmentId = getCookie("departmentid");

    removeCookieWithPath('studentid', '/students');
    removeCookieWithPath('departmentlevelid', '/students');
    removeCookieWithPath('departmentid', '/students');


    if (studentId) {

        //show student profile

    } else if (departmentLevelId) {

        //Show students list
        init_students_list_table(departmentLevelId);

    } else if (departmentId) {
        
        //Prompt user to select level
         init_level_selection_view(departmentId);

    } else {
        //redirect to departments page
        window.location = "/departments?returnUrl=/students";
    }

}

function init_level_selection_view(departmentId) {

    var parent = $('#department-levels');
    
    listDepartmentLevels(departmentId).then((data) => {

        for (var k in data) {
            var v = data[k];

            var item = $('<div class="item" data-level-id="' + k + '"><div class="right floated content"><div class="ui button select-level">Select</div></div><img class="ui avatar image" src="assets/images/no-image.png"><div class="content">' + get_ce_translate_node(v) + '</div></div>');
            parent.append(item);
        }
        
        parent.find('.select-level').on('click', function(){
           var levelId = $(this).parent().parent().attr('data-level-id');
           window.location = "/students?departmentlevelid=" + levelId;
        });
        
        show_department_levels_container();
    });
}



function init_students_list_table(departmentLevelId) {

    getStudents(departmentLevelId).then(data => {

        var table = $('#students-table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('id')));
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

        show_students_table_container();

        $('#students-table > tbody > tr').on('click', function () {

            var studentId = $(this).attr('data-student-id');
            window.location = "/students?studentId=" + studentId;
        });

    });

}


function show_students_table_container() {
    $('#students-table-container').css('display', 'flex');
}

function show_department_levels_container() {
    $('#department-levels-container').css('display', 'flex');
}


