--// modify rule center tables
-- Migration SQL that makes the change goes here.

alter table tb_re_rule add column last_config_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次修改配置的时间' ;
alter table tb_re_condition add column last_config_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次修改配置的时间' ;

rename table tb_re_plan_group to rc_plan_group ;
rename table tb_re_plan to rc_plan ;
rename table tb_re_rule to rc_rule ;
rename table tb_re_condition to rc_condition ;

--//@UNDO
-- SQL to undo the change goes here.

rename table rc_plan_group to tb_re_plan_group ;
rename table rc_plan to tb_re_plan ;
rename table rc_rule to tb_re_rule ;
rename table rc_condition to tb_re_condition ;

alter table tb_re_rule drop column last_config_time ;
alter table tb_re_condition drop column last_config_time ;

