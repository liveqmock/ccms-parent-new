--// update transitsetpinfo tableName to transitstepinfo
-- Migration SQL that makes the change goes here.

alter table plt_taobao_transitsetpinfo rename to plt_taobao_transitstepinfo;

--//@UNDO
-- SQL to undo the change goes here.

alter table plt_taobao_transitstepinfo rename to plt_taobao_transitsetpinfo;
