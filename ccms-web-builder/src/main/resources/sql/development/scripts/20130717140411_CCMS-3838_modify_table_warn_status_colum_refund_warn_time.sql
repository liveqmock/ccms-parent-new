--// CCMS-3838_modify_table_warn_status_colum_refund_warn_time
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_warn_status
CHANGE COLUMN refund_end_time refund_warn_time  timestamp NULL DEFAULT NULL COMMENT '退款告警时间' AFTER refund_warn_status;


--//@UNDO
-- SQL to undo the change goes here.


