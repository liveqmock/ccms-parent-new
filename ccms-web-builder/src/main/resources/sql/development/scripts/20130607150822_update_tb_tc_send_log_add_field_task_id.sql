--// update tb_tc_send_log add field task_id
-- Migration SQL that makes the change goes here.
alter table tb_tc_send_log add task_id varchar(100) DEFAULT NULL COMMENT '任务id,格式：应用ID(0) + "-" + 平台ID(taobao) + "-" +userName+"-"+uuid';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_send_log drop column task_id;

