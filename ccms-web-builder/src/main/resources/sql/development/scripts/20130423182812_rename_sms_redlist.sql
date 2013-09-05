--// create blacklist table
-- Migration SQL that makes the change goes here.

ALTER TABLE tw_sms_redlist rename to tw_mobile_redlist;



--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tw_mobile_redlist rename to tw_sms_redlist;