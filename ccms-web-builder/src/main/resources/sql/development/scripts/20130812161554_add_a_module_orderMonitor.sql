--// add a module orderMonitor
-- Migration SQL that makes the change goes here.

INSERT INTO module_type
	(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES
	(66,'orderMonitor','订单监控',NULL,'#/order/orderMonitor',NULL,NULL,'0','17',NULL)
;

INSERT INTO module
	(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES
	(66 ,NULL,'66','38',NULL,NULL,NULL,NULL,NULL,NULL,'700',NULL)
;

--//@UNDO
-- SQL to undo the change goes here.

delete from module where id=66;
delete from module_type where id=66;
