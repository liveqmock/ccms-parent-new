--// add region_city to dic_value
-- Migration SQL that makes the change goes here.

INSERT INTO tm_dic(dic_id,plat_code,show_name,remark)
VALUES ( '52','ALL','城市','个性化包裹用,订单收货地址 包含');

insert into tm_dic_value(dic_value_id,dic_id,parent_id,dic_value,show_name,remark)
	select c.dic_value_id*10, 52, c.parent_id*10, c.dic_value, c.show_name, c.remark
	from tm_dic_value c where dic_id=23;

--//@UNDO
-- SQL to undo the change goes here.

delete from tm_dic_value where dic_id=52;

delete from tm_dic where dic_id=52;