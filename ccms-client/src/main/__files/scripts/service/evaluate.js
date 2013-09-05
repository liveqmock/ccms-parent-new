$(function(){
	$(".searchbox_condition tr:eq(1)").hide();
	$(".unfold").toggle(function(){
		$(".searchbox_condition tr:eq(1)").show();
		$(this).addClass("shrink")
	},function(){
		$(".searchbox_condition tr:eq(1)").hide();
		$(this).removeClass("shrink");
	})
})
//分页定位
function windowWidth() {
	var de = document.documentElement;
	return (de && de.clientWidth)||document.body.clientWidth;
} 
function changeWindowSize(){
	if($('#kfzx_searchbox').length){
	var ww=windowWidth();
	document.getElementById("PageDive").style.width = (ww - 190 )+"px";
	}
}
changeWindowSize();
$(window).resize(changeWindowSize); 
$(function(){
	$(".dunning_tip").live("mouseover",function(){
		$(this).siblings(".kf_tip_box").show();
	});
	$(".dunning_tip").live("mouseout",function(){
		$(this).siblings(".kf_tip_box").hide();
	});
	$(".deal_time").live("mouseover",function(){
		$(this).siblings(".kf_tip_box").show();
	});
	$(".deal_time").live("mouseout",function(){
		$(this).siblings(".kf_tip_box").hide();
	});
	$('#shopinfo').hover(function(){
		$(this).find("dl").stop(true,true).slideDown().siblings('span').addClass("spanHover");
	},function(){
		$(this).find("dl").stop(true,true).hide().siblings('span').removeClass("spanHover");
	}).find('a').live('click',function(){
		$(this).parents('dl').hide();
	});
	var _urpayST = $('#createdStart'),
		_urpayET = $('#createdEnd');
	_urpayST.datetimepicker({
		timeFormat:'HH:mm:ss',
		showSecond:true,
		changeMonth:true,
		changeYear:true,
		//minDate:-30,
		ShowCheckBox:true,
		onClose: function(dateText){
			if(dateText){
				_urpayST.datetimepicker('setDate', dateText);
				var startDate = _urpayST.datetimepicker('getDate'),
					endDate = _urpayET.datetimepicker('getDate');
				_urpayET.datetimepicker('option', 'minDate', dateText);
				
			}
		}
	});
	_urpayET.datetimepicker({
		timeFormat:'HH:mm:ss',
		showSecond:true,
		changeMonth:true,
		changeYear:true,
		onClose: function(dateText) {
			if(dateText){
				_urpayET.datetimepicker('setDate', dateText);
				var startDate = _urpayST.datetimepicker('getDate'),
					endDate = _urpayET.datetimepicker('getDate');
				_urpayST.datetimepicker('option', 'maxDate', dateText);
			}
		}
	});
	

})

