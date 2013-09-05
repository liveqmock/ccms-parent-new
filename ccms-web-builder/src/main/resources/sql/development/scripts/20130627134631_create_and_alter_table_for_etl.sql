--// create and alter table for etl
-- Migration SQL that makes the change goes here.
CREATE TABLE IF NOT EXISTS plt_taobao_refund (
  refund_id VARCHAR(200) NOT NULL COMMENT '退款单号' ,
  tid VARCHAR(50) NULL COMMENT '淘宝交易单号' ,
  oid VARCHAR(50) NULL COMMENT '子订单号' ,
  dp_id VARCHAR(50) NULL COMMENT '店铺ID' ,
  total_fee VARCHAR(200) NULL COMMENT '交易总金额' ,
  buyer_nick VARCHAR(50) NULL COMMENT '买家昵称' ,
  seller_nick VARCHAR(50) NULL COMMENT '买家昵称' ,
  created DATETIME NULL COMMENT '退款申请时间' ,
  modified DATETIME NULL COMMENT '更新时间' ,
  order_status VARCHAR(50) NULL COMMENT '退款对应的交易状态' ,
  status VARCHAR(50) NULL COMMENT '退款状态' ,
  good_status VARCHAR(50) NULL COMMENT '货物状态' ,
  has_good_return VARCHAR(200) NULL COMMENT '买家是否需要退货.可选值:true, false' ,
  refund_fee DECIMAL(12,2) NULL COMMENT '退还金额(退还给买家的金额)' ,
  payment VARCHAR(200) NULL COMMENT '支付给卖家的金额(交易总额-退还给买家的金额)' ,
  reason VARCHAR(200) NULL COMMENT '退款原因' ,
  refund_desc VARCHAR(200) NULL COMMENT '退款说明' ,
  title VARCHAR(200) NULL COMMENT '商品标题' ,
  num INT(11) NULL COMMENT '商品购买数量' ,
  company_name VARCHAR(200) NULL COMMENT '物流公司名称' ,
  sid VARCHAR(200) NULL COMMENT '退货运单号' ,
  PRIMARY KEY (refund_id) )
COMMENT = '退款信息表';


