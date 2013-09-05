--// add_cloum_for_affairs
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_affairs ADD oid varchar(50) NOT NULL after tid;
ALTER TABLE tb_tc_affairs ADD dp_id varchar(50) NOT NULL after pkid;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_affairs DROP oid;
ALTER TABLE tb_tc_affairs DROP dp_id;


