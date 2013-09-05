--// create blacklist table
-- Migration SQL that makes the change goes here.

alter table twf_node_sms add column delivery_category varchar(20) ;


--//@UNDO
-- SQL to undo the change goes here.

alter table twf_node_sms drop column delivery_category ;