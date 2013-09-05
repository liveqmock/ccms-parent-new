--// create twf node retry
-- Migration SQL that makes the change goes here.

create table twf_node_retry (
	id  bigint(20)  NOT NULL AUTO_INCREMENT ,
	job_id  bigint(20) DEFAULT NULL ,
	node_id  bigint(20) DEFAULT NULL ,
	is_test_execute  tinyint(1) DEFAULT NULL ,
	failed_reason  varchar(200) DEFAULT NULL ,
	failed_code  varchar(200) DEFAULT NULL , 
	PRIMARY KEY (id)
)
COMMENT "节点重试表";

CREATE TABLE twf_log_channel_user (
uni_id  varchar(64) NOT NULL COMMENT '统一客户ID' ,
createtime  datetime COMMENT '创建时间' ,
subjob_id  bigint(20) ,
status  decimal(3,0) COMMENT '渠道执行状态（对应渠道反馈维表）' ,
serial_id  decimal(18,0) COMMENT '序列号' ,
node_id  bigint(20) ,
user_channel_info  varchar(100) COMMENT '用户在特定渠道上的联系信息，渠道为短信，记录手机号；渠道为EDM时，记录Email地址；' ,
KEY idx_twf_log_channel_user_uni_id (uni_id) ,
KEY idx_twf_log_channel_user_subjob_id (subjob_id)
)
COMMENT = '用户渠道沟通日志表';


--//@UNDO
-- SQL to undo the change goes here.

drop table twf_node_retry ;

drop table twf_log_channel_user ; 