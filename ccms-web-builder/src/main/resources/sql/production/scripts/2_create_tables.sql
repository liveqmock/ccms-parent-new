###--------------------------------------------------------------------------------------------------------------------
###  2_create_tables.sql
###  建表及字段索引脚本
###--------------------------------------------------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS=0;
-- SET DEFAULT_STORAGE_ENGINE = INNODB;


CREATE TABLE acl_sid (
id  bigint(20) NOT NULL AUTO_INCREMENT ,
principal  tinyint(1) ,
sid  varchar(100) NOT NULL ,
PRIMARY KEY (id),
UNIQUE INDEX uk_acl_sid_sid USING BTREE (sid, principal)
);


CREATE TABLE acl_class (
id  bigint(20) NOT NULL AUTO_INCREMENT ,
class  varchar(100) NOT NULL ,
PRIMARY KEY (id),
UNIQUE INDEX uk_acl_class_class USING BTREE (class)
);


CREATE TABLE acl_object_identity (
id  bigint(20) NOT NULL AUTO_INCREMENT ,
object_id_class  bigint(20) NOT NULL ,
object_id_identity  bigint(20) NOT NULL ,
parent_object  bigint(20) ,
owner_sid  bigint(20) ,
entries_inheriting  tinyint(1) ,
PRIMARY KEY (id),
CONSTRAINT fk_acl_object_identity_object_id_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT fk_acl_object_identity_owner_sid FOREIGN KEY (owner_sid) REFERENCES acl_sid (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT fk_acl_object_identity_parent_object FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
UNIQUE INDEX uk_acl_object_identity_object_id_class USING BTREE (object_id_class, object_id_identity)
);


CREATE TABLE acl_entry (
id  bigint(20) NOT NULL AUTO_INCREMENT ,
acl_object_identity  bigint(20) NOT NULL ,
ace_order  int(11) NOT NULL ,
sid  bigint(20) NOT NULL ,
mask  int(11) NOT NULL ,
granting  tinyint(1) ,
audit_success  tinyint(1) ,
audit_failure  tinyint(1) ,
PRIMARY KEY (id),
CONSTRAINT fk_acl_entry_acl_object_identity FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT fk_acl_entry_sid FOREIGN KEY (sid) REFERENCES acl_sid (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
UNIQUE INDEX uk_acl_entry_ace_order USING BTREE (ace_order, acl_object_identity)
);


CREATE TABLE tb_role (
id  bigint(20) NOT NULL AUTO_INCREMENT ,
name  varchar(50) ,
memo  varchar(200) ,
PRIMARY KEY (id)
) AUTO_INCREMENT = 100000
COMMENT = '系统用户角色表';


CREATE TABLE tb_role_permission (
role_id  bigint(20) NOT NULL ,
permission_id  bigint(20) NOT NULL ,
PRIMARY KEY (role_id, permission_id)
)
COMMENT = '系统用户角色－权限关系表';


CREATE TABLE tb_sysuser (
id  bigint(20) NOT NULL AUTO_INCREMENT ,
user_type  varchar(10)  NOT NULL DEFAULT 'build-in' COMMENT '用户类型' ,
login_name  varchar(20) NOT NULL COMMENT '用户登录名称' ,
password  varchar(100)NOT NULL DEFAULT '' COMMENT '用户登录密码' ,
real_name  varchar(50) COMMENT '用户真实姓名' ,
mobile  varchar(20) COMMENT '用户手机号码' ,
email  varchar(100) ,
disabled  varchar(10) ,
create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '用户创建时间' ,
PRIMARY KEY (id) ,
UNIQUE INDEX uk_tb_sysuser_loginname_usertype (login_name, user_type)
) AUTO_INCREMENT = 100000
COMMENT = '系统用户表';


CREATE TABLE tb_user_role (
user_id  bigint(20) NOT NULL ,
role_id  bigint(20) NOT NULL ,
PRIMARY KEY (user_id, role_id)
);


CREATE TABLE tds_permission (
id  bigint(20) NOT NULL AUTO_INCREMENT,
name  varchar(50) ,
memo  varchar(200) ,
permission_key  varchar(50) ,
PRIMARY KEY (id)
);


CREATE TABLE tb_app_expired (
associate_username  varchar(20) NOT NULL COMMENT 'rmi用户名' ,
expire_date  date COMMENT '淘宝应用订购过期时间' ,
PRIMARY KEY (associate_username)
)
COMMENT = '应用订购期';


CREATE TABLE tb_app_properties (
prop_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数ID,仅作主键,不应该被其他表应用',
prop_group varchar(20) NOT NULL COMMENT '参数所属组别,取值：FTP、MQ、CHANNEL、CCMS',
prop_name varchar(50) NOT NULL COMMENT '配置参数名',
prop_value varchar(200) DEFAULT NULL COMMENT '配置值',
prop_desc varchar(255) DEFAULT NULL COMMENT '描述',
PRIMARY KEY (prop_id)
)
COMMENT='应用级属性参数配置表';


CREATE TABLE tb_function_limit (
id  decimal(12,0) NOT NULL COMMENT '编号' ,
edition  char(16) NOT NULL COMMENT 'CCMS规格版本号; BASIC: 基础版； STANDARD: 标准； ADVANCED: 高级版，ULTIMATE：旗舰版' ,
limit_item  char(32) NOT NULL COMMENT '限制项名称，ACTIVITY_CAMPAIGNS：活动； SYS_USERS:系统用户 ' ,
limit_count  int(11) ,
description  varchar(100) COMMENT '描述' ,
disabled  tinyint(1) COMMENT '是否有效' ,
create_at  datetime NOT NULL COMMENT '创建时间' ,
update_at  datetime COMMENT '修改时间',
PRIMARY KEY (id)
)
COMMENT = '系统功能限制';


CREATE TABLE tb_campaign (
camp_id  bigint(20) NOT NULL AUTO_INCREMENT ,
prog_id  bigint(20) ,
camp_name  varchar(512) COMMENT '活动的名称' ,
camp_type  int(11) COMMENT '客户自定义活动类别，对应tdu_campaign_type主键' ,
camp_status  varchar(15) DEFAULT 'A1' COMMENT '活动的状态,对应的字典表td_camp_status' ,
creater  int(11) ,
investigator  int(11) ,
created_time  datetime COMMENT '创建时间' ,
start_time  datetime COMMENT '开始时间' ,
end_time  datetime COMMENT '结束时间' ,
camp_desc  varchar(1024) COMMENT '描述' ,
comments  varchar(1024) COMMENT '备注' ,
workflow_id bigint(20) NULL COMMENT '流程id' ,
workflow_type  varchar(20) COMMENT '活动所属流程类别; STANDARD：标准，RFM：RFM，CUSTOM_INDEX：自定义标签，MEMBER_MANAGE：会员管理' ,
plat_code char(20) NOT NULL COMMENT '平台代码' ,
edition varchar(20) COMMENT '版本Edition' ,
pic_url varchar(255) COMMENT '流程图片地址' ,
disabled  tinyint(1) ,
PRIMARY KEY (camp_id)
)
COMMENT = '活动的基本信息表';


CREATE TABLE tb_camp_investigate_comments (
comments_id  bigint(20) NOT NULL AUTO_INCREMENT ,
camp_id  bigint(20) ,
create_date  datetime COMMENT '备注创建的时间' ,
comments  varchar(512) COMMENT '备注的内容' ,
operator  int(11) ,
operation  varchar(20) COMMENT '审批的操作。如：提交审批，通过审批，拒绝审批' ,
PRIMARY KEY (comments_id)
)
COMMENT = '活动的审批的备注';


CREATE TABLE tb_template (
template_id  bigint(20) NOT NULL AUTO_INCREMENT ,
template_name  varchar(100) NOT NULL COMMENT '模板名称' ,
creator  int(11) COMMENT '创建人',
created_time  datetime NOT NULL COMMENT '创建时间' ,
updated_time  datetime COMMENT '修改时间' ,
template_desc  varchar(1024) COMMENT '描述' ,
comments  varchar(2048) COMMENT '备注' ,
workflow_id bigint(20) NULL COMMENT '流程id' ,
plat_code char(20) NOT NULL COMMENT '平台代码' ,
edition varchar(20) COMMENT '版本Edition' ,
pic_url varchar(255) COMMENT '流程图片地址' ,
disabled  tinyint(1)  NULL  COMMENT '模版状态(1:生效、0:失效)' ,
PRIMARY KEY (template_id)
)
auto_increment = 1000
COMMENT = '活动模板表';


CREATE TABLE tb_program (
prog_id  bigint(20) NOT NULL AUTO_INCREMENT ,
prog_name  varchar(512) COMMENT '项目名称' ,
type  int(11) ,
creator  int(11) ,
created_time  datetime COMMENT '创建时间' ,
start_time  datetime COMMENT '开始时间' ,
end_time  datetime COMMENT '结束时间' ,
prog_desc  varchar(1024) COMMENT '项目描述' ,
comments  varchar(1024) COMMENT '备注' ,
plat_code char(20) NOT NULL COMMENT '平台代码' ,
edition varchar(20) COMMENT '版本Edition' ,
PRIMARY KEY (prog_id)
)
COMMENT = '沟通项目表';


CREATE TABLE tdu_prog_type (
prog_type_id  bigint(20) NOT NULL AUTO_INCREMENT ,
prog_type  varchar(15) COMMENT '项目类型名' ,
disabled  tinyint(1) ,
PRIMARY KEY (prog_type_id)
)
COMMENT = '项目类型字典表';


CREATE TABLE tds_campaign_status (
status_id  varchar(20) NOT NULL COMMENT '活动状态ID （定义 待补充）' ,
status_value  varchar(20) NOT NULL COMMENT '活动状态显示值' ,
orderid  decimal(2,0) COMMENT '顺序' ,
PRIMARY KEY (status_id)
)
COMMENT = '活动状态表';


CREATE TABLE tdu_campaign_category (
category_id  bigint(20) NOT NULL ,
category_value  varchar(15) COMMENT '活动类型名' ,
disabled  tinyint(1) ,
PRIMARY KEY (category_id)
)
COMMENT = '活动类型表';


CREATE TABLE tds_job_status (
status_id  decimal(2,0) NOT NULL ,
status_name  varchar(20) ,
PRIMARY KEY (status_id)
)
COMMENT = '任务状态维表';


CREATE TABLE tds_subjob_status (
status_id  decimal(2,0) NOT NULL ,
status_name  varchar(20) ,
PRIMARY KEY (status_id)
)
COMMENT = '子任务状态维表';


CREATE TABLE twf_log_group (
subjob_id  bigint(20) ,
node_id  bigint(20) ,
group_type  decimal(3,0) COMMENT '用户组类型，见td_group_type' ,
createtime  datetime COMMENT '创建时间' ,
job_id  bigint(20) ,
user_count_sending  int(11) ,
user_count_control  int(11) NULL
)
COMMENT = '活动执行用户组列表';


CREATE TABLE twf_log_group_user (
subjob_id  bigint(20) NOT NULL ,
uni_id  varchar(64) NOT NULL COMMENT '统一客户ID' ,
control_group_type  int(2) COMMENT '发送控制组类型，见td_control_group_type' ,
PRIMARY KEY (subjob_id, uni_id)
)
COMMENT = '活动执行用户组用户 (subjob_id每隔10000就建立一个新的分区)'
PARTITION BY RANGE (subjob_id)
(
    PARTITION p000 VALUES LESS THAN (60000),
    PARTITION p001 VALUES LESS THAN (70000),
    PARTITION p002 VALUES LESS THAN (80000),
    PARTITION p003 VALUES LESS THAN (90000),
    PARTITION p004 VALUES LESS THAN (100000),
    PARTITION p005 VALUES LESS THAN (110000),
    PARTITION p006 VALUES LESS THAN (120000),
    PARTITION p007 VALUES LESS THAN (130000),
    PARTITION p008 VALUES LESS THAN (140000),
    PARTITION p009 VALUES LESS THAN (150000),
    PARTITION p010 VALUES LESS THAN (160000),
    PARTITION p011 VALUES LESS THAN (170000),
    PARTITION p012 VALUES LESS THAN (180000),
    PARTITION p013 VALUES LESS THAN (190000),
    PARTITION p014 VALUES LESS THAN (200000),
    PARTITION p015 VALUES LESS THAN (210000),
    PARTITION p016 VALUES LESS THAN (220000),
    PARTITION p017 VALUES LESS THAN (230000),
    PARTITION p018 VALUES LESS THAN (240000),
    PARTITION p019 VALUES LESS THAN (250000),
    PARTITION p020 VALUES LESS THAN (260000),
    PARTITION p021 VALUES LESS THAN (270000),
    PARTITION p022 VALUES LESS THAN (280000),
    PARTITION p023 VALUES LESS THAN (290000),
    PARTITION p024 VALUES LESS THAN (300000),
    PARTITION p025 VALUES LESS THAN (310000),
    PARTITION p026 VALUES LESS THAN (320000),
    PARTITION p027 VALUES LESS THAN (330000),
    PARTITION p028 VALUES LESS THAN (340000),
    PARTITION p029 VALUES LESS THAN (350000),
    PARTITION p030 VALUES LESS THAN (360000),
    PARTITION p031 VALUES LESS THAN (370000),
    PARTITION p032 VALUES LESS THAN (380000),
    PARTITION p033 VALUES LESS THAN (390000),
    PARTITION p034 VALUES LESS THAN (400000),
    PARTITION p035 VALUES LESS THAN (410000),
    PARTITION p036 VALUES LESS THAN (420000),
    PARTITION p037 VALUES LESS THAN (430000),
    PARTITION p038 VALUES LESS THAN (440000),
    PARTITION p039 VALUES LESS THAN (450000),
    PARTITION p040 VALUES LESS THAN (460000),
    PARTITION p041 VALUES LESS THAN (470000),
    PARTITION p042 VALUES LESS THAN (480000),
    PARTITION p043 VALUES LESS THAN (490000),
    PARTITION p044 VALUES LESS THAN (500000),
    PARTITION p045 VALUES LESS THAN (510000),
    PARTITION p046 VALUES LESS THAN (520000),
    PARTITION p047 VALUES LESS THAN (530000),
    PARTITION p048 VALUES LESS THAN (540000),
    PARTITION p049 VALUES LESS THAN (550000),
    PARTITION p050 VALUES LESS THAN (560000),
    PARTITION p051 VALUES LESS THAN (570000),
    PARTITION p052 VALUES LESS THAN (580000),
    PARTITION p053 VALUES LESS THAN (590000),
    PARTITION p054 VALUES LESS THAN (600000),
    PARTITION p055 VALUES LESS THAN (610000),
    PARTITION p056 VALUES LESS THAN (620000),
    PARTITION p057 VALUES LESS THAN (630000),
    PARTITION p058 VALUES LESS THAN (640000),
    PARTITION p059 VALUES LESS THAN (650000),
    PARTITION p060 VALUES LESS THAN (660000),
    PARTITION p061 VALUES LESS THAN (670000),
    PARTITION p062 VALUES LESS THAN (680000),
    PARTITION p063 VALUES LESS THAN (690000),
    PARTITION p064 VALUES LESS THAN (700000),
    PARTITION p065 VALUES LESS THAN (710000),
    PARTITION p066 VALUES LESS THAN (720000),
    PARTITION p067 VALUES LESS THAN (730000),
    PARTITION p068 VALUES LESS THAN (740000),
    PARTITION p069 VALUES LESS THAN (750000),
    PARTITION p070 VALUES LESS THAN (760000),
    PARTITION p071 VALUES LESS THAN (770000),
    PARTITION p072 VALUES LESS THAN (780000),
    PARTITION p073 VALUES LESS THAN (790000),
    PARTITION p074 VALUES LESS THAN (800000),
    PARTITION p075 VALUES LESS THAN (810000),
    PARTITION p076 VALUES LESS THAN (820000),
    PARTITION p077 VALUES LESS THAN (830000),
    PARTITION p078 VALUES LESS THAN (840000),
    PARTITION p079 VALUES LESS THAN (850000),
    PARTITION p080 VALUES LESS THAN (860000),
    PARTITION p081 VALUES LESS THAN (870000),
    PARTITION p082 VALUES LESS THAN (880000),
    PARTITION p083 VALUES LESS THAN (890000),
    PARTITION p084 VALUES LESS THAN (900000),
    PARTITION p085 VALUES LESS THAN (910000),
    PARTITION p086 VALUES LESS THAN (920000),
    PARTITION p087 VALUES LESS THAN (930000),
    PARTITION p088 VALUES LESS THAN (940000),
    PARTITION p089 VALUES LESS THAN (950000),
    PARTITION p090 VALUES LESS THAN (960000),
    PARTITION p091 VALUES LESS THAN (970000),
    PARTITION p092 VALUES LESS THAN (980000),
    PARTITION p093 VALUES LESS THAN (990000),
    PARTITION p094 VALUES LESS THAN (1000000),
    PARTITION p100 VALUES LESS THAN MAXVALUE
);


CREATE TABLE twf_log_job (
job_id  bigint(20) NOT NULL AUTO_INCREMENT ,
camp_id  bigint(20) ,
status  decimal(3,0) NOT NULL COMMENT '状态，见表td_job_status' ,
starttime  datetime COMMENT '开始时间' ,
endtime  datetime COMMENT '结束时间' ,
is_test  tinyint(1) ,
plantime  datetime ,
last_job  tinyint(1) ,
PRIMARY KEY (job_id),
KEY idx_twf_log_job_camp_id (camp_id)
)
COMMENT = '活动执行总表';



CREATE TABLE twf_log_subjob (
subjob_id  bigint(20) NOT NULL AUTO_INCREMENT ,
job_id  bigint(20) ,
camp_id  bigint(20) ,
node_id  bigint(20) ,
status  decimal(3,0) COMMENT '状态，参考td_job_status' ,
plantime  datetime COMMENT '计划执行时间' ,
starttime  datetime COMMENT '开始时间' ,
endtime  datetime COMMENT '结束时间' ,
output_msg  varchar(64)  COMMENT '输出提示信息' ,
memo  varchar(1024) COMMENT '备注' ,
is_test  tinyint(1) ,
PRIMARY KEY (subjob_id),
UNIQUE INDEX uk_twf_log_subjob_job_id_node_id (job_id, node_id)
)
COMMENT = '流程执行子任务表';


create table twf_log_job_data (
subjob_id  bigint(20),
job_id  bigint(20) ,
camp_id  bigint(20) ,
source  bigint(20) COMMENT '源节点id',
target  bigint(20) COMMENT '目标节点id',
data_type varchar(20) COMMENT '数据类型',
data_code varchar(50) COMMENT '数据名称',
KEY idx_twf_log_subjob_data_jobid_target (job_id,target)
)
COMMENT = '流程节点间数据传输记录保存';


CREATE TABLE twf_node (
node_id  bigint(20) NOT NULL AUTO_INCREMENT,
value  varchar(40) COMMENT '名字' ,
style  varchar(200) ,
vertex  varchar(10) ,
workflow_id  bigint(20) NOT NULL COMMENT '流程id',
x  int(11) ,
y  int(11) ,
width  int(11) ,
height  int(11) ,
as_t  varchar(20) ,
type  varchar(30) COMMENT '节点类型' ,
description  varchar(140) COMMENT '节点描述信息（不超过140个汉字）' ,
PRIMARY KEY (node_id)
)
auto_increment = 1000
COMMENT = '保存流程的节点信息';


CREATE TABLE twf_workflow (
workflow_id  bigint(20) NOT NULL AUTO_INCREMENT ,
create_time  datetime NOT NULL COMMENT '创建时间' ,
update_time  datetime COMMENT '更新时间' ,
PRIMARY KEY (workflow_id)
)
auto_increment = 1000
COMMENT = '流程信息表';


CREATE TABLE twf_connect (
connect_id  bigint(20) NOT NULL AUTO_INCREMENT ,
edge  varchar(20) ,
workflow_id  bigint(20) ,
source  int(11) NOT NULL,
target  int(11) NOT NULL,
relative  varchar(20) ,
as_t  varchar(10) ,
PRIMARY KEY (connect_id),
KEY idx_twf_connect_source (source),
KEY idx_twf_connect_target (target)
)
auto_increment = 1000
COMMENT = '流程的连接信息表';


CREATE TABLE dashboard_module (
dashboard_module_id  bigint(20) NOT NULL AUTO_INCREMENT ,
dashboard_module_name  varchar(100) NOT NULL ,
dashboard_module_url  varchar(200) NOT NULL ,
dashboard_module_createtime  time DEFAULT NULL ,
dashboard_default_module  tinyint(4) NOT NULL COMMENT '是否为默认加载模块' ,
PRIMARY KEY (dashboard_module_id)
)
COMMENT='dashboard模块';


CREATE TABLE dashboard_module_config (
user_id  bigint(20) NOT NULL ,
module_id  bigint(20) NOT NULL ,
disabled  varchar(10) DEFAULT NULL ,
PRIMARY KEY (user_id, module_id)
)
COMMENT='dashboard模块配置';


CREATE TABLE twf_node_type (
id  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
title  varchar(20) COMMENT '标题',
img_url  varchar(100) COMMENT '图标地址',
name  varchar(50) COMMENT '节点名称',
disabled  tinyint(1) COMMENT '是否禁用, 1： 节点被禁用，不会添加到菜单中',
remark  varchar(255) COMMENT '备注',
PRIMARY KEY (id)
)
COMMENT='节点类型配置表';


CREATE TABLE twf_node_type_group (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
name varchar(20) COMMENT '名称',
text varchar(20) COMMENT '显示文本',
remark varchar(255) COMMENT '备注',
PRIMARY KEY (id)
)
COMMENT='节点类型分组';


CREATE TABLE twf_workflow_node_group (
id bigint(20) NOT NULL COMMENT '主键ID',
type_id bigint(20) COMMENT '流程类型id,普通流程、特殊流程，对应 twf_workflow_type 主键',
toolbar_id bigint(20) COMMENT '节点组栏目， 对应 twf_node_type_group 主键',
remark varchar(255) COMMENT '备注',
PRIMARY KEY (id)
)
COMMENT='“流程类别”所对应的“节点类型分组”';


CREATE TABLE twf_node_type_mid2_toolbar (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
node_config_id bigint(20) COMMENT '对应twf_node_type',
mid_id bigint(20) COMMENT '对应表twf_mid_toolbar_node_type',
disabled tinyint(1) COMMENT '是否disabled, true该节点显示但是不可以拖动节点到画布',
remark varchar(255) COMMENT '备注',
PRIMARY KEY (id)
);


CREATE TABLE twf_node_time (
node_id  bigint(20) NOT NULL ,
iscycle  tinyint(1) ,
isrealtime  tinyint(1) ,
realtime_begin_date  date COMMENT '非周期性的开始日期' ,
realtime_begin_time  varchar(10) COMMENT '非周期性的开始时间' ,
cycle_begin_date  date COMMENT '周期性的开始日期' ,
cycle_end_date  date COMMENT '周期性的结束日期' ,
cycle_begin_time  varchar(10) COMMENT '周期性的开始时间' ,
cycle_type  varchar(10) COMMENT '周期类型： day 每天 week 每周 month 每月' ,
cycle_value  varchar(100) COMMENT '周期值' ,
PRIMARY KEY (node_id)
)
COMMENT = '时间节点的配置信息';


CREATE TABLE twf_node_wait (
node_id  bigint(20) NOT NULL ,
isdate  int(11) ,
wait_date  datetime COMMENT '等待到指定日期(2010-10-10)' ,
wait_day  int(11) ,
wait_time  varchar(10) COMMENT '等待到指定日期或者天数的时间(如：17:00)' ,
node_name  varchar(50) ,
wait_hour int(5) COMMENT '等待n小时后执行' ,
wait_minute int(5) COMMENT '等待m分钟后执行' ,
PRIMARY KEY (node_id)
)
COMMENT = '等待节点配置信息表';


CREATE TABLE twf_node_target (
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
name  varchar(20) COMMENT '名称' ,
control_group_type  int(4) COMMENT '控制组生成方式： 1 百分比 2 人数 ' ,
control_group_value  varchar(10) COMMENT '控制组生成值，或者是百分比，或者是指定的人数' ,
remark  varchar(128) COMMENT '备注' ,
created datetime COMMENT '创建时间' ,
PRIMARY KEY (node_id)
)
COMMENT '目标组节点' ;


CREATE TABLE twf_node_sms (
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
name  varchar(20) COMMENT '名称' ,
unsubscribe_enabled  tinyint(1) COMMENT '可否订阅 0-否 1-可' ,
blacklist_disabled  tinyint(1) COMMENT '屏蔽黑名单 0-否 1-是' ,
redlist_enabled  tinyint(1) COMMENT '发送红名单 0-不 1-是' ,
delivery_channel_id  int(4) COMMENT '发送通道ID' ,
test_phone_string  varchar(256) COMMENT '测试执行号码串' ,
delivery_category varchar(20) COMMENT '发送类别' ,
message_value  text COMMENT '短信内容' ,
phone_num_source  varchar(20) COMMENT '手机号码来源' ,
output_control  varchar(20) COMMENT '输出控制' ,
delivery_selection  varchar(20) COMMENT '发送方式选择' ,
assign_delivery_date  varchar(20) COMMENT '指定发送日期，字符各式 YYYY-MM-DD' ,
assign_delivery_time  varchar(20) COMMENT '指定发送时分，字符各式 HH:MM' ,
over_assign_delivery_period  tinyint(1) COMMENT '是否超时预设时间' ,
sampling_enabled  tinyint(1) COMMENT '可抽样 0-不可 1-可' ,
sampling_copies  int(4) COMMENT '抽样数' ,
remark  varchar(128) COMMENT '备注' ,
created datetime COMMENT '创建时间' ,
PRIMARY KEY (node_id)
)
COMMENT '短信节点' ;

CREATE TABLE twf_node_sms_sample_record (
id  bigint(20) NOT NULL AUTO_INCREMENT,
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
subjob_id  bigint(20) NOT NULL ,
uni_id  varchar(128) ,
content  varchar(128) ,
PRIMARY KEY (id)
)
COMMENT '短信节点生成抽样数据' ;


CREATE TABLE twf_node_sms_execution_record (
id  bigint(20) NOT NULL AUTO_INCREMENT,
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
subjob_id  bigint(20) NOT NULL ,
target_group_customers  bigint(20) COMMENT '目标组用户数' ,
control_group_customers  bigint(20) COMMENT '控制组用户数' ,
valid_phone_amount  bigint(20) COMMENT '有效手机号码数' ,
invalid_phone_amount bigint(20) COMMENT '无效手机号码数' ,
sending_total_num  bigint(20) COMMENT '短信发送条数' ,
sending_price  decimal(15,2) DEFAULT NULL COMMENT '发送单价' ,
created_time  datetime DEFAULT NULL COMMENT '创建时间' ,
PRIMARY KEY (id)
)
COMMENT '短信节点执行后生成数据' ;


CREATE TABLE tb_channel (
channel_id  bigint(20) NOT NULL AUTO_INCREMENT,
channel_name  varchar(255) NOT NULL COMMENT '渠道名称' ,
channel_type  int(4) NOT NULL COMMENT '渠道类型' ,
channel_price  decimal(15,2) DEFAULT NULL COMMENT '渠道单价' ,
channel_desc  varchar(512) COMMENT '渠道描述' ,
PRIMARY KEY (channel_id)
)
COMMENT '渠道表' ;


CREATE TABLE tb_remote_log_exception (
exception_id  bigint(20) NOT NULL AUTO_INCREMENT,
user_id  bigint(20) NOT NULL COMMENT '操作用户ID' ,
function_name  varchar(256) COMMENT '触发函数名' ,
exception_desc  longtext DEFAULT NULL COMMENT '异常信息描述' ,
created  datetime COMMENT '生成时间' ,
PRIMARY KEY (exception_id)
)
COMMENT '访问异常日志表' ;


CREATE TABLE twf_log_channel (
	id  bigint(20) NOT NULL AUTO_INCREMENT ,
	subjob_id  bigint(20) NOT NULL COMMENT '节点任务ID' ,
	channel_id  int(11) NOT NULL COMMENT '渠道ID' ,
	create_time  datetime DEFAULT NULL COMMENT '创建时间' ,
	plan_start_time  datetime DEFAULT NULL COMMENT '计划开始时间' ,
	plan_end_time  datetime DEFAULT NULL COMMENT '计划结束时间' ,
	campaign_id  bigint(20) NOT NULL COMMENT '活动ID' ,
	node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
	channel_type  int(11) DEFAULT NULL COMMENT '渠道类型，比如SMS MMS' ,
	is_test_execute  tinyint(1) DEFAULT NULL ,
	task_id  varchar(255) DEFAULT NULL ,
	PRIMARY KEY (id)
)
COMMENT '渠道执行日志表' ;


create table twf_node_retry (
	id  bigint(20)  NOT NULL AUTO_INCREMENT ,
	job_id  bigint(20) DEFAULT NULL ,
	node_id  bigint(20) DEFAULT NULL ,
	is_test_execute  tinyint(1) DEFAULT NULL ,
	failed_reason  varchar(200) DEFAULT NULL ,
	failed_code  varchar(200) DEFAULT NULL ,
	PRIMARY KEY (id)
)
COMMENT "节点重试表";


CREATE TABLE twf_log_channel_user (
uni_id  varchar(64) NOT NULL COMMENT '统一客户ID' ,
createtime  datetime COMMENT '创建时间' ,
subjob_id  bigint(20) ,
status  decimal(3,0) COMMENT '渠道执行状态（对应渠道反馈维表）' ,
serial_id  decimal(18,0) COMMENT '序列号' ,
node_id  bigint(20) ,
user_channel_info  varchar(100) COMMENT '用户在特定渠道上的联系信息，渠道为短信，记录手机号；渠道为EDM时，记录Email地址；' ,
KEY idx_twf_log_channel_user_uni_id (uni_id) ,
KEY idx_twf_log_channel_user_subjob_id (subjob_id)
)
COMMENT = '用户渠道沟通日志表';


-- metadata begin

create table tm_catalog
(
   catalog_id           bigint not null auto_increment,
   catalog_type         decimal(3,0) not null comment '索引分类',
   parent_id            bigint comment '上级ID',
   show_name            varchar(50) not null comment '显示名称',
   show_order           decimal(3,0) comment '显示顺序',
   plat_code            varchar(32) comment '平台',
   primary key (catalog_id)
);

alter table tm_catalog comment '查询索引：原来的tm_view_dir表';

create table tm_catalog_criteria
(
   id                   bigint not null auto_increment,
   catalog_id           bigint not null comment '索引ID',
   query_criteria_id    bigint comment '查询属性ID',
   show_name            varchar(100) comment '显示名称，覆盖数据字段定义，可空',
   show_order           decimal(3,0) comment '显示顺序，覆盖数据字段定义，可空',
   primary key (id)
);

alter table tm_catalog_criteria comment '索引和查询属性的多对多关联，可以重新定义显示名称和显示顺序';

create table tm_db_column
(
   column_id            bigint(20) not null auto_increment,
   table_id             bigint(20) not null,
   dic_id               bigint(20),
   refer_id             bigint,
   db_name              varchar(50) character set utf8 not null comment '数据库字段名',
   show_name            varchar(100) character set utf8 not null comment '显示名称',
   db_type              varchar(15) character set utf8 not null comment '数据库字段类型',
   is_pk                tinyint(1) not null comment '是否主键',
   created              datetime comment '创建时间',
   updated              datetime comment '更新时间',
   remark               varchar(200) character set utf8 comment '备注',
   primary key (column_id)
)
auto_increment = 100001;

alter table tm_db_column comment '物理表字段定义';

create table tm_db_table
(
   table_id             bigint(20) not null auto_increment,
   db_name              varchar(50) character set utf8 not null comment '数据库表名',
   show_name            varchar(100) character set utf8 not null comment '显示名称',
   pk_column            varchar(50) character set utf8 comment '主键字段',
   plat_code            varchar(20) character set utf8 comment '平台代码',
   created              datetime comment '创建时间',
   updated              datetime comment '更新时间',
   remark               varchar(255) character set utf8 comment '备注',
   primary key (table_id)
)
auto_increment = 100001;

alter table tm_db_table comment '物理表定义';

create table tm_dic
(
   dic_id               bigint(20) not null auto_increment,
   plat_code            varchar(32) comment '平台代码',
   show_name            varchar(50) character set utf8 comment '字典名称',
   remark               varchar(200),
   primary key (dic_id)
)
auto_increment = 10000001;

alter table tm_dic comment '普通字典类型表';

create table tm_dic_value
(
   dic_value_id         bigint(20) not null auto_increment,
   dic_id               bigint(20) not null comment '字典表ID',
   parent_id            bigint(20) default NULL comment '上级字典值的ID，构造树结构',
   dic_value            varchar(32) character set utf8 not null comment '实际值',
   show_name            varchar(64) character set utf8 not null comment '显示名称',
   remark               varchar(200),
   primary key (dic_value_id),
   key idx_tm_dic_type_value_type_id (dic_id, dic_value, show_name)
)
auto_increment = 10000001;

alter table tm_dic_value comment '字典类型枚举值表';

create table tm_query
(
   query_id             bigint not null auto_increment,
   code                 varchar(50) not null comment '有含义的查询代码，唯一标识作用',
   show_name            varchar(50) not null comment '显示名称',
   phy_view             varchar(100) comment '数据库视图名称',
   plat_code            varchar(32) comment '平台代码',
   primary key (query_id)
)
auto_increment = 100001;

alter table tm_query comment '查询模版，代表一个查询的结构，但是查询条件尚未明确，需要用户保存查询条件';

create table tm_query_column
(
   query_column_id      bigint not null auto_increment,
   column_id            bigint(20),
   query_id    			bigint not null,
   query_table_id       bigint,
   column_expr          varchar(100) comment '显示表达式或者解析表达式',
   column_name          varchar(100) not null comment '显示名称或者匹配名称',
   column_order         decimal(3,0) comment '显示顺序',
   primary key (query_column_id)
)
auto_increment = 100001;

alter table tm_query_column comment '查询模版的字段';

create table tm_query_criteria
(
   query_criteria_id    bigint not null auto_increment,
   query_id             bigint comment '查询模版ID',
   column_id            bigint(20) comment '字段ID',
   query_type           varchar(32) comment '查询类型',
   quota_type           varchar(200) comment '指标类型',
   column_expr          varchar(100) comment '属性扩展表达式',
   primary key (query_criteria_id)
)
auto_increment = 100001;

alter table tm_query_criteria comment '查询属性';

create table tm_query_join_criteria
(
   query_join_id        bigint not null auto_increment,
   query_id             bigint not null,
   left_column_id       bigint not null comment '左表关联字段',
   right_column_id      bigint not null comment '右表关联字段',
   join_type            varchar(32) not null comment '关联类型 inner left right',
   primary key (query_join_id)
)
auto_increment = 100001;

alter table tm_query_join_criteria comment '查询where部分定义，也包含groupby 和 having';

create table tm_query_table
(
   query_table_id       bigint not null auto_increment,
   query_id             bigint comment '查询模版ID',
   table_id             bigint comment '表定义ID',
   is_master            boolean not null comment '是否主表',
   primary key (query_table_id)
);

alter table tm_query_table comment '查询模版包含的查询表';

create table tm_refer
(
   refer_id             bigint not null auto_increment,
   plat_code            varchar(32) comment '平台代码',
   refer_key            varchar(64) character set utf8 not null comment '字典值',
   refer_name           varchar(64) character set utf8 not null comment '字典名',
   refer_table          varchar(64) character set utf8 not null comment '来源表',
   refer_criteria_sql   varchar(128) character set utf8 comment '过滤条件',
   order_column 		varchar(32) DEFAULT NULL COMMENT '显示顺序',
   remark               varchar(200) comment '备注',
   primary key (refer_id)
)
auto_increment = 10000001;

alter table tm_refer comment '关联类型的字典';

create table twf_node_query
(
   node_id              bigint(20) not null comment '需要指定，不自增',
   is_exclude           boolean comment '是否排除',
   time_type            tinyint(1) comment '时间类型',
   plat_code            varchar(32) comment '平台',
   primary key (node_id)
);

alter table twf_node_query comment '查询节点';

create table twf_node_query_criteria
(
   node_criteria_id     bigint not null auto_increment,
   query_criteria_id    bigint not null comment '条件模版ID',
   query_defined_id     bigint comment '已定义查询ID',
   operator             varchar(32) not null comment '操作符',
   target_value         varchar(200) not null comment '目标值',
   sub_group            varchar(32) comment '嵌套的标识, 仅支持嵌套一层, 空表示字段不嵌套',
   relation             varchar(3) comment '关系符and or, 空默认为and',
   ui_code 				varchar(32) default null comment 'UI标识',
   primary key (node_criteria_id)
)
auto_increment = 100001;

alter table twf_node_query_criteria comment '已定义的查询条件（来自于用户界面）';

create table twf_node_query_defined
(
   query_defined_id     bigint not null auto_increment,
   node_id              bigint comment '节点',
   query_id             bigint comment '查询模版',
   relation             varchar(3) comment '关系符and或者or',
   ext_ctrl_info        varchar(200) comment '额外控制信息，提供扩展灵活性',
   primary key (query_defined_id)
)
auto_increment = 100001;

alter table twf_node_query_defined comment '已定义查询（保存界面定义的查询）';

--metadata end.

-- quartz 2.1.7 tables
CREATE TABLE twf_qrtz_timer_job_details (
sched_name varchar(120) not null,
job_name varchar(200) not null,
job_group varchar(200) not null,
description varchar(250) null,
job_class_name varchar(250) not null,
is_durable varchar(1) not null,
is_nonconcurrent varchar(1) not null,
is_update_data varchar(1) not null,
requests_recovery varchar(1) not null,
job_data blob null,
primary key (sched_name,job_name,job_group)
)
COMMENT 'JOB DETAILS';

CREATE TABLE twf_qrtz_timer_triggers (
sched_name varchar(120) not null,
trigger_name varchar(200) not null,
trigger_group varchar(200) not null,
job_name varchar(200) not null,
job_group varchar(200) not null,
description varchar(250) null,
next_fire_time bigint(13) null,
prev_fire_time bigint(13) null,
priority integer null,
trigger_state varchar(16) not null,
trigger_type varchar(8) not null,
start_time bigint(13) not null,
end_time bigint(13) null,
calendar_name varchar(200) null,
misfire_instr smallint(2) null,
job_data blob null,
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,job_name,job_group)
references twf_qrtz_timer_job_details(sched_name,job_name,job_group)
)
COMMENT 'TIME TRIGGER';

CREATE TABLE twf_qrtz_timer_simple_triggers (
sched_name varchar(120) not null,
trigger_name varchar(200) not null,
trigger_group varchar(200) not null,
repeat_count bigint(7) not null,
repeat_interval bigint(12) not null,
times_triggered bigint(10) not null,
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,trigger_name,trigger_group)
references twf_qrtz_timer_triggers(sched_name,trigger_name,trigger_group)
)
COMMENT 'TIMER SIMPLE TRIGGER';

