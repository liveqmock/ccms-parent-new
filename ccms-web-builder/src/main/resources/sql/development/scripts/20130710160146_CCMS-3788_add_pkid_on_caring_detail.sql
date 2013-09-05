--// CCMS-3788_add_pkid_on_caring_detail
-- Migration SQL that makes the change goes here.
ALTER TABLE tb_tc_caring_detail
ADD COLUMN pkid  int NULL AUTO_INCREMENT FIRST ,
MODIFY COLUMN tid  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单编号' AFTER pkid,
ADD PRIMARY KEY (pkid);




--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_tc_caring_detail
DROP COLUMN pkid,
MODIFY COLUMN tid  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单编号' FIRST ,
DROP PRIMARY KEY;

