
var RETURN_URL;
var DEPARTMENT_LEVEL_ID;

function initPageComponents() {

    var form = $('#course-create-form');

    form
            .form(
                    {
                        fields: {
                            course_code: {
                                identifier: 'course_code',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please enter the course code'
                                    }]
                            },

                            course_name: {
                                identifier: 'course_name',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please enter the course name'
                                    }]
                            },
                            course_unit_load: {
                                identifier: 'course_unit_load',
                                rules: [{
                                        type: 'integer',
                                        prompt: 'Please enter the course unit load'
                                    }]
                            },
                            course_semester: {
                                identifier: 'course_semester',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please select a semester'
                                    }]
                            },
                            course_dpt_levels: {
                                identifier: 'course_dpt_levels',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please specify the department level(s)'
                                    }]
                            },
                            course_lecturers: {
                                identifier: 'course_lecturers',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please specify the lecturers for this course.'
                                    }]
                            }
                        },

                        inline: true,
                        on: 'blur',
                        keyboardShortcuts: false,
                        onSuccess: function () {

                            form.find(".button").addClass("disabled");

                            var dptlevels = $('select[name="course_dpt_levels"]').dropdown('get value');
                            var lecturers = $('select[name="course_lecturers"]').dropdown('get value');

                            var payload = {
                                'code': $('input[name="course_code"]').val(),
                                'name': $('input[name="course_name"]').val(),
                                'point': $('input[name="course_unit_load"]').val(),
                                'semester': $('input[name="course_semester"]').val(),
                                'departmentLevels': dptlevels[dptlevels.length - 1],
                                'lecturers': lecturers[lecturers.length - 1]
                            };

                            createCourse(payload).then(() => {
                                console.log("Course has been created successfully");

                                if (RETURN_URL) {
                                    window.location = RETURN_URL + "?departmentLevelId=" + DEPARTMENT_LEVEL_ID;
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

        getAllSemesters().then((semesters) => {
            var semestersMenu = $('input[name="course_semester"] ~ .menu');
            for (var i in semesters) {
                semestersMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + semesters[i] + "</div>");
            }
            //$('input[name="course_semester"]').parent().dropdown();
        });

        getAssociatedDepartmentLevels(DEPARTMENT_LEVEL_ID).then(data => {
            var levelsMenu = $('select[name="course_dpt_levels"]');
            for (var k in data) {
                var v = data[k];
                levelsMenu.append($('<option value="' + k + '">' + v + '</option>'));
            }
            setTimeout(() => {
                levelsMenu.dropdown('set exactly', DEPARTMENT_LEVEL_ID);
            }, 100);
        });

        getLecturers(undefined, DEPARTMENT_LEVEL_ID).then(data => {
            var lecturersMenu = $('select[name="course_lecturers"]');
            for (var k in data) {
                var v = data[k];
                lecturersMenu.append($('<option value="' + v['id'] + '">' + v['name'] + '</option>'));
            }
        });

        resolve();
    });
}

function initPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    DEPARTMENT_LEVEL_ID = getCookieOnce("departmentlevelid");

    initPageComponents().then(() => {
        $(window).trigger('ce-load-page-end');
    });
}