function urpay($scope,$http,$location){
	var _urpayST = $('#startDate'),
		_urpayET = $('#endDate'),
		lastPage = '';
	$scope.times = [9,10,11,12,13,14,15,16,17,18,19,20,21,22];
	$scope.$watch(function(){return $location.search();},function(search){
		if(!/order\/urpay\?|order\/care\?/.test($location.url()) || search.type == lastPage){//只有在订单中心的催付和关怀才执行下面的语句（因为从订单中心切换到其他页面或是从其他页面到订单中心页面的时候也会执行，会报错）|| 阻止在当前页面继续点击该页面的链接
			$(window).unbind('resize',setSummaryH);
			return false;
		}
		var urpayType = $scope.urpayType = parseInt(search.type);
		var urpayMinDate = urpayType == 2 ? -3 : '0y 0m 0d';
		switch(urpayType){
			case 1:
				$scope.offsetTip1 = '下单';
				$scope.offsetTip2 = '分钟后开始催付';
				$scope.typeName = '自动催付';
				$scope.tips = '及时对下单未付款订单进行短信催付，有效提升订单付款率，减少销售额流失';
				$scope.offsetMax = 2880;
				break;
			case 2:
				$scope.offsetTip1 = '订单自动关闭';
				$scope.offsetTip2 = '分钟前催付';
				$scope.typeName = '预关闭催付';
				$scope.tips = '在订单即将关闭前提醒买家付款，减少因为客户遗忘带来的订单流失';
				$scope.offsetMax = 1440;
				break;
			case 3:
				$scope.offsetTip1 = '聚划算下单';
				$scope.offsetTip2 = '分钟后催付';
				$scope.typeName = '聚划算催付';
				$scope.tips = '对聚划算订单进行催付或借助催付进行关联营销，提升聚划算售罄率';
				$scope.offsetMax = 25;
				break;
			case 6:
				$scope.typeName = '下单关怀';
				$scope.tips = '客户下单/付款后进行关怀，提升客户体验，也可以通过关联引导提升客户客单价';
				$scope.subName = '下单';
				break;
			case 7:
				$scope.typeName = '发货关怀';
				$scope.tips = '订单发货后即时对客户进行关怀，创造更好体验，提升客户的满意度';
				$scope.subName = '发货';
				break;
			case 8:
				$scope.typeName = '同城关怀';
				$scope.tips = '包裹到达买家城市后即时对客户进行关怀，告知包裹已同城，提升客户的购物体验';
				$scope.subName = '同城';
				break;
			case 9:
				$scope.typeName = '派件关怀';
				$scope.tips = '包裹派件时对客户进行关怀，告知客户准备收件，提升客户的购物体验';
				$scope.subName = '派件';
				break;
			case 10:
				$scope.typeName = '签收关怀';
				$scope.tips = '包裹被签收时对客户进行关怀，引导客户确认收货并给予好评';
				$scope.subName = '签收';
				break;
			case 11:
				$scope.typeName = '退款关怀';
				$scope.tips = '在客户申请的退款成功时，对客户继续关怀和提醒，减少客户因退款产生的不满，提升服务态度的感知，提升DSR';
				$scope.subName = '退款';
				break;
			case 12:
				$scope.typeName = '确认收货关怀';
				$scope.tips = '客户确认收货时进行关怀，引导客户好评和打分,提升店铺DSR';
				$scope.subName = '确认收货';
				break;
		}
		$scope.isUrpay = $scope.urpayType < 6;
		if($scope.isUrpay){
			$scope.orderName = '催付';
			$scope.notifyOptionTt = '超出发送时间';
			$scope.timeName = '下单时间';
			$scope.urpayUrl = 'urpay/urpayConfig';
			$scope.summaryUrl = 'urpay/urpaySummary/urpaySummaryList';
			$scope.downloadUrl = 'urpay/urpaySummary/downloadUrpaySummary';
			$scope.typeVal = 'urpayType';
			$scope.startTimeKey = 'urpayStartTime';
			$scope.endTimeKey = 'urpayEndTime';
		}else{
			$scope.orderName = '关怀';
			$scope.notifyOptionTt = '超出关怀时间';
			$scope.timeName = $scope.subName + '时间';
			$scope.urpayUrl = 'care/careConfig';
			$scope.summaryUrl = 'urpay/urpaySummary/careSummaryList';
			$scope.downloadUrl = 'urpay/urpaySummary/downloadCareSummary';
			$scope.typeVal = 'careType';
			$scope.startTimeKey = 'careStartTime';
			$scope.endTimeKey = 'careEndTime';
		}
		$scope.offsetMin = urpayType == 2 ? 30 : 10;
		
		//首次加载获取店铺
		if(!$scope.shops){
			$http.get(root+"shop/taobao/list").success(function(res){
				$scope.shops = res.data.shops;
				$scope.init(urpayType,$scope.shops[0]);
			});
			
			//初始化订单范围的时间控件
			_urpayST.datetimepicker({
				timeFormat:'HH:mm:ss',
				showSecond:true,
				changeMonth:true,
				changeYear:true,
				minDate:urpayMinDate,
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
				minDate:urpayMinDate,
				onClose: function(dateText) {
					if(dateText){
						_urpayET.datetimepicker('setDate', dateText);
						var startDate = _urpayST.datetimepicker('getDate'),
							endDate = _urpayET.datetimepicker('getDate');
						_urpayST.datetimepicker('option', 'maxDate', dateText);
					}
				}
			});
		}else{
			$scope.init(urpayType,$scope.shop);
			_urpayST.datetimepicker('option', 'minDate', urpayMinDate);
			_urpayET.datetimepicker('option', 'minDate', urpayMinDate);
		}
		lastPage = search.type;
	});	
	
	//当关怀时刻为付款时，编辑器中有付款时间变量的话，清除
	/*
	$scope.$watch(function(){return $scope.careMoment},function(careMoment){
		console.log(careMoment);
	});
	*/
	$scope.careMomentFn = function(value){
		if(value == 0){
			//TODO   IE下有bug
			var editor = $('#mesIframe').contents();
			if($.browser.mozilla){
				editor.find('[alt=付款时间]').remove();
			}else{
				editor.find('span:contains(付款时间)').remove();
			}
		}
	}
	//初始化编辑器
	var kindEditor=kindEditorObj.creatEditor("#textEditor");
	$('.smsVar').bind('click',function(){
		kindEditorObj.editorAddElement(kindEditor,new Date().getTime(),$(this).attr('val'),$(this).attr('orderVal'));
	});
	$scope.init = function(type,shop){
		//if(($scope.shop && $scope.shop.shopName) != shop.shopName){
			$scope.showUrPay = $scope.showSummary = false;
			$scope.shop = shop;
			$scope.exportUrl = root+$scope.downloadUrl+'?urpayType='+type+'&dpId='+shop.shopId;
			var detailUrl = '#/order/urpayLog?dpId='+shop.shopId+'&type='+type;
			//催付配置
			$http.get(root+$scope.urpayUrl+'?dpId='+shop.shopId+'&'+$scope.typeVal+'='+type+'&?_='+new Date().getTime()).success(function(res){
				initUrpay(res);
			});
			
			
			//催付统计
			$http.get(root+$scope.summaryUrl+'?dpId='+shop.shopId+'&urpayType='+type+'&?_='+new Date().getTime()).success(function(res){
				var orderNum30 = orderNum7 = responseNum30 = responseNum7 = sendNum30 = sendNum7 = responseAmount7 = responseAmount30 = 0;
				$('#order_reportList tbody').html('');
				if($scope.isUrpay){
					var summaryList = $scope.summary = res.data.urpaySummaryList;
					$.each(summaryList,function(i,n){
						//计算响应率、ROI 并赋值给数组
						summaryList[i].responsivity = $scope.summary[i].responsivity = getResponsivity(n.responseNum,n.orderNum);
						summaryList[i].roi = $scope.summary[i].roi = getRoi(n.responseAmount,n.sendNum);
						var evenClass = i%2 ? ' class="even"' : '';
						$('#order_reportList tbody').append('<tr'+evenClass+'><td>'+n.urpayDate+'</td><td>'+n.orderNum+'</td><td>'+n.responseNum+'</td><td>'+n.responsivity+'%</td><td>'+n.sendNum+'</td><td>'+n.responseAmount+'</td><td>1:'+n.roi+'</td><td><a style="'+hideDetail(n.sendNum)+'" href="'+getdetailUrl(shop.shopId,type,n.urpayDate)+'">详情</a></td></tr>');

						//计算催付数、响应数、发送短信数的7天和30天的总和
						if(i < 7){
							orderNum7 += n.orderNum;
							responseNum7 += n.responseNum;
							sendNum7 += n.sendNum;
							responseAmount7 += n.responseAmount;
						}
						orderNum30 += n.orderNum;
						responseNum30 += n.responseNum;
						sendNum30 += n.sendNum;
						responseAmount30 += n.responseAmount;
					});
					$scope.orderNum7 = orderNum7;
					$scope.orderNum30 = orderNum30;
					$scope.responseNum7 = responseNum7;
					$scope.responseNum30 = responseNum30;
					$scope.responsivity7 = getResponsivity(responseNum7,orderNum7);
					$scope.responsivity30 = getResponsivity(responseNum30,orderNum30);
					$scope.roi7 = getRoi(responseAmount7,sendNum7);
					$scope.roi30 = getRoi(responseAmount30,sendNum30);

					//初始化图表
					$('#orderNum_chart').initHighcharts('#409b16',summaryList,'orderNum');
					$('#responseNum_chart').initHighcharts('#616161',summaryList,'responseNum');
					$('#responsivity_chart').initHighcharts('#b61113',summaryList,'responsivity');
					$('#roi_chart').highcharts({
						chart: {
							type: 'area',
							width:50,
							height:40,
							margin: [0, 0, 0, 0],
							backgroundColor:''
						},
						title: {text: ''},
						legend: {enabled: false},
						credits: {enabled: false},
						tooltip: {enabled: false},
						yAxis: {gridLineWidth:0},
						plotOptions: {
							area: {
								marker:{
									enabled:false,
								},
								color:'#b1ffa9',
								lineColor:'#47a521',
								lineWidth:1

							}
						},
						series: [{
							data: [summaryList[6].roi, summaryList[5].roi, summaryList[4].roi, summaryList[3].roi, summaryList[2].roi,summaryList[1].roi,summaryList[0].roi],
							dataLabels: {
								enabled: false,
							}
						}]
					});
				}else{
					var summaryList = $scope.summary = res.data.careSummaryList;
					$scope.visit = res.visit;
					$.each(summaryList,function(i,n){
						var evenClass = i%2 ? ' class="even"' : '';
						$('#order_reportList tbody').append('<tr'+evenClass+'><td>'+n.urpayDate+'</td><td>'+n.sendNum+'</td><td><a style="'+hideDetail(n.sendNum)+'" href="'+getdetailUrl(shop.shopId,type,n.urpayDate)+'">详情</a></td></tr>');
						if(i < 7) sendNum7 += n.sendNum;
						sendNum30 += n.sendNum;
					});
					$scope.sendNum7 = sendNum7;
					$scope.sendNum30 = sendNum30;
					$('#sendNum_chart').initHighcharts('#409b16',summaryList,'sendNum');
				}				
				setSummaryH();
				$(window).resize(setSummaryH);
				$scope.showSummary = true;
			});
			
			
		//}
	}
	
	function hideDetail(sendNum){
		return !sendNum ? 'display:none;' : '';
	}
	function getdetailUrl(shopId,type,date){
		return '#/order/urpayLog?dpId='+shopId+'&type='+type+'&startCreated='+date+' 00:00:00&endCreated='+date+' 23:59:59';
	}
	function setSummaryH(){//设置催付统计的高度
		var el = $('#order_reportList');
		el.height($(window).height() - el.offset().top - 2);
	}
	
	//初始化催付配置
	function initUrpay(response){
		//去除错误样式及错误提示
		$('label.error').remove();
		$('input.error,select.error,textarea.error').removeClass('error');
		var data = response.data;
		$scope.pkid= data.pkid;
		$scope.isSwitch = data.isSwitch;
		$scope.created = data.created;
		$scope.urpayData = data;
		$scope.taskType = data.taskType || 1;
		$scope.dateNumber = data.dateNumber || 0;
		$scope.dateType = data.dateType;
		$scope.isOpen = data.isOpen ? 1 : 0;
		$scope.notifyOption = data.notifyOption === '' ? 1 : data.notifyOption;
		$scope.careMoment = data.careMoment === '' ? 0 : data.careMoment;
		$scope.orderMinAcount = data.orderMinAcount == -1 ? '' : data.orderMinAcount;
		$scope.orderMaxAcount = data.orderMaxAcount == -1 ? '' : data.orderMaxAcount;
		$scope.order_acount = data.orderMinAcount == -1 ? true : false;
		$scope.smsContent = strToTag(data.smsContent); 
		
		$scope.offset = data.offset;
		$scope.careStatus = data.careStatus;
		$scope.includeCheap = data.includeCheap === '' ? -1 : data.includeCheap;
		
		//获取短信渠道信息
		$scope.gatewaySucs = false;
		$http.get(root+'gateway/sms_list?_='+new Date().getTime()).success(function(res){
			$scope.gatewayList = res;
			$scope.gateway = data.gatewayId ? ccms.getObjInAry(res, data.gatewayId, "gatewayId") : res[0];
			$scope.gatewaySucs = true;
		}).error(function(){
			$scope.gatewaySucs = true;
		});
		
		//编辑器内容设置
		kindEditor.html($scope.smsContent);

		//催付时间
		if($scope.taskType == 1){
			if(data[$scope.startTimeKey]){
				$scope.urpay_stime = parseInt(data[$scope.startTimeKey],10);
				$scope.urpay_etime = parseInt(data[$scope.endTimeKey],10);
			}else{
				$scope.urpay_stime = $scope.times[0];
				$scope.urpay_etime = $scope.times[$scope.times.length-1];
			}
		}else if($scope.taskType == 2){
			$scope.urpay_stime = $scope.times[0];
			$scope.urpay_etime = $scope.times[$scope.times.length-1];
			$scope.fixUrpayTime = data.fixUrpayTime;
		}
		
		$('#urpay_start_time,#urpay_end_time').html('');
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
		})
		//催付时间 end

		//初始化  若保存的为绝对时间且订单时间有值时，设置起始时间和结束时间的限制
		if(data.dateType == 1){
			var _urpayST = $('#startDate'),
				_urpayET = $('#endDate'),
				startVal = data.startDate,
				endVal = data.endDate;
			_urpayST.datetimepicker('option', 'maxDate',endVal);
			_urpayST.datetimepicker('setDate',startVal);

			_urpayET.datetimepicker('option', 'minDate',startVal);
			_urpayET.datetimepicker('setDate',endVal);
		}
		$scope.showUrPay = true;
		
		var submitFlag = true;
		//催付配置保存
		$('#order_payment').validate({
			rules:{
				orderMinAcount:{minAcountRule:true},
				orderMaxAcount:{maxAcountRule:true}
			},
			submitHandler:function(){
				if(!submitFlag) return false;
				var openMsg = $scope.getText($scope.isOpen),
					kindContent = kindEditorObj.getKindEditorVal(kindEditor.html()),
					errorStr = '';
				if($scope.gateway){
					if($('#startDate').is(':visible') && ccms.getSecond($('#startDate').val()) > ccms.getSecond($('#endDate').val())){
						errorStr += '<p>下单结束时间要大于开始时间</p>';
					}
					if($('#fixUrpayTime').is(':visible') && !$('#fixUrpayTime input:checked').length){
						errorStr += '<p>请至少选择一个催付时间</p>';
					}
					if(!$('[name=memberGrade]:checked').length){
						errorStr += '<p>请至少选择一个会员等级</p>';
					}
					if(!$('#order_acount').attr('checked') && !$.trim($('#orderMinAcount').val()) && !$.trim($('#orderMaxAcount').val())){
						errorStr += '<p>请填写订单金额</p>';
					}
					if($.trim(kindContent) == ''){
						errorStr += '<p>请填写短信内容</p>';
					}
				}else{
					errorStr += '<p>通道信息获取失败，不能'+openMsg+$scope.typeName+'</p>';
				}
				if(errorStr){
					$(this).Alert({"title":"提示","str":errorStr,"mark":true,"width":"190px"});
					return false;
				}
				
				
				//预览及获取实际发送条数
				var smsParams = {
					'content' : ccms.filterTags(kindContent).replace(/\s#.*?#/g,''),
					'gatewayType' : $scope.gateway.gatewayType,
					'wordsLimit' : $scope.gateway.wordsLimit,
					'markLength' : $scope.gateway.markLength,
					'isOrderBack' : 0 ,
					'backOrderInfo' : '',
					'sign' : ''
				};
				$http.post(root+'node/sms/calculate',smsParams).success(function(resp){
					//有变量
					var confirmStr = jhsConfirm = commConfirm = '',
						content = smsParams.content,
						smsLength = resp.length;
					if(!$scope.isOpen){
						if(/<img|<span/.test(content)){
							confirmStr = '<p>短信内容：'+content+'</p><p class="smsTips"><i><font color="red">*本短信包含变量，计费条数以实际发送字数为准！</font></i></p>'
						}else{
							for(var i=1;i<smsLength;i++){
								confirmStr += '<p>第<font color="red">'+i+'</font>条：'+resp[i]+'</p>';
							}
							confirmStr += '<p class="smsTips"><i>*本短信正式发送时共<font color="red">'+resp[0]+'</font>字，按<font color="red">'+(smsLength-1)+'</font>条短信计费!</i></p>'
						}
						if($scope.urpayType == 3){
							jhsConfirm = '自动催付中如选择"包含聚划算"选项，可能会和聚划算催付重复，请';
						}
						if($scope.urpayType >= 6 || $scope.urpayType == 1){
							commConfirm = '<p class="pop_highbox">由于订单中心是原来事件活动的升级版本，开启该活动之前请确认同类活动在事件活动中是否已经关闭，如果未关闭，请关闭后再回订单中心操作。</p>';
						}
					}
					$(this).Confirm({"title":"确认","str":commConfirm+confirmStr+"<p style='margin-bottom:10px;'>"+jhsConfirm+"确认要"+openMsg+$scope.typeName+"吗？</p>","mark":true},function(){
						var dateType = $('#dateType').attr('checked') ? 1 : 0,
							orderAmount = $('#order_acount').attr('checked'),
							taskType = $.parseInt($scope.taskType);
						var params = {
							dpId : $scope.shop.shopId,
							pkid : $scope.pkid,
							notifyOption : ($scope.urpayType == 2 || $scope.urpayType == 3 || $scope.urpayType == 6) ? 0 : $.parseInt($('[name=notifyOption]:checked').val()),
							dateType : dateType,
							dateNumber : dateType == 0 ? $.parseInt($('#dateNumber').val()) : '',
							startDate : dateType == 1 ? $('#startDate').val() : '',
							endDate : dateType == 1 ? $('#endDate').val() : '',
							filterCondition : getArySty($('[name=filterCondition]:checked')),
							memberGrade : getArySty($('[name=memberGrade]:checked')),
							orderMinAcount : orderAmount ? -1 : parseFloat($('#orderMinAcount').val()),
							orderMaxAcount : orderAmount ? -1 : parseFloat($('#orderMaxAcount').val()),
							isOpen:$scope.isOpen == 0 ? 1 : 0,
							isSwitch : $scope.isSwitch,
							created : $scope.created,
							excludeGoods : '',
							goods : '',
							gatewayId : $scope.gateway.gatewayId,
							smsContent : (function(){
								var str = '';
								//变量替换
								if($.browser.mozilla){
									str = kindContent.replace(/<.*?(#.*?#).*?\/>/g,'$1');
								}else{
									str = kindContent.replace(/<.*?(#.*?#).*?<\/span>(&nbsp;|)/g,'$1');
								}
								return ccms.filterTags(str);
							})()
						};
						if($scope.isUrpay){
							params = $.extend({
								urpayType : $scope.urpayType,
								taskType : taskType,
								fixUrpayTime : taskType == 2 ? getArySty($('#fixUrpayTime input:checked')) : '',
								urpayStartTime : taskType == 1 ? $('#urpay_start_time :selected').html()+':00' : '',
								urpayEndTime : taskType == 1 ? $('#urpay_end_time :selected').html()+':00' : '',
								offset : $.parseInt($scope.offset),
								includeCheap : (function(){
									switch($scope.urpayType){
										case 1:
											return $('#includeCheap').attr('checked') ? 1 : 0;
										case 2:
											return 0;
										case 3:
											return '';
									}
								})()
							},params);
						}else{
							params = $.extend({
								careType : $scope.urpayType,
								careStartTime : $('#urpay_start_time :selected').html()+':00',
								careEndTime : $('#urpay_end_time :selected').html()+':00',
								careStatus : $scope.careStatus,
								includeCheap : $.parseInt($scope.includeCheap)
							},params);
							if($scope.urpayType == 6){
								params = $.extend({careMoment: parseInt($scope.careMoment)},params);
							}
						}
						
						var myTips = yunatPop.tips.show('<div class="loading"><i></i>'+$scope.typeName+'配置'+openMsg+'中...</div>');
						$http.post(root+$scope.urpayUrl,params).success(function(res){
							if(res.status == 0){
								initUrpay(res);
								$(this).Alert({"title":"提示","str":openMsg+$scope.typeName+"成功","width":"160px","mark":true});
							}else{
								$(this).Alert({"title":"警告","str":res.message,"width":"160px","mark":true});
							}
							yunatPop.tips.hide(myTips);
						});
					});
					submitFlag = true;
				});
				submitFlag = false;
			}
		});
	}
	
	function getRoi(responseAmount,sendNum){
		return sendNum ? Math.round(responseAmount/(sendNum*0.05)) : 0;
	}
	function getResponsivity(responseNum,orderNum){
		return orderNum ? parseFloat((100*responseNum/orderNum).toFixed(2)) : 0;
	}
	function getArySty(obj){
		var str = '';
		obj.each(function(){
			if(!this.disabled){
				str += this.value + ',';
			}
		});
		str = str.replace(/\,$/,'');
		return str;
	}
	//短信内容转化为标签
	function strToTag(str){
		if(jQuery.browser.mozilla){
			str = str.replace(/#(.*?)#/g,function(){
				return '<img id="'+new Date().getTime()+'" class="varImg '+arguments[0]+'" src="" alt="'+$('[tVal='+arguments[1]+']').attr('val')+'" />';
			});
		}else{
			str = str.replace(/#(.*?)#/g,function(){
				return "<span id='"+new Date().getTime()+"' class='iframeButton "+arguments[0]+"'>"+$('[tVal='+arguments[1]+']').attr('val')+"</span>&nbsp;"
			});
		}
		return str;
	}
	
	$.parseInt = function(num){
		return num ? parseInt(num) : num;
	}	
	
	$scope.getText = function(type){
		return type ? '关闭' : '开启';
	}
	$scope.addZero = function(num){
		return parseInt(num) < 10 ? '0'+num : num;
	}
	$scope.formatDate = function(time){
		var d = new Date(time),z = $scope.addZero;
		return d.getFullYear()+'-'+z(d.getMonth()+1)+'-'+z(d.getDate())+' '+z(d.getHours())+':'+z(d.getMinutes())+':'+z(d.getSeconds());
	}
	$scope.round = function(num){
		return Math.round(num);
	}
	$scope.increment = function(m,n){
		var result = Math.round(100*(m-n)/n);
		return n == 0 ? '0%' : (result > 0 && '+')+result+'%';
	}
	$scope.inArray = function(value,aryStr){
		var ary = aryStr.split(',');
		return $.inArray(value,ary) >= 0;
	}
}
(function($){
	$(".ccms_tips").Tips();
	$.fn.extend({
		"initHighcharts":function(color,ary,key){//初始化 highcharts
			$(this).highcharts({
				chart: {
					type: 'column',
					width:51,
					height:36,
					margin: [0, 0, 0, 0],
					backgroundColor:'#f5f5f5'
				},
				title: {text: ''},
				legend: {enabled: false},
				credits: {enabled: false},
				tooltip: {enabled: false},
				yAxis: {gridLineWidth:0},
				plotOptions: {
					column: {
						pointPadding: 0,
						groupPadding:0,
						color:color,
						plotShadow: false
					}
				},
				series: [{
					data: [ary[6][key],ary[5][key],ary[4][key],ary[3][key],ary[2][key],ary[1][key],ary[0][key]],
					dataLabels: {
						enabled: false,
					}
				}]
			});
		}
	});
	$.validator.addMethod("minAcountRule", function(value, element, params) {
		return this.optional(element) || value >= 0;
	}, '订单最小金额必须大于等于0');
	$.validator.addMethod("maxAcountRule", function(value, element, params) {
		return this.optional(element) || value > (parseFloat($('#orderMinAcount').val()) || 0);
	}, '订单最大金额必须大于最小金额');

	window.disabledInput = function(obj){
		if(obj.value == -1){
			$('[name='+obj.name+']').not(obj).attr({'disabled':obj.checked,'checked':false});
		}
	}	
	
	$('.data_monitoring .area').hover(function(){
		$('.popover',this).show();
	},function(){
		$('.popover',this).hide();
	});

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
})(jQuery);