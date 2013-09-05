--// drop_index_for_order_tc
-- Migration SQL that makes the change goes here.
drop index idx_plt_taobao_order_tc_dp_id ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_pay_time ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_created ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_status ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_modified ON plt_taobao_order_tc;
drop index idx_plt_taobao_order_tc_order_trade_from ON plt_taobao_order_tc;


--//@UNDO
-- SQL to undo the change goes here.
create index idx_plt_taobao_order_tc_dp_id ON plt_taobao_order_tc(dp_id) using btree;
create index idx_plt_taobao_order_tc_pay_time ON plt_taobao_order_tc(pay_time) using btree;
create index idx_plt_taobao_order_tc_order_created ON plt_taobao_order_tc(created) using btree;
create index idx_plt_taobao_order_tc_order_status ON plt_taobao_order_tc(order_status) using btree;
create index idx_plt_taobao_order_tc_order_modified ON plt_taobao_order_tc(modified) using btree;
create index idx_plt_taobao_order_tc_order_trade_from ON plt_taobao_order_tc(trade_from) using btree;

