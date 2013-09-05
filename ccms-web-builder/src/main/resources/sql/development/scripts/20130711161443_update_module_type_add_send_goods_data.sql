--// update module_type add send goods data
-- Migration SQL that makes the change goes here.
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (59,'sendGoodsFollow','发货跟进',NULL,'#/affairs/sendGoods',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES ( 59,NULL,'59','48',NULL,NULL,NULL,NULL,NULL,NULL,'1100',NULL);



--//@UNDO
-- SQL to undo the change goes here.
delete from module_type where id in (59);
delete from module where id in (59);

