--// adjust meta config for refer
-- Migration SQL that makes the change goes here.

CREATE TABLE tds_order_status (
status_id  smallint(6) NOT NULL ,
status_value  varchar(40) ,
status_name  varchar(40) ,
orderid  smallint(6) ,
PRIMARY KEY (status_id)
)
COMMENT = 'CCMS订单状态维表';

#订单状态表
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (10, '已下单未付款','已下单未付款',1);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (20, '有效交易','有效交易', 2);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (21, '&nbsp;&nbsp;&nbsp;&nbsp;|---已付款未发货','已付款未发货', 3);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (22, '&nbsp;&nbsp;&nbsp;&nbsp;|---已发货待确认','已发货待确认', 4);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (23, '&nbsp;&nbsp;&nbsp;&nbsp;|---交易成功','交易成功', 5);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (30, '交易失败  ','交易失败', 6);

###--------------------------------------------------------------------------------------------------------------------
###   View: vm_taobao_order                                     
###   订单信息视图(基础版)
###--------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM = MERGE VIEW  vm_taobao_order AS
  SELECT
    plt_taobao_order.tid,
    plt_taobao_order.customerno,
    plt_taobao_order.receiver_name,
    plt_taobao_order.created,
    plt_taobao_order.pay_time,
    plt_taobao_order.consign_time,
    plt_taobao_shop.shop_name,
    plt_taobao_order.payment,
    plt_taobao_order.post_fee ,
    plt_taobao_order.receiver_mobile,
    plt_taobao_order.buyer_alipay_no,
    plt_taobao_order.buyer_email,
    plt_taobao_order.total_fee,
    tds_order_status.status_name as status,
    b.mobile as buyer_mobile,
    s.out_sid  as out_sid,
    s.company_name as company_name,
    s.created  as shipping_created
FROM plt_taobao_order
    join plt_taobao_shop on dp_id = shop_id left
    join tds_order_status on status_id = ccms_order_status
    join plt_taobao_customer b on plt_taobao_order.customerno = b.customerno
    left join plt_taobao_shipping s on plt_taobao_order.tid= s.tid;


--//@UNDO
-- SQL to undo the change goes here.

drop view vm_taobao_order;
drop table tds_order_status;
