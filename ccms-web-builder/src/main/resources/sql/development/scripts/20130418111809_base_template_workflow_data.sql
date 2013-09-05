--// base template workflow data
-- Migration SQL that makes the change goes here.

  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(5, '短信发送', 'tcommunicateSMS;image=../images/graph/icon/mb_note.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');

  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(6, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/fx_analysisorder.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	  values(4, '1', 1, 4, 5, '1', 'geometry');

  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	  values(5, '1', 1, 5, 6, '1', 'geometry');

  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(15, '邮件发送', 'tcommunicateEDM;image=../images/graph/icon/mb_mail.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcommunicateEDM', '');

  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(16, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/fx_analysisorder.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	  values(14, '1', 2, 14, 15, '1', 'geometry');

  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	  values(15, '1', 2, 15, 16, '1', 'geometry');

  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(25, '优惠券发送', 'tcommunicateUMP;image=../images/graph/icon/mb_youhui.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');

  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(26, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/fx_analysisorder.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	  values(24, '1', 3, 24, 25, '1', 'geometry');

  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	  values(25, '1', 3, 25, 26, '1', 'geometry');

--//@UNDO
-- SQL to undo the change goes here.
delete from twf_node where node_id in (5, 6, 15, 16, 25, 26) ;
delete from twf_connect where connect_id in (4, 5, 14, 15, 24, 25);

