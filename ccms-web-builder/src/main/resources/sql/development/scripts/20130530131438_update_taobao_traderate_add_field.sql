--// update taobao traderate add field
-- Migration SQL that makes the change goes here.
alter table plt_taobao_traderate add content varchar(500)  COMMENT '评价内容';
alter table plt_taobao_traderate add reply varchar(500) COMMENT '回评';


--//@UNDO
-- SQL to undo the change goes here.
alter table plt_taobao_traderate drop column content;
alter table plt_taobao_traderate drop column reply;

