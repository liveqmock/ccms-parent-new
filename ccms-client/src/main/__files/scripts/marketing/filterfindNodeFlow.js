$(function(){
//编辑权限判断
isEditNodeTpl();
//加减号的缩进
	$(".smallPlus a").toggle(function(){
    $(this).parents("h5").next().slideUp();
    $(this).text("+");
},function(){
    $(this).parents("h5").next().slideDown();
    $(this).text("-");
});
})
var filterfind = {
	mark:0,//区分name值来通过验证
	method:'',
	delcons:[],
	delqueries:[],
	nodeId:nodeTplId,
	orderTradeFromDic:[],
	orderStatusDic:[
			{"value":10,"name":"已下单未付款"},
			{"value":20,"name":"有效交易"},
			{"value":21,"name":"&nbsp;&nbsp;&nbsp;&nbsp;|---已付款未发货"},
			{"value":22,"name":"&nbsp;&nbsp;&nbsp;&nbsp;|---已发货待确认"},
			{"value":23,"name":"&nbsp;&nbsp;&nbsp;&nbsp;|---交易成功"},
			{"value":30,"name":"交易失败  "}
	],
	orderQuotaDic:[
		{"name":"购买金额（元）","value":111,"referType":"NUMBER"},
		{"name":"购买件数（件）","value":112,"referType":"NUMBER"},
		{"name":"购买次数（人-店-天）","value":113,"referType":"NUMBER"},
		{"name":"最后一次购买间隔（天）","value":114,"referType":"TIME"},
		{"name":"第一次购买间隔（天）","value":115,"referType":"TIME"},
		{"name":"最后一次购买时间","value":116,"referType":"DATETIME"},
		{"name":"第一次购买时间","value":117,"referType":"DATETIME"},
		{"name":"平均客单价","value":118,"referType":"TIME"},
		{"name":"平均购买周期（天）","value":119,"referType":"TIME"},
		{"name":"退款订单数","value":120,"referType":"NUMBER"},
		{"name":"退款金额","value":121,"referType":"NUMBER"}

	],
	orderQuotaTypeDic:[
		{"name":"大于","value":"GT"},
		{"name":"小于","value":"LT"},
		{"name":"大于或等于","value":"GE"},
		{"name":"小于或等于","value":"LE"},
		{"name":"等于","value":"EQ"},
		{"name":"不等于","value":"NE"}
	],
	orderTimeSltAry:[{"name":"下单时间","value":54},{"name":"付款时间","value":59},{"name":"发货时间","value":62},{"name":"变更时间","value":64},{"name":"结束时间","value":55}],
	attrHTml:function(mark,key,name,operators,sltValue,value,queryType,attrId){//relation：关系；key：属性的key；name：属性名；operators：属性关系；sltValue：选中的属性关系；valued：属性值
		var length = $('#nodeQueryCustomer tbody tr').length,
			disbledStr = length ? ' disabled="disabled"' : '',
			typeStr = '',
			index = mark+1,
			delClass = length ? 'nodeQueryCustomerDel' : 'resetItem',
			delStr = length ? '删除' : '重置';
		if(operators && sltValue){
			typeStr = this.getSltSelcted(operators,sltValue);
		}
		var html = '<tr index="'+index+'">'+
			 '<td><input type="hidden" class="attrId" value="'+attrId+'" /><select name="relation" class="borderHighlight relationSlt"'+disbledStr+'><option value="AND">并且</option><option value="OR">或者</option></select></td>'+
			 '<td class="showQueryPop">'+
			 	'<input onclick="filterfind.showQueryPop($(this));" readonly="readonly" name="queryInput_'+index+'" class="borderHighlight nodeQuerySearch" queryType="'+queryType+'" key="'+key+'" value="'+name+'">'+
			 	'<a onclick="filterfind.showQueryPop($(this));" href="javascript:void(0);" class="addAttribute"></a></td>'+
			 '<td><select class="borderHighlight queryCustomerType" style="width:73px;" name="op">'+typeStr+'</select></td>'+
			 '<td><span class="queryCustomerValue"></span></td>'+
			 '<td><div class="tool_wrap clearfix"><a onclick="filterfind.addItem($(this));" class="addAttrItem" href="javascript:void(0);" title="新增"></a>'+
			 	' <a onclick="filterfind.cloneItem($(this),\'attr\');" class="cloneItem" href="javascript:void(0);" title="复制"></a>'+
			 	' <a onclick="filterfind.delItem($(this),\'attr\');" class="'+delClass+'" href="javascript:void(0);" title="'+delStr+'"></a></div>'+
		'</td></tr>';
		return html;
	},
	orderhtml:function(opts){
		var _this = this,
			length = $('#nodeQueryOrder tbody tr').length,
			timeType = $('#marketingCampNodeQuery [name=timeType]:checked').val(),
			disbledStr = length ? ' disabled="disabled"' : '',
			orderTimeValue = '',
			absStartTime = '',absEndTime = '',
			relStartTime = '',relEndTime = '',
			hideOtherTime = function(type){
				return timeType == type ? '' : ' style="display:none"';
			},
			delStr = length ? '删除' : '重置',
			delClass = length ? 'nodeQueryCustomerDel' : 'resetItem',
			index = _this.mark + 1;

		if(!_this.orderTradeFromDic.length){
			$.ajax({//得到交易来源的选项
				url:root+'meta/dic/57/value',
				type:'GET',
				async: false,
				success:function(data){
					_this.orderTradeFromDic = data;
				}
			});
		}

		var orderStartTimeId = '',
			orderEndTimeId = '',
			orderTradeFromId = '',
			orderStatusId = '',
			orderQuotaId = '',
			orderTypeId = '',
			cons = opts.cons,
			orderTypeSlt = _this.getSltSelcted([{"name":"是","value":"LIKE"},{"name":"否","value":"NOTLIKE"}],cons && cons.OrderType ? cons.OrderType.op.value : ''),
			orderTradeFromSlt = _this.getSltSelcted(_this.orderTradeFromDic,cons && cons.OrderTradeFrom ? cons.OrderTradeFrom.values : ''),
			orderStatusSlt = _this.getSltSelcted(_this.orderStatusDic,cons && cons.OrderStatus ? cons.OrderStatus.values : ''),
			orderQuota = _this.getSltSelcted(_this.orderQuotaDic,cons && cons.OrderQuota ? cons.OrderQuota.key : ''),
			orderQuotaType = _this.getSltSelcted(_this.orderQuotaTypeDic,cons && cons.OrderQuota ? cons.OrderQuota.op.value : '');
		if(cons){
			if(cons.OrderStartTime){
				orderTimeValue = cons.OrderStartTime.key;
				var startTime = $.parseJSON(cons.OrderStartTime.values).value,endTime = $.parseJSON(cons.OrderEndTime.values).value;
				if(timeType == '2'){
					absStartTime = startTime;
					absEndTime = endTime;
				}else{
					relStartTime = startTime;
					relEndTime = endTime;
				}
				orderStartTimeId = cons.OrderStartTime.id;
			}
			if(cons.OrderEndTime){
				orderEndTimeId = cons.OrderEndTime.id
			}
			if(cons.OrderType){
				orderTypeId = cons.OrderType.id;
			}
			if(cons.OrderTradeFrom){
				orderTradeFromId = cons.OrderTradeFrom.id;
			}
			if(cons.OrderStatus){
				orderStatusId = cons.OrderStatus.id
			}
			if(cons.OrderQuota){
				orderQuotaId = cons.OrderQuota.id;
			}
		}

		var html = '<tr index="'+index+'">'+
			'<td><input type="hidden" class="attrId" value="'+(opts.id || '')+'" /><select class="borderHighlight relationSlt" name="relation"'+disbledStr+'><option value="AND">并且</option><option value="OR">或者</option></select></td>'+
			'<td>'+
				'<select class="borderHighlight" name="orderTime"><option></option>'+_this.getSltSelcted(_this.orderTimeSltAry,orderTimeValue)+'</select>'+
				'<span class="abs_time_wrap" stimeType="2"'+hideOtherTime(2)+'><input class="borderHighlight datetimepicker disabled" type="text" value="'+absStartTime+'" required cid="'+orderStartTimeId+'" name="absStartTime_'+index+'" readOnly="readOnly" disabled="disabled" /> - <input class="borderHighlight datetimepicker disabled" type="text" value="'+absEndTime+'" cid="'+orderEndTimeId+'" name="absEndTime_'+index+'" required readOnly="readOnly" disabled="disabled" /></span>'+
				'<span class="rel_time_wrap" stimeType="1"'+hideOtherTime(1)+'><input class="borderHighlight datetimepicker disabled" type="text" value="'+relStartTime+'" cid="'+orderStartTimeId+'" name="relStartTime_'+index+'" required readOnly="readOnly" disabled="disabled" /> - <input class="borderHighlight datetimepicker disabled" type="text" value="'+relEndTime+'" cid="'+orderEndTimeId+'" name="relEndTime_'+index+'" required readOnly="readOnly" disabled="disabled" /></span>'+
			'</td>'+
			'<td><select class="borderHighlight" name="isBuy_'+index+'"><option value=""></option>'+_this.getSltSelcted([{"name":"是","value":"true"},{"name":"否","value":"false"}],opts.isBuy)+'</select></td>'+
			'<!--<td><select class="borderHighlight"><option>小野香水</option></select></td>-->'+
			'<!--<td><input class="borderHighlight nodeQuerySearch"></td>-->'+
			'<td><select class="borderHighlight" cid="'+orderTradeFromId+'" name="orderTradeFrom_'+index+'"><option></option>'+orderTradeFromSlt+'</select></td>'+
			'<td><select class="borderHighlight" cid="'+orderTypeId+'" name="orderType_'+index+'"><option></option>'+orderTypeSlt+'</select></td>'+
			'<td><select class="borderHighlight" cid="'+orderStatusId+'" name="orderStatus_'+index+'"><option></option>'+orderStatusSlt+'</select></td>'+
			'<td><select class="borderHighlight quotaInput orderQuotaB" cid="'+orderQuotaId+'" name="orderQuota_'+index+'" required><option></option>'+orderQuota+'</select></td>'+
			'<td>'+
				'<select class="borderHighlight quotaInput" name="orderQuotaType_'+index+'" required><option value=""></option>'+orderQuotaType+'</select>'+
				'<span class="quotaInputWrap"></span>'+
			'</td>'+
			'<td><div class="tool_wrap clearfix"><a href="javascript:void(0);" class="addAttrItem" onclick="filterfind.addOrderItem({\'addObj\':$(this).parents(\'tr\')});" title="新增"></a>'+
				' <a href="javascript:void(0);" class="cloneItem" onclick="filterfind.cloneItem($(this),\'order\');" title="复制"></a>'+
				' <a href="javascript:void(0);" class="'+delClass+'" onclick="filterfind.delItem($(this),\'order\');" title="'+delStr+'"></a></div></td>'+
		'</tr>';
		return html;
	},
	init:function(event,treeId,treeNode){
		var _this = this;
		$.ajax({
			url:root+'node/query/config/'+this.nodeId,
			type:'GET',
			cache:false,//ie下面的缓存
			async:false,//改为同步请求以防止用户不断刷新该页面导致数据会重复添加
			success:function(data){
				if(data.queries.length){
					$('#marketingCampNodeQuery').find('[name=exclude]').attr('checked',data.exclude == 'true' ? true : false).end().find('[name=timeType][value='+data.timeType+']').attr('checked',true);
					var customLength = 0,orderLength = 0;
					$.each(data.queries,function(i,n){
						if(n.code == 'CUSTOMER'){//属性查询
							$('#property').val(n.id);
							$.each(n.cons,function(k,v){
								_this.addAttrItem(v,n.options);
								customLength++;
							});
						}else if(n.code == 'ORDER'){//订单消费查询
							_this.addOrderItem(n);
							orderLength++;
						}
					});
					if(!customLength) _this.addAttrItem();
					if(!orderLength) _this.addOrderItem();
					_this.method = 'PUT';
				}else{
					_this.addAttrItem();
					_this.addOrderItem();
				}
			}
		});
		//验证
		$('#marketingCampNodeQuery').find('[name=timeType]').change(function(){
			var val = this.value;
			$('#nodeQueryOrder tbody tr').each(function(){
				var el = $(this),refertype = el.find('[name^=orderQuota_] :selected').attr('refertype');
				el.find('[stimeType='+val+']').show().siblings('[stimeType]').hide();
				if(/DATETIME/.test(refertype)){
					_this.setQuotaInput(refertype,el,'');
				}
			});
		}).end().validate({
			submitHandler:function(){
				_this.saveData();
			}
		});

		//初始化zTree
		attrPop.init($("#zTreeToQuery"),_this.attrPopClick);
		_this.initTimePop();
	},
	initTimePop:function(){
		//消费信息查询  相对时间 初始化
		$('#relTimePop :radio').click(function(){
			$(this).parent().find(':text').attr('disabled',false).end().siblings().find(':text').attr('disabled',true).removeClass('error').val('');
		});
		$('#relTimePop p span').click(function(){
			$(this).parent().find(':radio').click();
		});

		$('#closeTimePop').click(function(){
			$('#relTimePop .close').click();
		});

		$('.relSecondTime').timepicker({timeFormat:'HH:mm:ss',showSecond:true});

		$('#relTimePop').validate({
			submitHandler:function(){
				var _pop = $('#relTimePop'),
					chcekdIndex =_pop.find(':radio').index(_pop.find(':checked')),
					_text = _pop.find('p').eq(chcekdIndex).find(':text:visible'),
					val = '';
				if(chcekdIndex == 0){
					var secTime = _text.eq(1).val() || '';
					val = '前'+_text.eq(0).val()+'天 '+secTime;
				}else if(chcekdIndex == 1){
					val = '前'+_text.eq(0).val()+'分钟';
				}else if(chcekdIndex == 2){
					var secTime = _text.eq(2).val() || '';
					val = '前'+_text.eq(0).val()+'月,第'+_text.eq(1).val()+'号 '+secTime
				}
				$('.showPop').val(val);
				_pop.find('.close').click();
			}
		});
	},
	addOrderItem:function(opts){
		var opts = $.extend({addObj:''},opts),
			_this = this,
			obj = $(_this.orderhtml(opts)),
			_orderQuota = obj.find("[name^=orderQuota_]"),
			orderTime = obj.find('[name=orderTime]'),
			orderTimeInput = obj.find('[stimetype] input'),
			index =  _this.mark + 1,
			orderQuotaValue = '',
			deReferType = '',
			timePatt = /TIME/,
			relation = opts.relation || $('#nodeQueryOrder tbody tr:first .relationSlt').val();
		_this.bindEvent(obj,opts.addObj,'nodeQueryOrder',relation);

		orderTime.change(function(){//改变下单时间联动
			if(this.value){
				orderTimeInput.attr('disabled',false).removeClass('disabled');
			}else{
				orderTimeInput.val('').attr('disabled',true).addClass('disabled').removeClass('error').siblings('.error').remove();;
				//在选择指标时禁用订单时间时，使其时间限制为空
				obj.find('[name^=absStartTime_]').datetimepicker('option','maxDate',null);
				obj.find('[name^=absEndTime_]').datetimepicker('option','minDate',null);
			}
		});
		//当用户操作过消费查询时给购买与否加上必填的验证
		obj.find('select').change(function(){
			obj.find('[name^=isBuy_]').addClass('required');
		});
		//指标值
		if(opts.cons && opts.cons.OrderQuota){
			orderQuotaValue = opts.cons.OrderQuota.values;
			deReferType = _orderQuota.find(':selected').attr('refertype');
		}
		
		_this.setQuotaInput(deReferType,obj,orderQuotaValue);
		_orderQuota.change(function(){
			_this.setQuotaInput($(':selected',this).attr('refertype'),obj,'');
		});
		
		if(!orderTime.attr('disabled') && orderTime.val()){
			orderTimeInput.attr('disabled',false).removeClass('disabled');
		}
		//指标 disabled
		obj.find('.quotaInput').attr('disabled',obj.find("[name^='isBuy_']").val() == 'true' ? false : true);
		obj.find("[name^='isBuy_']").change(function(){
			//指标 禁用
			obj.find('.quotaInput').val('').attr('disabled',this.value == 'true' ? false : true).removeClass('error').siblings('.error').remove();
			//订单时间 禁用
			if(this.value != 'true'){
				orderTime.attr('disabled',false);
				orderTimeInput.each(function(){
					if(this.disabled && orderTime.val()){
						$(this).attr('disabled',false).removeClass('disabled');
					}
				});
			}
		});

		//obj.find('.abs_time_wrap input').datetimepicker({timeFormat:'HH:mm:ss',showSecond:true});
		var _orderST = obj.find('[name^=absStartTime_]'),
			_orderET = obj.find('[name^=absEndTime_]');
			
		_orderST.datetimepicker({
			timeFormat:'HH:mm:ss',
			showSecond:true,
			changeMonth:true,
			changeYear:true,
			onClose: function(dateText){
				if(dateText){
					_orderST.datetimepicker('setDate', dateText);
					var startDate = _orderST.datetimepicker('getDate'),
						endDate = _orderET.datetimepicker('getDate');
					// if (endDate && startDate > endDate){
						// _orderET.datetimepicker('setDate', startDate);
					// }
					
					_orderET.datetimepicker('option', 'minDate', dateText);
				}
			}
		});
		_orderET.datetimepicker({
			timeFormat:'HH:mm:ss',
			showSecond:true,
			changeMonth:true,
			changeYear:true,
			onClose: function(dateText) {
				if(dateText){
					_orderET.datetimepicker('setDate', dateText);
					var startDate = _orderST.datetimepicker('getDate'),
						endDate = _orderET.datetimepicker('getDate');
					// if (startDate > endDate){
						// _orderST.datetimepicker('setDate', endDate);
					// }
					_orderST.datetimepicker('option', 'maxDate', dateText);
				}
			}
		});
		//初始化  若保存的为绝对时间且订单时间有值时，设置起始时间和结束时间的限制
		if(_orderST.val() && $('[name=timeType]').val() == '2'){
			_orderST.datetimepicker('option', 'maxDate', _orderET.val());//设置开始时间的选择范围
			_orderST.datetimepicker('setDate',_orderST.val()); //设置范围后重新设置时间，不然没有时分秒
			
			_orderET.datetimepicker('option', 'minDate', _orderST.val());
			_orderET.datetimepicker('setDate',_orderET.val());
		}
		
		//打开相对时间弹出框
		_this.showRelTime(obj.find('.rel_time_wrap input'),false);
		_this.mark++;
	},
	setQuotaInput:function(referType,obj,orderQuotaValue){
		var _this = this,
			index = _this.mark + 1,
			_orderTimeSlt = obj.find('[name=orderTime]'),
			orderTimeInput = obj.find('[stimetype] input');
		if(orderQuotaValue){
			orderQuotaValue = /DATETIME/.test(referType) ? $.parseJSON(orderQuotaValue).value : orderQuotaValue;
		}else{
			index = obj.attr('index');
		}
		if(/DATETIME/.test(referType)){
			obj.find('.quotaInputWrap').html('<input class="borderHighlight quotaInput" readOnly="readOnly" id="quotaInput_'+index+'" name="quotaInput_'+index+'" required type="text" value="'+orderQuotaValue+'" />');
			//判断是相对时间还是绝对时间
			var _input = obj.find('.quotaInputWrap input');
			if($('[name=timeType]:checked').val() == '2'){
				obj.find('.quotaInputWrap input').datepicker();
			}else{
				_this.showRelTime(_input,true);
			}
		}else{
			obj.find('.quotaInputWrap').html('<input class="borderHighlight quotaInput digits" id="quotaInput_'+index+'" name="quotaInput_'+index+'" required type="text" value="'+orderQuotaValue+'" />');
		}
		
		if(referType){
			if(/TIME/.test(referType)){
				orderTimeInput.attr('disabled',true).val('').removeClass('error').addClass('disabled').siblings('.error').remove();
				//在指标切换时，若选中为时间类型的，则默认是检索的下单时间
				_orderTimeSlt.attr('disabled',true).find('[value=54]').attr('selected',true);
				//在选择指标时禁用订单时间时，使其时间限制为空
				obj.find('[name^=absStartTime_]').datetimepicker('option','maxDate',null);
				obj.find('[name^=absEndTime_]').datetimepicker('option','minDate',null);
			}else{
				orderTimeInput.attr('disabled',false).removeClass('disabled');
				_orderTimeSlt.attr('disabled',false);
			}
		}
	},
	showRelTime:function(obj,hideSec){
		obj.click(function(){
			var val = this.value,
				valAry = val.split(' '),
				_radio = $('#relTimePop :radio'),
				type = 0,
				getTxt = function(type){
					return _radio.eq(type).parent().find(':text');
				};
			var $tr=$(this).parents("tr");
			
            var orderQuotaSecd=$tr.find(".orderQuotaB").val();
			if(orderQuotaSecd==117 || orderQuotaSecd==116){
				 $('#relTimePop').children("p:eq(1)").hide();
			}else{
				 $('#relTimePop').children("p:eq(1)").show();
			}
			//$('[name="orderQuota_"+filterfind.mark]').val();
			if(val){
				if(val.indexOf('天') > 0){
					type = 0;
					getTxt(type).last().val(valAry[1]);
				}else if(val.indexOf('分钟') > 0){
					type = 1;
				}else if(val.indexOf('月') > 0){
					type = 2;
					getTxt(type).eq(1).val(val.split(',')[1].match(/\d+/)[0]).end().last().val(valAry[1]);
				}
				getTxt(type).eq(0).val(valAry[0].match(/\d+/)[0]);
			}
			_radio.eq(type).click();
            
			$('.showPop').removeClass('showPop');
			$(this).addClass('showPop');
			$('#relTimePop').addInteractivePop({position:"fixed",magTitle:"选择相对时间（输入的时间必须为整数）",mark:true});

			$('.relSecondTime').css('display',hideSec ? 'none' : 'inline');
		});
	},
	addAttrItem:function(opts,options){
		opts = $.extend({
			addObj:'',
			relation:'',
			key:'',
			labelName:'',
			op:{value:''},
			values:'',
			queryType:'',
			id:''
		},opts);
		var addObj = opts.addObj,
			relation = opts.relation || $('#nodeQueryCustomer tbody tr:first .relationSlt').val(),
			key = opts.key,
			name = opts.labelName,
			sltValue = opts.op.value,
			value = opts.values,
			type = opts.queryType,
			id = opts.id,
			_this = this;
		if(options){
			var operators = options.operators[key],dicvalues = options.dicvalues[key];
		}

		var obj = $(_this.attrHTml(_this.mark,key,name,operators,sltValue,value,type,id));
		_this.bindEvent(obj,addObj,'nodeQueryCustomer',relation);
		//设置属性值的类型
		_this.mark++;
		_this.setQueryType(type,obj,key,dicvalues,value,false,name);
	},
	bindEvent:function(obj,addObj,wrapId,relation){
		if(addObj){
			obj.insertAfter(addObj);
		}else{
			obj.appendTo($('#'+wrapId+' tbody'));
		}

		//设置关系  AND OR
		obj.find('.relationSlt').find('option[value='+relation+']').attr('selected',true).end().change(function(){
			obj.siblings().find('.relationSlt option[value='+this.value+']').attr('selected',true);
		});
	},
	showQueryPop:function(obj){
		$("#nodeQueryPop").attr('index',obj.parents('tr').attr('index')).addInteractivePop({position:"fixed",magTitle:"属性",mark:true});
	},
	addItem:function(obj){//添加属性
		this.addAttrItem({'addObj':obj.parents('tr')});
	},
	cloneItem:function(obj,type){//复制
		var _this = this,
			_p = obj.parents('tr');
		if(type == 'attr'){
			_this.addAttrItem($.extend(_this.getAttrData(_p).consData,{"addObj":_p,"id":""}),_this.getAttrData(_p).options);
		}else{
			_this.addOrderItem($.extend(_this.getOrderData(_p),{"addObj":_p,"id":""}));
		}
	},
	delItem:function(obj,type){//删除or重置
		var _parent = obj.parents('tr'),
			_valueObj = _parent.find('.queryCustomerValue').children(),
			attrId = _parent.find('.attrId').val();

		if(_parent.index() == 0){//重置 -- 先新增再删除
			obj.siblings('.addAttrItem').click();
			_parent.next().find('.nodeQueryCustomerDel').attr({'title':'重置','class':'resetItem'}).end().find('.relationSlt').attr('disabled',false);
		}

		//将删除的属性id放为数组中保存时传递
		if(attrId){
			if(type == 'attr'){
				this.delcons.push(attrId);
			}else{
				this.delqueries.push(attrId);
			}
		}
		_parent.remove();
	},
	setQueryType:function(type,obj,key,dicvalues,value,index,labelName){//index为tr的索引
		var sltStr = '',
			value = value || '',
			nameKey = (index || this.mark)+'_'+(key || ''),
			valObj = obj.find('.queryCustomerValue'),
			disabledStr = !labelName && !value ? ' disabled="disabled"' : '';
		//判断属性值为下拉框还是文本框
		if(type == "DIC" || type == "ORDERED_DIC"){
			if(dicvalues){//不为undefined的话就是读取之前保存的数据，否则为弹开属性窗口选择后请求下拉框类型
				sltStr = this.getSltSelcted(dicvalues,value);
			}else{
				$.ajax({
					type : 'GET',
					async : false,
					url : root+'meta/dic/'+key+'/value',
					success:function(data){
						$.each(data,function(i,n){
							sltStr += '<option value="'+n.value+'">'+n.name+'</option>';
						});
					}
				});
			}
			valObj.html('<select style="width:157px;" class="borderHighlight" name="name_'+nameKey+'">'+sltStr+'</select>');
		}else{
			if(type == 'BIRTHDAY'){
				var value = $.parseJSON(value || '{"type":"","value":""}'),
					sltValue = value.type,
					birthdayType = [{"name":"绝对日期","value":1},{"name":"当天的前几天","value":2},{"name":"当天的后几天","value":3}];
				valObj.html('<select style="width:157px;" class="borderHighlight" name="birthdaySlt" onchange="filterfind.birthdayChange($(this),\''+nameKey+'\');">'+this.getSltSelcted(birthdayType,value.type)+'</select><span><input'+disabledStr+' name="name_'+nameKey+'" required type="text" class="borderHighlight" value="'+value.value+'" style="width:148px;"/></span>');
			}else{
				var perStr = '',inputW = '148px',maxRuleClass = '';
				if(labelName == '买家好评率'){
					perStr = '<span style="display:inline-block;width:18px;text-align:center;">%</span>';
					inputW = '130px';
					maxRuleClass = ' max="100"';
				}
				valObj.html('<input'+disabledStr+' name="name_'+nameKey+'" required'+maxRuleClass+' type="text" class="borderHighlight" value="'+value+'" style="width:'+inputW+';"/>'+perStr);
			}

			var valueInput = valObj.find('input');
			if(type == 'BIRTHDAY' && (sltValue !='' && sltValue !='1') || type == 'NUMBER' || labelName == '手机'){//数字
				valueInput.addClass('number');
			}
			if(type == 'BIRTHDAY' && (sltValue == '1' || !sltValue)){//绝对日期
				valObj.find('input').attr('readOnly',true).datepicker({
					dateFormat:'mm-dd',
					changeMonth: true
				});;
			}

			/*
			if(labelName == '手机'){
				valueInput.attr({"maxlength":11});
			}
			if(labelName == '邮箱'){
				valueInput.addClass('email');
			}
			*/


		}
	},
	birthdayChange:function(obj,nameKey){
		var _next = obj.next();
		_next.html('<input name="name_'+nameKey+'" required type="text" class="borderHighlight" value=""/>');
		if(obj.val() == '1'){
			_next.find('input').attr('readOnly',true).datepicker({
				dateFormat:'mm-dd',
				changeMonth: true
			});
		}else{
			_next.find('input').addClass('digits');
		}
	},
	getSltSelcted:function(values,sltValue){
		var sltStr = '';
		$.each(values,function(i,n){
			var disabledStr = n.value == sltValue ? ' selected="selected"' : '',
				referType = n.referType ? ' referType="'+n.referType+'"' : '',
				str = '<option'+referType+' value="'+n.value+'"'+disabledStr+'>'+n.name+'</option>';
			sltStr += str;
		});
		return sltStr;
	},
	saveData:function(){
		var _this = this,querieslength = 0,
			data = {
				"exclude":$('[name=exclude]').is(':checked') ? 'true' : 'false',
				"timeType":$('[name=timeType]:checked').val(),
				"queries":[],
				"delqueries":_this.delqueries
			},
			attrData = {
				"code":"CUSTOMER",
				"cons":{},
				"id":$('#property').val(),
				"delcons":_this.delcons
			};
		$('#nodeQueryCustomer tbody tr').each(function(){
			if($(this).find('[name^=queryInput_]').val()){
				attrData.cons[$(this).attr('index')] = _this.getAttrData($(this)).consData;
			}
		});
		data.queries.push(attrData);

		$('#nodeQueryOrder tbody tr').each(function(){
			if($(this).find('[name=orderTime]').val() || $(this).find('[name^=isBuy_]').val()){
				data.queries.push(_this.getOrderData($(this)));
			}
		});

		//判断 查询条件 是否为0
		$.each(attrData.cons,function(k,v){
			querieslength++;
		});
		$.each(data.queries,function(i,n){
			if(n.code == 'ORDER'){
				querieslength++;
			}
		});
		if(!querieslength){
			$(this).Alert({"title":"提示","str":"请至少选择一种查询条件","mark":true,"width":"160px"});
			return false;
		}

		$.ajax({
			type:'POST',
			data: {
		        "_method": _this.method,
		        "addinfo":JSON.stringify(data)
		    },
			url:root+'node/query/config/'+_this.nodeId,
			success:function(data){
				if(data.val == true){
					$(this).yAlert({"text":"保存成功"});
					removeAlert();
					setSaveFullIcon(_this.nodeId);
					$('#'+_this.nodeId).click();
				}else{
					$(this).Alert({"title":"提示","str":"保存失败","mark":true,"width":"100px"});
				}
			},error:function(){
				$(this).Alert({"title":"提示","str":"网络异常，请稍后再试","mark":true,"width":"160px"});
			}
		});

	},
	getOrderData:function(obj){
		var _orderTime = obj.find('[name=orderTime]'),
			_orderTimeV = obj.find('[stimetype]:visible input'),
			_orderQuota = obj.find('[name^=orderQuota_]'),
			_orderQuotaType = obj.find('[name^=orderQuotaType_]'),
			_orderTradeFrom = obj.find('[name^=orderTradeFrom_]'),
			_orderType = obj.find('[name^=orderType_]'),
			_orderStatus = obj.find('[name^=orderStatus_]'),
			refertype = _orderQuota.find(':selected').attr('refertype'),
			timeType = $('[name=timeType]:checked').val() == '2' ? "ABSTIME" : "RELTIME",
			quotaInputVal = obj.find('[name^=quotaInput_]').val(),
			orderTimeVal = _orderTime.attr('disabled') ? '' : _orderTime.val(),
			orderTimeVS = '',
			orderTimeVE = ''
			orderTimeSid = '',
			orderTimeEid = '';
		if(!_orderTimeV.attr('disabled')){
			orderTimeVS = _orderTimeV.eq(0).val();
			orderTimeVE = _orderTimeV.eq(1).val();
			orderTimeSid = _orderTimeV.eq(0).attr('cid');
			orderTimeEid = _orderTimeV.eq(1).attr('cid');
		}

		var orderData = {
			"id":obj.find('.attrId').val(),
			"code":"ORDER",
			"isBuy":obj.find('[name^=isBuy_]').val(),
			"relation":obj.find('[name=relation]').first().val(),
			"cons":{
				"OrderStartTime":{
					"id":orderTimeSid,
					"key":orderTimeVal,
					"op":{"value":"GE","name":"大于等于"},
					"values":"{\"type\":\""+ timeType +"\",\"value\":\""+orderTimeVS+"\"}"
				},
				"OrderEndTime":{
					"id":orderTimeEid,
					"key":orderTimeVal,
					"op":{"value":"LE","name":"小于等于"},
					"values":"{\"type\":\""+ timeType +"\",\"value\":\""+orderTimeVE+"\"}"
				},
				"OrderTradeFrom":{
					"id":_orderTradeFrom.attr('cid'),
					"key":"57",
					"op":{"value":"EQ","name":"等于"},
					"values":_orderTradeFrom.val()
				},
				"OrderType":{
					"id":_orderType.attr('cid'),
					"key":"58",
					"op":{"value":_orderType.val(),"name":""},
					"values":"step"
				},
				"OrderStatus":{
					"id":_orderStatus.attr('cid'),
					"key":"63",
					"op":{"value":"EQ","name":"等于"},
					"values":_orderStatus.val()
				},
				"OrderQuota":{
					"id":_orderQuota.attr('cid'),
					"key":_orderQuota.attr('disabled') ? '' : _orderQuota.val(),
					"op":{
						"value":_orderQuotaType.val(),
						"name":_orderQuotaType.find(':selected').html()
					},
					"values":refertype == 'DATETIME' ? '{"type":"'+timeType+'","value":"'+quotaInputVal+'"}' : quotaInputVal,
					"referType":refertype
				}
			}
		};
		return orderData;
	},
	getAttrData:function(obj){
		var birthdaySlt = obj.find('[name=birthdaySlt]'),
			key = obj.find('.nodeQuerySearch').attr('key'),
			consData = {
				"key":key,
				"labelName":obj.find('.nodeQuerySearch').val(),
				"relation":obj.find('.relationSlt').val(),
				"values":obj.find('.queryCustomerValue').children().val(),
				"op":{
					"name":obj.find('.queryCustomerType :selected').html(),
					"value":obj.find('.queryCustomerType').val()
				},
				"queryType":obj.find('.nodeQuerySearch').attr('querytype')
			},
			options = {
				"dicvalues":{},
				"operators":{}
			},
			dicvalues = [],
			operators = [];
			//options.dicvalues[key] = [],
			//options.operators[key] = [];

		obj.find('.queryCustomerType option').each(function(){
			operators.push({"name":this.innerHTML,"value":this.value});
		});
		obj.find('.queryCustomerValue select[name!=birthdaySlt] option').each(function(){
			dicvalues.push({"name":this.innerHTML,"value":this.value});
		});
		options.dicvalues[key] = dicvalues;
		options.operators[key] = operators;

		if(birthdaySlt.length){//如果是生日的话
			consData.values = '{"type":"'+birthdaySlt.val()+'","value":"'+birthdaySlt.next().find('input').val()+'"}';
		}
		if(obj.find('.attrId').val()){
			consData.id = obj.find('.attrId').val();
		}
		return {"consData":consData,"options":options};
	},
	attrPopClick:function(event,treeId,treeNode){
		var zTree = $.fn.zTree.getZTreeObj("zTreeToQuery");
		if (treeNode.isParent) {
			zTree.expandNode(treeNode);
			return false;
		}else{
			var index = $("#nodeQueryPop").attr('index'),_this = $('#nodeQueryCustomer tbody tr[index='+index+']'),key = treeNode.key;
			//$.get('data/findNode/attribute/key/operator.json',{"key":key},function(data){
			$.get(root+'meta/attribute/'+key+'/operator',function(data){
				_this.find('.queryCustomerType').html('');
				$.each(data,function(i,n){
					_this.find('.queryCustomerType').append('<option value="'+n.value+'">'+n.name+'</option>');
				});
				filterfind.setQueryType(treeNode.queryType,_this,key,'','',index,treeNode.labelName);
			},"JSON");

			_this.find('.showQueryPop input').val(treeNode.labelName).attr({'key':key,'querytype':treeNode.queryType})
			$("#nodeQueryPop .close").click();
		}
	}
}

filterfind.init();