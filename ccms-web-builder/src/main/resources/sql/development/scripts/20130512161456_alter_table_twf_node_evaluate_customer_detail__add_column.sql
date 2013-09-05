--// alter table twf_node_evaluate_customer_detail  add column
-- Migration SQL that makes the change goes here.

alter table twf_node_evaluate_customer_detail add email varchar(50) after mobile;

--//@UNDO
-- SQL to undo the change goes here.

alter table twf_node_evaluate_customer_detail drop column email;
