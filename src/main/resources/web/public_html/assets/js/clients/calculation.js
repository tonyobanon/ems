

 function canUserManageScoreGrades () {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/can-manage-score-grades"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getScoreGrades () {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/get-score-grades"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function updateScoreGrades (grades) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({grades: grades}), 
			 url: "/api/calculation/update-score-grades"
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
			 url: "/api/calculation/is-semester-course-sheet-created?courseCode=" + courseCode
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function canManageResultSheet (academicSemesterCourseId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/can-manage-score-sheet?academicSemesterCourseId=" + academicSemesterCourseId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function createScoreSheet (academicSemesterCourseId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({academicSemesterCourseId: academicSemesterCourseId}), 
			 url: "/api/calculation/create-score-sheet"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getScoreSheet (courseCode, academicSemesterId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/get-score-sheet?courseCode=" + courseCode + "&academicSemesterId=" + academicSemesterId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getScoreSheetWithId (academicSemesterCourseId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/get-score-sheet-with-id?academicSemesterCourseId=" + academicSemesterCourseId
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
			 url: "/api/calculation/update-score-sheet"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function submitScoreSheet (academicSemesterCourseId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({academicSemesterCourseId: academicSemesterCourseId}), 
			 url: "/api/calculation/submit-score-sheet"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function newAssessmentTotal (spec) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "PUT",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 contentType : 'application/json', 
			 data : JSON.stringify({spec: spec}), 
			 url: "/api/calculation/new-assessment-total"
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
			 url: "/api/calculation/delete-assessment-total"
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getAssessmentTotals (levelSemesterId) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/get-assessment-totals?levelSemesterId=" + levelSemesterId
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }


 function getLevelSemester (departmentLevelId, semester) {
	 return new Promise(function(resolve, reject) {
		 $.ajax({
			 method : "GET",
			 async: true,
			 processData: false,
			 statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader("X-Location");}},
			 url: "/api/calculation/get-level-semester?departmentLevelId=" + departmentLevelId + "&semester=" + semester
			 }).done(function(o) {
				 resolve(o);
			 }).fail(function(jqXHR, status, error){
				 if(jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302){
					 reject(jqXHR);
				 }
			 });
		 });
 }
