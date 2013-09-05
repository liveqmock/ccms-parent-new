//js


$(function(){



//评价分析 收起展开功能
$(".screen .unfold a").toggle(function(){
        $(".screen .screenDiv").slideDown();
        $(this).text("收起");
        $(this).css("background-position","-444px -459px");
    },function(){
        $(".screen .screenDiv").slideUp();
        $(this).text("展开");
        $(this).css("background-position","37px -459px");
    });
//评价分析 时间插件
 $.datepicker.regional[ "zh-CN" ];
$( "#orderdateS" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#orderdateE" ).datepicker( "option", "minDate", selectedDate );
    }
});
$( "#orderdateE" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#orderdateS" ).datepicker( "option", "maxDate", selectedDate );
    }
});


$( "#evadateS" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#evadateE" ).datepicker( "option", "minDate", selectedDate );
    }
});
$( "#evadateE" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#evadateS" ).datepicker( "option", "maxDate", selectedDate );
    }
});


//营销活动 时间节点 datepicker
$("#timeNodeOneS").datepicker();
$( "#timeNodeCycleS" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#timeNodeCycleE" ).datepicker( "option", "minDate", selectedDate );
    }
});
$( "#timeNodeCycleE" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#timeNodeCycleS" ).datepicker( "option", "maxDate", selectedDate );
    }
});

//营销活动  listtab 切换
$(".listTab li").click(function(){
    $(this).addClass("listSelected").siblings().removeClass("listSelected");
})
$(".listTab li span").click(function(){
    $(this).parent().siblings("li:last").addClass("listSelected");
    $(this).parent("li").remove();
})



//客户筛选
$(".marketingCampNodeQuery h5 a").toggle(function(){
    $(this).parent().next().slideUp();
    $(this).text("+");
},function(){
    $(this).parent().next().slideDown();
    $(this).text("-");
});


$( "#nodeQueryOrderS" ).datepicker();
$( "#nodeQueryOrderE" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function( selectedDate ) {
        $( "#nodeQueryOrderS" ).datepicker( "option", "maxDate", selectedDate );
    }
});

//$( "#nodeQueryOrderS" ).datepicker();
//$('#nodeQueryOrderS').datetimepicker();

//时间节点
        // $('#timeNodeOne').click(function(){
        //     if (this.checked == true) {
        //         $('.timeNodeOne input,select').attr('disabled',false);
        //         $('.timeNodeCycle input,select').attr('disabled',true);
        //         $('.timeNodeCycle input').first().attr('disabled',false);
        //     }else{
        //         $('.timeNodeCycle input,select').attr('disabled',false);
        //         $('.timeNodeOne input,select').attr('disabled',true);
        //         $('.timeNodeOne input').first().attr('disabled',false);
        //     };
        // });
// $('#timeNodeCycle').click(function(){
//     if (this.checked == true) {
//         $('.timeNodeOne input,select').attr('disabled',true);
//         $('.timeNodeOne input').first().attr('disabled',false);
//     }else{
//         $('.timeNodeOne input,select').attr('disabled',false);
//     };
// });
//     还有一个问题：初始状态后任何一个子input被选该父元素checked 另一个table里的input disabled







})