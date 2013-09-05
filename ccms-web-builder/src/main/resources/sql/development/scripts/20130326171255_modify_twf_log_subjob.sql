--// modify twf_log_subjob
-- Migration SQL that makes the change goes here.
alter table twf_log_subjob add column output_msg varchar(64)  null after endtime;
update twf_log_subjob set output_msg= case when output_count is not null then  concat(output_count,'人') else null END;
alter table twf_log_subjob drop column output_count ;

--//@UNDO
-- SQL to undo the change goes here.
alter table twf_log_subjob drop column output_msg ;

