--// CCMS-3836_add_not_good_warn_task_to_quartz_task_init
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_quartz_task_init VALUE (NULL,'NotGoodWarnTask','TC_GROUP','com.yunat.ccms.tradecenter.urpay.task.NotGoodWarnTask','0 0/5 * * * ?','1');


--//@UNDO
-- SQL to undo the change goes here.
DELETE FROM tb_tc_quartz_task_init WHERE job_name = 'NotGoodWarnTask' AND job_group = 'TC_GROUP';