--// alter table twf_node_evaluate_customer_detail  add column
-- Migration SQL that makes the change goes here.

insert into tb_role(id,name,memo)values(88,'Yunat客服支持','数云客服支持角色');

insert into tb_role_permission(role_id,permission_id)
values(88,1),(88,2),(88,3),(88,4),(88,5),(88,6),(88,7);

insert into tb_app_properties(prop_group,prop_name,prop_value,prop_desc)
values('CCMS','default_yunat_user_role','88','客服系统默认权限');


--//@UNDO
-- SQL to undo the change goes here.

delete from tb_role where id=88;

delete from tb_role_permission where role_id =88;

delete from tb_app_properties where prop_group='CCMS' and prop_name='default_yunat_user_role';