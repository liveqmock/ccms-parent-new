--// modify table job and subjob
-- Migration SQL that makes the change goes here.
alter table twf_log_job drop column run_optr_id;
alter table twf_log_job drop column version;

alter table twf_log_subjob drop column version;


--//@UNDO
-- SQL to undo the change goes here.

alter table twf_log_job add column run_optr_id bigint(20);
alter table twf_log_job add column version  varchar(20);

alter table twf_log_subjob add column version  varchar(20);


