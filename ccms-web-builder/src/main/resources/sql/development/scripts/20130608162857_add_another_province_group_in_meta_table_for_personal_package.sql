--// add another province_group in meta_table for personal package
-- Migration SQL that makes the change goes here.

insert ignore into tm_dic(dic_id,plat_code,show_name,remark)
values(51,'ALL','省份','个性化包裹用,订单收货地址 包含');

INSERT ignore INTO tm_dic_value(dic_value_id,dic_id,parent_id,dic_value,show_name,remark)
	select dic_value_id*10 as dic_value_id, 51, parent_id, dic_value, show_name, remark
	from tm_dic_value where dic_id=22
;

update tm_db_column set dic_id=51,db_type='region' where column_id=605;

--//@UNDO
-- SQL to undo the change goes here.

update tm_db_column set dic_id=22 where column_id=605;

delete from tm_dic_value where dic_id=51;

delete from tm_dic where dic_id=51;
