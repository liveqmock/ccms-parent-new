--// modify trigger add data orders_ship table
-- Migration SQL that makes the change goes here.

DROP TRIGGER IF EXISTS urpayStatusInsert;
CREATE TRIGGER urpayStatusInsert AFTER INSERT ON plt_taobao_order_tc FOR EACH ROW
BEGIN
  set @tid=new.tid;
  set @dpId=new.dp_id;
  set @created = new.created;
  insert ignore into tb_tc_urpay_status(tid,created,updated,auto_urpay_status,auto_urpay_thread,close_urpay_status,close_urpay_thread,cheap_urpay_status,cheap_urpay_thread,manual_urpay_status)
  values(@tid,now(),now(),0,'',0,'',0,'',0);
  insert ignore into  tb_tc_customer_orders_ship(dp_id,tid,order_created,created,updated) values(@dpId,@tid,@created,now(),now());
END;

insert ignore into tb_tc_customer_orders_ship(dp_id,tid,order_created,created,updated)  select dp_id,tid,created,now(),now() from plt_taobao_order_tc;

--//@UNDO
-- SQL to undo the change goes here.

DROP TRIGGER IF EXISTS urpayStatusInsert;
CREATE TRIGGER urpayStatusInsert AFTER INSERT ON plt_taobao_order_tc FOR EACH ROW
BEGIN
  set @tid=new.tid;
  insert ignore into tb_tc_urpay_status(tid,created,updated,auto_urpay_status,auto_urpay_thread,close_urpay_status,close_urpay_thread,cheap_urpay_status,cheap_urpay_thread,manual_urpay_status)
  values(@tid,now(),now(),0,'',0,'',0,'',0);
END;

