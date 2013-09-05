--// twf log channel 
-- Migration SQL that makes the change goes here.

CREATE TABLE twf_log_channel (
	id  bigint(20) NOT NULL ,
	subjob_id  bigint(20) NOT NULL COMMENT '节点任务ID' ,
	channel_id  int(11) NOT NULL COMMENT '渠道ID' ,
	create_time  datetime DEFAULT NULL COMMENT '创建时间' , 
	plan_start_time  datetime DEFAULT NULL COMMENT '计划开始时间' ,
	plan_end_time  datetime DEFAULT NULL COMMENT '计划结束时间' ,
	campaign_id  bigint(20) NOT NULL COMMENT '活动ID' ,
	node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
	channel_type  int(11) DEFAULT NULL COMMENT '渠道类型，比如SMS MMS' ,
	is_test_execute  tinyint(1) DEFAULT NULL ,
	task_id  varchar(255) DEFAULT NULL ,
	PRIMARY KEY (id)
)
COMMENT '渠道执行日志表' ;

--//@UNDO
-- SQL to undo the change goes here.

drop  table twf_log_channel ;
