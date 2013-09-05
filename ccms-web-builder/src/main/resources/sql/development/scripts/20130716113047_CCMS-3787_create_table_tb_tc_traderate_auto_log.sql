--// CCMS-3787 create table tb_tc_traderate_auto_log
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_traderate_auto_log(
id BINARY NOT NULL ,
dp_id VARCHAR(50) NOT NULL COMMENT '店铺ＩＤ',
oid VARCHAR(50) NOT NULL COMMENT '子订单ＩＤ',
tid VARCHAR(50) NOT NULL COMMENT '订单ＩＤ',
created datetime COMMENT '评价创建时间',
error_code VARCHAR(150) NOT NULL COMMENT '失败-错误吗',
error_msg VARCHAR(150) NOT NULL COMMENT '失败-错误描述',
status TINYINT DEFAULT 0 COMMENT '评价设置是否成功（0-失败  1-成功）'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '评价事务-自动评价回复日志';

INSERT INTO tb_tc_quartz_task_init VALUE (NULL,'AutoResponseTraderateTask','TC_GROUP','com.yunat.ccms.tradecenter.urpay.task.AutoResponseTraderateTask','0 0/10 * * * ?','1');

--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS tb_tc_traderate_auto_log;

DELETE FROM tb_tc_quartz_task_init WHERE job_name = 'AutoResponseTraderateTask' AND job_group = 'TC_GROUP';

