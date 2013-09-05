--// update tb_tc_dict update remark
-- Migration SQL that makes the change goes here.
update tb_tc_dict set remark='对于今天发送过{carename}的客户(淘宝昵称相同或手机号相同)不再次发送' where type=5 and code=1;


--//@UNDO
-- SQL to undo the change goes here.
update tb_tc_dict set remark='对于今天发送过的{carename}的客户不再次发送' where type=5 and code=1;

