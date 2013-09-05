--// update wholeTask to PrecloseTask
-- Migration SQL that makes the change goes here.

update tb_tc_quartz_task_init  set job_class_name = 'com.yunat.ccms.tradecenter.urpay.task.PreClosedUrpayTask' where job_name = 'PreClosedUrpayTask' and job_class_name = 'com.yunat.ccms.tradecenter.urpay.task.WhilePointUrpayTask';
update tb_tc_quartz_job_details set job_class_name = 'com.yunat.ccms.tradecenter.urpay.task.PreClosedUrpayTask' where job_name = 'PreClosedUrpayTask' and job_class_name = 'com.yunat.ccms.tradecenter.urpay.task.WhilePointUrpayTask';

--//@UNDO
-- SQL to undo the change goes here.