drop table if exists plt_taobao_item_seller_cat;
CREATE TABLE plt_taobao_item_seller_cat (
    type varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺类目类型：可选值：manual_type：手动分类，new_type：新品上价， tree_type：二三级类目树 ，property_type：属性叶子类目树， brand_type：品牌推广',
    cid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家自定义类目编号',
    parent_cid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '父类目编号，值等于0：表示此类目为店铺下的一级类目，值不等于0：表示此类目有父类目',
    dp_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺ID',
    name varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '卖家自定义类目名称',
    pic_url varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '链接图片地址',
    sort_order varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '该类目在页面上的排序位置',
    created datetime DEFAULT NULL COMMENT '创建时间。格式：yyyy-MM-dd HH:mm:ss',
    modified datetime DEFAULT NULL COMMENT '修改时间。格式：yyyy-MM-dd HH:mm:ss',
      PRIMARY KEY (cid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='自定义类目维度表';

CREATE TABLE  IF NOT EXISTS plt_taobao_product_onsale (
  num_iid varchar(50) COLLATE utf8_bin NOT NULL COMMENT '商品数字ID',
  detail_url varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品URL',
  title varchar(255) COLLATE utf8_bin NOT NULL COMMENT '商品标题，商品名称',
  created datetime DEFAULT NULL COMMENT 'Item的发布时间',
  is_fenxiao varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '非分销商品：0，代销：1，经销：2',
  cid bigint(20) DEFAULT NULL COMMENT '商品所属的叶子类目 id',
  pic_url varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片地址',
  list_time datetime DEFAULT NULL COMMENT '商品上架时间',
  delist_time datetime DEFAULT NULL COMMENT '下架时间',
  price decimal(12,2) DEFAULT NULL,
  modified datetime DEFAULT NULL COMMENT '修改时间',
  approve_status varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '商品上传后的状态。onsale出售中，instock库中',
  dp_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT '店铺ID',
  outer_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家外部编码',
  seller_cids text COLLATE utf8_bin COMMENT '商品所属的店铺内卖家自定义类目列表',
  props text COLLATE utf8_bin COMMENT '商品属性 格式：pid:vid;pid:vid',
  props_name varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '商品属性名称',
  input_pids varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '用户自行输入的类目属性ID串',
  input_str varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '用户自行输入的子属性名和属性值',
  PRIMARY KEY (num_iid),
  KEY idx_plt_taobao_product_title (title)
)COMMENT='淘宝商品表';


CREATE TABLE  IF NOT EXISTS plt_taobao_sku_onsale (
  properties_name varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku所对应的销售属性的中文名字串',
  sku_spec_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '表示SKu上的产品规格信息',
  with_hold_quantity int(10) DEFAULT '0' COMMENT '商品在付款减库存的状态下，该sku上未付款的订单数量',
  sku_delivery_time datetime DEFAULT NULL COMMENT 'sku级别发货时间',
  change_prop varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '基础色数据',
  sku_id varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'sku的id',
  iid varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku所属商品id',
  num_iid varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku所属商品数字id',
  properties varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku的销售属性组合字符串',
  quantity int(10) DEFAULT '0' COMMENT '属于这个sku的商品的数量',
  price decimal(12,2) DEFAULT NULL COMMENT '属于这个sku的商品的价格 ',
  outer_id varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商家设置的外部id',
  created datetime DEFAULT NULL COMMENT 'sku创建日期 时间格式：yyyy-MM-dd HH:mm:ss',
  modified datetime DEFAULT NULL COMMENT 'sku最后修改日期 时间格式：yyyy-MM-dd HH:mm:ss',
  status varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'sku状态。 normal:正常 ；delete:删除',
  dp_id varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺的唯一标识',
  PRIMARY KEY (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='sku表';

CREATE TABLE  IF NOT EXISTS plt_taobao_shop_cid_relation (
  cid varchar(30) COLLATE utf8_bin NOT NULL COMMENT '类目id',
  dp_id varchar(30) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  PRIMARY KEY (cid,dp_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


ALTER TABLE plt_taobao_shopping_cart
MODIFY COLUMN status  varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL 
COMMENT '-1、表示因为下单被删除. -2、用户以访客身份浏览商品并添加到访客购物车后，以会员身份再次登陆，\r\n则系统会将“访客”购物车与会员购物车合并，此时发现存在重复商品，则会删除其一，状态置为-2. -3、用户主动删除购物车内商品.\r\n-4、 A：商品已下架/删除.B：购物车内有两个相同的宝贝。由于客户端引致的并发请求，系统会作清理，将冗余的置-4\r\n0、 系统错误。1、正常购买状态. ' 
AFTER num;

ALTER TABLE plt_taobao_shopping_cart
DROP PRIMARY KEY,
ADD PRIMARY KEY (num_iid, sku_id, user_id, created);

ALTER TABLE plt_taobao_product  ADD seller_cids text COMMENT '商品所属的店铺内卖家自定义类目列表';
ALTER TABLE plt_taobao_product  ADD props text COMMENT '商品属性 格式：pid:vid;pid:vid';
ALTER TABLE plt_taobao_product  ADD props_name varchar(1000) COMMENT '商品属性名称';
ALTER TABLE plt_taobao_product  ADD input_pids varchar(200) COMMENT '用户自行输入的类目属性ID串';
ALTER TABLE plt_taobao_product  ADD input_str varchar(1000) COMMENT '用户自行输入的子属性名和属性值';

CREATE TABLE  IF NOT EXISTS plt_taobao_shop_cid_relation (
  cid varchar(30) COLLATE utf8_bin NOT NULL COMMENT '类目id',
  dp_id varchar(30) COLLATE utf8_bin NOT NULL COMMENT '店铺id',
  PRIMARY KEY (cid,dp_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


drop table if exists plt_taobao_item_cat;
drop table if exists plt_taobao_item_prop;
drop table if exists plt_taobao_item_prop_val;

CREATE TABLE plt_taobao_item_cat (
  cid varchar(30) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '商品所属类目ID',
  parent_cid varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '父类目ID=0时，代表的是一级的类目',
  name varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '类目名称',
  is_leaf varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '该类目是否没有子类目',
  sort_order varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数',
  feature_list varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Feature对象列表 目前已有的属性： 若Attr_key为 udsaleprop，attr_value为1 则允许卖家在改类目新增自定义销售属性,不然为不允许',
  PRIMARY KEY (cid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品类目';

CREATE TABLE plt_taobao_item_prop (
  cid varchar(60) COLLATE utf8_bin NOT NULL COMMENT '类目ID',
  pid varchar(60) COLLATE utf8_bin NOT NULL COMMENT '属性 ID',
  name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '属性名',
  is_key_prop varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '是否关键属性。可选值:true(是),false(否)',
  is_sale_prop varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '是否销售属性。可选值:true(是),false(否)',
  is_color_prop varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '是否颜色属性。可选值:true(是),false(否)',
  child_template varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '子属性的模板（卖家自行输入属性时需要用到）',
  parent_pid varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '上级属性ID',
  parent_vid varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '上级属性值ID',
  sort_order varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '排列序号。取值范围:大于零的整排列序号。取值范围:大于零的整数',
  is_allow_alias varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '是否允许别名。可选值：true（是），false（否）',
  type varchar(60) COLLATE utf8_bin DEFAULT NULL,
  required varchar(60) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (pid,cid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品属性';

CREATE TABLE plt_taobao_item_prop_val (
  cid varchar(60) COLLATE utf8_bin NOT NULL COMMENT '类目ID',
  pid varchar(60) COLLATE utf8_bin NOT NULL COMMENT '属性 ID',
  vid varchar(60) COLLATE utf8_bin NOT NULL COMMENT '属性值ID',
  prop_name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '属性名',
  name varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值',
  name_alias varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '属性值别名',
  sort_order varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '排列序号。取值范围:大于零的整数',
  child_pid varchar(80) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (vid,pid,cid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='属性值列表';


CREATE TABLE plt_taobao_message_bakup_tc (
  mid varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Messgae ID',
  mcontent varchar(4000) COLLATE utf8_bin DEFAULT NULL COMMENT 'Message 内容',
  mtype varchar(50) COLLATE utf8_bin DEFAULT NULL,     
  created datetime DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='message 备份表';

CREATE TABLE plt_taobao_message_bakup_ccms (
  mid varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Messgae ID',
  mcontent varchar(4000) COLLATE utf8_bin DEFAULT NULL COMMENT 'Message 内容',
  mtype varchar(50) COLLATE utf8_bin DEFAULT NULL,     
  created datetime DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='message 备份表';



--//@UNDO
-- SQL to undo the change goes here.

ALTER TABLE plt_taobao_product  drop column  seller_cids ;
ALTER TABLE plt_taobao_product  drop column props ;
ALTER TABLE plt_taobao_product  drop column props_name ;
ALTER TABLE plt_taobao_product  drop column input_pids;
ALTER TABLE plt_taobao_product  drop column input_str ;
