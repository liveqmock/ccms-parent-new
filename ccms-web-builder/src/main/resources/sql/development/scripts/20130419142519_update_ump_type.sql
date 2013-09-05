--// adjust meta config for refer
-- Migration SQL that makes the change goes here.

delete from twf_node where node_id in (25);
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(25, '优惠券发送', 'tcommunicateUMP;image=../images/graph/icon/mb_youhui.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateUMP', '');

--//@UNDO
-- SQL to undo the change goes here.

delete from twf_node where node_id in (25);
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	  values(25, '优惠券发送', 'tcommunicateUMP;image=../images/graph/icon/mb_youhui.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateUMP', '');

