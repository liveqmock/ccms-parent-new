--// add module for affairs_orderlist
-- Migration SQL that makes the change goes here.

UPDATE module_type SET url='#/affairs/orderlist' WHERE id='40';

--//@UNDO
-- SQL to undo the change goes here.

UPDATE module_type SET url='#/service/xiadanshiwu' WHERE id='40';
