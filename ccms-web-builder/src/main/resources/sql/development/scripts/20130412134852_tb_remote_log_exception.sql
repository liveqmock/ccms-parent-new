--// tb remote log exception 
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_remote_log_exception (
exception_id  bigint(20) NOT NULL AUTO_INCREMENT,
user_id  bigint(20) NOT NULL COMMENT '操作用户ID' ,
function_name  varchar(256) COMMENT '触发函数名' ,
exception_desc  longtext DEFAULT NULL COMMENT '异常信息描述' ,
created  datetime COMMENT '生成时间' , 
PRIMARY KEY (exception_id)
)
COMMENT '访问异常日志表' ;


--//@UNDO
-- SQL to undo the change goes here.

drop table tb_remote_log_exception;