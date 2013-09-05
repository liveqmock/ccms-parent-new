--// update module_type add shipping data
-- Migration SQL that makes the change goes here.
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (60,'shippingFollow','物流跟进',NULL,'#/affairs/logistics',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES (60,NULL,'60','48',NULL,NULL,NULL,NULL,NULL,NULL,'1200',NULL);


--//@UNDO
-- SQL to undo the change goes here.

delete from module_type where id in (60);
delete from module where id in (60);

