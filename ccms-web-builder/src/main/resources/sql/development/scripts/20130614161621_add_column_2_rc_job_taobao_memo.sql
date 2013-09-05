--// add column 2 rc job taobao memo
-- Migration SQL that makes the change goes here.

alter table rc_job_taobao_memo add column last_memo varchar(500) COMMENT '上一次的备注内容' ;

--//@UNDO
-- SQL to undo the change goes here.

alter table rc_job_taobao_memo drop column last_memo ;

