
var RETURN_URL;

function initPage() {

    if (Object.keys(window).indexOf("CE_ORIGIN_URL") !== -1) {
        RETURN_URL = CE_ORIGIN_URL;
    }

    var status = getCookieOnce("status");

    if (!status) {
        window.location = window.location + "?status=2";
        return;
    }

    init_application_list_table(status).then(() => {
        $(window).trigger('ce-load-page-end');
    });

}

function init_application_list_table(status) {

    return newListContext(1, -1, [{
            filters: {
                "status": status
            }
        }]).then((ctx) => {

        var contextKey = ctx['contextKey'];

        return nextListingResults(2, contextKey);

    }).then(data => {

        var table = $('#application-list-table');

        var thParent = table.find('> thead > tr');

        thParent.append($('<th></th>').html(get_ce_translate_node('Name')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Role')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Status')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Date_created')));
        thParent.append($('<th></th>').html(get_ce_translate_node('Date_updated')));

        var rows = table.find(' > tbody');

        for (var k in data) {

            var v = data[k];

            var row = $('<tr></tr>').attr('data-application-id', v['id']);

            row.append($('<td></td>').html(v['name']));
            row.append($('<td></td>').html(v['role']));
            row.append($('<td></td>').text(v['status']));

            row.append($('<td></td>').text(moment(v['dateCreated']).fromNow()));
            row.append($('<td></td>').text(moment(v['dateUpdated']).fromNow()));

            rows.append(row);
        }

        //Initialize Table
        var dtable = table.DataTable({
            "pagingType": "full_numbers_icon",
            order: [1, 'desc'],
            responsive: true
        });

        table.on('click', '> tbody > tr', function () {
            var applicationId = $(this).attr('data-application-id');
            if (RETURN_URL) {
                window.location = RETURN_URL + "?id=" + applicationId;
            } else {
                //Do nothing at this point
            }
        });

    });

}