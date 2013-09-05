--// update module_type add data
-- Migration SQL that makes the change goes here.
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (58,'traderateFollow','评价跟进',NULL,'#/affairs/evaluate',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES ( 58,NULL,'58','48',NULL,NULL,NULL,NULL,NULL,NULL,'1400',NULL);


--//@UNDO
-- SQL to undo the change goes here.
delete from module_type where id in (58);
delete from module where id in (58);


