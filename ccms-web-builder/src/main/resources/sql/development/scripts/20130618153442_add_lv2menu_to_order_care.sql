--// add lv2menu to order care
-- Migration SQL that makes the change goes here.

INSERT INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES
	(50,'orderCare','订单关怀',NULL,'#/order/care?type=7',NULL,NULL,'0','17',NULL),
 	(51,'sendCare','发货关怀',NULL,'#/order/care?type=7',NULL,NULL,'0','17',NULL),
	(52,'cityCare','同城关怀',NULL,'#/order/care?type=8',NULL,NULL,'0','17',NULL),
	(53,'receiveCare','签收关怀',NULL,'#/order/care?type=10',NULL,NULL,'0','17',NULL)
;

INSERT INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES
	( '50',NULL,'50','38',NULL,NULL,NULL,NULL,NULL,NULL,'1100',NULL),
	( '51',NULL,'51','50',NULL,NULL,NULL,NULL,NULL,NULL,'1000',NULL),
	( '52',NULL,'52','50',NULL,NULL,NULL,NULL,NULL,NULL,'1100',NULL),
	( '53',NULL,'53','50',NULL,NULL,NULL,NULL,NULL,NULL,'1200',NULL)
;
UPDATE module SET ranking='900' WHERE id='44';

--//@UNDO
-- SQL to undo the change goes here.

delete from module where id in(50,51,52,53);
delete from module_type where id in(50,51,52,53);
UPDATE module SET ranking=null WHERE id='44';
