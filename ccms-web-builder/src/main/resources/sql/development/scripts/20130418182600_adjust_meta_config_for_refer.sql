--// adjust meta config for refer
-- Migration SQL that makes the change goes here.

ALTER TABLE tm_refer add order_column varchar(32) DEFAULT NULL COMMENT '显示顺序';

UPDATE tm_db_column set dic_id = 44 where column_id = 57;
UPDATE tm_db_column set refer_id = 1 where column_id = 63;

UPDATE tm_query_criteria set query_type = 'DIC' where query_criteria_id = 57;
UPDATE tm_query_criteria set query_type = 'REFER' where query_criteria_id = 63;

INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (44, '交易来源', 	'TAOBAO', NULL);

INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1101,	44, 	NULL, 	'HITAO', 	'嗨淘');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1102,	44, 	NULL, 	'JHS', 		'聚划算');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1103,	44, 	NULL, 	'TAOBAO',	'普通淘宝');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1104,	44, 	NULL, 	'TOP', 		'TOP平台');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1105,	44, 	NULL, 	'WAP', 		'手机');

INSERT INTO tm_refer (refer_id, plat_code, refer_key, refer_name, refer_table, refer_criteria_sql, remark, order_column) VALUES (1, 'TAOBAO', 'status_value', 'status_name', 'tds_order_status', NULL, NULL, 'orderid');

--//@UNDO
-- SQL to undo the change goes here.

alter table tm_refer drop order_column;

UPDATE tm_db_column set dic_id = null where column_id = 57;
UPDATE tm_db_column set refer_id = null where column_id = 63;

UPDATE tm_query_criteria set query_type = 'STRING' where query_criteria_id = 57;
UPDATE tm_query_criteria set query_type = 'STRING' where query_criteria_id = 63;

DELETE FROM tm_dic where dic_id = 44;
DELETE FROM tm_dic_Value where dic_id = 44;

DELETE FROM tm_refer where refer_id = 1;


