--// Bootstrap.sql

-- This is the only SQL script file that is NOT
-- a valid migration and will not be run or tracked
-- in the changelog.  There is no @UNDO section.

--// Do I need this file?

-- New projects likely won't need this file.
-- Existing projects will likely need this file.
-- It's unlikely that this bootstrap should be run
-- in the production environment.

--// Purpose

-- The purpose of this file is to provide a facility
-- to initialize the database to a state before MyBatis
-- SQL migrations were applied.  If you already have
-- a database in production, then you probably have
-- a script that you run on your developer machine
-- to initialize the database.  That script can now
-- be put in this bootstrap file (but does not have
-- to be if you are comfortable with your current process.

--// Running

-- The bootstrap SQL is run with the "migrate bootstrap"
-- command.  It must be run manually, it's never run as
-- part of the regular migration process and will never
-- be undone. Variables (e.g. ${variable}) are still
-- parsed in the bootstrap SQL.

-- After the boostrap SQL has been run, you can then
-- use the migrations and the changelog for all future
-- database change management.

###-----------------------------------------------------------------------------------------------------------------------------
###  1_create_db.sql
###-----------------------------------------------------------------------------------------------------------------------------
--
-- Definition for database ccms
--
-- DROP DATABASE IF EXISTS ccms;
-- CREATE DATABASE IF NOT EXISTS ccms
-- CHARACTER SET utf8
-- COLLATE utf8_bin;

-- CREATE USER
-- FLUSH PRIVILEGES;
-- CREATE USER ccms IDENTIFIED BY 'ccms';
GRANT ALL PRIVILEGES ON *.* TO ccms@'%'  IDENTIFIED BY 'ccms' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO ccms@'localhost' IDENTIFIED BY 'ccms' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO ccms@'127.0.0.1'  IDENTIFIED BY 'ccms' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

--
-- Set default database
--
-- USE ccms;


###-----------------------------------------------------------------------------------------------------------------------------
###  2_create_tables_views.sql
###-----------------------------------------------------------------------------------------------------------------------------

#-------------------------------------------------------------------------------------------------------------------------------
#  建表及字段索引脚本
#-------------------------------------------------------------------------------------------------------------------------------
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
password  varchar(100) NOT NULL DEFAULT '' COMMENT '用户登录密码' ,
real_name  varchar(50) COMMENT '用户真实姓名' ,
mobile  varchar(20) COMMENT '用户手机号码' ,
email  varchar(100) ,
disabled  varchar(10) ,
PRIMARY KEY (id) ,
UNIQUE INDEX uk_tb_sysuser_login_name_usertype (login_name, user_type)
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
PRIMARY KEY (user_id,module_id)
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

-- metadata end.


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


CREATE TABLE plt_taobao_shop (
shop_id  varchar(50) NOT NULL COMMENT '店铺ID' ,
shop_name  varchar(100) NOT NULL COMMENT '店铺名称，即店主用户昵称' ,
order_created_earliest  datetime COMMENT '店铺最早订单时间' ,
order_created_latest  datetime COMMENT '店铺最新订单时间' ,
PRIMARY KEY (shop_id)
)
COMMENT = '淘宝店普信息表';


CREATE TABLE plt_taobao_coupon (
  coupon_id bigint(20) NOT NULL,
  coupon_name varchar(50) COLLATE utf8_bin NOT NULL COMMENT '优惠券名',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺',
  created_time datetime DEFAULT NULL COMMENT '创建时间',
  start_time datetime DEFAULT NULL COMMENT '生效日期',
  end_time datetime NOT NULL COMMENT '截止日期',
  threshold int(11) NOT NULL COMMENT '优惠券消费门槛(元)',
  denomination_id smallint(6) NOT NULL COMMENT '面额',
  creator int(11) DEFAULT NULL,
  enable tinyint(1) DEFAULT NULL COMMENT '优惠券是否启动',
  remark varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝优惠券信息表';


CREATE TABLE plt_taobao_coupon_denomination (
  denomination_id smallint(6) NOT NULL COMMENT '面额ID',
  denomination_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '面额名称',
  denomination_value smallint(6) DEFAULT NULL COMMENT '面额值',
  PRIMARY KEY (denomination_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝优惠券面额字典表';


CREATE TABLE twf_node_coupon (
  node_id bigint(20) NOT NULL COMMENT '节点ID',
  channel_id bigint(20) NOT NULL  COMMENT '发送渠道ID',
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  coupon_id bigint(20) NOT NULL COMMENT '优惠券ID',
  preview_customers TINYTEXT COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户',
  output_control varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '输出控制；1：发送组中发送成功客户和控制组客户 2：全量客户',
  remark varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券节点配置';


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


-- 创建 twf_log_node_mids  start
CREATE TABLE twf_log_node_mids (
  job_id bigint(20) NOT NULL,
  table_view_name varchar(50) COLLATE utf8_bin NOT NULL COMMENT '表或视图名',
  table_view_type varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '表或视图类型',
  created_time datetime DEFAULT NULL,
  KEY idx_log_node_mids_job_id_table_view_name (job_id,table_view_name) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='流程执行中间表列表' ;
-- 创建 twf_log_node_mids  end


CREATE TABLE tb_sys_taobao_user (
  id bigint(20) NOT NULL ,
  plat_user_id varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的用户id',
  plat_user_name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的用户名',
  is_subuser tinyint(1) DEFAULT NULL COMMENT '是否是子账号',
  plat_shop_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的店铺的id',
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_plat_user_id (plat_user_id) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ;


-- 模块
CREATE TABLE module_type (
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
) COMMENT '模块类型';

CREATE TABLE module (
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
) COMMENT '模块';

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
-- 模块结束

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


CREATE TABLE twf_node_evaluate_order_detail (
  evaluate_order_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评估报表订单明细主键',
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


SET FOREIGN_KEY_CHECKS=1;

###-----------------------------------------------------------------------------------------------------------------------------
### 3. view sql
###-----------------------------------------------------------------------------------------------------------------------------

###--------------------------------------------------------------------------------------------------------------------
###   View: vw_taobao_customer
###   淘宝客户信息统一视图(基础版)
###--------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM = MERGE  VIEW vw_taobao_customer AS
SELECT
    u.uni_id          AS uni_id,    #客户统一ID
    taobao.customerno AS customerno ,  #TODO:所有视图中的客户ID字段必须统一命名成customerno
    u.full_name       AS full_name, #客户姓名
    u.sex AS sex,  #性别
    u.job AS job,  #职业
    YEAR(CURRENT_DATE)-u.birth_year AS age,  #年龄
    u.birthday, #生日
    u.email email,
    u.mobile mobile,
    u.state state, #省份
    u.city city,     #城市
    u.district,      #区域
    u.address,     #地址
    u.zip,            #邮编
    CASE WHEN u.mobile REGEXP '^(1[3,4,5,8]){1}[[:digit:]]{9}$' THEN '1' ELSE '0' END AS is_mobile_valid, #手机号是否有效
    CASE WHEN u.email REGEXP '[A-Za-z0-9._-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,4}$' THEN '1' ELSE '0' END AS is_email_valid, #email是否有效
    taobao.vip_info,              #客户全站等级
    taobao.buyer_credit_lev, #买家信用等级
    taobao.created,              #淘宝用户注册时间
    round (CASE WHEN (taobao.buyer_credit_total_num > 0)
           THEN ((taobao.buyer_credit_good_num*1.0 / taobao.buyer_credit_total_num) * 100)
           ELSE NULL END, 1)                       AS buyer_good_ratio,  #买家好评率
    DATE_FORMAT(CURRENT_DATE, '%Y年%m月%d日')      AS ymd,               #当天年月日
    DATE_FORMAT(CURRENT_DATE, '%Y年%m月')          AS ym,                #当天年月
    crm.grade AS grade,									#会员等级
    IFNULL(label.label_name, '') AS　label_name			#客户标签
FROM  plt_taobao_customer taobao
    Left Join uni_customer_plat cp On cp.customerno = taobao.customerno And cp.plat_code = 'taobao'
    Left Join uni_customer u On u.uni_id = cp.uni_id
    Left Join plt_taobao_crm_member crm on taobao.customerno = crm.customerno
    Left Join plt_kfgzt_customer_label label on taobao.customerno = label.customerno;


###--------------------------------------------------------------------------------------------------------------------
###   View: vm_taobao_order
###   订单信息视图(基础版)
###--------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM = MERGE VIEW  vm_taobao_order AS
  SELECT
    plt_taobao_order.tid,
    plt_taobao_order.customerno,
    plt_taobao_order.receiver_name,
    plt_taobao_order.created,
    plt_taobao_order.pay_time,
    plt_taobao_order.consign_time,
    plt_taobao_shop.shop_name,
    plt_taobao_order.payment,
    plt_taobao_order.post_fee ,
    plt_taobao_order.receiver_mobile,
    plt_taobao_order.buyer_alipay_no,
    plt_taobao_order.buyer_email,
    plt_taobao_order.total_fee,
    tds_order_status.status_name as status,
    b.mobile as buyer_mobile,
    s.out_sid  as out_sid,
    s.company_name as company_name,
    s.created  as shipping_created
FROM plt_taobao_order
    join plt_taobao_shop on dp_id = shop_id left
    join tds_order_status on status_id = ccms_order_status
    join plt_taobao_customer b on plt_taobao_order.customerno = b.customerno
    left join plt_taobao_shipping s on plt_taobao_order.tid= s.tid;

###-----------------------------------------------------------------------------------------------------------------------------
###  4. functions
###-----------------------------------------------------------------------------------------------------------------------------

CREATE  function  func_get_dic_viewname(p_dic_type_id int, p_type_value varchar (32))
	RETURNS  varchar(64)  CHARSET utf8	DETERMINISTIC
	COMMENT  '返回元数据字典项显示值\r\n参数: 字典ID, 字典项值'
BEGIN
DECLARE  v_vn VARCHAR(64);
	select show_name into v_vn from tm_dic_value where dic_id = p_dic_type_id  and dic_value = p_type_value  limit 1;
	RETURN  v_vn;
END ;


###-----------------------------------------------------------------------------------------------------------------------------
###  5. procs
###-----------------------------------------------------------------------------------------------------------------------------
CREATE PROCEDURE proc_dedup_customer(IN p_plat_code char(8), IN p_parent_plat char(8), IN p_plat_priority smallint, IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime, OUT p_isdup boolean)
    DETERMINISTIC
	COMMENT '统一客户信息时执行的客户去重过程，需遵循 Dedup Rules，返回当前客户是否重复的判断结果'
BEGIN
  DECLARE v_uni_id varchar(64); #统一客户ID
  Set p_isdup = false; #是否重复客户

    #客户去重规则 --------------------------------------------------------------------------------------
    # DedupRule 1：如果当前客户的平台昵称存与本平台或者其父平台的一个客户一致，即可认为两个客户是相同的一个人
    # DedupRule 2：(TODO: 后继需加入更多dedup规则)
    #--------------------------------------------------------------------------------------------------

    #1.使用Dedup规则，判断当前客户是否为重复客户
    select uni_id into v_uni_id from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno limit 1; #检查当前客户与本平台关系
    IF (v_uni_id IS NOT NULL) THEN # 当前客户与本平台关系存在
        update uni_customer_plat set  changed = p_changed where plat_code = p_plat_code and customerno = p_customerno; #更新客户属性变更时间
        Set p_isdup = true; #客户被判定为重复
    ELSE
       #若当前客户与本平台关系不存在
       select uni_id into v_uni_id from uni_customer_plat where plat_code = p_parent_plat and customerno = p_customerno limit 1; #检查当前客户与父平台关系
       IF (v_uni_id IS NOT NULL) THEN #若有父平台客户与当前客户一致
           Set p_isdup = true; #客户被判定为重复
           #取父平台的统一客户ID作为当前客户的ID，并记录当前客户-当前平台关系
           insert into uni_customer_plat (uni_id, plat_code, customerno, plat_priority, changed)
                              values (v_uni_id, p_plat_code, p_customerno, p_plat_priority, p_changed);
       END IF;
    END IF;

    #2.若该客户是新的客户（非重复客户），则新增统一客户信息并记录客户平台关系表
    IF (!p_isdup) THEN
        begin
            #记录新统一客户信息
            Set v_uni_id = CONCAT(p_plat_code, '|', p_customerno); #以"平台代码 +竖线 + 平台内的客户编码"作为规则生成生成统一客户ID
            insert into uni_customer (uni_id, full_name, sex, job, birth_year, birthday, email, mobile, phone,
                                  zip, country, state, city, district, address)
                          values (v_uni_id, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                                  p_zip, p_country, p_state, p_city, p_district, p_address);
            #记录当前客户-当前平台关系
            insert into uni_customer_plat (uni_id, plat_code, customerno, plat_priority, changed)
                               values (v_uni_id, p_plat_code, p_customerno, p_plat_priority, p_changed);
        end;
    END IF;

    #RETURN v_isdup;
END ;


CREATE PROCEDURE proc_delete_unify_customer(IN p_plat_code char(8), IN p_customerno varchar(50))
    DETERMINISTIC
	COMMENT '删除统一客户信息'
BEGIN
  Delete from uni_customer where uni_id =
      (select uni_id from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno );
END ;


CREATE PROCEDURE proc_merge_customer(IN p_plat_code char(8), IN p_parent_plat char(8), IN p_plat_table varchar(50), IN p_plat_priority smallint, IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    DETERMINISTIC
	COMMENT '统一客户信息时执行的客户信息合并过程，需遵循Merge Rules'
BEGIN
  #DECLARE rec_customer_plat record; #客户和平台的关系信息
  #DECLARE rec_customer record; #平台相关的客户信息

  DECLARE v_plat_code char(8);
  DECLARE v_customerno varchar(50);
  DECLARE v_plat_table varchar(50);
  DECLARE v_uni_id varchar(64); #当前客户的统一ID

  DECLARE v_full_name varchar(50);
  DECLARE v_sex char(1);
  DECLARE v_job varchar(50);
  DECLARE v_birth_year smallint;
  DECLARE v_birthday date;
  DECLARE v_email varchar(100);
  DECLARE v_mobile varchar(20);
  DECLARE v_phone varchar(50);
  DECLARE v_zip varchar(20);
  DECLARE v_country varchar(50);
  DECLARE v_state varchar(50);
  DECLARE v_city varchar(50);
  DECLARE v_district varchar(100);
  DECLARE v_address varchar(255);

  DECLARE stmt varchar(1024);
  DECLARE v_isLastrow boolean DEFAULT FALSE; #define the flag for loop judgement
  DECLARE rowCursor Cursor For #define the cursor
          Select plat_code, customerno from uni_customer_plat where uni_id = v_uni_id order by plat_priority desc, IFNULL(changed, '0001-01-01'); #平台优先级第一序，变更时间第二序
  DECLARE Continue Handler for NOT FOUND set v_isLastrow = true; #define the continue handler for not found flag

    #客户合并规则 -------------------------------------------------------------------------------------
    # MergeRule 1：根据平台客户信息的平台优先级叠加和覆盖统一客户信息，优先级高的平台覆盖优先级低的平台(plat_proority)
    # MergeRule 2：根据平台客户信息更新时间的先后叠加和覆盖统一客户信息，新的变更覆盖旧的变更(changed)
    # MergeRule 3：(TODO: 将来可能会加入更多merge规则)
    #--------------------------------------------------------------------------------------------------

  select uni_id into v_uni_id from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno;

  #1.客户信息合并
  Open rowCursor; #开始打开游标
      rowLoop:Loop
          Fetch rowCursor Into v_plat_code, v_customerno;
          IF v_isLastrow THEN
             Leave rowLoop;
          ELSE
              Begin
                  Select plat_table into v_plat_table from uni_plat where plat_code = v_plat_code; #取当前平台表名
                  IF (p_plat_code = v_plat_code) THEN #若合并的来源平台与当前平台相同，则直接取触发器相应的客户记录
                      Set @full_name  = p_full_name;
                      Set @sex        = p_sex;
                      Set @job        = p_job;
                      Set @birth_year = p_birth_year;
                      Set @birthday   = p_birthday;
                      Set @email      = p_email;
                      Set @mobile     = p_mobile;
                      Set @phone      = p_phone;
                      Set @zip        = p_zip;
                      Set @country    = p_country;
                      Set @state      = p_state;
                      Set @city       = p_city;
                      Set @district   = p_district;
                      Set @address    = p_address;
                  ELSE # 否则到具体的平台表中取客户信息记录
                      /* Mysql触发器中动态SQL不可用，以静态SQL实现其功能
                      Set stmt = CONCAT('select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address '
                                       ,' Into @full_name,@sex,@job,@birth_year,@birthday,@email,@mobile,@phone,@zip,@country,@state,@city,@district,@address '
                                       ,' From ', v_plat_table, ' where customerno = ''' , v_customerno, '''');
                      EXECUTE stmt;
                      */
                      BEGIN
                          CASE v_plat_code
                              WHEN 'taobao'   THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_taobao_customer where customerno = v_customerno;
                              WHEN 'wdzx'     THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_wdzx_customer where customerno = v_customerno;
                              WHEN 'kfgzt'    THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_kfgzt_customer where customerno = v_customerno;
                              WHEN 'ext'      THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_ext_customer where customerno = v_customerno;
                              WHEN 'extaobao' THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_extaobao_customer where customerno = v_customerno;
                              WHEN 'modify'   THEN Select full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address
                                                   Into   @full_name, @sex, @job, @birth_year, @birthday, @email, @mobile, @phone, @zip, @country, @state, @city, @district, @address
                                                   From plt_modify_customer where customerno = v_customerno;
                          END CASE;
                      END;
                  END IF;

                  Set v_full_name = IFNULL(@full_name, v_full_name); #null字段不能覆盖有值字段！
                  Set v_sex = IFNULL(@sex, v_sex);
                  Set v_job = IFNULL(@job, v_job);
                  Set v_birth_year = IFNULL(@birth_year, v_birth_year);
                  Set v_birthday = IFNULL(@birthday, v_birthday);
                  Set v_email = IFNULL(@email, v_email);
                  Set v_mobile = IFNULL(@mobile, v_mobile);
                  Set v_phone = IFNULL(@phone, v_phone);
                  Set v_zip = IFNULL(@zip, v_zip);
                  Set v_country = IFNULL(@country, v_country);
                  Set v_state = IFNULL(@state, v_state);
                  Set v_city = IFNULL(@city, v_city);
                  Set v_district = IFNULL(@district, v_district);
                  Set v_address = IFNULL(@address, v_address);

              End;
          END IF;
      End Loop;
  Close rowCursor; #游标遍历结束

  #2.记录合并结果
  update uni_customer set full_name = v_full_name,
                          sex = v_sex,
                          job = v_job,
                          birth_year = v_birth_year,
                          birthday = v_birthday,
                          email = v_email,
                          mobile = v_mobile,
                          phone = v_phone,
                          zip = v_zip,
                          country = v_country,
                          state = v_state,
                          city = v_city,
                          district = v_district,
                          address = v_address
  where uni_id = v_uni_id;

END ;


CREATE PROCEDURE proc_sync_parent_plat(IN p_plat_code char(8), IN p_parent_plat char(8), IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    DETERMINISTIC
	COMMENT '将当前平台的客户信息同步到其父平台的过程'
BEGIN
    #DECLARE v_parent_plat char(8); #父平台代码

    #Select parent_plat Into v_parent_plat From uni_plat Where plat_code = v_plat_code;#取父平台代码
    CASE p_parent_plat
        WHEN 'taobao' THEN #若当前客户的父平台是“淘宝”
          BEGIN

              #若父平台是否不存在该客户，则在父平台插入该客户
              Insert Into plt_taobao_customer (customerno, full_name, sex, job, birth_year, birthday, email, mobile, phone, zip, country, state, city, district, address, changed)
              Select p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone, p_zip, p_country, p_state, p_city, p_district, p_address, p_changed
              From plt_taobao_customer
              Where NOT EXISTS (select 1 from  plt_taobao_customer where customerno = p_customerno) Limit 1;

          END;
    END CASE;

END ;


CREATE PROCEDURE proc_unify_customer(IN p_plat_code char(8), IN p_customerno varchar(50), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    DETERMINISTIC
	COMMENT '统一客户信息的全过程'
BEGIN
  DECLARE v_plat_table varchar(50);
  DECLARE v_parent_plat char(8);
  DECLARE v_plat_priority smallint;

  #1.执行Dedup过程
  Select plat_table, parent_plat, plat_priority Into v_plat_table, v_parent_plat, v_plat_priority From uni_plat Where plat_code = p_plat_code Limit 1;
  CALL proc_dedup_customer(p_plat_code, v_parent_plat, v_plat_priority,
                           p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                           p_zip, p_country, p_state, p_city, p_district, p_address, p_changed, @is_dedup);#是否重复客户

  #2.若客户重复，则执行Merge过程
  IF (@is_dedup) THEN #若该客户是老客户（重复客户），则将当前客户与老客户合并成新的统一信息
      CALL proc_merge_customer(p_plat_code, v_parent_plat, v_plat_table, v_plat_priority,
                               p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                               p_zip, p_country, p_state, p_city, p_district, p_address, p_changed);
  END IF;
END ;


CREATE PROCEDURE proc_unify_modify_customer(IN p_customerno varchar(64), IN p_full_name varchar(50), IN p_sex char(1), IN p_job varchar(50), IN p_birth_year smallint, IN p_birthday date, IN p_email varchar(100), IN p_mobile varchar(20), IN p_phone varchar(50), IN p_zip varchar(20), IN p_country varchar(50), IN p_state varchar(50), IN p_city varchar(50), IN p_district varchar(100), IN p_address varchar(255), IN p_changed datetime)
    DETERMINISTIC
	COMMENT '统一客户信息被手工修改时过程'
BEGIN
  DECLARE v_uni_id varchar(64); #统一客户ID
  DECLARE v_plat_code char(8) DEFAULT 'modify';
  DECLARE v_plat_table varchar(50);
  DECLARE v_parent_plat char(8);
  DECLARE v_plat_priority smallint;

  #1.记录手工修改统一客户信息到平台关系表
  Select plat_table, parent_plat, plat_priority Into v_plat_table, v_parent_plat, v_plat_priority From uni_plat Where plat_code = v_plat_code Limit 1;
  Select uni_id into v_uni_id from uni_customer_plat where plat_code = v_plat_code and customerno = p_customerno limit 1;
  IF (v_uni_id IS NULL) THEN
      insert into uni_customer_plat (uni_id, plat_code, customerno, plat_priority, changed)
          values (p_customerno, v_plat_code, p_customerno, v_plat_priority, p_changed); #对于uni_id字段，此处手工修改平台客户ID与统一客户ID相同
  END IF;

  #2.执行Merge过程
  Call proc_merge_customer(v_plat_code, v_parent_plat, v_plat_table, v_plat_priority,
                           p_customerno, p_full_name, p_sex, p_job, p_birth_year, p_birthday, p_email, p_mobile, p_phone,
                           p_zip, p_country, p_state, p_city, p_district, p_address, p_changed);

END ;


CREATE PROCEDURE proc_delete_customer(IN p_plat_code char(8), IN p_customerno varchar(50))
	DETERMINISTIC
    COMMENT '删除客户相关平台信息'
BEGIN
  Delete from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno;
END ;



###-----------------------------------------------------------------------------------------------------------------------------
###  6. triggers
###-----------------------------------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_insert_extaobao_customer AFTER INSERT ON plt_extaobao_customer
FOR EACH ROW Begin
    #1.将当前客户信息同步到父平台
    Call proc_sync_parent_plat('extaobao', 'taobao',
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
    #2.再统一客户信息
    Call proc_unify_customer('extaobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_update_extaobao_customer AFTER UPDATE ON plt_extaobao_customer
FOR EACH ROW Begin
    Call proc_unify_customer('extaobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


#plt_ext_customer 触发器----------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_insert_ext_customer AFTER INSERT ON plt_ext_customer
FOR EACH ROW Begin
    Call proc_unify_customer('ext' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_update_ext_customer AFTER UPDATE ON plt_ext_customer
FOR EACH ROW Begin
    Call proc_unify_customer('ext' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_delete_ext_customer AFTER DELETE ON plt_ext_customer
FOR EACH ROW BEGIN
    Call proc_delete_unify_customer('ext', OLD.customerno);
END ;


#plt_kfgzt_customer 触发器----------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_insert_kfgzt_customer AFTER INSERT ON plt_kfgzt_customer
FOR EACH ROW Begin
    #1.将当前客户信息同步到父平台
    Call proc_sync_parent_plat('kfgzt', 'taobao',
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
    #2.再统一客户信息
    Call proc_unify_customer('kfgzt' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_update_kfgzt_customer AFTER UPDATE ON plt_kfgzt_customer
FOR EACH ROW Begin
    Call proc_unify_customer('kfgzt' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


#plt_kfgzt_customer_label 触发器----------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_sync_kfgzt_customer AFTER INSERT ON plt_kfgzt_customer_label
FOR EACH ROW BEGIN
    Insert into plt_kfgzt_customer (customerno)  #“客服工作台”客户标签新增客户时, 若客服工作台不存在该客户，则同步到客服工作台的客户来源表
    Select NEW.customerno From plt_kfgzt_customer_label
    Where NOT EXISTS (select 1 from  plt_kfgzt_customer where customerno =NEW.customerno ) Limit 1;
END ;


#plt_modify_customer 触发器----------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_insert_modify_customer AFTER INSERT ON plt_modify_customer
FOR EACH ROW Begin
    Call proc_unify_modify_customer(NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                                        NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_update_modify_customer AFTER UPDATE ON plt_modify_customer
FOR EACH ROW Begin
    Call proc_unify_modify_customer(NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                                        NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


#plt_wdzx_customer 触发器----------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_insert_wdzx_customer AFTER INSERT ON plt_wdzx_customer
FOR EACH ROW Begin
    #1.将当前客户信息同步到父平台
    Call proc_sync_parent_plat('wdzx', 'taobao',
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
    #2.再统一客户信息
    Call proc_unify_customer('wdzx' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_update_wdzx_customer AFTER UPDATE ON plt_wdzx_customer
FOR EACH ROW Begin
    Call proc_unify_customer('wdzx' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


#plt_taobao_customer 触发器----------------------------------------------------------------------------------------------------
CREATE TRIGGER trigger_insert_taobao_customer AFTER INSERT ON plt_taobao_customer
FOR EACH ROW Begin
    Call proc_unify_customer('taobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_update_taobao_customer AFTER UPDATE ON plt_taobao_customer
FOR EACH ROW Begin
    Call proc_unify_customer('taobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END ;


CREATE TRIGGER trigger_delete_extaobao_customer_plat AFTER DELETE ON plt_extaobao_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('extaobao', OLD.customerno);
END ;


CREATE TRIGGER trigger_delete_kfgzt_customer_plat AFTER DELETE ON plt_kfgzt_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('kfgzt', OLD.customerno);
END ;


CREATE TRIGGER trigger_delete_wdzx_customer_plat AFTER DELETE ON plt_wdzx_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('wdzx', OLD.customerno);
END ;

###-----------------------------------------------------------------------------------------------------------------------------
###  init db data
###-----------------------------------------------------------------------------------------------------------------------------

-- #活动状态表
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('A1', '设计中', '1');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('A2', '待审批', '3');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('A3', '待执行', '5');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('A4', '中止', '7');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('A5', '执行完成', '8');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('A6', '执行结束（错误）', '9');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('B1', '设计时预执行', '2');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('B2', '待审批时预执行', '4');
INSERT INTO tds_campaign_status (status_id, status_value, orderid) VALUES ('B3', '执行中', '6');


-- #流程执行JOB状态维表
INSERT INTO tds_job_status (status_id, status_name) VALUES (10,'准备开始');
INSERT INTO tds_job_status (status_id, status_name) VALUES (11,'运行中');
INSERT INTO tds_job_status (status_id, status_name) VALUES (12,'运行中有错误');
INSERT INTO tds_job_status (status_id, status_name) VALUES (13,'运行中待中止');
INSERT INTO tds_job_status (status_id, status_name) VALUES (18,'等待操作');
INSERT INTO tds_job_status (status_id, status_name) VALUES (21,'运行正常结束');
INSERT INTO tds_job_status (status_id, status_name) VALUES (22,'运行错误结束');
INSERT INTO tds_job_status (status_id, status_name) VALUES (23,'运行中止结束');


-- #节点执行SUBJOB状态维表
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (10,'准备开始');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (11,'运行中');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (12,'运行中有错误');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (13,'运行中待中止');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (8,'等待资源');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (18,'等待操作');

INSERT INTO tds_subjob_status (status_id, status_name) VALUES (21,'运行正常结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (22,'运行错误结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (23,'运行中止结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (24,'运行跳过结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (25,'运行超时结束');

INSERT INTO tds_subjob_status (status_id, status_name) VALUES (51,'运行结束(成功)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (52,'运行结束(错误)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (53,'运行结束(中止)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (54,'运行结束(跳过)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (55,'运行结束(超时)');

-- #metadata begin:
#-----------------------------------------------------------------------------------------------------------------------
#  目录(索引)
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (1, 1,   NULL, '客户信息', 1);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (2, 21,  NULL, 'RFM指标', 2);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (3, 99,  NULL, '导入字段', 3);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (4, 31,  NULL, '自定义标签', 4);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (5, 5,   NULL, '当前时间', 5);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (6, 41,  NULL, '会员信息', 6);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (7, 51,  NULL, '订单信息', 7);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (8, 1,      1, '联系方式有效性', 8);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (9, 71,  NULL, '店铺购买指标', 9);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (10, 61, NULL, '时间变量', 10);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (11, 81, NULL, '店铺邮件订阅', 11);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (12, 91, NULL, '客户标签', 12);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (13, 10, NULL, '客户导入批次', 13);
INSERT INTO tm_catalog(catalog_id, catalog_type, parent_id, show_name, show_order) VALUES (14, 42, NULL, '会员信息', 14);

#-----------------------------------------------------------------------------------------------------------------------
#  元数据：初始化字典
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (1, 	'性别', 				'ALL', 		NULL);
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (2, 	'是否', 				'ALL', 		NULL);
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (3, 	'有效性', 			'ALL', 		NULL);
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (22, '省市', 				'ALL', 		'含省、直辖市、特别行政区');
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (23, '城市', 				'ALL', 		'含地级市、州、地区');
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (24, '区县', 				'ALL', 		'含县区级地区');
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (41, '淘宝VIP类型',			'taobao', NULL);
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (42, '信用等级', 			'taobao', NULL);
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (43, '店铺会员等级', 		'taobao', NULL);
INSERT INTO tm_dic (dic_id, show_name, plat_code, remark) VALUES (44, '交易来源', 			'taobao', NULL);

#-----------------------------------------------------------------------------------------------------------------------
#  元数据：初始化字典取值
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1, 1, NULL, 'm', '男');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (2, 1, NULL, 'f', '女');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (3, 1, NULL, 'none', '未知');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (21, 2, NULL, '1', '是');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (22, 2, NULL, '0', '否');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (31, 3, NULL, '1', '有效');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (32, 3, NULL, '0', '无效');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1001, 41, NULL, 'c', '普通会员');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1002, 41, NULL, 'asso_vip', '荣誉会员');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1003, 41, NULL, 'vip1', 'VIP1');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1004, 41, NULL, 'vip2', 'VIP2');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1005, 41, NULL, 'vip3', 'VIP3');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1006, 41, NULL, 'vip4', 'VIP4');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1007, 41, NULL, 'vip5', 'VIP5');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1008, 41, NULL, 'vip6', 'VIP6');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1010, 42, NULL, '0', '未分级');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1011, 42, NULL, '1', '一心');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1012, 42, NULL, '2', '二心');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1013, 42, NULL, '3', '三心');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1014, 42, NULL, '4', '四心');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1015, 42, NULL, '5', '五心');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1016, 42, NULL, '6', '一钻');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1017, 42, NULL, '7', '二钻');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1018, 42, NULL, '8', '三钻');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1019, 42, NULL, '9', '四钻');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1020, 42, NULL, '10', '五钻');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1021, 42, NULL, '11', '一皇冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1022, 42, NULL, '12', '二皇冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1023, 42, NULL, '13', '三皇冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1024, 42, NULL, '14', '四皇冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1025, 42, NULL, '15', '五皇冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1026, 42, NULL, '16', '一金冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1027, 42, NULL, '17', '二金冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1028, 42, NULL, '18', '三金冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1029, 42, NULL, '19', '四金冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1030, 42, NULL, '20', '五金冠');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1040, 43, NULL, '0', '未分级');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1041, 43, NULL, '1', '普通会员');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1042, 43, NULL, '2', '高级会员');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1043, 43, NULL, '3', 'VIP会员');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1044, 43, NULL, '4', '至尊VIP会员');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1101, 44, NULL, 'HITAO', 	'嗨淘');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1102, 44, NULL, 'JHS', 	'聚划算');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1103, 44, NULL, 'TAOBAO',	'普通淘宝');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1104, 44, NULL, 'TOP', 	'TOP平台');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1105, 44, NULL, 'WAP', 	'手机');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110000, 22, NULL, '北京', '北京');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110100, 23, 110000, '北京', '北京市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110101, 24, 110100, '东城区', '东城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110102, 24, 110100, '西城区', '西城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110105, 24, 110100, '朝阳区', '朝阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110106, 24, 110100, '丰台区', '丰台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110107, 24, 110100, '石景山区', '石景山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110108, 24, 110100, '海淀区', '海淀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110109, 24, 110100, '门头沟区', '门头沟区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110111, 24, 110100, '房山区', '房山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110112, 24, 110100, '通州区', '通州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110113, 24, 110100, '顺义区', '顺义区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110114, 24, 110100, '昌平区', '昌平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110115, 24, 110100, '大兴区', '大兴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110116, 24, 110100, '怀柔区', '怀柔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110117, 24, 110100, '平谷区', '平谷区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110228, 24, 110200, '密云县', '密云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110229, 24, 110200, '延庆县', '延庆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120000, 22, NULL, '天津', '天津');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120100, 23, 120000, '天津', '天津市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120101, 24, 120100, '和平区', '和平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120102, 24, 120100, '河东区', '河东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120103, 24, 120100, '河西区', '河西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120104, 24, 120100, '南开区', '南开区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120105, 24, 120100, '河北区', '河北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120106, 24, 120100, '红桥区', '红桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120110, 24, 120100, '东丽区', '东丽区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120111, 24, 120100, '西青区', '西青区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120112, 24, 120100, '津南区', '津南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120113, 24, 120100, '北辰区', '北辰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120114, 24, 120100, '武清区', '武清区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120115, 24, 120100, '宝坻区', '宝坻区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120116, 24, 120100, '滨海新区', '滨海新区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120221, 24, 120200, '宁河县', '宁河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120223, 24, 120200, '静海县', '静海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120225, 24, 120200, '蓟县', '蓟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130000, 22, NULL, '河北', '河北省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130100, 23, 130000, '石家庄', '石家庄市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130101, 24, 130100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130102, 24, 130100, '长安区', '长安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130103, 24, 130100, '桥东区', '桥东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130104, 24, 130100, '桥西区', '桥西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130105, 24, 130100, '新华区', '新华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130107, 24, 130100, '井陉矿区', '井陉矿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130108, 24, 130100, '裕华区', '裕华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130121, 24, 130100, '井陉县', '井陉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130123, 24, 130100, '正定县', '正定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130124, 24, 130100, '栾城县', '栾城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130125, 24, 130100, '行唐县', '行唐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130126, 24, 130100, '灵寿县', '灵寿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130127, 24, 130100, '高邑县', '高邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130128, 24, 130100, '深泽县', '深泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130129, 24, 130100, '赞皇县', '赞皇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130130, 24, 130100, '无极县', '无极县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130131, 24, 130100, '平山县', '平山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130132, 24, 130100, '元氏县', '元氏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130133, 24, 130100, '赵县', '赵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130181, 24, 130100, '辛集', '辛集市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130182, 24, 130100, '藁城', '藁城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130183, 24, 130100, '晋州', '晋州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130184, 24, 130100, '新乐', '新乐市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130185, 24, 130100, '鹿泉', '鹿泉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130200, 23, 130000, '唐山', '唐山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130201, 24, 130200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130202, 24, 130200, '路南区', '路南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130203, 24, 130200, '路北区', '路北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130204, 24, 130200, '古冶区', '古冶区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130205, 24, 130200, '开平区', '开平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130207, 24, 130200, '丰南区', '丰南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130208, 24, 130200, '丰润区', '丰润区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130223, 24, 130200, '滦县', '滦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130224, 24, 130200, '滦南县', '滦南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130225, 24, 130200, '乐亭县', '乐亭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130227, 24, 130200, '迁西县', '迁西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130229, 24, 130200, '玉田县', '玉田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130230, 24, 130200, '唐海县', '唐海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130281, 24, 130200, '遵化', '遵化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130283, 24, 130200, '迁安', '迁安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130300, 23, 130000, '秦皇岛', '秦皇岛市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130301, 24, 130300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130302, 24, 130300, '海港区', '海港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130303, 24, 130300, '山海关区', '山海关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130304, 24, 130300, '北戴河区', '北戴河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130321, 24, 130300, '青龙满族自治县', '青龙满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130322, 24, 130300, '昌黎县', '昌黎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130323, 24, 130300, '抚宁县', '抚宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130324, 24, 130300, '卢龙县', '卢龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130400, 23, 130000, '邯郸', '邯郸市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130401, 24, 130400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130402, 24, 130400, '邯山区', '邯山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130403, 24, 130400, '丛台区', '丛台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130404, 24, 130400, '复兴区', '复兴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130406, 24, 130400, '峰峰矿区', '峰峰矿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130421, 24, 130400, '邯郸县', '邯郸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130423, 24, 130400, '临漳县', '临漳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130424, 24, 130400, '成安县', '成安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130425, 24, 130400, '大名县', '大名县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130426, 24, 130400, '涉县', '涉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130427, 24, 130400, '磁县', '磁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130428, 24, 130400, '肥乡县', '肥乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130429, 24, 130400, '永年县', '永年县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130430, 24, 130400, '邱县', '邱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130431, 24, 130400, '鸡泽县', '鸡泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130432, 24, 130400, '广平县', '广平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130433, 24, 130400, '馆陶县', '馆陶县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130434, 24, 130400, '魏县', '魏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130435, 24, 130400, '曲周县', '曲周县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130481, 24, 130400, '武安', '武安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130500, 23, 130000, '邢台', '邢台市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130501, 24, 130500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130502, 24, 130500, '桥东区', '桥东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130503, 24, 130500, '桥西区', '桥西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130521, 24, 130500, '邢台县', '邢台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130522, 24, 130500, '临城县', '临城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130523, 24, 130500, '内丘县', '内丘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130524, 24, 130500, '柏乡县', '柏乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130525, 24, 130500, '隆尧县', '隆尧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130526, 24, 130500, '任县', '任县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130527, 24, 130500, '南和县', '南和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130528, 24, 130500, '宁晋县', '宁晋县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130529, 24, 130500, '巨鹿县', '巨鹿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130530, 24, 130500, '新河县', '新河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130531, 24, 130500, '广宗县', '广宗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130532, 24, 130500, '平乡县', '平乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130533, 24, 130500, '威县', '威县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130534, 24, 130500, '清河县', '清河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130535, 24, 130500, '临西县', '临西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130581, 24, 130500, '南宫', '南宫市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130582, 24, 130500, '沙河', '沙河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130600, 23, 130000, '保定', '保定市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130601, 24, 130600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130602, 24, 130600, '新市区', '新市区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130603, 24, 130600, '北市区', '北市区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130604, 24, 130600, '南市区', '南市区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130621, 24, 130600, '满城县', '满城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130622, 24, 130600, '清苑县', '清苑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130623, 24, 130600, '涞水县', '涞水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130624, 24, 130600, '阜平县', '阜平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130625, 24, 130600, '徐水县', '徐水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130626, 24, 130600, '定兴县', '定兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130627, 24, 130600, '唐县', '唐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130628, 24, 130600, '高阳县', '高阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130629, 24, 130600, '容城县', '容城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130630, 24, 130600, '涞源县', '涞源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130631, 24, 130600, '望都县', '望都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130632, 24, 130600, '安新县', '安新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130633, 24, 130600, '易县', '易县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130634, 24, 130600, '曲阳县', '曲阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130635, 24, 130600, '蠡县', '蠡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130636, 24, 130600, '顺平县', '顺平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130637, 24, 130600, '博野县', '博野县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130638, 24, 130600, '雄县', '雄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130681, 24, 130600, '涿州', '涿州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130682, 24, 130600, '定州', '定州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130683, 24, 130600, '安国', '安国市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130684, 24, 130600, '高碑店', '高碑店市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130700, 23, 130000, '张家口', '张家口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130701, 24, 130700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130702, 24, 130700, '桥东区', '桥东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130703, 24, 130700, '桥西区', '桥西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130705, 24, 130700, '宣化区', '宣化区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130706, 24, 130700, '下花园区', '下花园区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130721, 24, 130700, '宣化县', '宣化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130722, 24, 130700, '张北县', '张北县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130723, 24, 130700, '康保县', '康保县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130724, 24, 130700, '沽源县', '沽源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130725, 24, 130700, '尚义县', '尚义县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130726, 24, 130700, '蔚县', '蔚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130727, 24, 130700, '阳原县', '阳原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130728, 24, 130700, '怀安县', '怀安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130729, 24, 130700, '万全县', '万全县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130730, 24, 130700, '怀来县', '怀来县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130731, 24, 130700, '涿鹿县', '涿鹿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130732, 24, 130700, '赤城县', '赤城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130733, 24, 130700, '崇礼县', '崇礼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130800, 23, 130000, '承德', '承德市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130801, 24, 130800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130802, 24, 130800, '双桥区', '双桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130803, 24, 130800, '双滦区', '双滦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130804, 24, 130800, '鹰手营子矿区', '鹰手营子矿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130821, 24, 130800, '承德县', '承德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130822, 24, 130800, '兴隆县', '兴隆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130823, 24, 130800, '平泉县', '平泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130824, 24, 130800, '滦平县', '滦平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130825, 24, 130800, '隆化县', '隆化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130826, 24, 130800, '丰宁满族自治县', '丰宁满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130827, 24, 130800, '宽城满族自治县', '宽城满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130828, 24, 130800, '围场满族蒙古族自治县', '围场满族蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130900, 23, 130000, '沧州', '沧州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130901, 24, 130900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130902, 24, 130900, '新华区', '新华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130903, 24, 130900, '运河区', '运河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130921, 24, 130900, '沧县', '沧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130922, 24, 130900, '青县', '青县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130923, 24, 130900, '东光县', '东光县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130924, 24, 130900, '海兴县', '海兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130925, 24, 130900, '盐山县', '盐山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130926, 24, 130900, '肃宁县', '肃宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130927, 24, 130900, '南皮县', '南皮县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130928, 24, 130900, '吴桥县', '吴桥县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130929, 24, 130900, '献县', '献县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130930, 24, 130900, '孟村回族自治县', '孟村回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130981, 24, 130900, '泊头', '泊头市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130982, 24, 130900, '任丘', '任丘市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130983, 24, 130900, '黄骅', '黄骅市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (130984, 24, 130900, '河间', '河间市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131000, 23, 130000, '廊坊', '廊坊市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131001, 24, 131000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131002, 24, 131000, '安次区', '安次区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131003, 24, 131000, '广阳区', '广阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131022, 24, 131000, '固安县', '固安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131023, 24, 131000, '永清县', '永清县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131024, 24, 131000, '香河县', '香河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131025, 24, 131000, '大城县', '大城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131026, 24, 131000, '文安县', '文安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131028, 24, 131000, '大厂回族自治县', '大厂回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131081, 24, 131000, '霸州', '霸州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131082, 24, 131000, '三河', '三河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131100, 23, 130000, '衡水', '衡水市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131101, 24, 131100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131102, 24, 131100, '桃城区', '桃城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131121, 24, 131100, '枣强县', '枣强县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131122, 24, 131100, '武邑县', '武邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131123, 24, 131100, '武强县', '武强县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131124, 24, 131100, '饶阳县', '饶阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131125, 24, 131100, '安平县', '安平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131126, 24, 131100, '故城县', '故城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131127, 24, 131100, '景县', '景县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131128, 24, 131100, '阜城县', '阜城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131181, 24, 131100, '冀州', '冀州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (131182, 24, 131100, '深州', '深州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140000, 22, NULL, '山西', '山西省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140100, 23, 140000, '太原', '太原市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140101, 24, 140100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140105, 24, 140100, '小店区', '小店区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140106, 24, 140100, '迎泽区', '迎泽区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140107, 24, 140100, '杏花岭区', '杏花岭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140108, 24, 140100, '尖草坪区', '尖草坪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140109, 24, 140100, '万柏林区', '万柏林区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140110, 24, 140100, '晋源区', '晋源区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140121, 24, 140100, '清徐县', '清徐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140122, 24, 140100, '阳曲县', '阳曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140123, 24, 140100, '娄烦县', '娄烦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140181, 24, 140100, '古交', '古交市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140200, 23, 140000, '大同', '大同市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140201, 24, 140200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140202, 24, 140200, '城区', '城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140203, 24, 140200, '矿区', '矿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140211, 24, 140200, '南郊区', '南郊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140212, 24, 140200, '新荣区', '新荣区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140221, 24, 140200, '阳高县', '阳高县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140222, 24, 140200, '天镇县', '天镇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140223, 24, 140200, '广灵县', '广灵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140224, 24, 140200, '灵丘县', '灵丘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140225, 24, 140200, '浑源县', '浑源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140226, 24, 140200, '左云县', '左云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140227, 24, 140200, '大同县', '大同县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140300, 23, 140000, '阳泉', '阳泉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140301, 24, 140300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140302, 24, 140300, '城区', '城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140303, 24, 140300, '矿区', '矿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140311, 24, 140300, '郊区', '郊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140321, 24, 140300, '平定县', '平定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140322, 24, 140300, '盂县', '盂县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140400, 23, 140000, '长治', '长治市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140401, 24, 140400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140402, 24, 140400, '城区', '城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140411, 24, 140400, '郊区', '郊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140421, 24, 140400, '长治县', '长治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140423, 24, 140400, '襄垣县', '襄垣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140424, 24, 140400, '屯留县', '屯留县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140425, 24, 140400, '平顺县', '平顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140426, 24, 140400, '黎城县', '黎城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140427, 24, 140400, '壶关县', '壶关县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140428, 24, 140400, '长子县', '长子县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140429, 24, 140400, '武乡县', '武乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140430, 24, 140400, '沁县', '沁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140431, 24, 140400, '沁源县', '沁源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140481, 24, 140400, '潞城', '潞城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140500, 23, 140000, '晋城', '晋城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140501, 24, 140500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140502, 24, 140500, '城区', '城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140521, 24, 140500, '沁水县', '沁水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140522, 24, 140500, '阳城县', '阳城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140524, 24, 140500, '陵川县', '陵川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140525, 24, 140500, '泽州县', '泽州县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140581, 24, 140500, '高平', '高平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140600, 23, 140000, '朔州', '朔州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140601, 24, 140600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140602, 24, 140600, '朔城区', '朔城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140603, 24, 140600, '平鲁区', '平鲁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140621, 24, 140600, '山阴县', '山阴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140622, 24, 140600, '应县', '应县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140623, 24, 140600, '右玉县', '右玉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140624, 24, 140600, '怀仁县', '怀仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140700, 23, 140000, '晋中', '晋中市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140701, 24, 140700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140702, 24, 140700, '榆次区', '榆次区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140721, 24, 140700, '榆社县', '榆社县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140722, 24, 140700, '左权县', '左权县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140723, 24, 140700, '和顺县', '和顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140724, 24, 140700, '昔阳县', '昔阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140725, 24, 140700, '寿阳县', '寿阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140726, 24, 140700, '太谷县', '太谷县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140727, 24, 140700, '祁县', '祁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140728, 24, 140700, '平遥县', '平遥县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140729, 24, 140700, '灵石县', '灵石县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140781, 24, 140700, '介休', '介休市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140800, 23, 140000, '运城', '运城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140801, 24, 140800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140802, 24, 140800, '盐湖区', '盐湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140821, 24, 140800, '临猗县', '临猗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140822, 24, 140800, '万荣县', '万荣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140823, 24, 140800, '闻喜县', '闻喜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140824, 24, 140800, '稷山县', '稷山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140825, 24, 140800, '新绛县', '新绛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140826, 24, 140800, '绛县', '绛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140827, 24, 140800, '垣曲县', '垣曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140828, 24, 140800, '夏县', '夏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140829, 24, 140800, '平陆县', '平陆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140830, 24, 140800, '芮城县', '芮城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140881, 24, 140800, '永济', '永济市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140882, 24, 140800, '河津', '河津市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140900, 23, 140000, '忻州', '忻州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140901, 24, 140900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140902, 24, 140900, '忻府区', '忻府区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140921, 24, 140900, '定襄县', '定襄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140922, 24, 140900, '五台县', '五台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140923, 24, 140900, '代县', '代县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140924, 24, 140900, '繁峙县', '繁峙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140925, 24, 140900, '宁武县', '宁武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140926, 24, 140900, '静乐县', '静乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140927, 24, 140900, '神池县', '神池县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140928, 24, 140900, '五寨县', '五寨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140929, 24, 140900, '岢岚县', '岢岚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140930, 24, 140900, '河曲县', '河曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140931, 24, 140900, '保德县', '保德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140932, 24, 140900, '偏关县', '偏关县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (140981, 24, 140900, '原平', '原平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141000, 23, 140000, '临汾', '临汾市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141001, 24, 141000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141002, 24, 141000, '尧都区', '尧都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141021, 24, 141000, '曲沃县', '曲沃县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141022, 24, 141000, '翼城县', '翼城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141023, 24, 141000, '襄汾县', '襄汾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141024, 24, 141000, '洪洞县', '洪洞县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141025, 24, 141000, '古县', '古县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141026, 24, 141000, '安泽县', '安泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141027, 24, 141000, '浮山县', '浮山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141028, 24, 141000, '吉县', '吉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141029, 24, 141000, '乡宁县', '乡宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141030, 24, 141000, '大宁县', '大宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141031, 24, 141000, '隰县', '隰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141032, 24, 141000, '永和县', '永和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141033, 24, 141000, '蒲县', '蒲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141034, 24, 141000, '汾西县', '汾西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141081, 24, 141000, '侯马', '侯马市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141082, 24, 141000, '霍州', '霍州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141100, 23, 140000, '吕梁', '吕梁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141101, 24, 141100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141102, 24, 141100, '离石区', '离石区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141121, 24, 141100, '文水县', '文水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141122, 24, 141100, '交城县', '交城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141123, 24, 141100, '兴县', '兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141124, 24, 141100, '临县', '临县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141125, 24, 141100, '柳林县', '柳林县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141126, 24, 141100, '石楼县', '石楼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141127, 24, 141100, '岚县', '岚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141128, 24, 141100, '方山县', '方山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141129, 24, 141100, '中阳县', '中阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141130, 24, 141100, '交口县', '交口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141181, 24, 141100, '孝义', '孝义市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (141182, 24, 141100, '汾阳', '汾阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150000, 22, NULL, '内蒙古', '内蒙古自治区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150100, 23, 150000, '呼和浩特', '呼和浩特市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150101, 24, 150100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150102, 24, 150100, '新城区', '新城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150103, 24, 150100, '回民区', '回民区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150104, 24, 150100, '玉泉区', '玉泉区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150105, 24, 150100, '赛罕区', '赛罕区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150121, 24, 150100, '土默特左旗', '土默特左旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150122, 24, 150100, '托克托县', '托克托县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150123, 24, 150100, '和林格尔县', '和林格尔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150124, 24, 150100, '清水河县', '清水河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150125, 24, 150100, '武川县', '武川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150200, 23, 150000, '包头', '包头市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150201, 24, 150200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150202, 24, 150200, '东河区', '东河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150203, 24, 150200, '昆都仑区', '昆都仑区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150204, 24, 150200, '青山区', '青山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150205, 24, 150200, '石拐区', '石拐区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150206, 24, 150200, '白云鄂博矿区', '白云鄂博矿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150207, 24, 150200, '九原区', '九原区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150221, 24, 150200, '土默特右旗', '土默特右旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150222, 24, 150200, '固阳县', '固阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150223, 24, 150200, '达尔罕茂明安联合旗', '达尔罕茂明安联合旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150300, 23, 150000, '乌海', '乌海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150301, 24, 150300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150302, 24, 150300, '海勃湾区', '海勃湾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150303, 24, 150300, '海南区', '海南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150304, 24, 150300, '乌达区', '乌达区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150400, 23, 150000, '赤峰', '赤峰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150401, 24, 150400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150402, 24, 150400, '红山区', '红山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150403, 24, 150400, '元宝山区', '元宝山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150404, 24, 150400, '松山区', '松山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150421, 24, 150400, '阿鲁科尔沁旗', '阿鲁科尔沁旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150422, 24, 150400, '巴林左旗', '巴林左旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150423, 24, 150400, '巴林右旗', '巴林右旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150424, 24, 150400, '林西县', '林西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150425, 24, 150400, '克什克腾旗', '克什克腾旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150426, 24, 150400, '翁牛特旗', '翁牛特旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150428, 24, 150400, '喀喇沁旗', '喀喇沁旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150429, 24, 150400, '宁城县', '宁城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150430, 24, 150400, '敖汉旗', '敖汉旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150500, 23, 150000, '通辽', '通辽市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150501, 24, 150500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150502, 24, 150500, '科尔沁区', '科尔沁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150521, 24, 150500, '科尔沁左翼中旗', '科尔沁左翼中旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150522, 24, 150500, '科尔沁左翼后旗', '科尔沁左翼后旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150523, 24, 150500, '开鲁县', '开鲁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150524, 24, 150500, '库伦旗', '库伦旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150525, 24, 150500, '奈曼旗', '奈曼旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150526, 24, 150500, '扎鲁特旗', '扎鲁特旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150581, 24, 150500, '霍林郭勒', '霍林郭勒市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150600, 23, 150000, '鄂尔多斯', '鄂尔多斯市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150601, 24, 150600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150602, 24, 150600, '东胜区', '东胜区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150621, 24, 150600, '达拉特旗', '达拉特旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150622, 24, 150600, '准格尔旗', '准格尔旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150623, 24, 150600, '鄂托克前旗', '鄂托克前旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150624, 24, 150600, '鄂托克旗', '鄂托克旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150625, 24, 150600, '杭锦旗', '杭锦旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150626, 24, 150600, '乌审旗', '乌审旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150627, 24, 150600, '伊金霍洛旗', '伊金霍洛旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150700, 23, 150000, '呼伦贝尔', '呼伦贝尔市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150701, 24, 150700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150702, 24, 150700, '海拉尔区', '海拉尔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150721, 24, 150700, '阿荣旗', '阿荣旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150722, 24, 150700, '莫力达瓦达斡尔族自治旗', '莫力达瓦达斡尔族自治旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150723, 24, 150700, '鄂伦春自治旗', '鄂伦春自治旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150724, 24, 150700, '鄂温克族自治旗', '鄂温克族自治旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150725, 24, 150700, '陈巴尔虎旗', '陈巴尔虎旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150726, 24, 150700, '新巴尔虎左旗', '新巴尔虎左旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150727, 24, 150700, '新巴尔虎右旗', '新巴尔虎右旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150781, 24, 150700, '满洲里', '满洲里市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150782, 24, 150700, '牙克石', '牙克石市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150783, 24, 150700, '扎兰屯', '扎兰屯市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150784, 24, 150700, '额尔古纳', '额尔古纳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150785, 24, 150700, '根河', '根河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150800, 23, 150000, '巴彦淖尔', '巴彦淖尔市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150801, 24, 150800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150802, 24, 150800, '临河区', '临河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150821, 24, 150800, '五原县', '五原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150822, 24, 150800, '磴口县', '磴口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150823, 24, 150800, '乌拉特前旗', '乌拉特前旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150824, 24, 150800, '乌拉特中旗', '乌拉特中旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150825, 24, 150800, '乌拉特后旗', '乌拉特后旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150826, 24, 150800, '杭锦后旗', '杭锦后旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150900, 23, 150000, '乌兰察布', '乌兰察布市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150901, 24, 150900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150902, 24, 150900, '集宁区', '集宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150921, 24, 150900, '卓资县', '卓资县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150922, 24, 150900, '化德县', '化德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150923, 24, 150900, '商都县', '商都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150924, 24, 150900, '兴和县', '兴和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150925, 24, 150900, '凉城县', '凉城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150926, 24, 150900, '察哈尔右翼前旗', '察哈尔右翼前旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150927, 24, 150900, '察哈尔右翼中旗', '察哈尔右翼中旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150928, 24, 150900, '察哈尔右翼后旗', '察哈尔右翼后旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150929, 24, 150900, '四子王旗', '四子王旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (150981, 24, 150900, '丰镇', '丰镇市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152200, 23, 150000, '兴安盟', '兴安盟');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152201, 24, 152200, '乌兰浩特', '乌兰浩特市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152202, 24, 152200, '阿尔山', '阿尔山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152221, 24, 152200, '科尔沁右翼前旗', '科尔沁右翼前旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152222, 24, 152200, '科尔沁右翼中旗', '科尔沁右翼中旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152223, 24, 152200, '扎赉特旗', '扎赉特旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152224, 24, 152200, '突泉县', '突泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152500, 23, 150000, '锡林郭勒盟', '锡林郭勒盟');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152501, 24, 152500, '二连浩特', '二连浩特市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152502, 24, 152500, '锡林浩特', '锡林浩特市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152522, 24, 152500, '阿巴嘎旗', '阿巴嘎旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152523, 24, 152500, '苏尼特左旗', '苏尼特左旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152524, 24, 152500, '苏尼特右旗', '苏尼特右旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152525, 24, 152500, '东乌珠穆沁旗', '东乌珠穆沁旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152526, 24, 152500, '西乌珠穆沁旗', '西乌珠穆沁旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152527, 24, 152500, '太仆寺旗', '太仆寺旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152528, 24, 152500, '镶黄旗', '镶黄旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152529, 24, 152500, '正镶白旗', '正镶白旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152530, 24, 152500, '正蓝旗', '正蓝旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152531, 24, 152500, '多伦县', '多伦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152900, 23, 150000, '阿拉善盟', '阿拉善盟');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152921, 24, 152900, '阿拉善左旗', '阿拉善左旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152922, 24, 152900, '阿拉善右旗', '阿拉善右旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (152923, 24, 152900, '额济纳旗', '额济纳旗');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210000, 22, NULL, '辽宁', '辽宁省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210100, 23, 210000, '沈阳', '沈阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210101, 24, 210100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210102, 24, 210100, '和平区', '和平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210103, 24, 210100, '沈河区', '沈河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210104, 24, 210100, '大东区', '大东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210105, 24, 210100, '皇姑区', '皇姑区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210106, 24, 210100, '铁西区', '铁西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210111, 24, 210100, '苏家屯区', '苏家屯区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210112, 24, 210100, '东陵区', '东陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210113, 24, 210100, '沈北新区', '沈北新区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210114, 24, 210100, '于洪区', '于洪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210122, 24, 210100, '辽中县', '辽中县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210123, 24, 210100, '康平县', '康平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210124, 24, 210100, '法库县', '法库县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210181, 24, 210100, '新民', '新民市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210200, 23, 210000, '大连', '大连市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210201, 24, 210200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210202, 24, 210200, '中山区', '中山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210203, 24, 210200, '西岗区', '西岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210204, 24, 210200, '沙河口区', '沙河口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210211, 24, 210200, '甘井子区', '甘井子区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210212, 24, 210200, '旅顺口区', '旅顺口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210213, 24, 210200, '金州区', '金州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210224, 24, 210200, '长海县', '长海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210281, 24, 210200, '瓦房店', '瓦房店市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210282, 24, 210200, '普兰店', '普兰店市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210283, 24, 210200, '庄河', '庄河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210300, 23, 210000, '鞍山', '鞍山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210301, 24, 210300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210302, 24, 210300, '铁东区', '铁东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210303, 24, 210300, '铁西区', '铁西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210304, 24, 210300, '立山区', '立山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210311, 24, 210300, '千山区', '千山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210321, 24, 210300, '台安县', '台安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210323, 24, 210300, '岫岩满族自治县', '岫岩满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210381, 24, 210300, '海城', '海城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210400, 23, 210000, '抚顺', '抚顺市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210401, 24, 210400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210402, 24, 210400, '新抚区', '新抚区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210403, 24, 210400, '东洲区', '东洲区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210404, 24, 210400, '望花区', '望花区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210411, 24, 210400, '顺城区', '顺城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210421, 24, 210400, '抚顺县', '抚顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210422, 24, 210400, '新宾满族自治县', '新宾满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210423, 24, 210400, '清原满族自治县', '清原满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210500, 23, 210000, '本溪', '本溪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210501, 24, 210500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210502, 24, 210500, '平山区', '平山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210503, 24, 210500, '溪湖区', '溪湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210504, 24, 210500, '明山区', '明山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210505, 24, 210500, '南芬区', '南芬区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210521, 24, 210500, '本溪满族自治县', '本溪满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210522, 24, 210500, '桓仁满族自治县', '桓仁满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210600, 23, 210000, '丹东', '丹东市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210601, 24, 210600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210602, 24, 210600, '元宝区', '元宝区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210603, 24, 210600, '振兴区', '振兴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210604, 24, 210600, '振安区', '振安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210624, 24, 210600, '宽甸满族自治县', '宽甸满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210681, 24, 210600, '东港', '东港市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210682, 24, 210600, '凤城', '凤城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210700, 23, 210000, '锦州', '锦州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210701, 24, 210700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210702, 24, 210700, '古塔区', '古塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210703, 24, 210700, '凌河区', '凌河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210711, 24, 210700, '太和区', '太和区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210726, 24, 210700, '黑山县', '黑山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210727, 24, 210700, '义县', '义县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210781, 24, 210700, '凌海', '凌海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210782, 24, 210700, '北镇', '北镇市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210800, 23, 210000, '营口', '营口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210801, 24, 210800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210802, 24, 210800, '站前区', '站前区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210803, 24, 210800, '西市区', '西市区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210804, 24, 210800, '鲅鱼圈区', '鲅鱼圈区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210811, 24, 210800, '老边区', '老边区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210881, 24, 210800, '盖州', '盖州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210882, 24, 210800, '大石桥', '大石桥市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210900, 23, 210000, '阜新', '阜新市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210901, 24, 210900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210902, 24, 210900, '海州区', '海州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210903, 24, 210900, '新邱区', '新邱区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210904, 24, 210900, '太平区', '太平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210905, 24, 210900, '清河门区', '清河门区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210911, 24, 210900, '细河区', '细河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210921, 24, 210900, '阜新蒙古族自治县', '阜新蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (210922, 24, 210900, '彰武县', '彰武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211000, 23, 210000, '辽阳', '辽阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211001, 24, 211000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211002, 24, 211000, '白塔区', '白塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211003, 24, 211000, '文圣区', '文圣区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211004, 24, 211000, '宏伟区', '宏伟区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211005, 24, 211000, '弓长岭区', '弓长岭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211011, 24, 211000, '太子河区', '太子河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211021, 24, 211000, '辽阳县', '辽阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211081, 24, 211000, '灯塔', '灯塔市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211100, 23, 210000, '盘锦', '盘锦市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211101, 24, 211100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211102, 24, 211100, '双台子区', '双台子区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211103, 24, 211100, '兴隆台区', '兴隆台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211121, 24, 211100, '大洼县', '大洼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211122, 24, 211100, '盘山县', '盘山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211200, 23, 210000, '铁岭', '铁岭市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211201, 24, 211200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211202, 24, 211200, '银州区', '银州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211204, 24, 211200, '清河区', '清河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211221, 24, 211200, '铁岭县', '铁岭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211223, 24, 211200, '西丰县', '西丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211224, 24, 211200, '昌图县', '昌图县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211281, 24, 211200, '调兵山', '调兵山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211282, 24, 211200, '开原', '开原市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211300, 23, 210000, '朝阳', '朝阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211301, 24, 211300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211302, 24, 211300, '双塔区', '双塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211303, 24, 211300, '龙城区', '龙城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211321, 24, 211300, '朝阳县', '朝阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211322, 24, 211300, '建平县', '建平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211324, 24, 211300, '喀喇沁左翼蒙古族自治县', '喀喇沁左翼蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211381, 24, 211300, '北票', '北票市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211382, 24, 211300, '凌源', '凌源市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211400, 23, 210000, '葫芦岛', '葫芦岛市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211401, 24, 211400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211402, 24, 211400, '连山区', '连山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211403, 24, 211400, '龙港区', '龙港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211404, 24, 211400, '南票区', '南票区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211421, 24, 211400, '绥中县', '绥中县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211422, 24, 211400, '建昌县', '建昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (211481, 24, 211400, '兴城', '兴城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220000, 22, NULL, '吉林', '吉林省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220100, 23, 220000, '长春', '长春市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220101, 24, 220100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220102, 24, 220100, '南关区', '南关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220103, 24, 220100, '宽城区', '宽城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220104, 24, 220100, '朝阳区', '朝阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220105, 24, 220100, '二道区', '二道区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220106, 24, 220100, '绿园区', '绿园区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220112, 24, 220100, '双阳区', '双阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220122, 24, 220100, '农安县', '农安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220181, 24, 220100, '九台', '九台市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220182, 24, 220100, '榆树', '榆树市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220183, 24, 220100, '德惠', '德惠市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220200, 23, 220000, '吉林', '吉林市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220201, 24, 220200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220202, 24, 220200, '昌邑区', '昌邑区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220203, 24, 220200, '龙潭区', '龙潭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220204, 24, 220200, '船营区', '船营区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220211, 24, 220200, '丰满区', '丰满区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220221, 24, 220200, '永吉县', '永吉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220281, 24, 220200, '蛟河', '蛟河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220282, 24, 220200, '桦甸', '桦甸市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220283, 24, 220200, '舒兰', '舒兰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220284, 24, 220200, '磐石', '磐石市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220300, 23, 220000, '四平', '四平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220301, 24, 220300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220302, 24, 220300, '铁西区', '铁西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220303, 24, 220300, '铁东区', '铁东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220322, 24, 220300, '梨树县', '梨树县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220323, 24, 220300, '伊通满族自治县', '伊通满族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220381, 24, 220300, '公主岭', '公主岭市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220382, 24, 220300, '双辽', '双辽市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220400, 23, 220000, '辽源', '辽源市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220401, 24, 220400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220402, 24, 220400, '龙山区', '龙山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220403, 24, 220400, '西安区', '西安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220421, 24, 220400, '东丰县', '东丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220422, 24, 220400, '东辽县', '东辽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220500, 23, 220000, '通化', '通化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220501, 24, 220500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220502, 24, 220500, '东昌区', '东昌区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220503, 24, 220500, '二道江区', '二道江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220521, 24, 220500, '通化县', '通化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220523, 24, 220500, '辉南县', '辉南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220524, 24, 220500, '柳河县', '柳河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220581, 24, 220500, '梅河口', '梅河口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220582, 24, 220500, '集安', '集安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220600, 23, 220000, '白山', '白山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220601, 24, 220600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220602, 24, 220600, '八道江区', '八道江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220605, 24, 220600, '江源区', '江源区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220621, 24, 220600, '抚松县', '抚松县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220622, 24, 220600, '靖宇县', '靖宇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220623, 24, 220600, '长白朝鲜族自治县', '长白朝鲜族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220681, 24, 220600, '临江', '临江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220700, 23, 220000, '松原', '松原市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220701, 24, 220700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220702, 24, 220700, '宁江区', '宁江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220721, 24, 220700, '前郭尔罗斯蒙古族自治县', '前郭尔罗斯蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220722, 24, 220700, '长岭县', '长岭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220723, 24, 220700, '乾安县', '乾安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220724, 24, 220700, '扶余县', '扶余县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220800, 23, 220000, '白城', '白城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220801, 24, 220800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220802, 24, 220800, '洮北区', '洮北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220821, 24, 220800, '镇赉县', '镇赉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220822, 24, 220800, '通榆县', '通榆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220881, 24, 220800, '洮南', '洮南市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (220882, 24, 220800, '大安', '大安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222400, 23, 220000, '延边朝鲜族自治州', '延边朝鲜族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222401, 24, 222400, '延吉', '延吉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222402, 24, 222400, '图们', '图们市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222403, 24, 222400, '敦化', '敦化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222404, 24, 222400, '珲春', '珲春市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222405, 24, 222400, '龙井', '龙井市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222406, 24, 222400, '和龙', '和龙市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222424, 24, 222400, '汪清县', '汪清县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (222426, 24, 222400, '安图县', '安图县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230000, 22, NULL, '黑龙江', '黑龙江省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230100, 23, 230000, '哈尔滨', '哈尔滨市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230101, 24, 230100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230102, 24, 230100, '道里区', '道里区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230103, 24, 230100, '南岗区', '南岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230104, 24, 230100, '道外区', '道外区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230108, 24, 230100, '平房区', '平房区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230109, 24, 230100, '松北区', '松北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230110, 24, 230100, '香坊区', '香坊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230111, 24, 230100, '呼兰区', '呼兰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230112, 24, 230100, '阿城区', '阿城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230123, 24, 230100, '依兰县', '依兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230124, 24, 230100, '方正县', '方正县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230125, 24, 230100, '宾县', '宾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230126, 24, 230100, '巴彦县', '巴彦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230127, 24, 230100, '木兰县', '木兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230128, 24, 230100, '通河县', '通河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230129, 24, 230100, '延寿县', '延寿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230182, 24, 230100, '双城', '双城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230183, 24, 230100, '尚志', '尚志市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230184, 24, 230100, '五常', '五常市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230200, 23, 230000, '齐齐哈尔', '齐齐哈尔市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230201, 24, 230200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230202, 24, 230200, '龙沙区', '龙沙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230203, 24, 230200, '建华区', '建华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230204, 24, 230200, '铁锋区', '铁锋区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230205, 24, 230200, '昂昂溪区', '昂昂溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230206, 24, 230200, '富拉尔基区', '富拉尔基区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230207, 24, 230200, '碾子山区', '碾子山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230208, 24, 230200, '梅里斯达斡尔族区', '梅里斯达斡尔族区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230221, 24, 230200, '龙江县', '龙江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230223, 24, 230200, '依安县', '依安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230224, 24, 230200, '泰来县', '泰来县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230225, 24, 230200, '甘南县', '甘南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230227, 24, 230200, '富裕县', '富裕县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230229, 24, 230200, '克山县', '克山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230230, 24, 230200, '克东县', '克东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230231, 24, 230200, '拜泉县', '拜泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230281, 24, 230200, '讷河', '讷河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230300, 23, 230000, '鸡西', '鸡西市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230301, 24, 230300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230302, 24, 230300, '鸡冠区', '鸡冠区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230303, 24, 230300, '恒山区', '恒山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230304, 24, 230300, '滴道区', '滴道区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230305, 24, 230300, '梨树区', '梨树区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230306, 24, 230300, '城子河区', '城子河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230307, 24, 230300, '麻山区', '麻山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230321, 24, 230300, '鸡东县', '鸡东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230381, 24, 230300, '虎林', '虎林市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230382, 24, 230300, '密山', '密山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230400, 23, 230000, '鹤岗', '鹤岗市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230401, 24, 230400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230402, 24, 230400, '向阳区', '向阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230403, 24, 230400, '工农区', '工农区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230404, 24, 230400, '南山区', '南山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230405, 24, 230400, '兴安区', '兴安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230406, 24, 230400, '东山区', '东山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230407, 24, 230400, '兴山区', '兴山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230421, 24, 230400, '萝北县', '萝北县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230422, 24, 230400, '绥滨县', '绥滨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230500, 23, 230000, '双鸭山', '双鸭山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230501, 24, 230500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230502, 24, 230500, '尖山区', '尖山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230503, 24, 230500, '岭东区', '岭东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230505, 24, 230500, '四方台区', '四方台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230506, 24, 230500, '宝山区', '宝山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230521, 24, 230500, '集贤县', '集贤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230522, 24, 230500, '友谊县', '友谊县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230523, 24, 230500, '宝清县', '宝清县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230524, 24, 230500, '饶河县', '饶河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230600, 23, 230000, '大庆', '大庆市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230601, 24, 230600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230602, 24, 230600, '萨尔图区', '萨尔图区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230603, 24, 230600, '龙凤区', '龙凤区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230604, 24, 230600, '让胡路区', '让胡路区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230605, 24, 230600, '红岗区', '红岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230606, 24, 230600, '大同区', '大同区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230621, 24, 230600, '肇州县', '肇州县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230622, 24, 230600, '肇源县', '肇源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230623, 24, 230600, '林甸县', '林甸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230624, 24, 230600, '杜尔伯特蒙古族自治县', '杜尔伯特蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230700, 23, 230000, '伊春', '伊春市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230701, 24, 230700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230702, 24, 230700, '伊春区', '伊春区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230703, 24, 230700, '南岔区', '南岔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230704, 24, 230700, '友好区', '友好区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230705, 24, 230700, '西林区', '西林区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230706, 24, 230700, '翠峦区', '翠峦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230707, 24, 230700, '新青区', '新青区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230708, 24, 230700, '美溪区', '美溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230709, 24, 230700, '金山屯区', '金山屯区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230710, 24, 230700, '五营区', '五营区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230711, 24, 230700, '乌马河区', '乌马河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230712, 24, 230700, '汤旺河区', '汤旺河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230713, 24, 230700, '带岭区', '带岭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230714, 24, 230700, '乌伊岭区', '乌伊岭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230715, 24, 230700, '红星区', '红星区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230716, 24, 230700, '上甘岭区', '上甘岭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230722, 24, 230700, '嘉荫县', '嘉荫县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230781, 24, 230700, '铁力', '铁力市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230800, 23, 230000, '佳木斯', '佳木斯市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230801, 24, 230800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230803, 24, 230800, '向阳区', '向阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230804, 24, 230800, '前进区', '前进区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230805, 24, 230800, '东风区', '东风区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230811, 24, 230800, '郊区', '郊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230822, 24, 230800, '桦南县', '桦南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230826, 24, 230800, '桦川县', '桦川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230828, 24, 230800, '汤原县', '汤原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230833, 24, 230800, '抚远县', '抚远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230881, 24, 230800, '同江', '同江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230882, 24, 230800, '富锦', '富锦市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230900, 23, 230000, '七台河', '七台河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230901, 24, 230900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230902, 24, 230900, '新兴区', '新兴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230903, 24, 230900, '桃山区', '桃山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230904, 24, 230900, '茄子河区', '茄子河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (230921, 24, 230900, '勃利县', '勃利县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231000, 23, 230000, '牡丹江', '牡丹江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231001, 24, 231000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231002, 24, 231000, '东安区', '东安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231003, 24, 231000, '阳明区', '阳明区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231004, 24, 231000, '爱民区', '爱民区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231005, 24, 231000, '西安区', '西安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231024, 24, 231000, '东宁县', '东宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231025, 24, 231000, '林口县', '林口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231081, 24, 231000, '绥芬河', '绥芬河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231083, 24, 231000, '海林', '海林市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231084, 24, 231000, '宁安', '宁安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231085, 24, 231000, '穆棱', '穆棱市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231100, 23, 230000, '黑河', '黑河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231101, 24, 231100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231102, 24, 231100, '爱辉区', '爱辉区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231121, 24, 231100, '嫩江县', '嫩江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231123, 24, 231100, '逊克县', '逊克县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231124, 24, 231100, '孙吴县', '孙吴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231181, 24, 231100, '北安', '北安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231182, 24, 231100, '五大连池', '五大连池市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231200, 23, 230000, '绥化', '绥化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231201, 24, 231200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231202, 24, 231200, '北林区', '北林区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231221, 24, 231200, '望奎县', '望奎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231222, 24, 231200, '兰西县', '兰西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231223, 24, 231200, '青冈县', '青冈县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231224, 24, 231200, '庆安县', '庆安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231225, 24, 231200, '明水县', '明水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231226, 24, 231200, '绥棱县', '绥棱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231281, 24, 231200, '安达', '安达市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231282, 24, 231200, '肇东', '肇东市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (231283, 24, 231200, '海伦', '海伦市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (232700, 23, 230000, '大兴安岭地区', '大兴安岭地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (232721, 24, 232700, '呼玛县', '呼玛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (232722, 24, 232700, '塔河县', '塔河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (232723, 24, 232700, '漠河县', '漠河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310000, 22, NULL, '上海', '上海');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310100, 23, 310000, '上海', '上海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310101, 24, 310100, '黄浦区', '黄浦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310104, 24, 310100, '徐汇区', '徐汇区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310105, 24, 310100, '长宁区', '长宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310106, 24, 310100, '静安区', '静安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310107, 24, 310100, '普陀区', '普陀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310108, 24, 310100, '闸北区', '闸北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310109, 24, 310100, '虹口区', '虹口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310110, 24, 310100, '杨浦区', '杨浦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310112, 24, 310100, '闵行区', '闵行区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310113, 24, 310100, '宝山区', '宝山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310114, 24, 310100, '嘉定区', '嘉定区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310115, 24, 310100, '浦东新区', '浦东新区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310116, 24, 310100, '金山区', '金山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310117, 24, 310100, '松江区', '松江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310118, 24, 310100, '青浦区', '青浦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310120, 24, 310100, '奉贤区', '奉贤区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310230, 24, 310200, '崇明县', '崇明县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320000, 22, NULL, '江苏', '江苏省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320100, 23, 320000, '南京', '南京市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320101, 24, 320100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320102, 24, 320100, '玄武区', '玄武区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320103, 24, 320100, '白下区', '白下区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320104, 24, 320100, '秦淮区', '秦淮区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320105, 24, 320100, '建邺区', '建邺区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320106, 24, 320100, '鼓楼区', '鼓楼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320107, 24, 320100, '下关区', '下关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320111, 24, 320100, '浦口区', '浦口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320113, 24, 320100, '栖霞区', '栖霞区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320114, 24, 320100, '雨花台区', '雨花台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320115, 24, 320100, '江宁区', '江宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320116, 24, 320100, '六合区', '六合区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320124, 24, 320100, '溧水县', '溧水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320125, 24, 320100, '高淳县', '高淳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320200, 23, 320000, '无锡', '无锡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320201, 24, 320200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320202, 24, 320200, '崇安区', '崇安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320203, 24, 320200, '南长区', '南长区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320204, 24, 320200, '北塘区', '北塘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320205, 24, 320200, '锡山区', '锡山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320206, 24, 320200, '惠山区', '惠山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320211, 24, 320200, '滨湖区', '滨湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320281, 24, 320200, '江阴', '江阴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320282, 24, 320200, '宜兴', '宜兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320300, 23, 320000, '徐州', '徐州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320301, 24, 320300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320302, 24, 320300, '鼓楼区', '鼓楼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320303, 24, 320300, '云龙区', '云龙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320305, 24, 320300, '贾汪区', '贾汪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320311, 24, 320300, '泉山区', '泉山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320312, 24, 320300, '铜山区', '铜山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320321, 24, 320300, '丰县', '丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320322, 24, 320300, '沛县', '沛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320324, 24, 320300, '睢宁县', '睢宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320381, 24, 320300, '新沂', '新沂市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320382, 24, 320300, '邳州', '邳州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320400, 23, 320000, '常州', '常州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320401, 24, 320400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320402, 24, 320400, '天宁区', '天宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320404, 24, 320400, '钟楼区', '钟楼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320405, 24, 320400, '戚墅堰区', '戚墅堰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320411, 24, 320400, '新北区', '新北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320412, 24, 320400, '武进区', '武进区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320481, 24, 320400, '溧阳', '溧阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320482, 24, 320400, '金坛', '金坛市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320500, 23, 320000, '苏州', '苏州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320501, 24, 320500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320502, 24, 320500, '沧浪区', '沧浪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320503, 24, 320500, '平江区', '平江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320504, 24, 320500, '金阊区', '金阊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320505, 24, 320500, '虎丘区', '虎丘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320506, 24, 320500, '吴中区', '吴中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320507, 24, 320500, '相城区', '相城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320581, 24, 320500, '常熟', '常熟市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320582, 24, 320500, '张家港', '张家港市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320583, 24, 320500, '昆山', '昆山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320584, 24, 320500, '吴江', '吴江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320585, 24, 320500, '太仓', '太仓市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320600, 23, 320000, '南通', '南通市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320601, 24, 320600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320602, 24, 320600, '崇川区', '崇川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320611, 24, 320600, '港闸区', '港闸区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320612, 24, 320600, '通州区', '通州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320621, 24, 320600, '海安县', '海安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320623, 24, 320600, '如东县', '如东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320681, 24, 320600, '启东', '启东市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320682, 24, 320600, '如皋', '如皋市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320684, 24, 320600, '海门', '海门市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320700, 23, 320000, '连云港', '连云港市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320701, 24, 320700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320703, 24, 320700, '连云区', '连云区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320705, 24, 320700, '新浦区', '新浦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320706, 24, 320700, '海州区', '海州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320721, 24, 320700, '赣榆县', '赣榆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320722, 24, 320700, '东海县', '东海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320723, 24, 320700, '灌云县', '灌云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320724, 24, 320700, '灌南县', '灌南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320800, 23, 320000, '淮安', '淮安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320801, 24, 320800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320802, 24, 320800, '清河区', '清河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320803, 24, 320800, '楚州区', '楚州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320804, 24, 320800, '淮阴区', '淮阴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320811, 24, 320800, '清浦区', '清浦区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320826, 24, 320800, '涟水县', '涟水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320829, 24, 320800, '洪泽县', '洪泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320830, 24, 320800, '盱眙县', '盱眙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320831, 24, 320800, '金湖县', '金湖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320900, 23, 320000, '盐城', '盐城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320901, 24, 320900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320902, 24, 320900, '亭湖区', '亭湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320903, 24, 320900, '盐都区', '盐都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320921, 24, 320900, '响水县', '响水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320922, 24, 320900, '滨海县', '滨海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320923, 24, 320900, '阜宁县', '阜宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320924, 24, 320900, '射阳县', '射阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320925, 24, 320900, '建湖县', '建湖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320981, 24, 320900, '东台', '东台市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (320982, 24, 320900, '大丰', '大丰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321000, 23, 320000, '扬州', '扬州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321001, 24, 321000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321002, 24, 321000, '广陵区', '广陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321003, 24, 321000, '邗江区', '邗江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321012, 24, 321000, '江都区', '江都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321023, 24, 321000, '宝应县', '宝应县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321081, 24, 321000, '仪征', '仪征市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321084, 24, 321000, '高邮', '高邮市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321100, 23, 320000, '镇江', '镇江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321101, 24, 321100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321102, 24, 321100, '京口区', '京口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321111, 24, 321100, '润州区', '润州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321112, 24, 321100, '丹徒区', '丹徒区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321181, 24, 321100, '丹阳', '丹阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321182, 24, 321100, '扬中', '扬中市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321183, 24, 321100, '句容', '句容市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321200, 23, 320000, '泰州', '泰州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321201, 24, 321200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321202, 24, 321200, '海陵区', '海陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321203, 24, 321200, '高港区', '高港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321281, 24, 321200, '兴化', '兴化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321282, 24, 321200, '靖江', '靖江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321283, 24, 321200, '泰兴', '泰兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321284, 24, 321200, '姜堰', '姜堰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321300, 23, 320000, '宿迁', '宿迁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321301, 24, 321300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321302, 24, 321300, '宿城区', '宿城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321311, 24, 321300, '宿豫区', '宿豫区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321322, 24, 321300, '沭阳县', '沭阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321323, 24, 321300, '泗阳县', '泗阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (321324, 24, 321300, '泗洪县', '泗洪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330000, 22, NULL, '浙江', '浙江省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330100, 23, 330000, '杭州', '杭州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330101, 24, 330100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330102, 24, 330100, '上城区', '上城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330103, 24, 330100, '下城区', '下城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330104, 24, 330100, '江干区', '江干区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330105, 24, 330100, '拱墅区', '拱墅区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330106, 24, 330100, '西湖区', '西湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330108, 24, 330100, '滨江区', '滨江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330109, 24, 330100, '萧山区', '萧山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330110, 24, 330100, '余杭区', '余杭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330122, 24, 330100, '桐庐县', '桐庐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330127, 24, 330100, '淳安县', '淳安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330182, 24, 330100, '建德', '建德市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330183, 24, 330100, '富阳', '富阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330185, 24, 330100, '临安', '临安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330200, 23, 330000, '宁波', '宁波市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330201, 24, 330200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330203, 24, 330200, '海曙区', '海曙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330204, 24, 330200, '江东区', '江东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330205, 24, 330200, '江北区', '江北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330206, 24, 330200, '北仑区', '北仑区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330211, 24, 330200, '镇海区', '镇海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330212, 24, 330200, '鄞州区', '鄞州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330225, 24, 330200, '象山县', '象山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330226, 24, 330200, '宁海县', '宁海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330281, 24, 330200, '余姚', '余姚市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330282, 24, 330200, '慈溪', '慈溪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330283, 24, 330200, '奉化', '奉化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330300, 23, 330000, '温州', '温州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330301, 24, 330300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330302, 24, 330300, '鹿城区', '鹿城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330303, 24, 330300, '龙湾区', '龙湾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330304, 24, 330300, '瓯海区', '瓯海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330322, 24, 330300, '洞头县', '洞头县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330324, 24, 330300, '永嘉县', '永嘉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330326, 24, 330300, '平阳县', '平阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330327, 24, 330300, '苍南县', '苍南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330328, 24, 330300, '文成县', '文成县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330329, 24, 330300, '泰顺县', '泰顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330381, 24, 330300, '瑞安', '瑞安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330382, 24, 330300, '乐清', '乐清市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330400, 23, 330000, '嘉兴', '嘉兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330401, 24, 330400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330402, 24, 330400, '南湖区', '南湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330411, 24, 330400, '秀洲区', '秀洲区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330421, 24, 330400, '嘉善县', '嘉善县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330424, 24, 330400, '海盐县', '海盐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330481, 24, 330400, '海宁', '海宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330482, 24, 330400, '平湖', '平湖市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330483, 24, 330400, '桐乡', '桐乡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330500, 23, 330000, '湖州', '湖州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330501, 24, 330500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330502, 24, 330500, '吴兴区', '吴兴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330503, 24, 330500, '南浔区', '南浔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330521, 24, 330500, '德清县', '德清县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330522, 24, 330500, '长兴县', '长兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330523, 24, 330500, '安吉县', '安吉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330600, 23, 330000, '绍兴', '绍兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330601, 24, 330600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330602, 24, 330600, '越城区', '越城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330621, 24, 330600, '绍兴县', '绍兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330624, 24, 330600, '新昌县', '新昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330681, 24, 330600, '诸暨', '诸暨市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330682, 24, 330600, '上虞', '上虞市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330683, 24, 330600, '嵊州', '嵊州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330700, 23, 330000, '金华', '金华市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330701, 24, 330700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330702, 24, 330700, '婺城区', '婺城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330703, 24, 330700, '金东区', '金东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330723, 24, 330700, '武义县', '武义县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330726, 24, 330700, '浦江县', '浦江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330727, 24, 330700, '磐安县', '磐安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330781, 24, 330700, '兰溪', '兰溪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330782, 24, 330700, '义乌', '义乌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330783, 24, 330700, '东阳', '东阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330784, 24, 330700, '永康', '永康市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330800, 23, 330000, '衢州', '衢州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330801, 24, 330800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330802, 24, 330800, '柯城区', '柯城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330803, 24, 330800, '衢江区', '衢江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330822, 24, 330800, '常山县', '常山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330824, 24, 330800, '开化县', '开化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330825, 24, 330800, '龙游县', '龙游县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330881, 24, 330800, '江山', '江山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330900, 23, 330000, '舟山', '舟山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330901, 24, 330900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330902, 24, 330900, '定海区', '定海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330903, 24, 330900, '普陀区', '普陀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330921, 24, 330900, '岱山县', '岱山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (330922, 24, 330900, '嵊泗县', '嵊泗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331000, 23, 330000, '台州', '台州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331001, 24, 331000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331002, 24, 331000, '椒江区', '椒江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331003, 24, 331000, '黄岩区', '黄岩区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331004, 24, 331000, '路桥区', '路桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331021, 24, 331000, '玉环县', '玉环县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331022, 24, 331000, '三门县', '三门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331023, 24, 331000, '天台县', '天台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331024, 24, 331000, '仙居县', '仙居县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331081, 24, 331000, '温岭', '温岭市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331082, 24, 331000, '临海', '临海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331100, 23, 330000, '丽水', '丽水市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331101, 24, 331100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331102, 24, 331100, '莲都区', '莲都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331121, 24, 331100, '青田县', '青田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331122, 24, 331100, '缙云县', '缙云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331123, 24, 331100, '遂昌县', '遂昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331124, 24, 331100, '松阳县', '松阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331125, 24, 331100, '云和县', '云和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331126, 24, 331100, '庆元县', '庆元县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331127, 24, 331100, '景宁畲族自治县', '景宁畲族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (331181, 24, 331100, '龙泉', '龙泉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340000, 22, NULL, '安徽', '安徽省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340100, 23, 340000, '合肥', '合肥市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340101, 24, 340100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340102, 24, 340100, '瑶海区', '瑶海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340103, 24, 340100, '庐阳区', '庐阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340104, 24, 340100, '蜀山区', '蜀山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340111, 24, 340100, '包河区', '包河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340121, 24, 340100, '长丰县', '长丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340122, 24, 340100, '肥东县', '肥东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340123, 24, 340100, '肥西县', '肥西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340124, 24, 340100, '庐江县', '庐江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340181, 24, 340100, '巢湖', '巢湖市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340200, 23, 340000, '芜湖', '芜湖市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340201, 24, 340200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340202, 24, 340200, '镜湖区', '镜湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340203, 24, 340200, '弋江区', '弋江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340207, 24, 340200, '鸠江区', '鸠江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340208, 24, 340200, '三山区', '三山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340221, 24, 340200, '芜湖县', '芜湖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340222, 24, 340200, '繁昌县', '繁昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340223, 24, 340200, '南陵县', '南陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340225, 24, 340200, '无为县', '无为县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340300, 23, 340000, '蚌埠', '蚌埠市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340301, 24, 340300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340302, 24, 340300, '龙子湖区', '龙子湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340303, 24, 340300, '蚌山区', '蚌山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340304, 24, 340300, '禹会区', '禹会区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340311, 24, 340300, '淮上区', '淮上区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340321, 24, 340300, '怀远县', '怀远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340322, 24, 340300, '五河县', '五河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340323, 24, 340300, '固镇县', '固镇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340400, 23, 340000, '淮南', '淮南市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340401, 24, 340400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340402, 24, 340400, '大通区', '大通区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340403, 24, 340400, '田家庵区', '田家庵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340404, 24, 340400, '谢家集区', '谢家集区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340405, 24, 340400, '八公山区', '八公山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340406, 24, 340400, '潘集区', '潘集区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340421, 24, 340400, '凤台县', '凤台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340500, 23, 340000, '马鞍山', '马鞍山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340501, 24, 340500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340502, 24, 340500, '金家庄区', '金家庄区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340503, 24, 340500, '花山区', '花山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340504, 24, 340500, '雨山区', '雨山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340521, 24, 340500, '当涂县', '当涂县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340522, 24, 340500, '含山县', '含山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340523, 24, 340500, '和县', '和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340600, 23, 340000, '淮北', '淮北市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340601, 24, 340600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340602, 24, 340600, '杜集区', '杜集区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340603, 24, 340600, '相山区', '相山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340604, 24, 340600, '烈山区', '烈山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340621, 24, 340600, '濉溪县', '濉溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340700, 23, 340000, '铜陵', '铜陵市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340701, 24, 340700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340702, 24, 340700, '铜官山区', '铜官山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340703, 24, 340700, '狮子山区', '狮子山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340711, 24, 340700, '郊区', '郊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340721, 24, 340700, '铜陵县', '铜陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340800, 23, 340000, '安庆', '安庆市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340801, 24, 340800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340802, 24, 340800, '迎江区', '迎江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340803, 24, 340800, '大观区', '大观区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340811, 24, 340800, '宜秀区', '宜秀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340822, 24, 340800, '怀宁县', '怀宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340823, 24, 340800, '枞阳县', '枞阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340824, 24, 340800, '潜山县', '潜山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340825, 24, 340800, '太湖县', '太湖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340826, 24, 340800, '宿松县', '宿松县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340827, 24, 340800, '望江县', '望江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340828, 24, 340800, '岳西县', '岳西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (340881, 24, 340800, '桐城', '桐城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341000, 23, 340000, '黄山', '黄山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341001, 24, 341000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341002, 24, 341000, '屯溪区', '屯溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341003, 24, 341000, '黄山区', '黄山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341004, 24, 341000, '徽州区', '徽州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341021, 24, 341000, '歙县', '歙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341022, 24, 341000, '休宁县', '休宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341023, 24, 341000, '黟县', '黟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341024, 24, 341000, '祁门县', '祁门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341100, 23, 340000, '滁州', '滁州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341101, 24, 341100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341102, 24, 341100, '琅琊区', '琅琊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341103, 24, 341100, '南谯区', '南谯区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341122, 24, 341100, '来安县', '来安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341124, 24, 341100, '全椒县', '全椒县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341125, 24, 341100, '定远县', '定远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341126, 24, 341100, '凤阳县', '凤阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341181, 24, 341100, '天长', '天长市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341182, 24, 341100, '明光', '明光市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341200, 23, 340000, '阜阳', '阜阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341201, 24, 341200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341202, 24, 341200, '颍州区', '颍州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341203, 24, 341200, '颍东区', '颍东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341204, 24, 341200, '颍泉区', '颍泉区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341221, 24, 341200, '临泉县', '临泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341222, 24, 341200, '太和县', '太和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341225, 24, 341200, '阜南县', '阜南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341226, 24, 341200, '颍上县', '颍上县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341282, 24, 341200, '界首', '界首市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341300, 23, 340000, '宿州', '宿州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341301, 24, 341300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341302, 24, 341300, '埇桥区', '埇桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341321, 24, 341300, '砀山县', '砀山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341322, 24, 341300, '萧县', '萧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341323, 24, 341300, '灵璧县', '灵璧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341324, 24, 341300, '泗县', '泗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341500, 23, 340000, '六安', '六安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341501, 24, 341500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341502, 24, 341500, '金安区', '金安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341503, 24, 341500, '裕安区', '裕安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341521, 24, 341500, '寿县', '寿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341522, 24, 341500, '霍邱县', '霍邱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341523, 24, 341500, '舒城县', '舒城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341524, 24, 341500, '金寨县', '金寨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341525, 24, 341500, '霍山县', '霍山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341600, 23, 340000, '亳州', '亳州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341601, 24, 341600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341602, 24, 341600, '谯城区', '谯城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341621, 24, 341600, '涡阳县', '涡阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341622, 24, 341600, '蒙城县', '蒙城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341623, 24, 341600, '利辛县', '利辛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341700, 23, 340000, '池州', '池州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341701, 24, 341700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341702, 24, 341700, '贵池区', '贵池区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341721, 24, 341700, '东至县', '东至县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341722, 24, 341700, '石台县', '石台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341723, 24, 341700, '青阳县', '青阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341800, 23, 340000, '宣城', '宣城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341801, 24, 341800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341802, 24, 341800, '宣州区', '宣州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341821, 24, 341800, '郎溪县', '郎溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341822, 24, 341800, '广德县', '广德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341823, 24, 341800, '泾县', '泾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341824, 24, 341800, '绩溪县', '绩溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341825, 24, 341800, '旌德县', '旌德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (341881, 24, 341800, '宁国', '宁国市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350000, 22, NULL, '福建', '福建省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350100, 23, 350000, '福州', '福州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350101, 24, 350100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350102, 24, 350100, '鼓楼区', '鼓楼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350103, 24, 350100, '台江区', '台江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350104, 24, 350100, '仓山区', '仓山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350105, 24, 350100, '马尾区', '马尾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350111, 24, 350100, '晋安区', '晋安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350121, 24, 350100, '闽侯县', '闽侯县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350122, 24, 350100, '连江县', '连江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350123, 24, 350100, '罗源县', '罗源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350124, 24, 350100, '闽清县', '闽清县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350125, 24, 350100, '永泰县', '永泰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350128, 24, 350100, '平潭县', '平潭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350181, 24, 350100, '福清', '福清市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350182, 24, 350100, '长乐', '长乐市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350200, 23, 350000, '厦门', '厦门市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350201, 24, 350200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350203, 24, 350200, '思明区', '思明区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350205, 24, 350200, '海沧区', '海沧区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350206, 24, 350200, '湖里区', '湖里区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350211, 24, 350200, '集美区', '集美区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350212, 24, 350200, '同安区', '同安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350213, 24, 350200, '翔安区', '翔安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350300, 23, 350000, '莆田', '莆田市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350301, 24, 350300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350302, 24, 350300, '城厢区', '城厢区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350303, 24, 350300, '涵江区', '涵江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350304, 24, 350300, '荔城区', '荔城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350305, 24, 350300, '秀屿区', '秀屿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350322, 24, 350300, '仙游县', '仙游县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350400, 23, 350000, '三明', '三明市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350401, 24, 350400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350402, 24, 350400, '梅列区', '梅列区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350403, 24, 350400, '三元区', '三元区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350421, 24, 350400, '明溪县', '明溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350423, 24, 350400, '清流县', '清流县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350424, 24, 350400, '宁化县', '宁化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350425, 24, 350400, '大田县', '大田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350426, 24, 350400, '尤溪县', '尤溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350427, 24, 350400, '沙县', '沙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350428, 24, 350400, '将乐县', '将乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350429, 24, 350400, '泰宁县', '泰宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350430, 24, 350400, '建宁县', '建宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350481, 24, 350400, '永安', '永安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350500, 23, 350000, '泉州', '泉州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350501, 24, 350500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350502, 24, 350500, '鲤城区', '鲤城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350503, 24, 350500, '丰泽区', '丰泽区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350504, 24, 350500, '洛江区', '洛江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350505, 24, 350500, '泉港区', '泉港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350521, 24, 350500, '惠安县', '惠安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350524, 24, 350500, '安溪县', '安溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350525, 24, 350500, '永春县', '永春县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350526, 24, 350500, '德化县', '德化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350527, 24, 350500, '金门县', '金门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350581, 24, 350500, '石狮', '石狮市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350582, 24, 350500, '晋江', '晋江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350583, 24, 350500, '南安', '南安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350600, 23, 350000, '漳州', '漳州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350601, 24, 350600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350602, 24, 350600, '芗城区', '芗城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350603, 24, 350600, '龙文区', '龙文区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350622, 24, 350600, '云霄县', '云霄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350623, 24, 350600, '漳浦县', '漳浦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350624, 24, 350600, '诏安县', '诏安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350625, 24, 350600, '长泰县', '长泰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350626, 24, 350600, '东山县', '东山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350627, 24, 350600, '南靖县', '南靖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350628, 24, 350600, '平和县', '平和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350629, 24, 350600, '华安县', '华安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350681, 24, 350600, '龙海', '龙海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350700, 23, 350000, '南平', '南平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350701, 24, 350700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350702, 24, 350700, '延平区', '延平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350721, 24, 350700, '顺昌县', '顺昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350722, 24, 350700, '浦城县', '浦城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350723, 24, 350700, '光泽县', '光泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350724, 24, 350700, '松溪县', '松溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350725, 24, 350700, '政和县', '政和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350781, 24, 350700, '邵武', '邵武市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350782, 24, 350700, '武夷山', '武夷山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350783, 24, 350700, '建瓯', '建瓯市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350784, 24, 350700, '建阳', '建阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350800, 23, 350000, '龙岩', '龙岩市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350801, 24, 350800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350802, 24, 350800, '新罗区', '新罗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350821, 24, 350800, '长汀县', '长汀县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350822, 24, 350800, '永定县', '永定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350823, 24, 350800, '上杭县', '上杭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350824, 24, 350800, '武平县', '武平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350825, 24, 350800, '连城县', '连城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350881, 24, 350800, '漳平', '漳平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350900, 23, 350000, '宁德', '宁德市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350901, 24, 350900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350902, 24, 350900, '蕉城区', '蕉城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350921, 24, 350900, '霞浦县', '霞浦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350922, 24, 350900, '古田县', '古田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350923, 24, 350900, '屏南县', '屏南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350924, 24, 350900, '寿宁县', '寿宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350925, 24, 350900, '周宁县', '周宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350926, 24, 350900, '柘荣县', '柘荣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350981, 24, 350900, '福安', '福安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (350982, 24, 350900, '福鼎', '福鼎市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360000, 22, NULL, '江西', '江西省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360100, 23, 360000, '南昌', '南昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360101, 24, 360100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360102, 24, 360100, '东湖区', '东湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360103, 24, 360100, '西湖区', '西湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360104, 24, 360100, '青云谱区', '青云谱区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360105, 24, 360100, '湾里区', '湾里区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360111, 24, 360100, '青山湖区', '青山湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360121, 24, 360100, '南昌县', '南昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360122, 24, 360100, '新建县', '新建县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360123, 24, 360100, '安义县', '安义县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360124, 24, 360100, '进贤县', '进贤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360200, 23, 360000, '景德镇', '景德镇市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360201, 24, 360200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360202, 24, 360200, '昌江区', '昌江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360203, 24, 360200, '珠山区', '珠山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360222, 24, 360200, '浮梁县', '浮梁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360281, 24, 360200, '乐平', '乐平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360300, 23, 360000, '萍乡', '萍乡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360301, 24, 360300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360302, 24, 360300, '安源区', '安源区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360313, 24, 360300, '湘东区', '湘东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360321, 24, 360300, '莲花县', '莲花县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360322, 24, 360300, '上栗县', '上栗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360323, 24, 360300, '芦溪县', '芦溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360400, 23, 360000, '九江', '九江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360401, 24, 360400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360402, 24, 360400, '庐山区', '庐山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360403, 24, 360400, '浔阳区', '浔阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360421, 24, 360400, '九江县', '九江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360423, 24, 360400, '武宁县', '武宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360424, 24, 360400, '修水县', '修水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360425, 24, 360400, '永修县', '永修县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360426, 24, 360400, '德安县', '德安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360427, 24, 360400, '星子县', '星子县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360428, 24, 360400, '都昌县', '都昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360429, 24, 360400, '湖口县', '湖口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360430, 24, 360400, '彭泽县', '彭泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360481, 24, 360400, '瑞昌', '瑞昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360482, 24, 360400, '共青城', '共青城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360500, 23, 360000, '新余', '新余市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360501, 24, 360500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360502, 24, 360500, '渝水区', '渝水区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360521, 24, 360500, '分宜县', '分宜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360600, 23, 360000, '鹰潭', '鹰潭市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360601, 24, 360600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360602, 24, 360600, '月湖区', '月湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360622, 24, 360600, '余江县', '余江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360681, 24, 360600, '贵溪', '贵溪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360700, 23, 360000, '赣州', '赣州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360701, 24, 360700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360702, 24, 360700, '章贡区', '章贡区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360721, 24, 360700, '赣县', '赣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360722, 24, 360700, '信丰县', '信丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360723, 24, 360700, '大余县', '大余县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360724, 24, 360700, '上犹县', '上犹县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360725, 24, 360700, '崇义县', '崇义县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360726, 24, 360700, '安远县', '安远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360727, 24, 360700, '龙南县', '龙南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360728, 24, 360700, '定南县', '定南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360729, 24, 360700, '全南县', '全南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360730, 24, 360700, '宁都县', '宁都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360731, 24, 360700, '于都县', '于都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360732, 24, 360700, '兴国县', '兴国县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360733, 24, 360700, '会昌县', '会昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360734, 24, 360700, '寻乌县', '寻乌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360735, 24, 360700, '石城县', '石城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360781, 24, 360700, '瑞金', '瑞金市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360782, 24, 360700, '南康', '南康市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360800, 23, 360000, '吉安', '吉安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360801, 24, 360800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360802, 24, 360800, '吉州区', '吉州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360803, 24, 360800, '青原区', '青原区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360821, 24, 360800, '吉安县', '吉安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360822, 24, 360800, '吉水县', '吉水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360823, 24, 360800, '峡江县', '峡江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360824, 24, 360800, '新干县', '新干县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360825, 24, 360800, '永丰县', '永丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360826, 24, 360800, '泰和县', '泰和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360827, 24, 360800, '遂川县', '遂川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360828, 24, 360800, '万安县', '万安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360829, 24, 360800, '安福县', '安福县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360830, 24, 360800, '永新县', '永新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360881, 24, 360800, '井冈山', '井冈山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360900, 23, 360000, '宜春', '宜春市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360901, 24, 360900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360902, 24, 360900, '袁州区', '袁州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360921, 24, 360900, '奉新县', '奉新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360922, 24, 360900, '万载县', '万载县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360923, 24, 360900, '上高县', '上高县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360924, 24, 360900, '宜丰县', '宜丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360925, 24, 360900, '靖安县', '靖安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360926, 24, 360900, '铜鼓县', '铜鼓县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360981, 24, 360900, '丰城', '丰城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360982, 24, 360900, '樟树', '樟树市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (360983, 24, 360900, '高安', '高安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361000, 23, 360000, '抚州', '抚州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361001, 24, 361000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361002, 24, 361000, '临川区', '临川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361021, 24, 361000, '南城县', '南城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361022, 24, 361000, '黎川县', '黎川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361023, 24, 361000, '南丰县', '南丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361024, 24, 361000, '崇仁县', '崇仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361025, 24, 361000, '乐安县', '乐安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361026, 24, 361000, '宜黄县', '宜黄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361027, 24, 361000, '金溪县', '金溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361028, 24, 361000, '资溪县', '资溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361029, 24, 361000, '东乡县', '东乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361030, 24, 361000, '广昌县', '广昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361100, 23, 360000, '上饶', '上饶市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361101, 24, 361100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361102, 24, 361100, '信州区', '信州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361121, 24, 361100, '上饶县', '上饶县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361122, 24, 361100, '广丰县', '广丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361123, 24, 361100, '玉山县', '玉山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361124, 24, 361100, '铅山县', '铅山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361125, 24, 361100, '横峰县', '横峰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361126, 24, 361100, '弋阳县', '弋阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361127, 24, 361100, '余干县', '余干县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361128, 24, 361100, '鄱阳县', '鄱阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361129, 24, 361100, '万年县', '万年县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361130, 24, 361100, '婺源县', '婺源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (361181, 24, 361100, '德兴', '德兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370000, 22, NULL, '山东', '山东省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370100, 23, 370000, '济南', '济南市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370101, 24, 370100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370102, 24, 370100, '历下区', '历下区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370103, 24, 370100, '市中区', '市中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370104, 24, 370100, '槐荫区', '槐荫区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370105, 24, 370100, '天桥区', '天桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370112, 24, 370100, '历城区', '历城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370113, 24, 370100, '长清区', '长清区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370124, 24, 370100, '平阴县', '平阴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370125, 24, 370100, '济阳县', '济阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370126, 24, 370100, '商河县', '商河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370181, 24, 370100, '章丘', '章丘市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370200, 23, 370000, '青岛', '青岛市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370201, 24, 370200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370202, 24, 370200, '市南区', '市南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370203, 24, 370200, '市北区', '市北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370205, 24, 370200, '四方区', '四方区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370211, 24, 370200, '黄岛区', '黄岛区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370212, 24, 370200, '崂山区', '崂山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370213, 24, 370200, '李沧区', '李沧区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370214, 24, 370200, '城阳区', '城阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370281, 24, 370200, '胶州', '胶州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370282, 24, 370200, '即墨', '即墨市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370283, 24, 370200, '平度', '平度市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370284, 24, 370200, '胶南', '胶南市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370285, 24, 370200, '莱西', '莱西市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370300, 23, 370000, '淄博', '淄博市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370301, 24, 370300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370302, 24, 370300, '淄川区', '淄川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370303, 24, 370300, '张店区', '张店区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370304, 24, 370300, '博山区', '博山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370305, 24, 370300, '临淄区', '临淄区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370306, 24, 370300, '周村区', '周村区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370321, 24, 370300, '桓台县', '桓台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370322, 24, 370300, '高青县', '高青县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370323, 24, 370300, '沂源县', '沂源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370400, 23, 370000, '枣庄', '枣庄市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370401, 24, 370400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370402, 24, 370400, '市中区', '市中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370403, 24, 370400, '薛城区', '薛城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370404, 24, 370400, '峄城区', '峄城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370405, 24, 370400, '台儿庄区', '台儿庄区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370406, 24, 370400, '山亭区', '山亭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370481, 24, 370400, '滕州', '滕州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370500, 23, 370000, '东营', '东营市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370501, 24, 370500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370502, 24, 370500, '东营区', '东营区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370503, 24, 370500, '河口区', '河口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370521, 24, 370500, '垦利县', '垦利县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370522, 24, 370500, '利津县', '利津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370523, 24, 370500, '广饶县', '广饶县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370600, 23, 370000, '烟台', '烟台市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370601, 24, 370600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370602, 24, 370600, '芝罘区', '芝罘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370611, 24, 370600, '福山区', '福山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370612, 24, 370600, '牟平区', '牟平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370613, 24, 370600, '莱山区', '莱山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370634, 24, 370600, '长岛县', '长岛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370681, 24, 370600, '龙口', '龙口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370682, 24, 370600, '莱阳', '莱阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370683, 24, 370600, '莱州', '莱州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370684, 24, 370600, '蓬莱', '蓬莱市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370685, 24, 370600, '招远', '招远市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370686, 24, 370600, '栖霞', '栖霞市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370687, 24, 370600, '海阳', '海阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370700, 23, 370000, '潍坊', '潍坊市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370701, 24, 370700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370702, 24, 370700, '潍城区', '潍城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370703, 24, 370700, '寒亭区', '寒亭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370704, 24, 370700, '坊子区', '坊子区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370705, 24, 370700, '奎文区', '奎文区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370724, 24, 370700, '临朐县', '临朐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370725, 24, 370700, '昌乐县', '昌乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370781, 24, 370700, '青州', '青州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370782, 24, 370700, '诸城', '诸城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370783, 24, 370700, '寿光', '寿光市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370784, 24, 370700, '安丘', '安丘市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370785, 24, 370700, '高密', '高密市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370786, 24, 370700, '昌邑', '昌邑市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370800, 23, 370000, '济宁', '济宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370801, 24, 370800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370802, 24, 370800, '市中区', '市中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370811, 24, 370800, '任城区', '任城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370826, 24, 370800, '微山县', '微山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370827, 24, 370800, '鱼台县', '鱼台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370828, 24, 370800, '金乡县', '金乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370829, 24, 370800, '嘉祥县', '嘉祥县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370830, 24, 370800, '汶上县', '汶上县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370831, 24, 370800, '泗水县', '泗水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370832, 24, 370800, '梁山县', '梁山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370881, 24, 370800, '曲阜', '曲阜市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370882, 24, 370800, '兖州', '兖州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370883, 24, 370800, '邹城', '邹城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370900, 23, 370000, '泰安', '泰安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370901, 24, 370900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370902, 24, 370900, '泰山区', '泰山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370911, 24, 370900, '岱岳区', '岱岳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370921, 24, 370900, '宁阳县', '宁阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370923, 24, 370900, '东平县', '东平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370982, 24, 370900, '新泰', '新泰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (370983, 24, 370900, '肥城', '肥城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371000, 23, 370000, '威海', '威海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371001, 24, 371000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371002, 24, 371000, '环翠区', '环翠区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371081, 24, 371000, '文登', '文登市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371082, 24, 371000, '荣成', '荣成市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371083, 24, 371000, '乳山', '乳山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371100, 23, 370000, '日照', '日照市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371101, 24, 371100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371102, 24, 371100, '东港区', '东港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371103, 24, 371100, '岚山区', '岚山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371121, 24, 371100, '五莲县', '五莲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371122, 24, 371100, '莒县', '莒县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371200, 23, 370000, '莱芜', '莱芜市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371201, 24, 371200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371202, 24, 371200, '莱城区', '莱城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371203, 24, 371200, '钢城区', '钢城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371300, 23, 370000, '临沂', '临沂市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371301, 24, 371300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371302, 24, 371300, '兰山区', '兰山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371311, 24, 371300, '罗庄区', '罗庄区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371312, 24, 371300, '河东区', '河东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371321, 24, 371300, '沂南县', '沂南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371322, 24, 371300, '郯城县', '郯城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371323, 24, 371300, '沂水县', '沂水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371324, 24, 371300, '苍山县', '苍山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371325, 24, 371300, '费县', '费县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371326, 24, 371300, '平邑县', '平邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371327, 24, 371300, '莒南县', '莒南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371328, 24, 371300, '蒙阴县', '蒙阴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371329, 24, 371300, '临沭县', '临沭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371400, 23, 370000, '德州', '德州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371401, 24, 371400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371402, 24, 371400, '德城区', '德城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371421, 24, 371400, '陵县', '陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371422, 24, 371400, '宁津县', '宁津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371423, 24, 371400, '庆云县', '庆云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371424, 24, 371400, '临邑县', '临邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371425, 24, 371400, '齐河县', '齐河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371426, 24, 371400, '平原县', '平原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371427, 24, 371400, '夏津县', '夏津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371428, 24, 371400, '武城县', '武城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371481, 24, 371400, '乐陵', '乐陵市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371482, 24, 371400, '禹城', '禹城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371500, 23, 370000, '聊城', '聊城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371501, 24, 371500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371502, 24, 371500, '东昌府区', '东昌府区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371521, 24, 371500, '阳谷县', '阳谷县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371522, 24, 371500, '莘县', '莘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371523, 24, 371500, '茌平县', '茌平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371524, 24, 371500, '东阿县', '东阿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371525, 24, 371500, '冠县', '冠县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371526, 24, 371500, '高唐县', '高唐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371581, 24, 371500, '临清', '临清市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371600, 23, 370000, '滨州', '滨州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371601, 24, 371600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371602, 24, 371600, '滨城区', '滨城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371621, 24, 371600, '惠民县', '惠民县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371622, 24, 371600, '阳信县', '阳信县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371623, 24, 371600, '无棣县', '无棣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371624, 24, 371600, '沾化县', '沾化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371625, 24, 371600, '博兴县', '博兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371626, 24, 371600, '邹平县', '邹平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371700, 23, 370000, '菏泽', '菏泽市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371701, 24, 371700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371702, 24, 371700, '牡丹区', '牡丹区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371721, 24, 371700, '曹县', '曹县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371722, 24, 371700, '单县', '单县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371723, 24, 371700, '成武县', '成武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371724, 24, 371700, '巨野县', '巨野县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371725, 24, 371700, '郓城县', '郓城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371726, 24, 371700, '鄄城县', '鄄城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371727, 24, 371700, '定陶县', '定陶县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (371728, 24, 371700, '东明县', '东明县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410000, 22, NULL, '河南', '河南省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410100, 23, 410000, '郑州', '郑州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410101, 24, 410100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410102, 24, 410100, '中原区', '中原区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410103, 24, 410100, '二七区', '二七区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410104, 24, 410100, '管城回族区', '管城回族区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410105, 24, 410100, '金水区', '金水区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410106, 24, 410100, '上街区', '上街区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410108, 24, 410100, '惠济区', '惠济区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410122, 24, 410100, '中牟县', '中牟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410181, 24, 410100, '巩义', '巩义市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410182, 24, 410100, '荥阳', '荥阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410183, 24, 410100, '新密', '新密市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410184, 24, 410100, '新郑', '新郑市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410185, 24, 410100, '登封', '登封市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410200, 23, 410000, '开封', '开封市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410201, 24, 410200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410202, 24, 410200, '龙亭区', '龙亭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410203, 24, 410200, '顺河回族区', '顺河回族区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410204, 24, 410200, '鼓楼区', '鼓楼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410205, 24, 410200, '禹王台区', '禹王台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410211, 24, 410200, '金明区', '金明区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410221, 24, 410200, '杞县', '杞县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410222, 24, 410200, '通许县', '通许县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410223, 24, 410200, '尉氏县', '尉氏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410224, 24, 410200, '开封县', '开封县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410225, 24, 410200, '兰考县', '兰考县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410300, 23, 410000, '洛阳', '洛阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410301, 24, 410300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410302, 24, 410300, '老城区', '老城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410303, 24, 410300, '西工区', '西工区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410304, 24, 410300, '瀍河回族区', '瀍河回族区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410305, 24, 410300, '涧西区', '涧西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410306, 24, 410300, '吉利区', '吉利区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410311, 24, 410300, '洛龙区', '洛龙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410322, 24, 410300, '孟津县', '孟津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410323, 24, 410300, '新安县', '新安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410324, 24, 410300, '栾川县', '栾川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410325, 24, 410300, '嵩县', '嵩县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410326, 24, 410300, '汝阳县', '汝阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410327, 24, 410300, '宜阳县', '宜阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410328, 24, 410300, '洛宁县', '洛宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410329, 24, 410300, '伊川县', '伊川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410381, 24, 410300, '偃师', '偃师市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410400, 23, 410000, '平顶山', '平顶山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410401, 24, 410400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410402, 24, 410400, '新华区', '新华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410403, 24, 410400, '卫东区', '卫东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410404, 24, 410400, '石龙区', '石龙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410411, 24, 410400, '湛河区', '湛河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410421, 24, 410400, '宝丰县', '宝丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410422, 24, 410400, '叶县', '叶县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410423, 24, 410400, '鲁山县', '鲁山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410425, 24, 410400, '郏县', '郏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410481, 24, 410400, '舞钢', '舞钢市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410482, 24, 410400, '汝州', '汝州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410500, 23, 410000, '安阳', '安阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410501, 24, 410500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410502, 24, 410500, '文峰区', '文峰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410503, 24, 410500, '北关区', '北关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410505, 24, 410500, '殷都区', '殷都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410506, 24, 410500, '龙安区', '龙安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410522, 24, 410500, '安阳县', '安阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410523, 24, 410500, '汤阴县', '汤阴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410526, 24, 410500, '滑县', '滑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410527, 24, 410500, '内黄县', '内黄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410581, 24, 410500, '林州', '林州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410600, 23, 410000, '鹤壁', '鹤壁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410601, 24, 410600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410602, 24, 410600, '鹤山区', '鹤山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410603, 24, 410600, '山城区', '山城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410611, 24, 410600, '淇滨区', '淇滨区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410621, 24, 410600, '浚县', '浚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410622, 24, 410600, '淇县', '淇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410700, 23, 410000, '新乡', '新乡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410701, 24, 410700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410702, 24, 410700, '红旗区', '红旗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410703, 24, 410700, '卫滨区', '卫滨区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410704, 24, 410700, '凤泉区', '凤泉区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410711, 24, 410700, '牧野区', '牧野区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410721, 24, 410700, '新乡县', '新乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410724, 24, 410700, '获嘉县', '获嘉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410725, 24, 410700, '原阳县', '原阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410726, 24, 410700, '延津县', '延津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410727, 24, 410700, '封丘县', '封丘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410728, 24, 410700, '长垣县', '长垣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410781, 24, 410700, '卫辉', '卫辉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410782, 24, 410700, '辉县', '辉县市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410800, 23, 410000, '焦作', '焦作市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410801, 24, 410800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410802, 24, 410800, '解放区', '解放区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410803, 24, 410800, '中站区', '中站区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410804, 24, 410800, '马村区', '马村区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410811, 24, 410800, '山阳区', '山阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410821, 24, 410800, '修武县', '修武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410822, 24, 410800, '博爱县', '博爱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410823, 24, 410800, '武陟县', '武陟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410825, 24, 410800, '温县', '温县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410882, 24, 410800, '沁阳', '沁阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410883, 24, 410800, '孟州', '孟州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410900, 23, 410000, '濮阳', '濮阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410901, 24, 410900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410902, 24, 410900, '华龙区', '华龙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410922, 24, 410900, '清丰县', '清丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410923, 24, 410900, '南乐县', '南乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410926, 24, 410900, '范县', '范县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410927, 24, 410900, '台前县', '台前县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (410928, 24, 410900, '濮阳县', '濮阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411000, 23, 410000, '许昌', '许昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411001, 24, 411000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411002, 24, 411000, '魏都区', '魏都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411023, 24, 411000, '许昌县', '许昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411024, 24, 411000, '鄢陵县', '鄢陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411025, 24, 411000, '襄城县', '襄城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411081, 24, 411000, '禹州', '禹州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411082, 24, 411000, '长葛', '长葛市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411100, 23, 410000, '漯河', '漯河市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411101, 24, 411100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411102, 24, 411100, '源汇区', '源汇区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411103, 24, 411100, '郾城区', '郾城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411104, 24, 411100, '召陵区', '召陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411121, 24, 411100, '舞阳县', '舞阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411122, 24, 411100, '临颍县', '临颍县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411200, 23, 410000, '三门峡', '三门峡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411201, 24, 411200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411202, 24, 411200, '湖滨区', '湖滨区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411221, 24, 411200, '渑池县', '渑池县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411222, 24, 411200, '陕县', '陕县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411224, 24, 411200, '卢氏县', '卢氏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411281, 24, 411200, '义马', '义马市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411282, 24, 411200, '灵宝', '灵宝市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411300, 23, 410000, '南阳', '南阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411301, 24, 411300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411302, 24, 411300, '宛城区', '宛城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411303, 24, 411300, '卧龙区', '卧龙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411321, 24, 411300, '南召县', '南召县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411322, 24, 411300, '方城县', '方城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411323, 24, 411300, '西峡县', '西峡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411324, 24, 411300, '镇平县', '镇平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411325, 24, 411300, '内乡县', '内乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411326, 24, 411300, '淅川县', '淅川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411327, 24, 411300, '社旗县', '社旗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411328, 24, 411300, '唐河县', '唐河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411329, 24, 411300, '新野县', '新野县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411330, 24, 411300, '桐柏县', '桐柏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411381, 24, 411300, '邓州', '邓州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411400, 23, 410000, '商丘', '商丘市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411401, 24, 411400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411402, 24, 411400, '梁园区', '梁园区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411403, 24, 411400, '睢阳区', '睢阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411421, 24, 411400, '民权县', '民权县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411422, 24, 411400, '睢县', '睢县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411423, 24, 411400, '宁陵县', '宁陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411424, 24, 411400, '柘城县', '柘城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411425, 24, 411400, '虞城县', '虞城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411426, 24, 411400, '夏邑县', '夏邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411481, 24, 411400, '永城', '永城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411500, 23, 410000, '信阳', '信阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411501, 24, 411500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411502, 24, 411500, '浉河区', '浉河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411503, 24, 411500, '平桥区', '平桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411521, 24, 411500, '罗山县', '罗山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411522, 24, 411500, '光山县', '光山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411523, 24, 411500, '新县', '新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411524, 24, 411500, '商城县', '商城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411525, 24, 411500, '固始县', '固始县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411526, 24, 411500, '潢川县', '潢川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411527, 24, 411500, '淮滨县', '淮滨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411528, 24, 411500, '息县', '息县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411600, 23, 410000, '周口', '周口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411601, 24, 411600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411602, 24, 411600, '川汇区', '川汇区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411621, 24, 411600, '扶沟县', '扶沟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411622, 24, 411600, '西华县', '西华县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411623, 24, 411600, '商水县', '商水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411624, 24, 411600, '沈丘县', '沈丘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411625, 24, 411600, '郸城县', '郸城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411626, 24, 411600, '淮阳县', '淮阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411627, 24, 411600, '太康县', '太康县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411628, 24, 411600, '鹿邑县', '鹿邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411681, 24, 411600, '项城', '项城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411700, 23, 410000, '驻马店', '驻马店市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411701, 24, 411700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411702, 24, 411700, '驿城区', '驿城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411721, 24, 411700, '西平县', '西平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411722, 24, 411700, '上蔡县', '上蔡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411723, 24, 411700, '平舆县', '平舆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411724, 24, 411700, '正阳县', '正阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411725, 24, 411700, '确山县', '确山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411726, 24, 411700, '泌阳县', '泌阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411727, 24, 411700, '汝南县', '汝南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411728, 24, 411700, '遂平县', '遂平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (411729, 24, 411700, '新蔡县', '新蔡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (419001, 24, 419000, '济源', '济源市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420000, 22, NULL, '湖北', '湖北省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420100, 23, 420000, '武汉', '武汉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420101, 24, 420100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420102, 24, 420100, '江岸区', '江岸区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420103, 24, 420100, '江汉区', '江汉区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420104, 24, 420100, '硚口区', '硚口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420105, 24, 420100, '汉阳区', '汉阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420106, 24, 420100, '武昌区', '武昌区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420107, 24, 420100, '青山区', '青山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420111, 24, 420100, '洪山区', '洪山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420112, 24, 420100, '东西湖区', '东西湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420113, 24, 420100, '汉南区', '汉南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420114, 24, 420100, '蔡甸区', '蔡甸区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420115, 24, 420100, '江夏区', '江夏区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420116, 24, 420100, '黄陂区', '黄陂区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420117, 24, 420100, '新洲区', '新洲区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420200, 23, 420000, '黄石', '黄石市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420201, 24, 420200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420202, 24, 420200, '黄石港区', '黄石港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420203, 24, 420200, '西塞山区', '西塞山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420204, 24, 420200, '下陆区', '下陆区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420205, 24, 420200, '铁山区', '铁山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420222, 24, 420200, '阳新县', '阳新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420281, 24, 420200, '大冶', '大冶市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420300, 23, 420000, '十堰', '十堰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420301, 24, 420300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420302, 24, 420300, '茅箭区', '茅箭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420303, 24, 420300, '张湾区', '张湾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420321, 24, 420300, '郧县', '郧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420322, 24, 420300, '郧西县', '郧西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420323, 24, 420300, '竹山县', '竹山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420324, 24, 420300, '竹溪县', '竹溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420325, 24, 420300, '房县', '房县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420381, 24, 420300, '丹江口', '丹江口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420500, 23, 420000, '宜昌', '宜昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420501, 24, 420500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420502, 24, 420500, '西陵区', '西陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420503, 24, 420500, '伍家岗区', '伍家岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420504, 24, 420500, '点军区', '点军区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420505, 24, 420500, '猇亭区', '猇亭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420506, 24, 420500, '夷陵区', '夷陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420525, 24, 420500, '远安县', '远安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420526, 24, 420500, '兴山县', '兴山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420527, 24, 420500, '秭归县', '秭归县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420528, 24, 420500, '长阳土家族自治县', '长阳土家族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420529, 24, 420500, '五峰土家族自治县', '五峰土家族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420581, 24, 420500, '宜都', '宜都市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420582, 24, 420500, '当阳', '当阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420583, 24, 420500, '枝江', '枝江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420600, 23, 420000, '襄阳', '襄阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420601, 24, 420600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420602, 24, 420600, '襄城区', '襄城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420606, 24, 420600, '樊城区', '樊城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420607, 24, 420600, '襄州区', '襄州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420624, 24, 420600, '南漳县', '南漳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420625, 24, 420600, '谷城县', '谷城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420626, 24, 420600, '保康县', '保康县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420682, 24, 420600, '老河口', '老河口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420683, 24, 420600, '枣阳', '枣阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420684, 24, 420600, '宜城', '宜城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420700, 23, 420000, '鄂州', '鄂州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420701, 24, 420700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420702, 24, 420700, '梁子湖区', '梁子湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420703, 24, 420700, '华容区', '华容区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420704, 24, 420700, '鄂城区', '鄂城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420800, 23, 420000, '荆门', '荆门市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420801, 24, 420800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420802, 24, 420800, '东宝区', '东宝区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420804, 24, 420800, '掇刀区', '掇刀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420821, 24, 420800, '京山县', '京山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420822, 24, 420800, '沙洋县', '沙洋县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420881, 24, 420800, '钟祥', '钟祥市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420900, 23, 420000, '孝感', '孝感市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420901, 24, 420900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420902, 24, 420900, '孝南区', '孝南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420921, 24, 420900, '孝昌县', '孝昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420922, 24, 420900, '大悟县', '大悟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420923, 24, 420900, '云梦县', '云梦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420981, 24, 420900, '应城', '应城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420982, 24, 420900, '安陆', '安陆市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (420984, 24, 420900, '汉川', '汉川市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421000, 23, 420000, '荆州', '荆州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421001, 24, 421000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421002, 24, 421000, '沙市区', '沙市区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421003, 24, 421000, '荆州区', '荆州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421022, 24, 421000, '公安县', '公安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421023, 24, 421000, '监利县', '监利县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421024, 24, 421000, '江陵县', '江陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421081, 24, 421000, '石首', '石首市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421083, 24, 421000, '洪湖', '洪湖市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421087, 24, 421000, '松滋', '松滋市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421100, 23, 420000, '黄冈', '黄冈市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421101, 24, 421100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421102, 24, 421100, '黄州区', '黄州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421121, 24, 421100, '团风县', '团风县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421122, 24, 421100, '红安县', '红安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421123, 24, 421100, '罗田县', '罗田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421124, 24, 421100, '英山县', '英山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421125, 24, 421100, '浠水县', '浠水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421126, 24, 421100, '蕲春县', '蕲春县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421127, 24, 421100, '黄梅县', '黄梅县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421181, 24, 421100, '麻城', '麻城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421182, 24, 421100, '武穴', '武穴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421200, 23, 420000, '咸宁', '咸宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421201, 24, 421200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421202, 24, 421200, '咸安区', '咸安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421221, 24, 421200, '嘉鱼县', '嘉鱼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421222, 24, 421200, '通城县', '通城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421223, 24, 421200, '崇阳县', '崇阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421224, 24, 421200, '通山县', '通山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421281, 24, 421200, '赤壁', '赤壁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421300, 23, 420000, '随州', '随州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421301, 24, 421300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421303, 24, 421300, '曾都区', '曾都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421321, 24, 421300, '随县', '随县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (421381, 24, 421300, '广水', '广水市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422800, 23, 420000, '恩施土家族苗族自治州', '恩施土家族苗族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422801, 24, 422800, '恩施', '恩施市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422802, 24, 422800, '利川', '利川市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422822, 24, 422800, '建始县', '建始县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422823, 24, 422800, '巴东县', '巴东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422825, 24, 422800, '宣恩县', '宣恩县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422826, 24, 422800, '咸丰县', '咸丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422827, 24, 422800, '来凤县', '来凤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (422828, 24, 422800, '鹤峰县', '鹤峰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (429004, 24, 429000, '仙桃', '仙桃市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (429005, 24, 429000, '潜江', '潜江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (429006, 24, 429000, '天门', '天门市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (429021, 24, 429000, '神农架林区', '神农架林区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430000, 22, NULL, '湖南', '湖南省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430100, 23, 430000, '长沙', '长沙市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430101, 24, 430100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430102, 24, 430100, '芙蓉区', '芙蓉区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430103, 24, 430100, '天心区', '天心区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430104, 24, 430100, '岳麓区', '岳麓区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430105, 24, 430100, '开福区', '开福区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430111, 24, 430100, '雨花区', '雨花区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430112, 24, 430100, '望城区', '望城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430121, 24, 430100, '长沙县', '长沙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430124, 24, 430100, '宁乡县', '宁乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430181, 24, 430100, '浏阳', '浏阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430200, 23, 430000, '株洲', '株洲市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430201, 24, 430200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430202, 24, 430200, '荷塘区', '荷塘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430203, 24, 430200, '芦淞区', '芦淞区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430204, 24, 430200, '石峰区', '石峰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430211, 24, 430200, '天元区', '天元区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430221, 24, 430200, '株洲县', '株洲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430223, 24, 430200, '攸县', '攸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430224, 24, 430200, '茶陵县', '茶陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430225, 24, 430200, '炎陵县', '炎陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430281, 24, 430200, '醴陵', '醴陵市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430300, 23, 430000, '湘潭', '湘潭市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430301, 24, 430300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430302, 24, 430300, '雨湖区', '雨湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430304, 24, 430300, '岳塘区', '岳塘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430321, 24, 430300, '湘潭县', '湘潭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430381, 24, 430300, '湘乡', '湘乡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430382, 24, 430300, '韶山', '韶山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430400, 23, 430000, '衡阳', '衡阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430401, 24, 430400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430405, 24, 430400, '珠晖区', '珠晖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430406, 24, 430400, '雁峰区', '雁峰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430407, 24, 430400, '石鼓区', '石鼓区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430408, 24, 430400, '蒸湘区', '蒸湘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430412, 24, 430400, '南岳区', '南岳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430421, 24, 430400, '衡阳县', '衡阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430422, 24, 430400, '衡南县', '衡南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430423, 24, 430400, '衡山县', '衡山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430424, 24, 430400, '衡东县', '衡东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430426, 24, 430400, '祁东县', '祁东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430481, 24, 430400, '耒阳', '耒阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430482, 24, 430400, '常宁', '常宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430500, 23, 430000, '邵阳', '邵阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430501, 24, 430500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430502, 24, 430500, '双清区', '双清区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430503, 24, 430500, '大祥区', '大祥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430511, 24, 430500, '北塔区', '北塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430521, 24, 430500, '邵东县', '邵东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430522, 24, 430500, '新邵县', '新邵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430523, 24, 430500, '邵阳县', '邵阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430524, 24, 430500, '隆回县', '隆回县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430525, 24, 430500, '洞口县', '洞口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430527, 24, 430500, '绥宁县', '绥宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430528, 24, 430500, '新宁县', '新宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430529, 24, 430500, '城步苗族自治县', '城步苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430581, 24, 430500, '武冈', '武冈市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430600, 23, 430000, '岳阳', '岳阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430601, 24, 430600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430602, 24, 430600, '岳阳楼区', '岳阳楼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430603, 24, 430600, '云溪区', '云溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430611, 24, 430600, '君山区', '君山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430621, 24, 430600, '岳阳县', '岳阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430623, 24, 430600, '华容县', '华容县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430624, 24, 430600, '湘阴县', '湘阴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430626, 24, 430600, '平江县', '平江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430681, 24, 430600, '汨罗', '汨罗市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430682, 24, 430600, '临湘', '临湘市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430700, 23, 430000, '常德', '常德市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430701, 24, 430700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430702, 24, 430700, '武陵区', '武陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430703, 24, 430700, '鼎城区', '鼎城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430721, 24, 430700, '安乡县', '安乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430722, 24, 430700, '汉寿县', '汉寿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430723, 24, 430700, '澧县', '澧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430724, 24, 430700, '临澧县', '临澧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430725, 24, 430700, '桃源县', '桃源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430726, 24, 430700, '石门县', '石门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430781, 24, 430700, '津市', '津市市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430800, 23, 430000, '张家界', '张家界市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430801, 24, 430800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430802, 24, 430800, '永定区', '永定区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430811, 24, 430800, '武陵源区', '武陵源区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430821, 24, 430800, '慈利县', '慈利县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430822, 24, 430800, '桑植县', '桑植县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430900, 23, 430000, '益阳', '益阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430901, 24, 430900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430902, 24, 430900, '资阳区', '资阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430903, 24, 430900, '赫山区', '赫山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430921, 24, 430900, '南县', '南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430922, 24, 430900, '桃江县', '桃江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430923, 24, 430900, '安化县', '安化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (430981, 24, 430900, '沅江', '沅江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431000, 23, 430000, '郴州', '郴州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431001, 24, 431000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431002, 24, 431000, '北湖区', '北湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431003, 24, 431000, '苏仙区', '苏仙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431021, 24, 431000, '桂阳县', '桂阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431022, 24, 431000, '宜章县', '宜章县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431023, 24, 431000, '永兴县', '永兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431024, 24, 431000, '嘉禾县', '嘉禾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431025, 24, 431000, '临武县', '临武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431026, 24, 431000, '汝城县', '汝城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431027, 24, 431000, '桂东县', '桂东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431028, 24, 431000, '安仁县', '安仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431081, 24, 431000, '资兴', '资兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431100, 23, 430000, '永州', '永州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431101, 24, 431100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431102, 24, 431100, '零陵区', '零陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431103, 24, 431100, '冷水滩区', '冷水滩区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431121, 24, 431100, '祁阳县', '祁阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431122, 24, 431100, '东安县', '东安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431123, 24, 431100, '双牌县', '双牌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431124, 24, 431100, '道县', '道县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431125, 24, 431100, '江永县', '江永县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431126, 24, 431100, '宁远县', '宁远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431127, 24, 431100, '蓝山县', '蓝山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431128, 24, 431100, '新田县', '新田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431129, 24, 431100, '江华瑶族自治县', '江华瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431200, 23, 430000, '怀化', '怀化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431201, 24, 431200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431202, 24, 431200, '鹤城区', '鹤城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431221, 24, 431200, '中方县', '中方县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431222, 24, 431200, '沅陵县', '沅陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431223, 24, 431200, '辰溪县', '辰溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431224, 24, 431200, '溆浦县', '溆浦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431225, 24, 431200, '会同县', '会同县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431226, 24, 431200, '麻阳苗族自治县', '麻阳苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431227, 24, 431200, '新晃侗族自治县', '新晃侗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431228, 24, 431200, '芷江侗族自治县', '芷江侗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431229, 24, 431200, '靖州苗族侗族自治县', '靖州苗族侗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431230, 24, 431200, '通道侗族自治县', '通道侗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431281, 24, 431200, '洪江', '洪江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431300, 23, 430000, '娄底', '娄底市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431301, 24, 431300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431302, 24, 431300, '娄星区', '娄星区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431321, 24, 431300, '双峰县', '双峰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431322, 24, 431300, '新化县', '新化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431381, 24, 431300, '冷水江', '冷水江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (431382, 24, 431300, '涟源', '涟源市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433100, 23, 430000, '湘西土家族苗族自治州', '湘西土家族苗族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433101, 24, 433100, '吉首', '吉首市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433122, 24, 433100, '泸溪县', '泸溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433123, 24, 433100, '凤凰县', '凤凰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433124, 24, 433100, '花垣县', '花垣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433125, 24, 433100, '保靖县', '保靖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433126, 24, 433100, '古丈县', '古丈县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433127, 24, 433100, '永顺县', '永顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (433130, 24, 433100, '龙山县', '龙山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440000, 22, NULL, '广东', '广东省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440100, 23, 440000, '广州', '广州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440101, 24, 440100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440103, 24, 440100, '荔湾区', '荔湾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440104, 24, 440100, '越秀区', '越秀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440105, 24, 440100, '海珠区', '海珠区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440106, 24, 440100, '天河区', '天河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440111, 24, 440100, '白云区', '白云区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440112, 24, 440100, '黄埔区', '黄埔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440113, 24, 440100, '番禺区', '番禺区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440114, 24, 440100, '花都区', '花都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440115, 24, 440100, '南沙区', '南沙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440116, 24, 440100, '萝岗区', '萝岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440183, 24, 440100, '增城', '增城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440184, 24, 440100, '从化', '从化市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440200, 23, 440000, '韶关', '韶关市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440201, 24, 440200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440203, 24, 440200, '武江区', '武江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440204, 24, 440200, '浈江区', '浈江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440205, 24, 440200, '曲江区', '曲江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440222, 24, 440200, '始兴县', '始兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440224, 24, 440200, '仁化县', '仁化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440229, 24, 440200, '翁源县', '翁源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440232, 24, 440200, '乳源瑶族自治县', '乳源瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440233, 24, 440200, '新丰县', '新丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440281, 24, 440200, '乐昌', '乐昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440282, 24, 440200, '南雄', '南雄市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440300, 23, 440000, '深圳', '深圳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440301, 24, 440300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440303, 24, 440300, '罗湖区', '罗湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440304, 24, 440300, '福田区', '福田区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440305, 24, 440300, '南山区', '南山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440306, 24, 440300, '宝安区', '宝安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440307, 24, 440300, '龙岗区', '龙岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440308, 24, 440300, '盐田区', '盐田区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440400, 23, 440000, '珠海', '珠海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440401, 24, 440400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440402, 24, 440400, '香洲区', '香洲区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440403, 24, 440400, '斗门区', '斗门区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440404, 24, 440400, '金湾区', '金湾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440500, 23, 440000, '汕头', '汕头市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440501, 24, 440500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440507, 24, 440500, '龙湖区', '龙湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440511, 24, 440500, '金平区', '金平区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440512, 24, 440500, '濠江区', '濠江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440513, 24, 440500, '潮阳区', '潮阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440514, 24, 440500, '潮南区', '潮南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440515, 24, 440500, '澄海区', '澄海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440523, 24, 440500, '南澳县', '南澳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440600, 23, 440000, '佛山', '佛山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440601, 24, 440600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440604, 24, 440600, '禅城区', '禅城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440605, 24, 440600, '南海区', '南海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440606, 24, 440600, '顺德区', '顺德区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440607, 24, 440600, '三水区', '三水区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440608, 24, 440600, '高明区', '高明区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440700, 23, 440000, '江门', '江门市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440701, 24, 440700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440703, 24, 440700, '蓬江区', '蓬江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440704, 24, 440700, '江海区', '江海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440705, 24, 440700, '新会区', '新会区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440781, 24, 440700, '台山', '台山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440783, 24, 440700, '开平', '开平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440784, 24, 440700, '鹤山', '鹤山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440785, 24, 440700, '恩平', '恩平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440800, 23, 440000, '湛江', '湛江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440801, 24, 440800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440802, 24, 440800, '赤坎区', '赤坎区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440803, 24, 440800, '霞山区', '霞山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440804, 24, 440800, '坡头区', '坡头区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440811, 24, 440800, '麻章区', '麻章区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440823, 24, 440800, '遂溪县', '遂溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440825, 24, 440800, '徐闻县', '徐闻县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440881, 24, 440800, '廉江', '廉江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440882, 24, 440800, '雷州', '雷州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440883, 24, 440800, '吴川', '吴川市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440900, 23, 440000, '茂名', '茂名市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440901, 24, 440900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440902, 24, 440900, '茂南区', '茂南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440903, 24, 440900, '茂港区', '茂港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440923, 24, 440900, '电白县', '电白县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440981, 24, 440900, '高州', '高州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440982, 24, 440900, '化州', '化州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (440983, 24, 440900, '信宜', '信宜市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441200, 23, 440000, '肇庆', '肇庆市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441201, 24, 441200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441202, 24, 441200, '端州区', '端州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441203, 24, 441200, '鼎湖区', '鼎湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441223, 24, 441200, '广宁县', '广宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441224, 24, 441200, '怀集县', '怀集县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441225, 24, 441200, '封开县', '封开县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441226, 24, 441200, '德庆县', '德庆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441283, 24, 441200, '高要', '高要市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441284, 24, 441200, '四会', '四会市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441300, 23, 440000, '惠州', '惠州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441301, 24, 441300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441302, 24, 441300, '惠城区', '惠城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441303, 24, 441300, '惠阳区', '惠阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441322, 24, 441300, '博罗县', '博罗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441323, 24, 441300, '惠东县', '惠东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441324, 24, 441300, '龙门县', '龙门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441400, 23, 440000, '梅州', '梅州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441401, 24, 441400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441402, 24, 441400, '梅江区', '梅江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441421, 24, 441400, '梅县', '梅县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441422, 24, 441400, '大埔县', '大埔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441423, 24, 441400, '丰顺县', '丰顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441424, 24, 441400, '五华县', '五华县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441426, 24, 441400, '平远县', '平远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441427, 24, 441400, '蕉岭县', '蕉岭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441481, 24, 441400, '兴宁', '兴宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441500, 23, 440000, '汕尾', '汕尾市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441501, 24, 441500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441502, 24, 441500, '城区', '城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441521, 24, 441500, '海丰县', '海丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441523, 24, 441500, '陆河县', '陆河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441581, 24, 441500, '陆丰', '陆丰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441600, 23, 440000, '河源', '河源市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441601, 24, 441600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441602, 24, 441600, '源城区', '源城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441621, 24, 441600, '紫金县', '紫金县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441622, 24, 441600, '龙川县', '龙川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441623, 24, 441600, '连平县', '连平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441624, 24, 441600, '和平县', '和平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441625, 24, 441600, '东源县', '东源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441700, 23, 440000, '阳江', '阳江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441701, 24, 441700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441702, 24, 441700, '江城区', '江城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441721, 24, 441700, '阳西县', '阳西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441723, 24, 441700, '阳东县', '阳东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441781, 24, 441700, '阳春', '阳春市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441800, 23, 440000, '清远', '清远市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441801, 24, 441800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441802, 24, 441800, '清城区', '清城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441821, 24, 441800, '佛冈县', '佛冈县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441823, 24, 441800, '阳山县', '阳山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441825, 24, 441800, '连山壮族瑶族自治县', '连山壮族瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441826, 24, 441800, '连南瑶族自治县', '连南瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441827, 24, 441800, '清新县', '清新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441881, 24, 441800, '英德', '英德市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441882, 24, 441800, '连州', '连州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (441900, 23, 440000, '东莞', '东莞市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (442000, 23, 440000, '中山', '中山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445100, 23, 440000, '潮州', '潮州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445101, 24, 445100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445102, 24, 445100, '湘桥区', '湘桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445121, 24, 445100, '潮安县', '潮安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445122, 24, 445100, '饶平县', '饶平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445200, 23, 440000, '揭阳', '揭阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445201, 24, 445200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445202, 24, 445200, '榕城区', '榕城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445221, 24, 445200, '揭东县', '揭东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445222, 24, 445200, '揭西县', '揭西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445224, 24, 445200, '惠来县', '惠来县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445281, 24, 445200, '普宁', '普宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445300, 23, 440000, '云浮', '云浮市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445301, 24, 445300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445302, 24, 445300, '云城区', '云城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445321, 24, 445300, '新兴县', '新兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445322, 24, 445300, '郁南县', '郁南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445323, 24, 445300, '云安县', '云安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (445381, 24, 445300, '罗定', '罗定市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450000, 22, NULL, '广西', '广西壮族自治区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450100, 23, 450000, '南宁', '南宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450101, 24, 450100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450102, 24, 450100, '兴宁区', '兴宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450103, 24, 450100, '青秀区', '青秀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450105, 24, 450100, '江南区', '江南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450107, 24, 450100, '西乡塘区', '西乡塘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450108, 24, 450100, '良庆区', '良庆区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450109, 24, 450100, '邕宁区', '邕宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450122, 24, 450100, '武鸣县', '武鸣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450123, 24, 450100, '隆安县', '隆安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450124, 24, 450100, '马山县', '马山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450125, 24, 450100, '上林县', '上林县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450126, 24, 450100, '宾阳县', '宾阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450127, 24, 450100, '横县', '横县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450200, 23, 450000, '柳州', '柳州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450201, 24, 450200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450202, 24, 450200, '城中区', '城中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450203, 24, 450200, '鱼峰区', '鱼峰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450204, 24, 450200, '柳南区', '柳南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450205, 24, 450200, '柳北区', '柳北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450221, 24, 450200, '柳江县', '柳江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450222, 24, 450200, '柳城县', '柳城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450223, 24, 450200, '鹿寨县', '鹿寨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450224, 24, 450200, '融安县', '融安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450225, 24, 450200, '融水苗族自治县', '融水苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450226, 24, 450200, '三江侗族自治县', '三江侗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450300, 23, 450000, '桂林', '桂林市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450301, 24, 450300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450302, 24, 450300, '秀峰区', '秀峰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450303, 24, 450300, '叠彩区', '叠彩区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450304, 24, 450300, '象山区', '象山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450305, 24, 450300, '七星区', '七星区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450311, 24, 450300, '雁山区', '雁山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450321, 24, 450300, '阳朔县', '阳朔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450322, 24, 450300, '临桂县', '临桂县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450323, 24, 450300, '灵川县', '灵川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450324, 24, 450300, '全州县', '全州县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450325, 24, 450300, '兴安县', '兴安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450326, 24, 450300, '永福县', '永福县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450327, 24, 450300, '灌阳县', '灌阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450328, 24, 450300, '龙胜各族自治县', '龙胜各族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450329, 24, 450300, '资源县', '资源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450330, 24, 450300, '平乐县', '平乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450331, 24, 450300, '荔蒲县', '荔蒲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450332, 24, 450300, '恭城瑶族自治县', '恭城瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450400, 23, 450000, '梧州', '梧州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450401, 24, 450400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450403, 24, 450400, '万秀区', '万秀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450404, 24, 450400, '蝶山区', '蝶山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450405, 24, 450400, '长洲区', '长洲区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450421, 24, 450400, '苍梧县', '苍梧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450422, 24, 450400, '藤县', '藤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450423, 24, 450400, '蒙山县', '蒙山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450481, 24, 450400, '岑溪', '岑溪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450500, 23, 450000, '北海', '北海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450501, 24, 450500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450502, 24, 450500, '海城区', '海城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450503, 24, 450500, '银海区', '银海区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450512, 24, 450500, '铁山港区', '铁山港区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450521, 24, 450500, '合浦县', '合浦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450600, 23, 450000, '防城港', '防城港市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450601, 24, 450600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450602, 24, 450600, '港口区', '港口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450603, 24, 450600, '防城区', '防城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450621, 24, 450600, '上思县', '上思县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450681, 24, 450600, '东兴', '东兴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450700, 23, 450000, '钦州', '钦州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450701, 24, 450700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450702, 24, 450700, '钦南区', '钦南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450703, 24, 450700, '钦北区', '钦北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450721, 24, 450700, '灵山县', '灵山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450722, 24, 450700, '浦北县', '浦北县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450800, 23, 450000, '贵港', '贵港市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450801, 24, 450800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450802, 24, 450800, '港北区', '港北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450803, 24, 450800, '港南区', '港南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450804, 24, 450800, '覃塘区', '覃塘区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450821, 24, 450800, '平南县', '平南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450881, 24, 450800, '桂平', '桂平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450900, 23, 450000, '玉林', '玉林市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450901, 24, 450900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450902, 24, 450900, '玉州区', '玉州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450921, 24, 450900, '容县', '容县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450922, 24, 450900, '陆川县', '陆川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450923, 24, 450900, '博白县', '博白县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450924, 24, 450900, '兴业县', '兴业县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (450981, 24, 450900, '北流', '北流市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451000, 23, 450000, '百色', '百色市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451001, 24, 451000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451002, 24, 451000, '右江区', '右江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451021, 24, 451000, '田阳县', '田阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451022, 24, 451000, '田东县', '田东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451023, 24, 451000, '平果县', '平果县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451024, 24, 451000, '德保县', '德保县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451025, 24, 451000, '靖西县', '靖西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451026, 24, 451000, '那坡县', '那坡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451027, 24, 451000, '凌云县', '凌云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451028, 24, 451000, '乐业县', '乐业县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451029, 24, 451000, '田林县', '田林县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451030, 24, 451000, '西林县', '西林县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451031, 24, 451000, '隆林各族自治县', '隆林各族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451100, 23, 450000, '贺州', '贺州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451101, 24, 451100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451102, 24, 451100, '八步区', '八步区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451121, 24, 451100, '昭平县', '昭平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451122, 24, 451100, '钟山县', '钟山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451123, 24, 451100, '富川瑶族自治县', '富川瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451200, 23, 450000, '河池', '河池市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451201, 24, 451200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451202, 24, 451200, '金城江区', '金城江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451221, 24, 451200, '南丹县', '南丹县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451222, 24, 451200, '天峨县', '天峨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451223, 24, 451200, '凤山县', '凤山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451224, 24, 451200, '东兰县', '东兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451225, 24, 451200, '罗城仫佬族自治县', '罗城仫佬族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451226, 24, 451200, '环江毛南族自治县', '环江毛南族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451227, 24, 451200, '巴马瑶族自治县', '巴马瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451228, 24, 451200, '都安瑶族自治县', '都安瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451229, 24, 451200, '大化瑶族自治县', '大化瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451281, 24, 451200, '宜州', '宜州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451300, 23, 450000, '来宾', '来宾市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451301, 24, 451300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451302, 24, 451300, '兴宾区', '兴宾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451321, 24, 451300, '忻城县', '忻城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451322, 24, 451300, '象州县', '象州县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451323, 24, 451300, '武宣县', '武宣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451324, 24, 451300, '金秀瑶族自治县', '金秀瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451381, 24, 451300, '合山', '合山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451400, 23, 450000, '崇左', '崇左市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451401, 24, 451400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451402, 24, 451400, '江洲区', '江洲区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451421, 24, 451400, '扶绥县', '扶绥县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451422, 24, 451400, '宁明县', '宁明县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451423, 24, 451400, '龙州县', '龙州县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451424, 24, 451400, '大新县', '大新县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451425, 24, 451400, '天等县', '天等县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (451481, 24, 451400, '凭祥', '凭祥市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460000, 22, NULL, '海南', '海南省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460100, 23, 460000, '海口', '海口市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460101, 24, 460100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460105, 24, 460100, '秀英区', '秀英区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460106, 24, 460100, '龙华区', '龙华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460107, 24, 460100, '琼山区', '琼山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460108, 24, 460100, '美兰区', '美兰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460200, 23, 460000, '三亚', '三亚市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (460201, 24, 460200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469001, 24, 469000, '五指山', '五指山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469002, 24, 469000, '琼海', '琼海市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469003, 24, 469000, '儋州', '儋州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469005, 24, 469000, '文昌', '文昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469006, 24, 469000, '万宁', '万宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469007, 24, 469000, '东方', '东方市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469021, 24, 469000, '定安县', '定安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469022, 24, 469000, '屯昌县', '屯昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469023, 24, 469000, '澄迈县', '澄迈县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469024, 24, 469000, '临高县', '临高县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469025, 24, 469000, '白沙黎族自治县', '白沙黎族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469026, 24, 469000, '昌江黎族自治县', '昌江黎族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469027, 24, 469000, '乐东黎族自治县', '乐东黎族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469028, 24, 469000, '陵水黎族自治县', '陵水黎族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469029, 24, 469000, '保亭黎族苗族自治县', '保亭黎族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469030, 24, 469000, '琼中黎族苗族自治县', '琼中黎族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469031, 24, 469000, '西沙群岛', '西沙群岛');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469032, 24, 469000, '南沙群岛', '南沙群岛');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469033, 24, 469000, '中沙群岛的岛礁及其海域', '中沙群岛的岛礁及其海域');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500000, 22, NULL, '重庆', '重庆');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500100, 23, 500000, '重庆', '重庆市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500101, 24, 500100, '万州区', '万州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500102, 24, 500100, '涪陵区', '涪陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500103, 24, 500100, '渝中区', '渝中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500104, 24, 500100, '大渡口区', '大渡口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500105, 24, 500100, '江北区', '江北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500106, 24, 500100, '沙坪坝区', '沙坪坝区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500107, 24, 500100, '九龙坡区', '九龙坡区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500108, 24, 500100, '南岸区', '南岸区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500109, 24, 500100, '北碚区', '北碚区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500110, 24, 500100, '綦江区', '綦江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500111, 24, 500100, '大足区', '大足区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500112, 24, 500100, '渝北区', '渝北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500113, 24, 500100, '巴南区', '巴南区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500114, 24, 500100, '黔江区', '黔江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500115, 24, 500100, '长寿区', '长寿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500116, 24, 500100, '江津区', '江津区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500117, 24, 500100, '合川区', '合川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500118, 24, 500100, '永川区', '永川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500119, 24, 500100, '南川区', '南川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500223, 24, 500200, '潼南县', '潼南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500224, 24, 500200, '铜梁县', '铜梁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500226, 24, 500200, '荣昌县', '荣昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500227, 24, 500200, '璧山县', '璧山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500228, 24, 500200, '梁平县', '梁平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500229, 24, 500200, '城口县', '城口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500230, 24, 500200, '丰都县', '丰都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500231, 24, 500200, '垫江县', '垫江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500232, 24, 500200, '武隆县', '武隆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500233, 24, 500200, '忠县', '忠县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500234, 24, 500200, '开县', '开县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500235, 24, 500200, '云阳县', '云阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500236, 24, 500200, '奉节县', '奉节县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500237, 24, 500200, '巫山县', '巫山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500238, 24, 500200, '巫溪县', '巫溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500240, 24, 500200, '石柱土家族自治县', '石柱土家族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500241, 24, 500200, '秀山土家族苗族自治县', '秀山土家族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500242, 24, 500200, '酉阳土家族苗族自治县', '酉阳土家族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500243, 24, 500200, '彭水苗族土家族自治县', '彭水苗族土家族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510000, 22, NULL, '四川', '四川省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510100, 23, 510000, '成都', '成都市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510101, 24, 510100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510104, 24, 510100, '锦江区', '锦江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510105, 24, 510100, '青羊区', '青羊区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510106, 24, 510100, '金牛区', '金牛区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510107, 24, 510100, '武侯区', '武侯区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510108, 24, 510100, '成华区', '成华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510112, 24, 510100, '龙泉驿区', '龙泉驿区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510113, 24, 510100, '青白江区', '青白江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510114, 24, 510100, '新都区', '新都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510115, 24, 510100, '温江区', '温江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510121, 24, 510100, '金堂县', '金堂县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510122, 24, 510100, '双流县', '双流县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510124, 24, 510100, '郫县', '郫县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510129, 24, 510100, '大邑县', '大邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510131, 24, 510100, '蒲江县', '蒲江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510132, 24, 510100, '新津县', '新津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510181, 24, 510100, '都江堰', '都江堰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510182, 24, 510100, '彭州', '彭州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510183, 24, 510100, '邛崃', '邛崃市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510184, 24, 510100, '崇州', '崇州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510300, 23, 510000, '自贡', '自贡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510301, 24, 510300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510302, 24, 510300, '自流井区', '自流井区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510303, 24, 510300, '贡井区', '贡井区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510304, 24, 510300, '大安区', '大安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510311, 24, 510300, '沿滩区', '沿滩区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510321, 24, 510300, '荣县', '荣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510322, 24, 510300, '富顺县', '富顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510400, 23, 510000, '攀枝花', '攀枝花市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510401, 24, 510400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510402, 24, 510400, '东区', '东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510403, 24, 510400, '西区', '西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510411, 24, 510400, '仁和区', '仁和区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510421, 24, 510400, '米易县', '米易县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510422, 24, 510400, '盐边县', '盐边县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510500, 23, 510000, '泸州', '泸州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510501, 24, 510500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510502, 24, 510500, '江阳区', '江阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510503, 24, 510500, '纳溪区', '纳溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510504, 24, 510500, '龙马潭区', '龙马潭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510521, 24, 510500, '泸县', '泸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510522, 24, 510500, '合江县', '合江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510524, 24, 510500, '叙永县', '叙永县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510525, 24, 510500, '古蔺县', '古蔺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510600, 23, 510000, '德阳', '德阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510601, 24, 510600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510603, 24, 510600, '旌阳区', '旌阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510623, 24, 510600, '中江县', '中江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510626, 24, 510600, '罗江县', '罗江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510681, 24, 510600, '广汉', '广汉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510682, 24, 510600, '什邡', '什邡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510683, 24, 510600, '绵竹', '绵竹市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510700, 23, 510000, '绵阳', '绵阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510701, 24, 510700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510703, 24, 510700, '涪城区', '涪城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510704, 24, 510700, '游仙区', '游仙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510722, 24, 510700, '三台县', '三台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510723, 24, 510700, '盐亭县', '盐亭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510724, 24, 510700, '安县', '安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510725, 24, 510700, '梓潼县', '梓潼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510726, 24, 510700, '北川羌族自治县', '北川羌族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510727, 24, 510700, '平武县', '平武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510781, 24, 510700, '江油', '江油市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510800, 23, 510000, '广元', '广元市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510801, 24, 510800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510802, 24, 510800, '利州区', '利州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510811, 24, 510800, '元坝区', '元坝区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510812, 24, 510800, '朝天区', '朝天区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510821, 24, 510800, '旺苍县', '旺苍县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510822, 24, 510800, '青川县', '青川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510823, 24, 510800, '剑阁县', '剑阁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510824, 24, 510800, '苍溪县', '苍溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510900, 23, 510000, '遂宁', '遂宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510901, 24, 510900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510903, 24, 510900, '船山区', '船山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510904, 24, 510900, '安居区', '安居区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510921, 24, 510900, '蓬溪县', '蓬溪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510922, 24, 510900, '射洪县', '射洪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (510923, 24, 510900, '大英县', '大英县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511000, 23, 510000, '内江', '内江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511001, 24, 511000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511002, 24, 511000, '市中区', '市中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511011, 24, 511000, '东兴区', '东兴区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511024, 24, 511000, '威远县', '威远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511025, 24, 511000, '资中县', '资中县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511028, 24, 511000, '隆昌县', '隆昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511100, 23, 510000, '乐山', '乐山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511101, 24, 511100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511102, 24, 511100, '市中区', '市中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511111, 24, 511100, '沙湾区', '沙湾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511112, 24, 511100, '五通桥区', '五通桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511113, 24, 511100, '金口河区', '金口河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511123, 24, 511100, '犍为县', '犍为县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511124, 24, 511100, '井研县', '井研县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511126, 24, 511100, '夹江县', '夹江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511129, 24, 511100, '沐川县', '沐川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511132, 24, 511100, '峨边彝族自治县', '峨边彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511133, 24, 511100, '马边彝族自治县', '马边彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511181, 24, 511100, '峨眉山', '峨眉山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511300, 23, 510000, '南充', '南充市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511301, 24, 511300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511302, 24, 511300, '顺庆区', '顺庆区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511303, 24, 511300, '高坪区', '高坪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511304, 24, 511300, '嘉陵区', '嘉陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511321, 24, 511300, '南部县', '南部县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511322, 24, 511300, '营山县', '营山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511323, 24, 511300, '蓬安县', '蓬安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511324, 24, 511300, '仪陇县', '仪陇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511325, 24, 511300, '西充县', '西充县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511381, 24, 511300, '阆中', '阆中市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511400, 23, 510000, '眉山', '眉山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511401, 24, 511400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511402, 24, 511400, '东坡区', '东坡区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511421, 24, 511400, '仁寿县', '仁寿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511422, 24, 511400, '彭山县', '彭山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511423, 24, 511400, '洪雅县', '洪雅县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511424, 24, 511400, '丹棱县', '丹棱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511425, 24, 511400, '青神县', '青神县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511500, 23, 510000, '宜宾', '宜宾市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511501, 24, 511500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511502, 24, 511500, '翠屏区', '翠屏区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511503, 24, 511500, '南溪区', '南溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511521, 24, 511500, '宜宾县', '宜宾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511523, 24, 511500, '江安县', '江安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511524, 24, 511500, '长宁县', '长宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511525, 24, 511500, '高县', '高县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511526, 24, 511500, '珙县', '珙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511527, 24, 511500, '筠连县', '筠连县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511528, 24, 511500, '兴文县', '兴文县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511529, 24, 511500, '屏山县', '屏山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511600, 23, 510000, '广安', '广安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511601, 24, 511600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511602, 24, 511600, '广安区', '广安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511621, 24, 511600, '岳池县', '岳池县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511622, 24, 511600, '武胜县', '武胜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511623, 24, 511600, '邻水县', '邻水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511681, 24, 511600, '华蓥', '华蓥市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511700, 23, 510000, '达州', '达州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511701, 24, 511700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511702, 24, 511700, '通川区', '通川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511721, 24, 511700, '达县', '达县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511722, 24, 511700, '宣汉县', '宣汉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511723, 24, 511700, '开江县', '开江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511724, 24, 511700, '大竹县', '大竹县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511725, 24, 511700, '渠县', '渠县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511781, 24, 511700, '万源', '万源市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511800, 23, 510000, '雅安', '雅安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511801, 24, 511800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511802, 24, 511800, '雨城区', '雨城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511821, 24, 511800, '名山县', '名山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511822, 24, 511800, '荥经县', '荥经县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511823, 24, 511800, '汉源县', '汉源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511824, 24, 511800, '石棉县', '石棉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511825, 24, 511800, '天全县', '天全县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511826, 24, 511800, '芦山县', '芦山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511827, 24, 511800, '宝兴县', '宝兴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511900, 23, 510000, '巴中', '巴中市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511901, 24, 511900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511902, 24, 511900, '巴州区', '巴州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511921, 24, 511900, '通江县', '通江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511922, 24, 511900, '南江县', '南江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (511923, 24, 511900, '平昌县', '平昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (512000, 23, 510000, '资阳', '资阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (512001, 24, 512000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (512002, 24, 512000, '雁江区', '雁江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (512021, 24, 512000, '安岳县', '安岳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (512022, 24, 512000, '乐至县', '乐至县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (512081, 24, 512000, '简阳', '简阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513200, 23, 510000, '阿坝藏族羌族自治州', '阿坝藏族羌族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513221, 24, 513200, '汶川县', '汶川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513222, 24, 513200, '理县', '理县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513223, 24, 513200, '茂县', '茂县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513224, 24, 513200, '松潘县', '松潘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513225, 24, 513200, '九寨沟县', '九寨沟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513226, 24, 513200, '金川县', '金川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513227, 24, 513200, '小金县', '小金县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513228, 24, 513200, '黑水县', '黑水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513229, 24, 513200, '马尔康县', '马尔康县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513230, 24, 513200, '壤塘县', '壤塘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513231, 24, 513200, '阿坝县', '阿坝县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513232, 24, 513200, '若尔盖县', '若尔盖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513233, 24, 513200, '红原县', '红原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513300, 23, 510000, '甘孜藏族自治州', '甘孜藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513321, 24, 513300, '康定县', '康定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513322, 24, 513300, '泸定县', '泸定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513323, 24, 513300, '丹巴县', '丹巴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513324, 24, 513300, '九龙县', '九龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513325, 24, 513300, '雅江县', '雅江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513326, 24, 513300, '道孚县', '道孚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513327, 24, 513300, '炉霍县', '炉霍县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513328, 24, 513300, '甘孜县', '甘孜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513329, 24, 513300, '新龙县', '新龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513330, 24, 513300, '德格县', '德格县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513331, 24, 513300, '白玉县', '白玉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513332, 24, 513300, '石渠县', '石渠县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513333, 24, 513300, '色达县', '色达县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513334, 24, 513300, '理塘县', '理塘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513335, 24, 513300, '巴塘县', '巴塘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513336, 24, 513300, '乡城县', '乡城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513337, 24, 513300, '稻城县', '稻城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513338, 24, 513300, '得荣县', '得荣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513400, 23, 510000, '凉山彝族自治州', '凉山彝族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513401, 24, 513400, '西昌', '西昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513422, 24, 513400, '木里藏族自治县', '木里藏族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513423, 24, 513400, '盐源县', '盐源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513424, 24, 513400, '德昌县', '德昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513425, 24, 513400, '会理县', '会理县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513426, 24, 513400, '会东县', '会东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513427, 24, 513400, '宁南县', '宁南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513428, 24, 513400, '普格县', '普格县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513429, 24, 513400, '布拖县', '布拖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513430, 24, 513400, '金阳县', '金阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513431, 24, 513400, '昭觉县', '昭觉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513432, 24, 513400, '喜德县', '喜德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513433, 24, 513400, '冕宁县', '冕宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513434, 24, 513400, '越西县', '越西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513435, 24, 513400, '甘洛县', '甘洛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513436, 24, 513400, '美姑县', '美姑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (513437, 24, 513400, '雷波县', '雷波县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520000, 22, NULL, '贵州', '贵州省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520100, 23, 520000, '贵阳', '贵阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520101, 24, 520100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520102, 24, 520100, '南明区', '南明区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520103, 24, 520100, '云岩区', '云岩区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520111, 24, 520100, '花溪区', '花溪区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520112, 24, 520100, '乌当区', '乌当区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520113, 24, 520100, '白云区', '白云区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520114, 24, 520100, '小河区', '小河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520121, 24, 520100, '开阳县', '开阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520122, 24, 520100, '息烽县', '息烽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520123, 24, 520100, '修文县', '修文县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520181, 24, 520100, '清镇', '清镇市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520200, 23, 520000, '六盘水', '六盘水市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520201, 24, 520200, '钟山区', '钟山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520203, 24, 520200, '六枝特区', '六枝特区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520221, 24, 520200, '水城县', '水城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520222, 24, 520200, '盘县', '盘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520300, 23, 520000, '遵义', '遵义市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520301, 24, 520300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520302, 24, 520300, '红花岗区', '红花岗区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520303, 24, 520300, '汇川区', '汇川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520321, 24, 520300, '遵义县', '遵义县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520322, 24, 520300, '桐梓县', '桐梓县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520323, 24, 520300, '绥阳县', '绥阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520324, 24, 520300, '正安县', '正安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520325, 24, 520300, '道真仡佬族苗族自治县', '道真仡佬族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520326, 24, 520300, '务川仡佬族苗族自治县', '务川仡佬族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520327, 24, 520300, '凤冈县', '凤冈县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520328, 24, 520300, '湄潭县', '湄潭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520329, 24, 520300, '余庆县', '余庆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520330, 24, 520300, '习水县', '习水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520381, 24, 520300, '赤水', '赤水市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520382, 24, 520300, '仁怀', '仁怀市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520400, 23, 520000, '安顺', '安顺市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520401, 24, 520400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520402, 24, 520400, '西秀区', '西秀区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520421, 24, 520400, '平坝县', '平坝县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520422, 24, 520400, '普定县', '普定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520423, 24, 520400, '镇宁布依族苗族自治县', '镇宁布依族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520424, 24, 520400, '关岭布依族苗族自治县', '关岭布依族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520425, 24, 520400, '紫云苗族布依族自治县', '紫云苗族布依族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520500, 23, 520000, '毕节', '毕节市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520501, 24, 520500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520502, 24, 520500, '七星关区', '七星关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520521, 24, 520500, '大方县', '大方县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520522, 24, 520500, '黔西县', '黔西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520523, 24, 520500, '金沙县', '金沙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520524, 24, 520500, '织金县', '织金县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520525, 24, 520500, '纳雍县', '纳雍县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520526, 24, 520500, '威宁彝族回族苗族自治县', '威宁彝族回族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520527, 24, 520500, '赫章县', '赫章县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520600, 23, 520000, '铜仁', '铜仁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520601, 24, 520600, '市辖区 ', '市辖区 ');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520602, 24, 520600, '碧江区', '碧江区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520603, 24, 520600, '万山区', '万山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520621, 24, 520600, '江口县', '江口县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520622, 24, 520600, '玉屏侗族自治县', '玉屏侗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520623, 24, 520600, '石阡县', '石阡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520624, 24, 520600, '思南县', '思南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520625, 24, 520600, '印江土家族苗族自治县', '印江土家族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520626, 24, 520600, '德江县', '德江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520627, 24, 520600, '沿河土家族自治县', '沿河土家族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (520628, 24, 520600, '松桃苗族自治县', '松桃苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522300, 23, 520000, '黔西南布依族苗族自治州', '黔西南布依族苗族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522301, 24, 522300, '兴义', '兴义市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522322, 24, 522300, '兴仁县', '兴仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522323, 24, 522300, '普安县', '普安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522324, 24, 522300, '晴隆县', '晴隆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522325, 24, 522300, '贞丰县', '贞丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522326, 24, 522300, '望谟县', '望谟县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522327, 24, 522300, '册亨县', '册亨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522328, 24, 522300, '安龙县', '安龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522600, 23, 520000, '黔东南苗族侗族自治州', '黔东南苗族侗族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522601, 24, 522600, '凯里', '凯里市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522622, 24, 522600, '黄平县', '黄平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522623, 24, 522600, '施秉县', '施秉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522624, 24, 522600, '三穗县', '三穗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522625, 24, 522600, '镇远县', '镇远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522626, 24, 522600, '岑巩县', '岑巩县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522627, 24, 522600, '天柱县', '天柱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522628, 24, 522600, '锦屏县', '锦屏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522629, 24, 522600, '剑河县', '剑河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522630, 24, 522600, '台江县', '台江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522631, 24, 522600, '黎平县', '黎平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522632, 24, 522600, '榕江县', '榕江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522633, 24, 522600, '从江县', '从江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522634, 24, 522600, '雷山县', '雷山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522635, 24, 522600, '麻江县', '麻江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522636, 24, 522600, '丹寨县', '丹寨县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522700, 23, 520000, '黔南布依族苗族自治州', '黔南布依族苗族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522701, 24, 522700, '都匀', '都匀市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522702, 24, 522700, '福泉', '福泉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522722, 24, 522700, '荔波县', '荔波县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522723, 24, 522700, '贵定县', '贵定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522725, 24, 522700, '瓮安县', '瓮安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522726, 24, 522700, '独山县', '独山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522727, 24, 522700, '平塘县', '平塘县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522728, 24, 522700, '罗甸县', '罗甸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522729, 24, 522700, '长顺县', '长顺县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522730, 24, 522700, '龙里县', '龙里县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522731, 24, 522700, '惠水县', '惠水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (522732, 24, 522700, '三都水族自治县', '三都水族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530000, 22, NULL, '云南', '云南省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530100, 23, 530000, '昆明', '昆明市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530101, 24, 530100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530102, 24, 530100, '五华区', '五华区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530103, 24, 530100, '盘龙区', '盘龙区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530111, 24, 530100, '官渡区', '官渡区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530112, 24, 530100, '西山区', '西山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530113, 24, 530100, '东川区', '东川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530114, 24, 530100, '呈贡区', '呈贡区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530122, 24, 530100, '晋宁县', '晋宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530124, 24, 530100, '富民县', '富民县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530125, 24, 530100, '宜良县', '宜良县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530126, 24, 530100, '石林彝族自治县', '石林彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530127, 24, 530100, '嵩明县', '嵩明县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530128, 24, 530100, '禄劝彝族苗族自治县', '禄劝彝族苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530129, 24, 530100, '寻甸回族彝族自治县', '寻甸回族彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530181, 24, 530100, '安宁', '安宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530300, 23, 530000, '曲靖', '曲靖市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530301, 24, 530300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530302, 24, 530300, '麒麟区', '麒麟区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530321, 24, 530300, '马龙县', '马龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530322, 24, 530300, '陆良县', '陆良县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530323, 24, 530300, '师宗县', '师宗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530324, 24, 530300, '罗平县', '罗平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530325, 24, 530300, '富源县', '富源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530326, 24, 530300, '会泽县', '会泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530328, 24, 530300, '沾益县', '沾益县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530381, 24, 530300, '宣威', '宣威市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530400, 23, 530000, '玉溪', '玉溪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530401, 24, 530400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530402, 24, 530400, '红塔区', '红塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530421, 24, 530400, '江川县', '江川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530422, 24, 530400, '澄江县', '澄江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530423, 24, 530400, '通海县', '通海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530424, 24, 530400, '华宁县', '华宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530425, 24, 530400, '易门县', '易门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530426, 24, 530400, '峨山彝族自治县', '峨山彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530427, 24, 530400, '新平彝族傣族自治县', '新平彝族傣族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530428, 24, 530400, '元江哈尼族彝族傣族自治县', '元江哈尼族彝族傣族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530500, 23, 530000, '保山', '保山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530501, 24, 530500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530502, 24, 530500, '隆阳区', '隆阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530521, 24, 530500, '施甸县', '施甸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530522, 24, 530500, '腾冲县', '腾冲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530523, 24, 530500, '龙陵县', '龙陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530524, 24, 530500, '昌宁县', '昌宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530600, 23, 530000, '昭通', '昭通市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530601, 24, 530600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530602, 24, 530600, '昭阳区', '昭阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530621, 24, 530600, '鲁甸县', '鲁甸县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530622, 24, 530600, '巧家县', '巧家县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530623, 24, 530600, '盐津县', '盐津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530624, 24, 530600, '大关县', '大关县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530625, 24, 530600, '永善县', '永善县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530626, 24, 530600, '绥江县', '绥江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530627, 24, 530600, '镇雄县', '镇雄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530628, 24, 530600, '彝良县', '彝良县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530629, 24, 530600, '威信县', '威信县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530630, 24, 530600, '水富县', '水富县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530700, 23, 530000, '丽江', '丽江市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530701, 24, 530700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530702, 24, 530700, '古城区', '古城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530721, 24, 530700, '玉龙纳西族自治县', '玉龙纳西族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530722, 24, 530700, '永胜县', '永胜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530723, 24, 530700, '华坪县', '华坪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530724, 24, 530700, '宁蒗彝族自治县', '宁蒗彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530800, 23, 530000, '普洱', '普洱市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530801, 24, 530800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530802, 24, 530800, '思茅区', '思茅区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530821, 24, 530800, '宁洱哈尼族彝族自治县', '宁洱哈尼族彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530822, 24, 530800, '墨江哈尼族自治县', '墨江哈尼族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530823, 24, 530800, '景东彝族自治县', '景东彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530824, 24, 530800, '景谷傣族彝族自治县', '景谷傣族彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530825, 24, 530800, '镇沅彝族哈尼族拉祜族自治县', '镇沅彝族哈尼族拉祜族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530826, 24, 530800, '江城哈尼族彝族自治县', '江城哈尼族彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530827, 24, 530800, '孟连傣族拉祜族佤族自治县', '孟连傣族拉祜族佤族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530828, 24, 530800, '澜沧拉祜族自治县', '澜沧拉祜族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530829, 24, 530800, '西盟佤族自治县', '西盟佤族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530900, 23, 530000, '临沧', '临沧市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530901, 24, 530900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530902, 24, 530900, '临翔区', '临翔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530921, 24, 530900, '凤庆县', '凤庆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530922, 24, 530900, '云县', '云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530923, 24, 530900, '永德县', '永德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530924, 24, 530900, '镇康县', '镇康县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530925, 24, 530900, '双江拉祜族佤族布朗族傣族自治县', '双江拉祜族佤族布朗族傣族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530926, 24, 530900, '耿马傣族佤族自治县', '耿马傣族佤族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (530927, 24, 530900, '沧源佤族自治县', '沧源佤族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532300, 23, 530000, '楚雄彝族自治州', '楚雄彝族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532301, 24, 532300, '楚雄', '楚雄市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532322, 24, 532300, '双柏县', '双柏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532323, 24, 532300, '牟定县', '牟定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532324, 24, 532300, '南华县', '南华县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532325, 24, 532300, '姚安县', '姚安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532326, 24, 532300, '大姚县', '大姚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532327, 24, 532300, '永仁县', '永仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532328, 24, 532300, '元谋县', '元谋县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532329, 24, 532300, '武定县', '武定县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532331, 24, 532300, '禄丰县', '禄丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532500, 23, 530000, '红河哈尼族彝族自治州', '红河哈尼族彝族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532501, 24, 532500, '个旧', '个旧市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532502, 24, 532500, '开远', '开远市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532503, 24, 532500, '蒙自', '蒙自市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532523, 24, 532500, '屏边苗族自治县', '屏边苗族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532524, 24, 532500, '建水县', '建水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532525, 24, 532500, '石屏县', '石屏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532526, 24, 532500, '弥勒县', '弥勒县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532527, 24, 532500, '泸西县', '泸西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532528, 24, 532500, '元阳县', '元阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532529, 24, 532500, '红河县', '红河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532530, 24, 532500, '金平苗族瑶族傣族自治县', '金平苗族瑶族傣族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532531, 24, 532500, '绿春县', '绿春县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532532, 24, 532500, '河口瑶族自治县', '河口瑶族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532600, 23, 530000, '文山壮族苗族自治州', '文山壮族苗族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532601, 24, 532600, '文山', '文山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532622, 24, 532600, '砚山县', '砚山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532623, 24, 532600, '西畴县', '西畴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532624, 24, 532600, '麻栗坡县', '麻栗坡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532625, 24, 532600, '马关县', '马关县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532626, 24, 532600, '丘北县', '丘北县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532627, 24, 532600, '广南县', '广南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532628, 24, 532600, '富宁县', '富宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532800, 23, 530000, '西双版纳傣族自治州', '西双版纳傣族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532801, 24, 532800, '景洪', '景洪市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532822, 24, 532800, '勐海县', '勐海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532823, 24, 532800, '勐腊县', '勐腊县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532900, 23, 530000, '大理白族自治州', '大理白族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532901, 24, 532900, '大理', '大理市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532922, 24, 532900, '漾濞彝族自治县', '漾濞彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532923, 24, 532900, '祥云县', '祥云县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532924, 24, 532900, '宾川县', '宾川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532925, 24, 532900, '弥渡县', '弥渡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532926, 24, 532900, '南涧彝族自治县', '南涧彝族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532927, 24, 532900, '巍山彝族回族自治县', '巍山彝族回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532928, 24, 532900, '永平县', '永平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532929, 24, 532900, '云龙县', '云龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532930, 24, 532900, '洱源县', '洱源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532931, 24, 532900, '剑川县', '剑川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (532932, 24, 532900, '鹤庆县', '鹤庆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533100, 23, 530000, '德宏傣族景颇族自治州', '德宏傣族景颇族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533102, 24, 533100, '瑞丽', '瑞丽市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533103, 24, 533100, '芒', '芒市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533122, 24, 533100, '梁河县', '梁河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533123, 24, 533100, '盈江县', '盈江县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533124, 24, 533100, '陇川县', '陇川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533300, 23, 530000, '怒江傈僳族自治州', '怒江傈僳族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533321, 24, 533300, '泸水县', '泸水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533323, 24, 533300, '福贡县', '福贡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533324, 24, 533300, '贡山独龙族怒族自治县', '贡山独龙族怒族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533325, 24, 533300, '兰坪白族普米族自治县', '兰坪白族普米族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533400, 23, 530000, '迪庆藏族自治州', '迪庆藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533421, 24, 533400, '香格里拉县', '香格里拉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533422, 24, 533400, '德钦县', '德钦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (533423, 24, 533400, '维西傈僳族自治县', '维西傈僳族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540000, 22, NULL, '西藏', '西藏自治区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540100, 23, 540000, '拉萨', '拉萨市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540101, 24, 540100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540102, 24, 540100, '城关区', '城关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540121, 24, 540100, '林周县', '林周县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540122, 24, 540100, '当雄县', '当雄县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540123, 24, 540100, '尼木县', '尼木县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540124, 24, 540100, '曲水县', '曲水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540125, 24, 540100, '堆龙德庆县', '堆龙德庆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540126, 24, 540100, '达孜县', '达孜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (540127, 24, 540100, '墨竹工卡县', '墨竹工卡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542100, 23, 540000, '昌都地区', '昌都地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542121, 24, 542100, '昌都县', '昌都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542122, 24, 542100, '江达县', '江达县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542123, 24, 542100, '贡觉县', '贡觉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542124, 24, 542100, '类乌齐县', '类乌齐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542125, 24, 542100, '丁青县', '丁青县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542126, 24, 542100, '察雅县', '察雅县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542127, 24, 542100, '八宿县', '八宿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542128, 24, 542100, '左贡县', '左贡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542129, 24, 542100, '芒康县', '芒康县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542132, 24, 542100, '洛隆县', '洛隆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542133, 24, 542100, '边坝县', '边坝县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542200, 23, 540000, '山南地区', '山南地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542221, 24, 542200, '乃东县', '乃东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542222, 24, 542200, '扎囊县', '扎囊县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542223, 24, 542200, '贡嘎县', '贡嘎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542224, 24, 542200, '桑日县', '桑日县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542225, 24, 542200, '琼结县', '琼结县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542226, 24, 542200, '曲松县', '曲松县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542227, 24, 542200, '措美县', '措美县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542228, 24, 542200, '洛扎县', '洛扎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542229, 24, 542200, '加查县', '加查县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542231, 24, 542200, '隆子县', '隆子县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542232, 24, 542200, '错那县', '错那县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542233, 24, 542200, '浪卡子县', '浪卡子县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542300, 23, 540000, '日喀则地区', '日喀则地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542301, 24, 542300, '日喀则', '日喀则市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542322, 24, 542300, '南木林县', '南木林县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542323, 24, 542300, '江孜县', '江孜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542324, 24, 542300, '定日县', '定日县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542325, 24, 542300, '萨迦县', '萨迦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542326, 24, 542300, '拉孜县', '拉孜县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542327, 24, 542300, '昂仁县', '昂仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542328, 24, 542300, '谢通门县', '谢通门县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542329, 24, 542300, '白朗县', '白朗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542330, 24, 542300, '仁布县', '仁布县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542331, 24, 542300, '康马县', '康马县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542332, 24, 542300, '定结县', '定结县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542333, 24, 542300, '仲巴县', '仲巴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542334, 24, 542300, '亚东县', '亚东县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542335, 24, 542300, '吉隆县', '吉隆县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542336, 24, 542300, '聂拉木县', '聂拉木县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542337, 24, 542300, '萨嘎县', '萨嘎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542338, 24, 542300, '岗巴县', '岗巴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542400, 23, 540000, '那曲地区', '那曲地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542421, 24, 542400, '那曲县', '那曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542422, 24, 542400, '嘉黎县', '嘉黎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542423, 24, 542400, '比如县', '比如县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542424, 24, 542400, '聂荣县', '聂荣县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542425, 24, 542400, '安多县', '安多县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542426, 24, 542400, '申扎县', '申扎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542427, 24, 542400, '索县', '索县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542428, 24, 542400, '班戈县', '班戈县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542429, 24, 542400, '巴青县', '巴青县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542430, 24, 542400, '尼玛县', '尼玛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542500, 23, 540000, '阿里地区', '阿里地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542521, 24, 542500, '普兰县', '普兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542522, 24, 542500, '札达县', '札达县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542523, 24, 542500, '噶尔县', '噶尔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542524, 24, 542500, '日土县', '日土县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542525, 24, 542500, '革吉县', '革吉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542526, 24, 542500, '改则县', '改则县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542527, 24, 542500, '措勤县', '措勤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542600, 23, 540000, '林芝地区', '林芝地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542621, 24, 542600, '林芝县', '林芝县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542622, 24, 542600, '工布江达县', '工布江达县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542623, 24, 542600, '米林县', '米林县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542624, 24, 542600, '墨脱县', '墨脱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542625, 24, 542600, '波密县', '波密县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542626, 24, 542600, '察隅县', '察隅县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (542627, 24, 542600, '朗县', '朗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610000, 22, NULL, '陕西', '陕西省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610100, 23, 610000, '西安', '西安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610101, 24, 610100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610102, 24, 610100, '新城区', '新城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610103, 24, 610100, '碑林区', '碑林区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610104, 24, 610100, '莲湖区', '莲湖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610111, 24, 610100, '灞桥区', '灞桥区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610112, 24, 610100, '未央区', '未央区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610113, 24, 610100, '雁塔区', '雁塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610114, 24, 610100, '阎良区', '阎良区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610115, 24, 610100, '临潼区', '临潼区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610116, 24, 610100, '长安区', '长安区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610122, 24, 610100, '蓝田县', '蓝田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610124, 24, 610100, '周至县', '周至县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610125, 24, 610100, '户县', '户县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610126, 24, 610100, '高陵县', '高陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610200, 23, 610000, '铜川', '铜川市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610201, 24, 610200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610202, 24, 610200, '王益区', '王益区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610203, 24, 610200, '印台区', '印台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610204, 24, 610200, '耀州区', '耀州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610222, 24, 610200, '宜君县', '宜君县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610300, 23, 610000, '宝鸡', '宝鸡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610301, 24, 610300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610302, 24, 610300, '渭滨区', '渭滨区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610303, 24, 610300, '金台区', '金台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610304, 24, 610300, '陈仓区', '陈仓区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610322, 24, 610300, '凤翔县', '凤翔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610323, 24, 610300, '岐山县', '岐山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610324, 24, 610300, '扶风县', '扶风县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610326, 24, 610300, '眉县', '眉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610327, 24, 610300, '陇县', '陇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610328, 24, 610300, '千阳县', '千阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610329, 24, 610300, '麟游县', '麟游县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610330, 24, 610300, '凤县', '凤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610331, 24, 610300, '太白县', '太白县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610400, 23, 610000, '咸阳', '咸阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610401, 24, 610400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610402, 24, 610400, '秦都区', '秦都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610403, 24, 610400, '杨陵区', '杨陵区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610404, 24, 610400, '渭城区', '渭城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610422, 24, 610400, '三原县', '三原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610423, 24, 610400, '泾阳县', '泾阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610424, 24, 610400, '乾县', '乾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610425, 24, 610400, '礼泉县', '礼泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610426, 24, 610400, '永寿县', '永寿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610427, 24, 610400, '彬县', '彬县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610428, 24, 610400, '长武县', '长武县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610429, 24, 610400, '旬邑县', '旬邑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610430, 24, 610400, '淳化县', '淳化县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610431, 24, 610400, '武功县', '武功县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610481, 24, 610400, '兴平', '兴平市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610500, 23, 610000, '渭南', '渭南市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610501, 24, 610500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610502, 24, 610500, '临渭区', '临渭区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610521, 24, 610500, '华县', '华县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610522, 24, 610500, '潼关县', '潼关县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610523, 24, 610500, '大荔县', '大荔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610524, 24, 610500, '合阳县', '合阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610525, 24, 610500, '澄城县', '澄城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610526, 24, 610500, '蒲城县', '蒲城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610527, 24, 610500, '白水县', '白水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610528, 24, 610500, '富平县', '富平县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610581, 24, 610500, '韩城', '韩城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610582, 24, 610500, '华阴', '华阴市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610600, 23, 610000, '延安', '延安市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610601, 24, 610600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610602, 24, 610600, '宝塔区', '宝塔区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610621, 24, 610600, '延长县', '延长县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610622, 24, 610600, '延川县', '延川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610623, 24, 610600, '子长县', '子长县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610624, 24, 610600, '安塞县', '安塞县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610625, 24, 610600, '志丹县', '志丹县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610626, 24, 610600, '吴起县', '吴起县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610627, 24, 610600, '甘泉县', '甘泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610628, 24, 610600, '富县', '富县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610629, 24, 610600, '洛川县', '洛川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610630, 24, 610600, '宜川县', '宜川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610631, 24, 610600, '黄龙县', '黄龙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610632, 24, 610600, '黄陵县', '黄陵县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610700, 23, 610000, '汉中', '汉中市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610701, 24, 610700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610702, 24, 610700, '汉台区', '汉台区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610721, 24, 610700, '南郑县', '南郑县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610722, 24, 610700, '城固县', '城固县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610723, 24, 610700, '洋县', '洋县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610724, 24, 610700, '西乡县', '西乡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610725, 24, 610700, '勉县', '勉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610726, 24, 610700, '宁强县', '宁强县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610727, 24, 610700, '略阳县', '略阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610728, 24, 610700, '镇巴县', '镇巴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610729, 24, 610700, '留坝县', '留坝县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610730, 24, 610700, '佛坪县', '佛坪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610800, 23, 610000, '榆林', '榆林市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610801, 24, 610800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610802, 24, 610800, '榆阳区', '榆阳区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610821, 24, 610800, '神木县', '神木县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610822, 24, 610800, '府谷县', '府谷县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610823, 24, 610800, '横山县', '横山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610824, 24, 610800, '靖边县', '靖边县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610825, 24, 610800, '定边县', '定边县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610826, 24, 610800, '绥德县', '绥德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610827, 24, 610800, '米脂县', '米脂县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610828, 24, 610800, '佳县', '佳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610829, 24, 610800, '吴堡县', '吴堡县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610830, 24, 610800, '清涧县', '清涧县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610831, 24, 610800, '子洲县', '子洲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610900, 23, 610000, '安康', '安康市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610901, 24, 610900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610902, 24, 610900, '汉滨区', '汉滨区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610921, 24, 610900, '汉阴县', '汉阴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610922, 24, 610900, '石泉县', '石泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610923, 24, 610900, '宁陕县', '宁陕县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610924, 24, 610900, '紫阳县', '紫阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610925, 24, 610900, '岚皋县', '岚皋县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610926, 24, 610900, '平利县', '平利县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610927, 24, 610900, '镇坪县', '镇坪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610928, 24, 610900, '旬阳县', '旬阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (610929, 24, 610900, '白河县', '白河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611000, 23, 610000, '商洛', '商洛市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611001, 24, 611000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611002, 24, 611000, '商州区', '商州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611021, 24, 611000, '洛南县', '洛南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611022, 24, 611000, '丹凤县', '丹凤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611023, 24, 611000, '商南县', '商南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611024, 24, 611000, '山阳县', '山阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611025, 24, 611000, '镇安县', '镇安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (611026, 24, 611000, '柞水县', '柞水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620000, 22, NULL, '甘肃', '甘肃省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620100, 23, 620000, '兰州', '兰州市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620101, 24, 620100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620102, 24, 620100, '城关区', '城关区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620103, 24, 620100, '七里河区', '七里河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620104, 24, 620100, '西固区', '西固区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620105, 24, 620100, '安宁区', '安宁区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620111, 24, 620100, '红古区', '红古区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620121, 24, 620100, '永登县', '永登县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620122, 24, 620100, '皋兰县', '皋兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620123, 24, 620100, '榆中县', '榆中县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620200, 23, 620000, '嘉峪关', '嘉峪关市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620201, 24, 620200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620300, 23, 620000, '金昌', '金昌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620301, 24, 620300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620302, 24, 620300, '金川区', '金川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620321, 24, 620300, '永昌县', '永昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620400, 23, 620000, '白银', '白银市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620401, 24, 620400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620402, 24, 620400, '白银区', '白银区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620403, 24, 620400, '平川区', '平川区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620421, 24, 620400, '靖远县', '靖远县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620422, 24, 620400, '会宁县', '会宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620423, 24, 620400, '景泰县', '景泰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620500, 23, 620000, '天水', '天水市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620501, 24, 620500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620502, 24, 620500, '秦州区', '秦州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620503, 24, 620500, '麦积区', '麦积区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620521, 24, 620500, '清水县', '清水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620522, 24, 620500, '秦安县', '秦安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620523, 24, 620500, '甘谷县', '甘谷县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620524, 24, 620500, '武山县', '武山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620525, 24, 620500, '张家川回族自治县', '张家川回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620600, 23, 620000, '武威', '武威市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620601, 24, 620600, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620602, 24, 620600, '凉州区', '凉州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620621, 24, 620600, '民勤县', '民勤县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620622, 24, 620600, '古浪县', '古浪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620623, 24, 620600, '天祝藏族自治县', '天祝藏族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620700, 23, 620000, '张掖', '张掖市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620701, 24, 620700, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620702, 24, 620700, '甘州区', '甘州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620721, 24, 620700, '肃南裕固族自治县', '肃南裕固族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620722, 24, 620700, '民乐县', '民乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620723, 24, 620700, '临泽县', '临泽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620724, 24, 620700, '高台县', '高台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620725, 24, 620700, '山丹县', '山丹县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620800, 23, 620000, '平凉', '平凉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620801, 24, 620800, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620802, 24, 620800, '崆峒区', '崆峒区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620821, 24, 620800, '泾川县', '泾川县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620822, 24, 620800, '灵台县', '灵台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620823, 24, 620800, '崇信县', '崇信县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620824, 24, 620800, '华亭县', '华亭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620825, 24, 620800, '庄浪县', '庄浪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620826, 24, 620800, '静宁县', '静宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620900, 23, 620000, '酒泉', '酒泉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620901, 24, 620900, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620902, 24, 620900, '肃州区', '肃州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620921, 24, 620900, '金塔县', '金塔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620922, 24, 620900, '瓜州县', '瓜州县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620923, 24, 620900, '肃北蒙古族自治县', '肃北蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620924, 24, 620900, '阿克塞哈萨克族自治县', '阿克塞哈萨克族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620981, 24, 620900, '玉门', '玉门市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (620982, 24, 620900, '敦煌', '敦煌市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621000, 23, 620000, '庆阳', '庆阳市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621001, 24, 621000, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621002, 24, 621000, '西峰区', '西峰区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621021, 24, 621000, '庆城县', '庆城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621022, 24, 621000, '环县', '环县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621023, 24, 621000, '华池县', '华池县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621024, 24, 621000, '合水县', '合水县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621025, 24, 621000, '正宁县', '正宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621026, 24, 621000, '宁县', '宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621027, 24, 621000, '镇原县', '镇原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621100, 23, 620000, '定西', '定西市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621101, 24, 621100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621102, 24, 621100, '安定区', '安定区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621121, 24, 621100, '通渭县', '通渭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621122, 24, 621100, '陇西县', '陇西县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621123, 24, 621100, '渭源县', '渭源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621124, 24, 621100, '临洮县', '临洮县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621125, 24, 621100, '漳县', '漳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621126, 24, 621100, '岷县', '岷县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621200, 23, 620000, '陇南', '陇南市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621201, 24, 621200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621202, 24, 621200, '武都区', '武都区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621221, 24, 621200, '成县', '成县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621222, 24, 621200, '文县', '文县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621223, 24, 621200, '宕昌县', '宕昌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621224, 24, 621200, '康县', '康县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621225, 24, 621200, '西和县', '西和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621226, 24, 621200, '礼县', '礼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621227, 24, 621200, '徽县', '徽县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (621228, 24, 621200, '两当县', '两当县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622900, 23, 620000, '临夏回族自治州', '临夏回族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622901, 24, 622900, '临夏', '临夏市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622921, 24, 622900, '临夏县', '临夏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622922, 24, 622900, '康乐县', '康乐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622923, 24, 622900, '永靖县', '永靖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622924, 24, 622900, '广河县', '广河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622925, 24, 622900, '和政县', '和政县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622926, 24, 622900, '东乡族自治县', '东乡族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (622927, 24, 622900, '积石山保安族东乡族撒拉族自治县', '积石山保安族东乡族撒拉族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623000, 23, 620000, '甘南藏族自治州', '甘南藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623001, 24, 623000, '合作', '合作市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623021, 24, 623000, '临潭县', '临潭县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623022, 24, 623000, '卓尼县', '卓尼县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623023, 24, 623000, '舟曲县', '舟曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623024, 24, 623000, '迭部县', '迭部县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623025, 24, 623000, '玛曲县', '玛曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623026, 24, 623000, '碌曲县', '碌曲县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (623027, 24, 623000, '夏河县', '夏河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630000, 22, NULL, '青海', '青海省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630100, 23, 630000, '西宁', '西宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630101, 24, 630100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630102, 24, 630100, '城东区', '城东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630103, 24, 630100, '城中区', '城中区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630104, 24, 630100, '城西区', '城西区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630105, 24, 630100, '城北区', '城北区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630121, 24, 630100, '大通回族土族自治县', '大通回族土族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630122, 24, 630100, '湟中县', '湟中县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (630123, 24, 630100, '湟源县', '湟源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632100, 23, 630000, '海东地区', '海东地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632121, 24, 632100, '平安县', '平安县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632122, 24, 632100, '民和回族土族自治县', '民和回族土族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632123, 24, 632100, '乐都县', '乐都县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632126, 24, 632100, '互助土族自治县', '互助土族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632127, 24, 632100, '化隆回族自治县', '化隆回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632128, 24, 632100, '循化撒拉族自治县', '循化撒拉族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632200, 23, 630000, '海北藏族自治州', '海北藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632221, 24, 632200, '门源回族自治县', '门源回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632222, 24, 632200, '祁连县', '祁连县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632223, 24, 632200, '海晏县', '海晏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632224, 24, 632200, '刚察县', '刚察县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632300, 23, 630000, '黄南藏族自治州', '黄南藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632321, 24, 632300, '同仁县', '同仁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632322, 24, 632300, '尖扎县', '尖扎县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632323, 24, 632300, '泽库县', '泽库县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632324, 24, 632300, '河南蒙古族自治县', '河南蒙古族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632500, 23, 630000, '海南藏族自治州', '海南藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632521, 24, 632500, '共和县', '共和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632522, 24, 632500, '同德县', '同德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632523, 24, 632500, '贵德县', '贵德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632524, 24, 632500, '兴海县', '兴海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632525, 24, 632500, '贵南县', '贵南县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632600, 23, 630000, '果洛藏族自治州', '果洛藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632621, 24, 632600, '玛沁县', '玛沁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632622, 24, 632600, '班玛县', '班玛县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632623, 24, 632600, '甘德县', '甘德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632624, 24, 632600, '达日县', '达日县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632625, 24, 632600, '久治县', '久治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632626, 24, 632600, '玛多县', '玛多县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632700, 23, 630000, '玉树藏族自治州', '玉树藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632721, 24, 632700, '玉树县', '玉树县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632722, 24, 632700, '杂多县', '杂多县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632723, 24, 632700, '称多县', '称多县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632724, 24, 632700, '治多县', '治多县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632725, 24, 632700, '囊谦县', '囊谦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632726, 24, 632700, '曲麻莱县', '曲麻莱县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632800, 23, 630000, '海西蒙古族藏族自治州', '海西蒙古族藏族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632801, 24, 632800, '格尔木', '格尔木市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632802, 24, 632800, '德令哈', '德令哈市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632821, 24, 632800, '乌兰县', '乌兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632822, 24, 632800, '都兰县', '都兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (632823, 24, 632800, '天峻县', '天峻县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640000, 22, NULL, '宁夏', '宁夏回族自治区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640100, 23, 640000, '银川', '银川市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640101, 24, 640100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640104, 24, 640100, '兴庆区', '兴庆区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640105, 24, 640100, '西夏区', '西夏区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640106, 24, 640100, '金凤区', '金凤区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640121, 24, 640100, '永宁县', '永宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640122, 24, 640100, '贺兰县', '贺兰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640181, 24, 640100, '灵武', '灵武市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640200, 23, 640000, '石嘴山', '石嘴山市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640201, 24, 640200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640202, 24, 640200, '大武口区', '大武口区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640205, 24, 640200, '惠农区', '惠农区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640221, 24, 640200, '平罗县', '平罗县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640300, 23, 640000, '吴忠', '吴忠市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640301, 24, 640300, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640302, 24, 640300, '利通区', '利通区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640303, 24, 640300, '红寺堡区', '红寺堡区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640323, 24, 640300, '盐池县', '盐池县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640324, 24, 640300, '同心县', '同心县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640381, 24, 640300, '青铜峡', '青铜峡市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640400, 23, 640000, '固原', '固原市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640401, 24, 640400, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640402, 24, 640400, '原州区', '原州区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640422, 24, 640400, '西吉县', '西吉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640423, 24, 640400, '隆德县', '隆德县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640424, 24, 640400, '泾源县', '泾源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640425, 24, 640400, '彭阳县', '彭阳县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640500, 23, 640000, '中卫', '中卫市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640501, 24, 640500, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640502, 24, 640500, '沙坡头区', '沙坡头区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640521, 24, 640500, '中宁县', '中宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (640522, 24, 640500, '海原县', '海原县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650000, 22, NULL, '新疆', '新疆维吾尔自治区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650100, 23, 650000, '乌鲁木齐', '乌鲁木齐市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650101, 24, 650100, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650102, 24, 650100, '天山区', '天山区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650103, 24, 650100, '沙依巴克区', '沙依巴克区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650104, 24, 650100, '新市区', '新市区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650105, 24, 650100, '水磨沟区', '水磨沟区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650106, 24, 650100, '头屯河区', '头屯河区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650107, 24, 650100, '达坂城区', '达坂城区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650109, 24, 650100, '米东区', '米东区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650121, 24, 650100, '乌鲁木齐县', '乌鲁木齐县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650200, 23, 650000, '克拉玛依', '克拉玛依市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650201, 24, 650200, '市辖区', '市辖区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650202, 24, 650200, '独山子区', '独山子区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650203, 24, 650200, '克拉玛依区', '克拉玛依区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650204, 24, 650200, '白碱滩区', '白碱滩区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (650205, 24, 650200, '乌尔禾区', '乌尔禾区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652100, 23, 650000, '吐鲁番地区', '吐鲁番地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652101, 24, 652100, '吐鲁番', '吐鲁番市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652122, 24, 652100, '鄯善县', '鄯善县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652123, 24, 652100, '托克逊县', '托克逊县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652200, 23, 650000, '哈密地区', '哈密地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652201, 24, 652200, '哈密', '哈密市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652222, 24, 652200, '巴里坤哈萨克自治县', '巴里坤哈萨克自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652223, 24, 652200, '伊吾县', '伊吾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652300, 23, 650000, '昌吉回族自治州', '昌吉回族自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652301, 24, 652300, '昌吉', '昌吉市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652302, 24, 652300, '阜康', '阜康市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652323, 24, 652300, '呼图壁县', '呼图壁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652324, 24, 652300, '玛纳斯县', '玛纳斯县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652325, 24, 652300, '奇台县', '奇台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652327, 24, 652300, '吉木萨尔县', '吉木萨尔县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652328, 24, 652300, '木垒哈萨克自治县', '木垒哈萨克自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652700, 23, 650000, '博尔塔拉蒙古自治州', '博尔塔拉蒙古自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652701, 24, 652700, '博乐', '博乐市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652722, 24, 652700, '精河县', '精河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652723, 24, 652700, '温泉县', '温泉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652800, 23, 650000, '巴音郭楞蒙古自治州', '巴音郭楞蒙古自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652801, 24, 652800, '库尔勒', '库尔勒市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652822, 24, 652800, '轮台县', '轮台县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652823, 24, 652800, '尉犁县', '尉犁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652824, 24, 652800, '若羌县', '若羌县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652825, 24, 652800, '且末县', '且末县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652826, 24, 652800, '焉耆回族自治县', '焉耆回族自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652827, 24, 652800, '和静县', '和静县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652828, 24, 652800, '和硕县', '和硕县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652829, 24, 652800, '博湖县', '博湖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652900, 23, 650000, '阿克苏地区', '阿克苏地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652901, 24, 652900, '阿克苏', '阿克苏市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652922, 24, 652900, '温宿县', '温宿县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652923, 24, 652900, '库车县', '库车县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652924, 24, 652900, '沙雅县', '沙雅县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652925, 24, 652900, '新和县', '新和县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652926, 24, 652900, '拜城县', '拜城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652927, 24, 652900, '乌什县', '乌什县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652928, 24, 652900, '阿瓦提县', '阿瓦提县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (652929, 24, 652900, '柯坪县', '柯坪县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653000, 23, 650000, '克孜勒苏柯尔克孜自治州', '克孜勒苏柯尔克孜自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653001, 24, 653000, '阿图什', '阿图什市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653022, 24, 653000, '阿克陶县', '阿克陶县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653023, 24, 653000, '阿合奇县', '阿合奇县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653024, 24, 653000, '乌恰县', '乌恰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653100, 23, 650000, '喀什地区', '喀什地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653101, 24, 653100, '喀什', '喀什市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653121, 24, 653100, '疏附县', '疏附县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653122, 24, 653100, '疏勒县', '疏勒县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653123, 24, 653100, '英吉沙县', '英吉沙县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653124, 24, 653100, '泽普县', '泽普县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653125, 24, 653100, '莎车县', '莎车县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653126, 24, 653100, '叶城县', '叶城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653127, 24, 653100, '麦盖提县', '麦盖提县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653128, 24, 653100, '岳普湖县', '岳普湖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653129, 24, 653100, '伽师县', '伽师县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653130, 24, 653100, '巴楚县', '巴楚县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653131, 24, 653100, '塔什库尔干塔吉克自治县', '塔什库尔干塔吉克自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653200, 23, 650000, '和田地区', '和田地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653201, 24, 653200, '和田', '和田市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653221, 24, 653200, '和田县', '和田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653222, 24, 653200, '墨玉县', '墨玉县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653223, 24, 653200, '皮山县', '皮山县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653224, 24, 653200, '洛浦县', '洛浦县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653225, 24, 653200, '策勒县', '策勒县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653226, 24, 653200, '于田县', '于田县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (653227, 24, 653200, '民丰县', '民丰县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654000, 23, 650000, '伊犁哈萨克自治州', '伊犁哈萨克自治州');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654002, 24, 654000, '伊宁', '伊宁市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654003, 24, 654000, '奎屯', '奎屯市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654021, 24, 654000, '伊宁县', '伊宁县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654022, 24, 654000, '察布查尔锡伯自治县', '察布查尔锡伯自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654023, 24, 654000, '霍城县', '霍城县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654024, 24, 654000, '巩留县', '巩留县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654025, 24, 654000, '新源县', '新源县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654026, 24, 654000, '昭苏县', '昭苏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654027, 24, 654000, '特克斯县', '特克斯县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654028, 24, 654000, '尼勒克县', '尼勒克县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654200, 23, 650000, '塔城地区', '塔城地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654201, 24, 654200, '塔城', '塔城市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654202, 24, 654200, '乌苏', '乌苏市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654221, 24, 654200, '额敏县', '额敏县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654223, 24, 654200, '沙湾县', '沙湾县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654224, 24, 654200, '托里县', '托里县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654225, 24, 654200, '裕民县', '裕民县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654226, 24, 654200, '和布克赛尔蒙古自治县', '和布克赛尔蒙古自治县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654300, 23, 650000, '阿勒泰地区', '阿勒泰地区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654301, 24, 654300, '阿勒泰', '阿勒泰市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654321, 24, 654300, '布尔津县', '布尔津县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654322, 24, 654300, '富蕴县', '富蕴县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654323, 24, 654300, '福海县', '福海县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654324, 24, 654300, '哈巴河县', '哈巴河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654325, 24, 654300, '青河县', '青河县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (654326, 24, 654300, '吉木乃县', '吉木乃县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (659000, 23, 650000, '自治区直辖县', '自治区直辖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (659001, 24, 659000, '石河子', '石河子市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (659002, 24, 659000, '阿拉尔', '阿拉尔市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (659003, 24, 659000, '图木舒克', '图木舒克市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (659004, 24, 659000, '五家渠', '五家渠市');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (920000, 22, NULL, '台湾', '台湾省');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (970000, 22, NULL, '香港', '香港特别行政区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (990000, 22, NULL, '澳门', '澳门特别行政区');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1000000, 22, NULL, '海外', '海外');

INSERT INTO tm_refer (refer_id, plat_code, refer_key, refer_name, refer_table, refer_criteria_sql, remark, order_column) VALUES (1, 'TAOBAO', 'status_id', 'status_value', 'tds_order_status', NULL, NULL, 'orderid');

#-----------------------------------------------------------------------------------------------------------------------
#  初始化元数据表定义
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (1, 'vw_taobao_customer', 	'客户信息', 'uni_id', 'taobao',	now(), now());
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (2, 'plt_taobao_order', 			'订单信息', 	'tid',			'taobao', now(), now());
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (3, 'plt_taobao_order_item', 	'子订单信息', 'oid',			'taobao', now(), now());
INSERT INTO tm_db_table (table_id, db_name, show_name, pk_column, plat_code, created, updated)  VALUES (4, 'plt_taobao_product',		'商品信息', 	'num_iid',		'taobao', now(), now());

#-----------------------------------------------------------------------------------------------------------------------
#  初始化元数据字段定义
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (1,    1, NULL, NULL, 'uni_id', 					'客户统一ID',		'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (2,    1, NULL, NULL, 'customerno',			'客户ID', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (3,    1, NULL, NULL, 'full_name', 			'姓名', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (4,    1, 		1, NULL, 'sex', 						'性别', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (5,    1, NULL, NULL, 'job', 						'职业', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (6,    1, NULL, NULL, 'age',							'年龄', 				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (7,    1, NULL, NULL, 'birthday', 				'生日', 				'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (8,    1, NULL, NULL, 'email', 					'邮箱', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (9,    1, NULL, NULL, 'mobile', 					'手机', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (10,   1, 	 22, NULL, 'state', 					'省份', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (11,   1,   23, NULL, 'city', 						'城市', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (12,   1, NULL, NULL, 'district', 				'区域', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (13,   1, NULL, NULL, 'address', 				'地址', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (14,   1, NULL, NULL, 'zip', 						'邮编', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (15,   1, 		3, NULL, 'is_mobile_valid',	'手机号是否有效', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (16,   1, 		3, NULL, 'is_email_valid',	'邮箱是否有效', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (17,   1,   41, NULL, 'vip_info', 				'客户全站等级', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (18,   1,   42, NULL, 'buyer_credit_lev','买家信用等级', 'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (19,   1, NULL, NULL, 'created', 				'淘宝用户注册时间','date', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (20,   1, NULL, NULL, 'buyer_good_ratio','买家好评率', 	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (21,   1, NULL, NULL, 'ymd',							'当天年月日', 	'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (22,   1, NULL, NULL, 'ym',							'当天年月', 		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (23,   1,   43, NULL, 'grade',						'会员等级', 		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (24,   1, NULL, NULL, 'label_name',			'客户标签', 		'string', 	0, now(), now(), null);


INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (51,   2, NULL, NULL, 'tid', 							'订单号',							'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (52,   2, NULL, NULL, 'dp_id',		  				'店铺ID',							'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (53,   2, NULL, NULL, 'customerno',				'客户ID', 						'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (54,   2, NULL, NULL, 'created', 					'交易创建时间',				'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (55,   2, NULL, NULL, 'endtime', 					'交易结束时间',				'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (56,   2, NULL, NULL, 'status',						'交易状态', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (57,   2, 44, NULL, 'trade_from',				'交易来源', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (58,   2, NULL, NULL, 'type', 							'交易类型', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (59,   2, NULL, NULL, 'pay_time', 					'付款时间', 					'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (60,   2, NULL, NULL, 'total_fee', 				'商品金额', 					'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (61,   2, NULL, NULL, 'post_fee', 					'邮费', 							'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (62,   2, NULL, NULL, 'consign_time',			'卖家发货时间', 			'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (63,   2, NULL, 1, 'ccms_order_status',	'CCMS交易状态', 			'string',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (64,   2, NULL, NULL, 'modified', 					'订单修改时间', 			'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (65,   2, NULL, NULL, 'alipay_no', 				'支付宝交易号', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (66,   2, NULL, NULL, 'payment', 					'实付金额', 					'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (67,   2, NULL, NULL, 'discount_fee',			'系统优惠金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (68,   2, NULL, NULL, 'point_fee', 				'买家使用积分',				'number', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (69,   2, NULL, NULL, 'real_point_fee',		'实际使用积分', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (70,   2, NULL, NULL, 'shipping_type',			'物流方式', 					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (71,   2, NULL, NULL, 'buyer_cod_fee',			'买家货到付款服务费',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (72,   2, NULL, NULL, 'seller_cod_fee',		'卖家货到付款服务费',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (73,   2, NULL, NULL, 'express_agency_fee','快递代收款', 				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (74,   2, NULL, NULL, 'adjust_fee', 				'手工调整金额', 			'number',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (75,   2, NULL, NULL, 'buyer_obtain_point_fee', 	'买家获得积分',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (76,   2, NULL, NULL, 'cod_fee', 					'货到付款服务费', 		'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (77,   2, NULL, NULL, 'cod_status', 				'货到付款物流状态',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (78,   2, NULL, NULL, 'buyer_alipay_no', 	'买家支付宝账号',			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (79,   2, NULL, NULL, 'receiver_name', 		'收货人的姓名', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (80,   2, NULL, NULL, 'receiver_state', 		'收货人的所在省份',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (81,   2, NULL, NULL, 'receiver_city', 		'收货人的所在城市',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (82,   2, NULL, NULL, 'receiver_district', '收货人的所在地区',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (83,   2, NULL, NULL, 'receiver_address', 	'收货人的详细地址',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (84,   2, NULL, NULL, 'receiver_zip',			'收货人的邮编', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (85,   2, NULL, NULL, 'receiver_mobile',		'收货人的手机号码',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (86,   2, NULL, NULL, 'receiver_phone',		'收货人的电话号码',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (87,   2, NULL, NULL, 'buyer_email', 			'买家邮件地址', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (88,   2, NULL, NULL, 'commission_fee',		'交易佣金', 					'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (89,   2, NULL, NULL, 'refund_fee', 				'子订单的退款金额合计','number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (90,   2, NULL, NULL, 'num', 							'商品数量总计', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (91,   2, NULL, NULL, 'received_payment',	'支付宝打款金额', 		'number', 	0, now(), now(), null);


INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (151,   3, NULL, NULL, 'oid', 							'子订单ID',				'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (152,   3, NULL, NULL, 'tid', 							'订单ID',					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (153,   3, NULL, NULL, 'dp_id',		  			'店铺ID',					'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (154,   3, NULL, NULL, 'customerno',				'客户ID', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (155,   3, NULL, NULL, 'total_fee', 				'应付金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (156,   3, NULL, NULL, 'discount_fee',			'订单优惠金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (157,   3, NULL, NULL, 'adjust_fee', 			'手工调整金额',			'number',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (158,   3, NULL, NULL, 'payment', 					'实付金额', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (159,   3, NULL, NULL, 'status',						'订单状态', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (160,   3, NULL, NULL, 'num', 							'购买数量', 			'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (161,   3, NULL, NULL, 'num_iid', 					'商品ID', 				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (162,   3, NULL, NULL, 'created', 					'交易创建时间',		'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (163,   3, NULL, NULL, 'endtime', 					'交易结束时间',		'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (164,   3, NULL, NULL, 'trade_from',				'交易来源', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (165,   3, NULL, NULL, 'type', 						'交易类型', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (166,   3, NULL, NULL, 'pay_time', 				'付款时间', 			'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (167,   3, NULL, NULL, 'consign_time',			'卖家发货时间', 	'date', 		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (168,   3, NULL, NULL, 'refund_status', 		'退款状态',				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (169,   3, NULL, NULL, 'refund_fee', 			'退款金额',				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (170,   3, NULL, NULL, 'ccms_order_status','CCMS交易状态', 	'number',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (171,   3, NULL, NULL, 'title', 						'商品标题', 			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (172,   3, NULL, NULL, 'modified', 				'订单修改时间', 	'date', 		0, now(), now(), null);


INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (241,   4, NULL, NULL, 'num_iid',						'商品数字ID',		'string', 	1, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (242,   4, NULL, NULL, 'detail_url', 				'商品URL',			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (243,   4, NULL, NULL, 'title',		  				'商品名称',			'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (244,   4, NULL, NULL, 'created',						'发布时间', 		'datetime', 0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (245,   4, NULL, NULL, 'is_fenxiao', 				'是否分销商品',	'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (246,   4, NULL, NULL, 'cid',								'商品叶子类目',	'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (247,   4, NULL, NULL, 'pic_url', 						'商品图片地址', 'string',		0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (248,   4, NULL, NULL, 'list_time', 					'商品上架时间',	'datetime',	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (249,   4, NULL, NULL, 'delist_time',				'商品下架时间',	'datetime', 0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (250,   4, NULL, NULL, 'price', 							'价格', 				'number', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (251,   4, NULL, NULL, 'modified', 					'修改时间', 		'datetime', 0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (252,   4, NULL, NULL, 'approve_status', 		'上传后状态',		'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (253,   4, NULL, NULL, 'dp_id', 							'店铺ID',				'string', 	0, now(), now(), null);
INSERT INTO tm_db_column(column_id, table_id, dic_id, refer_id, db_name, show_name, db_type, is_pk, created, updated, remark) VALUES (254,   4, NULL, NULL, 'outer_id',						'商家外部编码', 'string', 	0, now(), now(), null);

#-----------------------------------------------------------------------------------------------------------------------
#  初始化查询模版
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_query(query_id, code, show_name, plat_code) values(1, 	'CUSTOMER', 	'客户信息', 					'taobao');
INSERT INTO tm_query(query_id, code, show_name, plat_code) values(2, 	'ORDER', 			'订单总体消费查询',		'taobao');
INSERT INTO tm_query(query_id, code, show_name, plat_code) values(3, 	'ORDER_ITEM',	'订单商品消费查询',		'taobao');

INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(1, 1, 1, 1);
INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(2, 2, 2, 1);
INSERT INTO tm_query_table(query_table_id, query_id, table_id, is_master) values(3, 3, 3, 1);

INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (2,    1,		2,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (3,    1,		3,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (4,    1,		4,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (5,    1,		5,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (7,    1,		7,		'BIRTHDAY', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (8,    1,		8,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (9,    1,		9,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (10,   1,		10,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (11,   1,		11,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (13,   1,		13,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (14,   1,		14,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (17,   1,		17,		'DIC', 					NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (18,   1,		18,		'ORDERED_DIC', 	NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (20,   1,		20,		'NUMBER', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (23,   1,		23,		'ORDERED_DIC', 	NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (24,   1,		24,		'STRING', 			NULL,  NULL);

INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (51,    2,		51,			'STRING', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (54,    2,		54,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (55,    2,		55,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (57,    2,		57,			'DIC', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (58,    2,		58,			'STRING', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (59,    2,		59,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (62,    2,		62,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (63,    2,		63,			'REFER', 		NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (64,    2,		64,			'DATETIME', 	NULL,  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (111,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.BUY_FEE',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (112,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.BUY_NUM',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (113,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.BUY_FREQ',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (114,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.LAST_INTERVAL', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (115,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.FIRST_INTERVAL',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (116,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.LAST_BUY_TIME', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (117,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.FIRST_BUY_TIME',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (118,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.AVG_BUY_FEE',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (119,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.AVG_BUY_FREQ',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (120,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.REFUND_NUM',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (121,   2,		NULL,		'QUOTA', 			'com.yunat.ccms.metadata.quota.QuotaOrder.REFUND_FEE',  	NULL);

INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (153,    3,		153,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (161,    3,		161,		'TDS_PRODUCT', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (162,    3,		162,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (163,    3,		163,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (164,    3,		164,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (165,    3,		165,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (166,    3,		166,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (167,    3,		167,		'DATETIME', 		NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (168,    3,		168,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (169,    3,		169,		'NUMBER', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (170,    3,		170,		'NUMBER', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (243,    3,		243,		'STRING', 			NULL,  NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (191,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.BUY_FEE',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (192,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.BUY_NUM',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (193,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.BUY_FREQ',  		NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (194,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.LAST_INTERVAL', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (195,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.FIRST_INTERVAL',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (196,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.LAST_BUY_TIME', 	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (197,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.FIRST_BUY_TIME',	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (198,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.AVG_BUY_FEE',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (199,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.AVG_BUY_FREQ',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (200,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.REFUND_NUM',  	NULL);
INSERT INTO tm_query_criteria(query_criteria_id, query_id, column_id, query_type, quota_type, column_expr)  VALUES (201,    3,		NULL,		'QUOTA', 		'com.yunat.ccms.metadata.quota.QuotaOrderItem.REFUND_FEE',  	NULL);

#-----------------------------------------------------------------------------------------------------------------------
#  索引查询字段
#-----------------------------------------------------------------------------------------------------------------------
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (2,    1,		2,		'淘宝昵称', 	5);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (3,    1,		3,		NULL, 				1);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (4,    1,		4,		NULL, 				2);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (5,    1,		5,		NULL, 				15);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (7,    1,		7,		NULL, 				3);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (8,    1,		8,		NULL, 				7);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (9,    1,		9,		NULL, 				6);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (10,   1,		10,		NULL, 				4);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (11,   1,		11,		NULL, 				4);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (13,   1,		13,		NULL, 				13);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (14,   1,		14,		NULL, 				14);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (17,   1,		17,		NULL, 				12);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (18,   1,		18,		NULL, 				10);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (20,   1,		20,		NULL, 				11);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (23,   1,		23,		NULL, 				8);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (24,   1,		24,		NULL, 				9);

-- #metadata end.

--  初始流程配置数据
insert into twf_workflow(workflow_id, create_time, update_time) values(1, now(), now());
insert into twf_workflow(workflow_id, create_time, update_time) values(2, now(), now());
insert into twf_workflow(workflow_id, create_time, update_time) values(3, now(), now());

--  基础版的模板配置数据
insert into tb_template(template_id, template_name, created_time, updated_time, template_desc,
	comments, workflow_id, plat_code, disabled, edition, pic_url) values (1, '短信营销模板', now(), now(),
	'', '', 1, 'taobao', 0, 'BASIC_L3', '../images/compType-dx.png');

insert into tb_template(template_id, template_name, created_time, updated_time, template_desc,
	comments, workflow_id, plat_code, disabled, edition, pic_url) values (2, '邮件营销模板', now(), now(),
	'', '', 2, 'taobao', 1, 'BASIC_L3', '../images/compType-yj.png');

insert into tb_template(template_id, template_name, created_time, updated_time, template_desc,
	comments, workflow_id, plat_code, disabled, edition, pic_url) values (3, '优惠券营销模板', now(), now(),
	'', '', 3, 'taobao', 0, 'BASIC_L3', '../images/compType-yhq.png');

-- 短信营销模板的节点与连接线配置数据
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(1, '开始设计', 'tflowstart;image=../images/graph/icon/lc_begin.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tflowstart', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(2, '营销时间', 'tflowtime;image=../images/graph/icon/lc_time.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tflowtime', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(3, '客户筛选', 'tfilterfind;image=../images/graph/icon/lc_lsearch.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tfilterfind', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(4, '目标客户', 'tcustomertargetgroup;image=../images/graph/icon/kh_clients.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcustomertargetgroup', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(5, '短信发送', 'tcommunicateSMS;image=../images/graph/icon/mb_note.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(6, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(1, '1', 1, 1, 2, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(2, '1', 1, 2, 3, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(3, '1', 1, 3, 4, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(4, '1', 1, 4, 5, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(5, '1', 1, 5, 6, '1', 'geometry');

--  邮件营销模板的节点与连接线配置
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(11, '开始设计', 'tflowstart;image=../images/graph/icon/lc_begin.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tflowstart', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(12, '营销时间', 'tflowtime;image=../images/graph/icon/lc_time.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tflowtime', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(13, '客户筛选', 'tfilterfind;image=../images/graph/icon/lc_lsearch.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tfilterfind', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(14, '目标客户', 'tcustomertargetgroup;image=../images/graph/icon/kh_clients.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcustomertargetgroup', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(15, '邮件发送', 'tcommunicateEDM;image=../images/graph/icon/mb_mail.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcommunicateEDM', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(16, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(11, '1', 2, 11, 12, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(12, '1', 2, 12, 13, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(13, '1', 2, 13, 14, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(14, '1', 2, 14, 15, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(15, '1', 2, 15, 16, '1', 'geometry');

--  优惠券营销模板的节点与连接线配置
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(21, '开始设计', 'tflowstart;image=../images/graph/icon/lc_begin.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tflowstart', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(22, '营销时间', 'tflowtime;image=../images/graph/icon/lc_time.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tflowtime', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(23, '客户筛选', 'tfilterfind;image=../images/graph/icon/lc_lsearch.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tfilterfind', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(24, '目标客户', 'tcustomertargetgroup;image=../images/graph/icon/kh_clients.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomertargetgroup', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(25, '优惠券发送', 'tcommunicateUMP;image=../images/graph/icon/mb_youhui.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateUMP', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(26, '短信发送', 'tcommunicateSMS;image=../images/graph/icon/mb_note.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(27, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(21, '1', 3, 21, 22, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(22, '1', 3, 22, 23, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(23, '1', 3, 23, 24, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(24, '1', 3, 24, 25, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(25, '1', 3, 25, 26, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	values(26, '1', 3, 26, 27, '1', 'geometry');

--  end 配置模板数据

#订单状态表
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (10, '已下单未付款','已下单未付款',1);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (20, '有效交易','有效交易', 2);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (21, '&nbsp;&nbsp;&nbsp;&nbsp;|---已付款未发货','已付款未发货', 3);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (22, '&nbsp;&nbsp;&nbsp;&nbsp;|---已发货待确认','已发货待确认', 4);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (23, '&nbsp;&nbsp;&nbsp;&nbsp;|---交易成功','交易成功', 5);
INSERT INTO tds_order_status (status_id, status_value,status_name, orderid) VALUES (30, '交易失败  ','交易失败', 6);


#初始化应用程序配置信息
#(按照“开发环境”参数进行配置，发布到“测试”或者“生产环境”时需要重新配置)
INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('CCMS', 'ccms_version', '4.0.0', 'ccms版本号'),
('CCMS', 'ccms_tenant_id', '0_taobao_100571094', 'ccms租户Id'),
('CCMS', 'ccms_tenant_password', '51d531kxsyofum3g6iq22sbx8mebvaquhnr5', 'ccms租户密钥'),
('CCMS', 'ccms_upload_dir', 'd:/upload', 'CCMS文件上传地址'),
('CCMS', 'ccms_node_retry_times', '3', '节点最多重试次数');


INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('CHANNEL', 'channel_client_name', 'ccms', 'ccms调用渠道用户中心的用户名'),
('CHANNEL', 'channel_client_password', 'rt6n3uani40yjgwpnlfosmdxoqt7pc0fn13p', 'ccms调用渠道用户中心的密码'),
('CHANNEL', 'channel_client_app_id', '0', 'ccms调用渠道的应用id'),
('CHANNEL', 'channel_client_send_batch_size', '20000', 'CCMS调用渠道分页发送每页数量');


INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('CHANNEL', 'channel_service_query_url', 'http://10.200.187.70:18080/channel-info/JsonServlet', '渠道查询服务接口地址 '),
('CHANNEL', 'channel_service_command_url', 'http://10.200.187.70:18081/channel-service/JsonServlet', '渠道命令服务接口地址'),
('CHANNEL', 'channel_report_send_result_url', 'http://10.200.187.70:18091/yunat-report/report?', '渠道发送报告链接地址'),
('CHANNEL', 'channel_report_announcement_url', 'http://10.200.187.70:18091/yunat-report/bulletin', '公告链接地址');

INSERT INTO tb_app_properties(prop_group, prop_name ,prop_value, prop_desc) VALUES
('CHANNEL', 'channel_shop_diagnosis_url', 'http://udp-test.ccms.fenxibao.com/udp-web/ccms?userName={0}&sign={1}', '店铺诊断');

INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('UCENTER', 'ucenter_service_rest_url', 'http://apptest.fenxibao.com/ucenter-restful-impl', '订购中心REST接口地址 ');


INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('TOP', 'top_service_rest_url', 'http://gw.api.taobao.com/router/rest', '淘宝开放平台rest地址'),
('TOP', 'top_ccms_appkey', '12283535', 'CCMS在淘宝开放平台的Appkey'),
('TOP', 'top_ccms_appsecret', '4283e0d7a760229ab34bca67cc87fcf2', 'CCMS在淘宝开放平台的Appsecret'),
('TOP', 'top_ccms_grant_url', 'http://container.api.taobao.com/container?appkey=12283535&scope=promotion,item,usergrade', '淘宝开放平台授权地址');


INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('MQ', 'mq_exchange_name', 'USER_NOTICE_MQ_EXCHANGE_TEST1', NULL),
('MQ', 'mq_host', '10.200.187.73', NULL),
('MQ', 'mq_password', '$datawinner$', NULL),
('MQ', 'mq_port', '5672', NULL),
('MQ', 'mq_username', 'datawinner', NULL),
('MQ', 'mq_virtualHost', 'datawinner', NULL);


-- 模块
INSERT INTO module_type
(id, key_name, name, name_plus, url, data_url, tip, lowest_edition_required, support_ops_mask,memo)
VALUES
	(1,'','',NULL,NULL,NULL,NULL,0,0,'containerOfTheModulesInEachPage:所有页面都有的模块的父模块的类型'),
	(2,'index','',NULL,NULL,NULL,NULL,0,17,'首页(页面)'),
	(3,'shopHealth','',NULL,NULL,NULL,NULL,0,17,'店铺诊断(页面)'),
	(4,'shopMonitor','',NULL,NULL,NULL,NULL,0,17,'店铺监控(页面)'),
	(5,'ordersCenter','',NULL,NULL,NULL,NULL,0,17,'订单中心(页面)'),
	(6,'marketing','',NULL,NULL,NULL,NULL,0,17,'营销活动(页面)'),
	(7,'coupon','',NULL,NULL,NULL,NULL,0,17,'促销管理(页面)'),
	(8,'customer','',NULL,NULL,NULL,NULL,0,17,'客户管理(页面)'),
	(9,'admin','',NULL,NULL,NULL,NULL,0,17,'系统管理(页面)'),
	(10,'nav','',NULL,NULL,'',NULL,0,17,'导航栏'),
	(11,'index_link','首页','导航栏上的链接','#/dashboard','',NULL,0,17,NULL),
	(12,'shopHealth_link','店铺诊断','导航栏上的链接','#/shop_diagnosis','',NULL,0,17,NULL),
	(13,'shopMonitor_link','店铺监控','导航栏上的链接','#/shop_monitor','',NULL,0,17,NULL),
	(14,'ordersCenter_link','订单中心','导航栏上的链接','#/order_center','',NULL,0,17,NULL),
	(15,'marketing_link','营销活动','导航栏上的链接','#/marketing/campaign.list','',NULL,0,17,NULL),
	(16,'coupon_link','促销管理','导航栏上的链接','#/coupon/tickets','',NULL,0,17,NULL),
	(17,'customer_link','客户管理','导航栏上的链接','#/customer/blacklist','',NULL,0,17,NULL),
	(18,'admin_link','系统管理','导航栏上的链接','#/admin/taobaouser','',NULL,0,17,NULL),
	(19,'ordersMonitor','订单监控',NULL,NULL,NULL,NULL,0,17,NULL),
	(20,'logisticsMonitor','异常物流监控',NULL,NULL,NULL,NULL,0,17,NULL),
	(21,'scheduledDeptCollect','定时催付',NULL,NULL,NULL,NULL,0,17,NULL),
	(22,'realTimeDeptCollect','实时催付',NULL,NULL,NULL,NULL,0,17,NULL),
	(23,'deliveryNotice','发货通知',NULL,NULL,NULL,NULL,0,17,NULL),
	(24,'sameCityNotice','同城通知',NULL,NULL,NULL,NULL,0,17,NULL),
	(25,'signNotice','签收通知',NULL,NULL,NULL,NULL,0,17,NULL),
	(26,'activityList','营销活动',NULL,NULL,NULL,NULL,0,17,NULL),
	(27,'couponList','优惠券',NULL,NULL,NULL,NULL,0,17,NULL),
	(28,'blacklistManage','黑名单管理',NULL,NULL,NULL,NULL,0,17,NULL),
	(29,'smsBlacklist','短信黑名单',NULL,NULL,NULL,NULL,0,17,NULL),
	(30,'wangwangSubuser','旺旺子号',NULL,NULL,NULL,NULL,0,17,NULL)
;

insert into module
(id, module_type_id, container_module_id,ranking,
key_name, name, url, data_url,
tip, lowest_edition_required, support_ops_mask, memo)
values
	(1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'所有页面都有的模块的父模块'),
	(2,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(3,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(4,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(5,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(6,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(7,7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(8,8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(9,9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(10,10,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(12,12,10,200,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(13,13,10,300,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(14,14,10,400,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(15,15,10,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(16,16,10,600,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(17,17,10,700,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(18,18,10,800,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(19,19,4,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(20,20,4,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(21,21,5,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(22,22,5,600,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(23,23,5,700,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(24,24,5,800,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(25,25,5,900,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(26,26,6,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(27,27,7,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(28,28,8,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(29,29,28,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(30,30,9,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)
;

insert into module_entry(id,module_id,permission_id,role_id,user_id,support_ops_mask,memo)
values
	(1,NULL,NULL,NULL,NULL,0,'最根的授权,包括permission为null者(匿名访问)'),
	(2,NULL,0,NULL,NULL,31,'permission不为null者对所有模块的权限的默认值'),
	(3,18,0,NULL,NULL,0,'permission不为null者对模块18(系统管理)的权限为0'),
	(4,18,8,NULL,NULL,17,'permission8对模块18(系统管理)17.本条覆盖上一条')
;


-- 用户,角色,权限
insert into tb_sysuser (id,disabled, email, login_name, mobile, password, real_name, user_type)
values (1,false,null,'tomwalk',null,'','tomwalk','taobao');

insert into tb_sys_taobao_user (id,is_subuser, plat_shop_id, plat_user_id, plat_user_name)
values (1,false,'taobao_100571094','12334','tomwalk');

INSERT INTO tds_permission(id,name,memo,permission_key)
VALUES
	(1,'首页',NULL,NULL),
	(2,'店铺诊断',NULL,NULL),
	(3,'店铺监控',NULL,NULL),
	(4,'订单中心',NULL,NULL),
	(5,'营销活动',NULL,NULL),
	(6,'促销管理',NULL,NULL),
	(7,'客户管理',NULL,NULL),
	(8,'系统管理',NULL,NULL)
;

insert into tb_role(id,name,memo)values(100000,'管理员',null),(100001,'普通用户',null);
insert into tb_user_role(user_id,role_id)values(1,100000);
insert into tb_role_permission(role_id,permission_id)
values
	(100000,1),(100000,2),(100000,3),(100000,4),(100000,5),(100000,6),(100000,7),(100000,8),
	(100001,1),(100001,2),(100001,3),(100001,4),(100001,5),(100001,6),(100001,7)
;


INSERT INTO plt_taobao_coupon_denomination VALUES (1, '3元', 3);
INSERT INTO plt_taobao_coupon_denomination VALUES (2, '5元', 5);
INSERT INTO plt_taobao_coupon_denomination VALUES (3, '10元', 10);
INSERT INTO plt_taobao_coupon_denomination VALUES (4, '20元', 20);
INSERT INTO plt_taobao_coupon_denomination VALUES (5, '50元', 50);
INSERT INTO plt_taobao_coupon_denomination VALUES (6, '100元', 100);

-- 新的测试店铺!!!!!!
insert into plt_taobao_shop(shop_id,shop_name)values('100571094','大狗子19890202');
