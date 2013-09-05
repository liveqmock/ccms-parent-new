--// add app properties mq etl queue
-- Migration SQL that makes the change goes here.

insert into tb_app_properties (prop_group, prop_name, prop_value, prop_desc) values ('MQ', 'mq_etl_queue', 'etltest', 'rabbitMQ的etl队列名');


--//@UNDO
-- SQL to undo the change goes here.

delete from tb_app_properties where prop_group = 'MQ' and prop_name = 'mq_etl_queue' ;

