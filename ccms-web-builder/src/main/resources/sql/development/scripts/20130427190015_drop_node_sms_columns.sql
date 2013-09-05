--// create blacklist table
-- Migration SQL that makes the change goes here.

alter table twf_node_sms drop column unsubscribe_message ;
alter table twf_node_sms drop column words_limit ;
alter table twf_node_sms drop column message_size ;
alter table twf_node_sms drop column message_signature ;


--//@UNDO
-- SQL to undo the change goes here.

alter table twf_node_sms add column unsubscribe_message varchar(64) COMMENT '退订消息' ;
alter table twf_node_sms add column words_limit int(4) ;
alter table twf_node_sms add column message_size int(4) ;
alter table twf_node_sms add column message_signature varchar(128) COMMENT '短信签名' ;
