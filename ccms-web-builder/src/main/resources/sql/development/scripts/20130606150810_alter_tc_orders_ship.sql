--// alter_tc_orders_ship
-- Migration SQL that makes the change goes here.



ALTER TABLE tb_tc_customer_orders_ship
MODIFY COLUMN service_staff_id  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '客服昵称',
MODIFY COLUMN dp_id varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '店铺id',
MODIFY COLUMN order_created  datetime NULL COMMENT '订单入库时间，用于增量统计';

ALTER TABLE tb_tc_customer_orders_ship ADD UNIQUE INDEX idx_tb_tc_customer_orders_ship_tid(tid);

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_tc_customer_orders_ship
MODIFY COLUMN service_staff_id  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客服昵称',
MODIFY COLUMN dp_id varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '店铺id',
MODIFY COLUMN order_created  datetime NOT NULL COMMENT '订单入库时间，用于增量统计';

drop index idx_tb_tc_customer_orders_ship_tid on tb_tc_customer_orders_ship;
