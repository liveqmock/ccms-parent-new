--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 把taobao_user的id改为自增的

ALTER TABLE tb_sys_taobao_user
CHANGE id id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键,使用tb_sysuser表的id';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_sys_taobao_user
CHANGE id id BIGINT(20) NOT NULL COMMENT '主键,使用tb_sysuser表的id';

