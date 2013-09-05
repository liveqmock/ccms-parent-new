--// add shopId to rc_job_buffer
-- Migration SQL that makes the change goes here.

ALTER TABLE rc_job_buffer ADD COLUMN shop_id varchar(50) COLLATE utf8_bin NOT NULL  COMMENT '所属的方案组id' after fact_type;


--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE rc_job_buffer DROP COLUMN shop_id ;
