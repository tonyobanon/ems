
//@TODO
//Find an efficient to validate email and phone on the client

function load() {
	
	var currentYear = new Date().getFullYear();
	
	getCountries().then(countries => {
		
		var countriesMenu = $('input[name="profile_country"] ~ .menu');
		for(var i in countries){
			countriesMenu.append("<div class=\"item\" data-value=\"" + i + "\"><i class=\"" + i.toLowerCase() + " flag\"></i>" +  countries[i] + "</div>");
		}
		
		$('input[name="profile_country"]').parent().dropdown({
			onChange: (value, text, choice) => {
				
				getTerritories(value).then(territories => {
					
					var territoriesMenu = $('input[name="profile_territory"] ~ .menu').html("");
					
					for(var i in territories){
						territoriesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" +  territories[i] + "</div>");
					}
					
					$('input[name="profile_territory"]').parent().removeClass("disabled").dropdown({
						onChange: (value, text, choice) => {					
							
							getCities(value).then(cities => {
								
								var citiesMenu = $('input[name="profile_city"] ~ .menu').html("");
								
								for(var i in cities){
									citiesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" +  cities[i] + "</div>");
								}
								
								$('input[name="profile_city"]').parent().removeClass("disabled").dropdown();
								
							});
							
						}
					});
					
				});
			}
		});
		
	});
        
        $('input[name="profile_gender"]').parent().dropdown();
	
	
	 $('.ui .form').form({
         fields: {
        	 profile_fname: {
        		 identifier: 'profile_fname',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please enter your first name'
        		 }]
        	 },
                 profile_mname: {
        		 identifier: 'profile_mname',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please enter your middle name'
        		 }]
        	 },
        	 profile_lname: {
        		 identifier: 'profile_lname',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please enter the last name'
        		 }]
        	 },
        	 profile_pass: {
        		 identifier: 'profile_pass',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please enter a password'
        		 }, 
        		 {
        	         type: 'length[6]',
        	         prompt: 'Your password must be at least 6 characters'
        	         
        		 },
        		 {
        	         type: 'match[profile_passv]',
        	         prompt: 'The two passwords entered must match'
        	    }]
        	 },
        	 profile_passv: {
        		 identifier: 'profile_passv',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please verify your password'
        		 }]
        	 },
        	 profile_email: {
        		 identifier: 'profile_email',
        		 rules: [{
        			 type: 'email',
        			 // type:
						// 'regExp[/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/]'
        			 prompt: 'Please enter a valid email address'
        		 }]
        	 },
        	 
        	 profile_dob_day: {
        		 identifier: 'profile_dob_day',
        		 rules: [{
        			 type: 'integer[1..31]',
        			 prompt: 'Please enter a valid date'
        		 }]
        	 },       	 
        	 
        	 profile_dob_month: {
        		 identifier: 'profile_dob_month',
        		 rules: [{
        			 type: 'integer[1..12]',
        			 prompt: 'Please enter a valid month'
        		 }]
        	 },       	 
        	 
        	 profile_dob_year: {
        		 identifier: 'profile_dob_year',
        		 rules: [{
        			 type: 'integer[1930..' + new Number(currentYear - 18) + ']',
        			 prompt: 'You must be at least 18 years old'
        		 }]
        	 },
        	 
        	 profile_address: {
        		 identifier: 'profile_address',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please enter your address'
        		 }]
        	 },
        	 
        	 profile_phone: {
        		 identifier: 'profile_phone',
        		 rules: [{
        			 type: 'integer',
        			 prompt: 'Please enter a valid phone number'
        		 }]
        	 },
                 
                 profile_gender: {
        		 identifier: 'profile_gender',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please selct your gender'
        		 }]
        	 },
        	 
        	 profile_country: {
        		 identifier: 'profile_country',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please selct a country'
        		 }]
        	 },
        	 
        	 profile_territory: {
        		 identifier: 'profile_territory',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please select a territory'
        		 }]
        	 },
        	 
        	 profile_city: {
        		 identifier: 'profile_city',
        		 rules: [{
        			 type: 'empty',
        			 prompt: 'Please select a city'
        		 }]
        	 }},
        	 
        	 inline: true,
        	 on: 'blur',
        	 keyboardShortcuts: false,
        	 onSuccess: function () {
        		
        		 if(localStorage.getItem("$SETUP_profiles") === null){
        			 localStorage.setItem("$SETUP_profiles", "[]");
        			 localStorage.setItem("$SETUP_keys", "[]");
        		 }
        		 
        		 var keys = JSON.parse(localStorage.getItem("$SETUP_keys"));
        		
        		 if(keys.indexOf($('input[name="profile_email"]').val()) !== -1
        				 ||  keys.indexOf($('input[name="profile_phone"]').val())  !== -1
        		 ){
        			 alert("All users must have unique emails and phone numbers");
        			 return;
        		 }
        		 
        		 var profile = {
        				 email:  $('input[name="profile_email"]').val(),
        				 password:  $('input[name="profile_pass"]').val(),
                                         
        				 firstName:  $('input[name="profile_fname"]').val(),
                                         middleName:  $('input[name="profile_mname"]').val(),
        				 lastName:  $('input[name="profile_lname"]').val(),
        				 
        				 dateOfBirth:  $('input[name="profile_dob_year"]').val() + "-"
        				 			+ $('input[name="profile_dob_month"]').val() + "-"
        				 					+ $('input[name="profile_dob_day"]').val(),
        				
        				 address:  $('input[name="profile_address"]').val(),
        				 phone:  $('input[name="profile_phone"]').val(),
        				 gender: $('input[name="profile_gender"]').val(),
                                         
        				 country:  $('input[name="profile_country"]').val(),
        				 territory:  $('input[name="profile_territory"]').val(),
        				 city:  $('input[name="profile_city"]').val()    				 
        		 }
        		 
        		 localStorage.setItem("$SETUP_profile____" + $('input[name="profile_email"]').val(), JSON.stringify(profile));
        		 
        		 var profiles = JSON.parse(localStorage.getItem("$SETUP_profiles"));        		   	
        		 profiles.push($('input[name="profile_email"]').val());
        		 localStorage.setItem("$SETUP_profiles", JSON.stringify(profiles));
        		 
        		 
        		 keys.push($('input[name="profile_email"]').val());
        		 keys.push($('input[name="profile_phone"]').val());
        		 localStorage.setItem("$SETUP_keys", JSON.stringify(keys));
        		 
        		 
        		  window.location = "/setup/three";
        	 },
        	 onFailure: function () {
        	
        	 }
     });
	
	 $(".ui .form .button").on('click', function (e) {
		 $('.ui .form').form('validate form');
		// e.stopPropagation();
	 });
	
}