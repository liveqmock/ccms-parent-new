--// init tb app properties
-- Migration SQL that makes the change goes here.

insert ignore into module_type(id,name,name_plus,tip,lowest_edition_required,support_ops_mask,memo)
values(1,'index',NULL,NULL,0,31,'首页'),
(2,'nav',NULL,NULL,0,31,'导航栏'),
(3,'首页','(链接)',NULL,0,0,'导航栏上的\"首页\"链接');

insert ignore into module(id,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,memo)
values(1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(2,2,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(3,3,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

insert ignore into module_entry(id,module_id,permission_id,role_id,user_id,support_ops_mask,memo)
values(1,NULL,NULL,NULL,NULL,0,'最根的授权'),
(2,1,0,NULL,NULL,31,'首页的授权,限定为非匿名用户');

--//@UNDO
-- SQL to undo the change goes here.

delete from module_type;
delete from module;
delete from module_entry;
