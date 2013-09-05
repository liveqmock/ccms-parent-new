--// add table evaluate_report_total_result
-- Migration SQL that makes the change goes here.

CREATE TABLE evaluate_report_total_result (
  evaluate_result_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表结果id(逻辑主键ID)',
  job_id bigint(20) NOT NULL COMMENT 'jobId对应哪个执行批次',
  node_id bigint(20) NOT NULL COMMENT '评估的渠道节点ID',
  pay_customer_count bigint(12) DEFAULT NULL COMMENT '付款客户数',
  pay_payment_sum double(12,2) DEFAULT NULL COMMENT '付款金额数',
  product_count bigint(12) DEFAULT NULL COMMENT '购买商品数',
  PRIMARY KEY (evaluate_result_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--//@UNDO
-- SQL to undo the change goes here.

drop table  evaluate_report_total_result;

