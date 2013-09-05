--// add support for etl
-- Migration SQL that makes the change goes here.

delete from  tb_app_properties where prop_group='CCMS' and prop_name='ccms_username';
insert into tb_app_properties(prop_group,prop_name,prop_value,prop_desc)values
('CCMS','ccms_username','0_taobao_100571094','为兼容ETL设计,CCMS程序内请使用ccms_tenant_id');
--//@UNDO
-- SQL to undo the change goes here.
delete from  tb_app_properties where prop_group='CCMS' and prop_name='ccms_username';

