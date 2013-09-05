--// modify_refund_warn_task_time
-- Migration SQL that makes the change goes here.
UPDATE tb_tc_quartz_task_init SET cron_expression = '0 10 8,12,16,20 * * ?' WHERE job_name = 'RefundWarnTask' AND job_group = 'TC_GROUP';


--//@UNDO
-- SQL to undo the change goes here.


