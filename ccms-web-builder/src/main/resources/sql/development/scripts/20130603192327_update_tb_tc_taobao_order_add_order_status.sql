--// update tb_tc_taobao_order add order_status
-- Migration SQL that makes the change goes here.
alter table tb_tc_taobao_order add order_status Integer DEFAULT NULL COMMENT '订单状态';


delete from tb_tc_dict where type=2;
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,-1,'不限',0,1,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,0,'新客户',0,2,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,1,'普通会员',0,3,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,2,'高级会员',0,4,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,3,'VIP',0,5,'');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,4,'至尊VIP',0,6,'');

--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_taobao_order drop column order_status;
delete from tb_tc_dict where type=2;
