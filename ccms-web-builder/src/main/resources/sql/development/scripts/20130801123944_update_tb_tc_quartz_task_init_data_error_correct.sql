--// update_tb_tc_quartz_task_init data error correct
-- Migration SQL that makes the change goes here.
delete from tb_tc_quartz_task_init where job_name='MQlogisticsNoticeTask' and job_class_name='com.yunat.ccms.tradecenter.urpay.task.MQlogisticsNoticeTask';
INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid)
    VALUES ('MQlogisticsNoticeTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.MQlogisticsNoticeTask', '0 0 6-22/2 * * ?', 1);

update tb_tc_quartz_cron_triggers set cron_expression='0 0 6-22/2 * * ?' where trigger_name='trigger-MQlogisticsNoticeTask';
update tb_tc_quartz_triggers set trigger_state='WAITING' where trigger_name='trigger-MQlogisticsNoticeTask';

--//@UNDO
-- SQL to undo the change goes here.
update tb_tc_quartz_cron_triggers set cron_expression='0 0 * * * ?' where trigger_name='trigger-MQlogisticsNoticeTask';

