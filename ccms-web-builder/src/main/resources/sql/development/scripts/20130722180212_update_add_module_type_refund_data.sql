--// update add module type refund data
-- Migration SQL that makes the change goes here.
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (61,'refundFollow','退款跟进',NULL,'#/affairs/refund',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES (61,NULL,'61','48',NULL,NULL,NULL,NULL,NULL,NULL,'1300',NULL);


--//@UNDO
-- SQL to undo the change goes here.

delete from module_type where id in (61);
delete from module where id in (61);
