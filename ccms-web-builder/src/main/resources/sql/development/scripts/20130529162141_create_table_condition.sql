--// create table condition
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_re_condition(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(50) NOT NULL ,
	rule_id BIGINT NOT NULL COMMENT '所属的规则id',
	position INT NOT NULL COMMENT '在规则的条件列表中的顺序',
	relation VARCHAR(10) NOT NULL COMMENT '与其他条件之间的关系',
	type VARCHAR(20) NOT NULL COMMENT '基于客户,基于订单',
	property_id BIGINT NOT NULL COMMENT '指标id,是tm_db_column表的column_id',
	condition_op_name VARCHAR(10) NOT NULL COMMENT '指标比较符',
	reference_value VARCHAR(1000) NOT NULL COMMENT '参考值',
	PRIMARY KEY (id)
);

--//@UNDO
-- SQL to undo the change goes here.

drop table tb_re_condition;
