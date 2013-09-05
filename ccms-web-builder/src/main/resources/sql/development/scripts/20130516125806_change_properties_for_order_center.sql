--// change properties for order center
-- Migration SQL that makes the change goes here.

UPDATE tb_app_properties set prop_name = 'channel_ordercenter_secret_key' where prop_group = 'CHANNEL' and prop_name = 'secret_key';

--//@UNDO
-- SQL to undo the change goes here.

UPDATE tb_app_properties set prop_name = 'secret_key' where prop_group = 'CHANNEL' and prop_name = 'channel_ordercenter_secret_key';
