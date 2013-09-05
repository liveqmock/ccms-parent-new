--// add version column
-- Migration SQL that makes the change goes here.
alter table tb_campaign add column version varchar(20) ;
alter table tb_program add column version varchar(20) ;
alter table tb_template add column version varchar(20) ;

--//@UNDO
-- SQL to undo the change goes here.
alter table tb_campaign drop column version ;
alter table tb_program drop column version ;
alter table tb_template drop column version ;

