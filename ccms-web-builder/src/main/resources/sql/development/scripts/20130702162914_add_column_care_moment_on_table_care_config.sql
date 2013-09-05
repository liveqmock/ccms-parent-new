--// add column care_moment on table care_config
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_tc_care_config ADD COLUMN care_moment int(2) DEFAULT NULL COMMENT '关怀时间（仅下单关怀使用) 0:下单后关怀 1:付款后关怀' AFTER date_number;

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_care_config DROP COLUMN care_moment;

