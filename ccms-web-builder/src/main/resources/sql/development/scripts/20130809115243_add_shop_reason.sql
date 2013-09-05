--// add_shop_reason
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_shop_reason (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  shop_id varchar(50) DEFAULT NULL,
  shop_type varchar(10) DEFAULT NULL,
  reason varchar(200) DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;


INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('RefundResonStaticsTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.RefundResonStaticsTask', '0 0 * * * ?', 1);




--//@UNDO
-- SQL to undo the change goes here.