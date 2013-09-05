--// change data of refer config
-- Migration SQL that makes the change goes here.

update tm_refer set refer_key = 'status_id' where refer_id = 1;
update tm_refer set refer_name = 'status_value' where refer_id = 1;

--//@UNDO
-- SQL to undo the change goes here.

update tm_refer set refer_key = 'status_value' where refer_id = 1;
update tm_refer set refer_name = 'status_name' where refer_id = 1;
