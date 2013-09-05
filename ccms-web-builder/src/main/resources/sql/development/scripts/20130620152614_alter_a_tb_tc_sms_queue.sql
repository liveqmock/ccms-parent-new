--// alter a tb_tc_sms_queue
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_sms_queue ADD COLUMN send_time  datetime NULL AFTER gateway_id;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_sms_queue DROP COLUMN send_time;

