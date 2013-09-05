--// create_tb_tc_refund_status
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_refund_status (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单编号',
  oid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '子订单编号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL,
  auto_refund_care tinyint(1) DEFAULT '0' COMMENT '订单中心关怀状态（0-未关怀  1-已关怀, 2-次日关怀, 3-不关怀）',
	refund_care tinyint(1) DEFAULT '0' COMMENT '退款事务关怀状态（0-未关怀  1-已关怀）',
  created timestamp NULL DEFAULT NULL,
  updated timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='退款关怀状态表';

ALTER TABLE tb_tc_customer_orders_ship
drop column traderate_hide,
DROP COLUMN refund_hide,
DROP COLUMN traderate_care_status,
DROP COLUMN refund_care_status;

--//@UNDO
-- SQL to undo the change goes here.
drop table if exists tb_tc_refund_status;

ALTER TABLE tb_tc_customer_orders_ship add column traderate_hide tinyint(1) DEFAULT '0' COMMENT '评价事务隐藏（0-没隐藏  1-隐藏）',
  add column refund_hide tinyint(1) DEFAULT '0' COMMENT '退款事务隐藏（0-没隐藏  1-隐藏）',
  add column traderate_care_status tinyint(1) DEFAULT '0' COMMENT '评价事务关怀状态（0-未关怀  1-已关怀）',
  add column refund_care_status tinyint(1) DEFAULT '0' COMMENT '退款事务关怀状态（0-未关怀  1-已关怀）';
