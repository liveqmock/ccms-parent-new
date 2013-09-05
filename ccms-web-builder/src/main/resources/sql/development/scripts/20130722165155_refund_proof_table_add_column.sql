--// refund_proof_table_add_column
-- Migration SQL that makes the change goes here.
alter table tb_tc_refund_top_content add column dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID';
alter table tb_tc_refund_proof_file add column dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_refund_top_content drop column dp_id;
alter table tb_tc_refund_proof_file drop column dp_id;