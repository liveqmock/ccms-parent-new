--// modify_refund_top_content_column_len
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_refund_top_content
MODIFY COLUMN content  varchar(400) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '常用话术内容' AFTER pkid;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_refund_top_content
MODIFY COLUMN content  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '常用话术内容' AFTER pkid;

