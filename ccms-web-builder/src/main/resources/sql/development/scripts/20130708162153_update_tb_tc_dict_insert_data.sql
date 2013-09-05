--// update tb_tc_dict insert data
-- Migration SQL that makes the change goes here.
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(5,'3','排除自动确认收货的订单',0,3,'淘宝默认发货后10天自动确认收货，排除确认收货时间在发货10天后的订单');


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=5 and code='3';

