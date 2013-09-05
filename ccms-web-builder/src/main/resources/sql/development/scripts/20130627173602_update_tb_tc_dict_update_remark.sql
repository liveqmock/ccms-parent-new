--// update tb_tc_dict update remark
-- Migration SQL that makes the change goes here.

update tb_tc_dict set remark='在店铺没有过交易成功订单的客户' where type=2 and code=0;

--//@UNDO
-- SQL to undo the change goes here.


