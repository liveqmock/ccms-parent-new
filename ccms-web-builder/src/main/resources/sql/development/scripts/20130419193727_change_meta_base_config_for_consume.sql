--// change meta base config for consume
-- Migration SQL that makes the change goes here.

delete from tm_query_table where query_table_id = 4;
delete from tm_query_join_criteria where query_join_id = 1;
update tm_query_criteria set query_type = 'TDS_PRODUCT' where query_criteria_id = 161;

--//@UNDO
-- SQL to undo the change goes here.

update tm_query_criteria set query_type = 'STRING' where query_criteria_id = 161;
INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(4, 3, 4, 0);
INSERT INTO tm_query_join_criteria(query_join_id, query_id, left_column_id, right_column_id, join_type) values(1, 3, 161, 241, 'INNER');
