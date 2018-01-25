
var RETURN_URL;
var DEPARTMENT_LEVEL_ID;

function initPageComponents() {

    
    $('input[name="dpt_accreditation"]').parent().checkbox();

    var form = $('#department-create-form');

    form
            .form(
                    {
                        fields: {
                            dpt_name: {
                                identifier: 'dpt_name',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please enter the department name'
                                    }]
                            },

                            dpt_faculty: {
                                identifier: 'dpt_faculty',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please specify the faculty'
                                    }]
                            },
                            dpt_duration: {
                                identifier: 'dpt_duration',
                                rules: [{
                                        type: 'integer',
                                        prompt: 'Please enter a valid programme duration'
                                    }]
                            }
                        },

                        inline: true,
                        on: 'blur',
                        keyboardShortcuts: false,
                        onSuccess: function () {

                            var payload = {
                                'name': $('input[name="dpt_name"]').val(),
                                'faculty': $('input[name="dpt_faculty"]').val(),
                                'isAccredited': $("input[name='dpt_accreditation']").prop('checked'),
                                'duration': $('input[name="dpt_duration"]').val()
                            };

                            createDepartment(payload).then(() => {

                                console.log("Department has been created successfully");

                                if (RETURN_URL) {
                                    //window.location = RETURN_URL;
                                } else {
                                    //Do nothing at this point
                                }

                            }, (e) => {
                                alert(e.responseJSON.message);
                                form.find(".button").removeClass("disabled");
                            });

                        },
                        onFailure: function () {

                        }
                    });

    form.find(".button").on('click', function (e) {
        form.form('validate form');
        // e.stopPropagation();
    });
    
    return new Promise(function (resolve, reject) {

        listFacultyNames().then((faculties) => {
            var facultiesMenu = $('input[name="dpt_faculty"] ~ .menu');
            for (var i in faculties) {
                facultiesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + faculties[i] + "</div>");
            }
            //$('input[name="dpt_faculty"]').parent().dropdown();
        });

        resolve();
    });
}

function initPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    initPageComponents().then(() => {
        $(window).trigger('ce-load-page-end');
    });
}