--// insert tb_tc_dict init data
-- Migration SQL that makes the change goes here.
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,1,'自动催付',0,1);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,2,'预关闭催付',0,2);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,3,'聚划算催付',0,3);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,4,'下单关怀',0,4);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,5,'发货通知',0,5);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,6,'同城通知',0,6);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,7,'派件通知',0,7);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,8,'签收通知',0,8);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,9,'退款关怀',0,9);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,10,'确认收货关怀',0,10);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,11,'评价关怀',0,11);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,12,'中差评告警',0,12);
insert into tb_tc_dict(type,code,name,is_valid,px) values(3,13,'退款告警',0,13);
alter table tb_tc_dict modify type int(11) DEFAULT NULL COMMENT '操作类型： 催付类： 1：自动催付 2：预关闭催付 3：聚划算催付 关怀类：4：下单关怀 5：发货通知 6：同城通知 7：派件通知 8：签收通知 9 ：退款关怀 10：确认收货关怀： 11：评价关怀  告警12：中差评告警 13：退款告警';


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=3;
alter table tb_tc_dict modify type int(11) DEFAULT NULL COMMENT '';
