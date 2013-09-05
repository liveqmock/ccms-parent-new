--// add pic url
-- Migration SQL that makes the change goes here.
alter table tb_campaign add column pic_url varchar(255) ;
alter table tb_template add column pic_url varchar(255) ;

--//@UNDO
-- SQL to undo the change goes here.
alter table tb_campaign drop column pic_url ;
alter table tb_template drop column pic_url ;

