--// update tb_tc_dict add data
-- Migration SQL that makes the change goes here.
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(2,'99','未分级',0,99,'');

--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=2 and code='99'

