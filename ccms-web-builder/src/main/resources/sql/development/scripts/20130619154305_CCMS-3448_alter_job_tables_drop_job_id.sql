--// CCMS-3448 alter job tables drop job_id
-- Migration SQL that makes the change goes here.

alter table rc_job drop column job_id;
alter table rc_job add PRIMARY KEY(tid);

alter table rc_job_log drop column job_id;
alter table rc_job_log add PRIMARY KEY(tid);

alter table rc_job_detail_log drop column job_id;
alter table rc_job_detail_log drop index idx_unique_rc_job_detail_log_jobid_ruleid;
alter table rc_job_detail_log add unique idx_unique_rc_job_detail_log_tid_ruleid(tid,rule_id);


--//@UNDO
-- SQL to undo the change goes here.
alter table rc_job drop PRIMARY KEY; 
alter table rc_job add column job_id  bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY;

alter table rc_job_log drop PRIMARY KEY; 
alter table rc_job_log add column job_id  bigint(20) NOT NULL PRIMARY KEY;

alter table rc_job_detail_log add column job_id  bigint(20) NOT NULL;
alter table rc_job_detail_log drop index idx_unique_rc_job_detail_log_tid_ruleid;
alter table rc_job_detail_log add unique idx_unique_rc_job_detail_log_jobid_ruleid(job_id,rule_id);


