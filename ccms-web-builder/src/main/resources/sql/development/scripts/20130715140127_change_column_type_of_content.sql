--// change column type of content
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_traderate CHANGE COLUMN content content TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;



--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE plt_taobao_traderate CHANGE COLUMN content content VARCHAR(500) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