CREATE TABLE twf_qrtz_timer_cron_triggers (
sched_name varchar(120) not null,
trigger_name varchar(200) not null,
trigger_group varchar(200) not null,
cron_expression varchar(120) not null,
time_zone_id varchar(80),
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,trigger_name,trigger_group)
references twf_qrtz_timer_triggers(sched_name,trigger_name,trigger_group)
)
COMMENT 'TIME CRON TRIGGERS';

CREATE TABLE twf_qrtz_timer_simprop_triggers (
sched_name varchar(120) not null,
trigger_name varchar(200) not null,
trigger_group varchar(200) not null,
str_prop_1 varchar(512) null,
str_prop_2 varchar(512) null,
str_prop_3 varchar(512) null,
int_prop_1 int null,
int_prop_2 int null,
long_prop_1 bigint null,
long_prop_2 bigint null,
dec_prop_1 numeric(13,4) null,
dec_prop_2 numeric(13,4) null,
bool_prop_1 varchar(1) null,
bool_prop_2 varchar(1) null,
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,trigger_name,trigger_group)
references twf_qrtz_timer_triggers(sched_name,trigger_name,trigger_group)
)
COMMENT 'TIMER SIMPROP TRIGGERS';

CREATE TABLE twf_qrtz_timer_blob_triggers (
sched_name varchar(120) not null,
trigger_name varchar(200) not null,
trigger_group varchar(200) not null,
blob_data blob null,
primary key (sched_name,trigger_name,trigger_group),
index (sched_name,trigger_name, trigger_group),
foreign key (sched_name,trigger_name,trigger_group)
references twf_qrtz_timer_triggers(sched_name,trigger_name,trigger_group)
)
COMMENT 'TIMER BLOB TRIGGERS';

