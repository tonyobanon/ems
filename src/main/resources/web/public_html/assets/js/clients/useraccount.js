

 function loginByPhone (phone, pass, rem, returnUrl) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 headers : {
				 phone: phone, 
				 pass: pass, 
				 rem: rem
			 }, 
			 url: "/api/users/accounts/phoneAuth?returnUrl=" + returnUrl
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function loginByEmail (email, pass, rem, returnUrl) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 headers : {
				 email: email, 
				 pass: pass, 
				 rem: rem
			 }, 
			 url: "/api/users/accounts/emailAuth?returnUrl=" + returnUrl
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }
