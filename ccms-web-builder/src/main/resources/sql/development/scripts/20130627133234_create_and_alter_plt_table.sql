--// create and alter plt table
-- Migration SQL that makes the change goes here.
CREATE TABLE IF NOT EXISTS plt_taobao_chatpeer (
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  service_staff_id varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '客服人员id',
  chat_time datetime NOT NULL COMMENT '聊天时间',
  buyer_nick varchar(50) COLLATE utf8_bin NOT NULL COMMENT '访客ID对应的淘宝昵称',
  direction varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '表示消息方向 0:from_id->to_id 1:to_id->from_id',
  chat_type varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'type & 1 ：自动回复 type & 2 : 陌生人消息（等于0是好友消息） type & 4 : 广播消息 type & 8 : 最近联系人陌生人消息 type & 16 : 离线消息 type & 32 : 子账号转发系统消息',
  content text COLLATE utf8_bin COMMENT '当direction=0有效，完整消息内容',
  url_lists text COLLATE utf8_bin COMMENT '当direction=1有效，url列表',
  word_lists text COLLATE utf8_bin COMMENT '当direction=1有效，（关键词，数量）列表',
  length int(10) DEFAULT '0' COMMENT '当direction=1有效，消息长度',
  PRIMARY KEY (dp_id,service_staff_id,chat_time,buyer_nick)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='旺旺聊天对象表';

--  Table: plt_taobao_group_member
CREATE TABLE  IF NOT EXISTS plt_taobao_group_member (
  group_name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '组名称',
  member_list text COLLATE utf8_bin DEFAULT NULL COMMENT '组成员列表，逗号分隔',
  group_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '组编号',
  manager_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '管理员ID',
  PRIMARY KEY (group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='旺旺客服表';

-- plt_taobao_favorite  
CREATE TABLE  IF NOT EXISTS plt_taobao_favorite (
  type varchar(2) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '收藏对象类型：0为店铺；1为宝贝',
  obj_id varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '收藏对象id，如商品ID，店铺ID',
  user_id varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '加密后的收藏者ID',
  status varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '收藏对象状态 若为宝贝 code=-1:小二删除商品 code= 0, 正常状态. code=101:卖家下架商品. code=102:小二下架商品.',
  src varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收藏来源(无具体URL)',
  created datetime DEFAULT NULL COMMENT '创建时间',
  modified datetime DEFAULT NULL COMMENT '修改时间',
  dp_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (type,obj_id,user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='收藏夹表';

-- plt_taobao_shopping_cart
CREATE TABLE  IF NOT EXISTS plt_taobao_shopping_cart (
  num_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品ID',
  sku_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'SKU ID',
  user_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '加密后的收藏者ID,可与其他api的buyer_id关联 ',
  num int(10) DEFAULT NULL COMMENT '购买数量',
  status varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '-1、表示因为下单被删除. -2、用户以访客身份浏览商品并添加到访客购物车后，以会员身份再次登陆，',
  created datetime DEFAULT NULL COMMENT '创建时间',
  modified datetime DEFAULT NULL COMMENT '最后修改时间',
  dp_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (num_iid,sku_id,user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='购物车表';

-- plt_taobao_product_skus_prop  
CREATE TABLE IF NOT EXISTS plt_taobao_product_skus_prop (
  num_iid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '商品ID',
  sku_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'sku的id',
  pid varchar(150) COLLATE utf8_bin NOT NULL COMMENT '属性ID',
  vid varchar(150) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值ID',
  pidname varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '属性名',
  vidname varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值名',
  PRIMARY KEY (num_iid,sku_id,pid,vid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='sku的prop长表';

-- plt_taobao_product_skus
CREATE TABLE IF NOT EXISTS plt_taobao_product_skus (
  properties_name varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku所对应的销售属性的中文名字串',
  sku_spec_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '表示SKu上的产品规格信息',
  with_hold_quantity int(10)  DEFAULT '0' COMMENT '商品在付款减库存的状态下，该sku上未付款的订单数量',
  sku_delivery_time datetime DEFAULT NULL COMMENT 'sku级别发货时间',
  change_prop varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '基础色数据',
  sku_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'sku的id',
  dp_id varchar(50) COLLATE utf8_bin DEFAULT NULL,
  iid varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku所属商品id',
  num_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku所属商品数字id',
  properties varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku的销售属性组合字符串',
  quantity int(10) DEFAULT '0' COMMENT '属于这个sku的商品的数量',
  price decimal(12,2) DEFAULT NULL COMMENT '属于这个sku的商品的价格 ',
  outer_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家设置的外部id',
  created datetime DEFAULT NULL COMMENT 'sku创建日期 时间格式：yyyy-MM-dd HH:mm:ss',
  modified datetime DEFAULT NULL COMMENT 'sku最后修改日期 时间格式：yyyy-MM-dd HH:mm:ss',
  status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku状态。 normal:正常 ；delete:删除',
  PRIMARY KEY (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='sku表';

--    Table: plt_taobao_product_seller_cat
 CREATE TABLE  IF NOT EXISTS plt_taobao_product_seller_cat (
  num_iid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '商品编号',
  seller_cid varchar(40) COLLATE utf8_bin NOT NULL COMMENT '卖家自定义类目编号',
  name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家自定义类目名称',
  type varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺类目类型：可选值：manual_type：手动分类，new_type：新品上价， tree_type：二三级类目树 ，property_type：属性叶子类目树， brand_type：品牌推广',
  PRIMARY KEY (num_iid,seller_cid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品对应的卖家自定义类目';

--  Table: plt_taobao_product_prop
CREATE TABLE  IF NOT EXISTS plt_taobao_product_prop (
  num_iid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '商品ID',
  pid varchar(150) COLLATE utf8_bin NOT NULL COMMENT '属性ID',
  prop_name varchar(150) COLLATE utf8_bin DEFAULT NULL COMMENT '属性名',
  vid varchar(150) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值ID',
  name varchar(150) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值名称',
  name_alias varchar(150) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值别名',
  is_input_prop tinyint(1) DEFAULT NULL,
  PRIMARY KEY (num_iid,pid,vid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品对应的商品属性';

--    Table: plt_taobao_order_item_channel
CREATE TABLE  IF NOT EXISTS plt_taobao_order_item_channel (
  oid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '子订单编号',
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单编号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  customerno varchar(50) COLLATE utf8_bin NOT NULL COMMENT '客户ID',
  total_fee decimal(12,2) DEFAULT NULL COMMENT '应付金额',
  discount_fee decimal(12,2) DEFAULT NULL COMMENT '订单优惠金额',
  adjust_fee decimal(12,2) DEFAULT NULL COMMENT '手工调整金额',
  payment decimal(12,2) DEFAULT NULL COMMENT '子订单实付金额',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单状态',
  num int(11) DEFAULT NULL COMMENT '购买数量',
  num_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品数字ID',
  created datetime DEFAULT NULL COMMENT '交易创建时间',
  endtime datetime DEFAULT NULL COMMENT '交易结束时间',
  trade_from varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)',
  type varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...',
  pay_time datetime DEFAULT NULL COMMENT '付款时间',
  consign_time datetime DEFAULT NULL COMMENT '卖家发货时间',
  refund_status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '退款状态。 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)',
  refund_fee decimal(12,2) DEFAULT NULL COMMENT '退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  ccms_order_status smallint(6) NOT NULL COMMENT 'CCMS自定义的订单状态',
  title varchar(255) COLLATE utf8_bin NOT NULL COMMENT '商品标题',
  modified datetime DEFAULT NULL COMMENT '订单修改时间（冗余字段）',
  price decimal(12,2) DEFAULT NULL COMMENT '商品价格',
  sku_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品的最小库存单位Sku的id',
  sku_properties_name text COLLATE utf8_bin DEFAULT NULL COMMENT 'SKU的值',
  cid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易商品对应的类目ID',
  refund_id varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '最近退款ID',
  item_meal_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐ID',
  item_meal_name varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐的值',
  outer_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家外部编码(可与商家外部系统对接)',
  pic_path varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片的绝对路径',
  PRIMARY KEY (oid),
  KEY idx_plt_taobao_order_item_channel_tid (tid),
  KEY idx_plt_taobao_order_item_channel_num_iid (num_iid),
  KEY idx_plt_taobao_order_item_channel_created (created),
  KEY idx_plt_taobao_order_item_channel_pay_time (pay_time),
  KEY idx_plt_taobao_order_item_channel_consign_time (consign_time),
  KEY idx_plt_taobao_order_item_channel_modified (modified),
  KEY idx_plt_taobao_order_item_channel_dp_id (dp_id),
  KEY idx_plt_taobao_order_item_channel_ccms_order_status (ccms_order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单明细表（子订单/商品明细）';

--  Table: plt_taobao_order_channel
CREATE TABLE  IF NOT EXISTS plt_taobao_order_channel (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  customerno varchar(50) COLLATE utf8_bin NOT NULL COMMENT '客户ID',
  created datetime DEFAULT NULL COMMENT '交易创建时间',
  endtime datetime DEFAULT NULL COMMENT '交易结束时间',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '交易状态,TRADE_NO_CREATE_PAY(没创建支付宝交易),WAIT_BUYER_PAY,WAIT_SELLER_SEND_GOODS,WAIT_BUYER_CONFIRM_GOODS,TRADE_BUYER_SIGNED(买家已签收,货到付款专用),TRADE_FINISHED,TRADE_CLOSED(付款以后用户退款成功，交易自动关闭),TRADE_CLOSED_BY_TAOBAO',
  step_trade_status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '万人团订单状态 FRONT_NOPAID_FINAL_NOPAID(未付订金),FRONT_PAID_FINAL_NOPAID(已付订金未付尾款)',
  trade_from varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)',
  type varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...',
  pay_time datetime DEFAULT NULL COMMENT '付款时间',
  total_fee decimal(12,2) DEFAULT NULL COMMENT '商品金额',
  post_fee decimal(12,2) DEFAULT NULL COMMENT '邮费',
  consign_time datetime DEFAULT NULL COMMENT '卖家发货时间',
  ccms_order_status smallint(6) NOT NULL,
  modified datetime DEFAULT NULL COMMENT '订单修改时间',
  alipay_no varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '支付宝交易号，如：2009112081173831',
  payment decimal(12,2) NOT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  discount_fee decimal(12,2) DEFAULT NULL COMMENT '系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分',
  point_fee decimal(12,2) DEFAULT NULL COMMENT '买家使用积分。格式:100;单位:个.',
  real_point_fee decimal(12,2) DEFAULT NULL COMMENT '买家实际使用积分（扣除部分退款使用的积分）。格式:100;单位:个',
  shipping_type varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：ems, express, post, free, virtual。',
  buyer_cod_fee decimal(12,2) DEFAULT NULL COMMENT '买家货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分',
  seller_cod_fee decimal(12,2) DEFAULT NULL COMMENT '卖家货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分。卖家不承担服务费的订单：未发货的订单获取服务费为0，发货后就能获取到正确值。',
  express_agency_fee decimal(12,2) DEFAULT NULL COMMENT '快递代收款。精确到2位小数;单位:元。如:212.07，表示:212元7分',
  adjust_fee decimal(12,2) DEFAULT NULL COMMENT '卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样',
  buyer_obtain_point_fee decimal(12,2) DEFAULT NULL COMMENT '买家获得积分,返点的积分。格式:100;单位:个。返点的积分要交易成功之后才能获得。',
  cod_fee decimal(12,2) DEFAULT NULL COMMENT '货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分。',
  cod_status varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '货到付款物流状态。 初始状态 NEW_CREATED,接单成功 CANCELED,接单失败 REJECTED_BY_COMPANY,接单超时 RECIEVE_TIMEOUT,揽收成功 TAKEN_IN_SUCCESS,揽收失败 TAKEN_IN_FAILED,揽收超时 RECIEVE_TIMEOUT,签收成功 SIGN_IN,签收失败 REJECTED_BY_OTHER_SIDE,订单等待发送给物流公司 WAITING_TO_BE_SENT,用户取消物流订单 CANCELED',
  buyer_alipay_no varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '买家支付宝账号',
  receiver_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的姓名',
  receiver_state varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的所在省份',
  receiver_city varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的所在城市',
  receiver_district varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的所在地区',
  receiver_address varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的详细地址',
  receiver_zip varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的邮编',
  receiver_mobile varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的手机号码',
  receiver_phone varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的电话号码',
  buyer_email varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '买家邮件地址',
  commission_fee decimal(12,2) DEFAULT NULL COMMENT '交易佣金。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  refund_fee decimal(12,2) DEFAULT NULL COMMENT '子订单的退款金额合计',
  num int(11) DEFAULT NULL COMMENT '商品数量总计',
  received_payment decimal(12,2) DEFAULT NULL COMMENT '卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  buyer_memo varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '买家备注',
  seller_memo varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家备注',
  seller_flag varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家备注旗帜',
  PRIMARY KEY (tid),
  KEY idx_plt_taobao_order_channel_dp_id (dp_id,customerno,created,endtime,pay_time,modified,consign_time,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,ccms_order_status),
  KEY idx_plt_taobao_order_channel_created (created,customerno,endtime,pay_time,modified,consign_time,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,dp_id,ccms_order_status),
  KEY idx_plt_taobao_order_channel_endtime (endtime,customerno,created,pay_time,modified,consign_time,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,dp_id,ccms_order_status),
  KEY idx_plt_taobao_order_channel_pay_time (pay_time,customerno,created,endtime,modified,consign_time,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,dp_id,ccms_order_status),
  KEY idx_plt_taobao_order_channel_modified (modified,customerno,created,endtime,pay_time,consign_time,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,dp_id,ccms_order_status),
  KEY idx_plt_taobao_order_channel_consign_time (consign_time,customerno,created,endtime,pay_time,modified,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,dp_id,ccms_order_status),
  KEY idx_plt_taobao_order_channel_customerno (customerno,created,consign_time,endtime,pay_time,modified,payment,status,type,total_fee,refund_fee,num,received_payment,trade_from,dp_id,ccms_order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';

--    Table: plt_taobao_order
alter table plt_taobao_order add column buyer_memo varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '买家备注' ;
alter table plt_taobao_order add column seller_memo varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家备注' ;
alter table plt_taobao_order add column seller_flag varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家备注旗帜' ;

--    Table: plt_taobao_order_item
alter table plt_taobao_order_item add column pic_path varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片的绝对路径' ;
alter table plt_taobao_order_item add column price decimal(12,2) DEFAULT NULL COMMENT '商品价格' ;
alter table plt_taobao_order_item add column sku_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品的最小库存单位Sku的id' ;
alter table plt_taobao_order_item add column sku_properties_name text COLLATE utf8_bin DEFAULT NULL COMMENT 'SKU的值' ;
alter table plt_taobao_order_item add column cid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易商品对应的类目ID' ;
alter table plt_taobao_order_item add column refund_id varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '最近退款ID' ;
alter table plt_taobao_order_item add column item_meal_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐ID' ;
alter table plt_taobao_order_item add column item_meal_name varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐的值' ;
alter table plt_taobao_order_item add column outer_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家外部编码(可与商家外部系统对接)' ;


--//@UNDO
-- SQL to undo the change goes here.


