--// create a table twf_log_node_mids
-- Migration SQL that makes the change goes here.


DROP TABLE IF EXISTS twf_log_node_mids;


CREATE TABLE twf_log_node_mids (
  job_id bigint(20) NOT NULL,
  table_view_name varchar(50) COLLATE utf8_bin NOT NULL COMMENT '表或视图名',
  table_view_type varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '表或视图类型',
  created_time datetime DEFAULT NULL,
  KEY idx_log_node_mids_job_id_table_view_name (job_id,table_view_name) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='流程执行中间表列表' ;

--//@UNDO
-- SQL to undo the change goes here.

drop table  twf_log_node_mids;


