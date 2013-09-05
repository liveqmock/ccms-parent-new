function myaffair($scope,$http, $location){
	$scope.affairFilter = [
		{"showName": "日期 由新到旧", "name":"created", "value": "desc"},
		{"showName": "日期 由旧到新", "name":"created", "value": "asc"},
		{"showName": "重要性 由低到高", "name":"important", "value": "asc"},
		{"showName": "重要性 由高到低", "name":"important", "value": "desc"},
		{"showName": "处理进度 由未到已", "name":"status", "value": "asc"},
		{"showName": "处理进度 由已到未", "name":"status", "value": "desc"},
	];
	
	$scope.init = function(shop, affairId){
		var shopId = shop.shopId;
		//加载 处理人(客服)
		$http.post(root+'taobaosubuser/simpleSubuserList', {nick: shop.shopName, shopId: shopId}).success(function(resp){
			$scope.customerService = resp.data;
		}).error(function(){
			$(this).Alert({"title":"提示","str":"客服列表加载失败，请再次刷新页面","mark":true,"width":"160px"});
		});
		//加载来源
		$http.post(root+'category/getCategoryListWithChildren', {parentId:0 ,outId: shopId ,outType: 'affairType'}).success(function(res){
			$scope.affairSource = res.data;
		});
		//判断是否需要验证登录用户是否绑定旺旺子账号
		$http.get(root+ 'taobaosubuser/checkIfBindSubuser?dp_id=' + shopId + '&_=' + new Date().getTime()).success(function(res){
			if(res.status == '0'){
				$scope.subUserName = res.message;
			}else{
				$scope.subUserName = null;
			}		
		});
		$scope.findAffairs(shop, affairId);
	}
	//加载事务列表
	$scope.findAffairs = function(shop, affairId){
		$scope.shop = shop;
		var params = {
			dpId: shop.shopId,
			id: affairId,
			keyword: $scope.keyword,
			firstOrder: $scope.aFilter.name,
			firstOrderSort: $scope.aFilter.value,
			sourceId: $scope.sourceId,
			sourceTypeId: $scope.sourceTypeId,
			status: $scope.status,
			founder: $scope.founder,
			currentHandler: $scope.currentHandler,
			important: $scope.important,
			currentPage: $scope.currentPage,
			pageSize: 12
		};
		
		$http.post(root+'affair/findAffairs', params).success(function(res){
			var data = res.data.data;
			if($scope.currentPage == 1){
				$scope.affairs = data;
			}else{
				$scope.affairs = $scope.affairs.concat(data);
			}
			$scope.showLoadmoreBtn = $scope.currentPage < res.data.totalPage ? true : false;
			$scope.visitTime = res.visit;
		});
	}
	
	$scope.reset = function(){
		$scope.aFilter = $scope.affairFilter[0];
		$scope.currentPage = 1;
		$scope.keyword = '';
		$scope.sourceId = $scope.sourceTypeId = $scope.important = $scope.status = $scope.founder = $scope.currentHandler = '';
	}
	
	
	//左侧悬浮搜索条件
	
	$scope.statusAry = [{'name':'待处理', 'value':1},{'name':'处理中', 'value':2},{'name':'已解决', 'value':3},{'name':'已关闭', 'value':4}];
	$scope.importantAry = [{'name':'高', 'value':3},{'name':'中', 'value':2},{'name':'低', 'value':1}];
	
	$scope.showFilterWin = false;

	

	//左侧悬浮 来源 搜索
	function filterTreeSearch(params){
		$scope.reset();
		angular.forEach(params ,function(v, k){
			$scope[k] = v;
		});
		$scope.findAffairs($scope.shop);
		//hide filter window
		$('#showFilterWin').hide();
	}
	
	function filterTreeFn(obj, params){
		var _next = obj.next(),
			isShow = _next.is(':visible');
		if(_next.find('li').length){
			if(isShow){
				_next.slideUp();
				obj.find('i').removeClass('affair_ico_sub');
			}else{
				_next.parent().siblings().find('ul').slideUp();
				_next.slideDown();
				obj.find('i').addClass('affair_ico_sub');
			}
		}else{
			filterTreeSearch(params);
		}
	}
	
	
	
	$scope.filterTree = function(e, params){
		var obj = e.target || e.srcElement;
		if(obj.tagName == 'I'){
			obj = obj.parentNode;
			filterTreeFn($(obj) ,params);
		}else{
			filterTreeSearch(params);
		}
		
	}
	
	$scope.curAffairIco = function(subList){
		return subList.length ? 'affair_ico_plus' : 'affair_ico';
	}
	
	$scope.affairReset = function(){
		$scope.affairNote = '';
		$scope.affairHandler = '';
		$scope.affairStatus = $scope.statusAry[1];
	}
	$scope.getStatusName = function(status){
		return status ? ccms.getObjInAry($scope.statusAry, status, 'value').name : '';
	}
	//左侧悬浮搜索条件 -end
	
	$scope.reset();
	$scope.loadAffairDetail = true;
	$scope.$watch(function(){return $location.search().id;},function(id){
		$scope.affairId = id;
		
		//首次加载事务列表
		if(!$scope.shops){
			$http.get(root+"shop/taobao/list").success(function(res){
				$scope.shops = res.data.shops;
				$scope.init($scope.shops[0], id);
			});			
		}
		
		if(!/affairs\/myaffair\?/.test($location.url())){
			$scope.showAffairDetail = false;
			//当有事务列表时，点击二级导航时，重置店铺和事务列表
			if($scope.affairs && $scope.affairs.length > 0){
				$scope.shop = $scope.shops[0];
				$scope.reset();
				$scope.init($scope.shops[0]);
				console.log('watch init');
			};
			return false;
		}
		
		$scope.showAffairDetail = true;
		$scope.loadAffairDetail = false;
		
		$http.get(root + 'affair/findAffairDetail?affair_id=' + id).success(function(res){
			var data = res.data;
			
			$scope.affairDetail = data.affairDetail;
			$scope.tidInfo = data.tidInfo;
			$scope.oidInfo = data.oidInfo;
			$scope.userInfo = data.userInfo;
			$scope.affairHandles = data.affairHandles;
			$scope.visit_time = res.visit;
			$scope.loadAffairDetail = true;
			
			$scope.affairReset();
			
		});		
		
	});
	
	
	ccms.fixedHeight($('#myAffairList'));
	ccms.fixedHeight($('#myaffairBoxRight'));
	$('#affairFilters li a').click(function(){
		if(!$(this).attr('ng-click')) filterTreeFn($(this));
	});
	$('.myaffair_box h4').click(function(){
		var objA = $(this).find('a')[0];
		if(objA.className == 'myaffair_icoDown'){
			objA.className = 'myaffair_icoUp';
			$(this).next().slideDown(400);
		}else{
			objA.className = 'myaffair_icoDown';
			$(this).next().slideUp(400);
		}
	});
	$('#showFilterBtn').hover(function(){
		$('#showFilterWin').show();
	},function(){
		$('#showFilterWin').hide();
	});
	
	
	
	
	//搜索
	$scope.affairSearch = function(type, e){
		if(type == 'keydow' && e.keyCode != 13) return false;
		$scope.currentPage = 1;
		$scope.findAffairs($scope.shop);
	}
	
	//加载更多事务记录
	$scope.addAffairs = function(){
		$scope.currentPage ++;
		$scope.findAffairs($scope.shop);
	}
	
	//时间计算
	$scope.overTime = function(ot, vt){
		var differTime = ccms.getDifferTime(vt, ot),
			differHour = differTime.d * 24 + differTime.h;
		return differTime != 'false' ? '剩余'+differHour+'小时' : '已超期';
	}
	
	//列表状态 class
	$scope.navAffair = function(affair){
		return{
			cur: affair.affairsId == $scope.affairId,
			in_hand: affair.status == 2,
			closed: affair.status == 3 || affair.status == 4
		}
	}
	
	//重要性图标class
	$scope.txtImportant = function(type, level, classNames){
		var classNames = classNames || '',
			flg = type == 'text';
		if(level == 1)
			return flg ? '低' : '';
		else if(level == 2) 
			return flg ? '中' : classNames + 'normal';
		else if(level == 3) 
			return flg ? '高' : classNames + 'high';
	}
	$scope.getStatus = function(refundStatus, status){
		return refundStatus ? refundStatus : status;
	}
	
	/*中差评设置*/
	$scope.setGradeIcon=function(result){
		return{
			result_good: result == 'good',
			result_neutral: result == 'neutral',
			result_bad: result == 'bad'
		}
	}
	
	$scope.showAffairHandle = false;
	$scope.handleAffairs = function(){
		if(!$scope.affairNote){
			$(this).Alert({"title":"提示","str":"请填写备注","mark":true,"width":"160px"});
			return false;
		}
		var note = $scope.affairNote,
			nextHandler = $scope.affairHandler.nick,
			affairsId = $scope.affairId,
			status = $scope.affairStatus.value;
		
		$http.post(root + 'affair/handleAffairs?status='+status, {note: note, nextHandler: nextHandler, affairsId: affairsId, status: status}).success(function(res){
			if(res.status == '0'){
				$scope.affairReset();
				$scope.affairHandles.push({note: note, founder: $scope.user.name, next_handler: nextHandler, created: res.visit});
				$scope.showAffairHandle = false;
				$(this).Alert({"title":"提示","str":"跟进信息增加成功","mark":true,"width":"160px"});
			}
		});
	}
	
	// 修改事务
	$scope.modifyAffair = {
		obj: $('#modifyAffair'),
		init: function(){
			$scope.affairModify = {title: '', importantObj: {}, statusObj: {}, currentHandlerObj: {}, expiration_time: '', note: ''};
			$('#expirationTime').datetimepicker({timeFormat: 'HH:mm:ss', showSecond: true, changeMonth: true, changeYear: true});
		},
		show: function(){
			//把详细页里的值赋到修改窗口   ng-model
			$scope.affairModify = {
				title: $scope.affairDetail.title,
				importantObj: ccms.getObjInAry($scope.importantAry, $scope.affairDetail.important, 'value'),
				statusObj: ccms.getObjInAry($scope.statusAry, $scope.affairDetail.status, 'value'),
				currentHandlerObj: ccms.getObjInAry($scope.customerService, $scope.affairDetail.current_handler, 'nick'),
				expiration_time: $scope.affairDetail.expiration_time,
				note: $scope.affairDetail.note,
			}
			this.obj.addInteractivePop({magTitle: "事务信息修改",mark :true,drag: true,position:"fixed"});
			
		},
		save: function(){			
			var affairModify = $scope.affairModify,
				params = {
					dpId: $scope.shop.shopId,
					pkid: $scope.affairId,
					title: affairModify.title,
					important: affairModify.importantObj.value,
					status: affairModify.statusObj.value,
					currentHandler: affairModify.currentHandlerObj.nick,
					expirationTime: $('#expirationTime').val(),
					note: affairModify.note
				};
			
			if($.trim(params.title) == ''){
				$(this).Alert({"title":"提示","str":"事务标题不能为空","mark":true,"width":"160px"});
				return false;
			}
			if($.trim(params.note) == ''){
				$(this).Alert({"title":"提示","str":"事务备注不能为空","mark":true,"width":"160px"});
				return false;
			}
			var _this = this;
			$http.post(root + 'affair/update', params).success(function(res){
				if(res.status == '0'){
					_this.close();
					//把修改过的值更新值到dom上
					$.extend($scope.affairDetail, params);
					$scope.affairDetail.current_handler = params.currentHandler;
					$scope.affairDetail.expiration_time = params.expirationTime;
					
					$(this).Alert({"title":"提示","str":"事务修改成功","mark":true,"width":"160px"});
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
			});
		},
		close: function(){
			this.obj.find('.title .close').click();
		}
	};
	$scope.modifyAffair.init();
}



