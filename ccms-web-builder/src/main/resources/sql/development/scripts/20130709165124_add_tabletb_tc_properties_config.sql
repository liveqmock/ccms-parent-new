--// add_tabletb_tc_properties_config
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_properties_config (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  dp_id varchar(20) DEFAULT NULL,
  name varchar(255) DEFAULT NULL COMMENT '配置项',
  value varchar(50) DEFAULT NULL COMMENT '配置值',
  description varchar(100) DEFAULT NULL COMMENT '配置项描述',
  group_name varchar(20) DEFAULT NULL COMMENT '属性分组',
  created datetime DEFAULT NULL,
  updated datetime DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;




--//@UNDO
-- SQL to undo the change goes here.
drop table tb_tc_properties_config;

