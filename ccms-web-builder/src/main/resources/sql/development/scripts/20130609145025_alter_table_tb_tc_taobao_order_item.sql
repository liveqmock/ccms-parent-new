--// alter_table_tb_tc_taobao_order_item
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_taobao_order_item ADD COLUMN sku_properties_name  varchar(50) NULL AFTER outer_iid;

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_taobao_order_item DROP COLUMN sku_properties_name;

