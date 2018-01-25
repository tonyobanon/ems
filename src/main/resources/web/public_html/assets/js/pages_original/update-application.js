
var APPLICATION_ID;

//@TODO
//Find an efficient to validate email and phone on the client

function initComponents() {

    getCountries().then(countries => {

        var countriesMenu = $('input[name="user_country"] ~ .menu');
        for (var i in countries) {
            countriesMenu.append("<div class=\"item\" data-value=\"" + i + "\"><i class=\"" + i.toLowerCase() + " flag\"></i>" + countries[i] + "</div>");
        }

        $('input[name="user_country"]').parent().dropdown({
            onChange: (value, text, choice) => {

                getTerritories(value).then(territories => {

                    var territoriesMenu = $('input[name="user_territory"] ~ .menu').html("");

                    for (var i in territories) {
                        territoriesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + territories[i] + "</div>");
                    }

                    $('input[name="user_territory"]').parent().removeClass("disabled").dropdown({
                        onChange: (value, text, choice) => {

                            getCities(value).then(cities => {

                                var citiesMenu = $('input[name="user_city"] ~ .menu').html("");

                                for (var i in cities) {
                                    citiesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + cities[i] + "</div>");
                                }

                                $('input[name="user_city"]').parent().removeClass("disabled").dropdown();

                            });

                        }
                    });

                });
            }
        });

    });


    listFacultyNames().then(faculties => {

        var facultiesMenu = $('input[name="user_faculty"] ~ .menu');
        for (var i in faculties) {
            facultiesMenu.append("<div class=\"item\" data-value=\"" + i + "\"><i class=\"" + i.toLowerCase() + " flag\"></i>" + faculties[i] + "</div>");
        }

        $('input[name="user_faculty"]').parent().dropdown({
            onChange: (value, text, choice) => {

                listDepartmentNames(value).then(departments => {

                    var departmentMenu = $('input[name="user_department"] ~ .menu').html("");

                    for (var i in departments) {
                        departmentMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + departments[i] + "</div>");
                    }

                    $('input[name="user_department"]').parent().removeClass("disabled").dropdown({
                        onChange: (value, text, choice) => {

                            listDepartmentLevels(value).then(levels => {

                                var levelMenu = $('input[name="user_level"] ~ .menu').html("");

                                for (var i in levels) {
                                    levelMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + levels[i] + "</div>");
                                }

                                $('input[name="user_level"]').parent().removeClass("disabled").dropdown();
                            });
                        }
                    });

                });
            }
        });

    });


    $('input[name="user_gender"]').parent().dropdown();





    var currentYear = new Date().getFullYear();

    var formConfig = {

        fields: {

            user_fname: {
                identifier: 'user_fname',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your first name'
                    }]
            },
            user_mname: {
                identifier: 'user_mname',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your middle name'
                    }]
            },
            user_lname: {
                identifier: 'user_lname',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter the last name'
                    }]
            },
            user_email: {
                identifier: 'user_email',
                rules: [{
                        type: 'email',
                        // type:
                        // 'regExp[/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/]'
                        prompt: 'Please enter a valid email address'
                    }]
            },

            user_dob_day: {
                identifier: 'user_dob_day',
                rules: [{
                        type: 'integer[1..31]',
                        prompt: 'Please enter a valid date'
                    }]
            },

            user_dob_month: {
                identifier: 'user_dob_month',
                rules: [{
                        type: 'integer[1..12]',
                        prompt: 'Please enter a valid month'
                    }]
            },

            user_dob_year: {
                identifier: 'user_dob_year',
                rules: [{
                        type: 'integer[1930..' + new Number(currentYear - 15) + ']',
                        prompt: 'You must be at least 15 years old'
                    }]
            },

            user_gender: {
                identifier: 'user_gender',
                rules: [{
                        type: 'empty',
                        prompt: 'Please selct your gender'
                    }]
            },

            user_address: {
                identifier: 'user_address',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your address'
                    }]
            },

            user_phone: {
                identifier: 'user_phone',
                rules: [{
                        type: 'integer',
                        prompt: 'Please enter a valid phone number'
                    }]
            },

            user_country: {
                identifier: 'user_country',
                rules: [{
                        type: 'empty',
                        prompt: 'Please selct a country'
                    }]
            },

            user_territory: {
                identifier: 'user_territory',
                rules: [{
                        type: 'empty',
                        prompt: 'Please select a territory'
                    }]
            },

            user_city: {
                identifier: 'user_city',
                rules: [{
                        type: 'empty',
                        prompt: 'Please select a city'
                    }]
            },

            user_faculty: {
                identifier: 'user_faculty',
                rules: [{
                        type: 'empty',
                        prompt: 'Please selct your faculty'
                    }]
            },

            user_department: {
                identifier: 'user_department',
                rules: [{
                        type: 'empty',
                        prompt: 'Please select your department'
                    }]
            },

            user_level: {
                identifier: 'user_level',
                rules: [{
                        type: 'empty',
                        prompt: 'Please select your current level'
                    }]
            },

            user_matric_number: {
                identifier: 'user_matric_number',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your matric number'
                    }]
            },

            user_jamb_reg_no: {
                identifier: 'user_jamb_reg_no',
                rules: [{
                        type: 'empty',
                        prompt: 'Please select your Jamb Reg No.'
                    }]
            }



        },

        inline: true,
        on: 'blur',
        keyboardShortcuts: false,
        onSuccess: function () {

            var dobValue = $('input[name="user_dob_year"]').val() + "-"
                    + $('input[name="user_dob_month"]').val() + "-"
                    + $('input[name="user_dob_day"]').val();
            $('input[name="user_dob"]').val(dobValue);


            var payload = {};

            $('.ui.form input').each(function (a, b) {
                var c = $(b);
                if (c.attr('id') !== undefined) {
                    payload[c.attr('id')] = c.val();
                }
            });


            updateApplication(APPLICATION_ID, payload).then(() => {
                if (confirm("Are you sure you want to submit your application.")) {
                    submitApplication(APPLICATION_ID).then(() => {
                        $('.ui.basic.modal').modal('show');
                    }, (e) => {
                        alert("Error: " + e.responseJSON.message);
                    });
                }
            });

        },
        onFailure: function () {

        }
    };



    $('.ui .form').form(formConfig);

    $(".ui .form .button").on('click', function (e) {
        $('.ui .form').form('validate form');
        // e.stopPropagation();
    });
}


