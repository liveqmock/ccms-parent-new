--// alter table twf_node_evaluate_customer_detail  add column
-- Migration SQL that makes the change goes here.

DELETE FROM module WHERE id='12';
DELETE FROM module WHERE id='13';
DELETE FROM module WHERE id='14';


--//@UNDO
-- SQL to undo the change goes here.

insert into module(id,module_type_id,container_module_id,ranking)
values(12,12,10,200),(13,13,10,300),(14,14,10,400);