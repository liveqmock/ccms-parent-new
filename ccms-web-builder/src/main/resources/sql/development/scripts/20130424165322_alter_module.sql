--// create blacklist table
-- Migration SQL that makes the change goes here.
-- 给module表加字段key_name,作为模块的key(key为关键字,故字段名加了个后缀name).原name字段作为模块的汉语名字.更好的实现应该是本地化而非在数据库配置不同语言的名字

ALTER TABLE module_type
ADD COLUMN key_name VARCHAR(255) NOT NULL COMMENT '模块名(英文)' AFTER id,
CHANGE name name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '模块名(中文)';

ALTER TABLE module
ADD COLUMN key_name VARCHAR(255) NULL COMMENT '模块名(英文)' AFTER id,
CHANGE name name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '模块名(中文)';

update module_type set key_name='index' where id=1;
update module_type set key_name='nav' where id=2;
update module_type set key_name='indexLink' where id=3;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE module_type DROP COLUMN key_name;
ALTER TABLE module DROP COLUMN key_name;
