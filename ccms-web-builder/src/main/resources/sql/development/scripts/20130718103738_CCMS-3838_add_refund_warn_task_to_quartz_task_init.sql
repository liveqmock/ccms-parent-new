--// CCMS-3838_add_refund_warn_task_to_quartz_task_init
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_quartz_task_init VALUE (NULL,'RefundWarnTask','TC_GROUP','com.yunat.ccms.tradecenter.urpay.task.RefundWarnTask','0 0/3 * * * ?','1');


--//@UNDO
-- SQL to undo the change goes here.
DELETE FROM tb_tc_quartz_task_init WHERE job_name = 'RefundWarnTask' AND job_group = 'TC_GROUP';

