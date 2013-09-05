--// alter a table tb_tc_send_log
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_send_log ADD COLUMN sms_num  int(11) NULL COMMENT '短信数量' AFTER type;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_send_log DROP COLUMN sms_num;


