--// create table plan
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_re_plan(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(50) NOT NULL COMMENT '方案名',
	position INT NOT NULL COMMENT '优先级顺序位置',
	active BOOL NOT NULL DEFAULT '0' COMMENT '是否已启动',
	plan_group_id VARCHAR(50) NOT NULL COMMENT '所属的方案组id',
	start_time DATETIME COMMENT '最后一次开启的时间',
	last_config_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次修改配置的时间',
	PRIMARY KEY (id)
);

--//@UNDO
-- SQL to undo the change goes here.

drop table tb_re_plan;
