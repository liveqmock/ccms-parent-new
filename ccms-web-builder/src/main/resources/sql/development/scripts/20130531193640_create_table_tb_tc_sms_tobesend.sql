--// create table tb_tc_sms_tobesend
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_tc_sms_queue (
  pkid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  updated datetime NOT NULL COMMENT '数据更新时间',
  created datetime NOT NULL COMMENT '数据创建时间',
  tid varchar(50) DEFAULT NULL COMMENT '订单ID',
  dp_id varchar(50) DEFAULT NULL COMMENT '店铺ID',
  buyer_nick varchar(50) DEFAULT NULL COMMENT '买家昵称',
  trade_created varchar(20) DEFAULT NULL COMMENT '订单创建时间',
  sms_content varchar(200) DEFAULT NULL COMMENT '短信内容',
  mobile varchar(20) DEFAULT NULL COMMENT '手机号码',
  send_user varchar(50) DEFAULT NULL COMMENT '发送者',
  type int(11) DEFAULT NULL COMMENT '发送类型',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='待发送短信队列表'

--//@UNDO
-- SQL to undo the change goes here.

DROP TABLE tb_tc_sms_queue;
