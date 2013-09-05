--// update module_type and module insert data
-- Migration SQL that makes the change goes here.
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(4,'#ENDTIME#','endtime',1,15,'确认收获时间');

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (56,'confirmCare','确认收货关怀',NULL,'#/order/care?type=12',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES ( 56,NULL,'56','50',NULL,NULL,NULL,NULL,NULL,NULL,'1500',NULL);

INSERT ignore INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES (57,'sendLog','发送记录',NULL,'#/order/urpayLog',NULL,NULL,'0','17',NULL);
INSERT ignore INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES ( 57,NULL,'57','38',NULL,NULL,NULL,NULL,NULL,NULL,'1600',NULL);


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=4 and code='#ENDTIME#';

delete from module where id in (56,57);
delete from module_type where id in (56,57);