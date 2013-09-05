--// add rule job and log tables
-- Migration SQL that makes the change goes here.

CREATE TABLE rc_job_buffer(
  fact_id varchar(50) NOT NULL ,
  fact_type varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'fact类型',
  fact_data text COLLATE utf8_bin NOT NULL COMMENT 'fact数据',
  submit_time timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '提交job时间',
  status varchar(20) COLLATE utf8_bin not NULL COMMENT '当前fact处理状态',
  last_modify_time datetime NULL DEFAULT NULL  COMMENT '最后更新时间',
  PRIMARY KEY(fact_id,fact_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '规则引擎处理等待处理任务表';


create table rc_job_log(
  fact_id varchar(50) NOT NULL ,
  fact_type varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'fact类型',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  hits int  not NULL COMMENT '匹配规则数量',
  submit_time datetime NULL DEFAULT NULL  COMMENT '提交job时间',
  start_time datetime NULL DEFAULT NULL  COMMENT '提交引擎执行fact开始时间',
  end_time datetime NULL DEFAULT NULL COMMENT '引擎执行fact返回时间',
  PRIMARY KEY(fact_id,fact_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '规则引擎处理日志结果表';


create table rc_job_detail_log(
  fact_id varchar(50) NOT NULL ,
  fact_type varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'fact类型',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  plan_id bigint(20) NOT NULL COMMENT '生效的方案id',
  rule_id bigint(20) NOT NULL COMMENT '生效的规则id',
  key(fact_id,fact_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '规则引擎处理日志详情表';


create table rc_job_taobao_memo(
	tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
	shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
	memo varchar(500) COLLATE utf8_bin  not NULL COMMENT '最终备注内容',
	status varchar(20)  COLLATE utf8_bin  not NULL COMMENT '备注任务状态',
	error_msg varchar(100)  COLLATE utf8_bin  NULL COMMENT '如果有错，注明错误原因',
	submit_time timestamp NULL DEFAULT NULL COMMENT '提交备注任务时间',
	start_time timestamp NULL DEFAULT NULL  COMMENT '开始处理备注任务时间',
	end_time timestamp NULL DEFAULT NULL  COMMENT '结束处理备注任务时间',
	PRIMARY KEY(tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'taobao备注任务表';


--//@UNDO
-- SQL to undo the change goes here.
drop table rc_job_buffer;
drop table rc_job_log;
drop table rc_job_detail_log;
drop table rc_job_taobao_memo;

