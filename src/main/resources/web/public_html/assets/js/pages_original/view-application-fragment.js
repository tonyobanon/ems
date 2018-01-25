
var APPLICATION_ID;
var APPLICATION_FIELD_VALUES;

var RETURN_URL;

function initPage() {

    //We need to determine what to display to the user

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    APPLICATION_ID = getCookieOnce("id");
    $(window).trigger('ce-load-page-end');
    init_application_view(APPLICATION_ID).then(() => {
//        $(window).trigger('ce-load-page-end');
    });

}

function init_application_view(id) {

    return getApplicationRole(APPLICATION_ID).then(role => {

        return getRoleRealm(role['role']).then(realm => {

            //get field valiues
            return getConsolidatedApplicationFieldValues(APPLICATION_ID).then(fieldValues => {

                APPLICATION_FIELD_VALUES = fieldValues;

                canUserReviewApplications().then((b) => {
                    if (b) {
                        $('#application-actions').show();
                    }
                });

                var fieldPromises = [];

                //List available sections
                return listApplicationFormSections(realm).then(sections => {

                    var sectionsContainer = $('#application_sections');

                    sectionsContainer.append($('<div class="section"><h3 class="ui header"> ROLE: &nbsp; ' + role['role'] + ' </h3></div>'));

                    for (var i in sections) {

                        var v = sections[i];

                        var sectionId = v['id'];

                        var sectionNode = $('<div id="' + sectionId + '" class="section"><h3 class="ui header"> ' + v['title'] + ' </h3></div>');

                        sectionsContainer.append(sectionNode);

                        fieldPromises.push(addFieldValues(sectionId));
                    }

                    return Promise.all(fieldPromises);

                });

            });
        });
    });


}

//        if (RETURN_URL) {
//                window.location = RETURN_URL;
//            } else {
//                //Do nothing at this point
//            }


//@TODO Consolidate Applicant Avatar, use no image as default. Ideally the first field of type image in the section
// should be used as the avatar
function addFieldValues(sectionId) {

    return listApplicationFormFieldNames(sectionId).then(fieldNames => {

        if (Object.keys(fieldNames).length === 0) {
            $('#' + sectionId).remove();
        }

        for (var id in fieldNames) {

            var name = fieldNames[id];

            if (!APPLICATION_FIELD_VALUES[id]) {
                continue;
            }

            var html = '<div class="field"><span class="name">' + name + ' :&nbsp;&nbsp;</span><span class="value">' + APPLICATION_FIELD_VALUES[id] + '</span></div>';

            $('#' + sectionId).append(html);
        }

    });

}

function acceptAcceptionEvent() {
    if (confirm("Are you sure you want to accept this application. This cannot be undone")) {
        acceptApplication(APPLICATION_ID).then(() => {
            if (RETURN_URL) {
                window.location = RETURN_URL;
            } else {
                //Do nothing at this point
            }
        }, (e) => {
            alert(e.responseJSON.message);
        });
    }
}

function declineApplicationEvent() {

}

function doDeclineApplication() {

}