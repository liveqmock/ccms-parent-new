--// init meta config of order
-- Migration SQL that makes the change goes here.


#-----------------------------------------------------------------------------------------------------------------------
#  调整表结构
#-----------------------------------------------------------------------------------------------------------------------
alter table tm_query_criteria change quota_type quota_type varchar(200) null COMMENT '指标类型';
alter table twf_node_query_criteria add sub_group varchar(32) null comment '嵌套的标识, 仅支持嵌套一层, 空表示字段不嵌套';
alter table twf_node_query_criteria add relation varchar(3) null comment '关系符and or, 空默认为and';
alter table tm_query_table add is_master boolean not null comment '是否主表';
alter table twf_node_query_criteria drop column target_type;

#-----------------------------------------------------------------------------------------------------------------------
#  初始化元数据表定义
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (2, 'plt_taobao_order', 			'订单信息', 	'tid',			'TAOBAO', now(), now());
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (3, 'plt_taobao_order_item', 	'子订单信息', 'oid',			'TAOBAO', now(), now());
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (4, 'plt_taobao_product',		'商品信息', 	'num_iid',		'TAOBAO', now(), now());

#-----------------------------------------------------------------------------------------------------------------------
#  初始化元数据字段定义
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (51,   2, NULL, NULL, 'tid', 							'订单号',							'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (52,   2, NULL, NULL, 'dp_id',		  				'店铺ID',							'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (53,   2, NULL, NULL, 'customerno',				'客户ID', 						'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (54,   2, NULL, NULL, 'created', 					'交易创建时间',				'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (55,   2, NULL, NULL, 'endtime', 					'交易结束时间',				'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (56,   2, NULL, NULL, 'status',						'交易状态', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (57,   2, NULL, NULL, 'trade_from',				'交易来源', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (58,   2, NULL, NULL, 'type', 							'交易类型', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (59,   2, NULL, NULL, 'pay_time', 					'付款时间', 					'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (60,   2, NULL, NULL, 'total_fee', 				'商品金额', 					'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (61,   2, NULL, NULL, 'post_fee', 					'邮费', 							'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (62,   2, NULL, NULL, 'consign_time',			'卖家发货时间', 			'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (63,   2, NULL, NULL, 'ccms_order_status',	'CCMS交易状态', 			'string',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (64,   2, NULL, NULL, 'modified', 					'订单修改时间', 			'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (65,   2, NULL, NULL, 'alipay_no', 				'支付宝交易号', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (66,   2, NULL, NULL, 'payment', 					'实付金额', 					'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (67,   2, NULL, NULL, 'discount_fee',			'系统优惠金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (68,   2, NULL, NULL, 'point_fee', 				'买家使用积分',				'number', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (69,   2, NULL, NULL, 'real_point_fee',		'实际使用积分', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (70,   2, NULL, NULL, 'shipping_type',			'物流方式', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (71,   2, NULL, NULL, 'buyer_cod_fee',			'买家货到付款服务费',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (72,   2, NULL, NULL, 'seller_cod_fee',		'卖家货到付款服务费',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (73,   2, NULL, NULL, 'express_agency_fee','快递代收款', 				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (74,   2, NULL, NULL, 'adjust_fee', 				'手工调整金额', 			'number',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (75,   2, NULL, NULL, 'buyer_obtain_point_fee', 	'买家获得积分',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (76,   2, NULL, NULL, 'cod_fee', 					'货到付款服务费', 		'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (77,   2, NULL, NULL, 'cod_status', 				'货到付款物流状态',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (78,   2, NULL, NULL, 'buyer_alipay_no', 	'买家支付宝账号',			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (79,   2, NULL, NULL, 'receiver_name', 		'收货人的姓名', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (80,   2, NULL, NULL, 'receiver_state', 		'收货人的所在省份',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (81,   2, NULL, NULL, 'receiver_city', 		'收货人的所在城市',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (82,   2, NULL, NULL, 'receiver_district', '收货人的所在地区',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (83,   2, NULL, NULL, 'receiver_address', 	'收货人的详细地址',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (84,   2, NULL, NULL, 'receiver_zip',			'收货人的邮编', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (85,   2, NULL, NULL, 'receiver_mobile',		'收货人的手机号码',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (86,   2, NULL, NULL, 'receiver_phone',		'收货人的电话号码',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (87,   2, NULL, NULL, 'buyer_email', 			'买家邮件地址', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (88,   2, NULL, NULL, 'commission_fee',		'交易佣金', 					'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (89,   2, NULL, NULL, 'refund_fee', 				'子订单的退款金额合计','number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (90,   2, NULL, NULL, 'num', 							'商品数量总计', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (91,   2, NULL, NULL, 'received_payment',	'支付宝打款金额', 		'number', 	0, now(), now(), null);


INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (151,   3, NULL, NULL, 'oid', 							'子订单ID',				'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (152,   3, NULL, NULL, 'tid', 							'订单ID',					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (153,   3, NULL, NULL, 'dp_id',		  			'店铺ID',					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (154,   3, NULL, NULL, 'customerno',				'客户ID', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (155,   3, NULL, NULL, 'total_fee', 				'应付金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (156,   3, NULL, NULL, 'discount_fee',			'订单优惠金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (157,   3, NULL, NULL, 'adjust_fee', 			'手工调整金额',			'number',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (158,   3, NULL, NULL, 'payment', 					'实付金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (159,   3, NULL, NULL, 'status',						'订单状态', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (160,   3, NULL, NULL, 'num', 							'购买数量', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (161,   3, NULL, NULL, 'num_iid', 					'商品ID', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (162,   3, NULL, NULL, 'created', 					'交易创建时间',		'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (163,   3, NULL, NULL, 'endtime', 					'交易结束时间',		'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (164,   3, NULL, NULL, 'trade_from',				'交易来源', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (165,   3, NULL, NULL, 'type', 						'交易类型', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (166,   3, NULL, NULL, 'pay_time', 				'付款时间', 			'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (167,   3, NULL, NULL, 'consign_time',			'卖家发货时间', 	'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (168,   3, NULL, NULL, 'refund_status', 		'退款状态',				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (169,   3, NULL, NULL, 'refund_fee', 			'退款金额',				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (170,   3, NULL, NULL, 'ccms_order_status','CCMS交易状态', 	'number',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (171,   3, NULL, NULL, 'title', 						'商品标题', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (172,   3, NULL, NULL, 'modified', 				'订单修改时间', 	'date', 		0, now(), now(), null);


INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (241,   4, NULL, NULL, 'num_iid',						'商品数字ID',		'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (242,   4, NULL, NULL, 'detail_url', 				'商品URL',			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (243,   4, NULL, NULL, 'title',		  				'商品名称',			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (244,   4, NULL, NULL, 'created',						'发布时间', 		'datetime', 0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (245,   4, NULL, NULL, 'is_fenxiao', 				'是否分销商品',	'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (246,   4, NULL, NULL, 'cid',								'商品叶子类目',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (247,   4, NULL, NULL, 'pic_url', 						'商品图片地址', 'string',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (248,   4, NULL, NULL, 'list_time', 					'商品上架时间',	'datetime',	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (249,   4, NULL, NULL, 'delist_time',				'商品下架时间',	'datetime', 0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (250,   4, NULL, NULL, 'price', 							'价格', 				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (251,   4, NULL, NULL, 'modified', 					'修改时间', 		'datetime', 0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (252,   4, NULL, NULL, 'approve_status', 		'上传后状态',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (253,   4, NULL, NULL, 'dp_id', 							'店铺ID',				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (254,   4, NULL, NULL, 'outer_id',						'商家外部编码', 'string', 	0, now(), now(), null);

#-----------------------------------------------------------------------------------------------------------------------
#  初始化查询模版
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_query(query_id, code, show_name, plat_code) values(2, 	'order', 			'订单总体消费查询',		'TAOBAO');
INSERT INTO tm_query(query_id, code, show_name, plat_code) values(3, 	'orderitem',	'订单商品消费查询',		'TAOBAO');

UPDATE tm_query_table set is_master = 1 where query_table_id = 1;
INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(2, 2, 2, 1);
INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(3, 3, 3, 1);
INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(4, 3, 4, 0);

INSERT INTO tm_query_join_criteria(query_join_id, query_id, left_column_id, right_column_id, join_type) values(1, 3, 161, 241, 'INNER');

#-----------------------------------------------------------------------------------------------------------------------
#  初始化查询模版的查询条件
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (51,    2,		51,			'STRING', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (54,    2,		54,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (55,    2,		55,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (57,    2,		57,			'STRING', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (58,    2,		58,			'STRING', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (59,    2,		59,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (62,    2,		62,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (63,    2,		63,			'STRING', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (64,    2,		64,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (111,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.BUY_FEE',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (112,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.BUY_NUM',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (113,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.BUY_FREQ',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (114,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.LAST_INTERVAL', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (115,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.FIRST_INTERVAL',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (116,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.LAST_BUY_TIME', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (117,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.FIRST_BUY_TIME',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (118,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.AVG_BUY_FEE',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (119,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.AVG_BUY_FREQ',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (120,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.REFUND_NUM',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (121,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.REFUND_FEE',  	NULL);

INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (153,    3,		153,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (161,    3,		161,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (162,    3,		162,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (163,    3,		163,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (164,    3,		164,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (165,    3,		165,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (166,    3,		166,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (167,    3,		167,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (168,    3,		168,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (169,    3,		169,		'NUMBER', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (170,    3,		170,		'NUMBER', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (243,    3,		243,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (191,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.BUY_FEE',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (192,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.BUY_NUM',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (193,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.BUY_FREQ',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (194,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.LAST_INTERVAL', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (195,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.FIRST_INTERVAL',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (196,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.LAST_BUY_TIME', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (197,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.FIRST_BUY_TIME',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (198,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.AVG_BUY_FEE',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (199,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.AVG_BUY_FREQ',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (200,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.REFUND_NUM',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (201,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.REFUND_FEE',  	NULL);

--//@UNDO
-- SQL to undo the change goes here.

alter table twf_node_query_criteria drop sub_group;
alter table twf_node_query_criteria drop relation;
alter table tm_query_table drop is_master;
alter table twf_node_query_criteria add column target_type varchar(32) null;

DELETE FROM tm_db_table where table_id in(2,3,4);
DELETE FROM tm_db_column where table_id in(2,3,4);
DELETE FROM tm_query where query_id in(2,3);
DELETE FROM tm_query_table where query_id in(2,3);
DELETE FROM tm_query_join_criteria where query_id in(2,3);
DELETE FROM tm_query_criteria where query_id in(2,3);
