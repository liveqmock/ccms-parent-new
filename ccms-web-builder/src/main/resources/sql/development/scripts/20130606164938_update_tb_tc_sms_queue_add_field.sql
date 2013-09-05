--// update tb_tc_sms_queue add field
-- Migration SQL that makes the change goes here.
alter table tb_tc_sms_queue add gateway_id Integer DEFAULT NULL COMMENT '短信发送通道';
alter table tb_tc_send_log add gateway_id Integer DEFAULT NULL COMMENT '短信发送通道';
alter table tb_tc_send_log add send_status Integer DEFAULT NULL COMMENT '发送状态(0:失败，1:成功)';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_sms_queue drop column gateway_id;
alter table tb_tc_send_log drop column gateway_id;
alter table tb_tc_send_log drop column send_status;

