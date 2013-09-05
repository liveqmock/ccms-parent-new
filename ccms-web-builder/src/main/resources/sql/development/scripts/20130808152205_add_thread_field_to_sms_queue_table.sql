--// add thread field to sms_queue table
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_tc_sms_queue ADD COLUMN thread VARCHAR(20)  AFTER send_time;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_tc_sms_queue DROP COLUMN thread;
