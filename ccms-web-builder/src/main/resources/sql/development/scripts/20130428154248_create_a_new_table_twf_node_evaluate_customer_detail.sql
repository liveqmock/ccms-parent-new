--// create a new table twf_node_evaluate_customer_detail
-- Migration SQL that makes the change goes here.

drop table twf_node_evaluate_customer_detail;

CREATE TABLE twf_node_evaluate_customer_detail (
  evaluate_customer_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表客户明细主键',
  job_id bigint(20) DEFAULT NULL COMMENT '执行ID',
  node_id bigint(20) DEFAULT NULL COMMENT '节点ID',
  evaluate_time date DEFAULT NULL COMMENT '评估时间',
  uni_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '平台唯一标识',
  full_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  sex char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  birthday date DEFAULT NULL COMMENT '生日',
  state varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  city varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  district varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区县',
  vip_info varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'vip信息',
  mobile varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  buyer_credit_lev int(11) DEFAULT NULL COMMENT '买家信用等级',
  good_rate varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '买家好评率',
  address varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地址',
  zip varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '邮编',
  job varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '职业',
  PRIMARY KEY (evaluate_customer_id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--//@UNDO
-- SQL to undo the change goes here.


drop table twf_node_evaluate_customer_detail;


CREATE TABLE twf_node_evaluate_customer_detail (
  job_id bigint(20) DEFAULT NULL COMMENT '执行ID',
  node_id bigint(20) DEFAULT NULL COMMENT '节点ID',
  evaluate_time date DEFAULT NULL COMMENT '评估时间',
  uni_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '平台唯一标识',
  full_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  sex char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  birthday date DEFAULT NULL COMMENT '生日',
  state varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  city varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  district varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区县',
  vip_info varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'vip信息',
  mobile varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  buyer_credit_lev int(11) DEFAULT NULL COMMENT '买家信用等级',
  good_rate varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '买家好评率',
  address varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地址',
  zip varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '邮编',
  job varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '职业'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


