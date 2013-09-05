--// CCMS-3789_add_field_tb_tc_send_log
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_send_log
ADD COLUMN oid  varchar(50) NULL COMMENT '子订单号' AFTER tid;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_tc_send_log
DROP COLUMN oid ;
