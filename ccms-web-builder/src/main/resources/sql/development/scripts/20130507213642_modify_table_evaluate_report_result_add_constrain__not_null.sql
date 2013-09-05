--// modify table evaluate_report_result add constrain  not null
-- Migration SQL that makes the change goes here.

ALTER TABLE evaluate_report_result MODIFY job_id bigint(20) NOT NULL;
ALTER TABLE evaluate_report_result MODIFY node_id bigint(20) NOT NULL;
ALTER TABLE evaluate_report_result MODIFY evaluate_time VARCHAR(50) NOT NULL;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE evaluate_report_result MODIFY job_id bigint(20)  NULL;
ALTER TABLE evaluate_report_result MODIFY node_id bigint(20)  NULL;
ALTER TABLE evaluate_report_result MODIFY evaluate_time VARCHAR(50)  NULL;
