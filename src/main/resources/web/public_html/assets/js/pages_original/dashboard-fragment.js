function initPage() {
	//$(window).trigger('ce-load-page-end');
}


$(document).ready(function () {
    
    var e = [
        [0, 57],
        [1, 58],
        [2, 93],
        [3, 11],
        [4, 40],
        [5, 93],
        [6, 29],
        [7, 19],
        [8, 87],
        [9, 14],
        [10, 130],
        [11, 91],
        [12, 80],
        [13, 49],
        [14, 59]
    ],
            t = [{
                    label: "Orders",
                    data: e,
                    color: "#17A88B",
                    lines: {
                        show: !0,
                        lineWidth: 2
                    },
                    curvedLines: {
                        apply: !0,
                        monotonicFit: !0
                    }
                }, {
                    data: e,
                    color: "#17A88B",
                    lines: {
                        show: !0,
                        lineWidth: 0
                    }
                }],
            o = {
                series: {
                    curvedLines: {
                        active: !0
                    },
                    shadowSize: 0
                },
                grid: {
                    hoverable: !0,
                    borderWidth: 0
                },
                xaxis: {
                    ticks: 0,
                    color: 3
                },
                yaxis: {
                    ticks: 0
                },
                tooltip: {
                    show: true
                },
                legend: {
                    show: !1
                }
            };
    $.plot($("#flot-order"), t, o), $("#flot-order").on("plothover", function (e, t, o) {
        o ? $(".flotTip").text("Orders: " + o.datapoint[1].toFixed(0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")).css({
            top: o.pageY + 15,
            left: o.pageX + 10
        }).show() : $(".flotTip").hide()
    });
    var i = [
        [0, 755],
        [1, 1133],
        [2, 1234],
        [3, 1448],
        [4, 1198],
        [5, 918],
        [6, 583],
        [7, 842],
        [8, 540],
        [9, 665],
        [10, 1195],
        [11, 742],
        [12, 1040],
        [13, 682],
        [14, 1190]
    ],
            a = [{
                    label: "Revenue",
                    data: i,
                    color: "#0667D6",
                    lines: {
                        show: !0,
                        lineWidth: 2
                    },
                    curvedLines: {
                        apply: !0,
                        monotonicFit: !0
                    }
                }, {
                    data: i,
                    color: "#0667D6",
                    lines: {
                        show: !0,
                        lineWidth: 0
                    }
                }],
            s = {
                series: {
                    curvedLines: {
                        active: !0
                    },
                    shadowSize: 0
                },
                grid: {
                    hoverable: !0,
                    borderWidth: 0
                },
                xaxis: {
                    ticks: 0
                },
                yaxis: {
                    ticks: 0
                },
                tooltip: {
                    show: !1
                },
                legend: {
                    show: !1
                }
            };
    $.plot($("#flot-avarage"), a, s), $("#flot-avarage").on("plothover", function (e, t, o) {
        o ? $(".flotTip").text("Avarage: %" + o.datapoint[1].toFixed(0).toString()).css({
            top: o.pageY + 15,
            left: o.pageX + 10
        }).show() : $(".flotTip").hide()
    });
    var l = [
        [0, 764],
        [1, 567],
        [2, 326],
        [3, 964],
        [4, 769],
        [5, 655],
        [6, 453],
        [7, 567],
        [8, 876],
        [9, 645],
        [10, 348],
        [11, 854],
        [12, 580],
        [13, 876],
        [14, 1190]
    ],
            n = [{
                    label: "User",
                    data: l,
                    color: "#8E23E0",
                    lines: {
                        show: !0,
                        lineWidth: 2
                    },
                    curvedLines: {
                        apply: !0,
                        monotonicFit: !0
                    }
                }, {
                    data: l,
                    color: "#8E23E0",
                    lines: {
                        show: !0,
                        lineWidth: 0
                    }
                }],
            r = {
                series: {
                    curvedLines: {
                        active: !0
                    },
                    shadowSize: 0
                },
                grid: {
                    hoverable: !0,
                    borderWidth: 0
                },
                xaxis: {
                    ticks: 0
                },
                yaxis: {
                    ticks: 0
                },
                tooltip: {
                    show: true
                },
                legend: {
                    show: !1
                }
            };
    $.plot($("#flot-saves"), n, r), $("#flot-saves").on("plothover", function (e, t, o) {
        o ? $(".flotTip").text("Saves: " + o.datapoint[1].toFixed(0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")).css({
            top: o.pageY + 15,
            left: o.pageX + 10
        }).show() : $(".flotTip").hide()
    });
    var d = [
        [0, 567],
        [1, 174],
        [2, 467],
        [3, 829],
        [4, 489],
        [5, 360],
        [6, 380],
        [7, 907],
        [8, 549],
        [9, 765],
        [10, 479],
        [11, 783],
        [12, 357],
        [13, 985],
        [14, 370]
    ],
            c = [{
                    label: "Feedback",
                    data: d,
                    color: "#E5343D",
                    lines: {
                        show: !0,
                        lineWidth: 2
                    },
                    curvedLines: {
                        apply: !0,
                        monotonicFit: !0
                    }
                }, {
                    data: d,
                    color: "#E5343D",
                    lines: {
                        show: !0,
                        lineWidth: 0
                    }
                }],
            h = {
                series: {
                    curvedLines: {
                        active: !0
                    },
                    shadowSize: 0
                },
                grid: {
                    hoverable: !0,
                    borderWidth: 0
                },
                xaxis: {
                    ticks: 0
                },
                yaxis: {
                    ticks: 0
                },
                tooltip: {
                    show: !1
                },
                legend: {
                    show: !1
                }
            };
    $.plot($("#flot-view"), c, h), $("#flot-view").on("plothover", function (e, t, o) {
        o ? $(".flotTip").text("Views: " + o.datapoint[1].toFixed(0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")).css({
            top: o.pageY + 15,
            left: o.pageX + 10
        }).show() : $(".flotTip").hide()
    });

});

