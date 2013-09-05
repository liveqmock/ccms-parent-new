--// change username to tenantid
-- Migration SQL that makes the change goes here.


delete from plt_taobao_shop;

insert into plt_taobao_shop(shop_id,shop_name)
values('100571094','大狗子19890202');

delete from tb_sysuser;

insert into tb_sysuser(id,user_type,login_name,password,real_name,disabled)
values('1','taobao','tomwalk','','tomwalk','0');

delete from tb_sys_taobao_user;

insert into tb_sys_taobao_user(id,plat_user_id,plat_user_name,is_subuser,plat_shop_id)
values('1','12334','tomwalk','0','taobao_100571094');

--//@UNDO
-- SQL to undo the change goes here.

delete from plt_taobao_shop;

insert into plt_taobao_shop(shop_id,shop_name)
values('100571094','大狗子19890202');

delete from tb_sys_taobao_user;

insert into tb_sys_taobao_user(id,plat_user_id,plat_user_name,is_subuser,plat_shop_id)
values('1','12334','tomwalk','0','taobao_100571094');