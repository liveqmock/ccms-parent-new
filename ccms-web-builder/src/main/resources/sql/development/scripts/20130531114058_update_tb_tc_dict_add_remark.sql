--// update tb tc dict add remark
-- Migration SQL that makes the change goes here.
alter table tb_tc_dict add remark varchar(200) COMMENT '备注';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_dict drop column remark;

