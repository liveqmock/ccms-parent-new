--// update tb_tc_mq_info_log new table
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_tc_mq_info_log (
  pkid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  msg text DEFAULT NULL COMMENT '消息内容',
  type int(11) DEFAULT NULL COMMENT '消息（1：发送或2：接收）',
  status int(11) DEFAULT NULL COMMENT '消息状态（0：初始状态，1：消费成功）',
  created datetime DEFAULT NULL COMMENT '数据创建时间',
  updated datetime DEFAULT NULL COMMENT '数据更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB AUTO_INCREMENT=33449 DEFAULT CHARSET=utf8 COMMENT='mq消息记录表';

--//@UNDO
-- SQL to undo the change goes here.
drop table tb_tc_mq_info_log;
