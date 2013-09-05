--// alter_table_plt_taobao_chat_log
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_chat_log
CHANGE COLUMN url_list url_lists  varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息中商品url列表 ' AFTER content;




--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_chat_log
CHANGE COLUMN url_lists url_list  varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息中商品url列表 ' AFTER content;



