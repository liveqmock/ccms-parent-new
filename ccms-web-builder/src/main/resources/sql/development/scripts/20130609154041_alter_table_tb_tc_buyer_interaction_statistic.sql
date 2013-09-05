--// alter_table_tb_tc_buyer_interaction_statistic
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_buyer_interaction_statistic
DROP COLUMN increment_statistic_start_time,
DROP COLUMN increment_statistic_end_time;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_buyer_interaction_statistic
ADD COLUMN increment_statistic_start_time  datetime NULL AFTER updated,
ADD COLUMN increment_statistic_end_time  datetime NULL AFTER increment_statistic_start_time;



