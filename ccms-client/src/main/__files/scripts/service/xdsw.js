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

	var _urpayST = $('#createdStart'),
		_urpayET = $('#createdEnd');
		
	_urpayST.datetimepicker({
		timeFormat:'HH:mm:ss',
		showSecond:true,
		changeMonth:true,
		changeYear:true,
		minDate:-3,
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

function customerCenter($scope,$http){
	
	$http.get(root+"shop/taobao/list").success(function(res){
		$scope.shops = res.data.shops;
		$scope.init($scope.shops[0]);
	});
	$scope.init = function(shop){
		if($scope.shopName != shop.shopName){
			$scope.shopName = shop.shopName;
			$scope.shopId = shop.shopId;
		}
		//加载旺旺
		$scope.serviceStaffName='';
		var parames = {
			"dpId" : $scope.shopId
		};
		$http.post(root + "customerCenter/orders/orderReceptionWw",parames).success(function(res){
			if(res.status=="0"){
				 $scope.serviceStaffNames = res.data;
			}
		});
		$scope.reset();
		$scope.searchOrdersList();
	}
	function getObjInAry(obj,id,key){
		for(var i=0;i<obj.length;i++){
			if(id == obj[i][key]) return obj[i];
		}
	}
	
	
	
	/*时间设置*/
	var newDate = new Date();
	var dated = newDate.setDate(newDate.getDate() -3 );
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
	
	$scope.reset = function(){
		$scope.serviceStaffName = "";
		$scope.urpayStatus = "";
		$scope.sortOrder = "created_desc";
		$scope.serviceStaffNick = "";
		$scope.isHideVal = "false";
		$scope.outerIid = "";
		$scope.goodsKeyWord = "";
		$scope.mobile = "";
		$("#createdStartTime").val('').datetimepicker('option', 'maxDate',null);
		$("#createdEndTime").val('').datetimepicker('option', 'minDate',null);
	}
	
	/*列表*/
	$scope.ordersList=[];
	$scope.flag = false;
	$scope.searchOrdersList=function(page,blog){
		$scope.showUrPay = false;
	    $scope.master=false;
		if(validationPhone($("#mobile").val())){
			var parame={};
			parame.dpId=$scope.shopId;
			parame.serviceStaffName=$scope.serviceStaffName;
			parame.urpayStatus= $scope.urpayStatus;
			parame.order= $scope.sortOrder;
			parame.customerno= $scope.serviceStaffNick;
			parame.createdStartTime=$("#createdStart").val();
			parame.createdEndTime=$("#createdEnd").val();
			parame.isHide= $scope.isHideVal;
			parame.title= $scope.goodsKeyWord;
			parame.outerIid=$scope.outerIid;
			parame.mobile= $scope.mobile;
			if(page && blog){
			   parame.currentPage=1;
			   $scope.currentPage=1;
			   $scope.enterPage=1;
			}else{
			   parame.currentPage=$scope.currentPage;
			}
			//console.log(parame.isHide);
			if(parame.isHide == ""){
				 $scope.urpayhides = "";
			}
			if(parame.isHide == "true"){
				$scope.urpayhides = "批量取消隐藏";
			}
			if(parame.isHide == "false"){
				$scope.urpayhides = "批量隐藏";
			}
			parame.pageSize=parseInt($scope.rp);
			if(ccms.getSecond($("#createdStart").val()) > ccms.getSecond($("#createdEnd").val())){
				$("#createdStart").css("border","1px solid red");
				$("#createdEnd").css("border","1px solid red");
				return false;
			}else{
				$("#createdStart").css("border","1px solid #D9D9D9");
				$("#createdEnd").css("border","1px solid #D9D9D9");
			}
			
			$http.post(root+"customerCenter/orders/ordersList",parame).success(function(res){
				if(res.status=="0"){
					 $scope.ordersList=res.data.data;
					 $scope.visit = res.visit;
					 //$scope.totalPage=Math.ceil((res.data.total)/(parame.pageSize));
					 $http.post(root+"customerCenter/orders/ordersCount",parame).success(function(d){
					       if(d.status=="0"){
						       $scope.totalPage=Math.ceil((d.data.total)/(parame.pageSize));
							   if($scope.totalPage==0){$scope.totalPage=1;}
						   }
					 });
					$scope.showUrPay = true;
					
				}
			});
		}
		

	}

	$scope.urpay=function(s,a,b){
	    $scope.urpays={};
		$scope.urpays.payTitle="";
		if(s == "0"){
		    $scope.selectionPay="pay_no";
			$scope.againdunning="baobei_dunning";
			$scope.urpays.tradeCount=a.tradeCount;
			$scope.urpays.noPayedCount=a.noPayedCount;
			$scope.urpays.payedCount=a.payedCount;
			$scope.urpays.closeCount=a.closeCount;
			$scope.urpays.msg=a.msg;
			if(a.advicesStatus == "0"){
                $scope.urpays.payTitle="不建议催付";
				$scope.myStyle = {color:'red'};
			}else if( a.advicesStatus == "1"){
				$scope.urpays.payTitle="建议催付";
				$scope.myStyle = {};
			}
			return "催付";
		}
		else if(s == "1"){
		    $scope.urpays.payTitle="订单已催付";
		    $scope.selectionPay="pay_yes";
			$scope.againdunning="baobei_againdunning";
			return "再次催付";
		}
	}
	
	$scope.cfsuggest = function(s,a){
		if(s == '0'){
			if(a.advicesStatus == "0"){
                return "notSuggest";
			}else if( a.advicesStatus == "1"){
				return "Suggest";
			}
		}else if(s == '1'){
			return "Suggested";
		}
	}
    $scope.manualUrpayStatus=function(s){
		switch (s){
			case 0: return "无催付方式";
			break;
			case 1: return "短信催付";
			break;
			case 2: return "旺旺催付";
			break;
		}
	}
	/*会员等级*/
	$scope.gradeClass = function(name){
		if(name == '新客户'){
			return 'newMember';
		}else if(name == '普通会员'){
			return 'generalMember';
		}else if(name == '高级会员'){
			return 'highMember';
		}else if(name == 'VIP会员'){
			return 'VIPMember';
		}else if(name == '至尊VIP'){
			return 'VIPMember';
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
	$scope.showsellerMemo = function(str){
		return str ? '' : ' style="display:none;"';
	}
	$scope.showColor = function(){
		return ' style="color:#999;"';
	}
	
	var kindEditor=kindEditorObj.creatEditor("#textEditor");
	var kindContent = kindEditorObj.getKindEditorVal(kindEditor.html());
	var status = true;
	/*批量催付*/
	$scope.urpayOrders = function(){
		if($("[name=orderChb]:checked").length==0){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要催付的订单","mark":true});
		}else{
			$("#SMS_position").addInteractivePop({magTitle:"未付款催付设置",mark:true,drag:true,position:"fixed"});
			var userIds = '';
			var phones = '';
			$('[name=orderChb]:checked').each(function(){
				var userId = $(this).parents('tr').find('.userId').html();
				var phone = $(this).parents('tr').find('.phone').html();
				if(userId) userIds += userId + ";";
				if(phone) phones += phone + ";";
			});
			var ids=[]
			$("[name=orderChb]:checked").each(function(){
				var $this=$(this);
				ids.push($this.siblings("span").text());
			});
			$scope.Staffs = userIds;
			$scope.mobiles = phones;
			$scope.screen = true;
			$http.get(root+"gateway/sms_list").success(function(res){
				$scope.gateway = res;
				$scope.gatewayList = $scope.gateway[0];
			});
			$scope.urpayOrderViewSave = function(){
				var gatewayId = $scope.gatewayList.gatewayId;
				var smsContent = ccms.filterTags(kindEditorObj.getKindEditorVal(kindEditor.html())) ;
				if(smscf.checked){
					if($.trim(userIds) == ''){
						$(this).Alert({"title":"提示","str":"没有可催付的买家","mark":true,"width":"160px"});
						return false;
					}
					if($.trim(phones) == ''){
						$(this).Alert({"title":"提示","str":"没有可催付的手机号码","mark":true,"width":"160px"});
						return false;
					}
					if($.trim(smsContent) == ''){
						$(this).Alert({"title":"提示","str":"请填写短信内容","mark":true,"width":"160px"});
						return false;
					}
					var params = {
						tids:ids,
						smsContent:smsContent,
						gatewayId:gatewayId,
						filterBlacklist:$scope.screen
					};	
			
					if(status){
						status = false;
						$http.post(root+"customerCenter/orders/urpayOrders",params).success(function(res){
							if(res.status == "-1"){
								var str = res.message;
								$(this).Alert({"title":"提示","str":str,"mark":true});
							}
							setTimeout(function(){
							if(res.status=="0"){
								$("#SMS_position").hide();
								$(".yunat_maskLayer").remove();
								$(this).Alert({"title":"提示","str":"催付成功","mark":true,"width":"160px"});
								status = true;
								$scope.searchOrdersList();
							}
							},1000);
							setTimeout(function(){status=true},2000);
						});
					}
				}else if(wanwancf.checked){
					if($.trim(userIds) == ''){
						$(this).Alert({"title":"提示","str":"没有可催付的买家","mark":true,"width":"160px"});
						return false;
					}
					var params = {
						tids:ids,
						note:$scope.note || '已经通过旺旺催付'
					};
					if(status){
						status = false;
						$http.post(root+"customerCenter/orders/wwUrpayOrders",params).success(function(res){
							if(res.status == "-1"){
								var str = res.message;
								$(this).Alert({"title":"提示","str":str,"mark":true});
							}
							if(res.status=="0"){
								$("#SMS_position").hide();
								$(".yunat_maskLayer").remove();
								$(this).Alert({"title":"提示","str":"催付成功","mark":true,"width":"160px"});
								$scope.searchOrdersList();
							}
							setTimeout(function(){status=true},2000);
						})
					}
				}
			}
		}
		
	}
	/*催付*/
    $scope.urpayOrder = function(ids){
		$("#SMS_position").addInteractivePop({magTitle:"未付款催付设置",mark:true,drag:true,position:"fixed"});	
		$scope.Staffs = $("#"+ids).parents('tr').find('.userId').html();
		$scope.mobiles = $("#"+ids).parents('tr').find('.phone').html();
		var smscf=document.getElementById("smscf");
		var wanwancf=document.getElementById("wanwancf");
		//var mobiles = document.getElementById("mobiles");
		$scope.screen = true;
		$http.get(root+"gateway/sms_list").success(function(res){
			$scope.gateway = res;
			$scope.gatewayList = $scope.gateway[0];
		});
		
		$scope.urpayOrderViewSave = function(){
			var gatewayId = $scope.gatewayList.gatewayId;
			var smsContent = ccms.filterTags(kindEditorObj.getKindEditorVal(kindEditor.html()));
			if(smscf.checked){
				if($.trim($scope.mobiles) == ''){
					$(this).Alert({"title":"提示","str":"没有可催付的手机号码","mark":true,"width":"160px"});
					return false;
				}
				if($.trim(smsContent) == ''){
					$(this).Alert({"title":"提示","str":"请填写短信内容","mark":true,"width":"160px"});
					return false;
				}
				var params={
					tid:ids,
					smsContent:smsContent,
					gatewayId:gatewayId,
					filterBlacklist:$scope.screen,
				};
				if(status){
					   status=false;
					   $http.post(root+"customerCenter/orders/urpayOrder",params).success(function(res){
						if(res.status == "-1"){
							var str = res.message;
							$(this).Alert({"title":"提示","str":str,"mark":true});
						}
						if(res.status=="0"){
							$("#SMS_position").hide();
							$(".yunat_maskLayer").remove();
							$(this).Alert({"title":"提示","str":"催付成功","mark":true,"width":"160px"});
							$scope.searchOrdersList();
						}
						setTimeout(function(){status=true},2000);
					  })
				}
			}else if(wanwancf.checked){
				var params={
					tid:ids,
					note:$scope.note || '已经通过旺旺催付'
				};
				if(status){
					status = false;
					$http.post(root+"customerCenter/orders/wwUrpayOrder",params).success(function(res){
						if(res.status == "-1"){
							var str = res.message;
							$(this).Alert({"title":"提示","str":str,"mark":true});
						}
						if(res.status=="0"){
							$("#SMS_position").hide();
							$(".yunat_maskLayer").remove();
							$(this).Alert({"title":"提示","str":"催付成功","mark":true,"width":"160px"});
							$scope.searchOrdersList();
						}
						setTimeout(function(){status=true},2000);
					})
				}
			}

		}
	}
	
	
	
	/*隐藏*/
	$scope.HideVal = function(h){
		if(h == true){
			return '取消隐藏';
		}else if(h == false){
			return '隐藏';
		}
	}
	$scope.isHide = function(id){
		var parame={tid:id,hideColumnName:"is_hide"};
		var ishide = $("#isHide").val();
		//console.log(ishide);
		switch (ishide){
			case "" : $http.post(root+"customerCenter/orders/hiddenOrder",parame).success(function(res){
						if(res.status=="0"){
							var obj = $("#" + id).parents("tr").siblings("tr").find(".baobei_hide");
							obj.val(obj.val() == '隐藏' ? '取消隐藏' : '隐藏');
						}else{
							 var str=res.data[0].errordesc;
							 $(this).Alert({"title":"提示","str":str,"mark":true});
						}
					});
					break;
			case "false": 	$http.post(root+"customerCenter/orders/hiddenOrder",parame).success(function(res){
								if(res.status=="0"){
									var obj = $("#" + id).parents("tr").siblings("tr").find(".baobei_hide");
									obj.val(obj.val() == '隐藏' ? '取消隐藏' : '隐藏');
								}else{
									 var str=res.data[0].errordesc;
									 $(this).Alert({"title":"提示","str":str,"mark":true});
								}
							});
							$(this).Alert({"title":"提示","str":"隐藏订单成功","mark":true});
							$("#" + id).parents("tbody").hide();
							$scope.searchOrdersList();
							break;
			case "true": 	$http.post(root+"customerCenter/orders/hiddenOrder",parame).success(function(res){
								if(res.status=="0"){
									var obj = $("#" + id).parents("tr").siblings("tr").find(".baobei_hide");
									obj.val(obj.val() == '隐藏' ? '取消隐藏' : '隐藏');
								}else{
									 var str=res.data[0].errordesc;
									 $(this).Alert({"title":"提示","str":str,"mark":true});
								}
							});
							$(this).Alert({"title":"提示","str":"取消隐藏订单成功","mark":true});
							$("#" + id).parents("tbody").hide();
							$scope.searchOrdersList();
							break;
		}
		
		
		
	}
	$scope.isHideVal = "false";
	$scope.urpayhides = "批量隐藏";
	
	
	/*批量隐藏*/
	$scope.isHides = function(){
		if($("[name=orderChb]:checked").length==0){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要隐藏的订单","mark":true});
		}else{
			var ids=[];
			$("[name=orderChb]:checked").each(function(){
				var $this=$(this);
				ids.push($this.siblings("span").text());
				$(this).parents("tbody").hide();
			});
			var hide = $scope.isHideVal == "false" ? true : false;
			var parame={
				tids:ids,
				isHide: hide,
				hideColumnName:"is_hide"
			};
			$http.post(root+"customerCenter/orders/hiddenOrders",parame).success(function(res){
				if(res.status=="0"){
					if($scope.isHideVal == 'true'){
						$(this).Alert({"title":"提示","str":"批量取消隐藏成功","mark":true});
					}else{
						$(this).Alert({"title":"提示","str":"批量隐藏成功","mark":true});
					}
					
				}else{
					var str=res.data[0].errordesc;
					$(this).Alert({"title":"提示","str":str,"mark":true});
				}
			});
		}
	}
	/*批量成功订单*/
	$scope.urpayOrderViewCancel = function(){
		$("#SMS_position").hide();
		$(".yunat_maskLayer").remove();
	}
	$scope.floor = function(a,b){
		var x = Math.floor(a/b);
		return x;
	}

	$scope.Fixed = function(timeVal){
		var num = new Number(timeVal);
		var n = num.toFixed(2);
		return n;
	}
	
	/*催付成功订单*/
	$scope.box = function(){
		$("#SOrder_position").addInteractivePop({magTitle:"催付成功订单",mark:true,drag:true,position:"fixed"});
		$scope.SOrder = function(page,blog){		
			$scope.successOrder = [];
			$.each(function(){
				var n = $scope.successOrder
			})
			var parame={}
			if(page && blog){
			   parame.currPage=1;
			   $scope.OcurrentPage=1;
			   $scope.OenterPage=1;
			}else{
			   parame.currPage=$scope.OcurrentPage;
			}
			parame.dpId = $scope.shopId;
			parame.pageSize=$scope.Orp;
			$http.post(root+"customerCenter/orders/urPayOrdersLogList",parame).success(function(res){
				if(res.status=="0"){
					 $scope.successOrder = res.data;
					 $scope.OtotalPage=Math.ceil((res.data.totalElements)/(parame.pageSize));
					 if($scope.OtotalPage==0){$scope.OtotalPage=1;}
				}
				
			});
			
		}	
	}
	
	//分页
	$scope.currentPage=1;
	$scope.totalPage=1;
	$scope.enterPage=1;
	$scope.rp=20;
	$scope.effectiveRange=function(val){
		if(isNaN(val)){
			$scope.currentPage=1;
			return false;
		  }else{
			  if(val<1 || val>$scope.totalPage){
				  return false;  
			   }else{
				  return true;  
			   }
		  }

	}
	$scope.firstPage=function(){
		if($scope.effectiveRange($scope.currentPage)){
			 $scope.currentPage=1;
			 $scope.enterPage=1;
			 $scope.searchOrdersList();
		}
	}
	$scope.lastPage=function (){
		if($scope.effectiveRange($scope.currentPage)){
			 $scope.currentPage=$scope.totalPage;
			 $scope.enterPage=$scope.totalPage;
			 $scope.searchOrdersList();
		}
	}
	$scope.prevPage=function (){
		if($scope.effectiveRange($scope.currentPage) && $scope.currentPage!=1){
			 $scope.currentPage--;
			 $scope.enterPage=$scope.currentPage;
			$scope.searchOrdersList();
		}
	}
	$scope.nextPage=function (){
		if($scope.effectiveRange($scope.currentPage) && $scope.currentPage!=$scope.totalPage){
			 $scope.currentPage++;
			 $scope.enterPage=$scope.currentPage;
			 $scope.searchOrdersList();
		}
	}
	$scope.changeRp=function(){
		 $scope.currentPage=1;
		 $scope.enterPage=1;
		 $scope.searchOrdersList();
	}
	//分页(催付成功订单)
	$scope.OcurrentPage=1;
	$scope.OtotalPage=1;
	$scope.OenterPage=1;
	$scope.Orp=20;
	$scope.OeffectiveRange=function(val){
		if(isNaN(val)){
			$scope.OcurrentPage=1;
			return false;
		  }else{
			  if(val<1 || val>$scope.OtotalPage){
				  return false;  
			   }else{
				  return true;  
			   }
		  }

	}
	$scope.orderfirstPage=function(){
		if($scope.OeffectiveRange($scope.OcurrentPage)){
			 $scope.OcurrentPage=1;
			 $scope.OenterPage=1;
			 $scope.SOrder();
		}
	}
	$scope.orderlastPage=function (){
		if($scope.OeffectiveRange($scope.OcurrentPage)){
			 $scope.currentPage=$scope.OtotalPage;
			 $scope.enterPage=$scope.OtotalPage;
			 $scope.SOrder();
		}
	}
	$scope.orderprevPage=function (){
		if($scope.OeffectiveRange($scope.OcurrentPage) && $scope.OcurrentPage!=1){
			 $scope.OcurrentPage--;
			 $scope.OenterPage=$scope.OcurrentPage;
			$scope.SOrder();
		}
	}
	$scope.ordernextPage=function (){
		if($scope.OeffectiveRange($scope.OcurrentPage) && $scope.OcurrentPage!=$scope.OtotalPage){
			 $scope.OcurrentPage++;
			 $scope.OenterPage=$scope.OcurrentPage;
			 $scope.SOrder();
		}
	}
	$scope.orderchangeRp=function(){
		 $scope.OcurrentPage=1;
		 $scope.OenterPage=1;
		 $scope.SOrder();
	}
	
/* 	function windowHeight() {
		var de = document.documentElement;
		return (de && de.clientHeight)||document.body.clientHeight;
	} */

	function windowWidth() {
		var de = document.documentElement;
		return (de && de.clientWidth)||document.body.clientWidth;
	}

	function changeWindowSize(){
		//var wh=windowHeight();
		if($('#kfzx_searchbox').length){
			/* var Sbox = document.getElementById("kfzx_searchbox").offsetHeight
			document.getElementById("result_order").style.height = (wh - Sbox - 210 )+"px"; */
			
			var ww=windowWidth();
			document.getElementById("PageDive").style.width = (ww - 170 )+"px";
		}
		
	}
	changeWindowSize();
	//window.onload = window.onresize = changeWindowSize;
	$(window).resize(changeWindowSize);
}





//手机验证
function validationPhone(str){
		 var partten = /^0{0,1}(13[0-9]|14[0-9]|15[0-9]|18[0-9])[0-9]{8}$/;
		 if(partten.test(str) || str==""){
				$("#mobile").siblings(".tips").html('').removeClass("error");
				return true;
		 }else{
				$("#mobile").siblings(".tips").html("手机号码输入格式不正确").addClass("error");
				return false;
		}
};
function WWCuifu(a){
	$(".wanwan_cuifu").show();
	$(".SMS_cuifu").hide();
};
function SMSCuifu(a){
	$(".wanwan_cuifu").hide();
	$(".SMS_cuifu").show();
};