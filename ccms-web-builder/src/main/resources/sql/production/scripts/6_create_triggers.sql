#-----------------------------------------------------------------------------------------------------------------------------------
#  建触发器脚本
#-----------------------------------------------------------------------------------------------------------------------------------

#plt_extaobao_customer 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_insert_extaobao_customer AFTER INSERT ON plt_extaobao_customer
FOR EACH ROW Begin
    #1.将当前客户信息同步到父平台
    Call proc_sync_parent_plat('extaobao', 'taobao',
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
    #2.再统一客户信息
    Call proc_unify_customer('extaobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_update_extaobao_customer AFTER UPDATE ON plt_extaobao_customer
FOR EACH ROW Begin
    Call proc_unify_customer('extaobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


#plt_ext_customer 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_insert_ext_customer AFTER INSERT ON plt_ext_customer
FOR EACH ROW Begin
    Call proc_unify_customer('ext' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_update_ext_customer AFTER UPDATE ON plt_ext_customer
FOR EACH ROW Begin
    Call proc_unify_customer('ext' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_delete_ext_customer AFTER DELETE ON plt_ext_customer
FOR EACH ROW BEGIN
    Call proc_delete_unify_customer('ext', OLD.customerno);
END
//
delimiter ;


#plt_kfgzt_customer 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_insert_kfgzt_customer AFTER INSERT ON plt_kfgzt_customer
FOR EACH ROW Begin
    #1.将当前客户信息同步到父平台
    Call proc_sync_parent_plat('kfgzt', 'taobao',
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
    #2.再统一客户信息
    Call proc_unify_customer('kfgzt' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_update_kfgzt_customer AFTER UPDATE ON plt_kfgzt_customer
FOR EACH ROW Begin
    Call proc_unify_customer('kfgzt' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


#plt_kfgzt_customer_label 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_sync_kfgzt_customer AFTER INSERT ON plt_kfgzt_customer_label
FOR EACH ROW BEGIN
    Insert into plt_kfgzt_customer (customerno)  #“客服工作台”客户标签新增客户时, 若客服工作台不存在该客户，则同步到客服工作台的客户来源表
    Select NEW.customerno From plt_kfgzt_customer_label
    Where NOT EXISTS (select 1 from  plt_kfgzt_customer where customerno =NEW.customerno ) Limit 1;
END
//
delimiter ;



#plt_modify_customer 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_insert_modify_customer AFTER INSERT ON plt_modify_customer
FOR EACH ROW Begin
    Call proc_unify_modify_customer(NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                                        NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;



delimiter //
CREATE TRIGGER trigger_update_modify_customer AFTER UPDATE ON plt_modify_customer
FOR EACH ROW Begin
    Call proc_unify_modify_customer(NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                                        NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;



#plt_wdzx_customer 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_insert_wdzx_customer AFTER INSERT ON plt_wdzx_customer
FOR EACH ROW Begin
    #1.将当前客户信息同步到父平台
    Call proc_sync_parent_plat('wdzx', 'taobao',
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
    #2.再统一客户信息
    Call proc_unify_customer('wdzx' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_update_wdzx_customer AFTER UPDATE ON plt_wdzx_customer
FOR EACH ROW Begin
    Call proc_unify_customer('wdzx' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


#plt_taobao_customer 触发器----------------------------------------------------------------------------------------------------
delimiter //
CREATE TRIGGER trigger_insert_taobao_customer AFTER INSERT ON plt_taobao_customer
FOR EACH ROW Begin
    Call proc_unify_customer('taobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_update_taobao_customer AFTER UPDATE ON plt_taobao_customer
FOR EACH ROW Begin
    Call proc_unify_customer('taobao' ,
                             NEW.customerno, NEW.full_name, NEW.sex, NEW.job, NEW.birth_year, NEW.birthday, NEW.email, NEW.mobile, NEW.phone,
                             NEW.zip, NEW.country, NEW.state, NEW.city, NEW.district, NEW.address, NEW.changed);
END
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_delete_extaobao_customer_plat AFTER DELETE ON plt_extaobao_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('extaobao', OLD.customerno);
END 
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_delete_kfgzt_customer_plat AFTER DELETE ON plt_kfgzt_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('kfgzt', OLD.customerno);
END 
//
delimiter ;


delimiter //
CREATE TRIGGER trigger_delete_wdzx_customer_plat AFTER DELETE ON plt_wdzx_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('wdzx', OLD.customerno);
END 
//
delimiter ;
