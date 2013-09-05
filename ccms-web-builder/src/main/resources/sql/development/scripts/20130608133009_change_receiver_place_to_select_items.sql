--// change receiver_place to select items
-- Migration SQL that makes the change goes here.

update tm_db_column set dic_id=22 where column_id=605;

--//@UNDO
-- SQL to undo the change goes here.

update tm_db_column set dic_id=null where column_id=605;
