--// update add tb_tc_quartz_task_init CloseOpen30DaysUrapyOrCareTask
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('CloseOpen30DaysUrapyOrCareTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.CloseOpen30DaysUrapyOrCareTask', '0 5 0 * * ?', 1);


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_quartz_task_init where job_name='CloseOpen30DaysUrapyOrCareTask' and job_class_name='com.yunat.ccms.tradecenter.urpay.task.CloseOpen30DaysUrapyOrCareTask';

