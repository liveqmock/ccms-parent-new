--// add index idx_status_dpid
-- Migration SQL that makes the change goes here.

create index idx_status_dpid on plt_taobao_order_tc(status,dp_id);


--//@UNDO
-- SQL to undo the change goes here.

drop index idx_status_dpid on plt_taobao_order_tc;