//dashboard page loading segment trigger
$(".refreshing").on("click", function () {
    $(".dimmer").addClass("active").delay(1500).queue(function () {
        $(".dimmer").removeClass("active").dequeue();
    });
});
//dashboard page loading segment trigger

'use strict';

$(document).ready(function () {

    // Make some random data for the Chart
    var d1 = [];
    for (var i = 0; i <= 10; i += 1) {
        d1.push([i, parseInt(Math.random() * 30)]);
    }
    var d2 = [];
    for (var i = 0; i <= 25; i += 4) {
        d2.push([i, parseInt(Math.random() * 30)]);
    }
    var d3 = [];
    for (var i = 0; i <= 10; i += 1) {
        d3.push([i, parseInt(Math.random() * 30)]);
    }

    // Chart Options
    var options = {
        series: {
            shadowSize: 0,
            curvedLines: {
                apply: true,
                active: true,
                monotonicFit: true
            },
            lines: {
                show: false,
                lineWidth: 0
            }
        },
        grid: {
            borderWidth: 0,
            labelMargin: 10,
            hoverable: true,
            clickable: true,
            mouseActiveRadius: 6

        },
        xaxis: {
            tickDecimals: 0,
            ticks: false
        },

        yaxis: {
            tickDecimals: 0,
            ticks: false
        },

        legend: {
            show: false
        }
    };

    // Let's create the chart
    if ($("#chart-curved-line")[0]) {
        $.plot($("#chart-curved-line"), [{
                data: d1,
                lines: {
                    show: true,
                    fill: 0.98
                },
                label: 'Product 1',
                stack: true,
                color: '#52489C'
            }, {
                data: d3,
                lines: {
                    show: true,
                    fill: 0.98
                },
                label: 'Product 2',
                stack: true,
                color: '#59C3C3'
            }], options);
    }

    // Tooltips for Flot Charts
    if ($('.flot-chart')[0]) {
        $('.flot-chart').on('plothover', function (event, pos, item) {
            if (item) {
                var x = item.datapoint[0].toFixed(2),
                        y = item.datapoint[1].toFixed(2);
                $('.flot-tooltip').html(item.series.label + ' of ' + x + ' = ' + y).css({top: item.pageY + 5, left: item.pageX + 5}).show();
            } else {
                $('.flot-tooltip').hide();
            }
        });

        $('<div class="flot-tooltip"></div>').appendTo('body');
    }
    
    $(window).trigger('ce-load-page-end');  
    
});