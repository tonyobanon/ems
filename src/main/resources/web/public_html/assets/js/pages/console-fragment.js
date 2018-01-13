
const CE_SESSION_TOKEN_COOKIE = 'X-Session-Token';
const CE_DEFAULT_LOCALE_COOKIE = 'DEFAULT_LOCALE';

const BLOBSTORE_URL = "/api/blobstore/get?blobId=";

var ACTIVITY_STREAM_CURRENT_DATE_TO = undefined;
var ACTIVITY_STREAM_LISTING_CONTEXT = null;

var ACTIVITY_STREAM_RECURSION_COUNT = 0;
var ACTIVITY_STREAM_RECURSION_MAX = 10;

'use strict';

$(document).ready(function () {
    ce_initialize_page();
});

function ce_initialize_page() {


    Promise.all([

        //Add supported languages
        ce_add_supported_countries(),

        //Consolidate role functionalities
        ce_consolidate_role_functionalities(),

        // Get searchable entities
        ce_get_searchable_list(),

        //Add user information
        ce_add_user_information()

    ]).then(() => {

        //Load activity stream
        ce_load_activity_stream()

                .then(() => {
                    //Translate Page
                    ce_translate_page();
                })

                .then(() => {
                    //Initialize UI components
                    initComponents();
                })

                .then(() => {
                    
                    //Complete initialization
                    $(window).trigger('ce-load-page-start');
                    
                });

    }, (err) => {

        //Error occured
        console.log(err);
    });
}

function ce_add_supported_countries() {

    return new Promise(function (resolve, reject) {

        getAvailableCountries().then(data => {

            var menu = $('#available_countries_menu');

            for (var locale in data) {
                var country = data[locale];
                menu.append('<a data-locale="' + locale + '" class="item"><i class="' + country["name"].toLowerCase() + ' flag"></i>' + country["name"] + '</a>');
            }

            menu.find('.item').on('click', function () {
                setCookie(CE_DEFAULT_LOCALE_COOKIE, $(this).attr('data-locale'), true);
                var currentLocation = window.location;
                window.location = currentLocation;
            });

            resolve();

        }, () => {
            reject("Error occured while fetching available locales");
        });
    });
}

function ce_consolidate_role_functionalities() {

    var adminFunctionalities;
    var roleFunctionalities;
    var unauthorizedFunctionalities = [];

    return new Promise(function (resolve, reject) {

        getRealmFunctionalities(1)

                .then((data) => {
                    adminFunctionalities = data;
                    return getOwnRole();
                })

                .then(role => {
                    return getRoleFunctionalities(role['role']);
                })

                .then((data) => {

                    roleFunctionalities = data;

                    for (var i in adminFunctionalities) {
                        i = parseInt(i);
                        if (roleFunctionalities.indexOf(i) === -1) {
                            unauthorizedFunctionalities.push(i);
                        }
                    }

                    /**
                     * Remove all page components that the current user does not
                     * have permissions to
                     */

                    for (var i in unauthorizedFunctionalities) {
                        var f = unauthorizedFunctionalities[i];
                        $('.ce-f-' + f).remove();
                    }

                    // Get all available routes
                    return getRouteFunctionalities();

                })

                .then(routeMappings => {

                    /**
                     * Remove all a html tags that point to url that the users do not have
                     * access to.
                     */

                    for (var uri in routeMappings) {

                        var isAllowed = true;

                        // get minimun functionalities required to access this uri
                        var functionalities = routeMappings[uri]['min'];

                        // verify that none of the functionalies exists in
                        // <unauthorizedFunctionalities>

                        for (var i in functionalities) {
                            if (unauthorizedFunctionalities.indexOf(functionalities[i]) !== -1) {
                                isAllowed = false;
                                break;
                            }
                        }

                        if (!isAllowed) {
                            // Get <a> elements that link to uri, and detach from the DOM
                            $("a[href^='" + uri + "']").remove();
                        }
                    }

                    // Consolidate all removed links, in the sidebar

                    $(".ce-fc-s-1").each(function (a, b) {
                        var c = $(b);
                        if (c.next().children().length === 0) {
                            $(this).next().remove();
                            $(this).remove();
                        }
                    });

                    $(".ce-fc-3c-3").each(function (a, b) {
                        var c = $(b);
                        if (c.find(":nth-child(3)").children().length < 3) {
                            $(this).remove();
                        }
                    });

                    resolve();

                });
    });
}

