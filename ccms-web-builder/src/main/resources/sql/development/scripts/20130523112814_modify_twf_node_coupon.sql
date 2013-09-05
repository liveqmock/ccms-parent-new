--// modify twf_node_coupon
-- Migration SQL that makes the change goes here.
alter table twf_node_coupon modify column preview_customers TEXT COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户';


--//@UNDO
-- SQL to undo the change goes here.
alter table twf_node_coupon modify column preview_customers TINYTEXT COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户';

