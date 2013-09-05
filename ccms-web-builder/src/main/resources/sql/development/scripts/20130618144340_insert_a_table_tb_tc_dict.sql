--// insert a table tb_tc_dict
-- Migration SQL that makes the change goes here.
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES (6,  'ETL_TIME_OUT','0',0,1,'ETL延迟时间');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 7, 'SIGNED', '签收字样', 1, 1, '签收|妥投|派送成功');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 7, 'REJECT', '拒签字样', 1, 2, '签收失败|拒绝签收|失败签收|签收异常|异常签收|未妥投|未签收');
INSERT INTO tb_tc_dict (type,code,name,is_valid,px,remark) VALUES ( 7, 'DELIVERY', '派件字样', 1, 3, '派件');


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_tc_dict where type=6;
delete from tb_tc_dict where type=7;


