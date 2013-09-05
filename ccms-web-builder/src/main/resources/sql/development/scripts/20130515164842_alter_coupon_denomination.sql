--// alter coupon denomination
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_coupon_denomination DROP COLUMN denomination_id;
ALTER TABLE plt_taobao_coupon_denomination ADD PRIMARY KEY (denomination_value);

ALTER TABLE plt_taobao_coupon CHANGE denomination_id denomination_value smallint(6) NOT NULL COMMENT '面额';
update plt_taobao_coupon set denomination_value=100 where denomination_value=6;
update plt_taobao_coupon set denomination_value=50  where denomination_value=5;
update plt_taobao_coupon set denomination_value=20  where denomination_value=4;
update plt_taobao_coupon set denomination_value=10  where denomination_value=3;
update plt_taobao_coupon set denomination_value=5   where denomination_value=2;
update plt_taobao_coupon set denomination_value=3   where denomination_value=1;


--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_coupon_denomination DROP PRIMARY KEY;
ALTER TABLE plt_taobao_coupon_denomination ADD COLUMN denomination_id smallint(6) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '面额ID' ;

ALTER TABLE plt_taobao_coupon CHANGE COLUMN denomination_value denomination_id  smallint(6) NOT NULL COMMENT '面额';
update plt_taobao_coupon set denomination_id=6 where denomination_id=100;
update plt_taobao_coupon set denomination_id=5 where denomination_id=50;
update plt_taobao_coupon set denomination_id=4 where denomination_id=20;
update plt_taobao_coupon set denomination_id=3 where denomination_id=10;
update plt_taobao_coupon set denomination_id=2 where denomination_id=5;
update plt_taobao_coupon set denomination_id=1 where denomination_id=3;

