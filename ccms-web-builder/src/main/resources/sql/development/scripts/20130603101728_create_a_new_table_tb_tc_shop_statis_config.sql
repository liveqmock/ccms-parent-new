--// create_a_new_table_tb_tc_shop_statis_config
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_shop_statis_config (
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  recently_order_statis_time datetime DEFAULT NULL COMMENT '最近订单统计时间',
  recently_urpay_statis_time datetime DEFAULT NULL COMMENT '最近催付统计时间',
  statis_interval int(11) DEFAULT NULL COMMENT '统计间隔，秒',
  created datetime DEFAULT NULL,
  updated datetime DEFAULT NULL,
  PRIMARY KEY (dp_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--//@UNDO
-- SQL to undo the change goes here.
drop table tb_tc_shop_statis_config;

