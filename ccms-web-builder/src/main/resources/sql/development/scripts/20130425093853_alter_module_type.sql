--// create blacklist table
-- Migration SQL that makes the change goes here.
-- 给module表加字段key_name,作为模块的key(key为关键字,故字段名加了个后缀name).原name字段作为模块的汉语名字.更好的实现应该是本地化而非在数据库配置不同语言的名字

ALTER TABLE module_type
ADD COLUMN data_url VARCHAR(255) NULL COMMENT '请求数据的地址' AFTER name_plus,
ADD COLUMN url VARCHAR(255) NULL COMMENT '点击时跳转的地址' AFTER name_plus;

--//@UNDO
-- SQL to undo the change goes here.

alter table module_type drop data_url;
alter table module_type drop url;
