var SELECTIONS = new Array();
var CURRENT_SELECTION;

var CTRL_CTX = false;
var CTX_MENU_VISIBLE = false;

var CONTENT_KEYS = new Object();

var KEYS = new Array();
var DIALOG_OPEN = false;

function init() {

	/** ************************* Retrieve Contents ******************** */
	$
			.ajax({
				url : apiRoutes["BLOG_LIST_API"],
				type : "GET",
				mimeType : "application/json",
				processData : false
			})
			.done(
					function(data, dataType, status) {

						/** **************** Update DOM accordingly ************* */

						if (jQuery.isEmptyObject(data)) {
							$('.container')
									.html(
											"<br> <div class='text-center'> "
													+ "<h1 class='pretty-print'> No Blog Content has been added yet.. </h1> <br> "
													+ "<br><br><br>"
													+ "<a type='button' href='"
													+ dispatchRoutes["BLOG_CREATE"]
													+ "' class='btn btn-default btn-lg' style=''>Create new content</a>");
							return;
						}

						$('.container')
								.empty()
								.append(
										"<br><h2 class='pretty-print'><i class='fa fa-codepen' style='color: #1279c6;'></i> Blog Manager</h2><br>"
												+ "<table style='padding-left: 20px;' class='table table-hover'></table>");

						$('table').append(
								"<thead><tr>"

								+ "<th>Title</th>" + "<th>Views</th>"
										+ "<th>Comments</th>"
										+ "<th>Owner</th>"
										+ "<th>Date Created</th>"

										+ "</tr></thead>");

						$('table').append("<tbody></tbody>");

						for ( var i in data) {

							var obj = data[i];

							$('table tbody').append(
									"<tr id='" + i
											+ "' style='cursor: pointer;'>");

							// obj.title will be split if it exceeds it
							obj.title = trimText(obj.title, 65);

							$('#' + i).append("<td>" + obj.title + "</td>")
									.append("<td>" + obj.views + "</td>")
									.append("<td>" + obj.comments + "</td>")
									.append(
											"<td>" + obj.principalName
													+ "</td>").append(
											"<td>" + obj.date + "</td>")

							KEYS.push(i);

							CONTENT_KEYS[i] = {
								ownsContentKey : obj.ownsContentKey,
								contentKey : obj.contentKey
							}
						}

						$('.container')
								.append(
										"<br><br>"
												+ "<a type='button' href='"
												+ dispatchRoutes["BLOG_CREATE"]
												+ "' class='btn btn-default'><span class='pretty-print'>Create new content<span></a>"
												+ "<br><br>");

						/** ******************************************************************* */

						post_init();
					});

	/** ***************************************************************************** */

}

