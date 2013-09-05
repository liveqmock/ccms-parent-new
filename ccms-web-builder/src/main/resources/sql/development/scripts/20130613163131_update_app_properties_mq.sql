--// update app properties mq
-- Migration SQL that makes the change goes here.

update tb_app_properties set prop_name = 'mq_ccms_username' where prop_group = 'MQ' and prop_name = 'mq_username' ;
update tb_app_properties set prop_name = 'mq_ccms_password' where prop_group = 'MQ' and prop_name = 'mq_password' ;
update tb_app_properties set prop_name = 'mq_ccms_virtualHost' where prop_group = 'MQ' and prop_name = 'mq_virtualHost' ;

insert into tb_app_properties (prop_group, prop_name, prop_value, prop_desc) values ('MQ', 'mq_etl_username', 'data-warehouse', 'rabbitMQ的etl用户名');
insert into tb_app_properties (prop_group, prop_name, prop_value, prop_desc) values ('MQ', 'mq_etl_password', '%data-warehouse', 'rabbitMQ的etl密码');
insert into tb_app_properties (prop_group, prop_name, prop_value, prop_desc) values ('MQ', 'mq_etl_virtualHost', 'datawarehouse', 'rabbitMQ的虚拟Host');


--//@UNDO
-- SQL to undo the change goes here.

update tb_app_properties set prop_name = 'mq_username' where prop_group = 'MQ' and prop_name = 'mq_ccms_username' ;
update tb_app_properties set prop_name = 'mq_password' where prop_group = 'MQ' and prop_name = 'mq_ccms_password' ;
update tb_app_properties set prop_name = 'mq_virtualHost' where prop_group = 'MQ' and prop_name = 'mq_ccms_virtualHost' ;

delete from tb_app_properties where prop_group = 'MQ' and prop_name = 'mq_etl_username' ;
delete from tb_app_properties where prop_group = 'MQ' and prop_name = 'mq_etl_password' ;
delete from tb_app_properties where prop_group = 'MQ' and prop_name = 'mq_etl_virtualHost' ;

