--// alter_table_tb_tc_taobao_order_item
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_order_item_tc
ADD COLUMN price  decimal(12,2) NULL COMMENT '商品价格' AFTER payment;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_order_item_tc
DROP COLUMN price;