function post_init() {
	
	// Initialize tooltips
	$(function() {
		$('[data-toggle="tooltip"]').tooltip()
	});
	
	// Set links
	$('#media_manager_link').attr('href', dispatchRoutes["MEDIA_LIST"]);

	/**
	 * ************************ Initialize Click Events ******************
	 */

	// initialize context Menu
	$
			.contextMenu({
				selector : 'table > tbody > tr',
				items : {

					copy : {
						name : "Copy Key",
						icon : 'copy',
						callback : function(key, opt) {
							copySelection();
						}
					},

					update : {
						name : "Update",
						icon : "edit",
						items : {
							title : {
								name : "Title",
								icon : 'tag',
								callback : function(key, opt) {
									$('#update_title_modal').modal('show');
								}
							},
							subtitle : {
								name : "Sub-Title",
								icon : 'underline',
								callback : function(key, opt) {
									$('#update_sub_title_modal').modal('show');
								}
							},
							image : {
								name : "Image",
								icon : 'photo',
								callback : function(key, opt) {
									$('#update_imageKey_modal').modal('show');
								}
							},
							content : {
								name : "Content",
								icon : 'html5',
								callback : function(key, opt) {
									setCookie("return_url",
											dispatchRoutes["BLOG_LIST"],
											dispatchRoutes["CONTENT_UPDATE"]);
									window.location = dispatchRoutes["CONTENT_UPDATE"]
											+ "?contentKey="
											+ CONTENT_KEYS[CURRENT_SELECTION].contentKey;
								},
								visible : function() {
									return CONTENT_KEYS[CURRENT_SELECTION].ownsContentKey;
								}
							}
						}
					},

					del : {
						name : "Delete",
						icon : 'delete',
						callback : function(key, opt) {
							deleteSelections();
						}
					},

					info : {
						name : "Properties",
						icon : 'info',
						callback : function(key, opt) {
							$('#info_modal').modal('show');
						}
					}
				},

				events : {
					show : function(opt) {

						var id = $(this).prop('id');

						if (SELECTIONS.indexOf(id) === -1) {

							// This element is not in the
							// SELECTIONS array, so we clear
							// others, and select only this

							clearSelections();
							select(id);

						} else {

							// This element already exists
							// in the SELECTIONS
							// array, so, just set
							// CURRENT_SELECTION
							CURRENT_SELECTION = id;
						}

						CTX_MENU_VISIBLE = true;
						return true;
					},
					hide : function(opt) {
						CTX_MENU_VISIBLE = false;
					}
				}

			});

	$('table > tbody > tr').mousedown(function(e) {

		e.preventDefault();

		// Now, jquery.contextMenu.js has
		// taken care
		// of right clicks, we need to
		// implement
		// logic for left clicks

		if ((e.which === 1)) {

			var id = $(this).prop('id');

			if (id === CURRENT_SELECTION && SELECTIONS.length === 1) {
				// Do nothing, the user is
				// clicking
				// the same
				// (single) file
				return;
			}

			if (CTRL_CTX) {

				if (jQuery.inArray(id, SELECTIONS) !== -1) {
					// Since, the file is
					// already selected,
					// we unselect
					unselect(id);
					return;
				}

			} else {
				clearSelections();
			}

			select(id);
		}

	});

	// If user left clicks outside the table, clear
	// selections
	// Note: we must not call clearSelections() if the
	// context menu is open, and if the dialog is open
	// The context menu, or dialog is pointless if CURRENT_SELECTION
	// becomes undefined, as a result of the call to clearSelections()

	$("body").mousedown(
			function(e) {
				if (e.which === 1
						&& !(e.target instanceof HTMLTableCellElement)
						&& !CTX_MENU_VISIBLE && !DIALOG_OPEN) {
					clearSelections();
				}
			});

	/** ******************************************************************** */

	/**
	 * ************************ Initialize Keyboard Events ******************
	 */

	// ctrl down
	$(document).keydown(function(e) {
		if (/* e.ctrlKey && */e.keyCode === 17) {
			CTRL_CTX = true;
			e.preventDefault();
		}
	});

	// ctrl up
	$(document).keyup(function(e) {
		if (/* e.ctrlKey && */e.keyCode === 17) {
			CTRL_CTX = false;
		}
	});

	// Ctrl + a
	$(document).keydown(function(e) {
		if (e.keyCode === 65 && e.ctrlKey) {
			e.preventDefault();
			selectAll();
		}
	});

	// Ctrl + c
	$(document).keydown(function(e) {
		if (e.keyCode === 67 && e.ctrlKey) {
			copySelection();
		}
	});

	// Ctrl + d
	$(document).keydown(function(e) {
		if (e.keyCode === 68 && e.ctrlKey) {
			e.preventDefault();
			deleteSelections();
		}
	});

	/** ******************************************************************** */

	/** ******************************************************************** */

	/**
	 * ******************** Initialize clipboardjs *******************
	 */

	// First, lets append a hidden button to act as the
	// trigger
	$('body')
			.append(
					"<button id='copybuttonHidden' style='visibility: hidden'></button>");

	// Then initialize
	var clipboard = new Clipboard('#copybuttonHidden', {
		text : function() {
			return CURRENT_SELECTION;
		}
	});

	/** **************************************************************** */

	/**
	 * ************************ Initialize 'Title Update' Modal and Button
	 * ************************
	 */

	// Initialize 'Update Title' Modal
	$('#update_title_modal').modal({
		show : false
	});

	$('#update_title_modal').on('show.bs.modal', function() {
		DIALOG_OPEN = true;

		// Clear messages
		$('#title_msg').html('');
		$('#title_msg').css('visibility', 'hidden');

		// Reset Input
		$('#title_input').val('');

		// Focus Input
		$('#title_input').focus()
	});

	$('#update_title_modal').on('hidden.bs.modal', function() {
		DIALOG_OPEN = false;
	});

	// Add Listener to the 'Save changes' button in the
	// 'Update Title' Modal
	$('#update_title_trigger').on(
			'click',
			function(e) {
				var title = $('#title_input').val();

				if (title === "") {
					$('#title_msg').html('Enter a valid title');
					$('#title_msg').css('visibility', 'visible');
					return;
				}

				$.ajax(
						{
							url : apiRoutes["BLOG_TITLE_API"] + "?blogId="
									+ CURRENT_SELECTION + "&title=" + title,
							type : "POST",
							processData : true
						}).done(function() {
					// Hide Modal
					$('#update_title_modal').modal('hide');

					// title will be split if it exceeds it
					title = trimText(title, 65);

					// Update 'Title' in Table
					$('#' + CURRENT_SELECTION + '>td:first').html(title);
				});

			});

	/** ***************************************************************************** */

	
	
	/**
	 * ************************ Initialize 'Sub Title Update' Modal and Button
	 * ************************
	 */

	// Initialize 'Update Sub-Title' Modal
	$('#update_sub_title_modal').modal({
		show : false
	});

	$('#update_sub_title_modal').on('show.bs.modal', function() {
		DIALOG_OPEN = true;

		// Reset Input
		$('#sub_title_input').val('');

		// Focus Input
		$('#sub_title_input').focus()
	});

	$('#update_sub_title_modal').on('hidden.bs.modal', function() {
		DIALOG_OPEN = false;
	});

	// Add Listener to the 'Save changes' button in the
	// 'Update Title' Modal
	$('#update_sub_title_trigger').on(
			'click',
			function(e) {
				var subtitle = $('#sub_title_input').val();

				$.ajax(
						{
							url : apiRoutes["BLOG_SUBTITLE_API"] + "?blogId="
									+ CURRENT_SELECTION + "&subtitle=" + subtitle,
							type : "POST",
							processData : true
						}).done(function() {
					// Hide Modal
					$('#update_sub_title_modal').modal('hide');
				});

			});

	/** ***************************************************************************** */

	
	
	
	
	/**
	 * ************************ Initialize 'Image Update' Modal and Button
	 * ************************
	 */

	// Initialize 'Image Update' Modal
	$('#update_imageKey_modal').modal({
		show : false
	});

	$('#update_imageKey_modal').on('show.bs.modal', function() {
		DIALOG_OPEN = true;

		// Clear messages
		$('#imageKey_msg').html('');
		$('#imageKey_msg').css('visibility', 'hidden');

		// Reset Input
		$('#imageKey_input').val('');

		// Focus Input
		$('#imageKey_input').focus()
	});

	$('#update_imageKey_modal').on('hidden.bs.modal', function() {
		DIALOG_OPEN = false;
	});

	// Add Listener to the 'Save changes' button in the
	// 'Image Update' Modal
	$('#update_imageKey_trigger').on('click', function(e) {
		var imageKey = $('#imageKey_input').val();

		if (imageKey === "") {
			$('#imageKey_msg').html('Enter a valid Image Key');
			$('#imageKey_msg').css('visibility', 'visible');
			return;
		}

		// Validate Image Key
		validate_imageBlobKey(imageKey);
	});

	/** ***************************************************************************** */

	/**
	 * ************************ Initialize 'Info' Modal ************************
	 */

	// Initialize 'Content Info' Modal
	$('#info_modal').modal({
		show : false
	});

	$('#info_modal').on('show.bs.modal', function() {
		DIALOG_OPEN = true;

		// Retrieve Properties and Update DOM
		$.ajax({
			url : apiRoutes["BLOG_INFO_API"] + "?blogId=" + CURRENT_SELECTION,
			type : "GET",
			mimeType : "application/json",
			processData : false
		}).done(function(data) {

			
			$('#info_id').html(data.id);
			$('#info_imageBlobKey').html(data.imageBlobKey);
			$('#info_subtitle').html(data.subtitle);
			$('#info_title').html(data.title);
			$('#info_content_preview').html(data.contentPreview);
			$('#info_owns_Content_Key').html(data.ownsContentKey);
			$('#info_content_key').html(data.contentKey);
			$('#info_content_last_updated').html(data.contentLastUpdated);
			$('#info_views').html(data.views);
			$('#info_comments').html(data.comments);
		
			$('#info_principal_name').html(data.principalName);
			$('#info_principal_referral').html(data.principalReferal);
			$('#info_date_created').html(data.date);
			$('#info_date_updated').html(data.lastUpdated);

		});

	});

	$('#info_modal').on('hidden.bs.modal', function() {
		DIALOG_OPEN = false;
	});

	/** ***************************************************************************** */

}

