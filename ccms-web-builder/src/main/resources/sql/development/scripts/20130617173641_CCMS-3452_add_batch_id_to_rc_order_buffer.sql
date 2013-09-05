--// CCMS-3448 add batch_id to rc_order_buffer
-- Migration SQL that makes the change goes here.

drop table IF EXISTS rc_order_buffer;

CREATE TABLE rc_order_buffer (
  batch_id bigint(20) not null COMMENT '同一个消息中的订单batch_id',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  modified datetime DEFAULT NULL COMMENT '订单修改时间',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  decision varchar(50) COLLATE utf8_bin NULL COMMENT '规则引擎采取的动作',
  PRIMARY KEY (batch_id,tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='规则引擎从MQ获得的订单';

CREATE TABLE rc_order_backlog (
  batch_id bigint(20) not null COMMENT '同一个消息中的订单batch_id',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  modified datetime DEFAULT NULL COMMENT '订单修改时间',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  decision varchar(50) COLLATE utf8_bin NULL COMMENT '规则引擎采取的动作',
  PRIMARY KEY (batch_id,tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='规则引擎处理过的订单备份';

alter table rc_job change last_modify_time modified timestamp NULL DEFAULT NULL  COMMENT '最后更新时间' after fact_type;
alter table rc_job modify submit_time datetime NOT NULL COMMENT '提交job时间';


--//@UNDO
-- SQL to undo the change goes here.

drop table IF EXISTS rc_order_buffer;

CREATE TABLE rc_order_buffer (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  decision varchar(50) COLLATE utf8_bin NULL COMMENT '规则引擎采取的动作',
  modify datetime DEFAULT NULL COMMENT '订单修改时间',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  PRIMARY KEY (tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='规则引擎从MQ获得的订单';

drop table IF EXISTS rc_order_backlog;

alter table rc_job change modified last_modify_time timestamp NULL DEFAULT NULL  COMMENT '最后更新时间' after job_status;

