--// tb channel 
-- Migration SQL that makes the change goes here.

ALTER  TABLE  twf_node_sms_after_execution_data  RENAME  TO  twf_node_sms_execution_record;
ALTER  TABLE  twf_node_sms_sampling_data  RENAME  TO  twf_node_sms_sample_record;

ALTER  TABLE  twf_node_sms  ADD  COLUMN  message_value  varchar(512) COMMENT '短信内容' ;
ALTER  TABLE  twf_node_sms  ADD  COLUMN  words_limit  int(4) ;
ALTER  TABLE  twf_node_sms  ADD  COLUMN  message_size  int(4) ;
ALTER  TABLE  twf_node_sms  ADD  COLUMN  message_signature  varchar(128) COMMENT '短信签名' ;

DROP  TABLE  tb_node_sms_message ;

--//@UNDO
-- SQL to undo the change goes here.

ALTER  TABLE  twf_node_sms_execution_record  RENAME  TO  twf_node_sms_after_execution_data;
ALTER  TABLE  twf_node_sms_sample_record  RENAME  TO  twf_node_sms_sampling_data;

ALTER  TABLE  twf_node_sms  DROP  COLUMN  message_value ;
ALTER  TABLE  twf_node_sms  DROP  COLUMN  words_limit ;
ALTER  TABLE  twf_node_sms  DROP  COLUMN  message_size ;
ALTER  TABLE  twf_node_sms  DROP  COLUMN  message_signature ;

CREATE TABLE tb_node_sms_message (
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
message_value  varchar(512) COMMENT '短信内容' ,
words_limit  int(4) ,
message_size  int(4) ,
message_signature  varchar(128) COMMENT '短信签名' ,
PRIMARY KEY (node_id)
)
COMMENT '短信内容记录表' ;


