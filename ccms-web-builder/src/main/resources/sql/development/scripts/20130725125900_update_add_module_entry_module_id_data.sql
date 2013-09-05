--// update add module_entry module_id data
-- Migration SQL that makes the change goes here.
delete from module_type where id in (41);
delete from module where id in (41);

insert into module_entry(module_id,permission_id,role_id,user_id,support_ops_mask) values(58,null,null,null,0);
insert into module_entry(module_id,permission_id,role_id,user_id,support_ops_mask) values(61,null,null,null,0);
insert into module_entry(module_id,permission_id,role_id,user_id,support_ops_mask) values(62,null,null,null,0);
insert into module_entry(module_id,permission_id,role_id,user_id,support_ops_mask) values(63,null,null,null,0);
insert into module_entry(module_id,permission_id,role_id,user_id,support_ops_mask) values(64,null,null,null,0);
insert into module_entry(module_id,permission_id,role_id,user_id,support_ops_mask) values(65,null,null,null,0);


--//@UNDO
-- SQL to undo the change goes here.
delete from module_entry where module_id in (58,61,62,63,64,65);

