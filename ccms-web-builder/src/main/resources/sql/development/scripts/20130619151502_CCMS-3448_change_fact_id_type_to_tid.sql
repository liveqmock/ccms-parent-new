--// CCMS-3448 change fact_id_type to tid
-- Migration SQL that makes the change goes here.

alter table rc_job change fact_id tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单ID';
alter table rc_job drop column fact_type;
alter table rc_job drop column fact_data;

alter table rc_job_log change fact_id tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单ID';
alter table rc_job_log drop column fact_type;

alter table rc_job_detail_log change fact_id tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单ID';
alter table rc_job_detail_log drop column fact_type;
alter table rc_job_detail_log drop index fact_id;



--//@UNDO
-- SQL to undo the change goes here.

alter table rc_job change tid fact_id varchar(50) COLLATE utf8_bin NOT NULL;
alter table rc_job add column fact_type varchar(50) COLLATE utf8_bin NOT NULL;
alter table rc_job add column fact_data text COLLATE utf8_bin NOT NULL COMMENT 'fact数据';

alter table rc_job_log change tid fact_id varchar(50) COLLATE utf8_bin NOT NULL;
alter table rc_job_log add column fact_type varchar(50) COLLATE utf8_bin NOT NULL;

alter table rc_job_detail_log change tid fact_id varchar(50) COLLATE utf8_bin NOT NULL;
alter table rc_job_detail_log add column fact_type varchar(50) COLLATE utf8_bin NOT NULL;
alter table rc_job_detail_log add key fact_id(fact_id,fact_type);

