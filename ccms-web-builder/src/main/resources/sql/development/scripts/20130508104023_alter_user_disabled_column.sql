--// modify table evaluate_report_result add constrain  not null
-- Migration SQL that makes the change goes here.

update tb_sysuser set disabled=0;

ALTER TABLE tb_sysuser     CHANGE disabled disabled TINYINT(4) DEFAULT '0' NOT NULL;

--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE tb_sysuser     CHANGE disabled disabled TINYINT(4) DEFAULT '0' NOT NULL;
