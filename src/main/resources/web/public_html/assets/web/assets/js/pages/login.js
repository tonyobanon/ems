


  function load() {

	$('input[name="login_remember"]').parent().checkbox();
	  
	$('.ui .form').form(
			{
				fields : {
					login_identifier : {
						identifier : 'login_identifier',
						rules : [ {
							type : 'email',
							prompt : 'Please enter your email'
						} ]
					},
					login_password : {
						identifier : 'login_password',
						rules : [ {
							type : 'empty',
							prompt : 'Please enter your password'
						} ]
					}
				},

				inline : true,
				on : 'blur',
				keyboardShortcuts : false,
				onSuccess : function() {
					
					var returnUrl = getCookie("returnUrl");
					removeCookie("returnUrl");
					
					loginByEmail(
							$('input[name="login_identifier"]').val(), 
							$('input[name="login_password"]').val(),
							$('input[name="login_remember"]').parent().checkbox('is checked'),
							returnUrl
					).catch((error)=>{
						    //@DEV 
							//Provide a suitable UI oriented message
							alert("Unable to login with the information provided");
					});
					
				},
				onFailure : function() {

				}
			});

	$(".ui .form .button").on('click', function(e) {
		$('.ui .form').form('validate form');
	});

  }
  
  