
//@TODO
//Check for nulls

function finish() {

	$(".ui .form .button").addClass("disabled");

	var payload = {};
         
	payload["companyName"] = localStorage.getItem("$SETUP_company_name");
	payload["companyLogoUrl"] = localStorage.getItem("$SETUP_company_logo_url");
        
	payload["country"] = localStorage.getItem("$SETUP_company_country");
	payload["audience"] = localStorage.getItem("$SETUP_company_audience");
        
	payload["studentCount"] = localStorage.getItem("$SETUP_company_student_count");
	payload["employeeCount"] = localStorage.getItem("$SETUP_company_staff_count");
        
        payload["academicSemester"] = JSON.parse(localStorage.getItem("$SETUP_academicSemester"));
        
	var profileList = [];
	var profiles = JSON.parse(localStorage.getItem("$SETUP_profiles"));

	for ( var i in profiles) {
		var email = profiles[i];
		profileList.push(JSON
				.parse(localStorage.getItem("$SETUP_profile____" + email)));
	}

	payload["admins"] = profileList;

        payload["mailCredentials"] = JSON.parse(localStorage.getItem("$SETUP_mailCredentials"));
           
	payload["language"] = localStorage.getItem("$SETUP_company_language");
        payload["currency"] = localStorage.getItem("$SETUP_company_currency");
        payload["timezone"] = localStorage.getItem("$SETUP_company_timezone");

        console.log(payload);

	showSpinner();
	doSetup(payload);

	// @TODO: Clear local storage entries
}
