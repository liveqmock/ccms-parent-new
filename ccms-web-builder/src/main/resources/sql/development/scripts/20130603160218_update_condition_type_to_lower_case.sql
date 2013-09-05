--// update condition type to lower case
-- Migration SQL that makes the change goes here.

UPDATE rc_condition SET type='customer' WHERE type='CUSTOMER_BASED';
UPDATE rc_condition SET type='order' WHERE type='ORDER_BASED';

--//@UNDO
-- SQL to undo the change goes here.

-- bu yong le
