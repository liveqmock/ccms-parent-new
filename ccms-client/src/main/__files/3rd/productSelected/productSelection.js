function proList($scope,$http){
	$scope.closeProPop=function(){
		$(".productSelected").hide();
		$(".yunat_maskLayer:last").remove();
	}
	//搜索商品列表
	angular.element("#productName").bind({
		"focus":function(){
			$scope.$apply(function(){
				if($scope.productName=="请输入至少两个字")	{
					$scope.productName="";
					angular.element(this).css("color","#333333");
				}
			})
		}
	});

	$scope.searchProductList=function(page,rp,flag){
	    $scope.prevCheckedAll=false;
	    $scope.productName=$scope.productName?$scope.productName:"请输入至少两个字";
		var seearchValFlag=$scope.productName=="请输入至少两个字";
		if($scope.productName.length<2){
			$scope.productName="请输入至少两个字";
			angular.element(this).css("color","#999999");
			return false;
		}
		if($scope.productName=="")	{
			$scope.productName="请输入至少两个字";
			angular.element(this).css("color","#999999");
		};
		
		/*if(($scope.productName.length<2 && !flag) || (seearchValFlag && !flag)){//搜索字符必须是大于两个字符
			$scope.productName=="请输入至少两个字";
		}*/
		var parame={};
		parame.q=($scope.productName=="请输入至少两个字")?"":$scope.productName;
		parame.pageNo=page;
		parame.pageSize=rp;
		$scope.goods=[];
		$http.post(root+$scope.searchProductListUrl,JSON.stringify(parame)).success(function(res){
				if(res.status==0){
					 $scope.seachGoods=res.data.list;
					 $scope.totalPage=Math.ceil((res.data.count)/(parame.pageSize));
				}

		});
	}
	$scope.searchProductList(1,20,true);
	//加载之前选中商品
	$scope.findGoods=[];
	$scope.findSelectedGoods=function(){
	    $scope.nextCheckedAll=false;
		var ids=$scope.findIds.join(",");
		$http.get(root+$scope.findProductListUrl+"/"+ids+"?_="+new Date().getTime()).success(function(res){
			    if(res.status=="0"){
					 $scope.findGoods=res.data.list;
					 $scope.checkedGoodsLen=$scope.findGoods.length;
				}

		});
	}
	if($scope.findIds.length>0){$scope.findSelectedGoods()};
	//选中产品
	$scope.addGoods=function(){
		if($scope.checkedGoodsLen>=50){
			$(this).Alert({"title":"提示","str":"选择商品数不得超过50个","mark":true,"width":"200px"})
			return false;
		}
		$("#prevSelection .pic input:checked").each(function(){
			  var $items=$(this).parents(".items");
			  var id=$items.find(".goodsId span").text();
			  if($scope.findIds.join(",").indexOf(id)<0){
				  var name=$items.find(".goodsName").text();
				  var src=$items.find(".pic img").attr("src");
				  var price=$items.find(".goodsPrice").text();
				  $scope.findGoods.push({numIid:id,title:name,picUrl:src,price:price});
				  $scope.findIds.push(id);
				  $scope.checkedGoodsLen=$scope.findGoods.length;
			  }
		});
		$scope.nextCheckedAll=false;
		$scope.prevChecked=false;
		$scope.prevCheckedAll=false;
	}
	//单个删除选中产品
	$scope.delGoods=function(index){
		 $scope.nextCheckedAll=false;
		 $scope.prevCheckedAll=false;
		 $scope.delObjProperty(index);
	}
	//批量删除选中商品
	$scope.clearGoods=function(){
		$scope.nextCheckedAll=false;
		$scope.prevCheckedAll=false;
		var x=0;
		$("#nextSelection .pic input:checked").each(function(){
			 var index=$("#nextSelection .pic input").index($(this));
			 index=index-x;
			 $scope.delObjProperty(index);
			 x++;
		});
	}
	$scope.delObjProperty=function(index){
		 $scope.findGoods.splice(index,1);
		 $scope.findIds.splice(index,1);
		 $scope.checkedGoodsLen=$scope.findGoods.length;
	}
	//保存选中商品
	$scope.saveSelectGoods=function(){
		$scope.returnProductParame.call($scope,$scope.findIds,$scope.checkedGoodsLen,$scope.findGoods);
		$scope.closeProPop();
	}
	//取消选中商品
	$scope.cancelSelectGoods=function(){
		 $scope.closeProPop();
	}
	//分页
	$scope.currentPage=1;
	$scope.totalPage=1;
	$scope.rp=20;
	$scope.effectiveRange=function(val){
		if(isNaN(val)){
			$scope.currentPage=1;
			//$(".pGroup .pageTips").autoVanishTips({str:"请输入正确的查询范围",timer:1500});
			return false;
		  }else{
			  if(val<1 || val>$scope.totalPage){
				  $scope.currentPage=1;
				  //$(".pGroup .pageTips").autoVanishTips({str:"请输入正确的查询范围",timer:1500});
				  return false;  
			   }else{
				  return true;  
			   }
		  }

	}
	$scope.firstPage=function(){
		if($scope.effectiveRange($scope.currentPage)){
			 $scope.currentPage=1;
			 $scope.searchProductList($scope.currentPage,parseInt($scope.rp));
		}
	}
	$scope.lastPage=function (){
		if($scope.effectiveRange($scope.currentPage)){
			 $scope.currentPage=$scope.totalPage;
			 $scope.searchProductList($scope.currentPage,parseInt($scope.rp));
		}
	}
	$scope.prevPage=function (){
		if($scope.effectiveRange($scope.currentPage) && $scope.currentPage!=1){
			 $scope.currentPage--;
			$scope.searchProductList($scope.currentPage,parseInt($scope.rp));
		}
	}
	$scope.nextPage=function (){
		if($scope.effectiveRange($scope.currentPage) && $scope.currentPage!=$scope.totalPage){
			 $scope.currentPage++;
			 $scope.searchProductList($scope.currentPage,parseInt($scope.rp));
		}
	}
	$scope.changeRp=function(){
		 $scope.currentPage=1;
		 $scope.searchProductList($scope.currentPage,parseInt($scope.rp));
	}
	$scope.searchPage=function(currentPage){
		 $scope.searchProductList(parseInt(currentPage),parseInt($scope.rp));
	}	
}