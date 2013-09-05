--// update grade to number
-- Migration SQL that makes the change goes here.

UPDATE tm_db_column SET db_type='number' WHERE table_id=10 and db_name='grade';

--//@UNDO
-- SQL to undo the change goes here.

UPDATE tm_db_column SET db_type='string' WHERE table_id=10 and db_name='grade';
