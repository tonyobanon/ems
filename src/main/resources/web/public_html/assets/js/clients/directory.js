
function newAcademicSemester(spec) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "PUT",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				spec : spec
			}),
			url : "/api/directory/new-academic-semester"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function endCurrentSemester() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/end-current-semester"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getCurrentSemester() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-current-semester"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function listAcademicSemester() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/list-academic-semester"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function createDepartment(spec) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "PUT",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				spec : spec
			}),
			url : "/api/directory/create-department"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getLecturers(departmentId, departmentLevelId) {
	return new Promise(function(resolve, reject) {
		$.ajax(
				{
					method : "GET",
					async : true,
					processData : false,
					statusCode : {
						302 : function(jqXHR, status, error) {
							window.location = jqXHR
									.getResponseHeader("X-Location");
						}
					},
					url : "/api/directory/get-lecturers?departmentId="
							+ departmentId + "&departmentLevelId="
							+ departmentLevelId
				}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getDepartment(departmentId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-department?departmentId=" + departmentId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getFaculty(facultyId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-faculty?facultyId=" + facultyId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getStudents(departmentLevelId) {
	return new Promise(function(resolve, reject) {
		$.ajax(
				{
					method : "GET",
					async : true,
					processData : false,
					statusCode : {
						302 : function(jqXHR, status, error) {
							window.location = jqXHR
									.getResponseHeader("X-Location");
						}
					},
					url : "/api/directory/get-students?departmentLevelId="
							+ departmentLevelId
				}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getAvailableCourses() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-available-courses"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function listStudentCourses(studentId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/list-student-courses?studentId=" + studentId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function registerStudentCourses(studentId, courses) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "PUT",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				studentId : studentId,
				courses : courses
			}),
			url : "/api/directory/register-student-courses"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getStudentSemesterCourses(studentId) {
	return new Promise(
			function(resolve, reject) {
				$
						.ajax(
								{
									method : "GET",
									async : true,
									processData : false,
									statusCode : {
										302 : function(jqXHR, status, error) {
											window.location = jqXHR
													.getResponseHeader("X-Location");
										}
									},
									url : "/api/directory/get-student-semester-courses?studentId="
											+ studentId
								})
						.done(function(o) {
							resolve(o);
						})
						.fail(
								function(jqXHR, status, error) {
									if (jqXHR.getResponseHeader("X-Location") === null
											&& jqXHR.status !== 302) {
										reject(jqXHR);
									}
								});
			});
}

function getStudentSemesterPerformance(studentId) {
	return new Promise(
			function(resolve, reject) {
				$
						.ajax(
								{
									method : "GET",
									async : true,
									processData : false,
									statusCode : {
										302 : function(jqXHR, status, error) {
											window.location = jqXHR
													.getResponseHeader("X-Location");
										}
									},
									url : "/api/directory/get-student-semester-performance?studentId="
											+ studentId
								})
						.done(function(o) {
							resolve(o);
						})
						.fail(
								function(jqXHR, status, error) {
									if (jqXHR.getResponseHeader("X-Location") === null
											&& jqXHR.status !== 302) {
										reject(jqXHR);
									}
								});
			});
}

function addLecturerCourses(lecturerId, courses) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "PUT",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				lecturerId : lecturerId,
				courses : courses
			}),
			url : "/api/directory/add-lecturer-courses"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function removeLecturerCourse(lecturerId, course) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "DELETE",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				lecturerId : lecturerId,
				course : course
			}),
			url : "/api/directory/remove-lecturer-course"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function canUserManageDepartments() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/can-manage-departments"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getAllSemesters() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-all-semesters"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getDepartmentForLevel(departmentLevelId) {
	return new Promise(
			function(resolve, reject) {
				$
						.ajax(
								{
									method : "GET",
									async : true,
									processData : false,
									statusCode : {
										302 : function(jqXHR, status, error) {
											window.location = jqXHR
													.getResponseHeader("X-Location");
										}
									},
									url : "/api/directory/get-department-for-level?departmentLevelId="
											+ departmentLevelId
								})
						.done(function(o) {
							resolve(o);
						})
						.fail(
								function(jqXHR, status, error) {
									if (jqXHR.getResponseHeader("X-Location") === null
											&& jqXHR.status !== 302) {
										reject(jqXHR);
									}
								});
			});
}

function getAssociatedDepartmentLevels(departmentLevelId) {
	return new Promise(
			function(resolve, reject) {
				$
						.ajax(
								{
									method : "GET",
									async : true,
									processData : false,
									statusCode : {
										302 : function(jqXHR, status, error) {
											window.location = jqXHR
													.getResponseHeader("X-Location");
										}
									},
									url : "/api/directory/get-associated-department-levels?departmentLevelId="
											+ departmentLevelId
								})
						.done(function(o) {
							resolve(o);
						})
						.fail(
								function(jqXHR, status, error) {
									if (jqXHR.getResponseHeader("X-Location") === null
											&& jqXHR.status !== 302) {
										reject(jqXHR);
									}
								});
			});
}

function listDepartmentLevels(departmentId) {
	return new Promise(function(resolve, reject) {
		$.ajax(
				{
					method : "GET",
					async : true,
					processData : false,
					statusCode : {
						302 : function(jqXHR, status, error) {
							window.location = jqXHR
									.getResponseHeader("X-Location");
						}
					},
					url : "/api/directory/list-department-levels?departmentId="
							+ departmentId
				}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function listDepartmentNames(facultyId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/list-department-names?facultyId=" + facultyId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function listDepartments(facultyId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/list-departments?facultyId=" + facultyId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function createFaculty(spec) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "PUT",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				spec : spec
			}),
			url : "/api/directory/create-faculty"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function canUserManageFaculties() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/can-manage-faculties"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function listFaculties() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/list-faculties"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function listFacultyNames() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/list-faculty-names"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getStudent(studentId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-student?studentId=" + studentId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getLecturer(lecturerId) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-lecturer?lecturerId=" + lecturerId
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function createCourse(spec) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "PUT",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			contentType : 'application/json',
			data : JSON.stringify({
				spec : spec
			}),
			url : "/api/directory/create-course"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function canUserManageCourses() {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/can-manage-courses"
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getCourse(courseCode) {
	return new Promise(function(resolve, reject) {
		$.ajax({
			method : "GET",
			async : true,
			processData : false,
			statusCode : {
				302 : function(jqXHR, status, error) {
					window.location = jqXHR.getResponseHeader("X-Location");
				}
			},
			url : "/api/directory/get-course?courseCode=" + courseCode
		}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}

function getSemesterCourses(departmentLevel, academicSemesterId) {
	return new Promise(
			function(resolve, reject) {
				$
						.ajax(
								{
									method : "GET",
									async : true,
									processData : false,
									statusCode : {
										302 : function(jqXHR, status, error) {
											window.location = jqXHR
													.getResponseHeader("X-Location");
										}
									},
									url : "/api/directory/get-semester-courses?departmentLevel="
											+ departmentLevel
											+ "&academicSemesterId="
											+ academicSemesterId
								})
						.done(function(o) {
							resolve(o);
						})
						.fail(
								function(jqXHR, status, error) {
									if (jqXHR.getResponseHeader("X-Location") === null
											&& jqXHR.status !== 302) {
										reject(jqXHR);
									}
								});
			});
}

function getLevelCourses(departmentLevel) {
	return new Promise(function(resolve, reject) {
		$.ajax(
				{
					method : "GET",
					async : true,
					processData : false,
					statusCode : {
						302 : function(jqXHR, status, error) {
							window.location = jqXHR
									.getResponseHeader("X-Location");
						}
					},
					url : "/api/directory/get-level-courses?departmentLevel="
							+ departmentLevel
				}).done(function(o) {
			resolve(o);
		}).fail(
				function(jqXHR, status, error) {
					if (jqXHR.getResponseHeader("X-Location") === null
							&& jqXHR.status !== 302) {
						reject(jqXHR);
					}
				});
	});
}
