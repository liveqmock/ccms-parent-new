--// remove tb_remote_log_exception user_id  null constraint
-- Migration SQL that makes the change goes here.
alter table tb_remote_log_exception modify user_id  bigint(20)  NULL COMMENT '操作用户ID' ;


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_remote_log_exception modify user_id  bigint(20) not NULL COMMENT '操作用户ID' ;

