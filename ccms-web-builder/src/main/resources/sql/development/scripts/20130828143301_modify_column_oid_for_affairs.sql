--// modify_column_oid_for_affairs
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_affairs
MODIFY COLUMN oid  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER tid;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_affairs
MODIFY COLUMN oid  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER tid;



