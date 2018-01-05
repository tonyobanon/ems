

 function newAssessmentTotal (spec) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({spec: spec}), 
			 url:  " /api/calculation/new-assessment-total"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getLevelAssessmentTotals (departmentLevel) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url:  " /api/calculation/get-level-assessment-totals?departmentLevel=" + departmentLevel
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function updateScoreSheet (academicSemesterCourseId, updates) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({academicSemesterCourseId: academicSemesterCourseId, updates: updates}), 
			 url:  " /api/calculation/update-course-sheet"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getAcademicSemesterCourses (academicSemesterId, courseCodes) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({academicSemesterId: academicSemesterId, courseCodes: courseCodes}), 
			 url:  " /api/calculation/get-academic-semester-courses"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function isSemesterCourseSheetCreated (courseCode) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url:  " /api/calculation/is-semester-course-sheet-created?courseCode=" + courseCode
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function createScoreSheet (courseCode) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({courseCode: courseCode}), 
			 url:  " /api/calculation/create-course-sheet"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function deleteAssessmentTotal (totalId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "POST",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({totalId: totalId}), 
			 url:  " /api/calculation/delete-assessment-total"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }
