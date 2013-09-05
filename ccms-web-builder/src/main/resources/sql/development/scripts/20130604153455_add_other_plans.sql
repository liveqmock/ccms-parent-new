--// add other plans
-- Migration SQL that makes the change goes here.

insert ignore into rc_plan(id,name,position,active,shop_id,start_time,last_config_time)
values(7,'方案2',2,0,'100571094',null,null),
(8,'方案3',3,0,'100571094',null,null)
;

update rc_plan set position=1 where id=6;

--//@UNDO
-- SQL to undo the change goes here.

delete from rc_plan where id in(7,8);