function evaluateCtr($scope,$http){
	$scope.showUrPay=true;
    $http.get(root+"shop/taobao/list").success(function(res){
		$scope.shops = res.data.shops;
		$scope.init($scope.shops[0]);
	});
	$scope.init = function(shop){
		if($scope.shopName != shop.shopName){
			$scope.shopName = shop.shopName;
			$scope.shopId = shop.shopId;
			$scope.shopType = shop.shopType
		}
		//加载旺旺
		$scope.serviceStaffName=[];
		var parames = {
			"dpId" : $scope.shopId
		};
		$http.post(root + "customerCenter/orders/orderReceptionWw",parames).success(function(res){
			if(res.status=="0"){
				 $scope.serviceStaffName = res.data;
			}
		});
		$scope.searchEvaluateList();
	}
	$scope.returnShopType=function(type){
	    if(type == "B"){
		    return true;	
		}else if(type == "C"){
		    return false;	
		}	
	}
	$scope.isShowAutoReplyComment=function(){
		 if($scope.returnShopType($scope.shopType)){   //为B店铺
		      return false;
		 }else{
			  return true;
		 }
	}

	/*时间设置*/
	var newDate = new Date();
	var dated = newDate.setDate(newDate.getDate() -30 );
	var y=newDate.getFullYear();
	var m=newDate.getMonth()+1;
	var d=newDate.getDate();
	var time=y+"-"+m+"-"+d; 
	var d = new Date();
	var year = d.getFullYear();
	var month = d.getMonth()+1;
	var date = d.getDate();
	var time1=year+"-"+month+"-"+date; 
	$scope.date1 = time + " 00:00:00";
	$scope.date2 = time1 + " 23:59:59";
	//搜索查询列表
	//初始化
	$scope.evlContent="";
	$scope.memberLevel="-1";
	$scope.goodsName="";
	$scope.result="";
	$scope.nick="";
	$scope.searchEvaluateList=function(){
		$scope.master=false;      
		$scope.showUrPay=false;
		 $scope.checkIsPager(arguments,$scope,1,20);//分页必须 -- 检查执行的函数是在页面其他地方发起还是分页上
		 var parame={};
		 parame.shopId=$scope.shopId;
		 parame.content=$scope.evlContent;
		 parame.memberLevel=$scope.memberLevel;
	     parame.isRegardFlag=$scope.regard;	 
		 parame.isExplainFlag=$scope.explain;
		 parame.itemTitle=$scope.goodsName;
		 parame.beginCreated=$("#createdStart").val();
		 parame.endCreated=$("#createdEnd").val();
		 parame.result=$scope.result;
		 parame.nick=$scope.nick;		 
		 parame.currPage=$scope.defaultPage;
		 parame.pageSize=$scope.defaultSize;
		 if($scope.regard !=　""){
			 parame.isRegardFlag=$scope.regard; 
		 }
		
		 $http.post(root+"traderate/query",parame).success(function(res){
			 $scope.showUrPay=true;
			 if(res.status=="0"){
				 var data=res.data;
				 $scope.ordersList=res.data.content;
			 }
			//分页必须  分页配置
			$scope.pagerSetting = {
				curpage: data.currPage,
				total: data.totalElements,
				totalPages: data.totalPages,
				//totalTip: '本次筛选出发送记录{total}条',
				//exportUrl: root+'traderate/query?'+$.param(params),
				rp: data.pageSize,
				submit: function(){
					$scope.searchEvaluateList('isPager');
				}
			}														
		})
	}
	$scope.searchEvaluateList();
	$scope.getRegardType=function(type){
		  if(type==27){
		     return "短信关怀";	  
		 }else if(type==28){
			 return "旺旺关怀";	 
		 }else if(type==29){
			 return "手机关怀";	 
		 }
	}
	/*表单重置*/
	$scope.reset=function(){
		$scope.evlContent="";
		$scope.itemTitle="";
        $scope.memberLevel="-1";
		$scope.regard="";
		$scope.result="";
		$scope.goodsName="";
        $scope.defaultPage=1;
		$scope.explain="";
		$scope.defaultSize=20;
		$scope.nick=""; 
		return false;
	}
	/*创建时间处理*/
	$scope.greatTime = function(time){
		function addZero(num){
			return num < 10 ? '0'+num : num;
		}
		var year =  new Date(time).getFullYear();
		var month =  addZero(new Date(time).getMonth() + 1);
		var date =  addZero(new Date(time).getDate());
		var hour =  addZero(new Date(time).getHours());
		var minute =  addZero(new Date(time).getMinutes());
		var second =  addZero(new Date(time).getSeconds());
		return year + "-" +  month + "-" +  date + " " + hour + ":" + minute + ":" + second;
	}
	/*中差评设置*/
	$scope.setGradeIcon=function(result){
		 if(result=="good"){
		     return "result_good";	 
		 }else if(result=="neutral"){
			 return "result_neutral";
		 }else if(result=="bad"){
			 return "result_bad";
		 }
	}
	/*会员等级*/
	$scope.gradeClass = function(grade){
		if(grade == '0' || grade == ''){
			return 'newMember';
		}else if(grade == '1'){
			return 'generalMember';
		}else if(grade == '2'){
			return 'highMember';
		}else if(grade == '3'){
			return 'VIPMember';
		}else if(grade == '4'){
			return 'VIPMember';
		}
	}
	$scope.gradeValue = function(grade,trade_count){
		if(grade == '0' || grade == ''){
			if(trade_count>0 && grade == '0'){
				return '未分级';
			}else{
				return '新客户';
			}
			
		}else if(grade == '1'){
			return '普通会员';
		}else if(grade == '2'){
			return '高级会员';
		}else if(grade == '3'){
			return 'VIP会员';
		}else if(grade == '4'){
			return '至尊VIP';
		}
	}
	
	/*备注标记*/
	$scope.flag = function(n){
		switch(n){
			case '' : return 'remark0';
				break;
			case 0 :return 'remark0';
				break;
			case 1 :return 'remark1';
				break;
			case 2 :return 'remark2';
				break;
			case 3 :return 'remark3';
				break;
			case 4 :return 'remark4';
				break;
			case 5 :return 'remark5';
				break;
		}
	}
	/*买家留言*/
	$scope.message=function(b){
		if(b == ""){
			return "";
		}else{
			return "comment";
		}
	}
	//关怀
	$scope.cfsuggestClass=function(flog){
		if(flog == false){
		   	 return "";
		}else{
			return "baobei_againdunning";
	    }
	}
	$scope.cfsuggestValue=function(flog){
		if(flog == false){
		   	 return "关怀";
		}else{
			return "再次关怀";
	    }
	}
	$scope.scope = $scope;
	$scope.careUrl = 'traderate/batch/regard';
	$scope.careTitle = '评价关怀设置';
	$scope.careTypeName = '关怀';
	$scope.careTypes = ["27","28","29"];
	$scope.careSuccess = function(){
		return $scope.searchEvaluateList();
	}
	//批量关怀
	$scope.carings=function(){
	    if($("[name=caringChb]:checked").length==0){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要催付的订单","mark":true});
		}else{
            var arr=[];
			$('[name=caringChb]:checked').each(function(){
				var tid = $(this).attr("tid");
				var oid = $(this).attr("oid");
				var uid = $(this).parents("tr").find(".userId").text();
				var mobile = $(this).attr("mobile");
				arr.push({"tid":tid,"oid":oid,"tel":mobile,"uid":uid});
			});
            $scope.careSetting($scope,arr);
		}
	}
	//解释
	$scope.isShowReplybtn=function(o){
		if(o.reply=="" && $scope.returnShopType($scope.shopType)){  //没有解释且为B店铺时显示解释按钮
		   return true;	
		}else{
		   return false;	
		}
	}
	$scope.replyViewCancel=function(){
		 $("#Explain_position").hide();
		 $(".yunat_maskLayer").remove();
	}
	$scope.explainFn=function(o){
		   $scope.fliter_customeInfo=true;
		   $scope.fliter_productInfo=true;
		   $scope.fliter_evaContent=true;
		   $scope.fliter_replyOid=false;
		   $scope.customerno=o.customerno;
		   $scope.detailUrl=o.detailUrl;
		   $scope.pic_url=o.pic_url;
		   $scope.pro_title=o.title;
		   $scope.eva_content=o.content;
		   $scope.eva_reply="";
		   $("#Explain_position").addInteractivePop({magTitle:"评价解释",mark:true,drag:true,position:"fixed"});
		   $scope.replyViewSave=function(){
			     if($.trim($scope.eva_reply) == ''){
						$(this).Alert({"title":"提示","str":"请填写解释内容","mark":true,"width":"160px"});
						return false;
				 }  
				 /*var params={
						reply:$scope.eva_reply,
						shopId:$scope.shopId
					};*/
				$http.get(root+"traderate/"+o.tid+"/"+o.oid+"/explain?reply="+$scope.eva_reply+"&shopId="+$scope.shopId).success(function(res){
						$("#Explain_position").hide();
						$(".yunat_maskLayer").remove();
						if(res.status=="0"){
							$scope.searchEvaluateList();
							$(this).Alert({"title":"提示","str":"解释成功","mark":true,"width":"160px"});
						}else{
							$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
						}														  
				})	
		   }
	}
	//批量解释
	$scope.explainsFn=function(){
		 if($("[name=caringChb]:checked").length==0){
			   $(this).Alert({"title":"提示","str":"请至少选择一条需要催付的订单","mark":true});
		 }else{
			   $scope.fliter_customeInfo=false;
			   $scope.fliter_productInfo=false;
			   $scope.fliter_evaContent=false;
			   $scope.fliter_replyOid=true;
			   $scope.eva_reply="";
			   $("#Explain_position").addInteractivePop({magTitle:"评价解释",mark:true,drag:true,position:"fixed"});
			   var tids=[];
			   var oids=[];	
			   
			   $scope.keyValueArray =[];
			   $('[name=caringChb]:checked').each(function(){
				   //已经解释过的，不再批量处理中再次处理了
				   if($(this).attr("reply") != ""){
					  
				   }else{     
					    var param ={};
						var tid = $(this).attr("tid");
						var oid = $(this).attr("oid");
						var shopId = $scope.shopId;
						//if(tid) tids.push(tid);
						if(oid) oids.push(oid);	
						
						param.tid=tid;
						param.oid=oid;
						param.shopId = shopId;
						$scope.keyValueArray.push(param);
				   }	
				});
			   $scope.reply_oid=oids.join(",");
			   $scope.replyViewSave=function(){
					  if($.trim($scope.eva_reply) == ''){
							$(this).Alert({"title":"提示","str":"请填写解释内容","mark":true,"width":"160px"});
							return false;
					  } 
					   //var array =[];
					   var params={};
					   //params.tid=tids;
					   //params.oid=oids;
					   params.reply=$scope.eva_reply;
					   //array.push(params);
					   //var  kk = [{"tid":"001","oid":"1001"},"reply":"yyy"];
					  
					  $scope.keyValueArray.push(params);
					  
					   $http.post(root+"traderate/explain",$scope.keyValueArray).success(function(res){
							$("#Explain_position").hide();
							$(".yunat_maskLayer").remove();
							if(res.status=="0"){
								$scope.searchEvaluateList();
								$(this).Alert({"title":"提示","str":"批量解释成功","mark":true,"width":"160px"});
							}else{
								$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
							}																				
					   })
			   } 
		}
	}
	//跟进
	$scope.getFollowupClass=function(type){
		if(type == 0){
		   	return "";
		}else{
			return "baobei_againdunning";
	    }
	}
	$scope.getFollowupVal=function(type){
		 if(type ==0){
		     return "跟进";	 
		 }else if(type==1){
		     return "跟进中";	 
		 }
    }
	//自动回评
	$scope.autoReplyComment=function(){
		 $("#Comment_position").addInteractivePop({magTitle:"自动回评",mark:true,drag:true,position:"fixed"});
		 
		 $http.get(root+"traderate/getautoset?dpId="+$scope.shopId).success(function(res){
				if(res.status == "0"){
					var data = res.data;
					if(data==""){
						 $scope.order_type="order_success";
						 $scope.eva_content="";
						 $scope.isOpen="0";
					}else{					
					    $scope.order_type = data.type;
						$scope.eva_content = data.content;
						$scope.isOpen = data.status;
						$(".SMS_Table .entryFontLen").text(data.content.length);
					}
					
				}																				  
		 });
		 $scope.autoCommentSave=function(){
		     if($scope.eva_content == ""){
				  $(this).Alert({"title":"提示","str":"请填写自动回评内容","mark":true,"width":"160px"});
				  return false;
			 }	
			 var params={};
			 params.dpId=$scope.shopId;
			 params.type=$scope.order_type;
			 params.content=$scope.eva_content;
			 params.status=$scope.isOpen;
			 $http.post(root+"traderate/autoset",params).success(function(res){
					$("#Comment_position").hide();
					$(".yunat_maskLayer").remove();
					if(res.status == "0"){
					     $(this).Alert({"title":"提示","str":"自动回评设置成功","mark":true,"width":"160px"});	
					}													 
			 });
		 };
		 $scope.autoCommentCancel=function(){
			  $("#Comment_position").hide();
			  $(".yunat_maskLayer").remove();
		 }
	}
}