function copySelection() {
	// Simulate a click on #copybuttonHidden, the clipboardjs trigger button
	$("#copybuttonHidden").trigger("click");
}

function clearSelections() {
	for ( var i in SELECTIONS) {
		$('#' + SELECTIONS[i]).css('backgroundColor', '');
	}
	SELECTIONS = new Array();
	CURRENT_SELECTION = undefined;
}

function select(id) {

	$('#' + id).css('backgroundColor', '#eae7ff');
	CURRENT_SELECTION = id;
	SELECTIONS.push(id);
}

function unselect(id) {

	$('#' + id).css('backgroundColor', '');

	if (CURRENT_SELECTION === id) {
		CURRENT_SELECTION = undefined;
	}

	var index = jQuery.inArray(id, SELECTIONS);
	SELECTIONS.splice(index, 1);
}

function selectAll() {
	SELECTIONS = new Array();
	for ( var i in KEYS) {
		select(KEYS[i]);
	}
}

function deleteSelections() {

	if (CURRENT_SELECTION === undefined || SELECTIONS.length === 0) {
		return;
	}

	$
			.ajax({
				url : apiRoutes["BLOG_API"],
				type : "DELETE",
				processData : false,
				data : JSON.stringify(SELECTIONS)
			})
			.done(
					function() {

						for ( var i in SELECTIONS) {

							// Remove deleted from FILE_KEYS array
							// First, we find its index in the FILE_KEYS array
							var index = jQuery
									.inArray(SELECTIONS[i], KEYS);
							KEYS.splice(index, 1);

							// Detach from DOM
							$('#' + SELECTIONS[i]).remove();
						}

						if (KEYS.length === 0) {
							$('.container')
									.html(
											"<br> <div class='text-center'> "
													+ "<h1 class='pretty-print'> No Blog Content has been added yet.. </h1> <br> "
													+ "<br><br><br>"
													+ "<a type='button' href='"
													+ dispatchRoutes["BLOG_CREATE"]
													+ "' class='btn btn-default btn-lg' style=''>Create new content</a>");

						}

						// Clear all currently selected, since they have all now
						// been deleted
						SELECTIONS = new Array();

						// just make CURRENT_SELECTION = undefined
						CURRENT_SELECTION = undefined;
						// select(FILE_KEYS[0]);

					});

}

function validate_imageBlobKey(blobKey) {

	$.ajax(
			{
				url : apiRoutes["MEDIA_VALIDATE_API"] + "?blobKey=" + blobKey
						+ "&t=image",
				type : "GET",
				processData : false
			}).done(function(data) {

		if (data === "true") {
			update_imageBlobKey(blobKey);
		} else {
			$('#imageKey_msg').html('Enter a valid Image Key');
			$('#imageKey_msg').css('visibility', 'visible');
			return;
		}
	});

}

function update_imageBlobKey(imageKey) {

	$.ajax(
			{
				url : apiRoutes["BLOG_IMAGE_API"] + "?blogId="
						+ CURRENT_SELECTION + "&imageBlobKey=" + imageKey,
				type : "POST",
				processData : true
			}).done(function() {
		// Hide Modal
		$('#update_imageKey_modal').modal('hide');

	});

}
