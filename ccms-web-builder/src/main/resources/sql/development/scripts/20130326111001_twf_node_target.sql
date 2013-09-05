--// twf node target
-- Migration SQL that makes the change goes here.

CREATE TABLE twf_node_target (
node_id  bigint(20) NOT NULL COMMENT '节点ID' ,
name  varchar(20) COMMENT '名称' ,
control_group_type  int(4) COMMENT '控制组生成方式： 1 百分比 2 人数 ' ,
control_group_value  varchar(10) COMMENT '控制组生成值，或者是百分比，或者是指定的人数' ,
remark  varchar(128) COMMENT '备注' ,
created datetime COMMENT '创建时间' 
)
COMMENT '目标组节点' ;

--//@UNDO
-- SQL to undo the change goes here.

drop table twf_node_target ;
