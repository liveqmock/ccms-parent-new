--// add column on top_item_seller_cat
-- Migration SQL that makes the change goes here.
ALTER TABLE `top_item_seller_cat` ADD COLUMN `job_execution_id` BIGINT(20) NULL;


--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE `top_item_seller_cat` DROP COLUMN `job_execution_id`;
