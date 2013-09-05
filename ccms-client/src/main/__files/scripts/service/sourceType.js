function sourceType($scope, $http, $timeout){
	$scope.sourceAry = [{"name": "未付款", "id": "2"}, {"name": "发货", "id": "3"}, {"name": "物流", "id": "4"}, {"name": "退款", "id": "5"}, {"name": "评价", "id": "6"}];
	$scope.init = function(shop){
		var source = $scope.sourceAry[0];
		if(!$scope.shop){
			//获取店铺
			$http.get(root+"shop/taobao/list").success(function(res){
				$scope.shops = res.data.shops;
				$scope.shop = $scope.shops[0];
				$scope.getSource($scope.shop.shopId, source);
			});
		}else{
			$scope.shop = shop;
			$scope.getSource(shop.shopId, source);
		}
	}
	
	$scope.getSource = function(dpId, source){
		$scope.showSourceType = false;
		$scope.source = source
		$scope.parentId = source.id;
		
		$http.post(root + 'category/findByParentId', {parentId: source.id, outId: dpId, outType: 'affairType'}).success(function(res){
			if(res.status == '0'){
				$scope.sourceTypes = res.data;
				$scope.showSourceType = true;
			}
		});
	}
	
	//删除来源
	$scope.delSource = function(id, index){
		$(this).Confirm({"title":"确认","str":"确定删除该类型？","mark":true},function(){
			$http.post(root+ 'category/delete', {pkid: id}).success(function(res){
				if(res.status == '0'){
					$scope.sourceTypes.splice(index, 1);
					$(this).Alert({"title":"提示","str":"类型删除成功","mark":true,"width":"160px"});
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
			});
		});
	}
	
	//新增类型
	$scope.addSource = {
		saveFlg: true,
		obj: $('#addSource'),
		init: function(){
			$scope.sourceName = '';
			$scope.sourceNote = '';
		},
		show: function(){
			this.obj.addInteractivePop({magTitle: "新增类型",mark :true,drag: true,position:"fixed"});
		},
		save: function(){
			var _this = this,
				shopId = $scope.shop.shopId,
				parentId = $scope.parentId,
				sourceName = $.trim($scope.sourceName),
				sourceNote = $.trim($scope.sourceNote);
				
			if(!_this.saveFlg) return false;
			if(sourceName == ''){
				$(this).Alert({"title":"提示","str":"类型名称不能为空","mark":true,"width":"160px"});
				return false;
			}
			if(sourceNote == ''){
				$(this).Alert({"title":"提示","str":"类型备注不能为空","mark":true,"width":"160px"});
				return false;
			}
			
			$http.post(root + 'category/add', {
				outId: shopId,
				parentId: parentId,
				name: sourceName,
				description: sourceNote,
				outType: 'affairType'
			}).success(function(res){
				if(res.status == '0'){
					// $scope.sourceTypes.push(params); 不用push，直接在页面上加入数据，因为要取到id，删除的时候用
					$scope.getSource(shopId, $scope.source);
					_this.init();
					_this.close();
					$(this).Alert({"title":"提示","str":"类型新增成功","mark":true,"width":"160px"});
				}else{
					$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
				}
				_this.saveFlg = true;
			});
			_this.saveFlg = false;
		},
		close: function(){
			this.obj.find('.title .close').click();
		}
	}
	$scope.addSource.init();
	
	$scope.init();
	ccms.fixedHeight($('#sourceTypeList'), 3);
}