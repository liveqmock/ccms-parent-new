--// modify camp to campaign 
-- Migration SQL that makes the change goes here.

CREATE TABLE twf_node_sms (
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
name  varchar(20) COMMENT '名称' ,
unsubscribe_enabled  tinyint(1) COMMENT '可否订阅 0-否 1-可' ,
unsubscribe_message  varchar(64) COMMENT '退订消息' ,
blacklist_disabled  tinyint(1) COMMENT '屏蔽黑名单 0-否 1-是' ,
redlist_enabled  tinyint(1) COMMENT '发送红名单 0-不 1-是' ,
delivery_channel_id  int(4) COMMENT '发送通道ID' ,
test_phone_string  varchar(256) COMMENT '测试执行号码串' ,
phone_num_source  varchar(20) COMMENT '手机号码来源' ,
output_control  varchar(20) COMMENT '输出控制' ,
delivery_selection  varchar(20) COMMENT '发送方式选择' ,
assign_delivery_date  varchar(20) COMMENT '指定发送日期，字符各式 YYYY-MM-DD' ,
assign_delivery_time  varchar(20) COMMENT '指定发送时分，字符各式 HH:MM' ,
over_assign_delivery_period  tinyint(1) COMMENT '是否超时预设时间' ,
sampling_enabled  tinyint(1) COMMENT '可抽样 0-不可 1-可' ,
sampling_copies  int(4) COMMENT '抽样数' ,
remark  varchar(128) COMMENT '备注' ,
created datetime COMMENT '创建时间' ,
PRIMARY KEY (node_id)
)
COMMENT '短信节点' ;


CREATE TABLE tb_node_sms_message (
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
message_value  varchar(512) COMMENT '短信内容' ,
words_limit  int(4) ,
message_size  int(4) ,
message_signature  varchar(128) COMMENT '短信签名' ,
PRIMARY KEY (node_id)
)
COMMENT '短信内容记录表' ;


CREATE TABLE twf_node_sms_sampling_data (
id  bigint(20) NOT NULL AUTO_INCREMENT,
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
subjob_id  bigint(20) NOT NULL ,
uni_id  varchar(128) ,
content  varchar(128) ,
PRIMARY KEY (id)
)
COMMENT '短信节点生成抽样数据' ;


CREATE TABLE twf_node_sms_after_execution_data (
id  bigint(20) NOT NULL AUTO_INCREMENT,
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
subjob_id  bigint(20) NOT NULL ,
target_group_customers  bigint(20) ,
control_group_customers  bigint(20) ,
valid_phone_amount  bigint(20) ,
invalid_phone_amount bigint(20) ,
PRIMARY KEY (id)
)
COMMENT '短信节点执行后生成数据' ;



--//@UNDO
-- SQL to undo the change goes here.

drop table twf_node_sms;
drop table twf_node_sms_message;
drop table twf_node_sms_sampling_data;
drop table twf_node_sms_after_execution_data;
