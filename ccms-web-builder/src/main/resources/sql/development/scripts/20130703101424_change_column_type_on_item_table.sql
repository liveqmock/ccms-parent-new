--// change column type on item_table
-- Migration SQL that makes the change goes here.
ALTER TABLE `top_item_job_buffer` CHANGE COLUMN `seller_cids` `seller_cids` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

ALTER TABLE `top_item` CHANGE COLUMN `seller_cids` `seller_cids` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE `top_item_job_buffer` CHANGE COLUMN `seller_cids` `seller_cids` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

ALTER TABLE `top_item` CHANGE COLUMN `seller_cids` `seller_cids` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

