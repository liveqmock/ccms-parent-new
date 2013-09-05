--// CCMS-3788_create_care_detail_table
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_tc_caring_detail (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单编号',
  oid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '子订单编号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL,
  customerno varchar(50) COLLATE utf8_bin NOT NULL COMMENT '客户ID',
  caring_type varchar(10) COLLATE utf8_bin NOT NULL,
  content varchar(500) COLLATE utf8_bin DEFAULT NULL,
  created timestamp NULL DEFAULT NULL,
  updated timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  caringperson varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '关怀客服',
  KEY idx_createdAndUpdated (created,updated) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '评价事物关怀明细表';

--//@UNDO
-- SQL to undo the change goes here.


drop table tb_tc_caring_detail;