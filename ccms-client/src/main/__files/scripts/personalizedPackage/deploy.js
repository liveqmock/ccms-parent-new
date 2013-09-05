// JavaScript Document
//主控制star
function deployCtr($scope,$http,$window){	

//淘宝店铺调取&&跳转判断
	var skipPlansHash=/=([0-9]+)$/.exec(location.hash);
	skipPlansIndex=skipPlansHash?skipPlansHash[1]:0;
	var selectShopId,plans,viewShopName;
		$http.get(root+"shop/taobao/list?_="+new Date().getTime()).success(function(d){
			$scope.shops=d.data.shops;
			viewShopName=d.data.shops[0].shopName;
			selectShopId=$scope.nowShopId=d.data.shops[0].shopId;
			cookiesObj.setcookiesMeth(viewShopName,selectShopId,false);
			$scope.shopModuleVal=cookiesObj.getcookiesMeth().shopName;	
			selectShopId=cookiesObj.getcookiesMeth().shopId;
			getShopInfo(selectShopId);//方案数据
		});
		
//店铺切换
	$scope.init=function(id,name){
		selectShopId=$scope.nowShopId=id;
		viewShopName=$scope.shopModuleVal=name;
		cookiesObj.setcookiesMeth(viewShopName,selectShopId,true);
		getShopInfo(selectShopId);
		
	}

	
//编辑
	$(".schemeList .xgListName").die("click").live("click",function(){
		var activeFalg=$(this).attr("active");
		if(activeFalg=="true"){return false;}
		$(this).siblings("a").hide();
		$(".schemeAlert").hide();
		$(this).siblings("input").show().focus().addClass("blue");
	})
	
	$(".schemeList .cur input").die("blur").live("blur",function(){
		var $inputThis=$(this);
		var plansVal=$.trim($inputThis.val());
		if(plansVal.length>20 || plansVal.length==0 ){
			$inputThis.Alert({"title":"提示","str":"方案名称最大长度为20字符且不为空！","mark":true,"width":"160px"},function(){
				$inputThis.val("").focus();
				$inputThis.siblings(".viewListVal").html("");
			});
			return false;
		}
		$(this).hide().removeClass("blue");;
		$(this).siblings(".viewListVal").text(plansVal).attr("title",plansVal).show();
	})
	
	$(".schemeHeader .schemeEditor").die("click").live("click",function(){
		$(this).html("[完成]");
		$(this).siblings("em").hide();
		$(this).siblings("input").show().focus();
	});
	$(".schemeHeader .inputName").die("blur").live("blur",function(){
		var thisVal=$.trim($(this).val()),
			valLen=thisVal.length;
			$inputThis=$(this);
			if(thisVal && (valLen <= 20) ){
				$inputThis.siblings("a").html("[编辑]");
				$inputThis.hide();
				$inputThis.siblings("em").text(thisVal).css("display","inline-block").attr("title",thisVal);
			}else{
				$inputThis.Alert({"title":"提示","str":"请填写规则名称，最大长度为20！","mark":true,"width":"160px"},function(){$inputThis.val("").focus()});
			} 
	});
	
	$(".conditionBox .termListBox").live({
		"mouseenter":function(){$(this).find("a").css("display","inline-block");},
		"mouseleave":function(){$(this).find("a").hide();}
	});

//初始化position和优先级
	function intMethod(){
		var $scheme=$(".schemeBox .scheme");
		var sumSchemeLen=$scheme.length;
		return [$scheme,sumSchemeLen]
	}
	function startPositionImg(){
		var resultInt=intMethod();
		resultInt[0].find(".fr a").css("display","inline-block");
		resultInt[0].eq(0).find("a.topRank,a.addRank").hide();
		resultInt[0].eq(resultInt[1]-1).find("a.lowestRank,a.lessRank").hide();
		$(".conditionBox .termListBox a").hide();
		sortPosition();	
	}
	function sortPosition(){
		var eleAry=intMethod();
		eleAry[0].each(function(index){
			$(this).find(".priority b").text(index+1);
		});
	}
//改变优先级  
	function changePosition(){
		//highest
		$(".schemeHeader .topRank").die("click").live("click",function(){
			$(this).closest(".scheme").prependTo(".schemeBox");
			startPositionImg();
		});
		//up
		$(".schemeHeader .addRank").die("click").live("click",function(){
			var $moveElementUp=$(this).closest(".scheme");
			$moveElementUp.after($moveElementUp.prev());
			startPositionImg();
		});
		//lowest
		$(".schemeHeader .lowestRank").die("click").live("click",function(){
			$(this).closest(".scheme").appendTo(".schemeBox");
			startPositionImg();
		});
		//down
		$(".schemeHeader .lessRank").die("click").live("click",function(){
			var $moveElementDown=$(this).closest(".scheme");
			$moveElementDown.before($moveElementDown.next());
			startPositionImg();
		});
	}
	changePosition();
//textarea 高度
	function setTextAreaH(ele,flag){//add od less
		var tdH;
		if(flag){
			tdH=(ele.closest(".termEditorBox").find(".termListBox").length)*17;
		}else{
			tdH=((ele.closest(".termEditorBox").find(".termListBox").length)-1)*17;
		}
		ele.css("height",tdH);
	}
//初始化条件选择	
	function initPopData(){
		$http.get(root+"rulecenter/condition/type/list").success(function(d){
			$scope.types=d.data;
			typeId=d.data[0].typeId;
			$scope.typeVal=d.data[0].typeName;
			setAjaxProp(typeId,true);
		});	
	}
	initPopData();
	function setAjaxProp(tId,changeFlag,data){
		var reloadData;
		$http.get(root+"rulecenter/condition/type/"+tId+"/property/list?_="+new Date().getTime()).success(function(d){
			$scope.choseLists=d.data;
			if(changeFlag){
				propId=d.data[0].id;
				$scope.choseVal=d.data[0].name;
				setAjaxContent(propId,true);
			}
			if(data){//修改初始化
				reloadData=data;
				var propertyId=reloadData.propertyId,firstpropertyName;
				$.each(d.data,function(key,val){
					if(propertyId == val.id){
						firstpropertyName=val.name;						
					}							  
				});
				$scope.choseVal=reloadData.propertyName?reloadData.propertyName:firstpropertyName;
				//商品
				if($scope.choseVal=="包含商品"){
					$scope.productSelectedIds=reloadData.referenceValue;//商品选择器
					var findProductLen=($scope.productSelectedIds).split(",").length;
					$(".editorPop .productMark,.editorPop .productView").css("display","inline-block");
					$scope.productViewName="您已选中"+findProductLen+"件商品";
				}else if($scope.choseVal=="收货人所在地"){
					$(".editorPop .cityList").show();
				}else{
					$(".editorPop .productMark,.editorPop .productView,.editorPop .cityList").hide();
				}
				defaultNameMethod(true,reloadData);
			}
		});
	}
	
	function setAjaxContent(pId,num,data){//是否重新填充满足条件数据
		$http.get(root+"rulecenter/condition/property/"+pId+"?_="+new Date().getTime()).success(function(d){
			var loadData,provinceIdOth,defaultCityVal;
			if(d.data.providedValues.length!=0){
				$scope.providedValues=d.data.providedValues;
				$scope.termSelectTwoVal=d.data.providedValues[0].name;
				$(".editorPop .termEditor,.editorPop .productMark,.editorPop .productView").hide();
				$(".editorPop .termSelectTwo").show();
				if(605==pId){//省市
					$(".editorPop .cityList").show();
					if(!data){//非修改
						provinceIdOth=d.data.providedValues[0].id;
						setCityData(provinceIdOth);
					}
				}else{
					$(".editorPop .cityList").hide();	
				}
			}else{
				$(".editorPop .termSelectTwo,.editorPop .cityList").hide();
				if(608==pId){//商品
					$(".editorPop .termEditor").hide();
					$(".editorPop .productMark,.editorPop .productView").css("display","inline-block");
				}else{
					$(".editorPop .productMark,.editorPop .productView").hide();
					$(".editorPop .termEditor").show();
				}
			}
			if(num){
				$scope.contentTerms=d.data.supportOps;
				$scope.termSelectOneVal=d.data.supportOps[0].label;
				if(data){
					loadData=data;
					var conditionId=loadData.conditionOpName,firstOptionVal;
					$.each(d.data.supportOps,function(key,val){
						if(conditionId == val.name){
							firstOptionVal=val.label;						
						}							  
					});
					$scope.termSelectOneVal=loadData.conditionOpVal?loadData.conditionOpVal:firstOptionVal;
					var referenceId,firstReferenceVal;
					if(605!=pId){//非省市
						referenceId=loadData.referenceValue;
					}else{
						referenceId=(loadData.referenceValue.split(","))[0];
						defaultCityVal=(loadData.referenceValue.split(","))[1];
					}
					$.each(d.data.providedValues,function(key,val){
						if(referenceId == val.value){
							firstReferenceVal=val.name;	
							provinceIdOth=val.id;
						}							  
					});
					$scope.termSelectTwoVal=loadData.referenceViewName?loadData.referenceViewName:firstReferenceVal;
					if(605==pId){setCityData(provinceIdOth,defaultCityVal,true)};
				}
			defaultNameMethod(true,loadData);
			}
		});
	}
	
	function setCityData(provinceId,defaultCityVal,flag){//flag 修改| select触发
		$http.get(root+"rulecenter/condition/region/"+provinceId+"?_="+new Date().getTime()).success(function(response){
			response.data.push({"name":"不限","value":"不限"});																									  
			$scope.citys=response.data;	
			if(flag && defaultCityVal){//修改初始化
				$scope.cityVal=defaultCityVal;
			}else{
				$scope.cityVal="不限";
			}
		});
	}
//end
//默认条件名称
function defaultNameMethod(changeFlag,d,oneFlag){
	/*angularJs 加载bug  param(changeFlag)-区别termSelectTwo与其他的选择框bug
	 * param(oneFlag)区别termSelectOne与指标选择框bug
	*/
	if($(".editorPop .checkBox").is(":checked")){
		var firstStr,twoStr,threeStr;
		var productFlag=($(".editorPop .productView").css("display"))=="none";
		var showFlag=($(".editorPop .termEditor").css("display"))=="none";
		var cityFlag=($(".editorPop .cityList").css("display"))=="none";
		if(changeFlag){
			firstStr=$scope.choseVal;
			if(!oneFlag){
				twoStr=$scope.termSelectOneVal;
			}else{
				twoStr=$(".popTermContent .termSelectOne ").val();
			}
			threeStr;
			if(showFlag && productFlag){//下拉
				if(cityFlag){
					threeStr=$scope.termSelectTwoVal;
				}else{//省市
					if(d){
						var cityDefaultVal=(d.referenceValue.split(","))[1]?(d.referenceValue.split(","))[1]:"不限";//初始化修改
					}
					$scope.cityVal=cityDefaultVal?cityDefaultVal:$scope.cityVal;
					//console.log($scope.cityVal)
					if(!$scope.cityVal){$scope.cityVal="不限"};
					threeStr=$scope.termSelectTwoVal+$scope.cityVal;	
				}
			}else if(!showFlag && productFlag){//editor
				threeStr=$(".popTermContent .termEditor").val();
			}else{//商品
				threeStr=$scope.productViewName?$scope.productViewName:"";
			}
		}else{
			firstStr=$(".popChoseList select").val();
			twoStr=$(".popTermContent .termSelectOne ").val();
			threeStr;
			if(showFlag && productFlag){
				if(cityFlag){
					threeStr=$(".popTermContent .termSelectTwo").val();
				}else{
					var thisClassFlag=/termSelectTwo/.test($(this).attr("class"));//province 触发
					if(!thisClassFlag){//city select触发
						threeStr=$(".popTermContent .termSelectTwo").val()+$(".popTermContent .cityList").val();
					}else{//province 触发
					//console.log($scope.cityVal+"|"+$(".popTermContent .cityList").val())
						$scope.cityVal="不限";
						threeStr=$(".popTermContent .termSelectTwo").val()+$scope.cityVal;
					}
				}
			}else if(!showFlag && productFlag){
				threeStr=$(".popTermContent .termEditor").val();
			}else{
				threeStr=$scope.productViewName?$scope.productViewName:"";
			}
		}
		$(".editorPop .editorName").val("").val(firstStr+twoStr+threeStr);
	}else{
		if(d){
			$(".editorPop .editorName").val(d.name);	
		}
	}
}
$(".popTermContent .termEditor").unbind("keyup").bind("keyup",function(){
	defaultNameMethod(false);																	   
});
//获取筛选条件数据&&筛选条件确认Btn
    function setContentBtn(th,ele,len,btnFlag){//条件父级Div、条件长度
		var setNowLen=len,typeId,propId;		
		var $popType=$(".editorPop .popType"),$popChoseList=$(".editorPop .popChoseList"),$popTermContent=$(".editorPop .popTermContent");
		$popType.unbind("change");
		$popChoseList.unbind("change");
		$(".editorPop .editorName").val("");
		if(btnFlag){//添加or修改
			$(".editorPop .editorName").prop("disabled",true);
			$(".editorPop .checkBox").prop("checked",true);
			$(".editorPop .termEditor").val("");
			$scope.productViewName="";
			$scope.productSelectedIds="";
		}else{
			//获取data
			var divIntData=$(th).closest(".termListBox").data();
			$(".editorPop .checkBox").prop("checked",divIntData.useDefaultName);
			$(".editorPop .editorName").prop("disabled",divIntData.useDefaultName);	
			$(".editorPop .popType option").each(function(){
				if(divIntData.type==$(this).attr("id")){
					$(this).prop("selected",true);
				}else{
					$(this).prop("selected",false);
				}
			});
			
			setAjaxProp(divIntData.type,false,divIntData);
			setAjaxContent(divIntData.propertyId,true,divIntData);
			if(divIntData.hasProvideValues){//下拉and弹框
				$(".editorPop .termEditor").hide();
				$(".editorPop .productView").hide();
				$(".editorPop .termSelectTwo").show();
			}else{
				$(".editorPop .termEditor").show();
				$(".editorPop .termSelectTwo").hide();
				$(".editorPop .productView").hide();
				$(".editorPop .termEditor").val(divIntData.referenceValue);
			}
		};

		$popType.unbind("change").bind("change",function(){
			$scope.productViewName="";
			$scope.productSelectedIds="";
			typeId=$("option:selected",this).attr("id");
			setAjaxProp(typeId,true);
		});
		
		$popChoseList.unbind("change").bind("change",function(){
			$scope.productViewName="";
			$scope.productSelectedIds="";
			$scope.cityVal="";
			propId=$("option:selected",this).attr("id");
			$(".editorPop .termEditor").val("");
			setAjaxContent(propId,true);

		});
		$popTermContent.find("select.termSelectOne").unbind("change").bind("change",function(){
			$(".editorPop .termEditor").val("");
			pId=$(this).closest(".popTermContent").siblings(".popChoseList").find("option:selected").attr("id");
			setAjaxContent(pId,false);
			//defaultNameMethod(false);
			defaultNameMethod(true,"",true);//第二个param angularJs 数据加载bug
		});
		$popTermContent.find("select.termSelectTwo").unbind("change").bind("change",function(){
			if("收货人所在地"==$scope.choseVal){
				var provinceId=$("option:selected",this).attr("id");
				setCityData(provinceId,"",false);	
			}
			defaultNameMethod.apply(this,[false]);																			 
		});
		
		$popTermContent.find("select.cityList").unbind("change").bind("change",function(){
			defaultNameMethod.apply(this,[false]);																			 
		});
	
		
		function getSaveData(){
			var popObj={};
			popObj.type=$("option:selected",$popType).prop("id");
			popObj.conditionVal=$("option:selected",$popType).val();
			popObj.propertyId=$("option:selected",$popChoseList).attr("id");
			popObj.propertyName=$("option:selected",$popChoseList).val();
			popObj.conditionOpName=$(".termSelectOne option:selected",$popTermContent).attr("name");
			popObj.conditionOpVal=$(".termSelectOne option:selected",$popTermContent).val();
			popObj.termEditorText=$(".editorPop .termEditor").val();
			var productFlag=($(".editorPop .productView").css("display"))=="none";
			var showFlag=($(".editorPop .termEditor").css("display"))=="none";
			var cityFlag=($(".editorPop .cityList").css("display"))=="none";
			if(showFlag && productFlag){//下拉
				var selectReferenceValue=$(".termSelectTwo option:selected",$popTermContent).attr("name"),
					selectReferenceViewName=$(".termSelectTwo option:selected",$popTermContent).attr("value");
				if(!cityFlag){//商品
					var cityVal=$(".cityList option:selected",$popTermContent).attr("name"),
						cityName=$(".cityList option:selected",$popTermContent).attr("value");
					popObj.referenceValue=cityName!="不限"?(selectReferenceValue+","+cityName):selectReferenceValue;
					popObj.referenceViewName=selectReferenceViewName;
				}else{
					popObj.referenceValue=selectReferenceValue;
					popObj.referenceViewName=selectReferenceViewName;
				}
				popObj.hasProvideValues=true;
				//console.log(popObj.referenceValue+"|"+popObj.referenceViewName)
			}else if(!showFlag && productFlag){//editor
				popObj.referenceViewName=popObj.referenceValue=$(".editorPop .termEditor").val();
				popObj.hasProvideValues=false;
			}else{//商品
				popObj.referenceViewName=$scope.productViewName?$scope.productViewName:"";//商品选择器无法angularJS获取
				popObj.referenceValue=$scope.productSelectedIds;
				popObj.hasProvideValues=false;	
			}
			if($(".editorPop .checkBox").is(":checked")){
				popObj.useDefaultName=true;	
			}else{
				popObj.useDefaultName=false;	
			}
			popObj.name=$(".editorPop .editorName").val();
			//console.log(popObj)
			return popObj;
		}
		$(".deployBtnBox .saveBtn").unbind("click").bind("click",function(){
			var $thisOne=$(this);
			var fillVal=getSaveData();
			var trimVal=$.trim(fillVal.name);
			var flagOne=(trimVal=="" || (trimVal.length>30));
			if(flagOne){
				$thisOne.Alert({"title":"提示","str":"请填写条件名称，最大长度为30！","mark":true,"width":"160px"},function(){
					$thisOne.closest(".editorPop").find(".editorName").val("").focus();
				});
				return false;
			}
		//条件输入框判断
			var flagTwo=$(".editorPop .termEditor").css("display")!="none";
			var flagThree=$(".editorPop .productView").css("display")!="none";
			if(flagTwo){
				var flagThree=/^0.+|[^0-9]/gi.test($.trim(fillVal.referenceValue));
				if(flagThree || ($.trim(fillVal.referenceValue)=="" || ($.trim(fillVal.referenceValue).length>9))){
					$(this).Alert({"title":"提示","str":"请正确填写条件，填写整数字,最大长度为9！","mark":true,"width":"160px"},function(){
						$(".editorPop .termEditor").val("").focus();
					});
					return false;
				};
			}
			if(flagThree){
				var productVal=fillVal.referenceValue?$.trim(fillVal.referenceValue):"";
				if(!productVal){
					$(this).Alert({"title":"提示","str":"请选择商品！","mark":true,"width":"160px"});	
					return false;
				}
			}
			var appendEleContent="";
			if(btnFlag){//添加
				var thFlag=$(th).closest(".termContent").find("option:selected").attr("val");
				if(setNowLen==0){
					ele.removeClass("undefined");
					ele.empty();//情况未定义的
					appendEleContent="<div class='termListBox' id=''><div class='termContent clearfix'><p class='fl'><span class='othSpan' title='"+fillVal.name+"'>"+fillVal.name+"</span></p><p class='fr'><a class='addTermContent' href='javascript:void(0)' title='增加'></a><a class='modifyTermContent' href='javascript:void(0)' title='修改'></a></p></div></div>";
				}else{
					appendEleContent="<div class='termListBox cur' id='"+fillVal.id+"'><div class='termContent clearfix'><p class='fl'><select><option val='AND'>并且</option><option val='OR'>或者</option></select><em>|</em><span class='curSpan' title='"+fillVal.name+"'>"+fillVal.name+"</span></p><p class='fr'><a class='addTermContent' href='javascript:void(0)' title='增加'></a><a class='modifyTermContent' href='javascript:void(0)' title='修改'></a><a class='delTermContent' href='javascript:void(0)' title='删除'></a></p></div></div>";
				};
				ele.append(appendEleContent);
				ele.find(".termListBox:eq("+setNowLen+")").data(fillVal);
				ele.find(".termListBox:eq("+setNowLen+") option").each(function(){
					var lastVal=$(this).attr("val");
					if(lastVal==thFlag){
						$(this).prop("selected",true);
					}else{
						$(this).prop("selected",false);
					}
				})
				var eleArea=$(th).closest(".termEditorBox").find(".pTextArea textarea");//计算宽度
				setTextAreaH(eleArea,true);
			}else{
				$(th).closest(".termListBox").data(fillVal).find("span").html(fillVal.name).attr("title",fillVal.name);
			}
				
			$(this).closest(".editorPop").hide();
			$(".yunat_maskLayer").remove();

		//绑定条件数据	
		//alert(ele.find(".termListBox"))
			methodBox();
		});
		$(".editorPop .checkBox").unbind("click").bind("click",function(){
			var checkFlag=$(this).prop("checked");
			$(this).siblings(".editorName").prop("disabled",checkFlag);
			if($(this).is(":checked")){
				defaultNameMethod(false);	
			}else{
				$(this).siblings(".editorName").val("");	
			}

		});
	}
//筛选条件取消Btn
	$(".deployBtnBox .cancelBtn").die("click").live("click",function(){
		$(this).closest(".editorPop").hide();
		$(".yunat_maskLayer").remove();
	})
	function methodBox(){//待数据加载完后，再调去，解决缓存严重问题
		//增加条件
		 $(".termListBox .addTermContent").die("click").live("click",function(){
		 	initPopData();
			var $parentInsert=$(this).closest(".conditionBox");
			var contentLen=$parentInsert.find(".termListBox").length;
			if(contentLen>29){$(this).Alert({"title":"提示","str":"规则条件个数小于30！","mark":true,"width":"160px"});return false;};
			$(".editorPop .title").remove();
			$(".editorPop").addInteractivePop({magTitle:"规则条件增加",mark:true,drag:true,position:"fixed"});
			setContentBtn(this,$parentInsert,contentLen,true);
		 });
		//修改条件
		 $(".termListBox .modifyTermContent").die("click").live("click",function(){
			var $parentInsert=$(this).closest(".conditionBox");
			var contentLen=$parentInsert.find(".termListBox").length;
			//console.log($(this).closest(".termListBox").data())
			$(".editorPop .title").remove();
			$(".editorPop").addInteractivePop({magTitle:"规则条件编辑",mark:true,drag:true,position:"fixed"});
			setContentBtn(this,$parentInsert,contentLen,false);
		 });
		//条件删除
		 $(".termListBox .delTermContent").die("click").live("click",function(){
		// $(".termListBox").delegate(".delTermContent","click",function(){
			var $this=$(this);
			$this.Confirm({"title":"删除提示","str":"确认删除该条件？","mark":true},function(){
				var eleArea=$this.closest(".termEditorBox").find(".pTextArea textarea");
				setTextAreaH(eleArea,false);
				$this.closest(".termListBox").remove();		
			});
		  })
		//未定义 编辑
		$(".undefined .termEditor").die("click").live("click",function(){
			initPopData();
			var $parentInsert=$(this).closest(".conditionBox");
			var contentLen=$parentInsert.find(".termListBox").length;
			$(".editorPop .title").remove();
			$(".editorPop").addInteractivePop({magTitle:"规则条件编辑",mark:true,drag:true,position:"fixed"});
			setContentBtn(this,$parentInsert,contentLen,true);
		});
		 //删除规则
		  $(".schemeHeader .delRank").die("click").live("click",function(){
			var $this=$(this);
			if(($(".ccmsPublicPopBg").length<1)){
				$this.Confirm({"title":"删除提示","str":"确认删除该规则？","mark":true},function(){
					$this.closest(".scheme").remove();
					startPositionImg();
				});
			}
		  });
		 //并且或者
		 $(".termListBox select").die("change").live("change",function(){
		 	var selectVal=$("option:selected",this).attr("val");
			$(this).closest(".conditionBox").find("option").each(function(){
				var optionAndFlag=$(this).attr("val")==selectVal;
				if(optionAndFlag){
					$(this).prop("selected",true);
				}else{
					$(this).prop("selected",false);
				}
			})
		 }) 
		 
	}
	
//获取店铺信息 
	function getShopInfo(shopId){
		
		$http.get(root+"rulecenter/planGroup/"+shopId+"?_="+new Date().getTime()).success(function(d){
			plans=d.data.plans;
			var plansNowLen=plans.length;
			if(!plans || ""==plans){
				$(".schemeAlert").show();
				//$schemeList.hide().eq(0).show();
				return "方案不存在"
			}else{
				$(".schemeAlert").hide();
			};
			$(".schemeList ul").empty();
			$.each(plans,function(key,val){
				var liTitleEle='<li><input type="text" value="'+val.name+'" /><a class="viewListVal" id="'+val.id+'" href="javascript:void(0)" title="'+val.name+'">'+val.name+'</a><a class="xgListName" href="javascript:void(0)" active="'+val.active+'"  title="编辑方案名称"></a></li>';
				$(".schemeList ul").append(liTitleEle);
			})
	
			//初始化
			var rulesDataOne=plans[skipPlansIndex].rules,
				rulesActive=plans[skipPlansIndex].active;
			$(".schemeList ul li:eq("+skipPlansIndex+")").addClass("cur");
			getPlanRules(rulesDataOne,rulesActive);
		}).error(function(){alert("error")});
		
	}
	
	function getPlanRules(rulesData,rulesActive){//获取规则
		$scope.rulesInfos=rulesData;
//循环加入条件
		$(".schemeBox").empty();
		$.each(rulesData,function(index,val){
			var $scheme="<div class='scheme' id='"+val.id+"'><div class='schemeHeader clearfix'><div class='fl'><span class='editor'><input class='inputName' type='text' value='"+val.name+"' /><em title='"+val.name+"'>"+val.name+"</em><a class='schemeEditor' href='javascript:void(0)' title='编辑规则名称'>[编辑]</a></span><span class='priority'>优先级：<b>"+val.position+"</b></span></div><div class='fr'><a class='topRank' href='javascript:void(0)' title='最高优先级'></a><a class='addRank' href='javascript:void(0)' title='提高优先级'></a><a class='lessRank' href='javascript:void(0)' title='降低优先级'></a><a class='lowestRank' href='javascript:void(0)' title='最低优先级'></a><a class='delRank' href='javascript:void(0)' title='删除'></a></div></div><div class='term'><table cellpadding='0' cellspacing='0' width='100%'><tr><th width='50%' class='borderright'><span class='spanOne'>条件</span></th><th><span>订单备注内容</span></th></tr></table></div><div class='termEditorBox'><table cellpadding='0' cellspacing='0' width='100%'><tr><td class='borderTd' width='50%'><div class='conditionBox'></div></td><td class='backgroundTd'><p class='pTextArea'><textarea>"+val.remarkContent+"</textarea></p></td></tr></table></div>";
			$(".schemeBox").append($scheme);		
			var key=index;
			$.each(val,function(indexOth,valOth){
				if(indexOth=="conditions"){
					var objCondition=valOth;
					$.each(objCondition,function(fillKey,fillVal){
							var element;
							if(fillKey==0){
								elementObj="<div class='termListBox' id='"+fillVal.id+"'><div class='termContent clearfix'><p class='fl'><span class='othSpan' title='"+fillVal.name+"'>"+fillVal.name+"</span></p><p class='fr'><a class='addTermContent' href='javascript:void(0)' title='增加'></a><a class='modifyTermContent' href='javascript:void(0)' title='修改'></a></p></div></div>";
							}else{
								elementObj="<div class='termListBox cur' id='"+fillVal.id+"'><div class='termContent clearfix'><p class='fl'><select><option val='AND'>并且</option><option val='OR'>或者</option></select><em>|</em><span class='curSpan' title='"+fillVal.name+"'>"+fillVal.name+"</span></p><p class='fr'><a class='addTermContent' href='javascript:void(0)' title='增加'></a><a class='modifyTermContent' href='javascript:void(0)' title='修改'></a><a class='delTermContent' href='javascript:void(0)' title='删除'></a></p></div></div>";
							}
							$(".conditionBox:eq("+index+")").append(elementObj);
							if("OR"==fillVal.relation){
								$(".conditionBox:eq("+index+")").find("select").val("或者");	
							}else if("AND"==fillVal.relation){
								$(".conditionBox:eq("+index+")").find("select").val("并且");	
							}
							var $textarea=$(".termEditorBox .pTextArea textArea:eq("+index+")");
							setTextAreaH($textarea,true);
							var openInitObj={};//初始化绑定数据
							openInitObj.name=fillVal.name;
							openInitObj.useDefaultName=fillVal.useDefaultName;
							openInitObj.type=fillVal.type;
							openInitObj.propertyId=fillVal.propertyId;
							openInitObj.conditionOpName=fillVal.conditionOpName;
							openInitObj.referenceValue=fillVal.referenceValue;
							openInitObj.hasProvideValues=fillVal.hasProvideValues;
							//openInitObj.termEditorText=fillVal.termEditorText;
							
							$(".conditionBox:eq("+index+")").find(".termListBox:eq("+fillKey+")").data(openInitObj);
							//$(".conditionBox:eq("+index+")").find(".termListBox:eq("+fillKey+")").attr("p","sasas");
							//console.log(openInitObj)
					})
				}
			})
		})
	
		startPositionImg();
		methodBox();
		if(rulesActive){
			$(".maincontainer").append("<div class='unEditorMark'></div>");
			$(":.termEditorBox .termContent select").addClass("unEditor");
			$(".buttonBox,.editor .schemeEditor,.schemeHeader div.fr").hide();
			$(".schemeList .xgListName").addClass("unxgListName").attr("title","方案已开启，不可编辑");
		}else{
			$(".maincontainer .unEditorMark").remove();
			$(".termEditorBox .termContent select").removeClass("unEditor");
			$(".buttonBox,.editor .schemeEditor,.schemeHeader div.fr").show();
			$(".schemeList .xgListName").removeClass("unxgListName").attr("title","编辑方案名称");
		};
	}
		
//切换
	$(".schemeList .viewListVal").die("click").live("click",function(){
		$(this).closest("ul").find("li").removeClass("cur").end().find("input,.xgListName").hide();
		$(this).parent().addClass("cur").find(".xgListName").css("display","inline-block");
		var index=$(".schemeList li").index($(this).closest("li"));
		$http.get(root+"rulecenter/planGroup/"+selectShopId+"?_="+new Date().getTime()).success(function(d){
			plans=d.data.plans;
		})
		if(plans[index]){getPlanRules(plans[index].rules,plans[index].active);}
	})

//添加规则	
	$(".buttonBox .ruleBtn").unbind("click").bind("click",function(){
		var date=new Date();
		var dataMonth=((date.getMonth()+1)>=10)?(date.getMonth()+1):"0"+(date.getMonth()+1);
		var dataDate=(date.getDate()>=10)?date.getDate():"0"+date.getDate();
		var dataHours=(date.getHours()>=10)?date.getHours():"0"+date.getHours();
		var dataMinutes=(date.getMinutes()>=10)?date.getMinutes():"0"+date.getMinutes();
		var dataSecondx=(date.getSeconds()>=10)?date.getSeconds():"0"+date.getSeconds();
		var dateMark=dataMonth+"-"+dataDate+"&nbsp;"+dataHours+":"+dataMinutes+":"+dataSecondx;
		var maxLength=($(".schemeBox .scheme").length+1);
		var addRuleName="规则名称："+dateMark;
		if(maxLength<=10){
			var elementStr="<div class='scheme' id=''><div class='schemeHeader clearfix'><div class='fl'><span class='editor'><input class='inputName' type='text' value='"+addRuleName+"' /><em title='"+addRuleName+"'>"+addRuleName+"</em><a class='schemeEditor' href='javascript:void(0)' title='编辑规则名称'>[编辑]</a></span><span class='priority'>优先级：<b>"+maxLength+"</b></span></div><div class='fr'><a class='topRank' href='javascript:void(0)' title='最高优先级'></a><a class='addRank' href='javascript:void(0)' title='提高优先级'></a><a class='lessRank' href='javascript:void(0)' title='降低优先级'></a><a class='lowestRank' href='javascript:void(0)' title='最低优先级'></a><a class='delRank' href='javascript:void(0)' title='删除'></a></div></div><div class='term'><table cellpadding='0' cellspacing='0' width='100%'><tr><th width='50%' class='borderright'><span class='spanOne'>条件</span></th><th><span>订单备注内容</span></th></tr></table></div><div class='termEditorBox'><table cellpadding='0' cellspacing='0' width='100%'><tr><td class='borderTd' width='50%'> <div class='undefined  conditionBox clearfix'><p class='fl'>未定义</p><a class='termEditor fr' href='javascript:void(0)' title='定义'></a></div></td><td class='backgroundTd'><p class='pTextArea'><textarea></textarea></p></td></tr></table></div>";
			$(".schemeBox").append(elementStr);
		}else{
			$(this).Alert({"title":"提示","str":"每分方案的规则上限为10！","mark":true,"width":"100px"});
		}
		startPositionImg();
		methodBox();
	});




//保存方案
	$(".buttonBox .saveBtn").unbind("click").bind("click",function(){
		var saveObj={};
		var $liCur=$(".schemeList li.cur");
		saveObj.id=$liCur.find(".viewListVal").attr("id");
		saveObj.name=$liCur.find(".viewListVal").text();
		saveObj.shopId=selectShopId;
		saveObj.rules=[];
		
		var $saveScheme=$(".schemeBox .scheme");
		$saveScheme.each(function(){
			var $this=$(this),rule={};
			rule.id=$this.attr("id");
			rule.name=$this.find(".editor em").text();
			rule.position=$this.find(".priority b").text();
			rule.remarkContent=$this.find(".pTextArea textarea").val();
			rule.conditions=[];
			var $saveTermListBox=$this.find(".conditionBox .termListBox");
			$saveTermListBox.each(function(){
				var $conditionEle=$(this),condition={};
				if($conditionEle.attr("id")!="undefined" && $conditionEle.attr("id")!="null" && $conditionEle.attr("id")!=""){
					condition.id=$conditionEle.attr("id");
				};
				condition.relation=$conditionEle.find("select option:selected").attr("val");
				condition.name=$conditionEle.find(".termContent span").text();
				condition.type=$conditionEle.data("type");
				condition.useDefaultName=$conditionEle.data("useDefaultName");
				condition.propertyId=$conditionEle.data("propertyId");
				condition.conditionOpName=$conditionEle.data("conditionOpName");
				condition.referenceValue=$conditionEle.data("referenceValue");
				rule.conditions.push(condition);
			});
			saveObj.rules.push(rule);
		});
		
		//console.log(saveObj);
//验证
		function contentValTest(){
			var conFalg=true;
			$(".termEditorBox textarea").each(function(){
				var contentVal=$.trim($(this).val());
				if(contentVal=="" || (contentVal.length>100)){
					conFalg=false	
				}
			})
			return conFalg;
		}
		
		function  conditionsLength(){
			var lengthFalg=true;
			$(".termEditorBox .conditionBox").each(function(){
				var l=$(this).find(".termListBox").length;
				if(0==l){
					lengthFalg=false;
				}
			})
			return lengthFalg;
		}
		var outConFlag=contentValTest();
		if(!outConFlag){
			$(this).Alert({"title":"提示","str":"请正确填写订单备注内容,最大长度为100！","mark":true,"width":"160px"});
			return false;
		}
		
		if(!saveObj.name){
			$(this).Alert({"title":"提示","str":"请先配置方案名称！","mark":true,"width":"160px"});
			return false;
		}
		
		var conditionsFlag=conditionsLength();
		if(!conditionsFlag){
			$(this).Alert({"title":"提示","str":"请先配置方案的条件！","mark":true,"width":"160px"});
			return false;
		}
		var savePlanId=$(".schemeList li.cur .viewListVal").attr("id");
		$.ajax({
			"url":root+"rulecenter/plan/"+savePlanId,
			"type":"POST",
			"dataType":"JSON",
			"cache":true,
			"contentType": "application/json; charset=utf-8",
			"data":JSON.stringify(saveObj),
			"success":function(postData){
				if(-1==postData.status){
					$(this).Alert({"title":"保存失败","str":postData.message,"mark":true,"width":"160px"})	
				}else{
					$(this).yAlert({"text":"保存成功"});
					removeAlert();	
				}
			},
			"error":function(){
				$(this).Alert({"title":"提示","str":"网络异常，请稍后再试！","mark":true,"width":"160px"});
			}
		});
		
	});

//弹框预演方案
	$scope.popPreview=function(){
		var planId=[];
		planId.push($(".schemeList li.cur .viewListVal").attr("id"));
		var aryLen=(planId.length-1);
		if(planId){
			$(this).Alert({"title":"提示","str":"当前无方案可预览！","mark":true,"width":"160px"});
		}else{
			$http.get(root+"rulecenter/plan/"+planId+"/preview?_="+new Date().getTime()).success(function(d){
				var previewData=d.data;
				$scope.previewData=previewData;
				$scope.previewRules=previewData.plans[aryLen].rules;
				$scope.handledPercent=((previewData.handled/previewData.total)*100).toFixed(2);
				$("#startRehearsal").addInteractivePop({magTitle:"方案预演",mark:true,drag:true,position:"fixed"});
			});
		}
	};
	
/*商品选择plug*/	
$scope.proSel=function(){
	var url1='rulecenter/shop/'+selectShopId+'/products',url2='rulecenter/shop/'+selectShopId+'/products';ids=$scope.productSelectedIds?($scope.productSelectedIds).split(","):[];
	$scope.searchProductListUrl=url1;
	$scope.findProductListUrl=url2;
	$scope.findIds=ids;              //ids为查询商品id数组
	$scope.productSelect="templates/productSelected/goodsList.html?_="+new Date().getTime();
}
$scope.popupProSel=function(){
	 $(".productSelected").addInteractivePop({position:"fixed",magTitle:"商品选择",mark:true});
}
$scope.returnProductParame=function(findIds,num,listArr){
	 $scope.productSelectedIds=(this.findIds).join(",");
	 if(this.checkedGoodsLen!=0){
		$scope.productViewName="您已选中"+this.checkedGoodsLen+"件商品";
	 }else{
		$scope.productViewName="";	
	 }
	 $scope.checkedGoodsData=this.findGoods;
}
/*商品选择end*/	
}
//controller end

