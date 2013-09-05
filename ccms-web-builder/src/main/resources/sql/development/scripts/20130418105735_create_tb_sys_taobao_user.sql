--// create  TWF_LOG_NODE_MIDS  table
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_sys_taobao_user (
  id bigint(20) NOT NULL COMMENT '主键 使用tb_sysuser表的id',
  plat_user_id varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的用户id',
  plat_user_name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的用户名',
  is_subuser tinyint(1) DEFAULT NULL COMMENT '是否是子账号',
  plat_shop_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户在平台方的店铺的id',
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_plat_user_id (plat_user_id) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ;

--//@UNDO
-- SQL to undo the change goes here.

drop table tb_sys_taobao_user ;