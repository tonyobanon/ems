IS_USER_FRAGMENT=!0;function initPage(){if(Object.keys(window).indexOf("CURRENT_USER_ID")!==-1){$(window).trigger('ce-load-page-end');init_student_profile().then(()=>{$('#user-stats-menu').parent().show();$('#user-role-custom-area').show()})}}
function init_student_profile(){return Promise.all([getStudentSemesterPerformance(CURRENT_USER_ID).then((data)=>{if(Object.keys(data).length===0){$('#pr-chart').html('<h2 class="ui header" style="color: rgba(0,0,0,.4);"> No report available for this student. </h2>')}else{var labels=[];var maxSeries=[];var minSeries=[];for(var k in data){var v=data[k];labels.push(k);minSeries.push(v.min);maxSeries.push(v.max)}
new Chartist.Line('#pr-chart',{labels:labels,series:[minSeries,maxSeries]},{low:0,showArea:!0,width:700,height:300})}
return Promise.resolve()}),getStudentSemesterCourses(CURRENT_USER_ID).then((data)=>{if(Object.keys(data).length===0){$('#semester-courses').html('<h2 class="ui header" style="color: rgba(0,0,0,.4);"> No courses available. </h2>')}else{var parent=$('#semester-courses');for(var k in data){var v=data[k];var item=$('<div class="item" data-course-code="'+v.code+'"><div class="right floated content">'+v.point+' '+get_ce_translate_node('points')+'</div><div class="content">'+v.name+' ('+v.code+')'+'</div></div>');parent.append(item)}
parent.find('> .item').on('click',function(){})}
return Promise.resolve()})])}