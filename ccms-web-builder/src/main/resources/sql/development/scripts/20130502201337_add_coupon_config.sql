--// add coupon config
-- Migration SQL that makes the change goes here.

insert into tb_app_properties(prop_id,prop_group,prop_name,prop_value,prop_desc) values(22,'CCMS','ccms_grant_url',
'http://container.api.taobao.com/container?appkey=12283535&scope=promotion,item,usergrade','淘宝权限授权地址');



--//@UNDO
-- SQL to undo the change goes here.

delete from tb_app_properties where prop_group='CCMS' and prop_name='ccms_grant_url';
