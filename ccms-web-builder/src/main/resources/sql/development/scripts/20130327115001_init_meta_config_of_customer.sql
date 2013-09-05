--// init meta config of customer
-- Migration SQL that makes the change goes here.

#淘宝客户信息统一视图(基础版)
CREATE OR REPLACE ALGORITHM = MERGE  VIEW vw_taobao_customer AS
SELECT
    u.uni_id          AS uni_id,    #客户统一ID
    taobao.customerno AS customerno ,  #TODO:所有视图中的客户ID字段必须统一命名成customerno
    u.full_name       AS full_name, #客户姓名
    u.sex AS sex,  #性别
    u.job AS job,  #职业
    YEAR(CURRENT_DATE)-u.birth_year AS age,  #年龄
    u.birthday, #生日
    u.email email,
    u.mobile mobile,
    u.state state, #省份
    u.city city,     #城市
    u.district,      #区域
    u.address,     #地址
    u.zip,            #邮编
    CASE WHEN u.mobile REGEXP '^(1[3,4,5,8]){1}[[:digit:]]{9}$' THEN '1' ELSE '0' END AS is_mobile_valid, #手机号是否有效
    CASE WHEN u.email REGEXP '[A-Za-z0-9._-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,4}$' THEN '1' ELSE '0' END AS is_email_valid, #email是否有效
    taobao.vip_info,              #客户全站等级
    taobao.buyer_credit_lev, #买家信用等级
    taobao.created,              #淘宝用户注册时间
    round (CASE WHEN (taobao.buyer_credit_total_num > 0)
           THEN ((taobao.buyer_credit_good_num*1.0 / taobao.buyer_credit_total_num) * 100)
           ELSE NULL END, 1)                       AS buyer_good_ratio,  #买家好评率
    DATE_FORMAT(CURRENT_DATE, '%Y年%m月%d日')      AS ymd,               #当天年月日
    DATE_FORMAT(CURRENT_DATE, '%Y年%m月')          AS ym,                #当天年月
    IFNULL(crm.grade, '') AS grade,									#会员等级
    IFNULL(label.label_name, '') AS　label_name			#客户标签
FROM  plt_taobao_customer taobao
    Left Join uni_customer_plat cp On cp.customerno = taobao.customerno And cp.plat_code = 'taobao'
    Left Join uni_customer u On u.uni_id = cp.uni_id
    Left Join plt_taobao_crm_member crm on taobao.customerno = crm.customerno
    Left Join plt_kfgzt_customer_label label on taobao.customerno = label.customerno;

#-----------------------------------------------------------------------------------------------------------------------
#  初始化元数据表定义
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (1, 'vw_taobao_customer', 	'客户信息', 'uni_id', 'TAOBAO',	now(), now());

#-----------------------------------------------------------------------------------------------------------------------
#  初始化元数据字段定义
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (1,    1, NULL, NULL, 'uni_id', 					'客户统一ID',		'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (2,    1, NULL, NULL, 'customerno',			'客户ID', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (3,    1, NULL, NULL, 'full_name', 			'姓名', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (4,    1, 		1, NULL, 'sex', 						'性别', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (5,    1, NULL, NULL, 'job', 						'职业', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (6,    1, NULL, NULL, 'age',							'年龄', 				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (7,    1, NULL, NULL, 'birthday', 				'生日', 				'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (8,    1, NULL, NULL, 'email', 					'邮箱', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (9,    1, NULL, NULL, 'mobile', 					'手机', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (10,   1, 	 22, NULL, 'state', 					'省份', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (11,   1,   23, NULL, 'city', 						'城市', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (12,   1, NULL, NULL, 'district', 				'区域', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (13,   1, NULL, NULL, 'address', 				'地址', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (14,   1, NULL, NULL, 'zip', 						'邮编', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (15,   1, 		3, NULL, 'is_mobile_valid',	'手机号是否有效', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (16,   1, 		3, NULL, 'is_email_valid',	'邮箱是否有效', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (17,   1,   41, NULL, 'vip_info', 				'客户全站等级', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (18,   1,   42, NULL, 'buyer_credit_lev','买家信用等级', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (19,   1, NULL, NULL, 'created', 				'淘宝用户注册时间','date', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (20,   1, NULL, NULL, 'buyer_good_ratio','买家好评率', 	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (21,   1, NULL, NULL, 'ymd',							'当天年月日', 	'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (22,   1, NULL, NULL, 'ym',							'当天年月', 		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (23,   1,   43, NULL, 'grade',						'会员等级', 		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (24,   1, NULL, NULL, 'label_name',			'客户标签', 		'string', 	0, now(), now(), null);

#-----------------------------------------------------------------------------------------------------------------------
#  初始化查询模版
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_query(query_id, code, show_name, plat_code) values(1, 'customer', 	'客户信息', 'TAOBAO');

INSERT INTO tm_query_table(query_table_id, query_id, table_id) values(1, 1, 1);

INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (2,    1,		2,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (3,    1,		3,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (4,    1,		4,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (5,    1,		5,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (7,    1,		7,		'BIRTHDAY', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (8,    1,		8,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (9,    1,		9,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (10,   1,		10,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (11,   1,		11,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (13,   1,		13,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (14,   1,		14,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (17,   1,		17,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (18,   1,		18,		'ORDERED_DIC', 	NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (20,   1,		20,		'NUMBER', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (23,   1,		23,		'ORDERED_DIC', 	NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (24,   1,		24,		'STRING', 			NULL,  NULL);

#-----------------------------------------------------------------------------------------------------------------------
#  索引查询字段
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (2,    1,		2,		'淘宝昵称', 	5);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (3,    1,		3,		NULL, 				1);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (4,    1,		4,		NULL, 				2);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (5,    1,		5,		NULL, 				15);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (7,    1,		7,		NULL, 				3);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (8,    1,		8,		NULL, 				7);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (9,    1,		9,		NULL, 				6);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (10,   1,		10,		NULL, 				4);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (11,   1,		11,		NULL, 				4);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (13,   1,		13,		NULL, 				13);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (14,   1,		14,		NULL, 				14);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (17,   1,		17,		NULL, 				12);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (18,   1,		18,		NULL, 				10);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (20,   1,		20,		NULL, 				11);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (23,   1,		23,		NULL, 				8);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (24,   1,		24,		NULL, 				9);

--//@UNDO
-- SQL to undo the change goes here.

DELETE FROM tm_db_table where table_id = 1;
DELETE FROM tm_db_column where table_id = 1;
DELETE FROM tm_query where query_id = 1;
DELETE FROM tm_query_table where query_id = 1;
DELETE FROM tm_query_criteria where query_id = 1;
DELETE FROM tm_catalog_criteria where catalog_id = 1;
