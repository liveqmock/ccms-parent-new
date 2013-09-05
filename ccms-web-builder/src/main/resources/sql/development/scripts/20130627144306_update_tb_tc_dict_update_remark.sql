--// update tb_tc_dict update remark
-- Migration SQL that makes the change goes here.
update tb_tc_dict set name='同一个手机号码只发送一次' where type=1 and code=2;
update tb_tc_dict set remark='在店铺过没有交易成功订单的客户' where type=2 and code=0;
update tb_tc_dict set remark='在店铺有过交易成功订单但没有会员等级的客户' where type=2 and code=99;

INSERT INTO tb_tc_quartz_task_init(job_name,job_group,job_class_name,cron_expression,is_valid) VALUES ('CustomerOrdersTask', 'TC_GROUP', 'com.yunat.ccms.tradecenter.urpay.task.CustomerOrdersTask', '0 0/5 * * * ?', 1);

--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_quartz_task_init where  job_name = 'CustomerOrdersTask' and job_group = 'TC_GROUP';
