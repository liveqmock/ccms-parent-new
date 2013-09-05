--// init template workflow data
-- Migration SQL that makes the change goes here.

--  初始流程配置数据
insert into twf_workflow(workflow_id, create_time, update_time) values(1, now(), now());
insert into twf_workflow(workflow_id, create_time, update_time) values(2, now(), now());
insert into twf_workflow(workflow_id, create_time, update_time) values(3, now(), now());

--  基础版的模板配置数据
insert into tb_template(template_id, template_name, created_time, updated_time, template_desc, 
	comments, workflow_id, plat_code, disabled, version, pic_url) values (1, '短信营销模板', now(), now(), 
	'', '', 1, 'taobao', 0, 'L3', '../images/compType-dx.png');
	
insert into tb_template(template_id, template_name, created_time, updated_time, template_desc, 
	comments, workflow_id, plat_code, disabled, version, pic_url) values (2, '邮件营销模板', now(), now(), 
	'', '', 2, 'taobao', 0, 'L3', '../images/compType-yj.png');
	
insert into tb_template(template_id, template_name, created_time, updated_time, template_desc, 
	comments, workflow_id, plat_code, disabled, version, pic_url) values (3, '优惠券营销模板', now(), now(), 
	'', '', 3, 'taobao', 0, 'L3', '../images/compType-yhq.png');

-- 短信营销模板的节点与连接线配置数据
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(1, '开始设计', 'tflowstart;image=../images/graph/icon/lc_begin.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tflowstart', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(2, '营销时间', 'tflowtime;image=../images/graph/icon/lc_time.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tflowtime', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(3, '客户筛选', 'tfilterfind;image=../images/graph/icon/lc_lsearch.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tfilterfind', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(4, '目标客户', 'tcustomertargetgroup;image=../images/graph/icon/kh_clients.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcustomertargetgroup', '');

--  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
--	  values(5, '短信发送', 'tcommunicateSMS;image=../images/graph/icon/mb_note.png', '1', 1, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');
--
--  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
--	  values(6, '效果评估', '', '1', 1, 100, 200, 52, 52, 'geometry', '', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(1, '1', 1, 1, 2, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(2, '1', 1, 2, 3, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(3, '1', 1, 3, 4, '1', 'geometry');

--  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
--	  values(4, '1', 1, 4, 5, '1', 'geometry');
--
--  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
--	  values(5, '1', 1, 5, 6, '1', 'geometry');

--  邮件营销模板的节点与连接线配置
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(11, '开始设计', 'tflowstart;image=../images/graph/icon/lc_begin.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tflowstart', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(12, '营销时间', 'tflowtime;image=../images/graph/icon/lc_time.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tflowtime', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(13, '客户筛选', 'tfilterfind;image=../images/graph/icon/lc_lsearch.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tfilterfind', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(14, '目标客户', 'tcustomertargetgroup;image=../images/graph/icon/kh_clients.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcustomertargetgroup', '');

--  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
--	  values(15, '邮件发送', 'tcommunicateEDM;image=../images/graph/icon/mb_mail.png', '1', 2, 100, 200, 52, 52, 'geometry', 'tcommunicateEDM', '');
--
--  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
--	  values(16, '效果评估', '', '1', 2, 100, 200, 52, 52, 'geometry', '', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(11, '1', 2, 11, 12, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(12, '1', 2, 12, 13, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(13, '1', 2, 13, 14, '1', 'geometry');

--  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
--	  values(14, '1', 2, 14, 15, '1', 'geometry');
--
--  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
--	  values(15, '1', 2, 15, 16, '1', 'geometry');

--  优惠券营销模板的节点与连接线配置
insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(21, '开始设计', 'tflowstart;image=../images/graph/icon/lc_begin.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tflowstart', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(22, '营销时间', 'tflowtime;image=../images/graph/icon/lc_time.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tflowtime', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(23, '客户筛选', 'tfilterfind;image=../images/graph/icon/lc_lsearch.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tfilterfind', '');

insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
	values(24, '目标客户', 'tcustomertargetgroup;image=../images/graph/icon/kh_clients.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcustomertargetgroup', '');

--  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
--	  values(25, '优惠券发送', 'tcommunicateUMP;image=../images/graph/icon/mb_youhui.png', '1', 3, 100, 200, 52, 52, 'geometry', 'tcommunicateSMS', '');
--
--  insert into twf_node(node_id, value, style, vertex, workflow_id, x, y, width, height, as_t, type, description)
--	  values(26, '效果评估', '', '1', 3, 100, 200, 52, 52, 'geometry', '', '');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(21, '1', 3, 21, 22, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(22, '1', 3, 22, 23, '1', 'geometry');

insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
	values(23, '1', 3, 23, 24, '1', 'geometry');

--  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
--	  values(24, '1', 3, 14, 15, '1', 'geometry');
--
--  insert into twf_connect(connect_id, edge, workflow_id, source, target, relative, as_t) 
--	  values(25, '1', 3, 15, 16, '1', 'geometry');

--  设置twf_workflow的自增长id 开始位置
ALTER TABLE twf_workflow AUTO_INCREMENT = 1000;
ALTER TABLE tb_template AUTO_INCREMENT = 1000;
ALTER TABLE twf_node AUTO_INCREMENT = 1000;
ALTER TABLE twf_connect AUTO_INCREMENT = 1000;



--//@UNDO
-- SQL to undo the change goes here.
delete from tb_campaign;
delete from tb_template;
delete from twf_node;
delete from twf_connect;
delete from twf_workflow;

