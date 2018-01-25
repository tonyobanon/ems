IS_USER_FRAGMENT = true;
// ENABLE_USER_PROFILE_SUGGESTIONS = true;

function initPage() {

	$(window).trigger('ce-load-page-end');

	$('#user-stats-menu').parent().show();
	$('#user-role-custom-area').show();

}