
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
                                    
                                        $(".ui .form .button").addClass("disabled");
					
					var returnUrl = getCookieOnce("returnurl");
					
					loginByEmail(
							$('input[name="login_identifier"]').val(), 
							$('input[name="login_password"]').val(),
							$('input[name="login_remember"]').parent().checkbox('is checked'),
							returnUrl
					).catch((error)=>{
                                            
                                                        $(".ui .form .button").removeClass("disabled");
                                    
                                                        console.log(error.statusText);
                                    
						        // @DEV 
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
  
  