--// plt_taobao_product change column
-- Migration SQL that makes the change goes here.

ALTER TABLE plt_taobao_product CHANGE COLUMN props_name props_name TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL;

--//@UNDO
-- SQL to undo the change goes here.


