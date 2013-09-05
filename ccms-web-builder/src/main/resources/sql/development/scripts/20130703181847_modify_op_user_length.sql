--// modify op_user length
-- Migration SQL that makes the change goes here.

alter table tb_tc_urpay_config modify op_user varchar(50) DEFAULT NULL COMMENT '操作人';
alter table tb_tc_care_config modify op_user varchar(50) DEFAULT NULL COMMENT '操作人';

--//@UNDO
-- SQL to undo the change goes here.

alter table tb_tc_urpay_config modify op_user varchar(10) DEFAULT NULL COMMENT '操作人';
alter table tb_tc_care_config modify op_user varchar(10) DEFAULT NULL COMMENT '操作人';

