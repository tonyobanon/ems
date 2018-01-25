
var RETURN_URL;

function initPageComponents() {

    var form = $('#faculty-create-form');

    form
            .form(
                    {
                        fields: {
                            faculty_name: {
                                identifier: 'faculty_name',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please enter the faculty name'
                                    }]
                            }
                        },

                        inline: true,
                        on: 'blur',
                        keyboardShortcuts: false,
                        onSuccess: function () {

                            form.find(".button").addClass("disabled");

                            var payload = {
                                'name': $('input[name="faculty_name"]').val(),
                            };

                            createFaculty(payload).then(() => {
                                
                                console.log("Faculty has been created successfully");

                                if (RETURN_URL) {
                                    window.location = RETURN_URL;
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
    
    return Promise.resolve();
}

function initPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    initPageComponents().then(() => {
        $(window).trigger('ce-load-page-end');
    });
}