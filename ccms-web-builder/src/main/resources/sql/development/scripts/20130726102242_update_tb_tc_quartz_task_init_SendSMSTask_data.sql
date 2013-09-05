--// update_tb_tc_quartz_task_init SendSMSTask data
-- Migration SQL that makes the change goes here.

update tb_tc_quartz_task_init set cron_expression='0 0/1 * * * ?'
 where job_name='SendSMSTask' and job_class_name='com.yunat.ccms.tradecenter.urpay.task.SendSMSTask';
update tb_tc_quartz_cron_triggers set cron_expression='0 0/1 * * * ?' where trigger_name='trigger-SendSMSTask';
update tb_tc_quartz_triggers set trigger_state='WAITING' where trigger_name='trigger-SendSMSTask';

--//@UNDO
-- SQL to undo the change goes here.

update tb_tc_quartz_task_init set cron_expression='0/10 * * * * ?'
 where job_name='SendSMSTask' and job_class_name='com.yunat.ccms.tradecenter.urpay.task.SendSMSTask';
update tb_tc_quartz_cron_triggers set cron_expression='0/10 * * * * ?' where trigger_name='trigger-SendSMSTask';
update tb_tc_quartz_triggers set trigger_state='WAITING' where trigger_name='trigger-SendSMSTask';
