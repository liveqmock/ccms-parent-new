--// CCMS-3873 autoTraderate save tb_tc_properties_config
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_properties_config value(NULL, NULL, 'autoTraderateSetLatestTime', now(), '评价事务-自动回评后台最新执行时间', 'tradeRate', now(), now());

INSERT INTO tb_tc_properties_config value(NULL, NULL, 'autoTraderateSetMaxItemCount', '5000', '评价事务-自动回评统计子订单单次查询最大数量', 'tradeRate', now(), now());



--//@UNDO
-- SQL to undo the change goes here.
DELETE FROM tb_tc_properties_config WHERE name = 'autoTraderateSetLatestTime' AND group_name = 'tradeRate';

DELETE FROM tb_tc_properties_config WHERE name = 'autoTraderateSetMaxItemCount' AND group_name = 'tradeRate';

