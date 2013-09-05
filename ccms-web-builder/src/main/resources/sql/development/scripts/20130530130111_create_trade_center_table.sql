--// create trade center table
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_care_config (
  pkid int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  updated datetime NOT NULL COMMENT '更新时间',
  created datetime NOT NULL COMMENT '创建时间',
  care_type int(11) DEFAULT NULL COMMENT '关怀类型',
  start_date date DEFAULT NULL COMMENT '订单范围开始时间',
  end_date date DEFAULT NULL COMMENT '订单范围结束时间',
  date_type int(11) DEFAULT NULL COMMENT '订单范围类型',
  care_start_time datetime DEFAULT NULL COMMENT '关怀时间开始时间',
  care_end_time datetime DEFAULT NULL COMMENT '关怀时间结束时间',
  care_status int(11) DEFAULT NULL COMMENT '关怀状态',
  notify_option int(1) DEFAULT NULL COMMENT '关怀选项,关怀选项：0:超出时间不催付,1:超出时间次日催付',
  filter_condition varchar(20) DEFAULT NULL COMMENT '过滤条件',
  member_grade varchar(20) DEFAULT NULL COMMENT '会员等级',
  order_minamount decimal(12,2) DEFAULT NULL COMMENT '催付订单最小金额',
  order_maxamount decimal(12,2) DEFAULT NULL COMMENT '催付订单最大金额',
  goods varchar(300) DEFAULT NULL COMMENT '商品',
  exclude_goods int(11) DEFAULT NULL COMMENT '排除商品标识',
  include_cheap int(1) DEFAULT NULL COMMENT '聚划算1:是，0：否',
  sms_content varchar(500) DEFAULT NULL COMMENT '短信内容',
  gateway_id int(11) DEFAULT NULL COMMENT '通道id',
  is_open int(1) DEFAULT NULL COMMENT '是否打开关怀功能,是否打开关怀功能,1:开,0:关',
  is_switch int(11) DEFAULT NULL COMMENT '是否关闭,是否关闭，1:开，0:关闭',
  user_name varchar(30) DEFAULT NULL COMMENT 'CCMS用户',
  plat_name varchar(10) DEFAULT NULL COMMENT '平台',
  op_user varchar(10) DEFAULT NULL COMMENT '操作人''',
  dp_id varchar(50) DEFAULT '' COMMENT '用户店铺',
  assess_key varchar(300) DEFAULT NULL COMMENT '评价关键字（仅评价使用）',
  assess_option int(2) DEFAULT NULL COMMENT '评价选项（仅评价使用）',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关怀配置表';

CREATE TABLE tb_tc_care_status (
  tid varchar(50) NOT NULL COMMENT '主键',
  updated datetime NOT NULL COMMENT '数据更新时间',
  created datetime NOT NULL COMMENT '数据创建时间',
  order_care_status smallint(6) DEFAULT '0' COMMENT '下单关怀状态,0：默认，1：已催付，2：次日催付',
  shipment_care_status smallint(6) DEFAULT '0' COMMENT '发货通知状态,0：默认，1：已催付，2：次日催付',
  arrive_care_status smallint(6) DEFAULT '0' COMMENT '同城通知状态,0：默认，1：已催付，2：次日催付',
  delivery_care_status smallint(6) DEFAULT '0' COMMENT '派件通知状态,0：默认，1：已催付，2：次日催付',
  sign_care_status smallint(6) DEFAULT '0' COMMENT '签收通知状态,0：默认，1：已催付，2：次日催付',
  refund_care_status smallint(6) DEFAULT '0' COMMENT '退款关怀状态,0：默认，1：已催付，2：次日催付',
  confirm_care_status smallint(6) DEFAULT '0' COMMENT '确认收货关怀状态,0：默认，1：已催付，2：次日催付',
  assess_care_status smallint(6) DEFAULT '0' COMMENT '评价关怀状态,0：默认，1：已催付，2：次日催付',
  PRIMARY KEY (tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关怀状态表';

CREATE TABLE tb_tc_config_log (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  type int(11) DEFAULT NULL COMMENT '催付类： 1：自动催付 2：预关闭催付 3：聚划算催付 关怀类：4：下单关怀  5：发货通知 6：同城通知 7：派件通知 8：签收通知 9 ：确认收货关怀 10：评价关怀 告警类： 11：退款告警 12：中差评告警',
  op_user varchar(50) DEFAULT NULL COMMENT '操作人',
  dp_id varchar(50) DEFAULT NULL COMMENT '店铺id',
  content varchar(800) DEFAULT NULL COMMENT '配置内容',
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置日志表';

CREATE TABLE tb_tc_dict (
  pkid int(11) NOT NULL AUTO_INCREMENT,
  type smallint(6) NOT NULL COMMENT '字典类型',
  code smallint(6) NOT NULL COMMENT '字典代码',
  name varchar(20) NOT NULL COMMENT '字典名称',
  is_valid smallint(6) NOT NULL DEFAULT '0' COMMENT '是否有效',
  px smallint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

CREATE TABLE tb_tc_order_filter (
  pkid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  type int(11) DEFAULT NULL COMMENT '催付关怀类型：-1:表示付款 1:自动催付 2：预关闭催付 3：聚划算催付',
  dp_id varchar(50) DEFAULT NULL COMMENT '店铺Id',
  customerno varchar(50) DEFAULT NULL COMMENT '客户Id',
  tid varchar(50) DEFAULT NULL COMMENT '订单ID',
  mobile varchar(11) DEFAULT NULL COMMENT '手机号码',
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催付关怀过滤表';

CREATE TABLE tb_tc_send_log (
  pkid int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  updated datetime NOT NULL COMMENT '数据更新时间',
  created datetime NOT NULL COMMENT '数据创建时间',
  tid varchar(50) DEFAULT NULL COMMENT '订单ID',
  dp_id varchar(50) DEFAULT NULL COMMENT '店铺ID',
  buyer_nick varchar(50) DEFAULT NULL COMMENT '买家昵称',
  trade_created varchar(20) DEFAULT NULL COMMENT '订单创建时间',
  sms_content varchar(200) DEFAULT NULL COMMENT '短信内容',
  mobile varchar(20) DEFAULT NULL COMMENT '手机号码',
  send_user varchar(50) DEFAULT NULL COMMENT '发送者',
  type int(11) DEFAULT NULL COMMENT '发送类型',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送记录表';

CREATE TABLE tb_tc_taobao_order (
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  customerno varchar(50) COLLATE utf8_bin NOT NULL COMMENT '客户ID',
  created datetime NOT NULL COMMENT '交易创建时间',
  endtime datetime DEFAULT NULL COMMENT '交易结束时间',
  status varchar(50) COLLATE utf8_bin NOT NULL COMMENT '交易状态,TRADE_NO_CREATE_PAY(没创建支付宝交易),WAIT_BUYER_PAY,WAIT_SELLER_SEND_GOODS,WAIT_BUYER_CONFIRM_GOODS,TRADE_BUYER_SIGNED(买家已签收,货到付款专用),TRADE_FINISHED,TRADE_CLOSED(付款以后用户退款成功，交易自动关闭),TRADE_CLOSED_BY_TAOBAO',
  trade_from varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)',
  pay_time datetime DEFAULT NULL COMMENT '付款时间',
  consign_time datetime DEFAULT NULL COMMENT '卖家发货时间',
  modified datetime DEFAULT NULL COMMENT '订单修改时间',
  payment decimal(12,2) NOT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  receiver_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的姓名',
  receiver_state varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的所在省份',
  receiver_city varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的所在城市',
  receiver_district varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的所在地区',
  receiver_address varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的详细地址',
  receiver_zip varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的邮编',
  receiver_mobile varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的手机号码',
  receiver_phone varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人的电话号码',
  num int(11) DEFAULT NULL COMMENT '商品数量总计',
  buyer_message varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
  seller_memo varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家备注',
  seller_flag int(11) DEFAULT NULL COMMENT '卖家备注旗帜',
  PRIMARY KEY (tid),
  KEY idx_tb_tc_taobao_order_dp_id (dp_id) USING BTREE,
  KEY idx_tb_tc_taobao_order_customerno (customerno) USING BTREE,
  KEY idx_tb_tc_taobao_order_pay_time (pay_time) USING BTREE,
  KEY idx_tb_tc_taobao_order_consign_time (consign_time) USING BTREE,
  KEY idx_tb_tc_taobao_order_created (created,customerno,dp_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';

CREATE TABLE tb_tc_taobao_order_item (
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
  created datetime NOT NULL COMMENT '交易创建时间',
  endtime datetime DEFAULT NULL COMMENT '交易结束时间',
  trade_from varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)',
  type varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...',
  pay_time datetime DEFAULT NULL COMMENT '付款时间',
  consign_time datetime DEFAULT NULL COMMENT '卖家发货时间',
  refund_status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '退款状态。 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)',
  refund_fee decimal(12,2) DEFAULT NULL COMMENT '退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  title varchar(60) COLLATE utf8_bin NOT NULL COMMENT '商品标题',
  modified datetime DEFAULT NULL COMMENT '订单修改时间（冗余字段）',
  pic_path varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT ' 商品图片URl',
  PRIMARY KEY (oid),
  KEY idx_tb_tc_taobao_order_item_tid (tid) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_dp_id (dp_id) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_num_iid (num_iid) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_created (created) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_pay_time (pay_time) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_consign_time (consign_time) USING BTREE,
  KEY idx_tb_tc_taobao_order_item_modified (modified) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单明细表（子订单/商品明细）';

CREATE TABLE tb_tc_taobao_shipping (
  order_code varchar(50) COLLATE utf8_bin NOT NULL COMMENT '物流订单编号',
  tid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '交易ID',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  is_quick_cod_order tinyint(1) DEFAULT NULL,
  seller_nick varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家昵称',
  buyer_nick varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家昵称',
  out_sid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '运单号.具体一个物流公司的运单号码.',
  receiver_mobile varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '收件人手机号码',
  receiver_name varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收件人姓名',
  receiver_phone varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收件人电话',
  status char(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流订单状态,CREATED(已创建) RECREATED(重新创建) CANCELLED(订单已取消) CLOSED(订单关闭) SENDING(等候发送给物流公司) ACCEPTING(已发送给物流公司,等待接单) ACCEPTED(物流公司已接单) REJECTED(物流公司不接单) PICK_UP(物流公司揽收成功) PICK_UP_FAILED(物流公司揽收失败) LOST(物流公司丢单) REJECTED_BY_RECEIVER(拒签) ACCEPTED_BY_RECEIVER(已签收)',
  company_name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '物流公司名称',
  is_success tinyint(1) DEFAULT NULL,
  created datetime DEFAULT NULL COMMENT '运单创建时间',
  modified datetime DEFAULT NULL COMMENT '运单修改时间',
  transit_step_info text COLLATE utf8_bin COMMENT '流转信息文件路径',
  shipping_status int(11) DEFAULT NULL COMMENT '流转状态：1:到同城，2、派件；3：已签收',
  signed_time datetime DEFAULT NULL COMMENT '签收时间',
  arrived_time datetime DEFAULT NULL COMMENT '到达同城时间',
  delivery_time datetime DEFAULT NULL COMMENT '派件时间',
  PRIMARY KEY (order_code),
  KEY idx_tb_tc_taobao_shipping_dp_id (dp_id) USING BTREE,
  KEY idx_tb_tc_taobao_shipping_tid (tid) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='淘宝物流运单';


CREATE TABLE tb_tc_urpay_config (
  pkid int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  created datetime DEFAULT NULL COMMENT '数据创建时间',
  updated datetime DEFAULT NULL COMMENT '数据更新时间',
  urpay_type int(2) DEFAULT NULL COMMENT '催付类型(1:自动催付、2:预关闭催付、3:聚划算催付)',
  task_type int(2) DEFAULT NULL COMMENT '1：实时催付，2：定时催付',
  start_date date DEFAULT NULL COMMENT '订单范围开始时间',
  end_date date DEFAULT NULL COMMENT '订单范围结束时间',
  date_type int(11) DEFAULT NULL COMMENT '订单范围类型(日期类型：1 自定义类型，0非自定义类型)',
  urpay_start_time datetime DEFAULT NULL COMMENT '催付时间开始时间',
  urpay_end_time datetime DEFAULT NULL COMMENT '催付时间结束时间',
  fix_urpay_time varchar(100) DEFAULT NULL COMMENT '定时催付时间',
  offset int(11) DEFAULT NULL COMMENT '催付时间间隔',
  notify_option int(1) DEFAULT NULL COMMENT '催付选项',
  order_minamount decimal(12,2) DEFAULT NULL COMMENT '催付订单最小金额',
  order_maxamount decimal(12,2) DEFAULT NULL COMMENT '催付订单最大金额',
  is_open int(1) DEFAULT NULL COMMENT '是否打开催付功能(1:开,0:关)',
  is_switch int(1) DEFAULT NULL COMMENT '是否关闭(1:开，0:关闭)',
  user_name varchar(30) DEFAULT NULL COMMENT 'CCMS用户',
  plat_name varchar(10) DEFAULT NULL COMMENT '平台名称',
  op_user varchar(10) DEFAULT NULL COMMENT '操作人',
  dp_id varchar(50) DEFAULT NULL COMMENT '店铺id',
  filter_condition varchar(20) DEFAULT NULL COMMENT '过滤条件',
  member_grade varchar(20) DEFAULT NULL COMMENT '会员等级',
  goods varchar(300) DEFAULT NULL COMMENT '商品',
  exclude_goods int(11) DEFAULT NULL COMMENT '排除商品标识',
  include_cheap int(1) DEFAULT NULL COMMENT '是否包含聚划算(1:是，0：否)',
  sms_content varchar(500) DEFAULT NULL COMMENT '短信内容',
  gateway_id int(3) DEFAULT NULL COMMENT '通道id',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催付配置表';

CREATE TABLE tb_tc_urpay_status (
  tid varchar(50) NOT NULL COMMENT '主键',
  updated datetime NOT NULL COMMENT '数据更新时间',
  created datetime NOT NULL COMMENT '数据创建时间',
  auto_urpay_status smallint(6) DEFAULT '0' COMMENT '自动催付状态，	0：默认，1：已催付，2：次日催付',
  auto_urpay_thread varchar(20) DEFAULT NULL COMMENT '自动催付扫描线程,记录扫描线程ID，时间戳',
  close_urpay_status smallint(6) DEFAULT '0' COMMENT '预关闭催付状态,0：默认，1：已催付，2：次日催付',
  close_urpay_thread varchar(20) DEFAULT NULL COMMENT '预关闭催付扫描线程,记录扫描线程ID，时间戳。',
  cheap_urpay_status smallint(6) DEFAULT '0' COMMENT '聚划算催付状态,0：默认，1：已催付，2：次日催付',
  cheap_urpay_thread varchar(20) DEFAULT NULL COMMENT '聚划算催付扫描线程，记录扫描线程ID，时间戳。',
  manual_urpay_status smallint(6) DEFAULT '0' COMMENT '手动催付状态;默认0，催付1',
  PRIMARY KEY (tid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催付状态表';

CREATE TABLE tb_tc_urpay_summary (
  pkid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  urpay_type int(11) DEFAULT NULL COMMENT '催付类型: 1：自动催付 2：预关闭催付 3：聚划算催付',
  dp_id varchar(50) DEFAULT NULL COMMENT '店铺ID',
  urpay_date date DEFAULT NULL COMMENT '催付日期',
  order_num int(11) DEFAULT NULL COMMENT '催付订单数',
  response_num int(11) DEFAULT NULL COMMENT '催付响应订单数据',
  response_amount decimal(12,2) DEFAULT NULL COMMENT '催付响应金额',
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催付统计表';


CREATE TABLE tb_tc_service_staff_interaction (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  service_staff_id varchar(50) NOT NULL COMMENT '客服昵称',
  dp_id varchar(50) NOT NULL COMMENT '店铺id',
  tid varchar(50) NOT NULL COMMENT ' 交易id',
  deal_date date NOT NULL COMMENT '处理日期',
  created datetime DEFAULT NULL,
  updated datetime DEFAULT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客服交互信息';


CREATE TABLE tb_tc_buyer_interaction_statistic (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  customerno varchar(50) NOT NULL COMMENT '用户昵称',
  dp_id varchar(50) NOT NULL COMMENT '店铺id',
  trade_count int(11) NOT NULL COMMENT '交易总数',
  trade_no_payed_count int(11) NOT NULL COMMENT '未付款交易数',
  trade_payed_count int(11) NOT NULL COMMENT '已付款交易数',
  trade_close_count int(11) NOT NULL COMMENT '已关闭交易数',
  urpay_count int(11) NOT NULL COMMENT '催付次数',
  deal_date date NOT NULL COMMENT '处理日期',
  increment_statistic_start_time datetime DEFAULT NULL COMMENT '增量统计开始时间',
  increment_statistic_end_time datetime DEFAULT NULL COMMENT '增量统计结束时间',
  created datetime NOT NULL,
  updated datetime NOT NULL,
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户交互信息统计表';


CREATE TABLE plt_taobao_chat_log (
  pkid bigint(20) NOT NULL AUTO_INCREMENT,
  dp_id varchar(50) NOT NULL COMMENT '店铺id',
  service_staff_id varchar(50) NOT NULL DEFAULT '' COMMENT '客服人员id',
  buyer_nick varchar(50) NOT NULL COMMENT '访客ID对应的淘宝昵称',
  direction tinyint(4) NOT NULL COMMENT '消息方向 （卖家 --> 买家  or   买家 --> 卖家）',
  type tinyint(4) NOT NULL COMMENT '消息类型',
  content varchar(200) DEFAULT NULL COMMENT ' 消息内容 （卖家 --> 买家）',
  url_list varchar(200) DEFAULT NULL COMMENT '消息中商品url列表 ',
  num_iids varchar(200) DEFAULT NULL COMMENT '消息中商品id列表 ',
  word_lists varchar(200) DEFAULT NULL COMMENT '消息中关键词列表：{关键词：出现次数} ',
  length int(11) DEFAULT NULL COMMENT '消息长度 （买家 --> 卖家）',
  time datetime DEFAULT NULL COMMENT '消息时间',
  PRIMARY KEY (pkid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='旺旺聊天记录模糊查询';

--//@UNDO
-- SQL to undo the change goes here.

drop table tb_tc_care_config;
drop table tb_tc_care_status;
drop table tb_tc_config_log;
drop table tb_tc_dict;
drop table tb_tc_order_filter;
drop table tb_tc_send_log;
drop table tb_tc_taobao_order;
drop table tb_tc_taobao_order_item;
drop table tb_tc_taobao_shipping;
drop table tb_tc_urpay_config;
drop table tb_tc_urpay_status;
drop table tb_tc_urpay_summary;
drop table tb_tc_service_staff_interaction;
drop table tb_tc_buyer_interaction_statistic;
drop table plt_taobao_chat_log;


