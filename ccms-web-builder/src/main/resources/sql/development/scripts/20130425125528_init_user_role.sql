--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 初始化模块、模块准入规则数据

-- 先删除之前插入的测试数据
delete from tb_user_role;

insert into tb_user_role(user_id,role_id)
values(1,100000);

--//@UNDO
-- SQL to undo the change goes here.
delete from tb_user_role;