$(function(){
	$(".searchbox_condition tr:eq(1)").hide();
	$(".unfold").toggle(function(){
		$(".searchbox_condition tr:eq(1)").show();
		$(this).addClass("shrink")
	},function(){
		$(".searchbox_condition tr:eq(1)").hide();
		$(this).removeClass("shrink");
	})
	$(".dunning_tip").live("mouseover",function(){
		$(this).siblings(".kf_tip_box").show();
	});
	$(".dunning_tip").live("mouseout",function(){
		$(this).siblings(".kf_tip_box").hide();
	});
	$(".deal_time").live("mouseover",function(){
		$(this).siblings(".kf_tip_box").show();
	});
	$(".deal_time").live("mouseout",function(){
		$(this).siblings(".kf_tip_box").hide();
	});
	$('#shopinfo').hover(function(){
		$(this).find("dl").stop(true,true).slideDown().siblings('span').addClass("spanHover");
	},function(){
		$(this).find("dl").stop(true,true).hide().siblings('span').removeClass("spanHover");
	}).find('a').live('click',function(){
		$(this).parents('dl').hide();
	});
});

function refund($scope,$http){
	var _startDate = $('#createdStartTime'),
		_endDate = $('#createdEndTime');
		
	$http.get(root+"shop/taobao/list").success(function(res){
		$scope.shops = res.data.shops;
		$scope.init($scope.shops[0]);
		
	});
	$scope.changeShop = function(shop){
		$scope.reset();
		$scope.init(shop,'');
	}
	$scope.init = function(shop){
		if($scope.shopName != shop.shopName){
			$scope.shopName = shop.shopName;
			$scope.shopId = shop.shopId;
			$scope.shopType = shop.shopType;
		}
		$scope.shop = shop;
		
		//加载退款原因
		//$scope.reason=[];
		var parames = {
			"dpId" : $scope.shopId
		};
		$http.post(root + "customerCenter/refund/refundReasons",parames).success(function(res){
			if(res.status=="0"){
				 $scope.reasons = res.data;
			}
		});
		
		refundTab();
		$scope.searchOrdersList();
		
		//初始化 上传控件
		$scope.loadImg = false;
		initFileUpload('upload_button', 'file_list', $scope.shopId, false);
		initFileUpload('msg_upload_button', 'msg_file_list', $scope.shopId, true);
	}
	function getObjInAry(obj,id,key){
		for(var i=0;i<obj.length;i++){
			if(id == obj[i][key]) return obj[i];
		}
	}
	
	
	
	/*时间设置*/
	var newDate = new Date();
	var dated = newDate.setDate(newDate.getDate() -30 );
	var y=newDate.getFullYear();
	var m=newDate.getMonth()+1;
	var d=newDate.getDate();
	var time=y+"-"+m+"-"+d; 
	var d = new Date();
	var year = d.getFullYear();
	var month = d.getMonth()+1;
	var date = d.getDate();
	var time1=year+"-"+month+"-"+date; 
	$scope.date1 = time + " 00:00:00";
	$scope.date2 = time1 + " 23:59:59";
	startVal = $scope.date1;
	endVal = $scope.date2;
	
	/*列表*/
	$scope.ordersList=[];
	$scope.flag = false;

	$scope.reset = function(){
		$scope.reason = '';
		$scope.hasGoodReturn = "";
		$scope.orderStatus = "";
		$scope.buyerNick = "";
		$scope.tid = "";
		$scope.refundId = "";
		$scope.needCustomerService = "";
		$scope.title = "";
		$scope.numIid = "";
		$("#createdStartTime").val('');
		$("#createdEndTime").val(''); 
	}
	
	$scope.searchOrdersList=function(){
	    $scope.master=false;
		$scope.checkIsPager(arguments,$scope,1,20);
		var parame={};
		parame.dpId=$scope.shopId;
		parame.reason = $scope.reason;
		parame.hasGoodReturn= $scope.hasGoodReturn;
		parame.orderStatus= $scope.orderStatus;
		parame.buyerNick= $scope.buyerNick;
		parame.tid= $scope.tid;
		parame.refundId=$scope.refundId;
		parame.createdStartTime=$("#createdStartTime").val();
		parame.createdEndTime=$("#createdEndTime").val();
		parame.needCustomerService= $scope.needCustomerService;
		parame.title= $scope.title;
		parame.numIid= $scope.numIid;
		parame.currentPage = $scope.defaultPage;
		parame.pageSize = $scope.defaultSize;
		parame.status = angular.element(".refundTab li.cur a").attr("_title");
		if(ccms.getSecond($("#createdStartTime").val()) > ccms.getSecond($("#createdEndTime").val())){
			$("#createdStartTime").css("border","1px solid red");
			$("#createdEndTime").css("border","1px solid red");
			return false;
		}else{
			$("#createdStartTime").css("border","1px solid #D9D9D9");
			$("#createdEndTime").css("border","1px solid #D9D9D9");
		}
		$scope.showUrPay = false;
		$http.post(root+"customerCenter/refund/refundList",parame).success(function(res){
			$scope.visit = res.visit;
			if(res.status=="0"){
				var data = res.data; 
				$scope.refunds = data.data;
				$scope.refund = data.statusStatistics.refund;
				$scope.HasSendGoods = data.statusStatistics.onlyRefundHasSendGoods;
				$scope.SendGoods = data.statusStatistics.refundNoSendGoods;
				$scope.WaitBuyer = data.statusStatistics.returnGoodsWaitBuyer;
				$scope.WaitSeller = data.statusStatistics.returnGoodsWaitSeller;
				$scope.SellerConfirm = data.statusStatistics.returnGoodsWaitSellerConfirm;
				$scope.pagersetting = {
					curpage: data.page,
					total: data.total,
					totalPages: data.totalPage,
					rp: data.pageSize,
					submit: function(){
						$scope.searchOrdersList('isPager');
					}
				 }
				$scope.showUrPay = true;
			}
		});

	}
	
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
	$scope.isHide = function(s,o,t){
		if(s == "退款关闭" || s == "退款成功" || (o == "等待卖家发货" && t == "C")){
			return false;
		}else{
			return true;
		}
	}
	
	/*会员等级*/
	$scope.gradeClass = function(name){
		if(name == '新客户'){
			return 'newMember';
		}else if(name == '普通会员'){
			return 'generalMember';
		}else if(name == '高级会员'){
			return 'highMember';
		}else if(name == 'VIP会员'){
			return 'VIPMember';
		}else if(name == '至尊VIP'){
			return 'VIPMember';
		}
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
	$scope.refundDesc = function(m){
		return m ? "comment" : "";
	}
	
	$scope.GoodReturn = function(s){
		return s ? "退货退款" : "仅退款";
	}
	
	$scope.tbStatus = function(t){
		if(t == "" || t == 1){
			return "";
		}else{
			return "小二已介入";
		}
	}
	
	$scope.showTimeout = function(str){
		return str ? '' : ' style="display:none;"';
	}
	
	$scope.refundStatus = function(status){
		if(status == "买家已经申请退款，等待卖家同意"){
			return "refundStatus1";
		}else if(status == "卖家已经同意退款，等待买家退货"){
			return "refundStatus2";
		}else if(status == "买家已经退货，等待卖家确认收货"){
			return "refundStatus2";
		}else if(status == "卖家拒绝退款"){
			return "refundStatus1";
		}else if(status == "退款关闭"){
			return "refundStatus4";
		}else if(status == "退款成功"){
			return "refundStatus5";
		}
	}
	
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
	
	
	function windowWidth() {
		var de = document.documentElement;
		return (de && de.clientWidth)||document.body.clientWidth;
	}

	function changeWindowSize(){
		//var wh=windowHeight();
		if($('#kfzx_searchbox').length){
			/* var Sbox = document.getElementById("kfzx_searchbox").offsetHeight
			document.getElementById("result_order").style.height = (wh - Sbox - 210 )+"px"; */
			
			var ww=windowWidth();
			document.getElementById("PageDive").style.width = (ww - 170 )+"px";
		}
		
	}
	changeWindowSize();
	//window.onload = window.onresize = changeWindowSize;
	$(window).resize(changeWindowSize);
	
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
	function refundTab(){
		$(".refundTab li:eq(0)").addClass("cur").siblings().removeClass("cur");
		$scope.result = false;
		$scope.searchOrdersList();
		$(".refundTab li").click(function(){
			$(this).siblings().removeClass("cur").end().addClass("cur");
			var a_title = $(".refundTab li.cur a").attr("_title");
			//console.log(a_title);
			if(a_title == "refund"){
				$scope.reset();
				$scope.result = false;
				$scope.searchOrdersList();
			}else if(a_title == "refundNoSendGoods"){
				$scope.hasGoodReturn = "false";
				$scope.result = true;
				$scope.orderStatus = "WAIT_SELLER_SEND_GOODS";
				$scope.reason = $scope.buyerNick = $scope.buyerNick = $scope.refundId = $scope.needCustomerService = $scope.title = $scope.numIid = "";
				$("#createdStartTime").val('');
				$("#createdEndTime").val(''); 
				$scope.searchOrdersList();
			}else if(a_title == "onlyRefundHasSendGoods"){
				$scope.hasGoodReturn = "false";
				$scope.orderStatus = "WAIT_BUYER_CONFIRM_GOODS";
				$scope.result = true;
				$scope.reason = $scope.buyerNick = $scope.buyerNick = $scope.refundId = $scope.needCustomerService = $scope.title = $scope.numIid = "";
				$("#createdStartTime").val('');
				$("#createdEndTime").val(''); 
				$scope.searchOrdersList();
			}else if(a_title == "returnGoodsWaitSeller"){
				$scope.hasGoodReturn = "true";
				$scope.orderStatus = "WAIT_BUYER_CONFIRM_GOODS";
				$scope.result = true;
				$scope.reason = $scope.buyerNick = $scope.buyerNick = $scope.refundId = $scope.needCustomerService = $scope.title = $scope.numIid = "";
				$("#createdStartTime").val('');
				$("#createdEndTime").val(''); 
				$scope.searchOrdersList();
			}else if(a_title == "returnGoodsWaitBuyer"){
				$scope.hasGoodReturn = "true";
				$scope.orderStatus = "WAIT_BUYER_CONFIRM_GOODS";
				$scope.result = true;
				$scope.reason = $scope.buyerNick = $scope.buyerNick = $scope.refundId = $scope.needCustomerService = $scope.title = $scope.numIid = "";
				$("#createdStartTime").val('');
				$("#createdEndTime").val(''); 
				$scope.searchOrdersList();
			}else if(a_title == "returnGoodsWaitSellerConfirm"){
				$scope.hasGoodReturn = "true";
				$scope.orderStatus = "WAIT_BUYER_CONFIRM_GOODS";
				$scope.result = true;
				$scope.reason = $scope.buyerNick = $scope.buyerNick = $scope.refundId = $scope.needCustomerService = $scope.title = $scope.numIid = "";
				$("#createdStartTime").val('');
				$("#createdEndTime").val(''); 
				$scope.searchOrdersList();
			}else if(a_title == ""){
				$scope.reason = $scope.buyerNick = $scope.hasGoodReturn = $scope.buyerNick = $scope.orderStatus = $scope.refundId = $scope.needCustomerService = $scope.title = $scope.numIid = "";
				$scope.result = false;
				_startDate.datetimepicker('setDate',startVal).datetimepicker('option', 'maxDate', endVal);
				_endDate.datetimepicker('setDate',endVal).datetimepicker('option', 'minDate',startVal);
				$scope.searchOrdersList();
			}
			
		});
	}


	$scope.scope = $scope;
	$scope.careUrl = 'customerCenter/refund/refundOrdersCare';
	$scope.careTitle = '退货关怀设置';
	$scope.careTypeName = '关怀';
	$scope.careTypes = ["24","25","26"];
	$scope.careSuccess = function(){
		return $scope.init($scope.shop, $scope.abnormalStatus);
	}
	$scope.massCare = function(){
		if(!getSltSize()){
			$(this).Alert({"title":"提示","str":"请至少选择一条需要关怀的订单","mark":true});
			return false;
		}else{
            var arr=[];
			$('[name=refundChb]:checked').each(function(){
				var tid = $(this).attr("tid");
				var oid = $(this).attr("oid");
				var uid = $(this).parents("tr").find(".userId").text();
				var mobile = $(this).attr("mobile");
				arr.push({"tid":tid,"oid":oid,"tel":mobile,"uid":uid});
			});
            $scope.careSetting($scope,arr,$scope.shopId);
		}
	}
	
	//常用 话术/凭证设置
	$scope.showVoucher = function(){
		$scope.voucherTab = true;
		$('#voucher_pop').addInteractivePop({magTitle: "常用凭证/话术配置",mark :true,drag: true,position:"fixed"});
		$http.post(root+'customerCenter/refund/topContentList',{dpId:$scope.shopId}).success(function(res){
			if(res.data.length == 0){
				res.data.push({added: true});
			}
			$scope.topContentList = res.data;
		});
	}
	$scope.delRefundTop = function(id,index){
		if(id){
			$(this).Confirm({'title': '操作确认', 'str': '确认删除该话术？', 'mark': true} ,function(){
				$http.post(root+'customerCenter/refund/topDel', {topId: id});
				$scope.topContentList.splice(index, 1);
			});
		}else{
			$scope.topContentList.splice(index, 1);
		}
	}
	
	/*凭证 */
	window.delVoucher = function(obj){
		var _parent = $(obj).parent(),
			iid = _parent.attr('iid');
		if(iid){
			$http.post(root+'customerCenter/refund/proofFileDel', {proofId: iid});
		}
		_parent.remove();
	}		
	$scope.showFileUpload = function(){
		if(!$scope.loadImg){
			$('#file_list').html('');
			$http.post(root+'customerCenter/refund/proofFilePaths', {dpId: $scope.shopId}).success(function(res){
				$.each(res.data, function(i, n){
					$('#file_list').append('<li iid="'+n.id+'">'+getItmeStr(n)+'</li>');
				});
			});
			$scope.loadImg = true;
		}
		$scope.voucherTab = false;
	}
	
	
	function initFileUpload(targetId, wrapId, shopId, temp){ //targetId -- 上传按钮id; wrapId -- 显示上传文件的id;  shopId -- 店铺id;   temp -- 判断是否是常用凭证/临时凭证
		$('#'+targetId).fileupload({
			url: root+'customerCenter/refund/uploadFile?dpId='+shopId+'&proofFile=proofFile&temp='+temp,
			dataType: 'json',
			add: function(e, data){
				var file = data.files[0],
					fileName = file.name;
				if(!/(\.|\/)(gif|jpe?g|png)$/i.test(fileName)){
					console.log(fileName + '格式不正确');
					return;
				}
				if(file.size > 512000){
					console.log(fileName + '超过500K');
					return;
				}
				data.context = $('<li><div class="progress_bar"><span>0%</span></div></li>').appendTo($('#'+wrapId));
				data.submit();
			},
			done: function (e, data) {
				var obj = data.context,
					data = data.result;
				if(obj){					
					obj.children().fadeOut('fast').remove();
					if(data.status == '0'){
						obj.html(getItmeStr(data.data));
						//如果是常用凭证的话，给li加上id，删除的时候用
						if(targetId == 'upload_button'){
							obj.attr('iid', data.data.id);
						}
					}else{
						obj.html(data.message).css('color','red');
						setTimeout(function(){
							obj.fadeOut().remove();
						}, 3000);
					}
				}
			},
			progress: function(e, data){
				if(data.context){
					var progress = Math.floor(data.loaded / data.total * 100);
					data.context.children().css('width', progress + '%').find('span').html(progress + '%');
				}
			}
		});
	}
	
	function getItmeStr(file){
		return '<a target="_blank" href="'+file.path+'"><img src="'+file.path+'" width="40" height="26" /></a> <a target="_blank" href="'+file.path+'">'+file.fileName+'</a><a href="javascript:void(0);" onclick="delVoucher(this);" class="file_del"></a>';
	}
	// 话术 保存
	$scope.hasModify = function(obj){
		if(obj.id){
			obj.modifyed = true;
		}
	}
	$scope.closeVoucher = function(){
		$('#voucher_pop .title .close').click();
	}
	$scope.saveVoucher = function(){
		if($scope.voucherTab){
			var params = [], modifyed = false, added = false;
			//console.log($scope.topContentList);
			angular.forEach($scope.topContentList, function(v){
				if(v.modifyed) modifyed = true;
				if(v.added && $.trim(v.content)) added = true;
				params.push({'dpId': $scope.shopId, 'topId': v.id, 'topContent': v.content});
			});
			if(modifyed || added){
				$http.post(root+'customerCenter/refund/topContents',params).success(function(res){
					if(res.status == 0){
						$scope.closeVoucher();
						$(this).Alert({"title":"提示","str":"话术保存成功","mark":true});
					}else{
						$(this).Alert({"title":"提示","str":res.message,"mark":true});
					}
				});
			}else{
				$scope.closeVoucher();
			}
		}else{
			$scope.closeVoucher();
		}
	}
	
	
	//上传留言/凭证
	$scope.uploadMsg = {
		refundId: '',
		show: function(refundId){
			$('#upload_msg').addInteractivePop({magTitle: "上传凭证",mark :true,drag: true,position:"fixed"});
			$scope.upRefundId = refundId;
			//加载常用话术
			$http.post(root+'customerCenter/refund/topContentList',{dpId: $scope.shopId}).success(function(res){
				if(res.data && res.data.length){
					$('#msg_area').ccmsAt({at: '/', data: res.data, showAllListId: 'commTopContent' ,showAllListStyle: 'width:360px;max-width:360px;', maxCount: 400});
				}
			});
			
			//加载常用凭证
			$http.post(root+'customerCenter/refund/proofFilePaths', {dpId: $scope.shopId}).success(function(res){
				$scope.proofFile = res.data;
			});
		},
		save: function(){
			var content = $.trim($('#msg_area').val()),
				_tempFile = $('#msg_file_list li'),
				_checkedItem = $('#comm_file_list :checked');
			if(content == ''){
				$(this).Alert({"title":"提示","str":"请输入留言内容","mark":true,"width":"160px"});
				return false;
			}
			/*if(!_tempFile.length && !_checkedItem.length){
				$(this).Alert({"title":"提示","str":"请至少选择一个常用凭证或上传一张临时凭证","mark":true,"width":"160px"});
				return false;
			}
			*/
			var params = {
				dpId: $scope.shopId,
				refundId : $scope.upRefundId,
				content: content,
				filePath: []
			}
			_tempFile.each(function(){
				params.filePath.push($('img', this).attr('src'));
			});
			_checkedItem.each(function(){
				params.filePath.push($(this).next().attr('href'));
			});
			
			$http.post(root + 'customerCenter/refund/refundProof', params).success(function(res){
				$scope.uploadMsg.close();
				$('#msg_area').val('');
				$('#msg_file_list').html('');
				$('#atCounter').html(0);
				$(this).Alert({"title":"提示","str":res.message,"mark":true,"width":"160px"});
			});
		},
		close:function(){
			$('#upload_msg .title .close').click();
		}
	}
}



