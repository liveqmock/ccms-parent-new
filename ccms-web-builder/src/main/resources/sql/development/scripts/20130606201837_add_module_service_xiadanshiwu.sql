--// add module service_xiadanshiwu
-- Migration SQL that makes the change goes here.

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES ( 39,'xiadanshiwu','客服中心',NULL,NULL,NULL,NULL,'0','17','页面'),
( 40,'xiadanshiwu_link','客服中心','导航栏上的链接','#/service/xiadanshiwu',NULL,NULL,'0','17',NULL)
;

INSERT INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES ( '39',NULL,'39',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '40',NULL,'40',10,NULL,NULL,NULL,NULL,NULL,NULL,450,NULL)
;

--//@UNDO
-- SQL to undo the change goes here.

delete from module where id in(39,40);
delete from module_type where id in(39,40);
