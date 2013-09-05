--// init tb app properties
-- Migration SQL that makes the change goes here.

INSERT ignore INTO tds_permission(id,name,memo,permission_key)
VALUES (1,'权限1',NULL,NULL),
(2,'权限2',NULL,NULL);

--//@UNDO
-- SQL to undo the change goes here.

delete from tds_permission where id in(1,2);
