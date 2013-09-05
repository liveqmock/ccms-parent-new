var admin = {
	disable:function(id,type){//启用，禁用
		$.post(root+'user/taobao/'+id,{"_method":"PUT","disabled":!type},function(){
			//alert((type ? '启用' : '禁用')+'成功');
			$(this).Alert({"title":"提示","str":(type ? '启用' : '禁用')+'成功',"mark":true,"width":"150px"});
			$('#taobaouserGrid').flexReload();
		});
	}
}
$('#taobaouserGrid').flexigrid({
	url: root+'user/taobao/list',
	dataType: 'json',
	method : 'GET',
	colModel : [
		{display: 'id',name:'id', width : 0, sortable : true ,dataindex:'id',hide:true},
		{display: '旺旺帐号',name:'taobao_nick', width : 3, sortable : true ,dataindex:'platUserName'},
		{display: '所属店铺', name:'shopName', width : 2, sortable : true,align:'center' ,dataindex:'shopName'},
		{display: '账号类型', name:'shopType', width : 2, sortable : true,align:'center' ,dataindex:'subuser',renderer:function(v){
			if (v == true) {
				return "子账号";
			}else if (v == false) {
				return "主账号";
			}
		}},
		{display: '状态', name:'disabled', width : 2, sortable : true,align:'center' ,dataindex:'disabled',mapping:['id'],convert:function(v,mappVal){
			if(v==false){
				return '<a href="javascript:void(0);" onclick="admin.disable(\''+mappVal[0]+'\','+v+');" title="点击禁用" class="enable_btn"></a>';
			}
			if(v==true){
				return '<a href="javascript:void(0);" onclick="admin.disable(\''+mappVal[0]+'\','+v+');" title="点击启用" class="disabled_btn"></a>';
			}
		}}
	],
	sortname: "id",
	usepager: true,
	useRp: true,
	rp: 10,
	showTableToggleBtn: true,
	colAutoWidth:true
});