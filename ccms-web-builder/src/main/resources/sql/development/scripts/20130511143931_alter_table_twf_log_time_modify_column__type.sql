--// alter table twf_log_time modify column  type
-- Migration SQL that makes the change goes here.

ALTER TABLE twf_node_time  MODIFY COLUMN realtime_begin_date date;

ALTER TABLE twf_node_time  MODIFY COLUMN cycle_begin_date date;

ALTER TABLE twf_node_time  MODIFY COLUMN cycle_end_date date;


--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE twf_node_time  MODIFY COLUMN realtime_begin_date datetime;

ALTER TABLE twf_node_time  MODIFY COLUMN cycle_begin_date datetime;

ALTER TABLE twf_node_time  MODIFY COLUMN cycle_end_date datetime;