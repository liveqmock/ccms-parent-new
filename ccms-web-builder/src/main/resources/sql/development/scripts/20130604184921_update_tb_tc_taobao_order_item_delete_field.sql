--// update tb_tc_taobao_order_item delete field
-- Migration SQL that makes the change goes here.
alter table tb_tc_taobao_order_item drop column customerno;
alter table tb_tc_taobao_order_item drop column created;
alter table tb_tc_taobao_order_item drop column endtime;
alter table tb_tc_taobao_order_item drop column pay_time;
alter table tb_tc_taobao_order_item drop column consign_time;
alter table tb_tc_taobao_order_item drop column modified;


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_taobao_order_item add customerno varchar(50) DEFAULT NULL COMMENT '客户ID';
alter table tb_tc_taobao_order_item add created datetime NOT NULL COMMENT '交易创建时间';
alter table tb_tc_taobao_order_item add endtime datetime DEFAULT NULL COMMENT '交易结束时间';
alter table tb_tc_taobao_order_item add pay_time datetime DEFAULT NULL COMMENT '付款时间';
alter table tb_tc_taobao_order_item add consign_time datetime DEFAULT NULL COMMENT '卖家发货时间';
alter table tb_tc_taobao_order_item add modified datetime DEFAULT NULL COMMENT '订单修改时间（冗余字段）';

