--// update tb_tc_care_config update state_date type to datetime
-- Migration SQL that makes the change goes here.
alter table tb_tc_care_config modify start_date datetime DEFAULT NULL COMMENT '订单范围开始时间';
alter table tb_tc_care_config modify end_date datetime DEFAULT NULL COMMENT '订单范围结束时间';


--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_care_config modify start_date date DEFAULT NULL COMMENT '订单范围开始时间';
alter table tb_tc_care_config modify end_date date DEFAULT NULL COMMENT '订单范围结束时间';

