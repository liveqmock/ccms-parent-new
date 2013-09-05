--// update mq etl queue
-- Migration SQL that makes the change goes here.

update tb_app_properties set prop_value = 'queue_0_taobao_100571094' where prop_group = 'MQ' and prop_name = 'mq_etl_queue' ;

--//@UNDO
-- SQL to undo the change goes here.

update tb_app_properties set prop_value = 'etltest' where prop_group = 'MQ' and prop_name = 'mq_etl_queue' ;
