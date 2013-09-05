--// update table evaluate_report_result
-- Migration SQL that makes the change goes here.

drop table evaluate_report_result;


CREATE TABLE evaluate_report_result (
  evaluate_result_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表结果id(逻辑主键ID)',
  job_id bigint(20) DEFAULT NULL COMMENT 'jobId对应哪个执行批次',
  node_id bigint(20) DEFAULT NULL COMMENT '评估的渠道节点ID',
  evaluate_time varchar(50) COLLATE utf8_bin DEFAULT NULL,
  buy_order_count int(11) DEFAULT NULL COMMENT '拍下订单数',
  buy_customer_count int(11) DEFAULT NULL COMMENT '拍下人数',
  buy_payment_sum double(12,2) DEFAULT NULL COMMENT '拍下金额',
  pay_order_count int(11) DEFAULT NULL COMMENT '付款订单数',
  pay_customer_count int(11) DEFAULT NULL COMMENT '付款客户数',
  pay_payment_sum double(12,2) DEFAULT NULL COMMENT '付款金额数',
  customer_avg_fee double(12,2) DEFAULT NULL COMMENT '客单价',
  product_count int(11) DEFAULT NULL COMMENT '购买商品数',
  roi varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '投资回报率',
  PRIMARY KEY (evaluate_result_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--//@UNDO
-- SQL to undo the change goes here.

drop table evaluate_report_result;

CREATE TABLE evaluate_report_result (
  job_id bigint(20) NOT NULL COMMENT 'jobId对应哪个执行批次',
  node_id bigint(20) NOT NULL COMMENT '评估的渠道节点ID',
  evaluate_time varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '评估日期',
  buy_order_count int(11) DEFAULT NULL COMMENT '拍下订单数',
  buy_customer_count int(11) DEFAULT NULL COMMENT '拍下人数',
  buy_payment_sum double(12,2) DEFAULT NULL COMMENT '拍下金额',
  pay_order_count int(11) DEFAULT NULL COMMENT '付款订单数',
  pay_customer_count int(11) DEFAULT NULL COMMENT '付款客户数',
  pay_payment_sum double(12,2) DEFAULT NULL COMMENT '付款金额数',
  customer_avg_fee double(12,2) DEFAULT NULL COMMENT '客单价',
  product_count int(11) DEFAULT NULL COMMENT '购买商品数',
  roi varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '投资回报率'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