function ce_get_searchable_list() {

    return new Promise(function (resolve, reject) {

        getSearchableList().then(data => {

            var menu = $('#search_menu');

            for (var k in data) {
                var v = data[k];
                menu.append('<div class="item" data-url="' + v['listingPageUrl'] + '"><img class="ui avatar image" src="' + (Object.keys(v).indexOf("icon") !== -1 ? v['icon'] : '/assets/images/no-image.png') + '" alt="label-image" />' + get_ce_translate_node(v['name']) + '</div>');
            }

            menu.find('.item').on('click', function () {
                window.location = $(this).attr('data-url') + "?query=" + $('#search_input').val();
            });

            resolve();

        }), () => {
            reject("Error occured while getting searchable entites");
        };
    });
}

function ce_add_user_information() {

    return new Promise(function (resolve, reject) {

        // Signout
        $('#signout_button').on('click', function () {
            removeCookie(CE_SESSION_TOKEN_COOKIE);
            window.location = "/";
        });

        getOwnAvatar().then(data => {
            if (data['image'] !== null) {
                $('#user_avatar').attr('src', BLOBSTORE_URL + data['image']);
            }
        }).then(() => {
            return getPersonName();
        })
       .then(data => {
            $('#user_name').html(data['name']);
            resolve();
        });

    });
}

function ce_load_activity_stream() {

    return new Promise(function (resolve, reject) {

        if ($('.ce-f-340').length === 0) {
            //Activity stream is not allowed for current user
            resolve();
            return;
        }

        isActivityStreamEnabled().then(data => {
            $("input[name='activity_stream_enabled']").prop('checked', data['isEnabled'] === true);
        });

        $("input[name='activity_stream_enabled']").on('change', function () {
            var checked = $(this).prop('checked');
            switch (checked) {
                case true:
                    enableActivityStream();
                    break;
                case false:
                    disableActivityStream();
                    break;
            }
        });

        $("input[name='activity_stream_timeline']").on('change', function () {
            setActivityStreamTimeline($(this).prop('value'));
            ce_activity_stream_refresh();
        });

//        This was added directly in the HTML instead. Todo: Fix
//        $('#activity_stream_next_button').one('click', function () {
//            ce_activity_stream_next();
//        });

        getActivityStreamTimeline().then(data => {
            $("input[name='activity_stream_timeline'][value=" + data['timeline'] + "]").prop('checked', true);
            ce_activity_stream_refresh();
        });

        resolve();
    });
}

function ce_activity_stream_refresh() {

    //set current "to" date
    ACTIVITY_STREAM_CURRENT_DATE_TO = moment();

    //remove activity stream event divs from DOM
    $('#activity_stream_events > .event').remove();

    ACTIVITY_STREAM_LISTING_CONTEXT = null;
    ACTIVITY_STREAM_RECURSION_COUNT = 0;

    ce_activity_stream_next();
}

function ce_next_activity_stream_timeline() {

    var currentTimeline = parseInt($("input[name='activity_stream_timeline']:checked").attr('value'));

    var toDate = ACTIVITY_STREAM_CURRENT_DATE_TO;
    var fromDate;

    switch (currentTimeline) {
        case 1:
            fromDate = moment(toDate).hour(toDate.hour() - 1);
            break;
        case 2:
            fromDate = moment(toDate).day(toDate.day() - 1);
            break;
        case 3:
            fromDate = moment(toDate).week(toDate.week() - 1);
            break;
    }

    ACTIVITY_STREAM_CURRENT_DATE_TO = fromDate;

    return {
        "from": fromDate,
        "to": toDate
    };
}

function ce_stringify_activity_stream_timeline() {
    var currentTimeline = parseInt($("input[name='activity_stream_timeline']:checked").attr('value'));
    switch (currentTimeline) {
        case 1:
            return "hours";
        case 2:
            return "days";
        case 3:
            return "weeks";
    }
}

