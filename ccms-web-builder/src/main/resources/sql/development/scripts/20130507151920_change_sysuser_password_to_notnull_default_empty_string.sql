--// change username to tenantid
-- Migration SQL that makes the change goes here.

update tb_sysuser set password='' where password is null;

ALTER TABLE tb_sysuser
CHANGE password password VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户登录密码';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_sysuser
CHANGE password password VARCHAR(100) NULL  COMMENT '用户登录密码';