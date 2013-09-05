###-----------------------------------------------------------------------------------------------------------------------------
###  4. functions
###-----------------------------------------------------------------------------------------------------------------------------

delimiter //
CREATE  function  func_get_dic_viewname(p_dic_type_id int, p_type_value varchar (32)) 
	RETURNS  varchar(64)  CHARSET utf8  DETERMINISTIC 
	COMMENT  '返回元数据字典项显示值\r\n参数: 字典ID, 字典项值'
BEGIN 
DECLARE  v_vn VARCHAR(64);
	select show_name into v_vn from tm_dic_value where dic_id = p_dic_type_id  and dic_value = p_type_value  limit 1;
	RETURN  v_vn;
END 
//
delimiter ;
