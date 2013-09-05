--// update module of rc run and index
-- Migration SQL that makes the change goes here.

UPDATE module_type SET url='#/personalizedPackage/subIndex' WHERE id='32';
UPDATE module_type SET url='#/personalizedPackage/subIndex' WHERE id='34';
UPDATE module_type SET url='#/personalizedPackage/statuses' WHERE id='36';

--//@UNDO
-- SQL to undo the change goes here.

UPDATE module_type SET url='#/personalizedPackage/subIndex' WHERE id='32';
UPDATE module_type SET url='#/personalizedPackage/subIndex' WHERE id='34';
UPDATE module_type SET url='#/personalizedPackage/statuses' WHERE id='36';
