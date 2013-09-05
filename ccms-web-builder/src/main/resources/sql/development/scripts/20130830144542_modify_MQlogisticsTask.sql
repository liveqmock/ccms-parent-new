--// modify_MQlogisticsTask
-- Migration SQL that makes the change goes here.
update tb_tc_quartz_task_init t set t.cron_expression = '0 0 8,12,16,20 * * ?' where t.job_name = 'MQlogisticsNoticeTask';
update tb_tc_quartz_task_init t set t.cron_expression = '0 0 6,7,9,10,11,13,14,15,17,18,19,21,22 * * ?' where t.job_name = 'mQLogisticsCareOpenTask';


--//@UNDO
-- SQL to undo the change goes here.
update tb_tc_quartz_task_init t set t.cron_expression = '0 0 6-22/2 * * ?' where t.job_name = 'MQlogisticsNoticeTask';
update tb_tc_quartz_task_init t set t.cron_expression = '0 0 6-22/2 * * ?' where t.job_name = 'mQLogisticsCareOpenTask';


