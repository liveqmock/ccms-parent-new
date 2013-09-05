--// init tds status
-- Migration SQL that makes the change goes here.

-- #活动状态表
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('A1', '设计中', '1');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('A2', '待审批', '3');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('A3', '待执行', '5');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('A4', '中止', '7');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('A5', '执行完成', '8');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('A6', '执行结束（错误）', '9');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('B1', '设计时预执行', '2');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('B2', '待审批时预执行', '4');
INSERT INTO tds_camp_status (status_id, status_value, orderid) VALUES ('B3', '执行中', '6');

-- #流程执行JOB状态维表
INSERT INTO tds_job_status (status_id, status_name) VALUES (10,'准备开始');
INSERT INTO tds_job_status (status_id, status_name) VALUES (11,'运行中');
INSERT INTO tds_job_status (status_id, status_name) VALUES (12,'运行中有错误');
INSERT INTO tds_job_status (status_id, status_name) VALUES (13,'运行中待中止');
INSERT INTO tds_job_status (status_id, status_name) VALUES (18,'等待操作');
INSERT INTO tds_job_status (status_id, status_name) VALUES (21,'运行正常结束');
INSERT INTO tds_job_status (status_id, status_name) VALUES (22,'运行错误结束');
INSERT INTO tds_job_status (status_id, status_name) VALUES (23,'运行中止结束');

-- #节点执行SUBJOB状态维表
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (10,'准备开始');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (11,'运行中');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (12,'运行中有错误');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (13,'运行中待中止');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (8,'等待资源');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (18,'等待操作');

INSERT INTO tds_subjob_status (status_id, status_name) VALUES (21,'运行正常结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (22,'运行错误结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (23,'运行中止结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (24,'运行跳过结束');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (25,'运行超时结束');

INSERT INTO tds_subjob_status (status_id, status_name) VALUES (51,'运行结束(成功)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (52,'运行结束(错误)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (53,'运行结束(中止)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (54,'运行结束(跳过)');
INSERT INTO tds_subjob_status (status_id, status_name) VALUES (55,'运行结束(超时)');

--//@UNDO
-- SQL to undo the change goes here.

truncate tds_camp_status;
truncate tds_job_status;
truncate tds_subjob_status;
