--// base template workflow data
-- Migration SQL that makes the change goes here.
delete from twf_node where node_id = 26;
delete from twf_connect where connect_id = 25;

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(26, '短信发送', 'tcommunicateSMS;image=../images/graph/icon/mb_note.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(27, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	  values(25, '1', 3, 25, 26, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	  values(26, '1', 3, 26, 27, '1', 'geometry');


--//@UNDO
-- SQL to undo the change goes here.
delete from twf_node where node_id in (26, 27);
delete from twf_connect where connect_id in (25, 26);

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(26, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t)
	  values(25, '1', 3, 25, 26, '1', 'geometry');


