--// modify twf node sms
-- Migration SQL that makes the change goes here.

alter  table twf_node_sms modify column message_value text COMMENT '短信内容' ;

--//@UNDO
-- SQL to undo the change goes here.

alter  table twf_node_sms modify column message_value varchar(512) COMMENT '短信内容' ;
