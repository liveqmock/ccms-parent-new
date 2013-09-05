--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 把permission的id改为自增的

ALTER TABLE tds_permission
CHANGE id id BIGINT(20) NOT NULL AUTO_INCREMENT;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tds_permission
CHANGE id id BIGINT(20) NOT NULL;
