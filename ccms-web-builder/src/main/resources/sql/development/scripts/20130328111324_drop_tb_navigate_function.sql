--// drop tb navigate function
-- Migration SQL that makes the change goes here.

drop table tb_navigate_function;

--//@UNDO
-- SQL to undo the change goes here.

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
