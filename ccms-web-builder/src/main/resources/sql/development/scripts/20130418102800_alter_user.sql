--// create  TWF_LOG_NODE_MIDS  table
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_sysuser  
CHANGE usertype user_type VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'build-in' NOT NULL COMMENT '用户类型', 
CHANGE username login_name VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户登录名称',
CHANGE name real_name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL  COMMENT '用户真实姓名';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_sysuser  
CHANGE user_type usertype VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'build-in' NOT NULL COMMENT '用户类型', 
CHANGE login_name username VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户登录名称',
CHANGE real_name name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL  COMMENT '用户真实姓名';
