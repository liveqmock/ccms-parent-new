$('a[setData]').live('click',function(){
	$('body').data('fl_rec',$(this).parents('tr').data('rec'));
});

$('#userGrid').flexigrid({
	url: 'admin/user',
	dataType: 'json',
	colModel : [
		{display: 'id',name:'id', width : 0, sortable : true ,dataindex:'id',hide:true},
		{display: '用户名',name:'userName', width : 2, sortable : true ,dataindex:'displayName'},
		{display: '姓名', name:'name', width : 2, sortable : true,align:'center' ,dataindex:'name'},
		{display: '手机号', name:'mobile', width : 2, sortable : true,align:'center' ,dataindex:'mobile'},
		{display: 'Email', name:'email', width : 2, sortable : true,align:'center' ,dataindex:'email'},
		{display: '状态', name:'disabled', width : 2, sortable : true,align:'center' ,dataindex:'disabled',renderer:function(v){
			if(v=='false'){
				return '<a href="javascript:void(0);" title="点击禁用" class="enable_btn"></a>';
			}
			if(v=='true'){
				return '<a href="javascript:void(0);" title="点击启用" class="disabled_btn"></a>';
			}
		}},
		{display:'操作',name:'operation',width:1,sortable:true,align:'center',dataindex:'id',renderer:function(v){
			return '<a setData href="#/admin/users/modify" class="modify_btn" title="修改"></a><a href="javascript:void(0);" class="del_btn" title="删除" onclick="alert(\'delete item\')"></a>'
		}}

	],
	buttons : [
		{name: '新建用户 ', bclass: 'add_btn',url:'#/admin/users/add',id:'add_btn'}
	],
	searchitems :{display: '用户名', name : 'userName'},
	usepager: true,
	useRp: true,
	rp: 10,
	showTableToggleBtn: true,
	colAutoWidth:true,
	rowDblClick:function(){
		location.href += '/modify:'+$(this).data('rec').id;
	}
});

/*自定义搜索*/
var el = $('#userGrid')[0];
$('[name=show_myactivity]').change(function(){
	el.grid.addParams(this.name,this.checked);
	el.grid.populate();
});



$('[name=disabled]').change(function(){
	el.grid.addParams(this.name,this.value);
	el.grid.populate();
});