--// add_new_navigation
-- Migration SQL that makes the change goes here.
-- 我的事物
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (67,'myFollow','我的事物','二级菜单','javascript:void(0);',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
  VALUES (67,NULL,'67','39',NULL,NULL,NULL,NULL,NULL,NULL,'1100',NULL);


-- 事物列表
INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
  VALUES (68,'followList','事物列表','','#/affairs/myaffair',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
  VALUES (68,NULL,'68','67',NULL,NULL,NULL,NULL,NULL,NULL,'1000',NULL);


--//@UNDO
-- SQL to undo the change goes here.
delete from module_type where id in (67);
delete from module where id in (67);

delete from module_type where id in (68);
delete from module where id in (68);


