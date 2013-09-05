--// modify camp status default
-- Migration SQL that makes the change goes here.

alter table tb_campaign change camp_status camp_status varchar(15) DEFAULT 'A1' COMMENT '活动的状态,对应的字典表td_camp_status' ;


--//@UNDO
-- SQL to undo the change goes here.

alter table tb_campaign change camp_status camp_status varchar(15) COMMENT '活动的状态,对应的字典表td_camp_status' ;


