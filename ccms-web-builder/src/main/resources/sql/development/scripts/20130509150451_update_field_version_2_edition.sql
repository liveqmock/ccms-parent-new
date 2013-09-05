--// update field version 2 edition
-- Migration SQL that makes the change goes here.
update tb_template set version = 'BASIC_L3' ;
update tb_campaign set version = 'BASIC_L3' ;
update tb_program  set version = 'BASIC_L3' ;

alter table tb_template change version edition varchar(20) COMMENT '软件包版本' ;
alter table tb_campaign change version edition varchar(20) COMMENT '软件包版本' ;
alter table tb_program change  version edition varchar(20) COMMENT '软件包版本' ;

--//@UNDO
-- SQL to undo the change goes here.

alter table tb_template change edition version  varchar(20) COMMENT '软件包版本' ;
alter table tb_campaign change edition version  varchar(20) COMMENT '软件包版本' ;
alter table tb_program  change edition version  varchar(20) COMMENT '软件包版本' ;

update tb_template set version = 'L3' ;
update tb_campaign set version = 'L3' ;
update tb_program  set version = 'L3' ;
