--// alter a table tb_tc_dict
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_dict  MODIFY COLUMN code  varchar(50) NOT NULL COMMENT '字典代码' AFTER type;
ALTER TABLE tb_tc_dict  MODIFY COLUMN name  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字典名称' AFTER code;
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#NAME#', 'logistics_company_key', 1, 1, '物流公司');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#ID#', 'logistics_id_key', 1, 2, '物流单号');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#BUYER#', 'buyer', 1, 3, '淘宝昵称');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#TRADE_CREATED#', 'trade_created', 1, 4, '下单时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#PAY_TIME#', 'paytime', 1, 5, '付款时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#SHOP#', 'seller', 1, 6, '店铺名称');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#CITY#', 'city', 1, 7, '收货城市');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 4, '#SIGN_TIME#', 'signtime', 1, 8, '收货时间');

--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=4;
ALTER TABLE tb_tc_dict  MODIFY COLUMN code  smallint(6) NOT NULL COMMENT '字典代码' AFTER type;
ALTER TABLE tb_tc_dict  MODIFY COLUMN name  varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字典名称' AFTER code;