CREATE TABLE twf_qrtz_timer_calendars (
sched_name varchar(120) not null,
calendar_name varchar(200) not null,
calendar blob not null,
primary key (sched_name,calendar_name)
)
COMMENT 'TIMER CALENDARS';

CREATE TABLE twf_qrtz_timer_paused_trigger_grps (
sched_name varchar(120) not null,
trigger_group varchar(200) not null,
primary key (sched_name,trigger_group)
)
COMMENT 'TIMER PAUSED TRIGGER';

CREATE TABLE twf_qrtz_timer_fired_triggers (
sched_name varchar(120) not null,
entry_id varchar(95) not null,
trigger_name varchar(200) not null,
trigger_group varchar(200) not null,
instance_name varchar(200) not null,
fired_time bigint(13) not null,
priority integer not null,
state varchar(16) not null,
job_name varchar(200) null,
job_group varchar(200) null,
is_nonconcurrent varchar(1) null,
requests_recovery varchar(1) null,
primary key (sched_name,entry_id)
)
COMMENT 'TIMER FIRED TRIGGERS';

CREATE TABLE twf_qrtz_timer_scheduler_state (
sched_name varchar(120) not null,
instance_name varchar(200) not null,
last_checkin_time bigint(13) not null,
checkin_interval bigint(13) not null,
primary key (sched_name,instance_name)
)
COMMENT 'TIMER SCHEDULER STATE';