function load() {

    APPLICATION_ID = getCookieOnce('id');

    getApplicationRole(APPLICATION_ID).then(role => {
        getRoleRealm(role['role']).then(realm => {
            getApplicationFormFieldIds(realm).then((formFieldIds) => {

                var FieldMappings = {};

                for (var k in formFieldIds) {

                    var v = formFieldIds[k];

                    var inputName;

                    switch (k) {

                        case "ADDRESS":
                            inputName = "user_address";
                            break;
                        case "CITY":
                            inputName = "user_city";
                            break;
                        case "COUNTRY":
                            inputName = "user_country";
                            break;
                        case "DATE_OF_BIRTH":
                            inputName = "user_dob";
                            break;
                        case "DEPARTMENT":
                            inputName = "user_department";
                            break;
                        case "EMAIL":
                            inputName = "user_email";
                            break;
                        case "FACULTY":
                            inputName = "user_faculty";
                            break;
                        case "FIRST_NAME":
                            inputName = "user_fname";
                            break;
                        case "GENDER":
                            inputName = "user_gender";
                            break;
                        case "JAMB_REG_NO":
                            inputName = "user_jamb_reg_no";
                            break;
                        case "LAST_NAME":
                            inputName = "user_lname";
                            break;
                        case "LEVEL":
                            inputName = "user_level";
                            break;
                        case "MATRIC_NUMBER":
                            inputName = "user_matric_number";
                            break;
                        case "MIDDLE_NAME":
                            inputName = "user_mname";
                            break;
                        case "PHONE_NUMBER":
                            inputName = "user_phone";
                            break;
                        case "STATE":
                            inputName = ("user_territory");
                            break;
                    }

                    FieldMappings[inputName] = v;
                }

                // Add element ids
                for (k in FieldMappings) {
                    var v = FieldMappings[k];
                    $('input[name="' + k + '"]').attr('id', v);
                }


                //remove unused elements

                switch (realm) {

                    case 1:
                        //ADMIN
                        $('#institution_profile').remove();
                        break;

                    case 2:
                        //EXAM_OFFICER
                        $('#institution_profile').remove();
                        break;

                    case 3:
                        //HEAD_OF_DEPARTMENT
                        $('input[name="user_jamb_reg_no"]').parent().remove();
                        $('input[name="user_matric_number"]').parent().remove();
                        $('input[name="user_level"]').parent().parent().remove();
                        break;

                    case 4:
                        //LECTURER
                        $('input[name="user_jamb_reg_no"]').parent().remove();
                        $('input[name="user_matric_number"]').parent().remove();
                        $('input[name="user_level"]').parent().parent().remove();
                        break;

                    case 5:
                        //DEAN
                        $('input[name="user_jamb_reg_no"]').parent().remove();
                        $('input[name="user_matric_number"]').parent().remove();
                        $('input[name="user_level"]').parent().parent().remove();
                        $('input[name="user_department"]').parent().parent().remove();
                        break;

                    case 6:
                        //STUDENT
                        break;
                }

                initComponents();

                $("#form-parent").show();

            });
        });
    });

}