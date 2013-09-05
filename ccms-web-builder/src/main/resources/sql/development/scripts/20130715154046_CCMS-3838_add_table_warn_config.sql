--// CCMS-3838_add_table_warn_config
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_warn_config (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  dp_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺id',
  created timestamp NULL DEFAULT NULL,
  updated timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  warn_type smallint(6) NOT NULL COMMENT '警告类型：30.中差评告警；31.退款告警',
  warn_start_time timestamp NULL DEFAULT NULL COMMENT '告警开始时间',
  warn_end_time timestamp NULL DEFAULT NULL COMMENT '告警结束时间',
  user_name varchar(30) COLLATE utf8_bin DEFAULT NULL,
  op_user varchar(10) COLLATE utf8_bin DEFAULT NULL,
  is_open int(1) NOT NULL DEFAULT '1',  -- 用户是否开启任务
  is_switch tinyint(4) NOT NULL DEFAULT '0', -- 后台是否开启任务
  content varchar(300) COLLATE utf8_bin DEFAULT NULL,
  warn_mobiles varchar(500) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ;


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS tb_tc_warn_config;

