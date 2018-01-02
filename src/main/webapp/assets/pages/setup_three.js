
//@TODO
//Check for nulls

function finish() {

	$(".ui .form .button").addClass("disabled");

	var payload = {};
	payload["companyName"] = localStorage.getItem("company_name");
	payload["companyLogoUrl"] = localStorage.getItem("company_logo");
	payload["country"] = localStorage.getItem("company_country");
	payload["language"] = localStorage.getItem("company_language");
	payload["audience"] = localStorage.getItem("company_audience");

	var profileList = [];
	var profiles = JSON.parse(localStorage.getItem("profiles"));

	for ( var i in profiles) {
		var email = profiles[i];
		profileList.push(JSON
				.parse(localStorage.getItem("profile____" + email)));
	}

	payload["admins"] = profileList;

	showSpinner();
	doSetup(payload);

	// @TODO: Clear local storage entries
}