CREATE TABLE twf_qrtz_timer_locks (
sched_name varchar(120) not null,
lock_name varchar(40) not null,
primary key (sched_name,lock_name)
)
COMMENT 'TIMER LOCKS';

create index idx_twf_qrtz_timer_j_req_recovery on twf_qrtz_timer_job_details(sched_name,requests_recovery);
create index idx_twf_qrtz_timer_j_grp on twf_qrtz_timer_job_details(sched_name,job_group);

create index idx_twf_qrtz_timer_t_j on twf_qrtz_timer_triggers(sched_name,job_name,job_group);
create index idx_twf_qrtz_timer_t_jg on twf_qrtz_timer_triggers(sched_name,job_group);
create index idx_twf_qrtz_timer_t_c on twf_qrtz_timer_triggers(sched_name,calendar_name);
create index idx_twf_qrtz_timer_t_g on twf_qrtz_timer_triggers(sched_name,trigger_group);
create index idx_twf_qrtz_timer_t_state on twf_qrtz_timer_triggers(sched_name,trigger_state);
create index idx_twf_qrtz_timer_t_n_state on twf_qrtz_timer_triggers(sched_name,trigger_name,trigger_group,trigger_state);
create index idx_twf_qrtz_timer_t_n_g_state on twf_qrtz_timer_triggers(sched_name,trigger_group,trigger_state);
create index idx_twf_qrtz_timer_t_next_fire_time on twf_qrtz_timer_triggers(sched_name,next_fire_time);
create index idx_twf_qrtz_timer_t_nft_st on twf_qrtz_timer_triggers(sched_name,trigger_state,next_fire_time);
create index idx_twf_qrtz_timer_t_nft_misfire on twf_qrtz_timer_triggers(sched_name,misfire_instr,next_fire_time);
create index idx_twf_qrtz_timer_t_nft_st_misfire on twf_qrtz_timer_triggers(sched_name,misfire_instr,next_fire_time,trigger_state);
create index idx_twf_qrtz_timer_t_nft_st_misfire_grp on twf_qrtz_timer_triggers(sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state);

