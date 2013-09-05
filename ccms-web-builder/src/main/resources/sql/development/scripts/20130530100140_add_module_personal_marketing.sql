--// add module personal marketing
-- Migration SQL that makes the change goes here.

INSERT INTO module_type(id,key_name,name,name_plus,url,data_url,tip,lowest_edition_required,support_ops_mask,memo)
VALUES
(31,'personalizedPackage','个性化营销','','',NULL,NULL,'0','17','页面'),
(32,'personalizedPackage_link','个性化营销','导航栏上的链接','#/personalizedPackage/mainIndex',NULL,NULL,'0','17',NULL),
(33,'personalizedPackage1','个性化包裹','','#/personalizedPackage/mainIndex',NULL,NULL,'0','17',NULL),
(34,'personalizedPackage_deploy','方案部署','','#/personalizedPackage/mainIndex',NULL,NULL,'0','17',NULL),
(35,'personalizedPackage_conf','方案配置','','#/personalizedPackage/deploy',NULL,NULL,'0','17',NULL),
(36,'personalizedPackage_run','运行监控','','#/personalizedPackage/run',NULL,NULL,'0','17',NULL),
(37,'personalizedPackage_info','明细查询','','#/personalizedPackage/info',NULL,NULL,'0','17',NULL)
;

INSERT INTO module(id,key_name,module_type_id,container_module_id,url,data_url,name,tip,lowest_edition_required,support_ops_mask,ranking,memo)
VALUES
('31',NULL,'31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'650',NULL),
('32',NULL,'32','10',NULL,NULL,NULL,NULL,NULL,NULL,'300',NULL),
('33',NULL,'33','31',NULL,NULL,NULL,NULL,NULL,NULL,'300',NULL),
('34',NULL,'34','31',NULL,NULL,NULL,NULL,NULL,NULL,'400',NULL),
('35',NULL,'35','31',NULL,NULL,NULL,NULL,NULL,NULL,'500',NULL),
('36',NULL,'36','31',NULL,NULL,NULL,NULL,NULL,NULL,'600',NULL),
('37',NULL,'36','31',NULL,NULL,NULL,NULL,NULL,NULL,'600',NULL)
;

--//@UNDO
-- SQL to undo the change goes here.

delete from module where id=37;
delete from module where id=36;
delete from module where id=35;
delete from module where id=34;
delete from module where id=33;
delete from module where id=32;
delete from module where id=31;
delete from module_type where id in(31,32,33,34,35,36,37);
