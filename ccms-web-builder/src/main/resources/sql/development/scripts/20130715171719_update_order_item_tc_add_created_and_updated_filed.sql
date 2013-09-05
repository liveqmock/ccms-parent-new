--// update order item tc add created and updated filed
-- Migration SQL that makes the change goes here.
alter table plt_taobao_order_item_tc add item_order_created datetime DEFAULT NULL COMMENT '子订单数据入库时间';
alter table plt_taobao_order_item_tc add item_order_updated datetime DEFAULT NULL COMMENT '子订单数据修改时间';



--//@UNDO
-- SQL to undo the change goes here.
alter table plt_taobao_order_item_tc drop column item_order_created;
alter table plt_taobao_order_item_tc drop column item_order_updated;

