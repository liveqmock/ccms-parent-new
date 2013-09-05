--// create twf node retry
-- Migration SQL that makes the change goes here.

CREATE TABLE module_type(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(255) NOT NULL COMMENT '模块名',
	name_plus VARCHAR(50) COMMENT '名称附注(内部用)',
	tip VARCHAR(255) COMMENT '提示文案',
	lowest_edition_required INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '最低版本要求',
	support_ops_mask INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '支持的操作',
	memo VARCHAR(255) COMMENT '备注',
	
	PRIMARY KEY (id)
)COMMENT '模块类型';

CREATE TABLE module(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	module_type_id BIGINT UNSIGNED NOT NULL COMMENT '模块类型id',
	container_module_id BIGINT COMMENT '外围模块id',
	url VARCHAR(255) ,
	data_url VARCHAR(255) COMMENT '数据的请求地址',
	name VARCHAR(30) COMMENT '名字,可用于展示',
	tip VARCHAR(30) COMMENT '提示文案',
	lowest_edition_required INT UNSIGNED COMMENT '最低版本要求',
	support_ops_mask INT UNSIGNED COMMENT '支持的操作',
	memo VARCHAR(255) COMMENT '备注',
	
	PRIMARY KEY (id)
)COMMENT '模块';


--//@UNDO
-- SQL to undo the change goes here.

drop table module_type ;

drop table module ; 