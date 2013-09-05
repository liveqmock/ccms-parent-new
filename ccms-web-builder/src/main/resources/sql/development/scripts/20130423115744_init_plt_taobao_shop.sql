--// init tb app properties
-- Migration SQL that makes the change goes here.

INSERT ignore INTO plt_taobao_shop(id,name,order_created_earliest,order_created_latest)
VALUES ('taobao_68784446','qiushi_shop_test',NULL,NULL);

--//@UNDO
-- SQL to undo the change goes here.

delete from plt_taobao_shop where id='taobao_68784446';
