//检查登录
var userName = '',popFlag,thisTime = new Date().getTime();
(function($){
	var s=location.toString();
	var i=s.lastIndexOf('?');
	$.ajax({
		url:root+'loginInfo'+(i<0?'':s.substring(i)),
		type:'GET',
		async:false,
		success:function(data){
			if(data.needLogin){//true 需要登录
				location.href = data.loginUrl;// || 'login.html';
			}else{
				userName = data.loginUser.loginName;
				if(!location.hash) location.href = root+'#/shop_diagnosis';
			}
			popFlag=data.pop;
		}
	});
})(jQuery);
function ccmsCtrl($scope,$location,$document,$http){
	$scope.logout = root + 'logout';
//账号管理url	
	$.ajax({
		url:root+"tenant/channel/recharge",
		type:"GET",
		cacle:false,
		success:function(d){
			$scope.userDoUrl=d.data.rechargeUrl;	
		}
	})
//

	if(popFlag){
		$(this).Alert(
			{"title":"友情提示","str":"尊敬的数据赢家用户：<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的CRM平台已经部署好了!!我们正在努力为您同步历史数据,数据同步需要一些时间，请稍后.......<br/>历史数据将会在部署后的24小时后同步完成,欢迎明天再来使用数据赢家。<br/><span style='display:block;text-align:right;'>数据赢家服务团队</span>","mark":true}
		);	
	}
	
	var lastpageUrl = '';
	$scope.$watch(function(){return $location.path();},function(path){
		if(typeof(jobRefreshTimer)!="undefined"){   //clear活动节点的状态定时器
			clearTimeout(jobRefreshTimer);
			clearTimeout(campRefreshTimer);
	    }
		if(!path || /dashboard/.test(path)){
			$scope.tmlLoaded = false;
			$scope.pageUrl = 'templates/dashboard.html';
			$scope.showSideBar = false;
		}else if(/shop_diagnosis|order_center|shop_monitor|bi_url/.test(path)){//iframe 嵌入 新增的话，只需要在这里加个url的判断即可
			$scope.tmlLoaded = false;
			$scope.pageUrl = 'templates/otherpage.html?_='+new Date().getTime();
			$scope.showSideBar = false;
		}else{
			if(path == '/order/care') path = '/order/urpay';
			if(path.split('.')[0] != lastpageUrl.split('.')[0]){
				$scope.tmlLoaded = false;
				if(path.indexOf('.') > 0) path = path.split('.')[0];
				if(path.indexOf(':') > 0) path = path.split(':')[0];
				path = path.replace(/\/modify$/,'/add'); //修改的时候加载add模版
				path = path.replace('/order/care','/order/urpay');//订单关怀模版为催付的模板，只是在url上体现出区别来
				$scope.pageUrl = 'templates'+path+'.html?_='+new Date().getTime();
			}

			$scope.showSideBar = true;

			//每次请求二级导航数据
			var subNavAry = [];
			$http.get(root+"module/"+path.match(/[^\/]([a-zA-Z0-9_-])+[^\/]/)[0]).success(function(data){
				angular.forEach(data.children,function(n){
					if(/R/.test(n.supportOps)){//R为可读
						subNavAry.push(n);
					}
				});
				$scope.subNavs = subNavAry;
			});
		}
		lastpageUrl = path;
	});
	$scope.navClass = function(url){
		return{
			//navCur:new RegExp(url.replace(/#|\?.*|\..*/g,'')).test($location.path().replace('/order/care','/order/urpay')) //如果为真则返回navCur，否则不返回值
			navCur:new RegExp(url.match(/\/.*?(\/|$)/)[0]).test($location.path())
		};
	};

	$scope.afterLoadCont = function(){
		$scope.tmlLoaded = true;
	};
	$scope.user = {
		"name":userName,
		"noticeCount":58
	};
	var navAry = [];
	$http.get(root+"module/nav").success(function(data){
		angular.forEach(data.children,function(n){
			if(/R/.test(n.supportOps)){//R为可读
				navAry.push(n);
			}
		});
		$scope.user.nav = navAry;

		//请求main.js
		$.getScript('scripts/main.js');
	});
	
	
	//检查请求是否是在分页上发起的
	$scope.checkIsPager = function(arg,scope,curPgae,rp){//arg 为函数的默认参数,scope 作用域 ,curPage 默认当前页 ,rp 默认显示条数
		var isPager = false
		$.each(arg,function(i,n){
			if(n == 'isPager'){
				isPager = true;
				return
			}
		});
		if(isPager){
			scope.defaultPage = scope.pager.curpage;			
		}else{
			scope.defaultPage = curPgae;	
		}
		if(scope.pager){
			scope.defaultSize = scope.pager.rp;
		}else{
			scope.defaultSize = rp;
		}
	}
	
	$scope.floor2 = function(num){
		return Math.floor(num*100)/100;
	}
	
	//客服中心 来源类型设置
	$scope.sourceAry = [{"name": "未付款", "id": "2"}, {"name": "发货", "id": "3"}, {"name": "物流", "id": "4"}, {"name": "退款", "id": "5"}, {"name": "评价", "id": "6"}];
}


