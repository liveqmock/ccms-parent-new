--// alter a table tb_tc_urpay_summary
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_urpay_summary ADD COLUMN send_num  int(11) NULL AFTER response_amount;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_urpay_summary DROP COLUMN send_num;

