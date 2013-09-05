--// CCMS-4142-add_subuser_and_loginuser_relation
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_login_subuser_relation (
id  int NOT NULL,
login_name  varchar(20) NOT NULL,
taobao_subuser  varchar(100) NULL,
created  timestamp NULL,
updated  timestamp NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id)
)
COMMENT='用户与旺旺子账号关联表'
;

CREATE TABLE tb_tc_login_subuser_relation_log (
id  int NOT NULL,
login_name  varchar(20) NOT NULL,
last_taobao_subuser  varchar(100) NULL,
next_taobao_subuser  varchar(100) NULL,
created  timestamp NULL,
PRIMARY KEY (id)
)
COMMENT='用户与旺旺子账号关联修改记录表'
;

ALTER TABLE tb_tc_login_subuser_relation
MODIFY COLUMN id  int(11) NOT NULL AUTO_INCREMENT FIRST ;

ALTER TABLE tb_tc_login_subuser_relation_log
MODIFY COLUMN id  int(11) NOT NULL AUTO_INCREMENT FIRST ;


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE tb_tc_login_subuser_relation;
DROP TABLE tb_tc_login_subuser_relation_log;

