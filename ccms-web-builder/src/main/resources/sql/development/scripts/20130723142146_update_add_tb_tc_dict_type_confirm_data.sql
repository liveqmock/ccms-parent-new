--// update add tb_tc_dict type confirm data
-- Migration SQL that makes the change goes here.
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(8,1,'排除今天发过客户',0,1,'对于今天发送过{carename}的客户(淘宝昵称相同或手机号相同)不再次发送');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(8,2,'屏蔽短信黑名单用户',0,1,'不对短信黑名单用户发送{carename}');


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=8;

