--// update order center url
-- Migration SQL that makes the change goes here.

update module_type set url='#/order/urpay' where id=14;

insert ignore into module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
values(38,'order','订单中心',null,null,null,null,0,17,'页面');

INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES('38',NULL,'38',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'650',NULL);

--//@UNDO
-- SQL to undo the change goes here.

-- bu yong le
