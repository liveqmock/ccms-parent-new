--// alter_order_item
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_tc_taobao_order_item MODIFY COLUMN outer_iid  varchar(50) NULL DEFAULT NULL COMMENT '商家外部编码'


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_taobao_order_item MODIFY COLUMN outer_iid  datetime NULL DEFAULT NULL COMMENT '商家外部编码'
