--// add task init sql
-- Migration SQL that makes the change goes here.

DELETE FROM tb_tc_quartz_task_init;
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('RealTimeUrpayTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.RealTimeUrpayTask', '0 0/5 * * * ?', 1);
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('WhilePointUrpayTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.WhilePointUrpayTask', '0 0 * * * ?', 1);
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('CheapUrpayTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.CheapUrpayTask', '0 0/3 * * * ?', 1);
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('PreClosedUrpayTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.WhilePointUrpayTask', '0 0/5 * * * ?', 1);
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('UrpaySummaryTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.UrpaySummaryTask', '0 0/30 * * * ?', 1);
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('SendSMSTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.SendSMSTask', '0/10 * * * * ?', 1);

--//@UNDO
-- SQL to undo the change goes here.

DELETE FROM tb_tc_quartz_task_init;
