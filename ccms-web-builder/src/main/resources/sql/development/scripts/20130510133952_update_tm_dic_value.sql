--// update tm_dic_value
-- Migration SQL that makes the change goes here.

delete from tm_dic_value where dic_id = 23 and dic_value_id in (110200, 120200, 310200, 500200, 419000, 429000, 469000);
update tm_dic_value set show_name = '北京市' where dic_value_id = 110100;
update tm_dic_value set show_name = '天津市' where dic_value_id = 120100;
update tm_dic_value set show_name = '上海市' where dic_value_id = 310100;
update tm_dic_value set show_name = '重庆市' where dic_value_id = 500100;

--//@UNDO
-- SQL to undo the change goes here.

INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (110200, 23, 110000, '县', '县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (120200, 23, 120000, '县', '县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (310200, 23, 310000, '县', '县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (500200, 23, 500000, '县', '县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (419000, 23, 410000, '省直辖县', '省直辖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (429000, 23, 420000, '省直辖县', '省直辖县');
INSERT INTO tm_dic_value (dic_value_id, dic_id, parent_id, dic_value, show_name) VALUES (469000, 23, 460000, '省直辖县', '省直辖县');

update tm_dic_value set show_name = '市辖区' where dic_value_id = 110100;
update tm_dic_value set show_name = '市辖区' where dic_value_id = 120100;
update tm_dic_value set show_name = '市辖区' where dic_value_id = 310100;
update tm_dic_value set show_name = '市辖区' where dic_value_id = 500100;