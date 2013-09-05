--// alter table twf_node_evaluate_customer_detail modified  column type
-- Migration SQL that makes the change goes here.

ALTER TABLE tb_sysuser
ADD COLUMN create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '用户创建时间' AFTER disabled;

update tb_sysuser set create_time=now();

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_sysuser DROP COLUMN create_time;
