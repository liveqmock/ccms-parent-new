--// CCMS-3254 alter  table  twf_node_evaluate_customer_detail  modify column
-- Migration SQL that makes the change goes here.

ALTER TABLE twf_node_evaluate_customer_detail  MODIFY COLUMN sex VARCHAR(10);

--//@UNDO
-- SQL to undo the change goes here.


ALTER TABLE twf_node_evaluate_customer_detail  MODIFY COLUMN sex char(1);