//查看报告共用方法
	//最大化
function maxWindow(){
	$('.marketingCampNodeBg .fullTable').toggle(function(){
		var windowsWidth = $(document).width();
		var windowsHeight = $(window).height();
		$(this).parents('.marketingCampNodeBg').addClass('fullWindow').css('width',windowsWidth-10)
		$(this).parents('.marketingCampNode').css('height',windowsHeight-12);
		$(this).addClass('minTable');
	},function(){
		$(this).parents('.marketingCampNodeBg').removeClass('fullWindow').removeAttr('style');
		$(this).parents('.marketingCampNode').removeAttr('style');
		$(this).removeClass('minTable');
	});
}
//关闭
function closeReport(id){
	$('.marketingCampNodeBg .closeTable').bind("click",function(){
		$(this).parents('.marketingCampNodeBg').hide();
		$(id).show();
	})
}

var campId=window.location.hash.split(':')[1];
function isEditNodeTpl(type){
	 if(tplAuthorizationFlag.indexOf("W")<0){
		  disabledToForm(true);
	 }else{
		  $.ajax({
				 url:root+'schedule/campaign/'+campId+'/snapshot',
				 cache:false,
				 async:true,
				 type:"GET",
				 dataType:"json",
				 success:function(response){
						  var data=response.data;
						  var campId=data.campaignId;
						  campStatus=data.campaignStatus;
						  if(campStatus!="A1"){
							  disabledToForm(true);
						  }else{
	
							  disabledToForm(false);
						  };
						  if(type=="tcommunicateSMS"){
							  if(campStatus=="B1" || campStatus=="B3" ){
								  disabledToForm(false);
							  }
						  }
				}
          }); 
	  }
	 
}
function setSaveFullIcon(nodeId){
     $(".graphRect").each(function(){
	       var id=$(this).attr("id");
		   if(id==nodeId){
			    $(this).parent().find(".unfilled").hide();
		   }
	 });
}
function validationNodeFull(fnc){
		 $.ajax({
		     url:root+"workflow/validate/campaign/"+campId,
			 async:false,
			 type:"GET",
			 cache:false,
			 dataType:"json",
			 success:function(response){
			     var data=response.data;
				 if(response.status==-1){
					  $(this).Alert({"title":"提示","str":response.message,"mark":true,"width":"160px"});
				 }else{
					 var isPass=data.pass;
					 if(isPass){
						  if(fnc){fnc();}

					 }else{
						 var details=data.details;
						 var len=details.length;
						 if(fnc){
							 $(this).Alert({"title":"提示","str":data.details[0].message,"mark":true,"width":"160px"});
						};
						 for(var i=0;i<len;i++){
								if(details[i].nodeId){
								    var id=details[i].nodeId;
									$(".graphRect").each(function(){
										   var $this=$(this);
										   var $unfilled=$this.parent().find(".unfilled");
										   var nId=$(this).attr("id");
										   if(nId==id){

											   var tips='<span class="tips">'+details[i].message+'</span>';
											   $unfilled.after($(tips)).show();
											   if($this.siblings(".queryTime").length!=0){
												  $unfilled.parent().css({left:"67px",top:"38px"});
											   }
											   $unfilled.on("mouseover",function(){
													$(this).siblings(".tips").css("display","inline-block").addClass("zIndex");
											   }).on("mouseout",function(){
													$(this).siblings(".tips").css("display","none").removeClass("zIndex");
											   });
										    }
									});
								}
						  }

					 }
				 }
			 }
		 });
}
//判断活动是否正在执行中
function isCampRunning(campStatus){
	if(campStatus=="B1"||campStatus=="B2"||campStatus=="B3"){
		//disabledToForm(true);
		return true;
	}
	return false;
}

