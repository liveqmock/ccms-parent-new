--// modify rc drl changelog
-- Migration SQL that makes the change goes here.

alter table rc_drl_changelog add column id  bigint(20) NOT NULL AUTO_INCREMENT primary key ;

--//@UNDO
-- SQL to undo the change goes here.

alter table rc_drl_changelog drop column id ;
