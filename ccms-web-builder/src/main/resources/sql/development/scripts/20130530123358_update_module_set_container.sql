--// update module set container
-- Migration SQL that makes the change goes here.

UPDATE module SET ranking='650' WHERE id='32';
UPDATE module SET ranking=NULL WHERE id='31';

--//@UNDO
-- SQL to undo the change goes here.

-- bu yong le
