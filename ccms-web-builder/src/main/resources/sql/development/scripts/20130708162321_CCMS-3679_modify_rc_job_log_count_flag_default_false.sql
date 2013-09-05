--// CCMS-3679 modify rc_job_log count_flag default false
-- Migration SQL that makes the change goes here.

alter table rc_job_log modify column count_flag boolean  NULL default false COMMENT '统计时是否计入';
alter table rc_job_detail_log modify column count_flag boolean  NULL default false COMMENT '统计时是否计入';

--//@UNDO
-- SQL to undo the change goes here.

alter table rc_job_log modify column count_flag boolean  NULL default true COMMENT '统计时是否计入';
alter table rc_job_detail_log modify column count_flag boolean  NULL default true COMMENT '统计时是否计入';
