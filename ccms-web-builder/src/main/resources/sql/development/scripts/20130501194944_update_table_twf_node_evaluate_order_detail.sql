--// update table twf_node_evaluate_order_detail
-- Migration SQL that makes the change goes here.


alter table twf_node_evaluate_order_detail change oid tid varchar(100);

--//@UNDO
-- SQL to undo the change goes here.


alter table twf_node_evaluate_order_detail change tid oid varchar(100)
