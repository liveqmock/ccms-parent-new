--// update show name for propery query
-- Migration SQL that makes the change goes here.

update tm_db_column set show_name = '淘宝昵称' where column_id = 2;

--//@UNDO
-- SQL to undo the change goes here.

update tm_db_column set show_name = '客户ID' where column_id = 2;
