--// add mq logis init sql
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('MQlogisticsNoticeTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.MQlogisticsNoticeTask', '0 0 * * * ?', 1);


--//@UNDO
-- SQL to undo the change goes here.
DELETE FROM tb_tc_quartz_task_init where job_name = 'MQlogisticsNoticeTask' and job_group = 'TC_GROUP';

