--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 初始化模块、模块准入规则数据

-- 先删除之前插入的测试数据
delete from module_type;
delete from module;
delete from module_entry;

INSERT INTO module_type
(id, key_name, name, name_plus, url, data_url, tip, lowest_edition_required, support_ops_mask,memo)
VALUES
	(1,'','',NULL,NULL,NULL,NULL,0,0,'containerOfTheModulesInEachPage:所有页面都有的模块的父模块的类型'),
	(2,'index','',NULL,NULL,NULL,NULL,0,17,'首页(页面)'),
	(3,'shopHealth','',NULL,NULL,NULL,NULL,0,17,'店铺诊断(页面)'),
	(4,'shopMonitor','',NULL,NULL,NULL,NULL,0,17,'店铺监控(页面)'),
	(5,'ordersCenter','',NULL,NULL,NULL,NULL,0,17,'订单中心(页面)'),
	(6,'marketing','',NULL,NULL,NULL,NULL,0,17,'营销活动(页面)'),
	(7,'promotions','',NULL,NULL,NULL,NULL,0,17,'促销管理(页面)'),
	(8,'customerManage','',NULL,NULL,NULL,NULL,0,17,'客户管理(页面)'),
	(9,'sys','',NULL,NULL,NULL,NULL,0,17,'系统管理(页面)'),
	
	(10,'nav','',NULL,NULL,'',NULL,0,17,'导航栏'),
	
	(11,'index_link','首页','导航栏上的链接','#/dashboard','',NULL,0,17,NULL),
	(12,'shopHealth_link','店铺诊断','导航栏上的链接','#/analysis/evaluateSearch','',NULL,0,17,NULL),
	(13,'shopMonitor_link','店铺监控','导航栏上的链接','','',NULL,0,17,NULL),
	(14,'ordersCenter_link','订单中心','导航栏上的链接','','',NULL,0,17,NULL),
	(15,'marketing_link','营销活动','导航栏上的链接','#/marketing/campaign.list','',NULL,0,17,NULL),
	(16,'promotions_link','促销管理','导航栏上的链接','#/discount/tickets','',NULL,0,17,NULL),
	(17,'customerManage_link','客户管理','导航栏上的链接','#/customer/blacklist','',NULL,0,17,NULL),
	(18,'sys_link','系统管理','导航栏上的链接','#/admin/taobaouser','',NULL,0,17,NULL)
;

insert into module
(id, key_name, name, module_type_id, url, data_url, tip, lowest_edition_required, support_ops_mask, memo, container_module_id)
values
	(1,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,'所有页面都有的模块的父模块',NULL),
	(2,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(3,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(4,NULL,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(5,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(6,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(7,NULL,NULL,7,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(8,NULL,NULL,8,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(9,NULL,NULL,9,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	
	(10,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,1),
	
	(11,NULL,NULL,11,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(12,NULL,NULL,12,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(13,NULL,NULL,13,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(14,NULL,NULL,14,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(15,NULL,NULL,15,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(16,NULL,NULL,16,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(17,NULL,NULL,17,NULL,NULL,NULL,NULL,NULL,NULL,10),
	(18,NULL,NULL,18,NULL,NULL,NULL,NULL,NULL,NULL,10)
;

insert into module_entry(id,module_id,permission_id,role_id,user_id,support_ops_mask,memo)
values
	(1,NULL,NULL,NULL,NULL,0,'最根的授权,包括permission为null者(匿名访问)'),
	(2,NULL,0,NULL,NULL,31,'permission不为null者对所有模块的权限的默认值'),
	
	(3,18,0,NULL,NULL,0,'permission不为null者对模块18(系统管理)的权限为0'),
	(4,18,8,NULL,NULL,17,'permission8对模块18(系统管理)17.本条覆盖上一条')
;
--//@UNDO
-- SQL to undo the change goes here.

delete from module_type;
delete from module;
delete from module_entry;
