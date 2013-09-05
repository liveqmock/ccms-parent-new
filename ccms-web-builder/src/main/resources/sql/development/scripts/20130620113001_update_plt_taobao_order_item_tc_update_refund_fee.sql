--// update plt_taobao_order_item_tc update refund_fee
-- Migration SQL that makes the change goes here.
alter table plt_taobao_order_item_tc drop column refund_fee;

--//@UNDO
-- SQL to undo the change goes here.
alter table plt_taobao_order_item_tc add refund_fee decimal(12,2) DEFAULT NULL COMMENT '退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分';

