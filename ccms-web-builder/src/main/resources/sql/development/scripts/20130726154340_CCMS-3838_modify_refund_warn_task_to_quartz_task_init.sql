--// CCMS-3838_modify_refund_warn_task_to_quartz_task_init
-- Migration SQL that makes the change goes here.
UPDATE tb_tc_quartz_task_init SET cron_expression = '0 0 8,12,16,20 * * ?' WHERE job_name = 'RefundWarnTask' AND job_group = 'TC_GROUP';


--//@UNDO
-- SQL to undo the change goes here.


