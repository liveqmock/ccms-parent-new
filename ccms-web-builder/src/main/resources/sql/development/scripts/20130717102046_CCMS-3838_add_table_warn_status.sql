--// CCMS-3838_add_table_warn_status
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_warn_status (
  oid varchar(50) NOT NULL COMMENT '主键',
  created datetime NOT NULL COMMENT '数据创建时间',
  updated datetime COMMENT '数据更新时间',
  not_good_warn_status smallint(6) DEFAULT '0' COMMENT '中差评告警状态，  0：未告警，1：已告警',
  not_good_warn_time timestamp NULL DEFAULT NULL COMMENT '中差评告警告时间',
  refund_warn_status smallint(6) DEFAULT '0' COMMENT '退款告警状态，  0：未告警，1：已告警',
  refund_end_time timestamp NULL DEFAULT NULL COMMENT '退款告警时间',
  PRIMARY KEY (oid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='警告状态表';


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS tb_tc_warn_status;

