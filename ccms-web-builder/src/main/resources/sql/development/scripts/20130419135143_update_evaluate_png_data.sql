--// adjust meta config for refer
-- Migration SQL that makes the change goes here.

delete from twf_node where node_id in (6, 16, 26);
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(6, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(16, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(26, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/lc_valuate.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');


--//@UNDO
-- SQL to undo the change goes here.
delete from twf_node where node_id in (6, 16, 26);
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(6, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/fx_analysisorder.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(16, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/fx_analysisorder.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(26, '效果评估', 'tcustomerevaluate;image=../images/graph/icon/fx_analysisorder.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomerevaluate', '');


