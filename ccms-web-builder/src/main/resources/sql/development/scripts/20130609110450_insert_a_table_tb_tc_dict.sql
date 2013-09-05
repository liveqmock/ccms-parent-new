--// insert a table tb_tc_dict
-- Migration SQL that makes the change goes here.
delete from tb_tc_dict where type=4;
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#CUSTOMERNO#', 'customerno', 1, 1, '淘宝昵称');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#RECEIVER_NAME#', 'receiver_name', 1, 2, '姓名');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#CREATED#', 'created', 1, 3, '下单时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#TID#', 'tid', 1, 4, '订单编号');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#PAYMENT#', 'payment', 1, 5, '付款金额');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#CONSIGN_TIME#', 'consign_time', 1, 6, '发货时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#ARRIVED_TIME#', 'arrived_time', 1, 7, '同城时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#DELIVERY_TIME#', 'delivery_time', 1, 8, '派件时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#SIGNED_TIME#', 'signed_time', 1, 9, '签收时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#PAY_TIME#', 'pay_time', 1, 10, '付款时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#COMPANY_NAME#', 'company_name', 1, 11, '物流公司');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#OUT_SID#', 'out_sid', 1, 12, '物流单号');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#RECEIVER_CITY#', 'receiver_city', 1, 13, '收货城市');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#RECEIVER_DISTRICT#', 'receiver_district', 1, 14, '收货县区');


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=4;
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#NAME#', 'logistics_company_key', 1, 1, '物流公司');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#ID#', 'logistics_id_key', 1, 2, '物流单号');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#BUYER#', 'buyer', 1, 3, '淘宝昵称');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#TRADE_CREATED#', 'trade_created', 1, 4, '下单时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#PAY_TIME#', 'paytime', 1, 5, '付款时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#SHOP#', 'seller', 1, 6, '店铺名称');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#CITY#', 'city', 1, 7, '收货城市');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#SIGN_TIME#', 'signtime', 1, 8, '收货时间');

