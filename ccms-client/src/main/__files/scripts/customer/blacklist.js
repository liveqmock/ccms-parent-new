
$('.blacklistMobile').flexigrid({
	url:root + 'channel/blacklist/MOBILE/page',
	//url: '/data/blacklist/mobile.json',
	method : 'GET',
	dataType: 'json',
	colModel : [
		{display: '手机',name:'contact', width : 150,align:'center', sortable : true ,dataindex:'contact'},
		{display: '来源',name:'source', width : 100,align:'center', sortable : true ,dataindex:'source'},
		{display: '时间', name:'created', width : 200,align:'center', sortable : true,dataindex:'created'},
		{display:'操作',name:'operation',width:100,align:'center',dataindex:'campId',renderer:function(v){
			return '<a href="javascript:void(0);" class="del_btn" title="删除" onclick="mobile.delBlacklist(this)"></a>'
		}}
	],
	sortname: "contact",
	sortorder: "asc",
	//searchitems :{display: '用户名', name : 'userName'},
	usepager: true,
	useRp: true,
	rp: 20,
	showTableToggleBtn: true
	//colAutoWidth:true,
});
var el = $('.blacklistMobile')[0];
$('#marketSearch').click(function(){
	el.p.qtype = $('.hdSearch').attr('name');
	el.p.query = $('.hdSearch').val();
	el.grid.populate();
});


 var mobile={
	mobileContent:$("#mobileContent"),
	mobileList:$(".mobileList"),
    del:function(obj){
		$(obj).closest("li").remove();
	},
	showMobilePop:function(){
		//mobile.mobileList.addInteractivePop({magTitle:"选择模板",mark:true});
		this.mobileContent.show();
		this.mobileList.find("li").remove();
	},
	hideMobilePop:function(){
	    this.mobileContent.hide();
		this.mobileContent.find(".tips").html("").removeClass("error");
		this.mobileContent.find("input:[name=phone]").val("");
	},
	add:function(){
		var li='<li><div class="line"><input type="text" value="" name="phone" class="borderHighlight " onblur="mobile.loadBlacklist(this)" /><span class="delline" onclick="mobile.del(this)"></span><span class="tips"></span></div></li>';
		this.mobileList.append($(li));
		/*var $phone=$("#phone");
		var str=$phone.val;
		if(str==""){

		}
	    alert(this.validationPhone(str));*/

	},
	loadBlacklist:function(obj){
		var $this=$(obj);
		var str=$this.val();
		if(str==""){
			$this.siblings(".tips").html("请输入手机号").addClass("error");
		}else{
			if(this.validationPhone(str)){
				   $.ajax({
					    url:root+'channel/blacklist/MOBILE/exists?value='+str,
					   //url:"/data/market/loadTpl.json",
						async: false,
						type:"GET",
						cache:false,
						dataType : "json",
						success:function(response){

                              if(response){
                            	  $this.siblings(".tips").html("手机号已存在").addClass("error");
                              }else{
                            	  $this.siblings(".tips").html('<img src="images/graph/news/state_executed.png" style="position:relative;top:3px;">').removeClass("error");
                              }
						}
				   });

			      //$this.siblings(".tips").text("").removeClass("error");

			}else{
				  $this.siblings(".tips").html("手机号码输入格式不正确").addClass("error");
			}
		}
	},
	createBlacklist:function(){
		var flag1=true;
		var flag2=true;
		var arr=[];
		this.mobileContent.find(".tips").each(function(){
			  var $input=$(this).siblings("input");
			  if($input.val()==""){
				    $(this).html("请输入手机号").addClass("error");
					flag1=false;
			   }else{
				    if($(this).hasClass("error")){
						flag2=false;
					}else{
                        arr.push($input.val());
				    }
			   }

		});
		var parame = JSON.stringify(arr);
		if(flag1 && flag2){
			  $.ajax({
					url:root+'channel/blacklist/MOBILE',
					//url:"/data/market/loadTpl.json",
					async: false,
					type:"POST",
					data: parame,
					dataType : "json",
					contentType:'application/json',
					success:function(response){
						 $(this).Alert({"title":"保存提示","str":"保存成功","mark":true},function(){
							    $('.blacklistMobile').flexReload();
								mobile.hideMobilePop();
						 });
					}
			   });
		}
	},
	delBlacklist:function(obj){
		var arr=[];
	    var $tr=$(obj).closest("tr");
		var rec=$(obj).closest('tr').data('rec');
		arr.push(rec.contact);
		var parame=JSON.stringify(arr);
		$(this).Confirm({"title":"删除提示","str":"确认删除该手机号码？","mark":true},function(){
			   $.ajax({
				  url:root+"channel/blacklist/MOBILE",
				  async: false,
				  type:"DELETE",
				  data:parame,
				  contentType:'application/json',
				  success:function(response){
					  var data=response;
					  var isSuccess=data.success;
                      if(isSuccess){
                    	  $(obj).closest("tr").detach();
                      }else{
                    	  $(this).Alert({"title":"删除提示","str":"删除失败","mark":true,"width":"150px"});
                      }

				  }
			   });
		});
	},
	validationPhone:function(str){
		 var partten = /^0{0,1}(13[0-9]|14[0-9]|15[0-9]|18[0-9])[0-9]{8}$/; 
		 if(partten.test(str)){
				return true ;
		 }else{
				return false ;
		}
	}
 };

 $(".hdSearch").live("focus",function(){
	  $(this).siblings(".inputTips").hide();
}).live("blur",function(){
	var val=$(this).val();
	if(val==""){
		   $(this).siblings(".inputTips").show();
		   el.p.query="";
		   $('.blacklistMobile').flexReload();
	 }
});