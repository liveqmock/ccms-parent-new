--// init_affair_category
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('2', '未付款', '未付款', NULL, '0', 'affairType', '0', '2013-08-15 10:09:35', '2013-08-15 10:09:37', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('3', '发货', '发货', NULL, '0', 'affairType', '0', '2013-08-15 10:10:04', '2013-08-15 10:10:07', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('4', '物流', '物流', NULL, '0', 'affairType', '0', '2013-08-15 10:10:26', '2013-08-15 10:10:28', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('5', '退款', '退款', NULL, '0', 'affairType', '0', '2013-08-15 10:11:00', '2013-08-15 10:11:03', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('6', '评价', '评价', NULL, '0', 'affairType', '0', '2013-08-15 10:11:22', '2013-08-15 10:11:26', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('10', '用户等级', '用户等级', NULL, '2', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('11', '催付', '催付', NULL, '2', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('13', '关怀', '关怀', NULL, '3', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('14', '延迟', '延迟', NULL, '3', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('15', '订单备注', '订单备注', NULL, '3', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('16', '疑难件', '疑难件', NULL, '4', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('17', '超区件', '超区件', NULL, '4', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('18', '拒收', '拒收', NULL, '4', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('19', '无流转', '无流转', NULL, '4', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('20', '确认', '确认', NULL, '5', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('21', '退货', '退货', NULL, '5', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('22', '关怀', '关怀', NULL, '5', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('23', '产品', '产品', NULL, '6', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('24', '包装', '包装', NULL, '6', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('25', '物流', '物流', NULL, '6', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('26', '服务', '服务', NULL, '6', 'affairType', '0', '2013-08-15 10:34:41', '2013-08-15 10:34:41', '0');



--//@UNDO
delete from   tb_tc_category where pkid <= 26;
-- SQL to undo the change goes here.


