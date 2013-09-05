--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 把password改为可null的

ALTER TABLE tb_sysuser
CHANGE password password VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NULL  COMMENT '用户登录密码';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_sysuser
CHANGE password password VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL  COMMENT '用户登录密码';

