--// drop selecton table
-- Migration SQL that makes the change goes here.

drop table tm_query_selecton;

alter table tm_query_column drop query_selecton_id;
alter table tm_query_column add query_id  bigint not null;

--//@UNDO
-- SQL to undo the change goes here.

create table tm_query_selecton
(
   query_selecton_id    bigint not null auto_increment,
   query_id             bigint comment '查询模版ID',
   purpose_type         varchar(32) not null comment '用途：分运行，展示，导入和导出',
   primary key (query_selecton_id)
);

alter table tm_query_column drop query_id;
alter table tm_query_column add query_selecton_id bigint(20) DEFAULT NULL;