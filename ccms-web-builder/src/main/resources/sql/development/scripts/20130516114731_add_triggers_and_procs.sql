--// update tb template
-- Migration SQL that makes the change goes here.

CREATE TRIGGER trigger_delete_extaobao_customer_plat AFTER DELETE ON plt_extaobao_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('extaobao', OLD.customerno);
END ;


CREATE TRIGGER trigger_delete_kfgzt_customer_plat AFTER DELETE ON plt_kfgzt_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('kfgzt', OLD.customerno);
END ;


CREATE TRIGGER trigger_delete_wdzx_customer_plat AFTER DELETE ON plt_wdzx_customer
FOR EACH ROW BEGIN
    Call proc_delete_customer('wdzx', OLD.customerno);
END ;


CREATE PROCEDURE proc_delete_customer(IN p_plat_code char(8), IN p_customerno varchar(50))
    COMMENT '删除客户相关平台信息'
BEGIN
  Delete from uni_customer_plat where plat_code = p_plat_code and customerno = p_customerno;
END ;


--//@UNDO
-- SQL to undo the change goes here.
drop TRIGGER trigger_delete_extaobao_customer_plat;
drop TRIGGER trigger_delete_kfgzt_customer_plat;
drop TRIGGER trigger_delete_wdzx_customer_plat;
drop PROCEDURE proc_delete_customer;
