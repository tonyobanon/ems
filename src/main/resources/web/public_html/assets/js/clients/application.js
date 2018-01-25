

 function getApplicationDeclineReasons () {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/user-applications/get-decline-reasons"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function declineApplication (applicationId, reason) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({applicationId: applicationId, reason: reason}), 
			 url: "/api/user-applications/decline-application"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getPDFQuestionnaire (roleName) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/user-applications/get-pdf-questionnaire?roleName=" + roleName
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getApplicationFieldValues (applicationId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/user-applications/get-field-values?applicationId=" + applicationId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getConsolidatedApplicationFieldValues (applicationId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/user-applications/get-consolidated-field-values?applicationId=" + applicationId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function acceptApplication (applicationId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({applicationId: applicationId}), 
			 url: "/api/user-applications/accept-application"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function submitApplication (applicationId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({applicationId: applicationId}), 
			 url: "/api/user-applications/submit-application"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getApplicationRole (applicationId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/user-applications/get-application-role?applicationId=" + applicationId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function updateApplication (applicationId, values) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({applicationId: applicationId, values: values}), 
			 url: "/api/user-applications/update-application"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function createApplication (roleName) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({roleName: roleName}), 
			 url: "/api/user-applications/create-application"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function canUserReviewApplications () {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/user-applications/can-user-review"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }
