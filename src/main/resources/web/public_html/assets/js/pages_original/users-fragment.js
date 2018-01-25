
var IS_USER_FRAGMENT = false;
var ENABLE_USER_PROFILE_SUGGESTIONS = true;

var CURRENT_USER_ID;

var USER_ACTIVITY_STREAM_CURRENT_DATE_TO = undefined;
var USER_ACTIVITY_STREAM_LISTING_CONTEXT = null;

var USER_ACTIVITY_STREAM_RECURSION_COUNT = 0;
var USER_ACTIVITY_STREAM_RECURSION_MAX = 1;

var RETURN_URL;

function initUserPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }
	
    var userId = getCookieOnce("id");

    CURRENT_USER_ID = userId;

    return Promise.all([

        //Load user profile
        load_user_profile()
//
//        //Load activity stream
//        ,load_user_activity_stream(),
//
//        //Load profile suggestions
//        load_profile_suggestions()

    ]).then(() => {

        var activityContainer = $('#user-activity-stream-events').parent();

        if (activityContainer.find('.event').length === 0) {
            activityContainer.html('<h2 class="ui header" style="color: rgba(0,0,0,.4);"> No activity recorded for this user </h2>');
        }

        activityContainer.show();

        //Load activity stream
        load_user_activity_stream();

        //Load profile suggestions
        load_profile_suggestions();

        if (!IS_USER_FRAGMENT) {
            $(window).trigger('ce-load-page-end');
        } else {
            //Allow the child page to trigger ce-load-page-end
        }

    });

}

function load_user_profile() {

    return getUserProfile(CURRENT_USER_ID).then(data => {

        if (data['image']) {
            $('#user_avatar').attr('src', BLOBSTORE_URL + data['image']);
        }

        $('#user_full_name').text(data['firstName'] + ' ' + data['middleName'] + ' ' + data['lastName'] + ' (' + data['role'] + ')');

        $('#user_contact').text('+' + (data['countryDialingCode'] + data['phone']) + (data['email'] ? (', ' + data['email']) : ''));
        $('#user_dob').text(moment(data['dateOfBirth']).format('DD/MM/YYYY'));
        $('#user_gender').text(data['gender'] === 1 ? "Female" : "Male");
        //@Todo: Use countryName here, please :)
        $('#user_address').text(data['address'] + ',' + data['cityName'] + ',' + data['territoryName']);

    });
}

function load_profile_suggestions() {

    var parent = $('#user_suggested_profile_items').html('');

    if (!ENABLE_USER_PROFILE_SUGGESTIONS) {
        return Promise.resolve();
    }

    return getSuggestedProfiles(CURRENT_USER_ID).then(data => {

        for (var k in data) {

            var v = data[k];

            //@Todo: data['roles'], data['dateCreated'] is not used

            var name = v['name'];
            var link = RETURN_URL + "?id=" + v['id'];

            var image = v['image'];
            var description = v['description'];

            var elem = '<div class="item"><div class="ui rounded tiny image"><img src="' + (image ? (BLOBSTORE_URL + image) : 'assets/images/no-avatar.png') + '" alt="label-image" /><i class="circle mini green icon avt" data-content="Online" data-variation="inverted greenli"></i></div><div class="content"><a class="header" href="' + link + '">' + name + '</a><div class="meta"><span class="cinema">' + description + '</span></div><div class="description"><p></p></div><div class="extra"><a class="ui tiny button follow" href="' + link + '">View</a></div></div></div>';

            parent.append($(elem));

        }

        $('#user_profile_suggestions').show();
    });

}


function load_user_activity_stream() {

//      This was added directly in the HTML instead. Todo: Fix
//    $('#user_activity_stream_next_button').on('click', function () {
//        user_activity_stream_next();
//    });

    return user_activity_stream_refresh();
}

function user_activity_stream_refresh() {

    //set current "to" date
    USER_ACTIVITY_STREAM_CURRENT_DATE_TO = moment();

    //remove activity stream event divs from DOM
    $('#user-activity-stream-events > .event').remove();

    USER_ACTIVITY_STREAM_LISTING_CONTEXT = null;
    USER_ACTIVITY_STREAM_RECURSION_COUNT = 0;

    return user_activity_stream_next();
}

function next_user_activity_stream_timeline() {

    var toDate = USER_ACTIVITY_STREAM_CURRENT_DATE_TO;
    var fromDate = moment(toDate).month(toDate.month() - 1);

    USER_ACTIVITY_STREAM_CURRENT_DATE_TO = fromDate;

    return {
        "from": fromDate,
        "to": toDate
    };
}

function stringify_user_activity_stream_timeline() {
    return "weeks";
}

function user_activity_stream_next() {

    $('#user_activity_stream_next_container').hide();

    if (USER_ACTIVITY_STREAM_RECURSION_COUNT > USER_ACTIVITY_STREAM_RECURSION_MAX) {
        //@Todo: Use a Message Toast, instead, Translate
        console.log("No activities were recorded in the past " + USER_ACTIVITY_STREAM_RECURSION_MAX + " " + stringify_user_activity_stream_timeline());

        $('#user_activity_stream_next_container').remove();
        return;
    }

    return Promise.resolve(
            new Promise(function (resolve, reject) {

                //check if a current listing context exists.
                if (USER_ACTIVITY_STREAM_LISTING_CONTEXT === null) {
                    resolve(false);
                    return;
                }

                //Verify that the current listing context contains more results
                return hasListingCursor(2, USER_ACTIVITY_STREAM_LISTING_CONTEXT).then(data => {
                    return resolve(data);
                });
            }))

            .then((hasCurrentContext) => {

                switch (hasCurrentContext) {

                    case true:

                        console.log("Using existing activity timeline listing context");

                        return {
                            'contextKey': USER_ACTIVITY_STREAM_LISTING_CONTEXT
                        };

                    case false:

                        var nextTimeline = next_user_activity_stream_timeline();

                        console.log("Creating new listing context, for activity timeline: " + nextTimeline["from"].fromNow() + "  to  " + nextTimeline["to"].fromNow());

                        return newListContext(4, 20, [{
                                filters: {
                                    "subject": CURRENT_USER_ID + "",
                                    "date >": nextTimeline["from"].format('YYYY-MM-DDTHH:mm:ss.SSS'),
                                    "date <=": nextTimeline["to"].format('YYYY-MM-DDTHH:mm:ss.SSS')
                                }
                            },
                            {
                                filters: {
                                    "person": CURRENT_USER_ID + "",
                                    "date >": nextTimeline["from"].format('YYYY-MM-DDTHH:mm:ss.SSS'),
                                    "date <=": nextTimeline["to"].format('YYYY-MM-DDTHH:mm:ss.SSS')
                                }
                            }], "-date");
                }
            })

            .then(data => {

                var contextKey = data['contextKey'];

                return hasListingCursor(2, contextKey).then((hasNext) => {

                    if (!hasNext) {

                        //The current timeline does not have any activity stream
                        console.log("Timeline does not have any activity stream. Continuing to next timeline");

                        USER_ACTIVITY_STREAM_RECURSION_COUNT++;
                        return user_activity_stream_next();
                    }

                    //Now, we know that it has more results
                    USER_ACTIVITY_STREAM_LISTING_CONTEXT = contextKey;


                    var container = $('#user-activity-stream-events');

                    return nextListingResults(2, contextKey).then((results) => {

                        for (var k in results) {

                            var v = results[k];

                            var now = moment(v['date']);

                            var date = now.format('ddd, h:mm A') + " (" + now.fromNow() + ")";
                            var html = v['html'];
                            var likes = v['likes'];

                            var image = (Object.keys(v).indexOf("personImage") !== -1 ? BLOBSTORE_URL + v['personImage'] : ((Object.keys(v).indexOf("subjectImage") !== -1 ? BLOBSTORE_URL + v['subjectImage'] : '/assets/images/no-image.png')));

                            var div = '<div class="event"><div class="label"><img src="' + image + '" alt="label-image"></div><div class="content"><div class="summary">' + html + '.<div class="date">' + date + '</div></div><div class="meta"><a class="like"><i class="like icon"></i> ' + likes + ' ' + get_ce_translate_node('Likes') + '</a></div></div></div>';

                            container.append(div);
                        }

                        $('#user_activity_stream_next_container').show();

                        if (IS_CONSOLE_FRAGMENT_LOADED) {
                            return ce_translate_page();
                        }

                    });
                });

            });

}


