--// CCMS-3788_add_gateway_id_on_caringDetail
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_caring_detail ADD COLUMN gateway_id  int NOT NULL AFTER caring_type;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_caring_detail DROP COLUMN gateway_id, MODIFY COLUMN content  varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL AFTER caring_type;



