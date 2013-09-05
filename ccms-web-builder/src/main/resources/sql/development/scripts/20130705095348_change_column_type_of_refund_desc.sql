--// change column type of refund_desc
-- Migration SQL that makes the change goes here.
alter table top_refund change column refund_desc refund_desc text CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

alter table top_refund_job_buffer change column refund_desc refund_desc text CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

alter table top_jobmid_refund change column refund_desc refund_desc text CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

alter table plt_taobao_refund change column refund_desc refund_desc text CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;


--//@UNDO
-- SQL to undo the change goes here.
alter table top_refund change column refund_desc refund_desc VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

alter table top_refund_job_buffer change column refund_desc refund_desc VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

alter table top_jobmid_refund change column refund_desc refund_desc VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;

alter table plt_taobao_refund change column refund_desc refund_desc VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL  ;



