--// update tb_tc_care_config add field
-- Migration SQL that makes the change goes here.
alter table tb_tc_care_config add date_number Integer DEFAULT NULL COMMENT '非自定义日期数';
alter table tb_tc_care_config modify sms_content text DEFAULT NULL COMMENT '短信内容';

insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(5,'1','选中排除今天发过的客户',0,1,'用户下单后判断如果这个用户今天已经发过下单营销了，就不在发送');
insert into tb_tc_dict(type,code,name,is_valid,px,remark) values(5,'2','屏蔽短信黑名单用户',0,2,'短信黑名单中的手机号码不发送催付短信');

alter table tb_tc_care_config modify care_start_time time DEFAULT NULL COMMENT '关怀时间开始时间';
alter table tb_tc_care_config modify care_end_time time DEFAULT NULL COMMENT '关怀时间结束时间';



--//@UNDO
-- SQL to undo the change goes here.
alter table tb_tc_care_config drop column date_number;
alter table tb_tc_care_config modify sms_content varchar(500) DEFAULT NULL COMMENT '短信内容';

delete from tb_tc_dict where type=5;

alter table tb_tc_care_config modify care_start_time datetime DEFAULT NULL COMMENT '关怀时间开始时间';
alter table tb_tc_care_config modify care_end_time datetime DEFAULT NULL COMMENT '关怀时间结束时间';