
function showError(msg) {
	alert(msg);
}

function showSuccess(msg) {
	alert(msg);
}

function showSpinner() {
	 $('.ui.loader').parent().css('visibility', 'visible');
}

function hideSpinner() {
	 $('.ui.loader').parent().css('visibility', 'hidden');
}

function removeCookie(key) {
    var lastyear = new Date();
    lastyear.setFullYear(lastyear.getFullYear() - 1);
    document.cookie = key + "=" + "" + "; expires=" + lastyear.toGMTString();
}

function getCookie(key) {
    var allcookies = document.cookie;
    var pos = allcookies.indexOf(key);
    if (pos !== -1) {
        var len = key.length + 1;
        var start = pos + len;
        var end = allcookies.indexOf(";", start);
        if (end === -1
                )
            end = allcookies.length;
        var value = allcookies.substring(start, end);
        value = unescape(value);
        return value;
    } else {
        return null;
    }
}

function setCookie(key, value, rem) {
    if (rem) {
        var nextyear = new Date();
        nextyear.setFullYear(nextyear.getFullYear() + 1);
        document.cookie = key + "=" + value + "; expires=" + nextyear.toGMTString() + "; path=/"
                ;
    } else {
        document.cookie = key + "=" + value + "; path=/";
    }
}

function trimText(string, maxLength) {

	if (string.length > maxLength) {
		var trimmedText = "";
		// We split the text, then add '...'
		for (var ind = 0; ind < maxLength; ind++) {
			trimmedText = trimmedText + string.charAt(ind);
		}
		string = trimmedText + ' ...';
	}

	return string;
}
