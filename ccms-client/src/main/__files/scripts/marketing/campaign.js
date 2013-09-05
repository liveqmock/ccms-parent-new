//属性窗口
var attrPop = {
	init:function(obj,onClick){
		//初始化zTree
		$.get(root+"meta/catalog",function(zNodes){
			zNodes[0].open = true;
			//console.log(attrPop.setting(onClick)+'\n'+zNodes);
			$.fn.zTree.init(obj,attrPop.setting(onClick), zNodes);
		},"JSON");
	},
	setting:function(onClick){
		var setting = {
			data: {
				key: {
					title:"labelName",name:"labelName"
				},
				simpleData: {
					enable: true
				}

			},
			callback:{
				onClick:onClick
			}
		}
		return setting;
	}
};

//设置节点id
function setNodeId(a,b){
    var id=a.attr("id");
    b.find("h4 em").text(id);
}
/* href的格式还是得loadPanel.list来区分，不然最外层那监听到后，会按其方式来加载页面 */
var campHdAry = [{
	href:'#/marketing/campaign.list',
	name:'活动列表',
	id:'' //id用于去重
}];
function Market($scope,$location,$http) {
	//营销活动页面强制跳转到列表页面
	window.campaignHref=location.href = location.href.match(/(.*)\./)[1] + '.list';
	$scope.nodeTpl = 'templates/marketing/list.html';

	$scope.$watch(function(){return $location.path()},function(path){
		//var tplStr = path.match(/\.(.*):/);
		//console.log(tplStr);
		if(typeof(jobRefreshTimer)!="undefined"){   //clear活动节点的状态定时器
				clearTimeout(jobRefreshTimer);
				clearTimeout(campRefreshTimer);
		}

		$scope.snodeTpl = '';
		$scope.addMarketTpl = '';
		if(path.indexOf('.') > 0){
			$scope.campHdAry = campHdAry;
			//表头当前状态
			$scope.navCampList = function(href){
				return{
					listSelected:href.indexOf(path) > 0
				};
			}

			var newpath;
			if(path.indexOf(':') > 0){
				newpath = path.match(/\.(.*):/)[1];
			}else{
				newpath = path.split('.')[1];
			}

			$scope.nodeTpl = 'templates/marketing/'+newpath+'.html?_='+new Date().getTime();
            if(path.indexOf(":")>0){
				//加载6个节点
				$http.get('campaign/workflow/snapshot?campId='+path.split(':')[1]+'&_='+new Date().getTime()).success(function(d){
					var newNodes = d.data[0].nodes;
					$.each(newNodes,function(i,n){
						$.each(n,function(k,v){
							if(v=="tflowtime"){
								 $scope.nodeTplId = window.nodeTplId = newNodes[i].id;
							}
							if(k == 'style'){
								n['imageSrc'] = v.match(/\.\/(.*)$/)[1];
							}
						})
					});
					$http.get('campaign/'+path.split(':')[1]+'?_='+new Date().getTime()).success(function(d){
					       var picUrl=d.data[0].picUrl;
						   window.tplAuthorizationFlag=d.data[0].supportOps;
						   $scope.nodes=$scope.sortToNodes(newNodes,picUrl);           //节点配置
						   $scope.snodeTpl = 'templates/marketing/nodeTpl/tflowtime.html?_='+new Date().getTime();

					 });

				});

           }
		}

		$scope.curLat = function(){
			return{
				curLat:this.$last,
				curFirst:this.$first
			}
		}

        $scope.sortToNodes=function(a,b){
		      var arr=[];
			  var len=a.length;
			  if(b=="../images/compType-dx.png"){    //加载短信模板
				 for(var i=0;i<len;i++){
					    if(a[i].type=="tflowstart"){
							arr[0]=a[i];
						 }else if(a[i].type=="tflowtime"){
							arr[1]=a[i];
						 }else if(a[i].type=="tfilterfind"){
							arr[2]=a[i];
						 }else if(a[i].type=="tcustomertargetgroup"){
							arr[3]=a[i];
						 }else if(a[i].type=="tcommunicateSMS"){
							arr[4]=a[i];
						 }else if(a[i].type=="tcustomerevaluate"){
							arr[5]=a[i];
						}
				 }
			 }else if(b=="../images/compType-yj.png"){    //加载邮件模板
					for(var i=0;i<len;i++){
					    if(a[i].type=="tflowstart"){
							arr[0]=a[i] ;
						 }else if(a[i].type=="tflowtime"){
							arr[1]=a[i];
						 }else if(a[i].type=="tfilterfind"){
							arr[2]=a[i];
						 }else if(a[i].type=="tcustomertargetgroup"){
							arr[3]=a[i];
						 }else if(a[i].type=="tcommunicateEDM"){
							arr[4]=a[i];
						 }else if(a[i].type=="tcustomerevaluate"){
							arr[5]=a[i];
						}
				 	}
			 }else if(b=="../images/compType-yhq.png"){    //加载优惠券模板
				for(var i=0;i<len;i++){
					    if(a[i].type=="tflowstart"){
							arr[0]=a[i];
						 }else if(a[i].type=="tflowtime"){
							arr[1]=a[i];
						 }else if(a[i].type=="tfilterfind"){
							arr[2]=a[i];
						 }else if(a[i].type=="tcustomertargetgroup"){
							arr[3]=a[i];
						 }else if(a[i].type=="tcommunicateEC"){
							arr[4]=a[i];
						 }else if(a[i].type=="tcommunicateSMS"){
							arr[5]=a[i];
						 }else if(a[i].type=="tcustomerevaluate"){
							arr[6]=a[i];
						}
				 	}
			 }
			 return arr;
		}
	});

	var testAry = ['11','bbb','cccc'];

	$scope.delCampHd = function(index,href){
		$scope.campHdAry.splice(index,1);
		var l = location;
		if(l.hash == href){//如果删除的是当前活动
			l.hash = campHdAry[campHdAry.length-1].href;
		}
	}
	$scope.showNodeTpl = function(type,id){
		if(type=="tflowstart"){
		   return false;
		}else{
			$scope.snodeTplLoaded = false;
			$scope.snodeTpl = 'templates/marketing/nodeTpl/'+type+'.html?_='+new Date().getTime();
		    $scope.nodeTplId = window.nodeTplId=id;
		}
	}
	$scope.afterLoadNodeTpl = function(){
		$('.triangleArrows').css('left',4+$('.canvas .graphRect[id!=""]').index($('#'+$scope.nodeTplId))*102);
		$scope.snodeTplLoaded = true;
	}
	$scope.addMarket=function(){
	    $scope.addMarketTpl="templates/marketing/addMarketLayer.html?_="+new Date().getTime();
	}
	$scope.clickConcelMarket=function(){
	    $scope.addMarketTpl="";
	}
	$scope.clickSureMarket=function(){   //先增项目
	     var f=validateCustom();
             if(f){
                   var campName=$("#createMaketLayer [name='campName']").val();
                   var campDesc=$("#createMaketLayer [name='campDesc']").val();
                   var campId=$("#createMaketLayer [name='templateId']").attr("id");
                   var parame={
                       campName:campName,
                       campDesc:campDesc,
                       templateId:campId,
                       platCode:"taobao"
                   }
                   parame=JSON.stringify(parame);
                  $.ajax({
                      url:"campaign",
                      async: false,
                      type:"POST",
                      data: parame,
                      contentType:'application/json',
                      dataType:'json',
                      success:function(response){
                           var data=response.data;
                           if(response.status=="error"){
							   $(this).Alert({"title":"提示","str":response.data[0].errordesc,"mark":true,"width":"160px"});
                           }else{
                              $scope.addMarketTpl="";
							  location.href = location.href.match(/(.*)\./)[1] + '.list';
                              $('.campaignListCon').flexReload();
                           }
                      }
                   });
             }
             return false;
	}
}