--// CCMS-1356 update a table tb_tc_urpay_summary
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_urpay_summary MODIFY COLUMN urpay_date varchar(50) DEFAULT NULL COMMENT '催付日期' ;
--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_urpay_summary MODIFY COLUMN urpay_date DATE DEFAULT NULL COMMENT '催付日期';