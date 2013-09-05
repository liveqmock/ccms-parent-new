--// alter plan planGroupId to shopId
-- Migration SQL that makes the change goes here.

ALTER TABLE rc_plan CHANGE plan_group_id shop_id VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '所属的方案组id';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE rc_plan CHANGE shop_id plan_group_id VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '所属的方案组id';
