--// CCMS-3889 add_tb_tc_caring_detail_index
-- Migration SQL that makes the change goes here.

create  index idx_tb_tc_caring_detail_tid on tb_tc_caring_detail(tid);
create  index idx_tb_tc_caring_detail_oid on tb_tc_caring_detail(oid);

--//@UNDO
-- SQL to undo the change goes here.

drop index idx_tb_tc_caring_detail_tid on tb_tc_caring_detail;
drop index idx_tb_tc_caring_detail_oid on tb_tc_caring_detail;


