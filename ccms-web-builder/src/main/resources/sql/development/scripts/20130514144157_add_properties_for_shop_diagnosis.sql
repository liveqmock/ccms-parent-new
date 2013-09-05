--// add properties for shop diagnosis
-- Migration SQL that makes the change goes here.

INSERT INTO tb_app_properties(prop_group, prop_name ,prop_value, prop_desc) VALUES ('CHANNEL', 'channel_shop_diagnosis_url', 'http://10.200.187.96:18081/udp-web/ccms?userName={0}&sign={1}', '店铺诊断');

--//@UNDO
-- SQL to undo the change goes here.

DELETE FROM tb_app_properties WHERE prop_group = 'CHANNEL' and prop_name = 'channel_shop_diagnosis_url';
