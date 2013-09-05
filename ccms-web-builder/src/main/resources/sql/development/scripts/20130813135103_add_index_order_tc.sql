--// add_index_order_tc
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_order_tc
DROP INDEX idx_plt_taobao_order_tc_consign_time,
ADD INDEX idx_consigntime_dpid_status (consign_time, dp_id, status) ;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_order_tc
DROP INDEX idx_consigntime_dpid_status,
ADD INDEX idx_plt_taobao_order_tc_consign_time (consign_time) ;


