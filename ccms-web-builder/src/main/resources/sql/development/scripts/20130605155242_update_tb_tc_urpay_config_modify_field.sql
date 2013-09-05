--// update tb_tc_urpay_config modify field
-- Migration SQL that makes the change goes here.

alter table tb_tc_urpay_config modify urpay_start_time time DEFAULT NULL COMMENT '催付时间开始时间';
alter table tb_tc_urpay_config modify urpay_end_time time DEFAULT NULL COMMENT '催付时间结束时间';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_urpay_config modify urpay_start_time datetime DEFAULT NULL COMMENT '催付时间开始时间';
alter table tb_tc_urpay_config modify urpay_end_time datetime DEFAULT NULL COMMENT '催付时间结束时间';

