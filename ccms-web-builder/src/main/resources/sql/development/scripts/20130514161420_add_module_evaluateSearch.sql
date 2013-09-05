--// alter table twf_node_evaluate_customer_detail  add column
-- Migration SQL that makes the change goes here.

update module_type set url='#/evaluateSearch' where id=12;

insert into module(id,module_type_id,container_module_id,ranking)
values(12,12,10,200);

--//@UNDO
-- SQL to undo the change goes here.

update module_type set url='#/analysis/evaluateSearch' where id=12;

DELETE FROM module WHERE id='12';