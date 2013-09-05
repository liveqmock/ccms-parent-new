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
role_id  bigint(20) NOT NULL AUTO_INCREMENT ,
role_name  varchar(50) ,
memo  varchar(200) ,
PRIMARY KEY (role_id)
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
usertype  varchar(10)  NOT NULL DEFAULT 'build-in' COMMENT '用户类型' ,
username  varchar(20) NOT NULL COMMENT '用户登录名称' ,
password  varchar(100) NOT NULL COMMENT '用户登录密码' ,
name  varchar(50) COMMENT '用户真实姓名' ,
mobile  varchar(20) COMMENT '用户手机号码' ,
email  varchar(100) ,
disabled  varchar(10) ,
PRIMARY KEY (id) ,
UNIQUE INDEX uk_tb_sysuser_username_usertype (username, usertype)
) AUTO_INCREMENT = 100000
COMMENT = '系统用户表';


CREATE TABLE tb_user_role (
user_id  bigint(20) NOT NULL ,
role_id  bigint(20) NOT NULL ,
PRIMARY KEY (user_id, role_id)
);


CREATE TABLE tds_permission (
permission_id  bigint(20) NOT NULL ,
permission_name  varchar(50) ,
memo  varchar(200) ,
permission_key  varchar(50) ,
PRIMARY KEY (permission_id)
);


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
CREATE TABLE tb_app_expired (
associate_username  varchar(20) NOT NULL COMMENT 'rmi用户名' ,
expire_date  date COMMENT '淘宝应用订购过期时间' ,
PRIMARY KEY (associate_username)
)
COMMENT = '应用订购期';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
CREATE TABLE tb_app_properties (
prop_id bigint(20) NOT NULL,
prop_group varchar(20) NOT NULL COMMENT '参数所属组别,取值：FTP、MQ、CHANNEL、CCMS',
prop_name varchar(50) NOT NULL COMMENT '配置参数名',
prop_value varchar(200) DEFAULT NULL COMMENT '配置值',
prop_desc varchar(255) DEFAULT NULL COMMENT '描述',
PRIMARY KEY (prop_id)
)
COMMENT='应用级属性参数配置表';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
CREATE TABLE tb_navigate_function (
id  decimal(12,0) NOT NULL COMMENT '编号' ,
parent_id  decimal(12,0) COMMENT '父功能编号' ,
show_name  varchar(50) COMMENT '功能名称' ,
link_url  varchar(200) COMMENT '对应的路径(页面/请求)' ,
sort_no  int(11) COMMENT '排序号' ,
disabled  tinyint(1) COMMENT '是否有效' ,
create_at  datetime NOT NULL COMMENT '创建时间' ,
update_at  datetime COMMENT '修改时间',
PRIMARY KEY (id),
CONSTRAINT fk_tb_function_parent_id FOREIGN KEY (parent_id) REFERENCES tb_navigate_function (id) ON DELETE NO ACTION ON UPDATE NO ACTION
)
COMMENT = '系统功能表';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
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


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
CREATE TABLE tb_campaign (
camp_id  bigint(20) NOT NULL AUTO_INCREMENT ,
prog_id  bigint(20) ,
camp_name  varchar(512) COMMENT '活动的名称' ,
camp_type  int(11) COMMENT '客户自定义活动类别，对应tdu_camp_type主键' ,
camp_status  varchar(15) NOT NULL COMMENT '活动的状态,对应的字典表td_camp_status' ,
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


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
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
disabled  tinyint(1)  NULL  COMMENT '模版状态(1:生效、0:失效)' ,
PRIMARY KEY (template_id)
)
COMMENT = '活动模板表';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
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
PRIMARY KEY (prog_id)
)
COMMENT = '沟通项目表';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
CREATE TABLE tdu_prog_type (
prog_type_id  bigint(20) NOT NULL AUTO_INCREMENT ,
prog_type  varchar(15) COMMENT '项目类型名' ,
disabled  tinyint(1) ,
PRIMARY KEY (prog_type_id)
)
COMMENT = '项目类型字典表';


