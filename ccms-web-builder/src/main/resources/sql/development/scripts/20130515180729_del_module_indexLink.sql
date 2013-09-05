--// change properties for shop diagnosis
-- Migration SQL that makes the change goes here.

DELETE FROM module WHERE id='11';

--//@UNDO
-- SQL to undo the change goes here.

insert into module(id,module_type_id,container_module_id,ranking)
values(11,11,10,100);
