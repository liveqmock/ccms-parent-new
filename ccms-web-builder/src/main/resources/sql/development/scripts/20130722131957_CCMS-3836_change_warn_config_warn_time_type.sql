--// CCMS-3836_change_warn_config_warn_time_type
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_warn_config
MODIFY COLUMN warn_start_time  time NULL DEFAULT NULL COMMENT '告警开始时间' AFTER warn_type,
MODIFY COLUMN warn_end_time  time NULL DEFAULT NULL COMMENT '告警结束时间' AFTER warn_start_time;


--//@UNDO
-- SQL to undo the change goes here.


