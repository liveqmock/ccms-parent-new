$(function(){
	$(".ccms_tips").Tips();	
});
function alarmCtr($scope,$http,$location){
	var lastPage="";
	$scope.addMoble = function() {
		
		 var mobile =$('#mobileText').val();
		 var partten = /^0{0,1}(13[0-9]|14[0-9]|15[0-9]|18[0-9])[0-9]{8}$/; 
		 if(partten.test(mobile)){
			   
			 //判断是否已经是添加了的手机号
			 var mobiles = $('.items').text();
			 mobiles=  mobiles.substring(0,mobiles.length-1);
			 var existMobileArray = mobiles.split("×");
			 
			 Array.prototype.contains = function(item){
				    return RegExp(item).test(this);
			 };
			
			//说明手机号已经添加过了
			if(existMobileArray.contains(mobile)){
				$(this).Alert({"title":"提示","str":"该手机号码已经添加过,请重新填写！","width":"160px","mark":true});
				$('#mobileText').val("");
				return false ;
			}
			 
			 
			 $('<div class="items" >'+$('#mobileText').val()+'<span class="close" onClick = "javascript:cancelMobile(this);" style="display:none;">×</span></div>').appendTo($('.additivePhone'));
			 $('#mobileText').val("");
		 }else{
				$(this).Alert({"title":"提示","str":"请填写正确的手机号码","width":"160px","mark":true});
				$('#mobileText').val("");
				return false ;
		}
	};
	
	
	
	$('.additivePhone .items').live('mouseover',function(){
		$(this).find("span").attr("style","display:block");
	});
	
	$('.additivePhone .items').live('mouseout',function(){
		$(this).find("span").attr("style","display:none");  
	});
	
	
	
	cancelMobile = function(obj){
		$(obj).parent().remove();
	};
	
	
	$scope.initMethod = function(shop){
		if($scope.shopName != shop.shopName){
			$scope.shopName = shop.shopName;
			$scope.shopId = shop.shopId;
			$scope.shopType = shop.shopType;
		}
		$('.additivePhone .items').remove();
		$scope.searchWarnRecord($scope.shopId);
	};
	
	
	$scope.searchWarnRecord=function(shopId){
		
		var  warnType; 
		var  urpayType = $scope.urpayType;
		if(urpayType ==1){
			//中差评告警
			warnType  = 30;
			$(".addset").show();
		}else if(urpayType ==2){
			//退款告警
			warnType  = 31;
			$(".addset").hide();
			
		}
		
		 
		
		 $http.get(root+"warn/load?dpId="+shopId+"&warnType="+warnType).success(function(res){
			
			 //查询成功
			 if(res.status=="0"){
				 var data=res.data;
				 
				 // 清理工作
				 $('.additivePhone .items').remove();
				 
				 //第一次查找
				 if(res.data == ""){
					 $scope.openFlag = false;
					 $scope.warnContent = urpayType == 1 ? "【店铺名称】【差评发生时间】收到一个【中/差评】，昵称：【客户昵称】详情：【评价内容】【数据赢家】" 
														 : "您的店铺【店铺昵称】，【时间段】累计收到【退款笔数】笔退款。其中【未完成退款笔数】笔仍未完成，合计 【总金额】 元【数据赢家】";
					 
					 $('#mobileText').attr("readonly", false);
					 $('#addBtn').attr("disabled",false);
					 $("select").prop("disabled", false);
					 
					 
				 }else{
					 $scope.openFlag = data.isOpen;
					 $scope.warnContent = data.content;
					 var mobileArray =  data.warnMobiles.split(",");
					 
					 loadWarnTime(data.warnStartTime,data.warnEndTime);
					  
					 
					 //开启的
					 if($scope.openFlag ==1){
						 //页面所有元素不能编辑
						 $('#mobileText').attr("readonly", true);
						 $('#addBtn').attr("disabled",true);
						 $("select").prop("disabled", true);
						 
						 
						 for(var m =0 ;m<mobileArray.length;m++){
							  $('<div class="items" >'+mobileArray[m]+'<span class="close" onClick = "javascript:cancelMobile(this);"></span></div>').appendTo($('.additivePhone'));
						  }
						 
						 
					 }else{
						 //关闭的-页面所有元素除了短信内容都能编辑
						 $('#mobileText').attr("readonly", false);
						 $('#addBtn').attr("disabled",false);
						 $("select").prop("disabled", false);
						 
						 for(var m =0 ;m<mobileArray.length;m++){
							  $('<div class="items" >'+mobileArray[m]+'<span class="close" onClick = "javascript:cancelMobile(this);" style="display:none;">×</span></div>').appendTo($('.additivePhone'));
						 }
					 }
				}
			 }
			 
		 });
	
	};
	
	
	loadWarnTime = function(warnStartTime,warnEndTime){
		 var urpay_start_time = document.getElementById('urpay_start_time'); 
		 var urpay_end_time = document.getElementById('urpay_end_time'); 
		 
		 for(var i=0;i<urpay_start_time.options.length;i++){  
		    if(urpay_start_time.options[i].innerHTML == parseInt(warnStartTime)+":00"){  
		    	urpay_start_time.options[i].selected = true;  
		     break;  
		     }  
		 }
		 for(var i=0;i<urpay_end_time.options.length;i++){  
			    if(urpay_end_time.options[i].innerHTML == parseInt(warnEndTime)+":00"){  
			    	urpay_end_time.options[i].selected = true;  
			     break;  
			     }  
		 }
	};
	
	
	
	
	$scope.times = [9,10,11,12,13,14,15,16,17,18,19,20,21,22];
	
	$scope.$watch(function(){return $location.search();},function(search){
	    if(search.type == lastPage){
			   return false;
		}
		var urpayType = $scope.urpayType = parseInt(search.type);
		switch(urpayType){
			 case 1:
			    $scope.alarmTitle="中差评告警";
			    $scope.offsetTip2=true;
				$scope.offsetTip1=false;
			 break;
			 case 2:
			    $scope.alarmTitle="退款告警";
			    $scope.offsetTip1=true;
				$scope.offsetTip2=false;
			 break;
		}
		$scope.searchWarnRecord($scope.shopId);
	});
	$scope.showUrPay=true;
    $http.get(root+"shop/taobao/list").success(function(res){
		$scope.shops = res.data.shops;
		$scope.init($scope.shops[0]);
	});
	$scope.init = function(shop){
		if($scope.shopName != shop.shopName){
			$scope.shopName = shop.shopName;
			$scope.shopId = shop.shopId;
			$scope.shopType = shop.shopType;
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
		$scope.searchWarnRecord($scope.shopId);
		
	};
	
	$scope.addZero = function(num){
		return parseInt(num) < 10 ? '0'+num : num;
	};
	
	
	//催付时间，前后时间限制
	$('#urpay_start_time,#urpay_end_time').change(function(){
		var el = this,
			sltVal = parseInt(el.value);
		$(this).siblings().find('option').each(function(){
			var thisVal = parseInt(this.value);
			if(el.id == 'urpay_start_time'){
				$(this).attr('disabled',thisVal <= sltVal ? true : false);
			}else{
				$(this).attr('disabled',thisVal >= sltVal ? true : false);
			}
		});
	});
	
	
	//加载告警开始结束时间
	$scope.urpay_stime = 9;
	$scope.urpay_etime = 22;
	$.each($scope.times,function(i,n){
		$('#urpay_start_time').append(function(){
			var disabledStr = n >= $scope.urpay_etime ? ' disabled="disabled"' : '';
			var sltStr = n == $scope.urpay_stime ? ' selected="selected"' : '';
			return '<option'+disabledStr+sltStr+'>'+$scope.addZero(n)+':00</option>';
		});
		$('#urpay_end_time').append(function(){
			var disabledStr = n <= $scope.urpay_stime ? ' disabled="disabled"' : '';
			var sltStr = n ==  $scope.urpay_etime? ' selected="selected"' : '';
			return '<option'+disabledStr+sltStr+'>'+$scope.addZero(n)+':00</option>';
		});
	});
	
	
	
	
	//进入设置
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
		 };
	};
	
	
	
	
	//点击开启/关闭开关
	
	$scope.saveMethod=function(openFlag){
		var setSwitch;
		
        var mobiles = $('.items').text();
		
		//后台读出的配置告警记录是打开的
        if(openFlag == 1){
			setSwitch  = 0;
			
		}else {
			setSwitch  = 1;
			
			//手机号完整性验证
			if(mobiles == ""){
				$(this).Alert({"title":"提示","str":"手机号码不能为空,请填写手机号码并进行添加","width":"160px","mark":true});
				return false;
			}
		}
		
        String.prototype.replaceAll = function (AFindText,ARepText){
   			raRegExp = new RegExp(AFindText,"g");
   			return this.replace(raRegExp,ARepText);
        };
        
        mobiles=  mobiles.substring(0,mobiles.length-1).replaceAll("×",",");
		
		
		var  warnType; 
		if($scope.urpayType == 1){
			warnType =30;
		} else if ($scope.urpayType == 2){
			warnType =31;
		}
		
		
		var smsParams = {
				'dpId' : $scope.shopId,
				'warnStartTime' : $('#urpay_start_time').val(),
				'warnEndTime' : $('#urpay_end_time').val(),
				//30：中差评告警  31：退款告警
				'warnType' : warnType,
				'content' : $scope.warnContent,
				'mobiles' : mobiles,
				'isOpen' : setSwitch
			};
			$http.post(root+'warn/add',smsParams).success(function(res){
				if(res.status == 0){
					$('.additivePhone .items').remove();
					$(this).Alert({"title":"提示","str":"操作成功","width":"160px","mark":true});
					$scope.searchWarnRecord($scope.shopId);
				}else{
					$(this).Alert({"title":"警告","str":"操作失败","width":"160px","mark":true});
				}
			});
	};
	
	
	
}