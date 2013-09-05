--// add_module_source_type
-- Migration SQL that makes the change goes here.
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (69,'sourceTypeConfig','来源类型配置','','#/affairs/sourceType',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
  VALUES (69,NULL,'69','67',NULL,NULL,NULL,NULL,NULL,NULL,'1100',NULL);


--//@UNDO
-- SQL to undo the change goes here.
delete from module_type where id in (69);
delete from module where id in (69);


