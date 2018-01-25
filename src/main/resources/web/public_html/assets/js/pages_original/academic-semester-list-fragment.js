
var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user
    
    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }
    
    init_academic_semesters().then(() => {
        $(window).trigger('ce-load-page-end');
    });
}

function init_academic_semesters() {

    var parent = $('#academic-semesters');

    return listAcademicSemester().then((data) => {

        for (var k in data) {
            
            var v = data[k];

            var html = get_ce_translate_node(v['semesterString']) + '&nbsp;&nbsp;(' + v['lowerYearBound'] + '&nbsp;/&nbsp;' + v['upperYearBound'] + '&nbsp;&nbsp;' + get_ce_translate_node('Academic_session') + ')';

            var item = $('<div class="item" data-semester-id="' + v['id'] + '"><div class="right floated content"><div class="ui button select-semester">Select</div></div><img class="ui avatar image" src="assets/images/no-image.png"><div class="content">' + html + '</div></div>');
            parent.append(item);
        }

        parent.find('.select-semester').on('click', function () {
            
            var semesterId = $(this).parent().parent().attr('data-semester-id');

            if (RETURN_URL) {
                
                var reqParams;
                
                if(window.location.search === ""){
                    reqParams = "?academicSemesterId=" + semesterId;
                } else {
                    reqParams = window.location.search + "&academicSemesterId=" + semesterId; 
                }
               
                window.location = RETURN_URL + reqParams;
                
            } else {
                //Do nothing at this point
            }

        });

    });
}