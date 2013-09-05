--// modify table evaluate_report_result add constrain  not null
-- Migration SQL that makes the change goes here.

delete from tb_user_role;

insert into tb_user_role(user_id,role_id)
select u.id as user_id,r.id as role_id from tb_sysuser u,tb_role r order by u.id,r.id limit 1;

--//@UNDO
-- SQL to undo the change goes here.

delete from tb_user_role;

