--// CCMS-3705 add operatorId to rc_drl
-- Migration SQL that makes the change goes here.

alter table rc_drl add column operator_id bigint(20)  NULL COMMENT '操作的用户Id';
alter table rc_drl_changelog add column operator_id bigint(20)  NULL COMMENT '操作的用户Id';

--//@UNDO
-- SQL to undo the change goes here.

alter table rc_job drop column operator_id ;
alter table rc_job drop column operator_id ;
