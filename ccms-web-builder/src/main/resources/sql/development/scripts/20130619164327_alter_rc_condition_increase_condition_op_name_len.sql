--// alter rc_condition increase condition_op_name len
-- Migration SQL that makes the change goes here.

ALTER TABLE rc_condition
CHANGE condition_op_name condition_op_name VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '指标比较符';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE rc_condition
CHANGE condition_op_name condition_op_name VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '指标比较符';

