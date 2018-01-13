

 function doSetup (payload) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({payload: payload}), 
			 url: "/api/platform/tools/setup"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }
