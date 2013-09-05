--// CCMS-3448 alter rc_order_buffer change shop_id null
-- Migration SQL that makes the change goes here.

alter table rc_order_buffer modify shop_id varchar(50) COLLATE utf8_bin NULL COMMENT '店铺ID';
alter table rc_order_buffer modify status varchar(50) COLLATE utf8_bin NULL COMMENT '订单状态';

--//@UNDO
-- SQL to undo the change goes here.

alter table rc_order_buffer modify shop_id varchar(50) COLLATE utf8_bin not NULL COMMENT '店铺ID';
alter table rc_order_buffer modify status varchar(50) COLLATE utf8_bin not NULL COMMENT '订单状态';

