--// create a table twf_node_evaluate_order_detail
-- Migration SQL that makes the change goes here.

CREATE TABLE twf_node_evaluate_order_detail (
  job_id bigint(20) DEFAULT NULL,
  node_id bigint(20) DEFAULT NULL,
  evaluate_time date DEFAULT NULL,
  oid varchar(50) COLLATE utf8_bin DEFAULT NULL,
  customerno varchar(50) COLLATE utf8_bin DEFAULT NULL,
  created datetime DEFAULT NULL,
  pay_time datetime DEFAULT NULL,
  consign_time datetime DEFAULT NULL,
  total_fee decimal(12,2) DEFAULT NULL,
  payment decimal(12,2) DEFAULT NULL,
  status varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--//@UNDO
-- SQL to undo the change goes here.

drop table twf_node_evaluate_order_detail;

