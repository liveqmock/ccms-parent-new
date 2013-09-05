--// create table rule
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_re_rule(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(50) NOT NULL DEFAULT '' ,
	position INT NOT NULL COMMENT '优先级顺序',
	plan_id BIGINT UNSIGNED NOT NULL COMMENT '所属的方案的id',
	remark_content VARCHAR(255) NOT NULL DEFAULT '' COMMENT '要在备注上添加的内容',
	PRIMARY KEY (id)
);

--//@UNDO
-- SQL to undo the change goes here.

drop table tb_re_rule;
