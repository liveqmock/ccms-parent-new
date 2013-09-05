
//TIP层的显示隐藏
function showPreview(o){
	$(o).children("p").show();
}
function hidepreview(o){
	$(o).children("p").hide();
}


//所有店铺展示
function showAllShop(){
	$(".shoplist").show();
}
function hideAllShop(){
	$(".shoplist").hide();
}

//选择方案---预演弹框的显示
function startrehearsal(){
	$("#startRehearsal").show();
	$("#selectRehearsal").hide();
}

//方案左右移动
//arrowleftgray向左灰按钮 arrowrightgray向右灰按钮
//arrowleftbluea向左选中蓝按钮  arrowrightbluea向右选中蓝按钮
//arrowleftblueb向左正常蓝按钮 arrowrightblueb向右正常蓝按钮
function initArrow(arrow){
	var curarrow = $(arrow).parent();//当前点的按钮的父元素	
	var curarrowindex = $(arrow).index();//当前点的按钮是左还是右
	var curdiv = $(arrow).parent().parent();//当前点的按钮所在的DIV
	var curdivleft = curdiv.prev("div");//当前点的按钮所在的DIV的前一个DIV
	var curdivright = curdiv.next("div");//当前点的按钮所在的DIV的后一个DIV
	var curindex = $(arrow).parent().parent().index();//当前点的按钮所在的DIV是第几个
	/*alert(curdiv.html());alert(curindex);alert(curarrowindex);alert(curdivleft.html());alert(curdivright.html());*/
	if( curindex=="0" ){
		if( curarrowindex=="0" ){
			$(arrow).addClass("arrowleftgray").parent().parent().siblings().find(".arrowleftgray").removeClass("arrowleftgray");
		}else if( curarrowindex=="1" ){
			//当前点的按钮是右
			curdivright.after(curdiv);
			$(arrow).addClass("arrowrightbluea").parent().parent().siblings().find(".arrowrightbluea").removeClass("arrowrightbluea");//右按钮高亮
			$(arrow).siblings().removeClass("arrowleftgray").addClass("arrowleftblueb");//左按钮正常蓝
			$("#mobileSolution div:eq(0)").children().find(".arrowleftblueb").removeClass("arrowleftblueb").addClass("arrowleftgray");//第一个DIV的左按钮灰掉
			$("#mobileSolution div:eq(2)").children().find(".arrowrightblueb").removeClass("arrowrightblueb").addClass("arrowrightgray");//第一个DIV的左按钮灰掉
		}
	}else if( curindex=="1" ){
		if( curarrowindex=="0" ){
			//当前点的按钮是左
			curdivleft.before(curdiv);
			$(arrow).addClass("arrowleftgray").parent().parent().siblings().find(".arrowleftgray").removeClass("arrowleftgray").addClass("arrowleftblueb");//第二个DIV向左后左按钮灰掉
			$(arrow).siblings().addClass("arrowrightbluea");//右按钮高亮
		}else if( curarrowindex=="1" ){
			//当前点的按钮是右
			curdivright.after(curdiv);
			$(arrow).addClass("arrowrightgray").parent().parent().siblings().find(".arrowrightgray").removeClass("arrowrightgray").addClass("arrowrightblueb");//第二个DIV向左后左按钮灰掉
			$(arrow).siblings().addClass("arrowleftbluea");//右按钮高亮
		}
	}else if( curindex=="2" ){
		if( curarrowindex=="0" ){
			//当前点的按钮是左
			curdivleft.children().find(".arrowrightblueb").removeClass("arrowrightblueb").addClass("arrowrightgray");
			curdivleft.before(curdiv);
			$(arrow).addClass("arrowleftbluea").parent().parent().siblings().find(".arrowrightbluea").removeClass("arrowrightbluea").addClass("arrowrightblueb");//第二个DIV向左后左按钮灰掉
			$(arrow).siblings().addClass("arrowrightblueb").removeClass("arrowrightgray");//右按钮高亮
		}else if( curarrowindex=="1" ){
			$(arrow).addClass("arrowrightgray").parent().parent().siblings().find(".arrowrightgray").removeClass("arrowrightgray").addClass("arrowrightblueb");
		}
	}
}

//今日--累计
/*function todayCum(tc){
	var tcclass = $(tc).attr("class");
	if( tcclass =="todaycumulative" ){
		$(tc).addClass("curTodaycumulative").removeClass("todaycumulative").siblings().removeClass("curTodaycumulative");
	}else if( tcclass =="curTodaycumulative" ){
		$(tc).removeClass("curTodaycumulative").addClass("todaycumulative");
	}
}*/

