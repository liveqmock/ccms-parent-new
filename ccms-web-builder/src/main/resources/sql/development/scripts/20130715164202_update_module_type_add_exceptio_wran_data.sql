--// update module_type add exceptio wran data
-- Migration SQL that makes the change goes here.
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (62,'exceptionWarn','异常告警','二级菜单','javascript:void(0);',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES (62,NULL,'62','38',NULL,NULL,NULL,NULL,NULL,NULL,'1700',NULL);

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (63,'traderateWarn','中差评告警',NULL,'#/order/alarm?type=1',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES (63,NULL,'63','62',NULL,NULL,NULL,NULL,NULL,NULL,'1800',NULL);

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (64,'refundWarn','退款告警',NULL,'#/order/alarm?type=2',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES (64,NULL,'64','62',NULL,NULL,NULL,NULL,NULL,NULL,'1900',NULL);


--//@UNDO
-- SQL to undo the change goes here.

delete from module_type where id in (62,63,64);
delete from module where id in (62,63,64);

