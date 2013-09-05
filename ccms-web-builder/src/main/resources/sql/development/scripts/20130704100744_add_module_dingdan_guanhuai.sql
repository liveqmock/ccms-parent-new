--// add module dingdan_guanhuai
-- Migration SQL that makes the change goes here.

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES
	(54,'orderingCare','下单关怀',NULL,'#/order/care?type=6',NULL,NULL,'0','17',NULL),
 	(55,'sendingCare','派件关怀',NULL,'#/order/care?type=9',NULL,NULL,'0','17',NULL)
;

INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES
	( '54',NULL,'54','50',NULL,NULL,NULL,NULL,NULL,NULL,'1300',NULL),
	( '55',NULL,'55','50',NULL,NULL,NULL,NULL,NULL,NULL,'1400',NULL)
;

--//@UNDO
-- SQL to undo the change goes here.

delete from module where id in(54,55);
delete from module_type where id in(54,55);
