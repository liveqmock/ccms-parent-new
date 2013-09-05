--// change a word CCMS-3894
-- Migration SQL that makes the change goes here.

update tm_db_column set show_name='订单级优惠金额' where column_id=606;

--//@UNDO
-- SQL to undo the change goes here.

update tm_db_column set show_name='订单优惠金额' where column_id=606;