function ce_activity_stream_next() {

    if (ACTIVITY_STREAM_RECURSION_COUNT > ACTIVITY_STREAM_RECURSION_MAX) {
        //@Todo: Use a Message Toast, instead, Translate
        console.log("No activities were recorded in the past " + ACTIVITY_STREAM_RECURSION_MAX + " " + ce_stringify_activity_stream_timeline());

        var refreshButtonContainer = $('#activity_stream_next_container');
        refreshButtonContainer.remove();
        return;
    }

    return Promise.resolve(
            new Promise(function (resolve, reject) {

                //check if a current listing context exists.
                if (ACTIVITY_STREAM_LISTING_CONTEXT === null) {
                    resolve(false);
                    return;
                }

                //Verify that the current listing context contains more results
                return hasListingCursor(2, ACTIVITY_STREAM_LISTING_CONTEXT).then(data => {
                    return resolve(data);
                });
            }))

            .then((hasCurrentContext) => {

                switch (hasCurrentContext) {

                    case true:

                        console.log("Using existing activity timeline listing context");

                        return {
                            'contextKey': ACTIVITY_STREAM_LISTING_CONTEXT
                        };

                    case false:

                        var nextTimeline = ce_next_activity_stream_timeline();

                        console.log("Creating new listing context, for activity timeline: " + nextTimeline["from"].fromNow() + "  to  " + nextTimeline["to"].fromNow());

                        return newListContext(4, {

                            "date >": nextTimeline["from"].format('YYYY-MM-DDTHH:mm:ss.SSS'),
                            "date <=": nextTimeline["to"].format('YYYY-MM-DDTHH:mm:ss.SSS')

                        }, 20, "-date");
                }
            })

            .then(data => {

                var contextKey = data['contextKey'];

                return hasListingCursor(2, contextKey).then((hasNext) => {

                    if (!hasNext) {

                        //The current timeline does not have any activity stream
                        console.log("Timeline does not have any activity stream. Continuing to next timeline");

                        ACTIVITY_STREAM_RECURSION_COUNT++;
                        return ce_activity_stream_next();
                    }

                    //Now, we know that it has more results
                    if (!ACTIVITY_STREAM_LISTING_CONTEXT) {
                        ACTIVITY_STREAM_LISTING_CONTEXT = contextKey;
                    }


                    var refreshButtonContainer = $('#activity_stream_next_container');
                    refreshButtonContainer.remove();

                    var container = $('#activity_stream_events');

                    nextListingResults(2, contextKey).then((results) => {

                        for (var k in results) {

                            var v = results[k];

                            var now = moment(v['date']);

                            var date = now.format('ddd, h:mm A') + " (" + now.fromNow() + ")";
                            var html = v['html'];
                            var image = (Object.keys(v).indexOf("image") !== -1 ? BLOBSTORE_URL + v['image'] : '/assets/images/no-image.png');

                            var div = '<div class="event"><div class="label"><img src="' + image + '" alt="label-image"></div><div class="content"><div class="summary">' + html + '.<div class="date">' + date + '</div></div></div></div>';

                            container.append(div);
                        }

                        container.append(refreshButtonContainer);

                        return ce_translate_page();
                    });
                });

            });
}

function ce_translate_page() {

    return new Promise(function (resolve, reject) {
        var elements = new Map();
        var keys = {};

        $(".ce-translate").each(function (index) {
            elements.set(index, this);
            keys[index] = $(this).text().trim();
        });

        getRbEntry(keys).then(data => {
            for (var k in data) {
                var v = data[k];
                $(elements.get(parseInt(k))).replaceWith(v);
            }
            resolve();
        }, () => {
            reject("Error while translating page");
        });
    });
}


function getRouteFunctionalities() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            url: "/route_functionalities.json"
        }).done(function (o) {
            resolve(o);
        });
    });
}

function get_ce_translate_node(text) {
    return '<span class="ce-translate">' +  text + '</span>'; 
}


/**    UI Initialization Stuff    */


