--// add node coupon 
-- Migration SQL that makes the change goes here.

CREATE TABLE `twf_node_coupon` (
  `node_id` bigint(20) NOT NULL COMMENT '节点ID',
  `channel_id` bigint(20) NOT NULL  COMMENT '发送渠道ID',
  `shop_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券ID',
  `preview_customers` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '测试用户',
  `output_control` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '输出控制；1：发送组中发送成功客户和控制组客户 2：全量客户',
  `remark` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券节点配置';


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE if exists twf_node_coupon;

