--// alter_tables_tb_tc_properties_config
-- Migration SQL that makes the change goes here.
update tb_tc_properties_config set value = '三里屯,中关村,镇江,乌镇' where value = '三里屯,中关村';



--//@UNDO
-- SQL to undo the change goes here.
update tb_tc_properties_config set value = '三里屯,中关村' where value = '三里屯,中关村,镇江,乌镇';

