--// add module for order_urpay
-- Migration SQL that makes the change goes here.

UPDATE module_type SET url='#/order/urpay?type=1' WHERE id='14';

INSERT INTO module_type
(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES ( 41,'zidongcuifu','自动催付','页面',NULL,NULL,NULL,'0','0',NULL),
 ( 42,'yuguanbicuifu','预关闭催付','页面',NULL,NULL,NULL,'0','17',NULL),
 ( 43,'juhuasuancuifu','聚划算催付','页面',NULL,NULL,NULL,'0','17',NULL),
 ( 44,'dingdancuifu','订单催付','二级菜单',NULL,NULL,NULL,'0','17',NULL),
 ( 45,'zidongcuifu_link','自动催付',NULL,'#/order/urpay?type=1',NULL,NULL,'0','17',NULL),
 ( 46,'yuguanbicuifu_link','预关闭催付',NULL,'#/order/urpay?type=2',NULL,NULL,'0','17',NULL),
 ( 47,'juhuasuancuifu_link','聚划算催付',NULL,'#/order/urpay?type=3',NULL,NULL,'0','17',NULL)
;

INSERT INTO module
(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo) 
VALUES 
( '41',NULL,'41',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '42',NULL,'42',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '43',NULL,'43',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '44',NULL,'44',38,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '45',NULL,'45',44,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '46',NULL,'46',44,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
( '47',NULL,'47',44,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)
;


--//@UNDO
-- SQL to undo the change goes here.

delete from module where id in(41,42,43,44,45,46,47);
delete from module_type where id in(41,42,43,44,45,46,47);
