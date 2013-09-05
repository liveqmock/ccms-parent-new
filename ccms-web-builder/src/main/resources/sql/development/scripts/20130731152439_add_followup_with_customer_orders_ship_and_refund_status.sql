--// add_followup_with_customer_orders_ship_and_refund_status
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_customer_orders_ship
    ADD COLUMN order_followup  tinyint(1) NULL DEFAULT 0 COMMENT '未付款跟进（0-未跟进 1-已跟进）' AFTER logistics_care_status,
    ADD COLUMN sendgoods_followup  tinyint(1) NULL DEFAULT 0 COMMENT '发货事务跟进（0-未跟进 1-已跟进）' AFTER order_followup,
    ADD COLUMN logistics_followup  tinyint(1) NULL DEFAULT 0 COMMENT '物流事务跟进（0-未跟进 1-已跟进）' AFTER sendgoods_followup;

ALTER TABLE tb_tc_refund_status
    ADD COLUMN refund_followup  tinyint(1) NULL DEFAULT 0 COMMENT '退款事务跟进（0-未跟进 1-已跟进）' AFTER updated,
    ADD COLUMN traderate_followup  tinyint(1) NULL DEFAULT 0 COMMENT '评价事务跟进（0-未跟进 1-已跟进）' AFTER refund_followup;



--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_customer_orders_ship
    DROP COLUMN sendgoods_followup,
    DROP COLUMN logistics_followup,
    DROP COLUMN order_followup;

ALTER TABLE tb_tc_refund_status
    DROP COLUMN refund_followup,
    DROP COLUMN traderate_followup;

