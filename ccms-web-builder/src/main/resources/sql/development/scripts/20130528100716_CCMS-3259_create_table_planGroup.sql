--// CCMS-3259 create table planGroup
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_re_plan_group(
	shop_id VARCHAR(50) NOT NULL COMMENT '店铺id',
	sign VARCHAR(100) NOT NULL DEFAULT '' COMMENT '备注的签名',
	PRIMARY KEY (shop_id)
);

--//@UNDO
-- SQL to undo the change goes here.

drop table tb_re_plan_group;
