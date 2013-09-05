(function($){
     $.fn.pElect=function(opts){
	     var $this=$(this);
		 var posi=getPosition($this);		 
		 $this.children().remove();
		 var $prevSelectionDiv=$('<div>',{
			     "class":"selectionBox",
				 "id":"prevSelection"
			 }).css({"width":posi.x,height:$this.innerHeight()}).appendTo($this);
		 var $selectionBtn=$('<div>',{
			     "class":"selectionBtn",
				 "id":"selectionBtn"
			 }).css({"top":posi.y}).appendTo($this);
		 var $nextSelectionDiv=$('<div>',{
			     "class":"selectionBox",
				 "id":"nextSelection"
			 }).css({"width":posi.x,height:$this.innerHeight()}).appendTo($this);
		 /*$(window).resize(function(){
			  var posi=getPosition($this);
			  $prevSelectionDiv.css("right",posi.x);
			  $nextSelectionDiv.css("left",posi.x);
			  $selectionBtn.css({left:posi.x-70,top:posi.y});
		 });*/
		 $selectionBtn.append($('<div><a href="javascript:void(0);"><img src="css/img/selected.png" /></a></div>'));
		 $prevSelectionDiv.append('<div class="mt"><input type="checkbox" id="prevAllchecked">全选</div>');
		 $nextSelectionDiv.append('<div class="mt"><input type="checkbox" id="nextAllchecked">全选&nbsp;&nbsp;<a href="javascript:void(0);" class="delProList">清楚</a></div><div class="productList"></div>');
		 $("#prevAllchecked").click(function(){
			  setAllCheckbox($prevSelectionDiv);					 
		 });
		 $("#nextAllchecked").click(function(){
			  setAllCheckbox($nextSelectionDiv);
		 });
		 $selectionBtn.find("a").click(function(){
		      createSelectedProductList($nextSelectionDiv);								
		 });
		 $("#nextSelection .delProList").click(function(){
				clearProductList();										 
		 });
		 $.fn.pSeartch=function(opts){
			 $prevSelectionDiv.find(".mt").siblings().remove();
		     var p=$.extend({
				  page:0,
				  rp:20,
				  ajaxType:"get"
				},opts);
			 if(p.ajaxType=="get"){
				  $.ajax({
					 url:"js/goal.json",
					 async:false,
					 type:"get",
					 cache:false,
					 dataType:"json",
					 data: opts,
					 success:function(response){
						 var dataList=response.list;
						 $prevSelectionDiv.append('<div class="productList"></div>');
						 createSeartchProductList($prevSelectionDiv,dataList);
						  var pageNum=Math.ceil((response.count)/(p.rp));
						  if(pageNum>1){
							 page.setPage($prevSelectionDiv); 
							 page.setPNum(p.page,pageNum,$prevSelectionDiv.find("#contexPage")); 
						  }
					 }		
				 }) 
			}else{
				
			}
			 
			 
		 }
		 //创建商品搜索列表
		 function createSeartchProductList(obj,data){
			 var len=data.length;
			 for(var i=0;i<len;i++){
				  var $div=$('<div class="items"></div>'); 
				  var html='<div class="pic">'
				         +'    <input type="checkbox" class="checkbox">'
						 +'    <img width="45" height="45" src='+data[i].imgurl+'>'
						 +' </div>'
						 +' <div class="info">'
						 +'    <p class="goodsName">'+data[i].goodname+'</p>'
						 +'    <p><span class="goodsId">商品ID：<span>'+data[i].goodno+'</span></span><span class="goodsPrice">￥:'+data[i].price+'</span></p>'
						 +' </div>';
				  $div.append(html);
				  obj.find(".productList").append($div); 				  
			 }
		     obj.find(".items").click(function(){
				 var $this=$(this);
			     $this.toggleClass("selected");	
				 if($this.hasClass("selected")){
				     $this.find(".checkbox").attr("checked",true);
				}else{
					 $this.find(".checkbox").attr("checked",false);
				}
			 });
			 
		 }
		 //创建商品选中列表
		 function createSelectedProductList(obj){
			  $prevSelectionDiv.find(".checkbox").each(function(){
				   var $this=$(this);
				   if($this.attr("checked")){
					   $this.attr("checked",false);
					   $this.parents(".items").removeClass("selected");
					   var thisId=$this.parents(".items").find(".goodsId span").text();
					   var thisTitle=$this.parents(".items").find(".goodsName").text();
					   var selectedId=$("#nextSelection .goodsId span").text();
					   var selectedTitle=$("#nextSelection .goodsName").text();
					   if(selectedId.indexOf(thisId)<0 && selectedTitle.indexOf(thisTitle)<0){
						     $this.parents(".items").clone().appendTo($nextSelectionDiv.find(".productList"));  
						}
				   }
				  
			 }); 
			  $nextSelectionDiv.find(".items").click(function(){
						 var $this=$(this);
						 $this.toggleClass("selected");	
						 if($this.hasClass("selected")){
							 $this.find(".checkbox").attr("checked",true);
						}else{
							 $this.find(".checkbox").attr("checked",false);
						}
				  });
		 }
		 //checkbox 全选
		 function setAllCheckbox(obj){
			  var checked=obj.find(".mt input").attr("checked");
		      if(obj.find(".items").length>0){
				   if(checked){
					  obj.find(".items").each(function(){
						  $(this).find(".checkbox").attr("checked",true).end().addClass("selected");									  
				      });
				   }else{
					   obj.find(".items").each(function(){
						  $(this).find(".checkbox").attr("checked",false).end().removeClass("selected");									  
				       });
				   }
				   
			  }		
		 }
		 //清楚
		 function clearProductList(){
			  if($("#nextSelection").find(".selected").length>0){
				   $("#nextSelection").find(".selected").each(function(){
					    $(this).remove();												   
				   });
			  }
		  }
		 //分页
		 var page={
            setPage:function(obj){
				obj.append('<div class="page"><a id="prev" href="javascript:void(0)">上一页</a><span id="contexPage"></span><a id="next" href="javascript:void(0)">下一页</a></div>');
				obj.find(".page").css({position:"absolute",bottom:8,right:6});
			},
			setPNum:function(a,b,obj){
						if(b<=5){
							   for(var i=1;i<=b;i++){
								   var $a=$("<a>",{
									   "text":i,
									   "href":"javascript:void(0)"
								   })
								   $a.appendTo(obj);
							   }
							}else{
							   if(a-4>=1 && a+2<b){   //中间区域
								   $('<a href="javascript:void(0);">1</a>').appendTo(obj);
								   $('<span>...</span>').appendTo(obj);
								   for(var i=a-2;i<a+4;i++){
									   var $a=$("<a>",{
										   "text":i,
										   "href":"javascript:void(0)"
									   })
									   $a.appendTo(obj);
								   }
								   $('<span>...</span>').appendTo(obj);
								   $('<a href="javascript:void(0);">'+b+'</a>').appendTo(obj);
						
							   }else if(a-4<1 && a+2<b){
								   for(var i=1;i<=a+3;i++){
									   var $a=$("<a>",{
										   "text":i,
										   "href":"javascript:void(0)"
									   })
									   $a.appendTo(obj);
								   }
								   $('<span>...</span>').appendTo(obj);
								   $('<a href="javascript:void(0);">'+b+'</a>').appendTo(obj);
							   }else if(a+5>b){
								   $('<a href="javascript:void(0);">1</a>').appendTo(obj);
								   $('<span>...</span>').appendTo(obj);
								   for(var i=a;i<=b;i++){
									   var $a=$("<a>",{
										   "text":i,
										   "href":"javascript:void(0)"
									   })
									   $a.appendTo(obj);
								   }
							   }
							}
							obj.find("a").each(function(){
							   var i=(a+1)+"";
							   if($(this).text()==i){
								   $(this).addClass("current");
							   }
							});
			       }
		 };
	 }	
	 function getPosition(obj,selectionBtn){
		 var posi={}
		 var h=obj.innerHeight();
		 var w=obj.innerWidth();
		 var x=parseInt(w/2)-37;
		 var y=parseInt(h/2);
         if(selectionBtn){
			  y=parseInt(h/2)-parseInt(selectionBtn.height());
		  }
		  posi.x=x;
		  posi.y=y;
		  return posi;
	 }
	 
})(jQuery)