var sideBarIsHide = false;
var ManuelSideBarIsHide = false;
var ManuelSideBarIsState = false;
$(".openbtn").on("click", function () {
    ManuelSideBarIsHide = true;
    if (!ManuelSideBarIsState) {
        resizeSidebar("1");
        ManuelSideBarIsState = true;
    } else {
        resizeSidebar("0");
        ManuelSideBarIsState = false;
    }
});


$(window).resize(function () {
    if (ManuelSideBarIsHide === false) {
        if ($(window).width() <= 767) {
            if (!sideBarIsHide)
                ;
            {
                resizeSidebar("1");
                sideBarIsHide = true;
                $(".colhidden").addClass("displaynone");

            }
        } else {
            if (sideBarIsHide)
                ;
            {
                resizeSidebar("0");
                sideBarIsHide = false;

                $(".colhidden").removeClass("displaynone");

            }
        }
    }
});
var isMobile = window.matchMedia("only screen and (max-width: 768px)");

if (isMobile.matches) {
    resizeSidebar("1");
    $(".computer.only").toggleClass("displaynone");
    $(".colhidden").toggleClass("displaynone");
} else {

}

function resizeSidebar(op) {

    if (op === "1") {

        $(".ui.sidebar.left").addClass("very thin icon");
        $(".navslide").addClass("marginlefting");
        $(".sidebar.left span").addClass("displaynone");
        $(".sidebar .accordion").addClass("displaynone");
        $(".ui.dropdown.item.displaynone").addClass("displayblock");
        $($(".logo img")[0]).addClass("displaynone");
        $($(".logo img")[1]).removeClass("displaynone");
        $(".hiddenCollapse").addClass("displaynone");


    } else {

        $(".ui.sidebar.left").removeClass("very thin icon");
        $(".navslide").removeClass("marginlefting");
        $(".sidebar.left span").removeClass("displaynone");
        $(".sidebar .accordion").removeClass("displaynone");
        $(".ui.dropdown.item.displaynone").removeClass("displayblock");
        $($(".logo img")[1]).addClass("displaynone");
        $($(".logo img")[0]).removeClass("displaynone");
        $(".hiddenCollapse").removeClass("displaynone");
    }
}


function toggleFullScreen(elem) {
    // ## The below if statement seems to work better ## if
    // ((document.fullScreenElement && document.fullScreenElement !== null) ||
    // (document.msfullscreenElement && document.msfullscreenElement !== null)
    // || (!document.mozFullScreen && !document.webkitIsFullScreen)) {
    if ((document.fullScreenElement !== undefined && document.fullScreenElement === null) || (document.msFullscreenElement !== undefined && document.msFullscreenElement === null) || (document.mozFullScreen !== undefined && !document.mozFullScreen) || (document.webkitIsFullScreen !== undefined && !document.webkitIsFullScreen)) {
        if (elem.requestFullScreen) {
            elem.requestFullScreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullScreen) {
            elem.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
    } else {
        if (document.cancelFullScreen) {
            document.cancelFullScreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
    }
}


function initComponents() {
// using context
    $('.ui.right.sidebar')
            .sidebar({
                context: $('#contextWrap .pusher'),
                transition: 'slide out',
                silent: true
            })
            .sidebar('attach events', '.rightsidebar');


    $(".ui.dropdown").dropdown({
        allowCategorySelection: true,
        transition: "fade up"
    });

    $('.ui.accordion').accordion({
        selector: {}
    });

    $(document).ready(function () {
        $('.tabular .item').tab();
    });

// $(".refreshing").on("click", function() {
// $(".dimmer").addClass("active").delay(1500).queue(function() {
// $(".dimmer").removeClass("active").dequeue();
// });
// });
// dashboard page loading segment trigger

// Pace Loading (On Navbar) Function
// function paceLoading() {
// var paceOptions = {
// restartOnRequestAfter: false
// };
// $(document).ajaxStart(function() {
// Pace.restart();
// });
// }
// Pace Loading (On Navbar) Function


    $('#total_users').progress({
        duration: 200,
        total: 200,
        percent: 45
    });
    $('#overall_student_performance').progress({
        duration: 200,
        total: 200,
        percent: 68
    });

    $('.counter').counterUp({
        delay: 60,
        time: 1000
    });
}