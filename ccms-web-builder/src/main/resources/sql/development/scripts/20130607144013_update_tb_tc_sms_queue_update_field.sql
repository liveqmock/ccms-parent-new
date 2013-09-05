--// update tb_tc_sms_queue update field
-- Migration SQL that makes the change goes here.
alter table tb_tc_sms_queue modify trade_created datetime DEFAULT NULL COMMENT '订单创建时间';

--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_sms_queue modify trade_created varchar(20) DEFAULT NULL COMMENT '订单创建时间';

