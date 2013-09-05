--// update plt_taobao_order_tc add index created
-- Migration SQL that makes the change goes here.
create index idx_plt_taobao_order_tc_order_created ON plt_taobao_order_tc(created) using btree;

drop index idx_plt_taobao_order_tc_tid ON plt_taobao_order_tc;
drop index idx_plt_taobao_transitstepinfo_tid ON plt_taobao_transitstepinfo;
drop index idx_plt_taobao_transitstepinfo_tc_tid ON plt_taobao_transitstepinfo_tc;
drop index idx_tb_tc_urpay_status_tid ON tb_tc_urpay_status;
drop index idx_tb_tc_care_status_tid ON tb_tc_care_status;

--//@UNDO
-- SQL to undo the change goes here.
drop index idx_plt_taobao_order_tc_order_created ON plt_taobao_order_tc;

create index idx_plt_taobao_order_tc_tid ON plt_taobao_order_tc(tid) using btree;
create index idx_plt_taobao_transitstepinfo_tid ON plt_taobao_transitstepinfo(tid) using btree;
create index idx_plt_taobao_transitstepinfo_tc_tid ON plt_taobao_transitstepinfo_tc(tid) using btree;
create index idx_tb_tc_urpay_status_tid ON tb_tc_urpay_status(tid) using btree;
create index idx_tb_tc_care_status_tid ON tb_tc_care_status(tid) using btree;

