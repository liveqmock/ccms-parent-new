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
            //营销活动 时间节点
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

})