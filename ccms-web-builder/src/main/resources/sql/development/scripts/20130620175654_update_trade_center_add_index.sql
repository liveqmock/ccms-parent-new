--// update trade center add index
-- Migration SQL that makes the change goes here.
-- 订单(plt_taobao_order_tc)
drop index idx_tb_tc_taobao_order_dp_id ON plt_taobao_order_tc;
drop index idx_tb_tc_taobao_order_customerno ON plt_taobao_order_tc;
drop index idx_tb_tc_taobao_order_pay_time ON plt_taobao_order_tc;
drop index idx_tb_tc_taobao_order_consign_time ON plt_taobao_order_tc;
drop index idx_tb_tc_taobao_order_created ON plt_taobao_order_tc;

create index idx_plt_taobao_order_tc_tid ON plt_taobao_order_tc(tid) using btree;
create index idx_plt_taobao_order_tc_dp_id ON plt_taobao_order_tc(dp_id) using btree;
create index idx_plt_taobao_order_tc_pay_time ON plt_taobao_order_tc(pay_time) using btree;
create index idx_plt_taobao_order_tc_consign_time ON plt_taobao_order_tc(consign_time) using btree;
create index idx_plt_taobao_order_tc_order_status ON plt_taobao_order_tc(order_status) using btree;
create index idx_plt_taobao_order_tc_order_modified ON plt_taobao_order_tc(modified) using btree;
create index idx_plt_taobao_order_tc_order_trade_from ON plt_taobao_order_tc(trade_from) using btree;

-- 子订单(plt_taobao_order_item_tc)
drop index idx_tb_tc_taobao_order_item_tid ON plt_taobao_order_item_tc;
drop index idx_tb_tc_taobao_order_item_dp_id ON plt_taobao_order_item_tc;
drop index idx_tb_tc_taobao_order_item_num_iid ON plt_taobao_order_item_tc;

create index idx_plt_taobao_order_item_tc_tid ON plt_taobao_order_item_tc(tid) using btree;
create index idx_plt_taobao_order_item_tc_dp_id ON plt_taobao_order_item_tc(dp_id) using btree;

-- 物流流转表(plt_taobao_transitstepinfo)
drop index idx_tb_tc_taobao_shipping_tid ON plt_taobao_transitstepinfo;
create index idx_plt_taobao_transitstepinfo_tid ON plt_taobao_transitstepinfo(tid) using btree;

-- 物流业务表(plt_taobao_transitstepinfo_tc)
drop index idx_tb_tc_taobao_shipping_tc_tid ON plt_taobao_transitstepinfo_tc;

create index idx_plt_taobao_transitstepinfo_tc_tid ON plt_taobao_transitstepinfo_tc(tid) using btree;
create index idx_plt_taobao_transitstepinfo_tc_status ON plt_taobao_transitstepinfo_tc(status) using btree;
create index idx_plt_taobao_transitstepinfo_tc_shipping_status ON plt_taobao_transitstepinfo_tc(shipping_status) using btree;

-- 发送历史表(tb_tc_send_log)
create index idx_tb_tc_send_log_dp_id ON tb_tc_send_log(dp_id) using btree;
create index idx_tb_tc_send_log_type ON tb_tc_send_log(type) using btree;
create index idx_tb_tc_send_log_trade_created ON tb_tc_send_log(trade_created) using btree;
create index idx_tb_tc_send_log_mobile ON tb_tc_send_log(mobile) using btree;
create index idx_tb_tc_send_log_buyer_nick ON tb_tc_send_log(buyer_nick) using btree;
create index idx_tb_tc_send_log_send_status ON tb_tc_send_log(send_status) using btree;

-- 催付状态(tb_tc_urpay_status)
create index idx_tb_tc_urpay_status_tid ON tb_tc_urpay_status(tid) using btree;
create index idx_tb_tc_urpay_status_auto_urpay_status ON tb_tc_urpay_status(auto_urpay_status) using btree;
create index idx_tb_tc_urpay_status_close_urpay_status ON tb_tc_urpay_status(close_urpay_status) using btree;
create index idx_tb_tc_urpay_status_cheap_urpay_status ON tb_tc_urpay_status(cheap_urpay_status) using btree;
create index idx_tb_tc_urpay_status_manual_urpay_status ON tb_tc_urpay_status(manual_urpay_status) using btree;

