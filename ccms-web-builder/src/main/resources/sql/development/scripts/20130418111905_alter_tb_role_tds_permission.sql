--// create alter tb role tds permission  table
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_role
CHANGE role_id id BIGINT(20) NOT NULL AUTO_INCREMENT,
CHANGE role_name name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL  COMMENT '角色名';

ALTER TABLE tds_permission
CHANGE permission_id id BIGINT(20) NOT NULL,
CHANGE permission_name name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '权限名';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_role
CHANGE id role_id BIGINT(20) NOT NULL AUTO_INCREMENT,
CHANGE name role_name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL  COMMENT '角色名';

ALTER TABLE tds_permission
CHANGE id permission_id BIGINT(20) NOT NULL,
CHANGE name permission_name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '权限名';