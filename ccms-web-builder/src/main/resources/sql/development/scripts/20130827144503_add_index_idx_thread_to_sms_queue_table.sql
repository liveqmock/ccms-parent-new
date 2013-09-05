--// add index idx_thread to sms_queue table
-- Migration SQL that makes the change goes here.

create index idx_tb_tc_sms_queue_thread on tb_tc_sms_queue(thread) using btree;

--//@UNDO
-- SQL to undo the change goes here.

drop index idx_tb_tc_sms_queue_thread on tb_tc_sms_queue;
