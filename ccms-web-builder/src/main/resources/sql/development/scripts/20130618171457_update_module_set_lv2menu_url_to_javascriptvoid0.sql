--// update module set lv2menu url to javascriptvoid0
-- Migration SQL that makes the change goes here.

update module_type set url='javascript:void(0);' where id in(19,27,28,33,44,48,50);

--//@UNDO
-- SQL to undo the change goes here.

update module_type set url='javascript:void(0);' where id in(19,27,28,33,44,48,50);
