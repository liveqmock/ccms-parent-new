--// uni_plat
-- Migration SQL that makes the change goes here.

delete from uni_plat where plat_code in ('ext','extaobao','kfgzt','modify','taobao','wdzx');

INSERT INTO uni_plat (plat_code, parent_plat, plat_table, plat_priority, sync_parent, enable_delete, plat_desc) VALUES ('modify', null, 'plt_modify_customer', '0', null, false, '手工修改统一客户信息');
INSERT INTO uni_plat (plat_code, parent_plat, plat_table, plat_priority, sync_parent, enable_delete, plat_desc) VALUES ('ext', null, 'plt_ext_customer', '1', null, true, '外部导入客户信息（非接驳平台）');
INSERT INTO uni_plat (plat_code, parent_plat, plat_table, plat_priority, sync_parent, enable_delete, plat_desc) VALUES ('extaobao', 'taobao', 'plt_extaobao_customer', '1', true, false, '外部导入客户信息（来源于“淘宝”平台）');
INSERT INTO uni_plat (plat_code, parent_plat, plat_table, plat_priority, sync_parent, enable_delete, plat_desc) VALUES ('kfgzt', 'taobao', 'plt_kfgzt_customer', '1', true, false, '“客服工作台”客户信息');
INSERT INTO uni_plat (plat_code, parent_plat, plat_table, plat_priority, sync_parent, enable_delete, plat_desc) VALUES ('wdzx', 'taobao', 'plt_wdzx_customer', '1', true, false, '“我的中心”客户信息');
INSERT INTO uni_plat (plat_code, parent_plat, plat_table, plat_priority, sync_parent, enable_delete, plat_desc) VALUES ('taobao', null, 'plt_taobao_customer', '1', null, false, '“淘宝”客户信息');



--//@UNDO
-- SQL to undo the change goes here.

delete from uni_plat;
