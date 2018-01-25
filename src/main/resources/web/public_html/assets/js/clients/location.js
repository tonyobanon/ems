

function getCountryDialingCode(countryCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/country-dialing-code?countryCode=" + countryCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getLocales(countryCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/localeList?countryCode=" + countryCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCountries() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/countryList"
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getTimezones() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/timezoneList"
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getTerritories(ctx) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/territoryList?ctx=" + ctx
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCities(ctx) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/cityList?ctx=" + ctx
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCityCoordinates(cityCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/cityCoordinates?cityCode=" + cityCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCityTimezone(cityCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/cityTimezone?cityCode=" + cityCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCurrencies() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/currencyList"
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getSpokenLanguages(countryCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/countryLanguages?countryCode=" + countryCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCurrencyName(countryCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/currencyName?countryCode=" + countryCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}


function getCurrencyCode(countryCode) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: "GET",
            async: true,
            processData: false,
            statusCode: {302: function (jqXHR, status, error) {
                    window.location = jqXHR.getResponseHeader("X-Location");
                }},
            url: "/api/locations/service/currencyCode?countryCode=" + countryCode
        }).done(function (o) {
            resolve(o);
        }).fail(function (jqXHR, status, error) {
            if (jqXHR.getResponseHeader("X-Location") === null && jqXHR.status !== 302) {
                reject(jqXHR);
            }
        });
    });
}
