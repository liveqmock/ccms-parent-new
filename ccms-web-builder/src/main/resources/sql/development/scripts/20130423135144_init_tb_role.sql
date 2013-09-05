--// init tb app properties
-- Migration SQL that makes the change goes here.

INSERT ignore INTO tb_sys_taobao_user(id,plat_user_id,plat_user_name,is_subuser,plat_shop_id)
VALUES ( '1','12334','求是',false,'taobao_68784446');
 
--//@UNDO
-- SQL to undo the change goes here.

delete from tb_sys_taobao_user where id='1';
