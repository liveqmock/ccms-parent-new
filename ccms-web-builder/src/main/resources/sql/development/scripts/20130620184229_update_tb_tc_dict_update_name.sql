--// update tb_tc_dict update name
-- Migration SQL that makes the change goes here.
update tb_tc_dict set name='排除今天发过客户' where type=5 and code=1;

--//@UNDO
-- SQL to undo the change goes here.
update tb_tc_dict set name='选中排除今天发过的客户' where type=5 and code=1;

