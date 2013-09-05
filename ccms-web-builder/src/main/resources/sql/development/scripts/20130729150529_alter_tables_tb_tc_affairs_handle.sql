--// alter_tables_tb_tc_affairs_handle
-- Migration SQL that makes the change goes here.
 ALTER TABLE tb_tc_affairs_handle ADD COLUMN affairs_id  bigint NOT NULL AFTER pkid;


--//@UNDO
-- SQL to undo the change goes here.
 	ALTER TABLE tb_tc_affairs_handle DROP COLUMN affairs_id;

