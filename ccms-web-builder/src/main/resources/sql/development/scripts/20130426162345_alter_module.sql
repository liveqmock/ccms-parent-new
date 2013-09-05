--// create blacklist table
-- Migration SQL that makes the change goes here.
-- 给module表增加一个ranking字段用来排序

ALTER TABLE module
ADD COLUMN ranking FLOAT NULL COMMENT '顺序' AFTER support_ops_mask;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE module
DROP COLUMN ranking;