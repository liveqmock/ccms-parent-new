--// CCMS-3679 modify rc_job tables
-- Migration SQL that makes the change goes here.

alter table rc_job add column error_code varchar(50)  COLLATE utf8_bin NULL COMMENT '错误提示代码';

alter table rc_job_log add column count_flag boolean  NULL default true COMMENT '统计时是否计入';
alter table rc_job_detail_log add column count_flag boolean  NULL default true COMMENT '统计时是否计入';


--//@UNDO
-- SQL to undo the change goes here.


alter table rc_job drop column error_code ;

alter table rc_job_log drop column count_flag ;
alter table rc_job_detail_log drop column count_flag ;