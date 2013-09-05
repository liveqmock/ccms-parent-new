--// alter a plt_taobao_transitstepinfo_tc
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_transitstepinfo_tc MODIFY COLUMN created  timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间' AFTER delivery_time;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_transitstepinfo_tc MODIFY COLUMN created  datetime NULL DEFAULT NULL COMMENT '数据创建时间' AFTER delivery_time;

