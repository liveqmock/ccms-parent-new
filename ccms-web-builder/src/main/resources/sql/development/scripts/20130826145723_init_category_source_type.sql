--// init_category_source_type
-- Migration SQL that makes the change goes here.
delete from tb_tc_category;

INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('2', '未付款', '未付款', NULL, '0', 'affairType', '0', '2013-08-15 10:09:35', '2013-08-15 10:09:37', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('3', '发货', '发货', NULL, '0', 'affairType', '0', '2013-08-15 10:10:04', '2013-08-15 10:10:07', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('4', '物流', '物流', NULL, '0', 'affairType', '0', '2013-08-15 10:10:26', '2013-08-15 10:10:28', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('5', '退款', '退款', NULL, '0', 'affairType', '0', '2013-08-15 10:11:00', '2013-08-15 10:11:03', '0');
INSERT INTO tb_tc_category (pkid, name, value, description, parent_id, out_type, out_id, created, updated, is_delete) VALUES ('6', '评价', '评价', NULL, '0', 'affairType', '0', '2013-08-15 10:11:22', '2013-08-15 10:11:26', '0');

INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '用户等级', '用户等级', '跟客户等级有关的问题，比如不同的等级，进行不同的订单处理', '2', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '催付', '催付', '订单需要进行催付处理，或者与催付有关的问题', '2', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '关怀', '关怀', '需要对用户进行关怀，或者与关怀有关的问题', '3', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '延迟', '延迟', '订单发货出现延迟问题', '3', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '订单备注', '订单备注', '订单需要根据备注，进行特殊处理', '3', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '疑难件', '疑难件', '包裹运送中，被判定为疑难件', '4', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '超区件', '超区件', '该物流超出了物流公司的派送范围', '4', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '拒收', '拒收', '买家拒绝收货', '4', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '无流转', '无流转', '包裹没有物流信息', '4', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '确认', '确认', '退货过程中，需要进行确认的问题', '5', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '退货', '退货', '遇到与退货有关的问题', '5', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '关怀', '关怀', '退货时，需要处理对买家进行关怀的问题', '5', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '产品', '产品', '评价中涉及到产品的问题', '6', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '包装', '包装', '评价中涉及到物品包装的问题', '6', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '物流', '物流', '评价中涉及到物流的问题', '6', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;
INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) select '服务', '服务', '评价中涉及到服务的问题', '6', 'affairType', s.shop_id, now(), now(), '0' from plt_taobao_shop s;



--//@UNDO
-- SQL to undo the change goes here.
delete from  tb_tc_category where pkid > 6;