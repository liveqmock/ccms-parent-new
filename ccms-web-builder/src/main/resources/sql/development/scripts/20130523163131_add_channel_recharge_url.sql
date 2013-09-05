--// add channel recharge url
-- Migration SQL that makes the change goes here.
INSERT INTO tb_app_properties (prop_group, prop_name, prop_value, prop_desc) VALUES 
( 'CHANNEL', 'channel_tenant_recharge_url', 'http://10.200.187.70:18090/yunat-recharge/ccmsController/accountManager?', '渠道充值链接地址');

-- 由于操作不规范, 导致开发库已经有一下字段, 
-- 添加此操作保证新部署库有以下字段
alter table plt_taobao_order
 add column step_trade_status varchar(50)
comment '万人团订单状态 FRONT_NOPAID_FINAL_NOPAID(未付订金),FRONT_PAID_FINAL_NOPAID(已付订金未付尾款)'
after status;

ALTER TABLE plt_taobao_crm_member ADD COLUMN last_sync datetime DEFAULT NULL;
--//@UNDO
-- SQL to undo the change goes here.

delete from tb_app_properties where prop_group='CHANNEL' and prop_name='channel_recharge_url';

alter table plt_taobao_order
 drop column step_trade_status;

ALTER TABLE plt_taobao_crm_member DROP COLUMN last_sync;