-- 关怀状态表(tb_tc_care_status)
create index idx_tb_tc_care_status_tid ON tb_tc_care_status(tid) using btree;
create index idx_tb_tc_care_status_order_care_status ON tb_tc_care_status(order_care_status) using btree;
create index idx_tb_tc_care_status_shipment_care_status ON tb_tc_care_status(shipment_care_status) using btree;
create index idx_tb_tc_care_status_arrive_care_status ON tb_tc_care_status(arrive_care_status) using btree;
create index idx_tb_tc_care_status_delivery_care_status ON tb_tc_care_status(delivery_care_status) using btree;
create index idx_tb_tc_care_status_sign_care_status ON tb_tc_care_status(sign_care_status) using btree;
create index idx_tb_tc_care_status_refund_care_status ON tb_tc_care_status(refund_care_status) using btree;
create index idx_tb_tc_care_status_confirm_care_status ON tb_tc_care_status(confirm_care_status) using btree;
create index idx_tb_tc_care_status_assess_care_status ON tb_tc_care_status(assess_care_status) using btree;

-- 待发送队列表(tb_tc_sms_queue)
create index idx_tb_tc_sms_queue_dp_id ON tb_tc_sms_queue(dp_id) using btree;
create index idx_tb_tc_sms_queue_buyer_nick ON tb_tc_sms_queue(buyer_nick) using btree;
create index idx_tb_tc_sms_queue_type ON tb_tc_sms_queue(type) using btree;
create index idx_tb_tc_sms_queue_mobile ON tb_tc_sms_queue(mobile) using btree;
create index idx_tb_tc_sms_queue_send_time ON tb_tc_sms_queue(send_time) using btree;


--//@UNDO
-- SQL to undo the change goes here.
drop index idx_plt_taobao_order_tc_tid ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_dp_id ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_pay_time ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_consign_time ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_status ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_modified ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_trade_from ON plt_taobao_order_tc;

drop index idx_plt_taobao_order_item_tc_tid ON plt_taobao_order_item_tc;
drop index idx_plt_taobao_order_item_tc_dp_id ON plt_taobao_order_item_tc;

drop index idx_plt_taobao_transitstepinfo_tid ON plt_taobao_transitstepinfo;

drop index idx_plt_taobao_transitstepinfo_tc_tid ON plt_taobao_transitstepinfo_tc;
drop index idx_plt_taobao_transitstepinfo_tc_status ON plt_taobao_transitstepinfo_tc;
drop index idx_plt_taobao_transitstepinfo_tc_shipping_status ON plt_taobao_transitstepinfo_tc;

drop index idx_tb_tc_send_log_dp_id ON tb_tc_send_log;
drop index idx_tb_tc_send_log_type ON tb_tc_send_log;
drop index idx_tb_tc_send_log_trade_created ON tb_tc_send_log;
drop index idx_tb_tc_send_log_mobile ON tb_tc_send_log;
drop index idx_tb_tc_send_log_buyer_nick ON tb_tc_send_log;
drop index idx_tb_tc_send_log_send_status ON tb_tc_send_log;

drop index idx_tb_tc_urpay_status_tid ON tb_tc_urpay_status;
drop index idx_tb_tc_urpay_status_auto_urpay_status ON tb_tc_urpay_status;
drop index idx_tb_tc_urpay_status_close_urpay_status ON tb_tc_urpay_status;
drop index idx_tb_tc_urpay_status_cheap_urpay_status ON tb_tc_urpay_status;
drop index idx_tb_tc_urpay_status_manual_urpay_status ON tb_tc_urpay_status;


drop index idx_tb_tc_care_status_tid ON tb_tc_care_status;
drop index idx_tb_tc_care_status_order_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_shipment_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_arrive_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_delivery_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_sign_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_refund_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_confirm_care_status ON tb_tc_care_status;
drop index idx_tb_tc_care_status_assess_care_status ON tb_tc_care_status;

drop index idx_tb_tc_sms_queue_dp_id ON tb_tc_sms_queue;
drop index idx_tb_tc_sms_queue_buyer_nick ON tb_tc_sms_queue;
drop index idx_tb_tc_sms_queue_type ON tb_tc_sms_queue;
drop index idx_tb_tc_sms_queue_mobile ON tb_tc_sms_queue;
drop index idx_tb_tc_sms_queue_send_time ON tb_tc_sms_queue;