create index idx_twf_qrtz_timer_ft_trig_inst_name on twf_qrtz_timer_fired_triggers(sched_name,instance_name);
create index idx_twf_qrtz_timer_ft_inst_job_req_rcvry on twf_qrtz_timer_fired_triggers(sched_name,instance_name,requests_recovery);
create index idx_twf_qrtz_timer_ft_j_g on twf_qrtz_timer_fired_triggers(sched_name,job_name,job_group);
create index idx_twf_qrtz_timer_ft_jg on twf_qrtz_timer_fired_triggers(sched_name,job_group);
create index idx_twf_qrtz_timer_ft_t_g on twf_qrtz_timer_fired_triggers(sched_name,trigger_name,trigger_group);
create index idx_twf_qrtz_timer_ft_tg on twf_qrtz_timer_fired_triggers(sched_name,trigger_group);


CREATE TABLE plt_extaobao_customer (
customerno  varchar(50) NOT NULL COMMENT '客户ID' ,
full_name  varchar(50) COMMENT '姓名' ,
sex  char(1) COMMENT '性别' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份' ,
birthday  date COMMENT '生日' ,
email  varchar(100) COMMENT '邮箱' ,
mobile  varchar(20) COMMENT '手机号码' ,
phone  varchar(50) COMMENT '座机号码' ,
zip  varchar(20) COMMENT '邮编' ,
country  varchar(50) COMMENT '国家' ,
state  varchar(50) COMMENT '省份' ,
city  varchar(50) COMMENT '城市（地级）' ,
district  varchar(100) COMMENT '区县（县级）' ,
address  varchar(255) COMMENT '地址' ,
changed  datetime COMMENT '客户属性变更时间（仅限统一客户信息表uni_customer中包含的属性）' ,
PRIMARY KEY (customerno)
)
COMMENT = '外部导入客户信息（来源于“淘宝”平台）';


CREATE TABLE plt_ext_batch_customer (
batch_id  int(11) NOT NULL COMMENT '导入批次ID' ,
plat_code  char(8) NOT NULL COMMENT '平台代码' ,
customerno  varchar(50) NOT NULL COMMENT '客户所属平台的客户ID' ,
PRIMARY KEY (batch_id, plat_code, customerno)
);


CREATE TABLE plt_ext_customer (
customerno  varchar(50) NOT NULL COMMENT '外部客户ID。导入非接驳平台客户时无需指定客户ID，因此本字段由统一用户ID生成规则生成，逻辑关联 uni_customer.uni_id' ,
full_name  varchar(50) COMMENT '姓名' ,
sex  char(1) COMMENT '性别' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份' ,
birthday  date COMMENT '生日' ,
email  varchar(100) COMMENT '邮箱' ,
mobile  varchar(20) COMMENT '手机号码' ,
phone  varchar(50) COMMENT '座机号码' ,
zip  varchar(20) COMMENT '邮编' ,
country  varchar(50) COMMENT '国家' ,
state  varchar(50) COMMENT '省份' ,
city  varchar(50) COMMENT '城市（地级）' ,
district  varchar(100) COMMENT '区县（县级）' ,
address  varchar(255) COMMENT '地址' ,
changed  datetime COMMENT '客户属性变更时间（仅限统一客户信息表uni_customer中包含的属性）' ,
PRIMARY KEY (customerno)
)
COMMENT = '外部导入客户信息（非接驳平台）';


CREATE TABLE plt_ext_import_batch (
batch_id  bigint(20) NOT NULL AUTO_INCREMENT ,
batch_name  varchar(50) NOT NULL COMMENT '导入批次名称' ,
plat_code  char(8) NOT NULL COMMENT '导入客户所属平台的平台代码，与uni_plat.plat_code关联' ,
file_name  varchar(255) COMMENT '文件名' ,
start_line  int(11) COMMENT '起始行' ,
internal_file_name  varchar(255) COMMENT '对应内部文件名' ,
has_column_name  tinyint(1) ,
split_char  varchar(20) COMMENT '分隔符' ,
import_table_name  varchar(50) COMMENT '导入数据的表名' ,
success_record  int(11) COMMENT '成功记录数' ,
total_record  int(11) COMMENT '总记录数' ,
has_clear  tinyint(1) ,
has_imported  tinyint(1) ,
created  datetime COMMENT '创建时间' ,
cleared  datetime COMMENT '清除时间' ,
creator  varchar(20) COMMENT '批次创建人的系统用户名，与tb_sysuser.username关联' ,
cleaner  varchar(20) COMMENT '批次清除人的系统用户名，与tb_sysuser.username关联' ,
PRIMARY KEY (batch_id)
)
COMMENT = '外部导入客户批次信息表';


CREATE TABLE plt_kfgzt_customer (
customerno  varchar(50) NOT NULL COMMENT '客户ID' ,
full_name  varchar(50) COMMENT '姓名' ,
sex  char(1) COMMENT '性别' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份' ,
birthday  date COMMENT '生日' ,
email  varchar(100) COMMENT '邮箱' ,
mobile  varchar(20) COMMENT '手机号码' ,
phone  varchar(50) COMMENT '座机号码' ,
zip  varchar(20) COMMENT '邮编' ,
country  varchar(50) COMMENT '国家' ,
state  varchar(50) COMMENT '省份' ,
city  varchar(50) COMMENT '城市（地级）' ,
district  varchar(100) COMMENT '区县（县级）' ,
address  varchar(255) COMMENT '地址' ,
changed  datetime COMMENT '客户属性变更时间（仅限统一客户信息表uni_customer中包含的属性）' ,
PRIMARY KEY (customerno)
)
COMMENT = '“客服工作台”同步客户信息';


CREATE TABLE plt_kfgzt_customer_label (
shop_name  varchar(50) NOT NULL COMMENT '店铺名（卖家昵称）' ,
customerno  varchar(50) NOT NULL COMMENT '客户id' ,
label_name  varchar(100) NOT NULL COMMENT '标签名称' ,
PRIMARY KEY (shop_name, customerno, label_name)
)
COMMENT = '“客服工作台”客户标签数据表';


CREATE TABLE plt_kfgzt_shop_label (
shop_name  varchar(100) NOT NULL COMMENT '店铺名（卖家昵称）' ,
label_id  bigint(20) NOT NULL COMMENT '自然主键' ,
label_name  varchar(100) COMMENT '标签名称' ,
label_explain  varchar(200) COMMENT '解释' ,
label_time  varchar(100) COMMENT '创建时间' ,
label_parent_id  bigint(20) COMMENT '父节点id(lable_id)' ,
is_parent  tinyint(1) ,
PRIMARY KEY (shop_name, label_id)
)
COMMENT = '“客服工作台”店铺签字典表';


CREATE TABLE plt_modify_customer (
customerno  varchar(64) NOT NULL COMMENT '客户ID, 手工修改的客户ID值与其uni_customer.uni_id一致, 有外键关联' ,
full_name  varchar(50) COMMENT '姓名' ,
sex  char(1) COMMENT '性别' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份' ,
birthday  date COMMENT '生日' ,
email  varchar(100) COMMENT '邮箱' ,
mobile  varchar(20) COMMENT '手机号码' ,
phone  varchar(50) COMMENT '座机号码' ,
zip  varchar(20) COMMENT '邮编' ,
country  varchar(50) COMMENT '国家' ,
state  varchar(50) COMMENT '省份' ,
city  varchar(50) COMMENT '城市（地级）' ,
district  varchar(100) COMMENT '区县（县级）' ,
address  varchar(255) COMMENT '地址' ,
changed  datetime COMMENT '客户属性变更时间（仅限统一客户信息表uni_customer中包含的属性）' ,
PRIMARY KEY (customerno)
)
COMMENT = '手工修改的统一客户信息表';


CREATE TABLE plt_modify_customer_log (
log_id  bigint(20) NOT NULL AUTO_INCREMENT ,
customerno  varchar(64) NOT NULL COMMENT '客户ID, 手工修改的客户ID值与其uni_customer.uni_id一致' ,
column_name  varchar(64) NOT NULL COMMENT '手工修改客户属性的数据库字段名, 可手工修改的字段名包括：full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address' ,
befor  varchar(255) COMMENT '修改前字段值' ,
after  varchar(255) COMMENT '修改后字段值' ,
sysuser_name  varchar(20) COMMENT '系统用户名，逻辑关联 tb_sysuser.username' ,
modified  datetime NOT NULL COMMENT '手工修改时间' ,
PRIMARY KEY (log_id)
)
COMMENT = '手工修改的统一客户信息表';


CREATE TABLE plt_wdzx_customer (
customerno  varchar(50) NOT NULL COMMENT '客户ID' ,
full_name  varchar(50) COMMENT '姓名' ,
sex  char(1) COMMENT '性别' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份' ,
birthday  date COMMENT '生日' ,
email  varchar(100) COMMENT '邮箱' ,
mobile  varchar(20) COMMENT '手机号码' ,
phone  varchar(50) COMMENT '座机号码' ,
zip  varchar(20) COMMENT '邮编' ,
country  varchar(50) COMMENT '国家' ,
state  varchar(50) COMMENT '省份' ,
city  varchar(50) COMMENT '城市（地级）' ,
district  varchar(100) COMMENT '区县（县级）' ,
address  varchar(255) COMMENT '地址' ,
changed  datetime COMMENT '客户属性变更时间（仅限统一客户信息表uni_customer中包含的属性）' ,
PRIMARY KEY (customerno)
)
COMMENT = '“我的中心”同步客户信息';


CREATE TABLE plt_wdzx_email_subscribe (
dp_id  varchar(50) NOT NULL COMMENT '店铺id' ,
customerno  varchar(50) NOT NULL COMMENT '用户昵称' ,
email  varchar(100) NOT NULL COMMENT '订阅邮箱' ,
dingyue_date  datetime COMMENT '订阅时间' ,
PRIMARY KEY (dp_id, customerno)
)
COMMENT = '“我的中心”店铺邮件订阅用户表';


CREATE TABLE plt_taobao_crm_member (
dp_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
customerno  varchar(50) NOT NULL COMMENT '买家昵称' ,
status  varchar(50) COMMENT '会员的状态，normal正常，delete被买家删除，blacklist黑名单' ,
grade  varchar(20) COMMENT '会员等级，1：普通会员，2：高级会员，3：vip会员，4：至尊vip' ,
trade_count  int(11) COMMENT '交易成功笔数' ,
trade_amount  double COMMENT '交易成功的金额' ,
last_trade_time  datetime COMMENT '最后交易时间' ,
PRIMARY KEY (dp_id, customerno),
KEY idx_plt_taobao_crm_member_4view (customerno, dp_id, last_trade_time)
)
COMMENT = '店铺买家会员信息表';


CREATE TABLE plt_taobao_customer (
customerno  varchar(50) NOT NULL COMMENT '用户id，CCMS系统用户ID,淘宝UID(number)' ,
full_name  varchar(50) COMMENT '客户姓名' ,
sex  char(1) COMMENT '性别。可选值:m(男),f(女)' ,
buyer_credit_lev  int(11) COMMENT '买家信用等级' ,
buyer_credit_score  int(11) COMMENT '买家信用评分' ,
buyer_credit_good_num  int(11) COMMENT '买家好评订单数' ,
buyer_credit_total_num  int(11) COMMENT '买家累计订单数' ,
zip  varchar(20) COMMENT '邮编' ,
address  varchar(255) COMMENT '地址' ,
city  varchar(50) COMMENT '城市' ,
state  varchar(50) COMMENT '省份' ,
country  varchar(100) COMMENT '国家' ,
district  varchar(100) COMMENT '区域' ,
created  datetime COMMENT '用户淘宝注册时间' ,
last_visit  datetime COMMENT '最后登陆时间' ,
birthday  date COMMENT '生日' ,
vip_info  varchar(20) COMMENT '用户的全站vip信息，可取值如下：c(普通会员),asso_vip(荣誉会员)，vip1,vip2,vip3,vip4,vip5,vip6(六个等级的正式vip会员)，共8种取值，其中asso_vip是由vip会员衰退而成，与主站上的vip0对应。' ,
email  varchar(100) COMMENT '用户email' ,
mobile  varchar(20) COMMENT '用户手机号' ,
phone  varchar(50) COMMENT '用户电话号码' ,
last_sync  datetime COMMENT '数据最后一次同步时间' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份，四位整数' ,
changed  datetime COMMENT '客户属性变更时间（仅限统一客户信息表uni_user中包含的属性）' ,
PRIMARY KEY (customerno),
KEY idx_plt_taobao_customer_last_sync (last_sync)
)
COMMENT = '客户信息表';


