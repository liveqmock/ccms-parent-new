$(function(){
	$(".searchbox_condition tr:eq(1)").hide();
	$(".unfold").toggle(function(){
		$(".searchbox_condition tr:eq(1)").show();
		$(this).addClass("shrink")
	},function(){
		$(".searchbox_condition tr:eq(1)").hide();
		$(this).removeClass("shrink");
	})
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
	var _urpayST = $('#payedStartTime'),
		_urpayET = $('#payedEndTime');
		
	_urpayST.datetimepicker({
		timeFormat:'HH:mm:ss',
		showSecond:true,
		changeMonth:true,
		changeYear:true,
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

function sendGoods($scope,$http){
	
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
		$scope.serviceStaffName=[];
		var parames = {
			"dpId" : $scope.shopId
		};
		$http.post(root + "customerCenter/orders/orderReceptionWw",parames).success(function(res){
			if(res.status=="0"){
				 $scope.serviceStaffName = res.data;
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

	
	$scope.isHideVal = "0";
	$scope.urpayhides = "批量隐藏";
	/*列表*/
	$scope.ordersList=[];
	$scope.flag = false;
		
	$scope.receiver = '-1';
	$http.get(root+"meta/dic/10/value").success(function(res){
		$scope.receivers = res;
	});
	
	$scope.reset = function(){
		$scope.waitDay = '2';
		$scope.careStatus = "-1";
		$scope.orderSort = "payTime_asc";
		$scope.tid = "";
		$scope.customerno = "";
		$scope.isHideVal = "0";
		$scope.receiver = "-1";
		$scope.title = "";
		$("#payedStartTime").val('').datetimepicker('option', 'maxDate',null);
		$("#payedEndTime").val('').datetimepicker('option', 'minDate',null);
	}
	
	$scope.searchOrdersList=function(){
	    $scope.master=false;
		$scope.checkIsPager(arguments,$scope,1,20);
		if(validationwaitDay($("#waitDay").val())){
			var parame={};
			parame.dpId=$scope.shopId;
			parame.waitDay = $scope.waitDay;
			parame.careStatus= $scope.careStatus;
			parame.orderSort= $scope.orderSort;
			parame.customerno= $scope.customerno;
			parame.receiverState=$scope.receiver;
			parame.payedStartTime=$("#payedStartTime").val();
			parame.payedEndTime=$("#payedEndTime").val();
			parame.isHide= $scope.isHideVal;
			parame.title= $scope.title;
			parame.tid= $scope.tid;
			parame.currentPage = $scope.defaultPage;
			parame.pageSize = $scope.defaultSize;
			if(parame.isHide == "-1"){
				 $scope.urpayhides = "";
			}
			if(parame.isHide == "0"){
				$scope.urpayhides = "批量隐藏";
			}
			if(parame.isHide == "1"){
				$scope.urpayhides = "批量取消隐藏";
			}
			
			if(ccms.getSecond($("#payedStartTime").val()) > ccms.getSecond($("#payedEndTime").val())){
				$("#payedStartTime").css("border","1px solid red");
				$("#payedEndTime").css("border","1px solid red");
				return false;
			}else{
				$("#payedStartTime").css("border","1px solid #D9D9D9");
				$("#payedEndTime").css("border","1px solid #D9D9D9");
			}
			$scope.showUrPay = false;
			$http.post(root+"sendGoods/orders/ordersList",parame).success(function(res){
				if(res.status=="0"){
					var data = res.data; 
					$scope.ordersList=data.data;
					$scope.serverTime = data.serverTime;
					$scope.pagersetting = {
						curpage: data.page,
						total: data.total,
						totalPages: data.totalPages,
						rp: data.pageSize,
						submit: function(){
							$scope.searchOrdersList('isPager');
						}
					 }
					$scope.showUrPay = true;
					
				}
			});

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
	$scope.Fixed = function(timeVal){
		var num = new Number(timeVal);
		var n = num.toFixed(2);
		return n;
	}
	
	$scope.shipStatus = function(status){
		if(status == '轻微延迟'){
			$scope.tipCare = "";
			return "dunning_tip Slight";
		}else if(status == '严重延迟'){
			$scope.tipCare = "care_tip_box";
			return "dunning_tip Serious";
			
		}
	}
	$scope.careStatu = function(careVal){
		if(careVal.length == 0){
			return "关怀";
		}else{
			return "再次关怀";
		}
	}
	$scope.againCare = function(careVal){
		if(careVal.length == 0){
			return "";
		}else{
			return "baobei_hide";
		}
	}
	$scope.cfCare = function(careVal){
		if(careVal.length == 0){
			return "";
		}else{
			return "dunning_tip Suggested";
		}
	}
	
	$scope.properties = function(){
		$("#properties_position").addInteractivePop({magTitle:"发货延迟预警条件配置",mark:true,drag:true,position:"fixed"});
		var parame = {
			dpId:$scope.shopId,
			groupName:"sendGoods"
		}
		$scope.averageDelay = "48";
		$scope.seriousDelay = "120";		
		$http.post(root+"properties/getGroup",parame).success(function(res){
			$scope.propertie = res.data;
			$scope.averageDelay = $scope.propertie[0].value;
			$scope.seriousDelay = $scope.propertie[1].value;
		});
	}
	$scope.urpayOrderViewCancel = function(){
		$("#properties_position").hide();
		$(".yunat_maskLayer").remove();
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
		var parame={tid:id,hideColumnName:"sendgoods_hide"};
		var ishide = $("#isHide").val();
		//console.log(ishide);
		switch (ishide){
			case "-1" : $http.post(root+"customerCenter/orders/hiddenOrder",parame).success(function(res){
						if(res.status=="0"){
							var obj = $("#" + id).parents("tr").siblings("tr").find(".baobei_hide");
							obj.val(obj.val() == '隐藏' ? '取消隐藏' : '隐藏');
						}else{
							 var str=res.data[0].errordesc;
							 $(this).Alert({"title":"提示","str":str,"mark":true});
						}
					});
					break;
			case "0": 	$http.post(root+"customerCenter/orders/hiddenOrder",parame).success(function(res){
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
			case "1": 	$http.post(root+"customerCenter/orders/hiddenOrder",parame).success(function(res){
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
			var hide = $scope.isHideVal == "0" ? true : false;
			var parame={
				tids:ids,
				isHide: hide,
				hideColumnName:"sendgoods_hide"
			};
			$http.post(root+"customerCenter/orders/hiddenOrders",parame).success(function(res){
				if(res.status=="0"){
					if($scope.isHideVal == '1'){
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
	
	//关怀配置
	$scope.scope = $scope;
	$scope.careUrl = 'sendGoods/orders/careOrders';
	$scope.careTitle = '未发货关怀设置';
	$scope.careTypeName = '关怀';
	$scope.careTypes = ["18","19","20"];
	$scope.careSuccess = function(){
		return $scope.searchOrdersList();
	}
	$scope.massCare = function(){
		if($("[name=orderChb]:checked").length==0){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要关怀的订单","mark":true});
		}else{
			var massData = [];
			$('#result_order tbody  tr').each(function(){
				if($(':checkbox',this).attr('checked')){
					massData.push({uid: $('.userId',this).text(), tel: $('.phone',this).text(), tid: $('.tid',this).text()});
				}
			});
			$scope.careSetting($scope, massData);
		}
		
	}
	
	$scope.propertiesViewSave = function(){			
		var parames = {
			dpId:$scope.shopId,
			names:["averageDelay", "seriousDelay"],
			values:[$("#averageDelay").val(), $("#seriousDelay").val()],
			groupName:"sendGoods"
		}
		if(parseInt($("#averageDelay").val()) > parseInt($("#seriousDelay").val())){
			$("#averageDelay").css("border","1px solid red");
			$("#seriousDelay").css("border","1px solid red");
			return false;
		}else{
			$("#averageDelay").css("border","1px solid #D9D9D9");
			$("#seriousDelay").css("border","1px solid #D9D9D9");
		}
		$http.post(root+"properties/addBatch",parames).success(function(res){
			if(res.status=="0"){
				$("#properties_position").hide();
				$(".yunat_maskLayer").remove();
			}
		});
	}
	
	$('#properties_position form').validate({	
		submitHandler:function(){
			$scope.propertiesViewSave();	
		}
	});
	
}

function validationwaitDay(str){
	var partten = /^[0-9]*[0-9][0-9]*$/;
	if(partten.test(str) || str==""){
		$("#waitDay").css("border","1px solid #D9D9D9");
		return true;
	}else{
		$("#waitDay").css("border","1px solid red");
		return false;
	}
};



