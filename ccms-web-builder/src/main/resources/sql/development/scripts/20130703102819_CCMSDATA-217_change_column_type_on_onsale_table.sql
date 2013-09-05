--// CCMSDATA-217 change column type on onsale_table
-- Migration SQL that makes the change goes here.
ALTER TABLE `top_item_onsale` CHANGE COLUMN `seller_cids` `seller_cids` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

ALTER TABLE `top_item_onsale_job_buffer` CHANGE COLUMN `seller_cids` `seller_cids` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

ALTER TABLE `top_jobmid_item` CHANGE COLUMN `seller_cids` `seller_cids` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE `top_item_onsale` CHANGE COLUMN `seller_cids` `seller_cids` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

ALTER TABLE `top_item_onsale_job_buffer` CHANGE COLUMN `seller_cids` `seller_cids` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

ALTER TABLE `top_jobmid_item` CHANGE COLUMN `seller_cids` `seller_cids` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;


