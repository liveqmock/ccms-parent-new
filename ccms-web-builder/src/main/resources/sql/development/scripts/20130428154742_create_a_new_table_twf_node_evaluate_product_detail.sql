--// create a new table twf_node_evaluate_product_detail
-- Migration SQL that makes the change goes here.

drop table twf_node_evaluate_product_detail;

CREATE TABLE twf_node_evaluate_product_detail (
  evaluate_product_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表商品明细主键',
  job_id bigint(20) DEFAULT NULL COMMENT '执行jobId',
  node_id bigint(20) DEFAULT NULL COMMENT '节点ID',
  evaluate_time date DEFAULT NULL COMMENT '评估时间',
  product_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品ID',
  product_name varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  customer_count int(11) DEFAULT NULL COMMENT '购买客户数',
  order_count int(11) DEFAULT NULL COMMENT '订单数',
  buy_num int(11) DEFAULT NULL COMMENT '购买数量',
  buy_fee decimal(12,2) DEFAULT NULL COMMENT '购买金额',
  outer_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家编码',
  PRIMARY KEY (evaluate_product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='评估节点_商品明细';

--//@UNDO
-- SQL to undo the change goes here.

drop table twf_node_evaluate_product_detail;

CREATE TABLE twf_node_evaluate_product_detail (
  job_id bigint(20) DEFAULT NULL COMMENT '执行jobId',
  node_id bigint(20) DEFAULT NULL COMMENT '节点ID',
  evaluate_time date DEFAULT NULL COMMENT '评估时间',
  product_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品ID',
  product_name varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  customer_count int(11) DEFAULT NULL COMMENT '购买客户数',
  order_count int(11) DEFAULT NULL COMMENT '订单数',
  buy_num int(11) DEFAULT NULL COMMENT '购买数量',
  buy_fee decimal(12,2) DEFAULT NULL COMMENT '购买金额',
  outer_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='评估节点_商品明细';
