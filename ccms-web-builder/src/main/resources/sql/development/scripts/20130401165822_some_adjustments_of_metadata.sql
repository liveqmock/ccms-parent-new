--// some adjustments of metadata
-- Migration SQL that makes the change goes here.

#调整表结构
alter table twf_node_query_defined add relation varchar(3) null comment '关系符';
alter table twf_node_query_defined add ext_ctrl_info varchar(200) null comment '额外控制信息';

#更新数据
update tm_query set code = 'CUSTOMER' where query_id = 1;
update tm_query set code = 'ORDER' where query_id = 2;
update tm_query set code = 'ORDER_ITEM' where query_id = 3;

update tm_dic set plat_code = 'taobao' where dic_id in(41, 42, 43);
update tm_db_table set plat_code = 'taobao';
update tm_query set plat_code = 'taobao';

--//@UNDO
-- SQL to undo the change goes here.

alter table twf_node_query_defined drop relation;
alter table twf_node_query_defined drop ext_ctrl_info;
