--// initdata tb tc dict insert
-- Migration SQL that makes the change goes here.
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(1,1,'多笔订单只发送一次',0,1,'为避免对客户造成不好体验,催付短信不能在晚上22点到早晨9点间发送');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(1,2,'多个手机只发送一次',0,2,'为为避免对客户造成不好体验,催付短信不能在晚上22点到早晨9点间发送');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(1,3,'排除有支付过的客户',0,3,'为避免对客户造成不好体验,催付短信不能在晚上22点到早晨9点间发送');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(1,4,'屏蔽短信黑名单用户',0,4,'为避免对客户造成不好体验,催付短信不能在晚上22点到早晨9点间发送');

insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,-1,'不限',0,1,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,1,'新客户',0,2,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,2,'普通会员',0,3,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,3,'高级会员',0,4,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,4,'VIP',0,5,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,5,'至尊VIP',0,6,'');

alter table tb_tc_urpay_config add date_number Integer DEFAULT NULL COMMENT '非自定义日数数';

--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type in (1,2);
alter table tb_tc_urpay_config drop column date_number;

