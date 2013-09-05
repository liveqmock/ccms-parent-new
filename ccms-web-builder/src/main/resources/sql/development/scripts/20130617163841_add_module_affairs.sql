--// add module affairs
-- Migration SQL that makes the change goes here.

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES
	(48,'transactionFollow','事务跟进',NULL,'#/affairs/orderlist',NULL,NULL,'0','17',NULL),
	(49,'unpayFollow','未付款跟进',NULL,'#/affairs/orderlist',NULL,NULL,'0','17',NULL)
;

INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES
	(48,NULL,'48','39',NULL,NULL,NULL,NULL,NULL,NULL,'1000',NULL),
	(49,NULL,'49','48',NULL,NULL,NULL,NULL,NULL,NULL,'1000',NULL)
;

 UPDATE module_type SET key_name='affairs' WHERE id='39';

--//@UNDO
-- SQL to undo the change goes here.

delete from module where id in(48,49);
delete from module_type where id in(48,49);
