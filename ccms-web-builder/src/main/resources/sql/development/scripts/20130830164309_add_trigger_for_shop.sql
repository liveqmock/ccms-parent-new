--// add_trigger_for_shop
-- Migration SQL that makes the change goes here.
CREATE TRIGGER shop_category_init AFTER INSERT ON plt_taobao_shop FOR EACH ROW
  BEGIN
    set @shopId=new.shop_id;

    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ('用户等级', '用户等级', '跟客户等级有关的问题，比如不同的等级，进行不同的订单处理', '2', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '催付', '催付', '订单需要进行催付处理，或者与催付有关的问题', '2', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '关怀', '关怀', '需要对用户进行关怀，或者与关怀有关的问题', '3', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '延迟', '延迟', '订单发货出现延迟问题', '3', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '订单备注', '订单备注', '订单需要根据备注，进行特殊处理', '3', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '疑难件', '疑难件', '包裹运送中，被判定为疑难件', '4', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '超区件', '超区件', '该物流超出了物流公司的派送范围', '4', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '拒收', '拒收', '买家拒绝收货', '4', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '无流转', '无流转', '包裹没有物流信息', '4', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '确认', '确认', '退货过程中，需要进行确认的问题', '5', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '退货', '退货', '遇到与退货有关的问题', '5', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '关怀', '关怀', '退货时，需要处理对买家进行关怀的问题', '5', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '产品', '产品', '评价中涉及到产品的问题', '6', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '包装', '包装', '评价中涉及到物品包装的问题', '6', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '物流', '物流', '评价中涉及到物流的问题', '6', 'affairType', @shopId, now(), now(), '0');
    INSERT INTO tb_tc_category (name, value, description, parent_id, out_type, out_id, created, updated, is_delete) values ( '服务', '服务', '评价中涉及到服务的问题', '6', 'affairType', @shopId, now(), now(), '0');
  END;


--//@UNDO
DROP TRIGGER IF EXISTS shop_category_init;


