--// add url to module marketing
-- Migration SQL that makes the change goes here.

UPDATE module_type SET url='#/marketing/campaign.list' WHERE id='26';

--//@UNDO
-- SQL to undo the change goes here.

-- do it again
UPDATE module_type SET url='#/marketing/campaign.list' WHERE id='26';
