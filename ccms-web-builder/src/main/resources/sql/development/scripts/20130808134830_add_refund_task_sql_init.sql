--// add refund task sql init
-- Migration SQL that makes the change goes here.

INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('RefundCareTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.RefundCareTask', '0 0/5 * * * ?', 1);

--//@UNDO
-- SQL to undo the change goes here.

delete from tb_tc_quartz_task_init where job_name = 'RefundCareTask' and job_class_name = 'com.yunat.ccms.tradecenter.urpay.task.RefundCareTask';