//js


$(function(){

//表格奇偶变色
 $(".ccmsTableB tbody tr:odd").addClass("even");

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
    $(this).find(".combobox").stop(true,true).hide();
});

$(".nav li a").click(function(){
	$(this).addClass('navCur').parent("li").siblings("li").find("a").removeClass('navCur');
})

$(".headerAside span").hover(function(){
    $(this).addClass("headerAsideHover");
},function(){
    $(this).removeClass("headerAsideHover");  
})
$(".newmailX").click(function(){
    $(".newmail").remove();
})

//左边导航
$(".sideNavNosec").click(function(){
    $(this).addClass("sbCurrent").parent().siblings("li").find(".sbCurrent").removeClass("sbCurrent");
    $(".sideNav dd a").removeClass("sbSmallCur");
})

$(".sideNav>li>a").mouseover(function(){
    $(this).addClass('bold');
});
$(".sideNav>li>a").mouseout(function(){
    $(this).removeClass('bold');
});

$(".sideNavArrow").parent("a").toggle(function(){
    $(this).next().slideDown();
    $(this).addClass("sideNavGray");
},function(){
    $(this).next().slideUp();
    $(this).removeClass("sideNavGray");
});
$(".sideNav dd a").click(function(){
    $(".sideNav dd a").removeClass("sbSmallCur");
    $(this).addClass('sbSmallCur');
    $(this).parents("dl").siblings("a").addClass("sbCurrent").parent().siblings("li").find(".sbCurrent").removeClass("sbCurrent");
})



// $(".sideNav>li>a").click(function(){
//     $(this).next().slideDown();
//     $(this).parent().siblings().find('dl').slideUp();
//     $(this).next().find('li:first').click();
// });   
// $(".sideNav dl dd a").click(function(){
//     $(this).addClass('sbSmallCur').parent().siblings().find('a').removeClass('sbSmallCur');
// });






})