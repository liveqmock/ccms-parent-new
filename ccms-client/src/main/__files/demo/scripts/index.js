
$(function(){




//活动监控表格切换
$(".moduleHeader li").click(function(){
    var index = $(this).index();
    $(this).addClass("bold").siblings().removeClass("bold");
    $(".campMonitor table").siblings().eq(index+1).show().siblings("table").hide();
})

//删除模块
$(".module").hover(function(){
	$(this).find(".closeTable").show();
},function(){
	$(this).find(".closeTable").hide();
})
$(".closeTable").click(function(){
$(this).parent().parent().remove();
})

//addBlock
$(".addBlockHeader a").toggle(function(){
    $(this).parent().next().slideDown();
},function(){
    $(this).parent().next().slideUp();
});
$(".addBlockMain a").hover(function(){
	$(this).next().css("color","#0083ba");
},function(){
	$(this).next().css("color","#3d3d3d");
})

})

