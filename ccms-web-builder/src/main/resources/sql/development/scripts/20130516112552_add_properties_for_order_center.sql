--// add properties for order center
-- Migration SQL that makes the change goes here.

INSERT INTO tb_app_properties(prop_group, prop_name ,prop_value, prop_desc) VALUES ('CHANNEL', 'channel_shop_monitor_url', 'http://pre-urpay.ccms.fenxibao.com/yunat-urpay-web/sso?userName={0}&password={1}&key={2}&callback=ccmsbaseshop.urpay', '店铺监控');
INSERT INTO tb_app_properties(prop_group, prop_name ,prop_value, prop_desc) VALUES ('CHANNEL', 'channel_order_center_url', 'http://pre-urpay.ccms.fenxibao.com/yunat-urpay-web/sso?userName={0}&password={1}&key={2}&callback=ccmsbaseorder.urpay', '订单中心');
INSERT INTO tb_app_properties(prop_group, prop_name ,prop_value, prop_desc) VALUES ('CHANNEL', 'secret_key', 'key-bd736e85223a28c805917b0247118181', '店铺诊断和订单中心访问辅助秘钥');

--//@UNDO
-- SQL to undo the change goes here.

DELETE FROM tb_app_properties WHERE prop_group = 'CHANNEL' and prop_name = 'channel_shop_monitor_url';
DELETE FROM tb_app_properties WHERE prop_group = 'CHANNEL' and prop_name = 'channel_order_center_url';
DELETE FROM tb_app_properties WHERE prop_group = 'CHANNEL' and prop_name = 'secret_key';
