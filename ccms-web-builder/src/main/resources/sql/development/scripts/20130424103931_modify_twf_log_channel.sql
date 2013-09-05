--// create blacklist table
-- Migration SQL that makes the change goes here.

alter table twf_log_channel  CHANGE  id  id  bigint(20)  NOT  NULL  AUTO_INCREMENT  COMMENT '非业务主键ID';


--//@UNDO
-- SQL to undo the change goes here.

alter table twf_log_channel  CHANGE  id  id  bigint(20)  NOT  NULL;
