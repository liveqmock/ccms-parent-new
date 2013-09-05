--// init tb app properties
-- Migration SQL that makes the change goes here.
-- 重新整理module数据.由于数据量大,需要比较小心,故直接delete然后insert

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
	(7,'coupon','',NULL,NULL,NULL,NULL,0,17,'促销管理(页面)'),
	(8,'customer','',NULL,NULL,NULL,NULL,0,17,'客户管理(页面)'),
	(9,'admin','',NULL,NULL,NULL,NULL,0,17,'系统管理(页面)'),
	
	(10,'nav','',NULL,NULL,'',NULL,0,17,'导航栏'),
	
	(11,'index_link','首页','导航栏上的链接','#/dashboard','',NULL,0,17,NULL),
	(12,'shopHealth_link','店铺诊断','导航栏上的链接','#/analysis/evaluateSearch','',NULL,0,17,NULL),
	(13,'shopMonitor_link','店铺监控','导航栏上的链接','','',NULL,0,17,NULL),
	(14,'ordersCenter_link','订单中心','导航栏上的链接','','',NULL,0,17,NULL),
	(15,'marketing_link','营销活动','导航栏上的链接','#/marketing/campaign.list','',NULL,0,17,NULL),
	(16,'coupon_link','促销管理','导航栏上的链接','#/coupon/tickets','',NULL,0,17,NULL),
	(17,'customer_link','客户管理','导航栏上的链接','#/customer/blacklist','',NULL,0,17,NULL),
	(18,'admin_link','系统管理','导航栏上的链接','#/admin/taobaouser','',NULL,0,17,NULL),
	
	(19,'ordersMonitor','订单监控',NULL,NULL,NULL,NULL,0,17,NULL),
	(20,'logisticsMonitor','异常物流监控',NULL,NULL,NULL,NULL,0,17,NULL),
	
	(21,'scheduledDeptCollect','定时催付',NULL,NULL,NULL,NULL,0,17,NULL),
	(22,'realTimeDeptCollect','实时催付',NULL,NULL,NULL,NULL,0,17,NULL),
	(23,'deliveryNotice','发货通知',NULL,NULL,NULL,NULL,0,17,NULL),
	(24,'sameCityNotice','同城通知',NULL,NULL,NULL,NULL,0,17,NULL),
	(25,'signNotice','签收通知',NULL,NULL,NULL,NULL,0,17,NULL),
	
	(26,'activityList','营销活动',NULL,NULL,NULL,NULL,0,17,NULL),
	
	(27,'couponList','优惠券',NULL,NULL,NULL,NULL,0,17,NULL),
	
	(28,'blacklistManage','黑名单管理',NULL,NULL,NULL,NULL,0,17,NULL),
	(29,'smsBlacklist','短信黑名单',NULL,NULL,NULL,NULL,0,17,NULL),
	
	(30,'wangwangSubuser','旺旺子号',NULL,NULL,NULL,NULL,0,17,NULL)
;

insert into module
(id, module_type_id, container_module_id,ranking,
key_name, name, url, data_url,
tip, lowest_edition_required, support_ops_mask, memo)
values
	(1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'所有页面都有的模块的父模块'),
	(2,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(3,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(4,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(5,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(6,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(7,7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(8,8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(9,9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(10,10,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(11,11,10,100,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(12,12,10,200,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(13,13,10,300,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(14,14,10,400,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(15,15,10,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(16,16,10,600,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(17,17,10,700,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(18,18,10,800,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	                                               
	(19,19,4,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(20,20,4,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(21,21,5,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(22,22,5,600,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(23,23,5,700,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(24,24,5,800,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(25,25,5,900,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(26,26,6,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(27,27,7,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(28,28,8,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(29,29,28,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),

	(30,30,9,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)
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

