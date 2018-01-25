
function initPage() {

    // Get available roles
    listRoles().then((roles) => {

        var rolesMenu = $('input[name="available_roles"] ~ .menu').html("");

        for (var i in roles) {
            rolesMenu.append("<div class=\"item\" data-value=\"" + i + "\">" + i + "</div>");
        }

        $('input[name="available_roles"]').parent().removeClass("disabled").dropdown();
    });



    $('.ui .form').form({
        fields: {
            available_roles: {
                identifier: 'available_roles',
                rules: [{
                        type: 'empty',
                        prompt: 'Please select a role'
                    }]
            }
        },

        inline: true,
        on: 'blur',
        keyboardShortcuts: false,
        onSuccess: function () {

            createApplication($('input[name="available_roles"]').val()).then(id => {
                window.location = "/update-application?id=" + id;
            });

        },
        onFailure: function () {

        }
    });

    $(".ui .form .button").on('click', function (e) {
        $('.ui .form').form('validate form');
        // e.stopPropagation();
    });

}

