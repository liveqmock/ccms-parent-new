--// update tb tc taobao order add field
-- Migration SQL that makes the change goes here.
alter table tb_tc_taobao_order add post_fee decimal(12,2) COMMENT '邮费';
alter table tb_tc_taobao_order add shipping_type varchar(20) COMMENT '创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：ems, express, post, free, virtual';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_taobao_order drop column post_fee;
alter table tb_tc_taobao_order drop column shipping_type;

