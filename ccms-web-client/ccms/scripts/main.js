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
		var navwidth = $(document).width()-385-$(".headerAside").width();
		var showlistnum = parseInt(navwidth/$(".nav>li").width());
		var hidelistnum = 10-showlistnum ;
		if (hidelistnum > 0 ) {
			$(".nav>li").slice(-hidelistnum).hide();
			$(".more ul li").slice(-hidelistnum).show();
			$(".more").show();
			$(".nav>li").slice(0,showlistnum).show();
			$(".more ul li").slice(0,showlistnum).hide();
		}else if (hidelistnum < 0) {
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
		$(this).find(".combobox").stop(true,true).slideUp();
	});

	/*
	$(".nav li a").click(function(){
		$(this).addClass('navCur').parent("li").siblings("li").find("a").removeClass('navCur');
	})
	*/

	$(".headerAside span").hover(function(){
		$(this).addClass("headerAsideHover");
	},function(){
		$(this).removeClass("headerAsideHover");  
	})
	$(".newmailX").click(function(){
		$(".newmail").remove();
	})

	//左边导航

	$(".sideNav>li>a").live('click',function(){
		$(this).addClass("sbCurrent").parent().siblings("li").find("a").removeClass("sbCurrent");
	})

	$(".sideNav>li>a").live('mouseover',function(){
		$(this).stop().animate({"padding-left":26});
	});
	$(".sideNav>li>a").live('mouseout',function(){
		$(this).stop().animate({"padding-left":19});
	});

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

});