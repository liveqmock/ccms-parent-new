--// alert_and_init_tb_tc_properties_config
-- Migration SQL that makes the change goes here.
 ALTER TABLE tb_tc_properties_config
    MODIFY COLUMN name  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置项' AFTER dp_id,
    MODIFY COLUMN value  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置值' AFTER name;
INSERT INTO tb_tc_properties_config (dp_id, name, value, description, group_name, created, updated) VALUES ( NULL, 'courierCompany', '申通E物流,圆通速递,韵达快运,EMS,中通速递,顺丰速运,天天快递,汇通快运,全峰快递,邮政国内小包,EMS经济快递,宅急送,国通快递', NULL, NULL, now(), now());
INSERT INTO tb_tc_properties_config (dp_id, name, value, description, group_name, created, updated) VALUES ( NULL, 'addressBlacklist', '村,镇,乡,旗,屯,自治区,自治州,林区,邮电,邮局,政府,监狱,军区,部队,寺,庙,香港,澳门,台湾,海外', NULL, 'addressBlacklist', now(), now());
INSERT INTO tb_tc_properties_config (dp_id, name, value, description, group_name, created, updated) VALUES ( NULL, 'addressBlacklistExclusive', '三里屯,中关村', NULL, 'addressBlacklist', now(), now());




--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_properties_config
  MODIFY COLUMN name  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置项' AFTER dp_id,
  MODIFY COLUMN value  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置值' AFTER name;
delete from tb_tc_properties_config where name = 'courierCompany';
delete from tb_tc_properties_config where name = 'addressBlacklist';
delete from tb_tc_properties_config where name = 'addressBlacklistExclusive';


