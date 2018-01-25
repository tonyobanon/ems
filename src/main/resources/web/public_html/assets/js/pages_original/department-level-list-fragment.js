
var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user
    
    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }
    
    var departmentId = getCookieOnce("departmentid");

    init_level_selection_view(departmentId).then(() => {
        $(window).trigger('ce-load-page-end');
    });
}

function init_level_selection_view(departmentId) {

    var parent = $('#department-levels');

    return listDepartmentLevels(departmentId).then((data) => {

        for (var k in data) {
            var v = data[k];

            var item = $('<div class="item" data-level-id="' + k + '"><div class="right floated content"><div class="ui button select-level">Select</div></div><img class="ui avatar image" src="assets/images/no-image.png"><div class="content">' + get_ce_translate_node(v) + '</div></div>');
            parent.append(item);
        }

        parent.find('.select-level').on('click', function () {
            var levelId = $(this).parent().parent().attr('data-level-id');

            if (RETURN_URL) {
                window.location = RETURN_URL + "?departmentLevelId=" + levelId;
            } else {
                //Do nothing at this point
            }

        });

    });
}