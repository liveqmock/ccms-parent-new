--// update plt_taobao_transitstepinfo
-- Migration SQL that makes the change goes here.
DROP TABLE IF EXISTS plt_taobao_transitstepinfo ;
CREATE TABLE plt_taobao_transitstepinfo (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单ID',
  out_sid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '运单号.具体一个物流公司的运单号码.',
  status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单的物流状态 * 等候发送给物流公司 *已提交给物流公司,等待物流公司接单 *已经确认消息接收，等待物流公司接单 *物流公司已接单 *物流公司不接单 *物流公司揽收失败 *物流公司揽收成功 *签收失败 *对方已签收 *对方拒绝签收',
  company_name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '物流公司名称',
  transit_step_info text COLLATE utf8_bin COMMENT '流转信息文件路径',
  created datetime DEFAULT NULL COMMENT '数据创建时间',
  PRIMARY KEY (tid),
  KEY idx_tb_tc_taobao_shipping_tid (tid) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝物流流转数据表';

DROP TABLE IF EXISTS plt_taobao_transitstepinfo_tc ;
CREATE TABLE plt_taobao_transitstepinfo_tc (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单ID',
  out_sid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '运单号.具体一个物流公司的运单号码.',
  status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单的物流状态 * 等候发送给物流公司 *已提交给物流公司,等待物流公司接单 *已经确认消息接收，等待物流公司接单 *物流公司已接单 *物流公司不接单 *物流公司揽收失败 *物流公司揽收成功 *签收失败 *对方已签收 *对方拒绝签收',
  logistics_status int(11) DEFAULT NULL COMMENT '解析的订单物流状态',
  company_name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '物流公司名称',
  transit_step_info text COLLATE utf8_bin COMMENT '流转信息文件路径',
  shipping_status int(11) DEFAULT NULL COMMENT '流转状态：1:到同城，2、派件；3：已签收',
  signed_time datetime DEFAULT NULL COMMENT '签收时间',
  arrived_time datetime DEFAULT NULL COMMENT '到达同城时间',
  delivery_time datetime DEFAULT NULL COMMENT '派件时间',
  created datetime DEFAULT NULL COMMENT '数据创建时间',
  updated datetime DEFAULT NULL COMMENT '数据修改时间',
  PRIMARY KEY (tid),
  KEY idx_tb_tc_taobao_shipping_tc_tid (tid) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝物流运单';


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS plt_taobao_transitstepinfo ;
CREATE TABLE IF NOT EXISTS plt_taobao_transitstepinfo (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '交易ID',
  out_sid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '运单号.具体一个物流公司的运单号码.',
  status char(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流订单状态,CREATED(已创建) RECREATED(重新创建) CANCELLED(订单已取消) CLOSED(订单关闭) SENDING(等候发送给物流公司) ACCEPTING(已发送给物流公司,等待接单) ACCEPTED(物流公司已接单) REJECTED(物流公司不接单) PICK_UP(物流公司揽收成功) PICK_UP_FAILED(物流公司揽收失败) LOST(物流公司丢单) REJECTED_BY_RECEIVER(拒签) ACCEPTED_BY_RECEIVER(已签收)',
  company_name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '物流公司名称',
  created datetime DEFAULT NULL COMMENT '运单创建时间',
  modified datetime DEFAULT NULL COMMENT '运单修改时间',
  transit_step_info text COLLATE utf8_bin COMMENT '流转信息文件路径',
  shipping_status int(11) DEFAULT NULL COMMENT '流转状态：1:到同城，2、派件；3：已签收',
  signed_time datetime DEFAULT NULL COMMENT '签收时间',
  arrived_time datetime DEFAULT NULL COMMENT '到达同城时间',
  delivery_time datetime DEFAULT NULL COMMENT '派件时间',
  PRIMARY KEY (tid),
  KEY idx_tb_tc_taobao_shipping_tid (tid) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝物流运单';
DROP TABLE IF EXISTS plt_taobao_transitstepinfo_tc ;


