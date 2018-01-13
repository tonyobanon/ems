
function load() {

    var currentYear = new Date().getFullYear();

    $('input[name="semester_session_start_year"]').val(currentYear);
    $('input[name="semester_session_end_year"]').val(currentYear + 1);

    $('input[name="semester_start_year"]').val(currentYear);

    $('input[name="semester_value"]').parent().dropdown();

    $('.ui .form').form({
        fields: {

            /** Emailing Credentials  */

            email_provider_url: {
                identifier: 'email_provider_url',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your email provider url'
                    }]
            },
            email_username: {
                identifier: 'email_username',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your email username'
                    }]
            },
            email_pass: {
                identifier: 'email_pass',
                rules: [{
                        type: 'empty',
                        prompt: 'Please enter your email password'
                    },
                    {
                        type: 'length[3]',
                        prompt: 'Your password must be at least 3 characters'

                    },
                    {
                        type: 'match[email_passv]',
                        prompt: 'The two passwords entered must match'
                    }]
            },
            email_passv: {
                identifier: 'email_passv',
                rules: [{
                        type: 'empty',
                        prompt: 'Please verify your email password'
                    }]
            },

            /** Academic Session  */

            semester_value: {
                identifier: 'semester_value',
                rules: [{
                        type: 'empty',
                        prompt: 'Please selct the current semester'
                    }]
            },

            semester_session_start_year: {
                identifier: 'semester_session_start_year',
                rules: [{
                        type: 'integer[' + new Number(currentYear) + ']',
                        prompt: 'Please enter a valid date'
                    }]
            },

            semester_session_end_year: {
                identifier: 'semester_session_end_year',
                rules: [{
                        type: 'integer[' + new Number(currentYear + 1) + ']',
                        prompt: 'Please enter a valid date'
                    }]
            },

            semester_start_day: {
                identifier: 'semester_start_day',
                rules: [{
                        type: 'integer[1..31]',
                        prompt: 'Please enter a valid date'
                    }]
            },

            semester_start_month: {
                identifier: 'semester_start_month',
                rules: [{
                        type: 'integer[1..12]',
                        prompt: 'Please enter a valid month'
                    }]
            },

            semester_start_year: {
                identifier: 'semester_start_year',
                rules: [{
                        type: 'integer[' + new Number(currentYear) + ']',
                        prompt: 'Please enter a valid date'
                    }]
            }



        },

        inline: true,
        on: 'blur',
        keyboardShortcuts: false,
        onSuccess: function () {

            $(".ui .form .button").addClass("disabled");

            // Save to Local Storage, and continue to next step


            /** Emailing Credentials  */

            var mailCredentials = {
                providerUrl: $('input[name="email_provider_url"]').val(),
                username: $('input[name="email_username"]').val(),
                password: $('input[name="email_pass"]').val()
            };

            localStorage.setItem("$SETUP_mailCredentials", JSON.stringify(mailCredentials));


            /** Academic Session  */


            var academicSemester = {
                lowerYearBound: $('input[name="semester_session_start_year"]').val(),
                upperYearBound: $('input[name="semester_session_end_year"]').val(),
                value: $('input[name="semester_value"]').val(),
                startDate:  $('input[name="semester_start_year"]').val() + "-"
                            + $('input[name="semester_start_month"]').val() + "-"
                            + $('input[name="semester_start_day"]').val()
            };

            localStorage.setItem("$SETUP_academicSemester", JSON.stringify(academicSemester));


            window.location = "/setup/four";
        },
        onFailure: function () {

        }
    });

    $(".ui .form .button").on('click', function (e) {
        $('.ui .form').form('validate form');
        // e.stopPropagation();
    });
}