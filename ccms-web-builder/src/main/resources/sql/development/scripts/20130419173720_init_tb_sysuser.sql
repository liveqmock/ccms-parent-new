--// create twf node retry
-- Migration SQL that makes the change goes here.

insert ignore into tb_sysuser(id,login_name,password,real_name,disabled)
values(1,'admin','e10adc3949ba59abbe56e057f20f883e','管理员',false);

--//@UNDO
-- SQL to undo the change goes here.

delete from tb_sysuser where id=1;