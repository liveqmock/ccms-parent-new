--// CCMS-3786 create_index_on_plt_taobao_traderate
-- Migration SQL that makes the change goes here.

create index idx_plt_taobao_traderate_nick on plt_taobao_traderate(nick); 

create index idx_plt_taobao_crm_member_grade on  plt_taobao_crm_member(grade);

--//@UNDO
-- SQL to undo the change goes here.

drop index idx_plt_taobao_traderate_nick on plt_taobao_traderate;

drop index idx_plt_taobao_crm_member_grade on plt_taobao_crm_member;


