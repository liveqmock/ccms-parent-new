--// alter coupon tables
-- Migration SQL that makes the change goes here.
alter table plt_taobao_coupon drop column available;

alter table twf_node_coupon modify column preview_customers TINYTEXT COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户';


--//@UNDO
-- SQL to undo the change goes here.

alter table plt_taobao_coupon add column available tinyint(1) DEFAULT NULL  COMMENT '优惠券是否有效';

alter table twf_node_coupon modify column preview_customers varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户';
