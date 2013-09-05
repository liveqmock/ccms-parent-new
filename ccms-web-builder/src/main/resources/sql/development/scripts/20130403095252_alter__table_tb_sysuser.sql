--// alter  table tb_sysuser
-- Migration SQL that makes the change goes here.


alter table tb_sysuser modify disabled  TINYINT;

--//@UNDO
-- SQL to undo the change goes here.


alter table tb_sysuser modify disabled  varchar(100);