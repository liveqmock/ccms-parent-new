--// modify  table  evaluate_report_result  column
-- Migration SQL that makes the change goes here.

ALTER TABLE evaluate_report_result  MODIFY COLUMN evaluate_time VARCHAR(50);

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE evaluate_report_result  MODIFY COLUMN evaluate_time Date;
