--// update ec node type
-- Migration SQL that makes the change goes here.
update twf_node set type='tcommunicateEC',style='tcommunicateEC;image=../images/graph/icon/mb_youhui.png' where type='tcommunicateUMP';




--//@UNDO
-- SQL to undo the change goes here.
update twf_node set type='tcommunicateUMP',style='tcommunicateUMP;image=../images/graph/icon/mb_youhui.png' where type='tcommunicateEC';