CREATE TABLE tds_camp_status (
status_id  varchar(20) NOT NULL COMMENT '活动状态ID （定义 待补充）' ,
status_value  varchar(20) NOT NULL COMMENT '活动状态显示值' ,
orderid  decimal(2,0) COMMENT '顺序' ,
PRIMARY KEY (status_id)
)
COMMENT = '活动状态表';


CREATE TABLE tdu_camp_type (
camp_type_id  bigint(20) NOT NULL ,
camp_type  varchar(15) COMMENT '活动类型名' ,
disabled  tinyint(1) ,
PRIMARY KEY (camp_type_id)
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


CREATE TABLE twf_log_job (
job_id  bigint(20) NOT NULL AUTO_INCREMENT ,
camp_id  bigint(20) ,
status  decimal(3,0) NOT NULL COMMENT '状态，见表td_job_status' ,
starttime  datetime COMMENT '开始时间' ,
endtime  datetime COMMENT '结束时间' ,
run_optr_id  bigint(20) ,
is_preexecute  tinyint(1) ,
plantime  datetime ,
last_job  tinyint(1) ,
version  varchar(20) COMMENT '用来区别新旧版本流程引擎创建的job' ,
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
memo  varchar(1024) COMMENT '备注' ,
output_count  int(11) ,
is_preexecute  tinyint(1) ,
version  varchar(20) COMMENT '用来区别新旧版本流程引擎创建的subjob' ,
PRIMARY KEY (subjob_id),
UNIQUE INDEX uk_twf_log_subjob_job_id_node_id (job_id, node_id)
)
COMMENT = '流程执行子任务表';



###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
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
COMMENT = '保存流程的节点信息';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
CREATE TABLE twf_workflow (
workflow_id  bigint(20) NOT NULL AUTO_INCREMENT ,
create_time  datetime NOT NULL COMMENT '创建时间' ,
update_time  datetime COMMENT '更新时间' ,
PRIMARY KEY (workflow_id)
)
COMMENT = '流程信息表';


###-------------------------------
### modify by 2013-03-08 tao.yang
###-------------------------------
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
COMMENT = '流程的连接信息表';


###-------------------------------
### modify by 2013-03-09 tao.yang
###-------------------------------
CREATE TABLE dashboard_module (
dashboard_module_id  bigint(20) NOT NULL AUTO_INCREMENT ,
dashboard_module_name  varchar(100) NOT NULL ,
dashboard_module_url  varchar(200) NOT NULL ,
dashboard_module_createtime  time DEFAULT NULL ,
dashboard_default_module  tinyint(4) NOT NULL COMMENT '是否为默认加载模块' ,
PRIMARY KEY (dashboard_module_id)
)
COMMENT='dashboard模块';


###-------------------------------
### modify by 2013-03-09 tao.yang
###-------------------------------
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
realtime_begin_date  datetime COMMENT '非周期性的开始日期' ,
realtime_begin_time  varchar(10) COMMENT '非周期性的开始时间' ,
cycle_begin_date  datetime COMMENT '周期性的开始日期' ,
cycle_end_date  datetime COMMENT '周期性的结束日期' ,
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


CREATE TABLE tm_db_column (
column_id  bigint(20) NOT NULL AUTO_INCREMENT ,
dir_id  bigint(20) ,
table_id  bigint(20) COMMENT '字段所属元数据表ID，关联tm_db_table.table_id' ,
dbname  varchar(50) COMMENT '字段数据库名' ,
viewname  varchar(100) COMMENT '字段显示名称' ,
memo  varchar(200) COMMENT '备注' ,
db_type  varchar(15) COMMENT '字段拼SQL时的数据类型,不同类型拼sql时的写法不一样,不是数据库原生类型,仅包括number,string,date,datetime' ,
view_type_id  bigint(20) ,
query_type  varchar(15) COMMENT '字段在前端查询页面中显示的类别。dic: 按数据字典选择、date: 日期选择、datetime: 日期时间选择、yearmonth: 年月份选择、input: 输入框' ,
limit_expr  varchar(100) COMMENT '字段查询格式限制条件,查询条件为输入框时使用' ,
order_id  decimal(3,0) COMMENT '字段显示顺序，从1开始' ,
can_query  tinyint(1) ,
static_info  varchar(1024) COMMENT '存储各度量分析类型字段的统计信息' ,
measure_type  varchar(15) COMMENT '度量分析类型。 cata：分类型(维度) ，val：连续型(指标)，desc：描述性字段' ,
is_pk  tinyint(1) ,
import_id  bigint(20) ,
ref_table  varchar(64) COMMENT '元数据字段外键关联的维表表名，仅当query_type为“ref”时，必须给改字段以及相关字段（ref_*）赋值' ,
ref_key  varchar(64) COMMENT '外键关联的维表主键（键值）字段名' ,
ref_value  varchar(64) COMMENT '外键关联的维表主键对应的显示值字段名' ,
ref_limit_sql  varchar(128) COMMENT '取外键关联表数据时SQL中的条件子句，条件必须以“Where”或者“Order by”开始' ,
PRIMARY KEY (column_id)
) AUTO_INCREMENT = 100000
COMMENT = '字段元数据';


CREATE TABLE tm_db_table (
table_id  bigint(20) NOT NULL AUTO_INCREMENT ,
dbname  varchar(50) NOT NULL COMMENT '表数据库名' ,
viewname  varchar(100) NOT NULL COMMENT '表名称' ,
pk_col  varchar(50) COMMENT '表主键字段名' ,
memo  varchar(200) COMMENT '表描述' ,
import_id  bigint(20) ,
dir_type  smallint(6) COMMENT '只有导入表才需填充本字段' ,
PRIMARY KEY (table_id)
) AUTO_INCREMENT = 100000
COMMENT = '表元数据';


CREATE TABLE tm_dic_type (
type_id  bigint(20) NOT NULL AUTO_INCREMENT ,
viewname  varchar(50) COMMENT '名称' ,
memo  varchar(200) COMMENT '描述' ,
import_id  bigint(20) ,
can_compare  tinyint(1) ,
PRIMARY KEY (type_id)
) AUTO_INCREMENT = 100000
COMMENT = '元数据定义的字段使用的维表类型';


CREATE TABLE tm_dic_type_value (
type_value_id  bigint(20) NOT NULL AUTO_INCREMENT ,
type_id  bigint(20) NOT NULL COMMENT '字典表ID' ,
parent_type_value_id  bigint(20) COMMENT '父字典类型值，逻辑关联本表的type_value_id字段' ,
type_value  varchar(32) NOT NULL COMMENT '字典表数据项内部数值（Key）' ,
viewname  varchar(64) COMMENT '字典表数据项显示值（Value）' ,
memo  varchar(200) COMMENT '说明' ,
import_id  bigint(20) ,
PRIMARY KEY (type_value_id)
) AUTO_INCREMENT 10000000
COMMENT = '字典表数据项各个数值';


CREATE TABLE tm_tdu_table_list (
id  int(11) NOT NULL  ,
table_name  varchar(100) COMMENT '数据库表名' ,
table_desc  varchar(100) COMMENT '描述' ,
key_column  varchar(100) COMMENT 'key字段名' ,
value_column  varchar(100) COMMENT 'value字段名' ,
PRIMARY KEY (id)
)
COMMENT = '用户维表元数据表';


CREATE TABLE tm_template_column (
serial_id  bigint(20) NOT NULL AUTO_INCREMENT ,
template_table_id  bigint(20) ,
dbname  varchar(30) ,
viewname  varchar(50) ,
memo  varchar(200) ,
db_type  varchar(15) ,
view_type_id  bigint(20) ,
query_type  varchar(15) ,
limit_expr  varchar(100) ,
order_id  decimal(3,0) ,
can_query  tinyint(1) ,
static_info  varchar(1024) NULL ,
measure_type  varchar(15) NOT NULL ,
is_pk  tinyint(1) ,
is_dic  tinyint(1) ,
PRIMARY KEY (serial_id)
) AUTO_INCREMENT 1000
COMMENT = '导入表字段元数据模板，各字段注释同tm_db_table';


CREATE TABLE tm_template_table (
serial_id  bigint(20) NOT NULL AUTO_INCREMENT ,
dbname  varchar(30) NOT NULL ,
viewname  varchar(50) NOT NULL ,
memo  varchar(200) ,
trad_version  varchar(20) ,
pk_column  varchar(50) ,
PRIMARY KEY (serial_id)
) AUTO_INCREMENT 100
COMMENT = '导入表元数据模板，各字段注释同tm_db_table';


CREATE TABLE tm_view_dir (
dir_id  bigint(20) NOT NULL AUTO_INCREMENT ,
dir_type  decimal(2,0) NOT NULL COMMENT '目录类别，由com.huaat.ccms.common.cons.DirTypeCons定义。1：客户属性目录、5：当前时间目录、21：RFM目录、31：自定义标签目录、41：会员等级目录、51：订单属性目录、61：时间变量目录、71：店铺购买指标目录、81：店铺邮件订阅目录、91：店铺客户标签、99：导入字段对应的目录' ,
viewname  varchar(50) NOT NULL COMMENT '目录名称' ,
parent_id  bigint(20) COMMENT '父目录ID' ,
order_id  decimal(3,0) COMMENT '顺序ID' ,
import_id  bigint(20) ,
memo  varchar(200) COMMENT '目录说明' ,
PRIMARY KEY (dir_id)
) AUTO_INCREMENT = 100000
COMMENT = '前台显示目录元数据';


CREATE TABLE twf_qrtz_timer_blob_triggers (
trigger_name  varchar(200) ,
trigger_group  varchar(200) ,
blob_data  blob NULL
);


CREATE TABLE twf_qrtz_timer_calendars (
calendar_name  varchar(200) ,
calendar  blob NULL
);


CREATE TABLE twf_qrtz_timer_cron_triggers (
trigger_name  varchar(200) ,
trigger_group  varchar(200) ,
cron_expression  varchar(120) ,
time_zone_id  varchar(80) NULL
);


CREATE TABLE twf_qrtz_timer_fired_triggers (
entry_id  varchar(95) ,
trigger_name  varchar(200) ,
trigger_group  varchar(200) ,
is_volatile  tinyint(1) ,
instance_name  varchar(200) ,
fired_time  bigint(20) ,
priority  int(11) ,
state  varchar(16) ,
job_name  varchar(200) ,
job_group  varchar(200) ,
is_stateful  tinyint(1) ,
requests_recovery  tinyint(1) NULL
);


CREATE TABLE twf_qrtz_timer_job_details (
job_name  varchar(200) ,
job_group  varchar(200) ,
description  varchar(250) ,
job_class_name  varchar(250) ,
is_durable  tinyint(1) ,
is_volatile  tinyint(1) ,
is_stateful  tinyint(1) ,
requests_recovery  tinyint(1) ,
job_data  blob NULL
);


CREATE TABLE twf_qrtz_timer_job_listeners (
job_name  varchar(200) ,
job_group  varchar(200) ,
job_listener  varchar(200) NULL
);


CREATE TABLE twf_qrtz_timer_locks (
lock_name  varchar(40) NULL
);


CREATE TABLE twf_qrtz_timer_paused_trigger_grps (
trigger_group  varchar(200) NULL
);


CREATE TABLE twf_qrtz_timer_scheduler_state (
instance_name  varchar(200) ,
last_checkin_time  bigint(20) ,
checkin_interval  bigint(20) NULL
);


CREATE TABLE twf_qrtz_timer_simple_triggers (
trigger_name  varchar(200) ,
trigger_group  varchar(200) ,
repeat_count  bigint(20) ,
repeat_interval  bigint(20) ,
times_triggered  bigint(20) NULL
);


CREATE TABLE twf_qrtz_timer_triggers (
trigger_name  varchar(200) ,
trigger_group  varchar(200) ,
job_name  varchar(200) ,
job_group  varchar(200) ,
is_volatile  tinyint(1) ,
description  varchar(250) ,
next_fire_time  bigint(20) ,
prev_fire_time  bigint(20) ,
priority  int(11) ,
trigger_state  varchar(16) ,
trigger_type  varchar(8) ,
start_time  bigint(20) ,
end_time  bigint(20) ,
calendar_name  varchar(200) ,
misfire_instr  smallint(6) ,
job_data  blob NULL
);


CREATE TABLE twf_qrtz_timer_trigger_listeners (
trigger_name  varchar(200) ,
trigger_group  varchar(200) ,
trigger_listener  varchar(200) NULL
);


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


SET FOREIGN_KEY_CHECKS=1;

