--// CCMS-4142-add_subuser_and_loginuser_colums
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_login_subuser_relation ADD COLUMN dp_id  varchar(50) NULL AFTER login_name;
ALTER TABLE tb_tc_login_subuser_relation_log ADD COLUMN dp_id  varchar(50) NULL AFTER login_name;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE tb_tc_login_subuser_relation
DROP COLUMN dp_id, MODIFY COLUMN taobao_subuser  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL AFTER login_name;
ALTER TABLE tb_tc_login_subuser_relation_log
DROP COLUMN dp_id, MODIFY COLUMN last_taobao_subuser  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL AFTER login_name;



