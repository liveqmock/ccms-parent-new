

//表格奇偶行变色
//$(".main table tbody tr:even").addClass("even");
/*
$(".main table tbody tr").hover(function(){
	$(this).addClass("over");
},function(){
	$(this).removeClass("over");
})
*/

//活动监控表格切换
$(".moduleHeader li").live('click',function(){
    //var index = $(this).index();
    $(this).addClass("bold").siblings().removeClass("bold");
    //$(".campMonitor table").siblings().eq(index+1).show().siblings("table").hide();
})

//删除模块
$(".closeTable").click(function(){
	$(this).parent().parent().remove();
})

//addBlock
$(".addBlockHeader a").toggle(function(){
    $(this).parent().next().slideDown();
},function(){
    $(this).parent().next().slideUp();
});
