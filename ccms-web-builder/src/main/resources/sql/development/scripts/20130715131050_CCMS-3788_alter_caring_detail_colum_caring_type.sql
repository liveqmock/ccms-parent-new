--// CCMS-3788_alter_caring_detail_colum_caring_type
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_caring_detail
MODIFY COLUMN caring_type  int NOT NULL AFTER customerno;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_caring_detail
MODIFY COLUMN caring_type  varchar(50) NOT NULL AFTER customerno;

