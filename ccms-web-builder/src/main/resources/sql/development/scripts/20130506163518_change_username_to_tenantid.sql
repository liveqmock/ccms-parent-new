--// change username to tenantid
-- Migration SQL that makes the change goes here.
update tb_app_properties set prop_name='ccms_tenant_id' where prop_name='ccms_username' ;
update tb_app_properties set prop_name='ccms_tenant_password' where prop_name='ccms_password';


--//@UNDO
-- SQL to undo the change goes here.
update tb_app_properties set prop_name='ccms_username' where prop_name='ccms_tenant_id';
update tb_app_properties set prop_name='ccms_password' where prop_name='ccms_tenant_password';
