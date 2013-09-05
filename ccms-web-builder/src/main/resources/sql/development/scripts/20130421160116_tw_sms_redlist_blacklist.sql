--// init tb app properties
-- Migration SQL that makes the change goes here.

CREATE TABLE tw_sms_blacklist (
mobile  varchar(20) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源' ,
created  datetime NULL COMMENT '录入时间' ,
PRIMARY KEY (mobile)
)
COMMENT = '短信手机黑名单表';

CREATE TABLE tw_sms_redlist (
mobile  varchar(20) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源' ,
created  datetime NULL COMMENT '录入时间' ,
PRIMARY KEY (mobile)
)
COMMENT = '手机短信红名单表';

--//@UNDO
-- SQL to undo the change goes here.

drop table tw_sms_blacklist;
drop table tw_sms_redlist;

