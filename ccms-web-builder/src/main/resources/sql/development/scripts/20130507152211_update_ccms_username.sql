--// change username to tenantid
-- Migration SQL that makes the change goes here.

update tb_app_properties set prop_value='0_taobao_100571094'
where prop_group='CCMS' and prop_name='ccms_tenant_id';

--//@UNDO
-- SQL to undo the change goes here.

update tb_app_properties set prop_value='qiushi'
where prop_group='CCMS' and prop_name='ccms_tenant_id';