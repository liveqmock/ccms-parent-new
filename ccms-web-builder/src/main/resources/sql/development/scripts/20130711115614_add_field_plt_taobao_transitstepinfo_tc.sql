--// add_field_plt_taobao_transitstepinfo_tc
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_transitstepinfo_tc
ADD COLUMN recently_time  datetime NULL COMMENT '最近物流更新时间' AFTER delivery_time,
ADD COLUMN abnormal_status  varchar(20) NULL COMMENT '异常状态信息' AFTER recently_time;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_transitstepinfo_tc
DROP COLUMN recently_time,
DROP COLUMN abnormal_status;