//判断Job是否正在执行中
function isJobRunning(jobStatus){
	if(jobStatus==21||jobStatus==22||jobStatus==23){
		return false;
	}
	return true;
}
window.setTimeout(validationNodeFull,2000);
clearTimeout(jobRefreshTimer);
clearTimeout(campRefreshTimer);
var campRefreshTimer=null;
var jobRefreshTimer=null;
(function(){
	var campStatus=null;
	var selectedJobId=null;
	var currentJobId=null;


	campRefreshTimer = window.setTimeout(function(){refreshCamp(campId)},1000);


	function ableProExecute(){
		 disabledToForm(true);
		 $.ajax({
			 url:root+"schedule/campaign/"+campId+"/test",
			 async:false,
			 type:"GET",
			 cache:false,
			 dataType:"json",
			 success:function(response){
				 processSchueduleResponse(response);
			 }
		 });
	}
	function breakProExecute(){
		 disabledToForm(false);
		 $.ajax({
			 url:root+"schedule/campaign/"+campId+"/stopTest",
			 async:false,
			 type:"GET",
			 cache:false,
			 dataType:"json",
			 success:function(response){
				 processSchueduleResponse(response);
				 clearTimeout(jobRefreshTimer);
			 }
		 });
	}
	function ableOfficialExecute(){
		disabledToForm(true);
		$(this).Confirm({"title":"正式执行","str":"您确定正式执行该活动吗？","mark":true},function(){
			  $.ajax({
				url:root+"schedule/campaign/"+campId+"/execute",
				 async:false,
				 type:"GET",
				 cache:false,
				 dataType:"json",
				 success:function(response){
					 processSchueduleResponse(response);
				 }
			 });																	   
		})
	}
	function bleakOfficialExecute(){
		 $.ajax({
				url:root+"schedule/campaign/"+campId+"/stop",
				 async:false,
				 type:"GET",
				 cache:false,
				 dataType:"json",
				 success:function(response){
					 processSchueduleResponse(response);
					 clearTimeout(jobRefreshTimer);
				 }
		});
	}

	$("[name=cycleExecute]").live("change",function(){
		  selectedJobId=$(this).val();
		  currentJobId = selectedJobId;
		  clearNodeStyle();
		  clearTimeout(jobRefreshTimer);
		  refreshJob(currentJobId);
	});

	//处理调度动作返回
	function processSchueduleResponse(response){
		 var data=response.data;
		 if(response.status==-1){
			 $(this).Alert({"title":"提示","str":response.message,"mark":true,"width":"160px"});
			  updateUIByCampStatus(campStatus);
		 }else{
			  campStatus= response.data.campaignStatus;
			  campId=response.data.campaignId;
			  updateUIByCampStatus(campStatus);
			  clearTimeout(campRefreshTimer);
			  campRefreshTimer = window.setTimeout(function(){refreshCamp(campId)},500);
		 }
	}

	//刷新活动状态
	function refreshCamp(campId){
		 $.ajax({
			 url:root+"schedule/campaign/"+campId+"/snapshot",
			 async:false,
			 type:"GET",
			 cache:false,
			 dataType:"json",
			 success:function(response){
				 var data=response.data;
				 if(response.status==-1){
					  $(this).Alert({"title":"提示","str":response.message,"mark":true,"width":"160px"});
				 }else{
					  var campId=data.campaignId;
					  campStatus=data.campaignStatus;
					  if(tplAuthorizationFlag.indexOf("W")>0){updateUIByCampStatus(campStatus);}
					  var jobStatusList=data.jobStatusList;
					  var len=data.jobStatusList.length;
					  if($("[name=cycleExecute]").length==0){
						  return;
					  }
					  $("[name=cycleExecute] option").remove();
					  if(len>0){
						  if(currentJobId==null){
							  currentJobId =  jobStatusList[0].jobId;
						  }else{
							  var currentJobIdAbsent = true;
							  for(var i=0;i<len;i++){
								  if(jobStatusList[i].jobId==currentJobId){
									  currentJobIdAbsent =false;
									  break;
								  }
							  }
							  if(currentJobIdAbsent){
								  currentJobId =  jobStatusList[0].jobId;
							  }
						  }
						  for(var i=0;i<len;i++){
							  $("[name=cycleExecute]").append('<option value='+jobStatusList[i].jobId+'>'+jobStatusList[i].startTime+'</option>');
							  if($("[name=cycleExecute]").get(0).options[i].value == currentJobId){
								  $("[name=cycleExecute]").get(0).options[i].selected = true;
							  }
						  }
					  }else{
						  selectedJobId = null;
						  currentJobId = null;
						  clearNodeStyle();
					  }
					  if(currentJobId!=null){
						  clearTimeout(jobRefreshTimer);
						  jobRefreshTimer = window.setTimeout(function(){refreshJob(currentJobId)},500);
					  }
					  if(isCampRunning(campStatus)){
						  clearTimeout(campRefreshTimer);
						  campRefreshTimer = window.setTimeout(function(){refreshCamp(campId)},5000);
					  }
				 }
			}
		 });
	}

	//刷新节点状态
	 function refreshJob(jobId){
		 $.ajax({
			 url:root+"schedule/job/"+jobId+"/node/snapshot",
			 async:false,
			 type:"GET",
			 cache:false,
			 dataType:"json",
			 success:function(response){
				 var data=response.data;
				 if(response.status==-1){
					  //alert(response.message);
				 }else{
					  var jobStatus = data.jobStatus;
					  var nodeStatusList=data.nodeStatusList;
					  var len=nodeStatusList.length;
					  $(".graphRect").siblings(".queryPeopleNum").remove();
					 $(".graphRect").siblings(".queryTime").remove();
					  for(var i=0;i<len;i++){
						  var nodeId=nodeStatusList[i].nodeId;
						  var nodeStatus=nodeStatusList[i].nodeStatus;
						  $(".nodeFlow .graphRect").each(function(){
								var id=$(this).attr("id");
								if(id==nodeId){
								   var $queryTime=$('<span class="queryTime"><img class="state_executed" src=""/><span class="tips"></span></span>');
								   var src=getStateImage(nodeStatus);
                                   $queryTime.find(".state_executed").attr("src",src);
								   $(this).after($queryTime);
								   $(this).after('<span class="queryPeopleNum">'+nodeStatusList[i].outputMsg+'</span>');
								   $queryTime.find(".tips").html('开始时间：'+nodeStatusList[i].startTime+'<br/>结束时间：'+nodeStatusList[i].endTime+'<br/>执行时间：'+nodeStatusList[i].duration);
								   $queryTime.find(".state_executed").on("mouseover",function(){
										$(this).siblings(".tips").css("display","inline-block").addClass("zIndex");
								   }).on("mouseout",function(){
										$(this).siblings(".tips").css("display","none").removeClass("zIndex");
								   });

								}
						  });
					  }
					  if(isJobRunning(jobStatus)){
						  clearTimeout(jobRefreshTimer);
						  jobRefreshTimer = window.setTimeout(function(){refreshJob(jobId)},3000);
					  }
				 }
			 }
		 });
	 }

	//根据活动状态更新界面元素
	function updateUIByCampStatus(campStatus){
		 clearExecuteBtnStyle();
		 $(".nodeFlow  .mt a").unbind("click");
		 if(campStatus=="A1"){                                                                      //设计状态，可进行测试执行与正式执行
			 $(".proExecute").addClass("ableProExecute").text("测试执行");
			 $(".officialExecute").addClass("ableOfficialExecute");
		 }else if(campStatus=="B1"){                                                                //设计时测试执行状态，可中止测试执行
			 $(".proExecute").addClass("breakProExecute").text("测试中止");
		 }else if(campStatus=="A3"){                                                                //等待执行状态，可进行正式执行
			 $(".officialExecute").addClass("ableOfficialExecute").text("开始执行");
		 }else if(campStatus=="B3"){                                                     //执行中状态，可中止正式执行
			 $(".officialExecute").addClass("bleakOfficialExecute").text("正式中止");
		 }
		 $(".nodeFlow  .mt a").bind("click",function(){
		       var cls=$(this).attr("class");
			   if(cls.indexOf("ableProExecute")>=0){
				     validationNodeFull(ableProExecute);
			   }else if(cls.indexOf("breakProExecute")>=0){
				     breakProExecute();
			   }else if(cls.indexOf("ableOfficialExecute")>=0){
				     validationNodeFull(ableOfficialExecute);
			   }else if(cls.indexOf("bleakOfficialExecute")>=0){
				    bleakOfficialExecute();
			   }
		 });
	}

	function clearExecuteBtnStyle(){
		$(".proExecute").removeClass("breakProExecute ableProExecute");
		$(".officialExecute").removeClass("ableOfficialExecute bleakOfficialExecute");
	}

	function clearNodeStyle(){
		 $(".nodeFlow .graphRect").each(function(){
			   $(this).siblings(".queryPeopleNum").remove();
			   $(this).siblings(".queryTime").remove();
		  });
	}



})()
