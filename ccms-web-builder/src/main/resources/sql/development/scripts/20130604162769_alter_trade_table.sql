--// alter_trade_table
-- Migration SQL that makes the change goes here.
alter table tb_tc_taobao_order add order_created datetime NOT NULL COMMENT '订单入库时间';
alter table tb_tc_taobao_order add order_updated datetime DEFAULT NULL COMMENT '订单修改时间';

alter table tb_tc_taobao_order_item add outer_iid datetime DEFAULT NULL COMMENT '商家外部编码';


alter table tb_tc_service_staff_interaction add is_hide tinyint(1) DEFAULT 0 COMMENT '订单隐藏状态，1是true为隐藏，0是false未隐藏';
ALTER TABLE tb_tc_service_staff_interaction CHANGE COLUMN deal_date order_created  datetime NOT NULL COMMENT '订单入库时间，用于增量统计';

alter table tb_tc_service_staff_interaction rename to tb_tc_customer_orders_ship;
alter table tb_tc_customer_orders_ship comment '客服订单关系表，由聊天，订单表而来';

--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_taobao_order drop column order_created;
alter table tb_tc_taobao_order drop column order_updated;

alter table tb_tc_taobao_order_item drop column outer_iid;


alter table tb_tc_customer_orders_ship rename to tb_tc_service_staff_interaction;
alter table tb_tc_service_staff_interaction comment '客服交互信息';

alter table tb_tc_service_staff_interaction drop column is_hide;
ALTER TABLE tb_tc_service_staff_interaction CHANGE COLUMN order_created deal_date  date NOT NULL COMMENT '处理日期';