CREATE TABLE plt_taobao_order (
tid  varchar(50) NOT NULL COMMENT '订单号' ,
dp_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
customerno  varchar(50) NOT NULL COMMENT '客户ID' ,
created  datetime COMMENT '交易创建时间' ,
endtime  datetime COMMENT '交易结束时间' ,
status  varchar(50) NOT NULL COMMENT '交易状态,TRADE_NO_CREATE_PAY(没创建支付宝交易),WAIT_BUYER_PAY,WAIT_SELLER_SEND_GOODS,WAIT_BUYER_CONFIRM_GOODS,TRADE_BUYER_SIGNED(买家已签收,货到付款专用),TRADE_FINISHED,TRADE_CLOSED(付款以后用户退款成功，交易自动关闭),TRADE_CLOSED_BY_TAOBAO' ,
trade_from  varchar(50) COMMENT '交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)' ,
type  varchar(100) COMMENT '交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...' ,
pay_time  datetime COMMENT '付款时间' ,
total_fee  decimal(12,2) COMMENT '商品金额' ,
post_fee  decimal(12,2) COMMENT '邮费' ,
consign_time  datetime COMMENT '卖家发货时间' ,
ccms_order_status  smallint(6) NOT NULL ,
modified  datetime COMMENT '订单修改时间' ,
alipay_no  varchar(50) COMMENT '支付宝交易号，如：2009112081173831' ,
payment  decimal(12,2) NOT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分' ,
discount_fee  decimal(12,2) COMMENT '系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分' ,
point_fee  decimal(12,2) COMMENT '买家使用积分。格式:100;单位:个.' ,
real_point_fee  decimal(12,2) COMMENT '买家实际使用积分（扣除部分退款使用的积分）。格式:100;单位:个' ,
shipping_type  varchar(20) COMMENT '创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：ems, express, post, free, virtual。' ,
buyer_cod_fee  decimal(12,2) COMMENT '买家货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分' ,
seller_cod_fee  decimal(12,2) COMMENT '卖家货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分。卖家不承担服务费的订单：未发货的订单获取服务费为0，发货后就能获取到正确值。' ,
express_agency_fee  decimal(12,2) COMMENT '快递代收款。精确到2位小数;单位:元。如:212.07，表示:212元7分' ,
adjust_fee  decimal(12,2) COMMENT '卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样' ,
buyer_obtain_point_fee  decimal(12,2) COMMENT '买家获得积分,返点的积分。格式:100;单位:个。返点的积分要交易成功之后才能获得。' ,
cod_fee  decimal(12,2) COMMENT '货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分。' ,
cod_status  varchar(30) COMMENT '货到付款物流状态。 初始状态 NEW_CREATED,接单成功 CANCELED,接单失败 REJECTED_BY_COMPANY,接单超时 RECIEVE_TIMEOUT,揽收成功 TAKEN_IN_SUCCESS,揽收失败 TAKEN_IN_FAILED,揽收超时 RECIEVE_TIMEOUT,签收成功 SIGN_IN,签收失败 REJECTED_BY_OTHER_SIDE,订单等待发送给物流公司 WAITING_TO_BE_SENT,用户取消物流订单 CANCELED' ,
buyer_alipay_no  varchar(100) COMMENT '买家支付宝账号' ,
receiver_name  varchar(50) COMMENT '收货人的姓名' ,
receiver_state  varchar(50) COMMENT '收货人的所在省份' ,
receiver_city  varchar(50) COMMENT '收货人的所在城市' ,
receiver_district  varchar(50) COMMENT '收货人的所在地区' ,
receiver_address  varchar(255) COMMENT '收货人的详细地址' ,
receiver_zip  varchar(10) COMMENT '收货人的邮编' ,
receiver_mobile  varchar(20) COMMENT '收货人的手机号码' ,
receiver_phone  varchar(50) COMMENT '收货人的电话号码' ,
buyer_email  varchar(100) COMMENT '买家邮件地址' ,
commission_fee  decimal(12,2) COMMENT '交易佣金。精确到2位小数;单位:元。如:200.07，表示:200元7分' ,
refund_fee  decimal(12,2) COMMENT '子订单的退款金额合计' ,
num  int(11) COMMENT '商品数量总计' ,
received_payment  decimal(12,2) COMMENT '卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分' ,
PRIMARY KEY (tid),
KEY idx_plt_taobao_order_customerno (customerno),
KEY idx_plt_taobao_order_created (created, customerno, ccms_order_status, dp_id),
KEY idx_plt_taobao_order_pay_time (pay_time),
KEY idx_plt_taobao_order_consign_time (consign_time)
)
COMMENT = '订单表';


CREATE TABLE plt_taobao_order_item (
oid  varchar(50) NOT NULL COMMENT '子订单编号' ,
tid  varchar(50) NOT NULL COMMENT '订单编号' ,
dp_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
customerno  varchar(50) NOT NULL COMMENT '客户ID' ,
total_fee  decimal(12,2) COMMENT '应付金额' ,
discount_fee  decimal(12,2) COMMENT '订单优惠金额' ,
adjust_fee  decimal(12,2) COMMENT '手工调整金额' ,
payment  decimal(12,2) COMMENT '子订单实付金额' ,
status  varchar(50) NOT NULL COMMENT '订单状态' ,
num  int(11) COMMENT '购买数量' ,
num_iid  varchar(50) COMMENT '商品数字ID' ,
created  datetime COMMENT '交易创建时间' ,
endtime  datetime COMMENT '交易结束时间' ,
trade_from  varchar(50) COMMENT '交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)' ,
type  varchar(64) COMMENT '交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...' ,
pay_time  datetime COMMENT '付款时间' ,
consign_time  datetime COMMENT '卖家发货时间' ,
refund_status  varchar(50) COMMENT '退款状态。 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)' ,
refund_fee  decimal(12,2) COMMENT '退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分' ,
ccms_order_status  smallint(6) NOT NULL COMMENT 'CCMS自定义的订单状态' ,
title  varchar(255) NOT NULL COMMENT '商品标题' ,
modified  datetime COMMENT '订单修改时间（冗余字段）' ,
PRIMARY KEY (oid),
KEY idx_plt_taobao_order_item_tid (tid),
KEY idx_plt_taobao_order_item_num_iid (num_iid),
KEY idx_plt_taobao_order_item_created (created),
KEY idx_plt_taobao_order_item_pay_time (pay_time),
KEY idx_plt_taobao_order_item_consign_time (consign_time),
KEY idx_plt_taobao_order_item_modified (modified)
)
COMMENT = '订单明细表（子订单/商品明细）';


CREATE TABLE plt_taobao_product (
num_iid  varchar(50) NOT NULL COMMENT '商品数字ID' ,
detail_url  varchar(255) COMMENT '商品URL' ,
title  varchar(255) NOT NULL COMMENT '商品标题，商品名称' ,
created  datetime COMMENT 'Item的发布时间' ,
is_fenxiao  varchar(10) COMMENT '非分销商品：0，代销：1，经销：2' ,
cid  bigint(20) COMMENT '商品所属的叶子类目 id' ,
pic_url  varchar(255) COMMENT '商品图片地址' ,
list_time  datetime COMMENT '商品上架时间' ,
delist_time  datetime COMMENT '下架时间' ,
price  decimal(12,2) ,
modified  datetime COMMENT '修改时间' ,
approve_status  varchar(20) COMMENT '商品上传后的状态。onsale出售中，instock库中' ,
dp_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
outer_id  varchar(50) COMMENT '商家外部编码' ,
PRIMARY KEY (num_iid),
KEY idx_plt_taobao_product_title (title)
)
COMMENT = '淘宝商品表';


CREATE TABLE plt_taobao_order_type (
type  varchar(64) NOT NULL COMMENT '交易类型，可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...' ,
type_name  varchar(64) NOT NULL COMMENT '交易类型名称' ,
PRIMARY KEY (type)
)
COMMENT = '淘宝订单交易类型';


CREATE TABLE plt_taobao_product_prop (
num_iid  varchar(50) NOT NULL COMMENT '商品ID' ,
pid  varchar(150) NOT NULL COMMENT '属性ID' ,
prop_name  varchar(150) COMMENT '属性名' ,
vid  varchar(150) COMMENT '属性值ID' ,
name  varchar(150) COMMENT '属性值名称' ,
name_alias  varchar(150) COMMENT '属性值别名' ,
is_input_prop  tinyint(1) ,
PRIMARY KEY (num_iid, pid)
)
COMMENT = '商品对应的商品属性';


CREATE TABLE plt_taobao_product_seller_cat (
num_iid  varchar(50) NOT NULL COMMENT '商品编号' ,
seller_cid  varchar(40) NOT NULL COMMENT '卖家自定义类目编号' ,
name  varchar(50) COMMENT '卖家自定义类目名称' ,
type  varchar(20) COMMENT '店铺类目类型：可选值：manual_type：手动分类，new_type：新品上价， tree_type：二三级类目树 ，property_type：属性叶子类目树， brand_type：品牌推广' ,
PRIMARY KEY (num_iid, seller_cid)
)
COMMENT = '商品对应的卖家自定义类目';


CREATE TABLE plt_taobao_shipping (
order_code  varchar(50) NOT NULL COMMENT '物流订单编号' ,
tid  varchar(50) NOT NULL COMMENT '交易ID' ,
dp_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
is_quick_cod_order  tinyint(1) ,
seller_nick  varchar(50) COMMENT '卖家昵称' ,
buyer_nick  varchar(50) COMMENT '买家昵称' ,
delivery_end  datetime COMMENT '预约取货结束时间' ,
delivery_start  datetime COMMENT '预约取货开始时间' ,
out_sid  varchar(50) COMMENT '运单号.具体一个物流公司的运单号码.' ,
item_title  varchar(255) COMMENT '货物名称' ,
receiver_mobile  varchar(20) COMMENT '收件人手机号码' ,
receiver_name  varchar(50) COMMENT '收件人姓名' ,
receiver_phone  varchar(50) COMMENT '收件人电话' ,
status  char(20) COMMENT '物流订单状态,CREATED(已创建) RECREATED(重新创建) CANCELLED(订单已取消) CLOSED(订单关闭) SENDING(等候发送给物流公司) ACCEPTING(已发送给物流公司,等待接单) ACCEPTED(物流公司已接单) REJECTED(物流公司不接单) PICK_UP(物流公司揽收成功) PICK_UP_FAILED(物流公司揽收失败) LOST(物流公司丢单) REJECTED_BY_RECEIVER(拒签) ACCEPTED_BY_RECEIVER(已签收)' ,
type  char(10) COMMENT '物流方式.可选值:free(卖家包邮),post(平邮),express(快递),ems(EMS).' ,
freight_payer  char(10) COMMENT '谁承担运费.可选值:buyer(买家承担),seller(卖家承担运费).' ,
seller_confirm  tinyint(1) ,
company_name  varchar(200) COMMENT '物流公司名称' ,
is_success  tinyint(1) ,
created  datetime COMMENT '运单创建时间' ,
modified  datetime COMMENT '运单修改时间' ,
PRIMARY KEY (order_code),
KEY idx_plt_taobao_shipping_dp_id (dp_id),
KEY idx_plt_taobao_shipping_tid (tid)
)
COMMENT = '淘宝物流运单';



