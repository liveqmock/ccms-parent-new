--// alter a table tb_tc_urpay_summary
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_urpay_summary MODIFY COLUMN send_num  int(11) NULL DEFAULT 0

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_urpay_summary MODIFY COLUMN send_num  int(11) NULL DEFAULT NULL

