--// init twf_qrtz_timer_locks
-- Migration SQL that makes the change goes here.
INSERT INTO twf_qrtz_timer_locks (lock_name) VALUES ('TRIGGER_ACCESS');
INSERT INTO twf_qrtz_timer_locks (lock_name) VALUES ('JOB_ACCESS');
INSERT INTO twf_qrtz_timer_locks (lock_name) VALUES ('CALENDAR_ACCESS');
INSERT INTO twf_qrtz_timer_locks (lock_name) VALUES ('STATE_ACCESS');
INSERT INTO twf_qrtz_timer_locks (lock_name) VALUES ('MISFIRE_ACCESS');


--//@UNDO
-- SQL to undo the change goes here.
delete from twf_qrtz_timer_locks;


