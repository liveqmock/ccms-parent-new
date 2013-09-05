--// init tb app properties
-- Migration SQL that makes the change goes here.

delete from tds_permission;

INSERT INTO tds_permission(id,name,memo,permission_key)
VALUES 
	(1,'首页',NULL,NULL),
	(2,'店铺诊断',NULL,NULL),
	(3,'店铺监控',NULL,NULL),
	(4,'订单中心',NULL,NULL),
	(5,'营销活动',NULL,NULL),
	(6,'促销管理',NULL,NULL),
	(7,'客户管理',NULL,NULL),
	(8,'系统管理',NULL,NULL)
;

delete from tb_role;

insert into tb_role(id,name,memo)
values(100000,'管理员',null),
	(100001,'普通用户',null)
;



-- 角色_权限.普通用户比管理员少一个"系统管理"权限

delete from tb_role_permission;
insert into tb_role_permission(role_id,permission_id)
values
	(100000,1),(100000,2),(100000,3),(100000,4),(100000,5),(100000,6),(100000,7),(100000,8),
	(100001,1),(100001,2),(100001,3),(100001,4),(100001,5),(100001,6),(100001,7)
;


--//@UNDO
-- SQL to undo the change goes here.

delete from tds_permission;
delete from tb_role;
delete from tb_role_permission;