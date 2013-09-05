function logisticsCtrl($scope,$http){
	var _startDate = $('#consignStartTime'),
		_endDate = $('#consignEndTime');


	//获取店铺
	$http.get(root+"shop/taobao/list").success(function(res){
		$scope.shops = res.data.shops;

		//获取快递公司
		$http.post(root+'properties/getArray',{name: "courierCompany"}).success(function(res){
			$scope.companyNames = res.data;
		});
		$scope.reset();
		$scope.init($scope.shops[0],'');
	});
	//获取省份
	$http.get(root+'meta/dic/10/value').success(function(res){
		$scope.receiverStates = res;
	});

	$scope.reset = function(){
		$scope.minInTransitDuration = '2';
		$scope.isHide = 'false';
		$scope.hideStatus = $scope.isCare = $scope.companyName = $scope.tid = $scope.customerno = $scope.consignStartTime = $scope.consignEndTime =  $scope.receiverState = $scope.shippingStatus = $scope.outSid = $scope.abnormalStatus = $scope.delayTids = '';
		_startDate.val('').datetimepicker('option', 'maxDate',null);
		_endDate.val('').datetimepicker('option', 'minDate',null);
		$('#abnormalStatus a').eq(0).addClass('cur').siblings().removeClass('cur');
	}

	$scope.changeShop = function(shop){
		$scope.reset();
		$scope.init(shop,'');
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
	
	$scope.init = function(shop,abnormalStatus){
		$scope.shop = shop;
		$scope.abnormalStatus = abnormalStatus;

		$scope.checkIsPager(arguments,$scope,1,20);
		var params = {
			dpId: $scope.shop.shopId,
			minInTransitDuration: $scope.minInTransitDuration,
			isCare : $scope.isCare,
			companyName: $scope.companyName,
			tid: $scope.tid,
			customerno: $scope.customerno,
			consignStartTime: $('#consignStartTime').val(),
			consignEndTime: $('#consignEndTime').val(),
			isHide: $scope.isHide,
			receiverState: $scope.receiverState,
			shippingStatus: $scope.shippingStatus,
			outSid : $scope.outSid,
			abnormalStatus : $scope.abnormalStatus,
			currentPage: $scope.defaultPage,
			pageSize: $scope.defaultSize
		};

		$scope.showLogistics = false;
		$http.post(root+'customerCenter/logistics/logisticsList',params).success(function(res){
			var data = res.data;
			$scope.logisticsList = data.data;
			$scope.hideStatus = $scope.isHide;

			$scope.pagerSetting = {
				curpage: data.page,
				total: data.total,
				totalPages: data.totalPage,
				rp: data.pageSize,
				submit: function(){
					$scope.init(shop,$scope.abnormalStatus,'isPager');
				}
			}
			$scope.showLogistics = true;
		});
	}

	//ccms.fixedPager($('#comm_list'));

	_startDate.datetimepicker({
		timeFormat:'HH:mm:ss',
		showSecond:true,
		changeMonth:true,
		changeYear:true,
		onClose: function(dateText){
			if(dateText){
				_startDate.datetimepicker('setDate', dateText);
				var startDate = _startDate.datetimepicker('getDate'),
					endDate = _endDate.datetimepicker('getDate');
				_endDate.datetimepicker('option', 'minDate', dateText);
			}
		}
	});
	_endDate.datetimepicker({
		timeFormat:'HH:mm:ss',
		showSecond:true,
		changeMonth:true,
		changeYear:true,
		onClose: function(dateText) {
			if(dateText){
				_endDate.datetimepicker('setDate', dateText);
				var startDate = _startDate.datetimepicker('getDate'),
					endDate = _endDate.datetimepicker('getDate');
				_startDate.datetimepicker('option', 'maxDate', dateText);
			}
		}
	});

	$scope.careClass = function(isCare){
		return isCare ? 'comm_btn comm_btn_hover' : 'comm_btn comm_btn_active';
	}
	$scope.careType = function(type){
		if(type == 1){
			return '短信';
		}else if(type == 2){
			return '旺旺';
		}else if(type == 3){
			return '电话';
		}
	}
	//已发货时间
	$scope.shippedTime = function(consignTime, serverTime){
		var differTime = ccms.getDifferTime(consignTime, serverTime);
		return differTime.d + '天 ' + differTime.h+'小时';
	}
	
	//关怀配置
	function getSltSize(){
		return $('#comm_list tbody tr :checked').length;
	}
	function getSltItem(){
		var result = [],arg = arguments;
		$('#comm_list tbody tr :checked').each(function(){
			var parent = $(this).parents('tr');
			var obj = {}
			$.each(arg,function(i,n){
				obj[n] = parent.find('.'+n).text();
			});
			result.push(obj);
		});
		return result;
	}
	
	$scope.scope = $scope;
	$scope.careUrl = 'customerCenter/logistics/careLogisticsOrders';
	$scope.careTitle = '物流关怀设置';
	$scope.careTypeName = '关怀';
	$scope.careTypes = ["21","22","23"];
	$scope.careSuccess = function(){
		return $scope.init($scope.shop, $scope.abnormalStatus);
	}
	$scope.massCare = function(){
		/*var massData = [];
		$('#comm_list tbody tr').each(function(){
			if($(':checkbox',this).attr('checked')){
				massData.push({uid: $('.uid',this).text(), tel: $('.tel',this).text(), tid: $('.tid',this).text()});
			}
		});
		*/
		if(!getSltSize()){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要关怀的订单","mark":true});
			return false;
		}
		$scope.careSetting($scope, getSltItem('uid', 'tel', 'tid'));
	}
	
	function hideFn(params, successStr, url){
		$http.post(root+url, params).success(function(res){
			if(res.status == '0'){
				$(this).Alert({"title":"提示","str":successStr,"mark":true});
				$scope.init($scope.shop, $scope.abnormalStatus);
			}
		});
	}
	//隐藏
	$scope.hideItem = function(tid, isHide){
		var str = isHide ? '取消隐藏订单成功' : '隐藏订单成功',
			params = {'tid': tid,'hideColumnName': 'logistics_hide'}
		hideFn(params, str, 'customerCenter/orders/hiddenOrder');
	}
	//批量隐藏
	$scope.hideAllItem = function(){
		var hideVal = $scope.hideStatus == 'true' ? false : true,
			tidObj = getSltItem('tid'),
			tids = [],
			str = $scope.hideStatus == 'true' ? '取消隐藏' : '隐藏';
		if(!getSltSize()){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要"+str+"的订单","mark":true});
			return false;
		}
		$.each(tidObj,function(i,n){
			tids.push(n.tid);
		});
		hideFn({'tids': tids, isHide: hideVal, 'hideColumnName':'logistics_hide'},'批量'+str+'订单成功', 'customerCenter/orders/hiddenOrders');
	}
	//关闭延长收货 弹出框
	$scope.closeDelay = function(){
		$('#delay_pop .close').click();
	}
	//延长收货  弹出框
	$scope.delayTime = function(tid){
		$scope.delayTidsP = [];
		if(tid){//单个
			$scope.delayTids = tid + ';';
			$scope.delayTidsP.push(tid);
		}else{//批量
			if(!getSltSize()){
				$(this).Alert({"title":"提示","str":"请至少选择一条需要延长收货的订单","mark":true});
				return false;
			}
			$.each(getSltItem('tid'),function(i,n){
				$scope.delayTids += n.tid + ';';
				$scope.delayTidsP.push(n.tid);
			});
		}
		$('#delay_pop').addInteractivePop({magTitle: '延长收货设置(同步到淘宝后台)', mark :true,drag: true,position:"fixed"});
	}
	//延长收货 保存
	$scope.saveDelay = function(){
		$http.post(root + 'customerCenter/logistics/extensionTimes', {'dpId': $scope.shop.shopId, tids: $scope.delayTidsP, extensionDays: $('#extensionDays').val()}).success(function(res){
			if(res.status == '0'){
				$scope.closeDelay();
				$scope.init($scope.shop, $scope.abnormalStatus);
				$(this).Alert({"title":"提示","str":res.message,"mark":true});
			}else{
				$(this).Alert({"title":"错误","str":res.message,"mark":true});
			}
		});
	}
	
	
	//疑难件配置 查询
	$scope.abnormal = {
		index:0,
		show: function(){
			$('#difficult_pop').addInteractivePop({magTitle: '疑难件智能判断设置',mark :true,drag: true,position:"fixed"});
			$('#abnormal_wrap').html('');
			$scope.showAbnormal = false;
			$http.post(root+'properties/getGroup',{'dpId': $scope.shop.shopId, 'groupName': 'abnormalLogistics'}).success(function(res){
				$scope.abnormalUpdateTime = '';
				var data = res.data;
				
				if(data.length == 0){
					$scope.abnormal.creat({});
				}else if(data.length == 1){
					$scope.abnormalUpdateTime = data[0].value;
					$scope.abnormal.creat({});
				}else{
					$scope.abnormalUpdateTime = data[0].value;
					$.each(data,function(i,n){
						if(i > 0){
							$scope.abnormal.index = i-1;
							$scope.abnormal.creat(n, $scope.abnormal.index ,true);
						}
					});
				}
				
				$scope.showAbnormal = true;
			});
		},
		creat:function(obj, i, saved){
			var name = obj.name || '',
				value = obj.value || '',
				index = i || 0,
				savedClass = saved ? ' saved' : '',
				btnStr = index ? '<a href="javascript:void(0);" class="delbtn"></a>' : '<a class="addBtn" href="javascript:void(0);"><span></span>添加</a>';
				html = '<p class="abnormal_item'+index+savedClass+'">发往<select class="borderHighlight" name="province'+index+'"><option value=""></option>'+$scope.abnormal.getOptions(name)+'</select>&nbsp;'+
					'的包裹发货<input type="text" size="4" value="'+value+'" min="1" max="1000000000" class="borderHighlight digits" name="re_time'+index+'" /> &nbsp;天未签收判断为疑难件'+btnStr+'</p>';
			$('#abnormal_wrap').append(html);
			$('.abnormal_item'+index).find('a').bind('click',function(){
				if(!index){//添加
					$scope.abnormal.index += 1;
					$scope.abnormal.creat({},$scope.abnormal.index);
				}else{//删除
					/*
					var _p = $('.abnormal_item'+index);
					if(_p.hasClass('saved')){
						$http.post(root+'properties/deletePropertiesByName',{'name':_p.find('select').val()}).success(function(res){
							if(res.status == '0'){
								_p.remove();
							}else{
								$('#hasEchoTip').html('删除失败').css('visibility','visible');
							}
						});
					}else{
						_p.remove();
					}
					*/
					$('.abnormal_item'+index).remove();
				}
			});
		},
		getOptions:function(name){
			var options = '';
			$.each($scope.receiverStates,function(i,n){
				var sltStr = n.name == name ? ' selected="selected"' : '';
				options += '<option value="'+n.name+'"'+sltStr+'>'+n.name+'</option>'
			});
			return options;
		},
		save:function(){
			var params = {
				names: [],
				values: [],
				groupName: 'abnormalLogistics',
				dpId : $scope.shop.shopId
			},hasEcho = false;
			
			if($('[name=update_time]').val()){
				params.names.push('abnormalDays');
				params.values.push($('[name=update_time]').val());
			}
			
			$('#abnormal_wrap p').each(function(){
				var sltVal = $('select',this).val(),
					txtVal = $('input',this).val();
				if($.inArray(sltVal, params.names) >= 0){
					hasEcho = true;
					$('select',this).addClass('error');
				}
				if(sltVal && !txtVal){
					$('input',this).addClass('error');
				}
				if(txtVal && !sltVal){
					$('select',this).addClass('error');
				}
				if(txtVal){
					params.names.push(sltVal);
					params.values.push(txtVal);
				}
				
			});
			if(hasEcho){
				$('#hasEchoTip').html('省份中不能有相同的值，请修改').css('visibility','visible');
				return false;
			}else{
				$('#hasEchoTip').html('').css('visibility','hidden');
			}
			if($('#abnormal_wrap input.error,#abnormal_wrap select.error').length) return false;
			
			
			$http.post(root+'properties/replaceBatch', params).success(function(res){
				if(res.status == '0'){
					$('#difficult_pop .title .close').click();
					$(this).Alert({"title":"提示","str":"疑难件智能判断设置成功","mark":true});
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true});
				}
			});
		}
	}
	//疑难件配置 保存
	$('#difficult_pop form').validate({
		submitHandler:function(){
			$scope.abnormal.save();
		}
	});
}

$(function(){
	$('#abnormalStatus a').live('click',function(){
		$(this).addClass('cur').siblings().removeClass('cur');
	});
});