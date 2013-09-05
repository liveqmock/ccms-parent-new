--// modify table plt_taobao_coupon
-- Migration SQL that makes the change goes here.
alter table plt_taobao_coupon  change creater creator int(11) DEFAULT NULL;


--//@UNDO
-- SQL to undo the change goes here.
alter table plt_taobao_coupon  change creator creater  int(11) DEFAULT NULL;

