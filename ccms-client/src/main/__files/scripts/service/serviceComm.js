var careKindEditor;
function serviceCtrl($scope, $http ,$compile){
	var submitFlag = true;
	$scope.close_care_pop = function(){
		$('#service_care_pop .close').click();
	}
	
	$scope.save_care_pop = function(scope){
		if(!submitFlag) return false;
		var smsContent = ccms.filterTags(kindEditorObj.getKindEditorVal(careKindEditor.html())),
			content = '';
			
		if($scope.careType == '0'){
			if($scope.tels == ''){
				$(this).Alert({"title":"提示","str":"没有可"+scope.careTypeName+"的手机号码","mark":true,"width":"160px"});
				return false;
			}
			if(smsContent == ''){
				$(this).Alert({"title":"提示","str":"请填写短信内容","mark":true,"width":"160px"});
				return false;
			}
			if(!$scope.gatewayList.gatewayId){
				$(this).Alert({"title":"提示","str":"短信通道信息获取失败，不能提交","mark":true,"width":"160px"});
				return false;
			}
			content = smsContent;
		}else if($scope.careType == '1'){
			content = $.trim($scope.wwNote) || '已经通过旺旺'+$scope.careTypeName;
		}else if($scope.careType == '2'){
			content = $.trim($scope.telNote) || '已经通过电话'+$scope.careTypeName;
		}
		var params = {
			dpId: $scope.cdpId,
			tids: $scope.tids,
			content: content,
			caringType: scope.careTypes[parseInt($scope.careType)],
		}
		if($scope.careType == '0'){
			params = $.extend({
				gatewayId: $scope.gatewayList.gatewayId,
				filterBlacklist: $scope.filterBlacklist
			},params);
		}
		if($scope.oids.length){
			params.oids = $scope.oids;
		}
		
		$http.post(root+scope.careUrl, params).success(function(res){
			if(res.status == '0'){
				scope.careSuccess();
				$scope.close_care_pop();
				$(this).Alert({"title":"提示","str":scope.careTypeName+"成功","mark":true,"width":"160px"});
			}else{
				$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
			}
			submitFlag = true;
		});
		submitFlag = false;
	}
	
	$scope.careSetting = function(scope,data,dpId){
		if(dpId){
			$scope.cdpId = dpId;
		}else{
			$scope.cdpId = undefined;
		}
		var htmls = '<div id="service_care_pop" style="display:none;">'+
			'<table class="SMS_Table">'+
				'<tr><th valign="top">{{careTypeName}}买家：</th><td><textarea readonly="readonly" ng-model="uids"></textarea></td></tr>'+
				'<tr><th valign="top"></th><td><label><input value="0" ng-model="careType" class="vm mr5" type="radio" name="careType" />短信{{careTypeName}}</label><label class="ml20"><input ng-model="careType" value="1" class="vm mr5" name="careType" type="radio"/>旺旺{{careTypeName}}</label><label class="ml20"><input ng-model="careType" class="vm mr5" type="radio" name="careType" value="2" />电话{{careTypeName}}</label></td></tr>'+
				'<tbody ng-show="careType==1"><tr><th valign="top">{{careTypeName}}备注：</th><td><p class="kf_rule mb5">旺旺{{careTypeName}}必须先手动点击旺旺图标对买家进行{{careTypeName}}后在此处进行备注。</p><textarea ng-model="wwNote" placeholder="已经通过旺旺{{careTypeName}}"></textarea></td></tr></tbody>'+
				'<tbody ng-show="careType==2"><tr><th valign="top">{{careTypeName}}备注：</th><td><p class="kf_rule mb5">电话{{careTypeName}}必须买家自己先进行电话沟通后在此处进行备注。</p><textarea placeholder="已经通过电话{{careTypeName}}" ng-model="telNote" ></textarea></td></tr></tbody>'+
				'<tbody ng-show="careType==0"><tr class="SMS_cuifu"><th valign="top">{{careTypeName}}号码：</th><td><textarea id="mobiles" readonly="readonly" ng-model="tels"></textarea></td></tr>'+
				'<tr class="SMS_cuifu"><th valign="top"><span>*</span> 短信内容：</th><td><textarea class="mh100" id="comm_care_editor" name="comm_care_editor"></textarea></td></tr>'+
				'<tr class="SMS_cuifu"><th></th><td><span class="fl">已输入<strong id="smsCont" class="mseLen">0</strong>字</span>  <span class="fr"><input ng-model="filterBlacklist" class="vm mr5" type="checkbox" />屏蔽黑名单</span></td></tr>'+
				'<tr class="SMS_cuifu"><th></th><td><p class="SMS_desc">{{gatewayList.notice}}</p></td></tr>'+
				'<tr class="SMS_cuifu"><th valign="top">短信通道：</th><td>'+
				'<select style="min-width:100px;" class="mr10 borderHighlight" ng-model="gatewayList" ng-options="c.gatewayName for c in gateway"></select><span ng-show="gateway">余额<strong>{{floor2(gatewayList.gatewayBalance)}}</strong>元</span><span ng-show="gatewaySucs && !gateway.length" style="color:red;">通道信息获取失败</span></td></tr></tbody>'+
				'<tr><th></th><td><div class="cc_btn">'+
						'<button ng-click="save_care_pop(cScope, cdpId)" class="btn btnBlue">确定</button><button ng-click="close_care_pop();" class="btn">取消</button>'+
				'</div></td></tr>'+
			'</table>'+
		'</div>';
		
		if(!$('#service_care_pop').length){
			$('.kf_wrap').append(htmls);
			$compile($('#service_care_pop').contents())($scope);
			careKindEditor = kindEditorObj.creatEditor("#comm_care_editor");
		}else{
			careKindEditor.html('');
		}
		$('#service_care_pop').addInteractivePop({magTitle: scope.careTitle,mark :true,drag: true,position:"fixed"});
		
		//获取短信通道
		$scope.gatewaySucs = false;
		$http.get(root+"gateway/sms_list?_="+new Date().getTime()).success(function(res){
			$scope.gateway = res;
			$scope.gatewayList = $scope.gateway[0] || {};
			$scope.gatewaySucs = true;
		}).error(function(){
			$scope.gatewaySucs = true;
		});
		
		$scope.cScope = scope;
		$scope.uids = $scope.tels = $scope.wwNote = $scope.telNote = '';
		$scope.tids = [];
		$scope.oids = []
		$scope.careType = '0';
		$scope.careTypeName = scope.careTypeName;
		$scope.filterBlacklist = true;
		$.each(data,function(i,n){
			if(n.uid) $scope.uids += n.uid + ';';
			if(n.tel) $scope.tels += n.tel + ';';
			if(n.tid) $scope.tids.push(n.tid);
			if(n.oid) $scope.oids.push(n.oid);
		});
		
	}
	
	
	$scope.statusAry = ['待处理', '处理中', '已解决', '已关闭'];
	//绑定旺旺子帐号
	$scope.bindSubuser = {
		checkedUser: false,
		checkBindUser: function(type){
			if(type){
				$scope.checkedTip = '修改';
			}else{
				$scope.checkedTip = '绑定';
				this.show($scope.checkedTip);
			}
		},
		show: function(str){
			var _subUsers = $('#subUsers');
			if(!_subUsers.find('option').length){
				$.each($scope.customerServiceAry, function(i ,n){
					_subUsers.append('<option value='+n.nick+'>'+n.nick+'</option>');
				});
			}
			_subUsers.find('option[value="'+$scope.subUser+'"]').attr('selected', true);
			$('#bindSubuser').addInteractivePop({magTitle:str+"旺旺子账号",mark:true,drag:true,position:"fixed"});
		},
		save: function(){
			var _this = this,
				userName = $('#subUsers').val();
			$http.post(root + 'taobaosubuser/saveBindSubuser' ,{taobaoSubuser: userName, dpId: $scope.commShopId}).success(function(res){
				if(res.status == '0'){
					_this.close();
					$scope.subUser = userName;
					_this.checkedUser = true;
					$(this).Alert({"title":"提示","str":"旺旺子帐号"+$scope.checkedTip+"成功","mark":true,"width":"160px"});
					_this.checkBindUser(true);
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
			});
		},
		close: function(){
			$('#bindSubuser .close').click();
		}
	}

	//新增事务跟进
	$scope.addAffair = {
		sourceNameAry: ['未付款','发货','物流','退款','评价'],
		init: function(){		
			$scope.importantAry = [{'name':'高', 'value':3},{'name':'中', 'value':2},{'name':'低', 'value':1}];
		},
		getSourceType: function(shopId, source){
			$http.post(root + 'category/findByParentId' ,{parentId: source.id, outId: shopId, outType: 'affairType'}).success(function(res){
				if(res.status == '0'){
					$scope.sourceTypeAry = res.data;
					
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
			});
		},
		show: function(opts){// tid 主订单; oid 子订单; buyerNick 买家昵称; oid  子订单; callback:回调函数名,params为参数
			this.opts = opts;
			//时间组件
			var _time = $('#expirationTime'),
				newTime = new Date(),
				newTimeTom = newTime.getDate()+1,
				sourceId = opts.sourceId;
			if(!_time.hasClass('hasDatepicker')){
				_time.datetimepicker({timeFormat: 'HH:mm:ss', showSecond: true, changeMonth: true, changeYear: true});
			}
			newTime.setDate(newTimeTom);
			_time.datetimepicker('setDate', newTime);
		
			var el = $scope.bindSubuser;
			//获取店铺客服列表			
			var shopObj = $('#shopinfo span'),
				shopId = shopObj.attr('id');
			$http.post(root + 'taobaosubuser/simpleSubuserList', {nick: shopObj.text(), shopId: shopObj.attr('id')}).success(function(res){
				if(res.status == '0'){
					$scope.customerServiceAry = res.data;
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
			});
			//获取来源分类
			if(!$scope.sourceTypeAry){
				this.getSourceType(shopId, {'id': sourceId});
			}
			
			//判断是否需要验证登录用户是否绑定旺旺子账号
			$http.get(root+ 'taobaosubuser/checkIfBindSubuser?dp_id=' + shopId + '&_=' + new Date().getTime()).success(function(res){
				if(res.status == '0'){
					el.checkedUser = true;
					$scope.subUser = res.message;
				}else{
					el.checkedUser = false;
					$scope.subUser = '您还没有绑定旺旺子帐号';
				}
				el.checkBindUser(el.checkedUser);
			});

			
			$scope.sourceId = sourceId;
			$scope.sourceName = this.sourceNameAry[sourceId -2];
			$scope.commShopId = shopId;
			$('#addAffair').addInteractivePop({magTitle:"新增事务跟进",mark:true,drag:true,position:"fixed"});
		},
		save: function(){
			var _this = this,
				opts = _this.opts,
				params = {
					dpId: $scope.commShopId,
					tid: opts.tid,
					oid: opts.oid,
					status: 1,
					customerno: opts.buyerNick,
					founder: $scope.subUser,
					title: $('#addAffairTitle').val(),
					important: $('#important').val(),
					currentHandler: $('#customerService').val(),
					expirationTime: $('#expirationTime').val(),
					note: $('#addAffairNote').val(),
					sourceId: $scope.sourceId + '',
					source: $scope.sourceName,
					sourceTypeId: $('#sourceType').val(),
					sourceType: $('#sourceType :selected').html()
				};
			if(!$scope.bindSubuser.checkedUser){
				$(this).Alert({"title":"提示","str":"请绑定旺旺子账号后再提交","mark":true,"width":"160px"});
				return false;
			}
			if(!$.trim(params.title)){
				$(this).Alert({"title":"提示","str":"请填写事务标题","mark":true,"width":"160px"});
				return false;
			}
			if(!$.trim(params.currentHandler)){
				$(this).Alert({"title":"提示","str":"请选择处理人","mark":true,"width":"160px"});
				return false;
			}
			$http.post(root + 'affair/add', params).success(function(res){
				if(res.status == '0'){
					_this.close();
					if(opts.callback){
						opts.callback.apply(null, opts.callbackParam);
					}
					$(this).Alert({"title":"提示","str":"新增事务成功","mark":true,"width":"160px"});
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
			});
		},
		close: function(){
			$("#addAffair .title .close").click();
			$('#addAffairTitle, #addAffairNote').val('');
			$('#addAffair').find('select').each(function(){
				$(this).find('option').eq(0).attr('selected', true);
			});
		}
	}
	$scope.addAffair.init();
	
	//编辑备注
	$scope.addRemark = {
		tid: '',
		show: function(tid){
			this.tid = tid;
			$("#addRemark").addInteractivePop({magTitle:"编辑备注",mark:true,drag:true,position:"fixed"});
		},
		save: function(){
			var flagVal = $(':radio[name="remark"]:checked').val();
			var remarkCont = $("#remarkCont").val();
			var shopObj = $('#shopinfo span');
			var params = {
							tid : this.tid,
							memo : remarkCont,
							flag : flagVal,
							shopId : shopObj.attr('id')
						};	
			$http.post(root+"affair/tradememo/add",params).success(function(res){
				if(res.status=="0"){
					$("#addRemark .title .close").click();
				}
			});
		},
		close: function(){
			$("#addRemark .title .close").click();
		}
	}
	
}
$(function(){
	$(".searchbox_condition tr:eq(1)").hide();
	$(".unfold").toggle(function(){
		$(".searchbox_condition tr:eq(1)").show();
		$(this).addClass("shrink")
	},function(){
		$(".searchbox_condition tr:eq(1)").hide();
		$(this).removeClass("shrink");
	});
});