--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 创建module_entry表

CREATE TABLE module_entry(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	module_id BIGINT COMMENT '模块id',
	permission_id BIGINT UNSIGNED NULL COMMENT '权限id',
	role_id BIGINT UNSIGNED NULL COMMENT '角色id',
	user_id BIGINT UNSIGNED NULL COMMENT '用户id',
	support_ops_mask INT UNSIGNED NOT NULL COMMENT '授权结果',
	memo VARCHAR(255) NULL COMMENT '备注',
	PRIMARY KEY (id)
);

--//@UNDO
-- SQL to undo the change goes here.

drop table module_entry;
