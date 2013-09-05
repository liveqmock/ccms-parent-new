--// tb_tc_refund_status add column nextday_senddate
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_tc_refund_status ADD COLUMN nextday_senddate varchar(8) DEFAULT NULL COMMENT '次日发送的日期' AFTER auto_refund_care;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_tc_refund_status DROP COLUMN nextday_senddate;
