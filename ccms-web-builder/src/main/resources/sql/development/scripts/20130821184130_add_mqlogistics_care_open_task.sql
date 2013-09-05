--// add_mqlogistics_care_open_task
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('mQLogisticsCareOpenTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.MQLogisticsCareOpenTask', '0 0/30 * * * ?', 1);



--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_quartz_task_init where job_name = 'mQLogisticsCareOpenTask' and job_class_name = 'com.yunat.ccms.tradecenter.urpay.task.MQLogisticsCareOpenTask';


