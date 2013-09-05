--// del module shopMonitor_link
-- Migration SQL that makes the change goes here.

delete from module where id in(13,37);

--//@UNDO
-- SQL to undo the change goes here.

INSERT INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES
	(13,13,10,300,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	('37',NULL,'37','31',NULL,NULL,NULL,NULL,NULL,NULL,'600',NULL)
;
