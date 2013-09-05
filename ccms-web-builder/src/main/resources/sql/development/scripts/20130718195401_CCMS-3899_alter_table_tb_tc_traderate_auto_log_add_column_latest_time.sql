--// CCMS-3899 alter table tb_tc_traderate_auto_log add column latest_time
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_traderate_auto_set ADD COLUMN latest_time datetime COMMENT '最新一次订单自动回评时间';

DELETE FROM tb_tc_properties_config WHERE name = 'autoTraderateSetLatestTime';
	

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_traderate_auto_set DROP COLUMN latest_time;

INSERT INTO tb_tc_properties_config value(NULL, NULL, 'autoTraderateSetLatestTime', now(), '评价事务-自动回评后台最新执行时间', 'tradeRate', now(), now());