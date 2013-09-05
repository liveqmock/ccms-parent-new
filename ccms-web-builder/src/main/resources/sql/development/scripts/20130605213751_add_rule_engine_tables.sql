--// add rule engine tables
-- Migration SQL that makes the change goes here.

CREATE TABLE rc_drl (
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  plan_id bigint(20) NOT NULL COMMENT '方案id',
  drl varchar(200) COLLATE utf8_bin NOT NULL COMMENT 'drl文件名',
  start_time timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '规则启用时间',
  PRIMARY KEY(shop_id,plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

create table rc_drl_changelog(
  shop_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  plan_id bigint(20) NOT NULL COMMENT '方案id',
  drl varchar(200) COLLATE utf8_bin NOT NULL COMMENT 'drl文件名',
  start_time datetime NOT NULL  COMMENT '规则启用时间',
  end_time timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '规则停用时间',
  key(shop_id,plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--//@UNDO
-- SQL to undo the change goes here.
drop table rc_drl;
drop table rc_drl_changelog;

