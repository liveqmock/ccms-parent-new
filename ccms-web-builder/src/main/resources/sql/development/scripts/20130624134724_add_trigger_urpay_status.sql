--// add trigger urpay status
-- Migration SQL that makes the change goes here.

CREATE TRIGGER urpayStatusInsert AFTER INSERT ON plt_taobao_order_tc FOR EACH ROW
BEGIN
  set @tid=new.tid;
  insert into tb_tc_urpay_status(tid,created,updated,auto_urpay_status,auto_urpay_thread,close_urpay_status,close_urpay_thread,cheap_urpay_status,cheap_urpay_thread,manual_urpay_status)
  values(@tid,now(),now(),0,'',0,'',0,'',0);
END

--//@UNDO
-- SQL to undo the change goes here.

DROP TRIGGER IF EXISTS urpayStatusInsert;
