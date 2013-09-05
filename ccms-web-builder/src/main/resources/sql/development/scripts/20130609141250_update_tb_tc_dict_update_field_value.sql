--// update tb_tc_dict update field value
-- Migration SQL that makes the change goes here.
update tb_tc_dict set remark='客户拍下多笔订单未付款只会发送1条催付短信,客户当天有发送过催付短信的不会再发送' where type=1 and code='1';
update tb_tc_dict set remark='不同客户的多笔订单如果出现同一个收货人手机只发送一条催付短信,该手机号码当天发送过催付短信的不会再发送' where type=1 and code='2';
update tb_tc_dict set remark='昨天和今天有过付款的客户不发送催付短信' where type=1 and code='3';
update tb_tc_dict set remark='短信黑名单中的手机号码不发送催付短信' where type=1 and code='4';


--//@UNDO
-- SQL to undo the change goes here.

update tb_tc_dict set remark='' where type=1 and code='1';
update tb_tc_dict set remark='' where type=1 and code='2';
update tb_tc_dict set remark='' where type=1 and code='3';
update tb_tc_dict set remark='' where type=1 and code='4';
