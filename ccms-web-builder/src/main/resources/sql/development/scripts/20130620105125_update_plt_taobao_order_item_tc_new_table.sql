--// update plt_taobao_order_item_tc new table
-- Migration SQL that makes the change goes here.

drop table if exists plt_taobao_order_item_tc;
CREATE TABLE plt_taobao_order_item_tc (
  oid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '子订单编号',
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单编号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  total_fee decimal(12,2) DEFAULT NULL COMMENT '应付金额',
  discount_fee decimal(12,2) DEFAULT NULL COMMENT '订单优惠金额',
  adjust_fee decimal(12,2) DEFAULT NULL COMMENT '手工调整金额',
  payment decimal(12,2) DEFAULT NULL COMMENT '子订单实付金额',
  price decimal(12,2) DEFAULT NULL COMMENT '商品价格',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  num int(11) DEFAULT NULL COMMENT '购买数量',
  num_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品数字ID',
  refund_status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '退款状态。 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)',
  refund_fee decimal(12,2) DEFAULT NULL COMMENT '退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  title varchar(60) COLLATE utf8_bin NOT NULL COMMENT '商品标题',
  pic_path varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT ' 商品图片URl',
  outer_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家外部编码',
  sku_properties_name varchar(50) COLLATE utf8_bin DEFAULT NULL,
  sku_id varchar(200) COLLATE utf8_bin DEFAULT NULL,
  trade_modified datetime DEFAULT NULL COMMENT '主订单修改时间',
  PRIMARY KEY (oid),
  KEY idx_tb_tc_taobao_order_item_tid (tid) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_dp_id (dp_id) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_num_iid (num_iid) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单明细表（子订单/商品明细）';


--//@UNDO
-- SQL to undo the change goes here.
drop table if exists plt_taobao_order_item_tc;
CREATE TABLE plt_taobao_order_item_tc (
  oid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '子订单编号',
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单编号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  total_fee decimal(12,2) DEFAULT NULL COMMENT '应付金额',
  discount_fee decimal(12,2) DEFAULT NULL COMMENT '订单优惠金额',
  adjust_fee decimal(12,2) DEFAULT NULL COMMENT '手工调整金额',
  payment decimal(12,2) DEFAULT NULL COMMENT '子订单实付金额',
  price decimal(12,2) DEFAULT NULL COMMENT '商品价格',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  num int(11) DEFAULT NULL COMMENT '购买数量',
  num_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品数字ID',
  refund_status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '退款状态。 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)',
  refund_fee decimal(12,2) DEFAULT NULL COMMENT '退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  title varchar(60) COLLATE utf8_bin NOT NULL COMMENT '商品标题',
  pic_path varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT ' 商品图片URl',
  outer_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家外部编码',
  sku_properties_name varchar(50) COLLATE utf8_bin DEFAULT NULL,
  sku_id varchar(200) COLLATE utf8_bin DEFAULT NULL,
  trade_modified datetime DEFAULT NULL COMMENT '主订单修改时间',
  PRIMARY KEY (oid),
  KEY idx_tb_tc_taobao_order_item_tid (tid) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_dp_id (dp_id) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_num_iid (num_iid) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单明细表（子订单/商品明细）';

