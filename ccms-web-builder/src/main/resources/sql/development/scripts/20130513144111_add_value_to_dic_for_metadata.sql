--// add value to dic for metadata
-- Migration SQL that makes the change goes here.

INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (1040, 43, NULL, '0', '未分级');

--//@UNDO
-- SQL to undo the change goes here.

DELETE FROM tm_dic_value where dic_value_id = 1040;

