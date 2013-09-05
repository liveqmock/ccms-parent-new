--// CCMS-3962_modify_affairs_add_tid_nick_colums
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_affairs
ADD COLUMN tid  varchar(50) NULL AFTER pkid,
ADD COLUMN customerno  varchar(50) NULL AFTER tid;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_affairs
DROP COLUMN tid,
DROP COLUMN customerno,
MODIFY COLUMN title  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER pkid;

