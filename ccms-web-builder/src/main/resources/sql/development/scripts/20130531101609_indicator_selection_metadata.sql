--// indicator selection metadata 
-- Migration SQL that makes the change goes here.

-- 基于客人的指标类型
insert into tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated, remark)
	values (10, 'vw_taobao_customer_quota', '基于客人的指标类型', 'uni_id', 'taobao', NOW(), NOW(), 'ps:不参与sql关联查询');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (500, 10, NULL, 'uni_id', '客户统一id', 'string', '1', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (501, 10, 50, 'customer_type', '客户类型', 'string', '0', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (502, 10, 43, 'grade', '会员等级', 'string', '0', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (503, 10, 42, 'buyer_good_ratio', '信用等级', 'string', '0', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (504, 10, 41, 'vip_info', '全站等级', 'string', '0', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (505, 10, NULL, 'trade_count', '交易成功笔数', 'number', '0', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (506, 10, NULL, 'trade_amount', '交易成功的金额', 'number', '0', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (507, 10, NULL, 'last_trade_date_diff', '最后交易间隔(天)', 'number', '0', NOW(), NOW(), '');

-- 客户类型dic
insert into tm_dic (dic_id, plat_code, show_name, remark) 
	values (50, 'taobao', '客户类型', '个性化包裹:基于客户的指标类型选择中的客户类型');
	
insert into tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name, remark)
	values (1500, 50, NULL, 1, '首次购买客人', '');

insert into tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name, remark)
	values (1501, 50, NULL, 2, '回头客', '');

-- 基于订单的指标类型
insert into tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated, remark)
	values (11, 'vw_taobao_order_quota', '基于订单的指标类型', 'tid', 'taobao', NOW(), NOW(), '');
	
insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (600, 11, NULL, 'tid', '订单号', 'string', '1', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (601, 11, 44, 'trade_from', '交易来源', 'string', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (602, 11, NULL, 'payment', '实付金额', 'number', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (603, 11, NULL, 'product_count', '商品种数', 'number', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (604, 11, NULL, 'product_amount', '商品件数', 'number', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (605, 11, NULL, 'receiver_district', '收货人所在地', 'string', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (606, 11, NULL, 'discount_fee', '订单优惠金额', 'number', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (607, 11, NULL, 'post_fee', '邮费', 'number', '0', NOW(), NOW(), '');

insert into tm_db_column (column_id, table_id, dic_id, db_name, show_name, db_type, is_pk, created, updated, remark)
	values (608, 11, NULL, 'has_products', '包含商品', 'string', '0', NOW(), NOW(), '备注:多个num_iid以逗号分隔开来');
	
--//@UNDO
-- SQL to undo the change goes here.

delete from tm_db_table where table_id in (10, 11);
delete from tm_db_column where column_id in (500, 501, 502, 503, 504, 505, 506, 507);
delete from tm_db_column where column_id in (600, 601, 602, 603, 604, 605, 606, 607, 608);
delete from tm_dic where dic_id = 50;
delete from tm_dic_value where dic_value_id in (1500, 1501);
