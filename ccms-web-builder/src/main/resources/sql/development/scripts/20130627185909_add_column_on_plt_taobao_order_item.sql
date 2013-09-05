--// c12121212121
-- Migration SQL that makes the change goes here.
ALTER TABLE `plt_taobao_order_item` ADD COLUMN `step_trade_status` VARCHAR(50) NULL DEFAULT NULL  AFTER `status` ;



--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE `plt_taobao_order_item` DROP COLUMN `step_trade_status` ;

