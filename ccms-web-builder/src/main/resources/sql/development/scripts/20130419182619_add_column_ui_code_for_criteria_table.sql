--// add column ui_code for criteria  table
-- Migration SQL that makes the change goes here.

alter table twf_node_query_criteria add ui_code varchar(32) default null comment 'UI标识';

--//@UNDO
-- SQL to undo the change goes here.

alter table twf_node_query_criteria drop ui_code;
