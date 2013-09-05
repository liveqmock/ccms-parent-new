--// update tb_tc_config_log modify field
-- Migration SQL that makes the change goes here.
alter table tb_tc_config_log modify content text DEFAULT NULL COMMENT '配置内容';

alter table tb_tc_config_log drop column updated;


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_config_log modify content varchar(800) DEFAULT NULL COMMENT '配置内容';

alter table tb_tc_config_log add updated datetime DEFAULT NULL COMMENT '修改时间';