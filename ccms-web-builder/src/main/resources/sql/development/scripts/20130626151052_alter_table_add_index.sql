--// alter_table_add_index
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_order_tc
ADD INDEX idx_created_dpid_status_customerno (created, dp_id, status, customerno) ;
ALTER TABLE plt_taobao_order_tc
ADD INDEX idx_customerno (customerno) ;
ALTER TABLE tb_tc_buyer_interaction_statistic
ADD INDEX idx_dpid_customerno (dp_id, customerno) ;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_order_tc
DROP INDEX idx_created_dpid_status_customerno;
ALTER TABLE plt_taobao_order_tc
DROP INDEX idx_customerno;
ALTER TABLE tb_tc_buyer_interaction_statistic
DROP INDEX idx_dpid_customerno;

