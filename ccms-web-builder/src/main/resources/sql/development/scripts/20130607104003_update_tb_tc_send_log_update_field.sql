--// update tb_tc_send_log update field
-- Migration SQL that makes the change goes here.
alter table tb_tc_urpay_config modify sms_content text DEFAULT NULL COMMENT '短信内容';
alter table tb_tc_send_log modify sms_content text DEFAULT NULL COMMENT '短信内容';
alter table tb_tc_send_log modify trade_created datetime DEFAULT NULL COMMENT '订单创建时间';


--//@UNDO
-- SQL to undo the change goes here.

alter table tb_tc_urpay_config modify sms_content varchar(500) DEFAULT NULL COMMENT '短信内容';
alter table tb_tc_send_log modify sms_content varchar(500) DEFAULT NULL COMMENT '短信内容';
alter table tb_tc_send_log modify trade_created datetime DEFAULT NULL COMMENT '订单创建时间';
