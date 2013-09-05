--// alter Condition add column useDefaultName
-- Migration SQL that makes the change goes here.

ALTER TABLE rc_condition
ADD COLUMN use_default_name BOOL DEFAULT '1' NOT NULL COMMENT '是否使用默认名称' AFTER last_config_time;

--//@UNDO
-- SQL to undo the change goes here.

alter table rc_condition drop column use_default_name;
