--// add ruleCenter mock data
-- Migration SQL that makes the change goes here.

insert  ignore into rc_plan_group(shop_id,sign) values ('100571094','【数云】');

insert ignore into rc_plan(id,name,position,active,plan_group_id,start_time,last_config_time)
values (6,'这是一个方案2013-05-31 11:41:54',1,0,'100571094',NULL,'2013-05-31 11:41:54');

insert ignore into rc_rule(id,name,position,plan_id,remark_content,last_config_time) values
(8,'这是一个规则2013-05-31 11:41:54',1,6,'送小样儿A','2013-05-31 11:41:54'),
(9,'这是一个规则2013-05-31 11:41:54',2,6,'发顺丰','2013-05-31 11:41:54')
;

insert ignore into rc_condition(id,name,rule_id,position,relation,type,property_id,condition_op_name,reference_value,last_config_time)
values (11,'这是一个条件2013-05-31 11:41:54',8,1,'AND','CUSTOMER_BASED',1,'GE','200695759','2013-05-31 11:41:54'),
(12,'这是一个条件2013-05-31 11:41:54',8,2,'AND','CUSTOMER_BASED',18,'EQ','1013','2013-05-31 11:41:54'),
(13,'这是一个条件2013-05-31 11:41:54',8,3,'AND','ORDER_BASED',54,'LE','2013-10-01 00:00:00','2013-05-31 11:41:54'),
(14,'这是一个条件2013-05-31 11:41:54',9,1,'AND','CUSTOMER_BASED',1,'GE','200695759','2013-05-31 11:41:54')
;

--//@UNDO
-- SQL to undo the change goes here.

delete from rc_condition;
delete from rc_rule;
delete from rc_plan;
delete from rc_plan_group;
