--// CCMS-3452 change primary key of rc_job tables
-- Migration SQL that makes the change goes here.

ALTER TABLE rc_job_buffer drop primary key;
ALTER TABLE rc_job_buffer add column job_id bigint(20) NOT NULL  primary key FIRST;
ALTER TABLE rc_job_buffer add UNIQUE idx_unique_rc_job_buffer_id_type(fact_id,fact_type);
 
ALTER TABLE rc_job_log drop primary key;
ALTER TABLE rc_job_log add column job_id bigint(20) NOT NULL  primary key FIRST;
ALTER TABLE rc_job_log add UNIQUE idx_unique_rc_job_log_id_type(fact_id,fact_type);

ALTER TABLE rc_job_detail_log add column id bigint(20) NOT NULL AUTO_INCREMENT primary key FIRST;
ALTER TABLE rc_job_detail_log add column job_id bigint(20) NOT NULL after id;
ALTER TABLE rc_job_detail_log add UNIQUE idx_unique_rc_job_detail_log_jobid_ruleid(job_id,rule_id);

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE rc_job_buffer drop column job_id;
ALTER TABLE rc_job_buffer drop index idx_unique_rc_job_buffer_id_type;
ALTER TABLE rc_job_buffer ADD PRIMARY KEY(fact_id,fact_type);

ALTER TABLE rc_job_log drop column job_id;
ALTER TABLE rc_job_log drop index idx_unique_rc_job_log_id_type;
ALTER TABLE rc_job_log ADD PRIMARY KEY(fact_id,fact_type);

ALTER TABLE rc_job_detail_log drop column id;
ALTER TABLE rc_job_detail_log drop column job_id;
ALTER TABLE rc_job_detail_log drop index idx_unique_rc_job_detail_log_jobid_ruleid;

