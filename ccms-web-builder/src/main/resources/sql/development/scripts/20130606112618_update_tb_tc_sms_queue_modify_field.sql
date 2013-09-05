--// update tb_tc_sms_queue modify field
-- Migration SQL that makes the change goes here.
alter table tb_tc_sms_queue modify sms_content text DEFAULT NULL COMMENT '短信内容';
alter table tb_tc_send_log modify sms_content varchar(500) DEFAULT NULL COMMENT '短信内容';

alter table tb_tc_urpay_config modify start_date datetime DEFAULT NULL COMMENT '订单范围开始时间';
alter table tb_tc_urpay_config modify end_date datetime DEFAULT NULL COMMENT '订单范围结束时间';

--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_sms_queue modify sms_content varchar(200) DEFAULT NULL COMMENT '短信内容';
alter table tb_tc_send_log modify sms_content varchar(200) DEFAULT NULL COMMENT '短信内容';

alter table tb_tc_urpay_config modify start_date date DEFAULT NULL COMMENT '订单范围开始时间';
alter table tb_tc_urpay_config modify end_date date DEFAULT NULL COMMENT '订单范围结束时间';
