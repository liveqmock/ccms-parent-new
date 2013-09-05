--// update mq host address
-- Migration SQL that makes the change goes here.

update tb_app_properties set prop_value = '10.200.187.74' where prop_group = 'MQ' and prop_name = 'mq_host' ;

--//@UNDO
-- SQL to undo the change goes here.

update tb_app_properties set prop_value = '10.200.187.73' where prop_group = 'MQ' and prop_name = 'mq_host' ;

