--// create meta and querynode tables
-- Migration SQL that makes the change goes here.

/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2013/3/26 16:23:33                           */
/*==============================================================*/

drop table if exists tm_catalog;

drop table if exists tm_catalog_criteria;

drop table if exists tm_db_column;

drop table if exists tm_db_table;

drop table if exists tm_dic;

drop table if exists tm_dic_value;

drop table if exists tm_query;

drop table if exists tm_query_column;

drop table if exists tm_query_criteria;

drop table if exists tm_query_join_criteria;

drop table if exists tm_query_selecton;

drop table if exists tm_query_table;

drop table if exists tm_refer;

drop table if exists twf_node_query;

drop table if exists twf_node_query_criteria;

drop table if exists twf_node_query_defined;

/*==============================================================*/
/* Table: tm_catalog                                            */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_catalog_criteria                                   */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_db_column                                          */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_db_table                                           */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_dic                                                */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_dic_value                                          */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_query                                              */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_query_column                                       */
/*==============================================================*/
create table tm_query_column
(
   query_column_id      bigint not null auto_increment,
   column_id            bigint(20),
   query_selecton_id    bigint,
   query_table_id       bigint,
   column_expr          varchar(100) comment '显示表达式或者解析表达式',
   column_name          varchar(100) not null comment '显示名称或者匹配名称',
   column_order         decimal(3,0) comment '显示顺序',
   primary key (query_column_id)
)
auto_increment = 100001;

alter table tm_query_column comment '查询模版的字段';

/*==============================================================*/
/* Table: tm_query_criteria                                     */
/*==============================================================*/
create table tm_query_criteria
(
   query_criteria_id    bigint not null auto_increment,
   query_id             bigint comment '查询模版ID',
   column_id            bigint(20) comment '字段ID',
   query_type           varchar(32) comment '查询类型',
   quota_type           varchar(32) comment '指标类型',
   column_expr          varchar(100) comment '属性扩展表达式',
   primary key (query_criteria_id)
)
auto_increment = 100001;

alter table tm_query_criteria comment '查询属性';

/*==============================================================*/
/* Table: tm_query_join_criteria                                */
/*==============================================================*/
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

/*==============================================================*/
/* Table: tm_query_selecton                                     */
/*==============================================================*/
create table tm_query_selecton
(
   query_selecton_id    bigint not null auto_increment,
   query_id             bigint comment '查询模版ID',
   purpose_type         varchar(32) not null comment '用途：分运行，展示，导入和导出',
   primary key (query_selecton_id)
);

alter table tm_query_selecton comment '查询模版的SELECT部分，分几种用途，分别用于节点运行，展示和导入导出
默认只有节点展示的配置。节点运行用';

/*==============================================================*/
/* Table: tm_query_table                                        */
/*==============================================================*/
create table tm_query_table
(
   query_table_id       bigint not null auto_increment,
   query_id             bigint comment '查询模版ID',
   table_id             bigint comment '表定义ID',
   primary key (query_table_id)
);

alter table tm_query_table comment '查询模版包含的查询表';

/*==============================================================*/
/* Table: tm_refer                                              */
/*==============================================================*/
create table tm_refer
(
   refer_id             bigint not null auto_increment,
   plat_code            varchar(32) comment '平台代码',
   refer_key            varchar(64) character set utf8 not null comment '字典值',
   refer_name           varchar(64) character set utf8 not null comment '字典名',
   refer_table          varchar(64) character set utf8 not null comment '来源表',
   refer_criteria_sql   varchar(128) character set utf8 comment '过滤条件',
   remark               varchar(200) comment '备注',
   primary key (refer_id)
)
auto_increment = 10000001;

alter table tm_refer comment '关联类型的字典';

/*==============================================================*/
/* Table: twf_node_query                                        */
/*==============================================================*/
create table twf_node_query
(
   node_id              bigint(20) not null comment '需要指定，不自增',
   is_exclude           boolean comment '是否排除',
   time_type            tinyint(1) comment '时间类型',
   plat_code            varchar(32) comment '平台',
   primary key (node_id)
);

alter table twf_node_query comment '查询节点';

/*==============================================================*/
/* Table: twf_node_query_criteria                               */
/*==============================================================*/
create table twf_node_query_criteria
(
   node_criteria_id     bigint not null auto_increment,
   query_criteria_id    bigint not null comment '条件模版ID',
   query_defined_id     bigint comment '已定义查询ID',
   operator             varchar(32) not null comment '操作符',
   target_type          varchar(32) not null comment '目标类型',
   target_value         varchar(200) not null comment '目标值',
   primary key (node_criteria_id)
)
auto_increment = 100001;

alter table twf_node_query_criteria comment '已定义的查询条件（来自于用户界面）';

/*==============================================================*/
/* Table: twf_node_query_defined                                */
/*==============================================================*/
create table twf_node_query_defined
(
   query_defined_id     bigint not null auto_increment,
   node_id              bigint comment '节点',
   query_id             bigint comment '查询模版',
   primary key (query_defined_id)
)
auto_increment = 100001;

alter table twf_node_query_defined comment '已定义查询（保存界面定义的查询）';


--//@UNDO
-- SQL to undo the change goes here.

drop table if exists tm_catalog;

drop table if exists tm_catalog_criteria;

drop table if exists tm_db_column;

drop table if exists tm_db_table;

drop table if exists tm_dic;

drop table if exists tm_dic_value;

drop table if exists tm_query;

drop table if exists tm_query_column;

drop table if exists tm_query_criteria;

drop table if exists tm_query_join_criteria;

drop table if exists tm_query_selecton;

drop table if exists tm_query_table;

drop table if exists tm_refer;

drop table if exists twf_node_query;

drop table if exists twf_node_query_criteria;

drop table if exists twf_node_query_defined;
