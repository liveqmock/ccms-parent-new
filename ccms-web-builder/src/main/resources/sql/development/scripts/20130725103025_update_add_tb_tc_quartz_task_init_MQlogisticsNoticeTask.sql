--// update add tb_tc_quartz_task_init MQlogisticsNoticeTask
-- Migration SQL that makes the change goes here.
update tb_tc_quartz_task_init set cron_expression='0 0 6-22/2 * * ?'
 where job_name='MQlogisticsNoticeTask' and job_class_name='com.yunat.ccms.tradecenter.urpay.task.MQlogisticsNoticeTask';
update tb_tc_quartz_cron_triggers set cron_expression='0 0 6-22/2 * * ?' where trigger_name='trigger-MQlogisticsNoticeTask';


--//@UNDO
-- SQL to undo the change goes here.
update tb_tc_quartz_task_init set cron_expression='0 0 * * * ?'
 where job_name='MQlogisticsNoticeTask' and job_class_name='com.yunat.ccms.tradecenter.urpay.task.MQlogisticsNoticeTask';
update tb_tc_quartz_cron_triggers set cron_expression='0 0 * * * ?' where trigger_name='trigger-MQlogisticsNoticeTask';

