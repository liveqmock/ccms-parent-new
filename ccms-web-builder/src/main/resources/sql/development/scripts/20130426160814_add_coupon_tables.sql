--// add coupon tables
-- Migration SQL that makes the change goes here.

drop table if exists plt_taobao_shop ;

CREATE TABLE `plt_taobao_shop` (
  `shop_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  `shop_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '店铺名称，即店主用户昵称',
  `shop_type` char(5) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺类型，B天猫店，C淘宝店',
  `order_created_earliest` datetime DEFAULT NULL,
  `order_created_latest` datetime DEFAULT NULL,
  `acookie_visit_date` date DEFAULT NULL,
  PRIMARY KEY (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝店铺信息表';


drop table if exists plt_taobao_coupon ;

CREATE TABLE `plt_taobao_coupon` (
  `coupon_id` bigint(20) NOT NULL,
  `coupon_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '优惠券名',
  `shop_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime DEFAULT NULL COMMENT '生效日期',
  `end_time` datetime NOT NULL COMMENT '截止日期',
  `threshold` int(11) NOT NULL COMMENT '优惠券消费门槛(元)',
  `denomination_id` smallint(6) NOT NULL COMMENT '面额',
  `creater` int(11) DEFAULT NULL,
  `available` tinyint(1) DEFAULT NULL  COMMENT '优惠券是否有效',
  `enable` tinyint(1) DEFAULT NULL COMMENT '优惠券是否启动',
  `remark` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝优惠券信息表';

drop table if exists plt_taobao_coupon_denomination ;
CREATE TABLE `plt_taobao_coupon_denomination`(
  `denomination_id` smallint(6) NOT NULL COMMENT '面额ID',
  `denomination_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '面额名称',
  `denomination_value` smallint(6) DEFAULT NULL COMMENT '面额值',
  PRIMARY KEY (`denomination_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝优惠券面额字典表';

INSERT INTO plt_taobao_coupon_denomination VALUES (1, '3元', 3);
INSERT INTO plt_taobao_coupon_denomination VALUES (2, '5元', 5);
INSERT INTO plt_taobao_coupon_denomination VALUES (3, '10元', 10);
INSERT INTO plt_taobao_coupon_denomination VALUES (4, '20元', 20);
INSERT INTO plt_taobao_coupon_denomination VALUES (5, '50元', 50);
INSERT INTO plt_taobao_coupon_denomination VALUES (6, '100元', 100);




--//@UNDO
-- SQL to undo the change goes here.



