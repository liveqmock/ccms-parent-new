function ccmsCtrl($scope,$location,$document){
	$scope.$watch(function(){return $location.path()},function(path){
		if(!path || /index/.test(path)){
			$scope.pageUrl = 'templates/index.html';
			$scope.showSideBar = false;
		}else{
			$scope.pageUrl = 'templates/'+path.substr(1)+'.html';
			$scope.showSideBar = true;
			
			//每次请求二级导航数据
			$scope.subNavs = [
				{"name":"营销项目","url":"#/a","threeNav":[]},
				{"name":"营销活动","url":"","threeNav":[
					{"name":"营销活动11","url":"#/c"},
					{"name":"营销活动22","url":"#/d"}
				]},
				{"name":"活动模板","url":"","threeNav":[
					{"name":"活动模板11","url":"#/f"},
					{"name":"活动模板22","url":"#/d"}
				]},
				{"name":"营销项目","url":"#/h","threeNav":[]},
			];
		}
	});
	
	$scope.navClass = function(url){
		return{
			navCur:url.indexOf($location.path()) > 0 //如果为真则返回navCur，否则不返回值
		};
	}
	$scope.navSub = function(threeNav){
		return{
			sideNavNosec:threeNav.length == 0
		}
	}
	
	$scope.afterLoadCont = function(){
		//加载标题
		$scope.pageTitle = '-'+$location.path().substr(1);
	}
	$scope.user = {
		"nav":[
			{"name":"首  页","url":"#/index"},
			{"name":"经营分析","url":"#/analysis/evaluateSearch"},
			{"name":"系统管理","url":"#/manage/userManage"},
			{"name":"营销活动","url":"#/"},
			{"name":"事件活动","url":"#/"},
			{"name":"会员管理","url":"#/"},
			{"name":"客户管理","url":"#/"},
			{"name":"模板管理1","url":"#/"},
			{"name":"模板管理2","url":"#/"},
			{"name":"模板管理3","url":"#/"}
		],
		"name":"skip.qian",
		"noticeCount":58
	}
}
