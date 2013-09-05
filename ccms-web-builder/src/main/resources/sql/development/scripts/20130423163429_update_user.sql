--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 把taobao_user的id改为自增的

insert into tb_sysuser (id,disabled, email, login_name, mobile, password, real_name, user_type)
values (1,false,null,'tomwalk',null,null,'tomwalk','taobao')
on duplicate key update
	disabled=false,
	email=null,
	login_name='tomwalk',
	mobile=null,
	password=null,
	real_name='tomwalk',
	user_type='taobao'
;
insert into tb_sys_taobao_user (id,is_subuser, plat_shop_id, plat_user_id, plat_user_name)
values (1,false,'taobao_68784446','12334','tomwalk')
on duplicate key update
	is_subuser=false,
	plat_shop_id='taobao_68784446',
	plat_user_id='12334',
	plat_user_name='tomwalk'
;

--//@UNDO
-- SQL to undo the change goes here.

delete from tb_sysuser where id=1;
delete from tb_sys_taobao_user where id=1;

