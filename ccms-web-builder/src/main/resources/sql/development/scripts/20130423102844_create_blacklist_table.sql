--// create blacklist table
-- Migration SQL that makes the change goes here.

ALTER TABLE tw_sms_blacklist rename to tw_mobile_blacklist;

CREATE TABLE tw_email_blacklist (
email  varchar(100) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源',
created  datetime NULL COMMENT '录入时间',
PRIMARY KEY (email)
)
COMMENT = 'EDM黑名单表';

-- 会员黑名单表
CREATE TABLE tw_member_blacklist (
  customerno varchar(50)  NOT NULL ,
  get_from varchar(20)  NULL COMMENT '名单来源',
  created datetime  NULL COMMENT '录入时间',
  PRIMARY KEY (customerno)
)
COMMENT='会员黑名单表';

CREATE TABLE tw_email_redlist (
email  varchar(100) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源',
created  datetime NULL COMMENT '录入时间',
PRIMARY KEY (email)
)
COMMENT = 'EDM红名单表';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tw_mobile_blacklist rename to tw_sms_blacklist;
drop table if exists tw_email_blacklist;
drop table if exists tw_member_blacklist;
drop table if exists tw_email_redlist;