CREATE TABLE plt_taobao_traderate (
tid  varchar(50) NOT NULL COMMENT '交易ID' ,
oid  varchar(50) NOT NULL COMMENT '子订单ID' ,
dp_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
valid_score  tinyint(1) ,
role  varchar(10) COMMENT '评价者角色.可选值:seller(卖家),buyer(买家)' ,
nick  varchar(50) COMMENT '评价者昵称' ,
result  varchar(10) COMMENT '评价结果,可选值:good(好评),neutral(中评),bad(差评)' ,
created  datetime COMMENT '评价创建时间,格式:yyyy-MM-dd HH:mm:ss' ,
rated_nick  varchar(50) COMMENT '被评价者昵称' ,
item_title  varchar(120) COMMENT '商品标题' ,
item_price  decimal(12,2) COMMENT '商品价格,精确到2位小数;单位:元.如:200.07，表示:200元7分' ,
PRIMARY KEY (tid, oid)
)
COMMENT = '交易评价信息';


CREATE TABLE plt_taobao_item_cat (
cid  varchar(40) NOT NULL COMMENT '商品所属类目ID' ,
parent_cid  varchar(40) COMMENT '父类目ID=0时，代表的是一级的类目' ,
name  varchar(50) COMMENT '类目名称' ,
is_parent  tinyint(1) ,
status  varchar(20) COMMENT '状态。可选值:normal(正常),deleted(删除)' ,
sort_order  int(11) COMMENT '排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数' ,
PRIMARY KEY (cid)
)
COMMENT = '商品类目';


CREATE TABLE plt_taobao_item_prop (
pid  varchar(40) NOT NULL COMMENT '属性 ID 例：品牌的PID=20000' ,
parent_pid  varchar(40) COMMENT '上级属性ID' ,
parent_vid  varchar(40) COMMENT '上级属性值ID' ,
name  varchar(50) COMMENT '属性名' ,
is_input_prop  tinyint(1) ,
is_key_prop  tinyint(1) ,
is_sale_prop  tinyint(1) ,
is_color_prop  tinyint(1) ,
is_enum_prop  tinyint(1) ,
is_item_prop  tinyint(1) ,
must  tinyint(1) ,
multi  tinyint(1) ,
status  varchar(20) COMMENT '状态。可选值:normal(正常),deleted(删除)' ,
sort_order  int(11) COMMENT '排列序号。取值范围:大于零的整排列序号。取值范围:大于零的整数' ,
child_template  varchar(1024) NULL ,
is_allow_alias  tinyint(1) ,
dp_id  varchar(50) COMMENT '店铺ID，当属性为店铺自定义属性时（is_input_prop=true）有值，否则为Null' ,
PRIMARY KEY (pid)
)
COMMENT = '商品属性';


CREATE TABLE plt_taobao_item_prop_val (
cid  varchar(40) NOT NULL COMMENT '类目ID' ,
pid  varchar(150) NOT NULL COMMENT '属性 ID' ,
prop_name  varchar(150) COMMENT '属性名' ,
vid  varchar(150) NOT NULL COMMENT '属性值ID' ,
name  varchar(150) COMMENT '属性值' ,
name_alias  varchar(150) COMMENT '属性值别名' ,
is_parent  tinyint(1) ,
status  varchar(20) COMMENT '状态。可选值:normal(正常),deleted(删除)' ,
sort_order  int(11) COMMENT '排列序号。取值范围:大于零的整数' ,
PRIMARY KEY (cid, pid, vid)
)
COMMENT = '商品属性值';


CREATE TABLE plt_taobao_item_seller_cat (
seller_cid  varchar(40) NOT NULL COMMENT '卖家自定义类目编号' ,
parent_cid  varchar(40) COMMENT '父类目编号，值等于0：表示此类目为店铺下的一级类目，值不等于0：表示此类目有父类目' ,
name  varchar(50) COMMENT '卖家自定义类目名称' ,
pic_url  varchar(1024) COMMENT '链接图片地址' ,
sort_order  int(11) COMMENT '该类目在页面上的排序位置' ,
type  varchar(20) COMMENT '店铺类目类型：可选值：manual_type：手动分类，new_type：新品上价， tree_type：二三级类目树 ，property_type：属性叶子类目树， brand_type：品牌推广' ,
dp_id  varchar(50) COMMENT '卖家店铺ID' ,
PRIMARY KEY (seller_cid)
)
COMMENT = '卖家自定义类目';


CREATE TABLE plt_taobao_pay_type (
pay_id  bigint(20) NOT NULL ,
pay_type  varchar(20) COMMENT '支付类型' ,
pay_type_desc  varchar(40) COMMENT '备注' ,
PRIMARY KEY (pay_id)
)
COMMENT = '零售版支付类型的数据字典表';


CREATE TABLE plt_taobao_target_type (
target_id  bigint(20) NOT NULL ,
target_value  varchar(32) ,
target_desc  varchar(200) ,
PRIMARY KEY (target_id)
)
COMMENT = '订单查询的客户汇总指标字典表';


CREATE TABLE uni_customer (
uni_id  varchar(64) NOT NULL COMMENT '主键。客户全局统一ID，由客户统一ID生成规则生成' ,
full_name  varchar(50) COMMENT '姓名' ,
sex  char(1) COMMENT '性别，m : 男； f : 女' ,
job  varchar(50) COMMENT '职业' ,
birth_year  smallint(6) COMMENT '出生年份，四位整数' ,
birthday  date COMMENT '生日' ,
mobile  varchar(20) COMMENT '手机号' ,
email  varchar(100) COMMENT '电子邮件地址' ,
phone  varchar(50) COMMENT '座机号码' ,
zip  varchar(20) COMMENT '邮编' ,
country  varchar(50) COMMENT '国家' ,
state  varchar(50) COMMENT '省份' ,
city  varchar(50) COMMENT '城市（地级）' ,
district  varchar(100) COMMENT '区县（县级）' ,
address  varchar(255) COMMENT '地址' ,
PRIMARY KEY (uni_id)
)
COMMENT = '客户统一信息表';


CREATE TABLE uni_customer_plat (
uni_id  varchar(64) NOT NULL COMMENT '客户全局统一ID，关联uni_customer.uni_id' ,
plat_code  char(8) NOT NULL COMMENT '来源平台代码，关联uni_plat.plat_code' ,
customerno  varchar(64) NOT NULL COMMENT '客户在来源平台的客户ID，如：在taobao平台下为淘宝昵称、在paipai平台下为拍拍昵称' ,
plat_priority  smallint(6) NOT NULL COMMENT '客户所属来源平台的优先级。平台优先级是从0开始的自然数，0最高，数字越大优先级越低' ,
changed  datetime COMMENT '客户属性在平台内的变更时间' ,
PRIMARY KEY (plat_code, customerno),
KEY idx_uni_customer_plat_uni_id (uni_id),
KEY idx_uni_customer_plat_customerno (customerno)
)
COMMENT = '客户各平台ID关系表';


CREATE TABLE uni_plat (
plat_code  char(8) NOT NULL COMMENT '来源平台代码, 固定8位以内的字符， 如：taobao，jingdong 等' ,
parent_plat  char(8) COMMENT '当前平台客户的父平台代码。如：“我的中心”平台的父平台是淘宝平台' ,
plat_table  varchar(50) NOT NULL COMMENT '客户来源平台的数据库表名' ,
plat_priority  smallint(6) NOT NULL COMMENT '客户所属来源平台的优先级。平台优先级是从0开始的自然数，0最高，数字越大优先级越低' ,
sync_parent  tinyint(1) COMMENT '是否将当前平台存在而父平台不存在的客户同步插入到父平台的客户表中？' ,
enable_delete  tinyint(1) COMMENT '平台中的客户记录是否被允许删除？默认为不允许。当允许删除时，其对应的统一客户表和客户关系表的记录也通过触发器一并被删除' ,
plat_desc  varchar(64) COMMENT '客户来源平台的描述' ,
PRIMARY KEY (plat_code)
)
COMMENT = '客户来源平台信息';


