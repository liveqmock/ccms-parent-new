--// CCMS-3452 add table rc_job
-- Migration SQL that makes the change goes here.

ALTER TABLE rc_job_buffer RENAME rc_job;

ALTER TABLE rc_job MODIFY column job_id bigint(20) NOT NULL AUTO_INCREMENT ;
ALTER TABLE rc_job CHANGE column status job_status varchar(20) COLLATE utf8_bin NOT NULL COMMENT '当前job处理状态';

CREATE TABLE rc_order_buffer(
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  modify datetime DEFAULT NULL COMMENT '订单修改时间',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  PRIMARY KEY(tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '规则引擎从MQ获得的订单';



--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE rc_job CHANGE column job_status status  varchar(20) COLLATE utf8_bin NOT NULL COMMENT '当前job处理状态';
ALTER TABLE rc_job  RENAME rc_job_buffer;
DROP TABLE rc_order_buffer;