//jquery
$(document).ready(function(){	
	//添加方案加号的高亮
	$(".add_deploy").click(function(){
		$(this).addClass("add_deployblue").siblings().removeClass("add_deployblue");
	})
	$(".deploymentlist").hover(function(){
		$(this).children("span").css("color","#0083ba");
		$(this).children(".add_deploy").addClass("add_deployblue");
	},function(){
		$(this).children("span").css("color","#165c88");
		$(this).children(".add_deployblue").removeClass("add_deployblue").addClass("add_deploy");
	})
	
	//默认备注签名输入框
	$(".fillinnotes").mouseover(function(){
		$(".icon_editgray").addClass("icon_editblue").removeClass("icon_editgray");
	})
	
	//方案预演点击效果
	$(".buttonrehearsalblue").click(function(){
		$(this).addClass("buttoncurrehearsal");
	})
	//常规蓝按钮点击效果
	$(".buttoncommblue").click(function(){
		$(this).addClass("curbuttoncommblue");
	})
	//常规灰按钮点击效果
	$(".buttoncommgray").click(function(){
		$(this).addClass("curbuttoncommgray");
	})
	
	//方案预演列表
	$(".smallsolution").click(function(){
		$(this).addClass("currenetsolution");
		//.parent("div").siblings().find(".currenetsolution").removeClass("currenetsolution");
		$(this).children("p").find(".successgreen").show();
		//.parents("div").siblings().children("p").find(".successgreen").hide();
	})
	
	//方案预演列表选中对应的表格数据改变
	$("#solutionlist div>div").click(function(){
		$(this).next("p").show().parent().siblings().children("p").hide();
		$(this).addClass("currenetsolution").parent().siblings().children(".currenetsolution").removeClass("currenetsolution");
	})
	
	
	
	
	//监控TAB切换[当前--历史]下个迭带
	$(".monitoringtab li").click(function(){
		$(this).addClass("cur").siblings().removeClass("cur");
		$(".monitoringcontent").eq($(".monitoringtab li").index(this)).show().siblings(".monitoringcontent").hide();
		if( $(".monitoringtab li").index(this)==0 ){
			$(".deploymenttipA").show();
			$(".monitoringcontent").css("padding-right","240px");
			$(".curMonitoring").css("padding-right","240px");
		}else if( $(".monitoringtab li").index(this)==1 ){
			$(".deploymenttipA").hide();
			$(".monitoringcontent").css("padding-right","0px");
			$(".curMonitoring").css("padding-right","0px");
		}
	})
	
	//今日--累计
	$(".todaycumulative").click(function(){
		$(this).attr("class","curTodaycumulative").siblings("input").attr("class","todaycumulative")
	})
	$(".curTodaycumulative").click(function(){
		$(this).attr("class","curTodaycumulative").siblings("input").attr("class","todaycumulative")
	})
	
	
	//查看明细筛选--取消
	$(".buttoncancle").click(function(){
		$(".tablediv .inputcomm").val("");
	})
	$(".inputcomm").live('blur',function(){
	  var txt_value = $(this).val();
	  if(txt_value == ""){
		  $(this).val(this.defaultValue);
		  $(this).css("color","#C5C5C5");
	  }
	});
	$(".inputcomm").live('focus',function(){
	  var txt_value = $(this).val();
	  if(txt_value == this.defaultValue){
		  $(this).val("");
		  $(this).css("color","#000");
	  }
	});
	
	//[开始时间][结束时间]
	  $( "#from" ).datepicker({
		  defaultDate: "+1w",
		  changeMonth: true,
		  numberOfMonths: 1,
		  onClose: function( selectedDate ) {
			  $( "#to" ).datepicker( "option", "minDate", selectedDate );
		  }
	  });
	  $( "#to" ).datepicker({
		  defaultDate: "+1w",
		  changeMonth: true,
		  numberOfMonths: 1,
		  onClose: function( selectedDate ) {
			  $( "#from" ).datepicker( "option", "maxDate", selectedDate );
		  }
	  });
	
	
})
//点空白处关闭预览的提不层
$(document).bind("click",function(e){
	var target  = $(e.target);
	if(target.closest("#showPreview").length == 0){
		$("#showPreview p").hide();
		$("#showPreview input").css("color","#999");
	}
})