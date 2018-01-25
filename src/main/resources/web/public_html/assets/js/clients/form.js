

 function listSystemConfigurationFieldNames (sectionId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/list-system-configuration-field-names?sectionId=" + sectionId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listApplicationFormFieldNames (sectionId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/list-application-form-field-names?sectionId=" + sectionId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listApplicationFormSections (realm) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/list-application-form-sections?realm=" + realm
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listSystemConfigurationSections () {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/list-system-configuration-sections"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listApplicationFormFields (sectionId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/list-application-form-fields?sectionId=" + sectionId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listAllApplicationFormFields (sectionIds) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({sectionIds: sectionIds}), 
			 url: "/api/forms/list-all-application-form-fields"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listSystemConfigurationFields (sectionId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/list-system-configuration-fields?sectionId=" + sectionId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function listAllSystemConfigurationFields (sectionIds) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({sectionIds: sectionIds}), 
			 url: "/api/forms/list-all-system-configuration-fields"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function deleteApplicationFormSection (sectionId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "DELETE",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/delete-application-form-section?sectionId=" + sectionId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function deleteSystemConfigurationSection (sectionId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "DELETE",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/delete-system-configuration-section?sectionId=" + sectionId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function deleteApplicationFormField (fieldId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "DELETE",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/delete-application-form-field?fieldId=" + fieldId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function deleteSystemConfigurationField (fieldId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "DELETE",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/delete-system-configuration-field?fieldId=" + fieldId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function newApplicationFormSimpleField (sectionId, spec) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({sectionId: sectionId, spec: spec}), 
			 url: "/api/forms/create-application-form-simple-field"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function newApplicationFormCompositeField (sectionId, spec) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({sectionId: sectionId, spec: spec}), 
			 url: "/api/forms/create-application-form-composite-field"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function newSystemConfigurationSimpleField (sectionId, spec) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({sectionId: sectionId, spec: spec}), 
			 url: "/api/forms/create-system-configuration-simple-field"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function newSystemConfigurationCompositeField (sectionId, spec) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({sectionId: sectionId, spec: spec}), 
			 url: "/api/forms/create-system-configuration-composite-field"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getApplicationFormFieldIds (realm) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/forms/get-application-form-field-ids?realm=" + realm
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function newApplicationFormSection (name, realm) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({name: name, realm: realm}), 
			 url: "/api/forms/new-application-form-section"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }
