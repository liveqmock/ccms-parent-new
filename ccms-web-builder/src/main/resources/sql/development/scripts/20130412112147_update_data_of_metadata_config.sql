--// update data of metadata config
-- Migration SQL that makes the change goes here.

update tm_query_criteria set query_type = 'CUSTOMER_LABEL' where query_criteria_id = 24;

--//@UNDO
-- SQL to undo the change goes here.

update tm_query_criteria set query_type = 'STRING' where query_criteria_id = 24;
