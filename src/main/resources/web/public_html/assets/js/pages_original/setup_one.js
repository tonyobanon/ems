
function load() {

    $('.ui.basic.modal').modal('show');

    getCountries().then(countries => {

        var countriesMenu = $('input[name="company_country"] ~ .menu');

        for (var i in countries) {
            countriesMenu.append("<div class=\"item\" data-value=\"" + i + "\"><i class=\"" + i.toLowerCase() + " flag\"></i>" + countries[i] + "</div>");
        }

        $('input[name="company_country"]').parent().dropdown({
            onChange: (value, text, choice) => {

                getSpokenLanguages(value).then(languages => {

                    var languagesMenu = $('input[name="company_language"] ~ .menu').html("");

                    for (var i in languages) {
                        languagesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + languages[i] + "</div>");
                    }

                    $('input[name="company_language"]').parent().removeClass("disabled").dropdown();

                });
                
                // Set default currency
                
                getCurrencyCode(value).then(currencies => {
                    $('input[name="company_currency"]').parent().removeClass("disabled").dropdown("set exactly", currencies[0]);
                });
                
            }
        });
 
        var audiencesMenu = $('input[name="company_audience"] ~ .menu');
        for (var i in countries) {
            audiencesMenu.append("<div class=\"item\" data-value=\"" + i + "\"><i class=\"" + i.toLowerCase() + " flag\"></i>" + countries[i] + "</div>");
        }
        $('input[name="company_audience"]').parent().dropdown();

    });


    getCurrencies().then(currencies => {

        var currenciesMenu = $('input[name="company_currency"] ~ .menu').html("");

        for (var i in currencies) {
            currenciesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + currencies[i] + "</div>");
        }

        $('input[name="company_currency"]').parent().removeClass("disabled").dropdown();
    });
    
    
    getTimezones().then(timezones => {

        var timezonesMenu = $('input[name="company_timezone"] ~ .menu').html("");

        for (var i in timezones) {
            timezonesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + timezones[i] + "</div>");
        }

        $('input[name="company_timezone"]').parent().removeClass("disabled").dropdown();
    });


    $('input[name="terms_of_service"]').parent().checkbox();

    $('.ui .form')
            .form(
                    {
                        fields: {
                            company_name: {
                                identifier: 'company_name',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please enter your company name'
                                    }]
                            },

                            company_country: {
                                identifier: 'company_country',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please select the country you currently operate'
                                    }]
                            },
                            company_languge: {
                                identifier: 'company_language',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please select your language'
                                    }]
                            },
                            company_audience: {
                                identifier: 'company_audience',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please select your audience'
                                    }]
                            },
                            company_currency: {
                                identifier: 'company_currency',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please select your currency'
                                    }]
                            },
                            company_timezone: {
                                identifier: 'company_timezone',
                                rules: [{
                                        type: 'empty',
                                        prompt: 'Please select your timezone'
                                    }]
                            },

                            company_student_count: {
                                identifier: 'company_student_count',
                                rules: [{
                                        type: 'integer[100..100000]',
                                        prompt: 'Please enter a valid number of students'
                                    }]
                            },

                            company_staff_count: {
                                identifier: 'company_staff_count',
                                rules: [{
                                        type: 'integer[50..100000]',
                                        prompt: 'Please enter a valid number of staffs'
                                    }]
                            },

                            terms_of_service: {
                                identifier: 'terms_of_service',
                                rules: [{
                                        type: 'checked',
                                        prompt: 'You must agree the terms and conditions'
                                    }]
                            }
                        },

                        inline: true,
                        on: 'blur',
                        keyboardShortcuts: false,
                        onSuccess: function () {

                            $(".ui .form .button").addClass("disabled");

                            // Save to Local Storage, and continue to next step

                            window.localStorage.setItem("$SETUP_company_name",
                                    $('input[name="company_name"]').val());

                            window.localStorage
                                    .setItem("$SETUP_company_logo_url", $(
                                            'input[name="company_logo_url"]')
                                            .val() === undefined ? " " : $(
                                            'input[name="company_logo_url"]').val());


                            window.localStorage.setItem("$SETUP_company_country", $('input[name="company_country"]').val());

                            window.localStorage.setItem("$SETUP_company_language", $('input[name="company_language"]').val());

                            window.localStorage.setItem("$SETUP_company_audience", $('input[name="company_audience"]').val());

                            window.localStorage.setItem("$SETUP_company_currency", $('input[name="company_currency"]').val());

                            window.localStorage.setItem("$SETUP_company_timezone", $('input[name="company_timezone"]').val());

                            window.localStorage.setItem(
                                    "$SETUP_company_student_count",
                                    $('input[name="company_student_count"]')
                                    .val());

                            window.localStorage.setItem(
                                    "$SETUP_company_staff_count",
                                    $('input[name="company_staff_count"]')
                                    .val());

                            window.location = "/setup/two";
                        },
                        onFailure: function () {

                        }
                    });

    $(".ui .form .button").on('click', function (e) {
        $('.ui .form').form('validate form');
        // e.stopPropagation();
    });
}
