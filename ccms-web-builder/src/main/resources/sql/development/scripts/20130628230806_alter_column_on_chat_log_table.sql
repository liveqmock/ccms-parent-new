--// alter column on chat log table
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_chat_log
CHANGE COLUMN time chat_time datetime NULL DEFAULT NULL comment '消息时间' AFTER length;

ALTER TABLE plt_taobao_chat_log CHANGE COLUMN type chat_type  tinyint(4) NOT NULL COMMENT '消息类型' AFTER direction;


--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE plt_taobao_chat_log
CHANGE COLUMN chat_time time datetime NULL DEFAULT NULL comment '消息时间' AFTER length;

ALTER TABLE plt_taobao_chat_log CHANGE COLUMN chat_type type  tinyint(4) NOT NULL COMMENT '消息类型' AFTER direction;
