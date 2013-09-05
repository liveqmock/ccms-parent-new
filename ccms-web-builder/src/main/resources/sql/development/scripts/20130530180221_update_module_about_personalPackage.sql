--// update module about personalPackage
-- Migration SQL that makes the change goes here.

update module set container_module_id=33 where id in(34,35,36,37);
update module set module_type_id=37,ranking=700 where id=37;

--//@UNDO
-- SQL to undo the change goes here.

-- bu yong le
