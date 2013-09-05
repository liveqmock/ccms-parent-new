//js

function subFn(obj){
	var _next = $(obj).next();
	if(_next.children().length){	
		if(_next.is(':visible')){
			_next.slideUp();
			$(obj).removeClass("sideNavGray");
		}else{
			_next.slideDown();
			$(obj).addClass("sideNavGray");
		}
	}
}


$(function(){

	//平台下拉框
	$('.platform').toggle(function(){
		$(this).find("ul").slideDown();
	},function(){
		$(this).find("ul").slideUp();
	})
	$(".platform ul li").click(function(){
		var listtext = $(this).text();
		$('.platform span').text(listtext);
		$(".platform ul").slideUp();
	})

	//顶部导航栏
	function setNav(){
		var listnum = 8//导航总共的个数
	    var navwidth = $(document).width()-$("h1").outerWidth(true)-$(".headerAside").outerWidth(true)-$(".platform").outerWidth(true)-80;
		var showlistnum = parseInt(navwidth/$(".nav>li").width());
		var hidelistnum = listnum-showlistnum ;
		if (hidelistnum > 0 ) {
			$(".nav>li").slice(-hidelistnum).hide();
			$(".more ul li").slice(-hidelistnum).show();
			$(".more").show();
			$(".nav>li").slice(0,showlistnum).show();
			$(".more ul li").slice(0,showlistnum).hide();
		}else if (hidelistnum <= 0) {
			$(".nav>li").slice(0,showlistnum).show();
			$(".more ul li").slice(0,showlistnum).hide();
			$(".more").hide();
		};
	}
	setNav();
	$(window).resize(setNav);


	$(".comboboxParent").hover(function(){
		$(this).find(".combobox").stop(true,true).slideDown();
	},function(){
		$(this).find(".combobox").stop(true,true).hide();
	});

	/*
	$(".nav li a").click(function(){
		$(this).addClass('navCur').parent("li").siblings("li").find("a").removeClass('navCur');
	})
	*/

	$(".headerAside span b").hover(function(){
		$(this).addClass("headerAsideHover");
	},function(){
		$(this).removeClass("headerAsideHover");  
	})
	$(".newmailX").click(function(){
		$(".newmail").remove();
	})

	//左边导航
$(".sideNavNosec").live('click',function(){
    $(this).addClass("sbCurrent").parent().siblings("li").find(".sbCurrent").removeClass("sbCurrent");
    $(".sideNav dd a").removeClass("sbSmallCur");
})

$(".sideNav>li>a").mouseover(function(){
    $(this).addClass('bold');
});
$(".sideNav>li>a").mouseout(function(){
    $(this).removeClass('bold');
});
/*
if($(".sideNavArrow").is(":visible")){
	$(".sideNavArrow").parent("a").toggle(function(){
	    $(this).next().slideDown();
	    $(this).addClass("sideNavGray");
	},function(){
	    $(this).next().slideUp();
	    $(this).removeClass("sideNavGray");
	});
}
*/


$(".sideNav dd a").live('click',function(){
    $(".sideNav dd a").removeClass("sbSmallCur");
    $(this).addClass('sbSmallCur');
    $(this).parents("dl").siblings("a").addClass("sbCurrent").parent().siblings("li").find(".sbCurrent").removeClass("sbCurrent");
})

	/*
	放在顶部
	$(".sideNav .navTopItem").live('click',function(){
		var _next = $(this).next();
		if(_next.children().length){	
			_next.slideDown();
			$(this).parent().siblings().find('ul').slideUp();
			_next.find('li:first').click();
			return false;
		}
	});
	*/ 
	$(".sideNav ul a").live('click',function(){
		$(this).addClass('sbSmallCur').parent().siblings().find('a').removeClass('sbSmallCur');
	});

$('#shopinfo').live('mouseenter',function(){
	$(this).find("dl").stop(true,true).slideDown().siblings('span').addClass("spanHover");
}).live('mouseleave',function(){
	$(this).find("dl").stop(true,true).hide().siblings('span').removeClass("spanHover");
}).find('a').live('click',function(){
	$(this).parents('dl').hide().siblings('span').removeClass("spanHover");
});	
	
});