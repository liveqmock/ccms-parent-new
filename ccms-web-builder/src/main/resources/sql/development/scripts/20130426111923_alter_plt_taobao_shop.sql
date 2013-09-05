--// create blacklist table
-- Migration SQL that makes the change goes here.
-- 由于plt前缀的表名的字段名与一些程序有接口约定关系,故将原先的改动改回去.

ALTER TABLE plt_taobao_shop
CHANGE id shop_id VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
CHANGE name shop_name VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺名称，即店主用户昵称';

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE plt_taobao_shop
CHANGE shop_id id VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
CHANGE shop_name name VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '店铺名称，即店主用户昵称';
