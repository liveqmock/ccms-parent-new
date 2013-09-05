--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 把shop_id改名为id,shop_name改名为name

ALTER TABLE plt_taobao_shop
CHANGE shop_id id VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
CHANGE shop_name name VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺名称，即店主用户昵称';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE plt_taobao_shop
CHANGE id shop_id VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
CHANGE name shop_name VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺名称，即店主用户昵称';