CREATE TABLE twf_log_node_mids (
  job_id bigint(20) NOT NULL,
  table_view_name varchar(50) COLLATE utf8_bin NOT NULL COMMENT '表或视图名',
  table_view_type varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '表或视图类型',
  created_time datetime DEFAULT NULL,
  KEY idx_log_node_mids_job_id_table_view_name (job_id,table_view_name) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='流程执行中间表列表';


CREATE TABLE tb_sys_taobao_user (
  id bigint(20) NOT NULL COMMENT '主键,使用tb_sysuser表的id',
  plat_user_id varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的用户id',
  plat_user_name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的用户名',
  is_subuser tinyint(1) DEFAULT NULL COMMENT '是否是子账号',
  plat_shop_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的店铺的id',
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_plat_user_id (plat_user_id) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 模块
CREATE TABLE module_type(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	key_name VARCHAR(255) COLLATE utf8_bin NOT NULL COMMENT '模块名(英文)',
	name VARCHAR(255) NOT NULL COMMENT '模块名',
	name_plus VARCHAR(50) COMMENT '名称附注(内部用)',
	url VARCHAR(255) COLLATE utf8_bin DEFAULT NULL COMMENT '点击时跳转的地址',
	data_url VARCHAR(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求数据的地址',
	tip VARCHAR(255) COMMENT '提示文案',
	lowest_edition_required INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '最低版本要求',
	support_ops_mask INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '支持的操作',
	memo VARCHAR(255) COMMENT '备注',
	PRIMARY KEY (id)
)COMMENT '模块类型';

CREATE TABLE module(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	key_name varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '模块名(英文)',
	module_type_id BIGINT UNSIGNED NOT NULL COMMENT '模块类型id',
	container_module_id BIGINT COMMENT '外围模块id',
	url VARCHAR(255) ,
	data_url VARCHAR(255) COMMENT '数据的请求地址',
	name VARCHAR(30) COMMENT '名字,可用于展示',
	tip VARCHAR(30) COMMENT '提示文案',
	lowest_edition_required INT UNSIGNED COMMENT '最低版本要求',
	support_ops_mask INT UNSIGNED COMMENT '支持的操作',
	ranking float DEFAULT NULL COMMENT '顺序',
	memo VARCHAR(255) COMMENT '备注',
	PRIMARY KEY (id)
)COMMENT '模块';

CREATE TABLE module_entry (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  module_id bigint DEFAULT NULL COMMENT '模块id',
  permission_id bigint unsigned DEFAULT NULL COMMENT '权限id',
  role_id bigint unsigned DEFAULT NULL COMMENT '角色id',
  user_id bigint unsigned DEFAULT NULL COMMENT '用户id',
  support_ops_mask int unsigned NOT NULL COMMENT '授权结果',
  memo varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) COMMENT '模块准入规则';

-- 

CREATE TABLE tds_order_status (
	status_id  smallint(6) NOT NULL ,
	status_value  varchar(40) ,
	status_name  varchar(40) ,
	orderid  smallint(6) ,
	PRIMARY KEY (status_id)
)
COMMENT = 'CCMS订单状态维表';


CREATE TABLE tw_mobile_blacklist (
mobile  varchar(20) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源' ,
created  datetime NULL COMMENT '录入时间' ,
PRIMARY KEY (mobile)
)
COMMENT = '短信手机黑名单表';


CREATE TABLE tw_mobile_redlist (
mobile  varchar(20) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源' ,
created  datetime NULL COMMENT '录入时间' ,
PRIMARY KEY (mobile)
)
COMMENT = '手机短信红名单表';


CREATE TABLE tw_email_blacklist (
email  varchar(100) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源',
created  datetime NULL COMMENT '录入时间',
PRIMARY KEY (email)
)
COMMENT = 'EDM黑名单表';


-- 会员黑名单表
CREATE TABLE tw_member_blacklist (
  customerno varchar(50)  NOT NULL ,
  get_from varchar(20)  NULL COMMENT '名单来源',
  created datetime  NULL COMMENT '录入时间',
  PRIMARY KEY (customerno)
)
COMMENT='会员黑名单表';


CREATE TABLE tw_email_redlist (
email  varchar(100) NOT NULL ,
get_from  varchar(20) NULL COMMENT '名单来源',
created  datetime NULL COMMENT '录入时间',
PRIMARY KEY (email)
)
COMMENT = 'EDM红名单表';


CREATE TABLE twf_node_evaluate (
  node_id bigint(20) NOT NULL COMMENT '评估节点ID',
  node_name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '评估节点名称',
  evaluate_cycle int(11) NOT NULL COMMENT '评估周期（1天~7天）',
  shop_id varchar(200) COLLATE utf8_bin NOT NULL COMMENT '评估的店铺',
  created datetime NOT NULL,
  PRIMARY KEY (node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE evaluate_report_result (
  evaluate_result_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表结果id(逻辑主键ID)',
  job_id bigint(20) NOT NULL COMMENT 'jobId对应哪个执行批次',
  node_id bigint(20) NOT NULL COMMENT '评估的渠道节点ID',
  evaluate_time varchar(50) COLLATE utf8_bin NOT NULL,
  buy_order_count bigint(12) DEFAULT NULL COMMENT '拍下订单数',
  buy_customer_count bigint(12) DEFAULT NULL COMMENT '拍下人数',
  buy_payment_sum double(12,2) DEFAULT NULL COMMENT '拍下金额',
  pay_order_count bigint(12) DEFAULT NULL COMMENT '付款订单数',
  pay_customer_count bigint(12) DEFAULT NULL COMMENT '付款客户数',
  pay_payment_sum double(12,2) DEFAULT NULL COMMENT '付款金额数',
  customer_avg_fee double(12,2) DEFAULT NULL COMMENT '客单价',
  product_count bigint(12) DEFAULT NULL COMMENT '购买商品数',
  roi varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '投资回报率',
  PRIMARY KEY (evaluate_result_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



CREATE TABLE evaluate_report_day_result (
  evaluate_result_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表结果id(逻辑主键ID)',
  job_id bigint(20) NOT NULL COMMENT 'jobId对应哪个执行批次',
  node_id bigint(20) NOT NULL COMMENT '评估的渠道节点ID',
  evaluate_time varchar(50) COLLATE utf8_bin NOT NULL,
  buy_order_count bigint(12) DEFAULT NULL COMMENT '拍下订单数',
  buy_customer_count bigint(12) DEFAULT NULL COMMENT '拍下人数',
  buy_payment_sum double(12,2) DEFAULT NULL COMMENT '拍下金额',
  pay_order_count bigint(12) DEFAULT NULL COMMENT '付款订单数',
  pay_customer_count bigint(12) DEFAULT NULL COMMENT '付款客户数',
  pay_payment_sum double(12,2) DEFAULT NULL COMMENT '付款金额数',
  customer_avg_fee double(12,2) DEFAULT NULL COMMENT '客单价',
  product_count bigint(12) DEFAULT NULL COMMENT '购买商品数',
  roi varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '投资回报率',
  PRIMARY KEY (evaluate_result_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



CREATE TABLE evaluate_report_total_result (
  evaluate_result_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表结果id(逻辑主键ID)',
  job_id bigint(20) NOT NULL COMMENT 'jobId对应哪个执行批次',
  node_id bigint(20) NOT NULL COMMENT '评估的渠道节点ID',
  pay_customer_count bigint(12) DEFAULT NULL COMMENT '付款客户数',
  pay_payment_sum double(12,2) DEFAULT NULL COMMENT '付款金额数',
  product_count bigint(12) DEFAULT NULL COMMENT '购买商品数',
  PRIMARY KEY (evaluate_result_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;




CREATE TABLE twf_node_evaluate_customer_detail (
  evaluate_customer_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表客户明细主键',
  job_id bigint(20) DEFAULT NULL COMMENT '执行ID',
  node_id bigint(20) DEFAULT NULL COMMENT '节点ID',
  evaluate_time date DEFAULT NULL COMMENT '评估时间',
  uni_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '平台唯一标识',
  full_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  sex varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  birthday date DEFAULT NULL COMMENT '生日',
  state varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  city varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  district varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区县',
  vip_info varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'vip信息',
  mobile varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  email varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'email',
  buyer_credit_lev varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家信用等级',
  good_rate varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '买家好评率',
  address varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地址',
  zip varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '邮编',
  job varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '职业',
  PRIMARY KEY (evaluate_customer_id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 评估节点的主订单信息(非订单明细)
CREATE TABLE twf_node_evaluate_order_detail (
  evaluate_order_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表订单(明细)主键',
  job_id bigint(20) DEFAULT NULL,
  node_id bigint(20) DEFAULT NULL,
  evaluate_time date DEFAULT NULL,
  tid varchar(50) COLLATE utf8_bin DEFAULT NULL,
  customerno varchar(50) COLLATE utf8_bin DEFAULT NULL,
  created datetime DEFAULT NULL,
  pay_time datetime DEFAULT NULL,
  consign_time datetime DEFAULT NULL,
  total_fee decimal(12,2) DEFAULT NULL,
  payment decimal(12,2) DEFAULT NULL,
  status varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (evaluate_order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE twf_node_evaluate_product_detail (
  evaluate_product_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表商品明细主键',
  job_id bigint(20) DEFAULT NULL COMMENT '执行jobId',
  node_id bigint(20) DEFAULT NULL COMMENT '节点ID',
  evaluate_time date DEFAULT NULL COMMENT '评估时间',
  product_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品ID',
  product_name varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  customer_count int(11) DEFAULT NULL COMMENT '购买客户数',
  order_count int(11) DEFAULT NULL COMMENT '订单数',
  buy_num int(11) DEFAULT NULL COMMENT '购买数量',
  buy_fee decimal(12,2) DEFAULT NULL COMMENT '购买金额',
  outer_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家编码',
  PRIMARY KEY (evaluate_product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='评估节点_商品明细';


CREATE TABLE plt_taobao_shop (
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  shop_name varchar(100) COLLATE utf8_bin NOT NULL COMMENT '店铺名称，即店主用户昵称',
  shop_type char(5) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺类型，B天猫店，C淘宝店',
  order_created_earliest datetime DEFAULT NULL,
  order_created_latest datetime DEFAULT NULL,
  acookie_visit_date date DEFAULT NULL,
  PRIMARY KEY (shop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝店铺信息表';


CREATE TABLE plt_taobao_coupon (
  coupon_id bigint(20) NOT NULL,
  coupon_name varchar(50) COLLATE utf8_bin NOT NULL COMMENT '优惠券名',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺',
  created_time datetime DEFAULT NULL COMMENT '创建时间',
  start_time datetime DEFAULT NULL COMMENT '生效日期',
  end_time datetime NOT NULL COMMENT '截止日期',
  threshold int(11) NOT NULL COMMENT '优惠券消费门槛(元)',
  denomination_value smallint(6) NOT NULL COMMENT '面额',
  creator int(11) DEFAULT NULL,
  enable tinyint(1) DEFAULT NULL COMMENT '优惠券是否启动',
  remark varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝优惠券信息表';


CREATE TABLE plt_taobao_coupon_denomination (
  denomination_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '面额名称',
  denomination_value smallint(6) NOT NULL COMMENT '面额值',
  PRIMARY KEY (denomination_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝优惠券面额字典表';


CREATE TABLE twf_node_coupon (
  node_id bigint(20) NOT NULL COMMENT '节点ID',
  channel_id bigint(20) NOT NULL  COMMENT '发送渠道ID',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  coupon_id bigint(20) NOT NULL COMMENT '优惠券ID',
  preview_customers TEXT COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户',
  output_control varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '输出控制；1：发送组中发送成功客户和控制组客户 2：全量客户',
  remark varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券节点配置';



-- 个性化包裹
-- start 
CREATE TABLE rc_plan_group(
	shop_id VARCHAR(50) NOT NULL COMMENT '店铺id',
	sign VARCHAR(100) NOT NULL DEFAULT '' COMMENT '备注的签名',
	PRIMARY KEY (shop_id)
);


CREATE TABLE rc_plan(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(50) NOT NULL COMMENT '方案名',
	position INT NOT NULL COMMENT '优先级顺序位置',
	active BOOL NOT NULL DEFAULT '0' COMMENT '是否已启动',
	plan_group_id VARCHAR(50) NOT NULL COMMENT '所属的方案组id',
	start_time DATETIME COMMENT '最后一次开启的时间',
	last_config_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次修改配置的时间',
	PRIMARY KEY (id)
);


CREATE TABLE rc_rule(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(50) NOT NULL DEFAULT '' ,
	position INT NOT NULL COMMENT '优先级顺序',
	plan_id BIGINT UNSIGNED NOT NULL COMMENT '所属的方案的id',
	remark_content VARCHAR(255) NOT NULL DEFAULT '' COMMENT '要在备注上添加的内容',
	last_config_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次修改配置的时间',
	PRIMARY KEY (id)
);


CREATE TABLE rc_condition(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	name VARCHAR(50) NOT NULL ,
	rule_id BIGINT NOT NULL COMMENT '所属的规则id',
	position INT NOT NULL COMMENT '在规则的条件列表中的顺序',
	relation VARCHAR(10) NOT NULL COMMENT '与其他条件之间的关系',
	type VARCHAR(20) NOT NULL COMMENT '基于客户,基于订单',
	property_id BIGINT NOT NULL COMMENT '指标id,是tm_db_column表的column_id',
	condition_op_name VARCHAR(10) NOT NULL COMMENT '指标比较符',
	reference_value VARCHAR(1000) NOT NULL COMMENT '参考值',
	last_config_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次修改配置的时间',
	PRIMARY KEY (id)
);

SET FOREIGN_KEY_CHECKS=1;