function ngDirectiver(fn){
	return function() {return fn;};
}
function loadScrip(obj){
	if(obj.attr('_href')){
		obj.attr('href',root+obj.attr('_href'));
	}
	if(obj.attr('_src')){
		$.ajax({
			url:root+obj.attr('_src'),
			dataType:'script',
			async:false,
			success:function(){
				obj.remove();
			}
		});
	}
}

angular.module('ccmsApp',['personalizedPackage']).directive({
	getRoot:ngDirectiver(function(scope,element){
		loadScrip(element);
	}),
	ngVisible:ngDirectiver(function(scope,element,attr){
		scope.$watch(attr.ngVisible, function(value){
			element.css('visibility', value ? '' : 'hidden');
		});
	}),
	ngDisabled:ngDirectiver(function(scope,element,attr){
		scope.$watch(attr.ngDisabled, function(value){
			if(value){
				//给默认的ngDisabled指令加上，禁用时:text 值清空，:radio,:checkbox 不选中
				if(element.is(':text') && !scope.showVoucher){
					element.val('');
				}else if(element.is(':radio') || element.is(':checkbox')){
					//单独为订单催付，预关闭催付的聚划算checkbox不做此处理
					if(scope.urpayType != 2 || element.attr('id') != 'includeCheap'){
						element.attr('checked',false);
					}
				}
			}
		});
	}),
	ngInputFocus:ngDirectiver(function(scope,element,attr){//退款事务 常用话术配置
		scope.$watch(attr.ngInputFocus, function(value){
			element.select();
		});
		if(scope.c.id){
			element.blur(function(){
				scope.$apply(function(){
					scope.c.added = false;
				});
			});
		}
	}),
	ngValidinput:ngDirectiver(function(scope,element){//angularjs内置验证加上错误class
		scope.$watch(function(){return element.parent().find('.error:visible').length;},function(length){
			if(length){
				element.addClass('errorInput');
			}else{
				element.removeClass('errorInput');
			}
		});
	}),
	ngTitle:ngDirectiver(function(scope,element,attr){
		var x=14,y=20,_toolTip;
		element.mouseover(function(e){
			var maxWidth = parseInt(element.attr('ng-title-max')) || 180;
			if(!$('#tooltip').length) $('body').append("<div id='tooltip' class='viewMarkInfoBox' style='width:auto;'></div>");
			_toolTip = $('#tooltip');
			_toolTip.html(attr.ngTitle).css({'visibility': 'hidden','max-width':maxWidth});
			var L = e.pageX+x,
				Y = e.pageY+y,
				W = $(window).width(),
				H = $(window).height(),
				OH = _toolTip.outerHeight(true);
			
			if(_toolTip.width() == maxWidth && L + maxWidth > W){
				_toolTip.css({'left':W - _toolTip.outerWidth(true) - 12,'width':maxWidth});
			}else{
				_toolTip.css({left: L,width: 'auto'});
			}
			
			if(Y + OH > H){
				_toolTip.css('top',H - OH);
			}else{
				_toolTip.css('top',Y);
			}
			
			_toolTip.css({'visibility':'','display':'block'});
			
		}).mouseout(function(){
			_toolTip.hide();
		});
	}),
	ngDisabledorderinput:ngDirectiver(function(scope,element,attr){//订单中心会员等级为不限时，禁用其余选项
		function disabledFn(obj){
			$('[name='+obj.attr('name')+']').not(obj).attr({'disabled':Boolean(obj.attr('checked')),'checked':false});
		}
		scope.$watch(attr.ngDisabledorderinput, function(value){
			if(element.val() == "-1") disabledFn(element);
		});
		element.click(function(){
			if(element.val() == "-1") disabledFn($(this));
		});
	}),
	ngAddsubcurrent:ngDirectiver(function(scope,element,attr){//首次加载添加二级导航样式
		scope.$watch(function(){return element.attr('href')}, function(href){
			if(location.hash.indexOf(href.replace(/\..*/g,'')) >= 0){
				element.addClass('sbCurrent');
			}
		});
	}),
	ngAddsubcur:ngDirectiver(function(scope,element,attr){
		scope.$watch(function(){return element.attr('href')}, function(href){
			if(location.hash.indexOf(href.replace(/\..*/g,'')) >= 0){
				var _parent = element.parents('li');
				_parent.find('>a').addClass('sbCurrent')
				_parent.find('dl').show();
				element.addClass('sbSmallCur');
			}
		});
	}),
	ngPager:function($compile){
		return function(scope,element,attr){
			scope.chnagePage = function(type,e){
				var np;
				switch(type){
					case 'first':
						np = 1;
						break;
					case 'prev':
						np = parseInt(scope.pager.curpage) <= 1 ? 1 : parseInt(scope.pager.curpage) -1;
						break;
					case 'next':
						np = scope.pager.curpage < scope.pager.totalPages ? parseInt(scope.pager.curpage) +1 : scope.pager.totalPages;
						break;
					case 'last':
						np = scope.pager.totalPages;
						break;
					case 'keydown':
						if(e.which != 13){
							return false;
						}else{
							var np = parseInt(e.target.value);
							if (isNaN(np)) {
								np = 1;
							}
							if (np < 1) {
								np = 1;
							} else if (np > scope.pager.totalPages) {
								np = scope.pager.totalPages;
							}
						}
						break;
				};
				if(type != 'rp'){
					if(np == scope.pager.curpage) return false;
					scope.pager.curpage = np;
				}
				scope.pager.submit(scope);
			}
			
			scope.$watch(attr.ngPager,function(params){//通过watch来监听传过来的参数会随着操作而变化
				if(!params) return false;//如果params == 'undefined'则跳过
				
				scope.pager = $.extend({
					total: 0,
					totalTip: '',
					exportUrl: '',
					exportTip: '只能查询30天内发送记录，请及时导出保存',
					rpAry: [10,15,20,30,50],
					submit: $.noop
				},params);
				scope.pagerTotals = scope.pager.totalPages || 1;
				
				if(scope.pager.totalTip){
					scope.pager.totalTip = scope.pager.totalTip.replace(/{total}/,scope.pager.total);
				}
				
				
				
				if(!element.html()){
					var pagerHtml = '<div class="flexigrid comm_pager">'+
						'<div class="pDiv clearfix">'+
							'<div class="pDiv1" ng-show="pager.totalTip || pager.exportUrl">'+
								'<div class="pGroup" ng-show="pager.totalTip">{{pager.totalTip}}</div>'+
								'<div class="pGroup" ng-show="pager.exportUrl"><a ng-href="{{pager.exportUrl}}" ng-title="{{pager.exportTip}}"><i class="ico_export"></i>导出</a></div>'+
							'</div>'+
							'<div class="pDiv2">'+
								'<div class="pGroup"> <div class="pFirst pButton" ng-click="chnagePage(\'first\');"><span></span></div><div class="pPrev pButton" ng-click="chnagePage(\'prev\');"><span></span></div> </div>'+
								'<div class="pGroup"><span class="pcontrol">跳至 <input onkeyup="this.value=this.value.replace(/\\D/g,\'\')" ng-keydown="chnagePage(\'keydown\',$event);" type="text" value="{{pager.curpage}}" size="4"> 页</span></div>'+
								'<div class="pGroup"> <div class="pNext pButton" ng-click="chnagePage(\'next\');"><span></span></div><div class="pLast pButton" ng-click="chnagePage(\'last\');"><span></span></div> <div class="pGroup pagetotal"></div></div>'+
								'<div class="pGroup" style="color:#000;">{{pager.curpage}}/{{pagerTotals}}</div>'+
								'<div class="pGroup">'+
									'<select style="width:50px;" ng-change="chnagePage(\'rp\')" ng-model="pager.rp" ng-options="rp for rp in pager.rpAry"></select>'+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>';
					element.html(pagerHtml)					
					$compile(element.contents())(scope);
				}
			});
		}
	},
	ngDifferTime:ngDirectiver(function(scope,element,attr){
		var timeFlg,
			attrs = scope.$eval(attr.ngDifferTime),
			serverTime = ccms.getSecond(attrs.sTime),
			newServerTime = serverTime,
			differ = {};
			
		function update(){
			timeFlg = setTimeout(function() {
				//通过得到服务器时间后，在页面每隔1s给服务器时间+1
				var d = new Date();
				d.setTime(newServerTime);
				var newServerTimeLocal = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate()+' '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds();
				
				differ = attrs.transfer ? ccms.getDifferTime(attrs.eTime, newServerTimeLocal) : ccms.getDifferTime(newServerTimeLocal, attrs.eTime);
				if(differ == 'false'){
					element.html('已结束');
				}else{
					element.html('<i>'+differ.d+'</i>天 <i>'+differ.h+'</i>时 <i>'+differ.m+'</i>分<i>'+differ.s+'</i>秒');						
					newServerTime += 1000;
					update();
				}
				
			}, 1000);
		}
		
		element.bind('$destroy', function() {
			clearTimeout(timeFlg);
		});
		
		update();
	}),
	ngTipBox:ngDirectiver(function(scope,element,attr){
		var _tipBox, tipFlag, tid = scope.$eval(attr.ngTipBox);
		
		function hideBox(obj){
			tipFlag = setTimeout(function(){
				obj.hide();
			},100);
		}
		

		if(!$('#commTipBox').length){
			 var htmls = '<div id="commTipBox" class="comm_tipbox">'+
				'<div class="pos_rel1" id="commTipIco"><span class="tip_icon2"><span class="tip_icon3"></span></span></div>'+
				'<div class="comm_tipbox_m">'+
					'<div class="tip_title clearfix"><h3 class="fl">物流信息</h3><div id="commTipHeaderR" class="fr">物流单号：<span id="receiptNo" class="receiptNo"></span><a target="_blank" href="#" id="tip_company">EMS</a>官网查询</div></div>'+
				'<div class="comm_tipbox_b"><div class="comm_tip_ico"><span class="tip_ico_first"></span><span class="tip_ico_end" id="tip_ico_end"></span></div><div id="comm_tipbox_m"></div></div></div>'+
			'</div>'
			$('body').append(htmls);
			_tipBox = $('#commTipBox');
		}else{
			_tipBox = $('#commTipBox');
		}
		
		_tipBox.hover(function(){
			clearTimeout(tipFlag);
		},function(){
			hideBox($(this));
		});
		
		element.hover(function(){
			clearTimeout(tipFlag);
		
			var el = $(this),
				offset = el.offset();
			if(!el.data('dataOInfo')){
				$.ajax({
					url: root+'customerCenter/logistics/showLogisticsInfo?tid='+tid+'&_='+new Date().getTime(),
					type: 'GET',
					async: false,
					success: function(res){
						el.data('dataOInfo',res);
					}
				});
			}
			
			var data = el.data('dataOInfo');
			$('#comm_tipbox_m').html('');
			$('#receiptNo').html(data.receiptNo).attr('data-clipboard-text',data.receiptNo);
			
			if(data.step && data.step.length && el.attr('status') != '无流转' && (data.step.length != 1 || data.step[0].info != 'null')){
				$('#commTipHeaderR,.comm_tip_ico').show();
				$('#tip_company').html(data.company).attr('href',data.companyUrl);
				$.each(data.step,function(i,n){
					if(n.info != 'null') $('#comm_tipbox_m').append('<p><span class="time_wrap">'+n.time+'</span>'+n.info+'</p>')
				});
				if(data.status == "3") $('#tip_ico_end').css('display','block');
			}else{			
				$('#commTipHeaderR,.comm_tip_ico').hide();
				$('#comm_tipbox_m').html('<p style="text-align:center;">该物流公司未返回跟踪信息，请到<a target="_blank" href="'+data.companyUrl+'">'+data.company+'</a>官网查询或联系其公示电话</p>');
			}
			if(offset.top + _tipBox.outerHeight(true) > $(window).height()){
				icoTop = offset.top + _tipBox.outerHeight(true) - $(window).height()
			}else{
				icoTop = 10;
			}
			$('#commTipIco').css('margin-top',icoTop);
			_tipBox.css({'left':offset.left - _tipBox.outerWidth(true),'top':offset.top - icoTop,'display':'block'});
		},function(){
			hideBox(_tipBox);
		});
	}),
	addSource:function($http){
		var Source = {
			obj: $('#addSource'),
			saveFlg: true,
			templates: function(){
				var htmls = '<div class="upload_msg" id="addSource">'+
					'<div class="upload_msg_m">'+
						'<dl>'+
							'<dt>来源：</dt>'+
							'<dd><span id="sourceName"></span></dd>'+
							'<dt>名称：</dt>'+
							'<dd>'+
								'<input type="text" class="borderHighlight" id="sourceTypeName" />'+
							'</dd>'+
							'<dt>备注：</dt>'+
							'<dd>'+
								'<div class="myaffair_textarea"><textarea style="height:80px;" id="sourceTypeNote"></textarea></div>'+
							'</dd>'+
						'</dl>'+
						'<div class="mt20"><button class="btn btnBlue" style="margin-left:0;" id="saveAddSource">确定</button><button class="btn" id="cloaseAddSource">取消</button></div>'+
					'</div>'+
				'</div>';
				return htmls;
			},
			show: function(option){
				var _this = this;
				
				if(!option.shopId){
					option.shopId = $('#shopinfo span').attr('id');
				}
				
				
				if(!_this.obj.length){
					$('body').append(this.templates());
					_this.obj = $('#addSource');
					$('#saveAddSource').click(function(){
						_this.save($('#sourceName').data('option'));
					});
					$('#cloaseAddSource').click(function(){
						_this.close();
					});
				}
				$('#sourceName').data('option', option).text(option.sourceName);
				
				_this.obj.addInteractivePop({magTitle: "新增类型",mark :true,drag: true,position:"fixed"});
			},
			save: function(option){
				var _this = this,
					shopId = option.shopId,
					parentId = option.sourceId,
					sourceTypeName = $.trim($('#sourceTypeName').val()),
					sourceTypeNote = $.trim($('#sourceTypeNote').val());
					
				if(!_this.saveFlg) return false;
				if(sourceTypeName == ''){
					$(this).Alert({"title":"提示","str":"类型名称不能为空","mark":true,"width":"160px"});
					return false;
				}
				if(sourceTypeNote == ''){
					$(this).Alert({"title":"提示","str":"类型备注不能为空","mark":true,"width":"160px"});
					return false;
				}
				
				
				$http.post(root + 'category/add', {
					outId: shopId,
					parentId: parentId,
					name: sourceTypeName,
					description: sourceTypeNote,
					outType: 'affairType'
				}).success(function(res){
					if(res.status == '0'){
						//刷新来源类型列表
						if(option.callback) option.callback(shopId, {'name': option.sourceName, 'id': option.sourceId});
						$('#sourceTypeName, #sourceTypeNote').val('');
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
		return function(scope,element,attr){
			element.click(function(){
				Source.show(scope.$eval(attr.addSource));
			});
		}
	},
	ngBindClipboard:ngDirectiver(function(scope,element,attr){
		new ZeroClipboard(element,{hoverClass: 'filter_out_sid_on',moviePath: '3rd/ZeroClipboard/ZeroClipboard.swf'});
	}),
	ngSearchPage:ngDirectiver(function(scope,element,attr){//分页input搜索
		element.keyup(function(e){
			 //element.parents(".pDiv2").find(".currentPage").text(element.val());
			 if(e.keyCode == 13){		     
				 if(isNaN(scope.enterPage)){
					 scope.$apply(function(){
						 scope.enterPage=1;
					 });
					  return false;
				  }else{
					  var currentPage=scope.enterPage;
					  if(currentPage<1 || currentPage>scope.totalPage){
						  scope.$apply(function(){
							 scope.enterPage=1;
						 });
						  return false;  
					   }else{
						   scope.currentPage=scope.enterPage;
						   scope.searchOrdersList(parseInt(scope.currentPage));
					   }
				  }

				 
			 }
				
		});
		
	})
	,ngOrderSearchPage:ngDirectiver(function(scope,element,attr){//分页input搜索
		element.keyup(function(e){
			 //element.parents(".pDiv2").find(".currentPage").text(element.val());
			if(e.keyCode == 13){		     
				 if(isNaN(scope.OenterPage)){
					 scope.$apply(function(){
						 scope.OenterPage=1;
					 });
					  return false;
				  }else{
					  var currentPage=scope.OenterPage;
					  if(currentPage<1 || currentPage>scope.OtotalPage){
						  scope.$apply(function(){
							 scope.OenterPage=1;
						 });
						  return false;  
					   }else{
						   scope.OcurrentPage=scope.OenterPage;
						   scope.SOrder(parseInt(scope.OcurrentPage));
					   }
				  }

				 
			 }	
		});
		
	})
	,ngInputPage:ngDirectiver(function(scope,element,attr){//分页input搜索
		element.keyup(function(e){
			 if(e.keyCode == 13){			     
				 if(isNaN(scope.currentPage)){
					 scope.$apply(function(){
					     scope.currentPage=1;					   
					 });
					 //$(".pGroup .pageTips").autoVanishTips({str:"请输入正确的查询范围",timer:1500});
					return false;
				  }else{
					  var currentPage=scope.currentPage;
					  if(currentPage<1 || currentPage>scope.totalPage){
						  scope.$apply(function(){
							 scope.currentPage=1;					   
						 });
						  //$(".pGroup .pageTips").autoVanishTips({str:"请输入正确的查询范围",timer:1500});
						  return false;  
					   }else{
						   scope.searchPage(currentPage);
					   }
				  }

				 
			 }
				
		});
		
	}),
	ngEntryFontlen:ngDirectiver(function(scope,element,attr){
	     element.bind('keyup copy paste blur',function(e){
				 element.parent().find(".entryFontLen").text(element.val().length);					  
		 });
         
	})
}); 

function trim(str){
	str = str.replace(/^(\s|\u00A0)+/,'');
	for(var i=str.length-1; i>=0; i--){
		if(/\S/.test(str.charAt(i))){
			str = str.substring(0, i+1);
			break;
		}
	}
	return str;
}