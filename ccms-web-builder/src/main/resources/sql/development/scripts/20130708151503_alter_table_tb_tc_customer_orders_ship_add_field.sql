--// alter_table_tb_tc_customer_orders_ship_add_field
-- Migration SQL that makes the change goes here.
ALTER TABLE `tb_tc_customer_orders_ship`
ADD COLUMN sendgoods_hide  tinyint(1) NULL DEFAULT 0 COMMENT '发货事务隐藏（0-没隐藏  1-隐藏）' AFTER is_hide,
ADD COLUMN logistics_hide  tinyint(1) NULL DEFAULT 0 COMMENT '物流事务隐藏（0-没隐藏  1-隐藏）' AFTER sendgoods_hide,
ADD COLUMN traderate_hide  tinyint(1) NULL DEFAULT 0 COMMENT '评价事务隐藏（0-没隐藏  1-隐藏）' AFTER logistics_hide,
ADD COLUMN refund_hide  tinyint(1) NULL DEFAULT 0 COMMENT '退款事务隐藏（0-没隐藏  1-隐藏）' AFTER traderate_hide,
ADD COLUMN sendgoods_care_status  tinyint(1) NULL DEFAULT 0 COMMENT '发货事务关怀状态（0-未关怀  1-已关怀）' AFTER refund_hide,
ADD COLUMN logistics_care_status  tinyint(1) NULL DEFAULT 0 COMMENT '物流事务关怀状态（0-未关怀  1-已关怀）' AFTER sendgoods_care_status,
ADD COLUMN traderate_care_status  tinyint(1) NULL DEFAULT 0 COMMENT '评价事务关怀状态（0-未关怀  1-已关怀）' AFTER logistics_care_status,
ADD COLUMN refund_care_status  tinyint(1) NULL DEFAULT 0 COMMENT '退款事务关怀状态（0-未关怀  1-已关怀）' AFTER traderate_care_status;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_customer_orders_ship
DROP COLUMN sendgoods_hide,
DROP COLUMN logistics_hide,
DROP COLUMN traderate_hide,
DROP COLUMN refund_hide,
DROP COLUMN sendgoods_care_status,
DROP COLUMN logistics_care_status,
DROP COLUMN traderate_care_status,
DROP COLUMN refund_care_status;



