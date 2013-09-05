--// add_refund_status_index
-- Migration SQL that makes the change goes here.
create unique index idx_tb_tc_refund_status_oid on tb_tc_refund_status(oid);


--//@UNDO
-- SQL to undo the change goes here.
drop index idx_tb_tc_refund_status_oid on tb_tc_refund_status;

