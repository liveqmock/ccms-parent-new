$('.campaignListCon').flexigrid({
	url: 'campaign',
	//url: 'data/marketList.json',
	method : 'GET',
	dataType: 'json',
	colModel : [
		{display: '活动ID',name:'camp_id', width : 1, sortable : true ,dataindex:'campId'},
		{display: '活动名称', name:'camp_name', width : 3, sortable : true,dataindex:'campName'},
		{display: '创建人', name:'creater', width : 2, sortable : false, align:'center', dataindex:'creater',renderer:function(v){
			 if(v == null || v == "null"){
			     return "";
			 }else{
				 return v;
			 }
		}},
		{display: '创建时间', name:'created_time', width : 2, sortable : true,align:'center' ,dataindex:'createdTime'},
		{display: '活动状态', name:'camp_status', width : 2, sortable : true,align:'center' ,dataindex:'status',renderer:function(v){
			switch (v){
				case "": return "不限";
				break;
				case "A1": return "设计中";
				break;
				case "B1": return "测试执行中";
				break;
				case "B3": return "正式执行中";
				break;
				case "A4": return "执行中止";
				break;
				case "A5": return "执行完成";
				break;
				case "A6": return "执行错误";
				break;
		    }
		}},
		{display:'操作',name:'operation',width:1,align:'center',dataindex:'campId',mapping:['supportOps'],convert:function(v,mappVal){
			if(mappVal[0].indexOf("W")>0 && mappVal[0].indexOf("D")>0){
			     return '<a href="#/marketing/modificationMarketLayer:'+v+'" class="modify_btn" title="修改"></a><a href="javascript:void(0);" class="del_btn" title="删除" onclick="delMarketList(this)"></a>' 	
			}else if(mappVal[0].indexOf("W")>0){
				 return '<a href="#/marketing/modificationMarketLayer:'+v+'" class="modify_btn" title="修改"></a>'
			}else if(mappVal[0].indexOf("D")>0){
				 return '<a href="javascript:void(0);" class="del_btn" title="删除" onclick="delMarketList(this)"></a>'
			}else{
				return '';
			}
			
		}}

	],
	sortname: "camp_id",
	sortorder: "desc",
	//searchitems :{display: '用户名', name : 'userName'},
	usepager: true,
	useRp: true,
	rp: 20,
	showTableToggleBtn: true,
	colAutoWidth:true,
	rowDblClick:function(){

		var rec = $(this).data('rec'),nodeHhash = '#/marketing/campaign.node:'+rec.campId;
		//还需要去重判断
		//campHdAry.push({href:'#/yingxiao/loadPanel.node:'+rec.campId,name:rec.campName});
		if(!checkValue(campHdAry,rec.campId)){
			campHdAry.push({href:nodeHhash,name:rec.campName,id:rec.campId});
		}
		location.hash = nodeHhash;

		window.setTimeout(isShowSlideBtn,1000);
	}
});
var $listHeader=$(".listHeader");
var listHeaderW=$listHeader.width()-40;
var $listTab=$listHeader.find(".listTab");
function isShowSlideBtn(){
	  var listTabW=$listTab.width();
	  var len=parseInt(listTabW/listHeaderW);
	  var maxlimitLeft=-len*listHeaderW;
	  var marginleft=parseInt($listTab.css("marginLeft"));
      if(marginleft>maxlimitLeft){
		   $listHeader.find(".slide_right_btn").show();
	  }else{
		   $listHeader.find(".slide_right_btn").hide();
		}
	  if(marginleft<0){
		  $listHeader.find(".slide_left_btn").show();
	  }else{
		  $listHeader.find(".slide_left_btn").hide();
	  }
}
$(".slide_right_btn").live("click",function(){
	 var marginleft=parseInt($listTab.css("marginLeft"));
     if(!$listTab.is(":animated")){
		  $listTab.animate({"marginLeft":marginleft-listHeaderW},500,function(){
			  isShowSlideBtn();
		  })
	 }
});
$(".slide_left_btn").live("click",function(){
	 var marginleft=parseInt($listTab.css("marginLeft"));
     if(!$listTab.is(":animated")){
		  $listTab.animate({"marginLeft":marginleft+listHeaderW},500,function(){
			   isShowSlideBtn();
		  })
	 }
});
//检查值是否存在数组中
function checkValue(ary,id){
	var hasItem = false;
	$.each(ary,function(i,n){
		if(n.id == id) hasItem = true;
	});
	return hasItem;
}

/*自定义搜索*/

var el = $('.campaignListCon')[0];
var $addHdModel=$("#addHdModel");
/*$('[name=showMyactivity]').change(function(){alert(2);
	el.grid.addParams(this.name,this.value);
	el.grid.populate();
});*/
$('[name=campState]').change(function(){
	el.grid.addParams(this.name,this.value);
	el.grid.populate();
});


$('[name=show_activity]').click(function(){
	el.grid.addParams(this.name,this.checked);
	el.grid.populate();
});

$('#marketSearch').click(function(){
	el.p.qtype = $('.hdSearch').attr('name');
	el.p.query = $('.hdSearch').val();
	el.grid.populate();
});
$(".hdSearch").live("focus",function(){
	  $(this).siblings(".inputTips").hide();
}).live("blur",function(){
	var val=$(this).val();
	if(val==""){
		   $(this).siblings(".inputTips").show();
		   el.p.query="";
		   $('.campaignListCon').flexReload();
	 }
});
/*删除列表*/
function delMarketList(obj){
   var $tr=$(obj).closest("tr");
		var rec=$(obj).closest('tr').data('rec');
		var campId=rec.campId;
		var campName=rec.campName;
		var campStatus=rec.status;
		var isOpen=false;
		var isExecute=false;
		$("ul.listTab li").each(function(){
			  if($(this).children("a").text()==campName){
				  isOpen=true;
				  $(this).Alert({"title":"删除提示","str":"请先退出该活动流程","mark":true,"width":"160px"});
			  }
		});
		if(campStatus=="B1" || campStatus=="B2" || campStatus=="B3"){
			   isExecute=true;
			   $(this).Alert({"title":"删除提示","str":"对不起，活动只有在设计中，执行完成，执行中止，执行错误状态下可以删除操作","mark":true});

		}
		if(!isOpen && !isExecute){
			$(this).Confirm({"title":"删除提示","str":"确认删除该活动？","mark":true},function(){
				   $.ajax({
					  url:"campaign/"+campId,
					  async: false,
					  type:"DELETE",
					  success:function(response){
						  var data=response;
						  if(data.status=="error"){
							  $(this).Alert({"title":"删除提示","str":data.data[0].errordesc,"mark":true,"width":"160px"}); 
						  }else{
							  $(obj).closest("tr").detach();  
						  }
					  }
				   });
			})
		}
		//$(obj).closest("tr").detach();
}