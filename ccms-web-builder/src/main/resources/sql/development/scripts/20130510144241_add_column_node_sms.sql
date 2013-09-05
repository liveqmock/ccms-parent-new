--// add column node sms
-- Migration SQL that makes the change goes here.

drop  table  twf_node_sms_execution_record ;
CREATE TABLE twf_node_sms_execution_record (
id  bigint(20) NOT NULL AUTO_INCREMENT,
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
subjob_id  bigint(20) NOT NULL ,
target_group_customers  bigint(20) COMMENT '目标组用户数' ,
control_group_customers  bigint(20) COMMENT '控制组用户数' ,
valid_phone_amount  bigint(20) COMMENT '有效手机号码数' ,
invalid_phone_amount bigint(20) COMMENT '无效手机号码数' ,
PRIMARY KEY (id)
)
COMMENT '短信节点执行后生成数据' ;


alter  table  twf_node_sms_execution_record  add  column  sending_total_num  bigint(20) COMMENT '短信发送条数' ;
alter  table  twf_node_sms_execution_record  add  column  sending_price  decimal(15,2) DEFAULT NULL COMMENT '发送单价' ;
alter  table  twf_node_sms_execution_record  add  column  created_time  datetime DEFAULT NULL COMMENT '创建时间' ;

--//@UNDO
-- SQL to undo the change goes here.

alter  table  twf_node_sms_execution_record  drop  column  sending_total_num ;
alter  table  twf_node_sms_execution_record  drop  column  sending_price ;
alter  table  twf_node_sms_execution_record  drop  column  created_time ;

