--// add config channel_client_get_resp_wait_time
-- Migration SQL that makes the change goes here.
INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES 
('CHANNEL', 'channel_client_get_resp_wait_time', '300000', 'CCMS调用渠道获取发送结果等待时间 ');





--//@UNDO
-- SQL to undo the change goes here.
delete from tb_app_properties where prop_name='channel_client_get_resp_wait_time';

