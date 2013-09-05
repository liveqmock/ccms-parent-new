--// change properties for shop diagnosis
-- Migration SQL that makes the change goes here.

UPDATE tb_app_properties set prop_value = 'http://udp-test.ccms.fenxibao.com/udp-web/ccms?userName={0}&sign={1}' WHERE prop_group = 'CHANNEL' and prop_name = 'channel_shop_diagnosis_url';

--//@UNDO
-- SQL to undo the change goes here.

UPDATE tb_app_properties set prop_value = 'http://10.200.187.96:18081/udp-web/ccms?userName={0}&sign={1}' WHERE prop_group = 'CHANNEL' and prop_name = 'channel_shop_diagnosis_url';

