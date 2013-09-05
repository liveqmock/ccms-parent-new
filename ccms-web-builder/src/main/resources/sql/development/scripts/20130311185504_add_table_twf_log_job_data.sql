--// add table twf_log_job_data
-- Migration SQL that makes the change goes here.
create table twf_log_job_data(
subjob_id  bigint(20),
job_id  bigint(20) ,
camp_id  bigint(20) ,
source  bigint(20) COMMENT '源节点id',
target  bigint(20) COMMENT '目标节点id',
data_type varchar(20) COMMENT '数据类型',
data_code varchar(50) COMMENT '数据名称',
KEY idx_twf_log_subjob_data_jobid_target (job_id,target)
)
COMMENT = '流程节点间数据传输记录保存';


alter table twf_log_job change is_preexecute is_test  tinyint(1) DEFAULT NULL;
alter table twf_log_subjob change is_preexecute is_test  tinyint(1) DEFAULT NULL;


--//@UNDO
-- SQL to undo the change goes here.
drop table twf_log_job_data;
alter table twf_log_job change is_test is_preexecute   tinyint(1) DEFAULT NULL;
alter table twf_log_subjob change is_test is_preexecute   tinyint(1) DEFAULT NULL;
