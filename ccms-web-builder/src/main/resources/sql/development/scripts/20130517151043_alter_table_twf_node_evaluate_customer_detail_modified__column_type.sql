--// alter table twf_node_evaluate_customer_detail modified  column type
-- Migration SQL that makes the change goes here.

ALTER TABLE twf_node_evaluate_customer_detail  MODIFY COLUMN buyer_credit_lev VARCHAR(50);

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE twf_node_evaluate_customer_detail  MODIFY COLUMN buyer_credit_lev INTEGER;
