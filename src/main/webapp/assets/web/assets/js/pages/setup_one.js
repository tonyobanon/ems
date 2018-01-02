
function load() {

	$('.ui.basic.modal').modal('show');

	$('input[name="terms_of_service"]').parent().checkbox();

	$('.ui .form')
			.form(
					{
						fields : {
							company_name : {
								identifier : 'company_name',
								rules : [ {
									type : 'empty',
									prompt : 'Please enter your company name'
								} ]
							},

							company_student_count : {
								identifier : 'company_student_count',
								rules : [ {
									type : 'integer[100..100000]',
									prompt : 'Please enter a valid number of students'
								} ]
							},

							company_staff_count : {
								identifier : 'company_staff_count',
								rules : [ {
									type : 'integer[50..100000]',
									prompt : 'Please enter a valid number of staffs'
								} ]
							},

							terms_of_service : {
								identifier : 'terms_of_service',
								rules : [ {
									type : 'checked',
									prompt : 'You must agree the terms and conditions'
								} ]
							}
						},

						inline : true,
						on : 'blur',
						keyboardShortcuts : false,
						onSuccess : function() {

							$(".ui .form .button").addClass("disabled");

							// Save to Local Storage, and continue to next step
							
							window.localStorage.setItem("$SETUP_company_name",
									$('input[name="company_name"]').val());
							
							window.localStorage
									.setItem("$SETUP_company_logo_url", $(
											'input[name="company_logo_url"]')
											.val() === undefined ? " " : $(
											'input[name="company_logo_url"]').val());
							
							window.localStorage.setItem(
									"$SETUP_company_student_count",
									$('input[name="company_student_count"]')
											.val());
							
							window.localStorage.setItem(
									"$SETUP_company_staff_count",
									$('input[name="company_staff_count"]')
											.val());

							window.location = "/w/setup/two";
						},
						onFailure : function() {

						}
					});

	$(".ui .form .button").on('click', function(e) {
		$('.ui .form').form('validate form');
		// e.stopPropagation();
	});
}
