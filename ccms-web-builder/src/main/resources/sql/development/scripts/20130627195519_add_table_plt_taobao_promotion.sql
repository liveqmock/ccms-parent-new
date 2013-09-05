--// add table plt_taobao_promotion
-- Migration SQL that makes the change goes here.
DROP TABLE IF EXISTS plt_taobao_promotion;
CREATE TABLE plt_taobao_promotion (
    tid varchar(50) NOT NULL COMMENT '订单编号' ,
    oid varchar(50) NOT NULL COMMENT '子订单编号' ,
    promotion_name VARCHAR(50) COMMENT '优惠信息的名称',
    discount_fee DECIMAL(12,2) COMMENT '优惠金额（免运费、限时打折时为空）,单位：元',
    gift_item_name VARCHAR(255) COMMENT '满就送商品时，所送商品的名称',
    gift_item_id BIGINT(20) COMMENT '赠品的宝贝id',
    gift_item_num BIGINT(20) COMMENT '满就送礼物的礼物数量',
    promotion_desc VARCHAR(500) COMMENT '优惠活动的描述',
    promotion_id VARCHAR(100) COMMENT '优惠id',
    KEY idx_plt_taobao_promotion_id (tid, oid)
)COMMENT = '订单优惠信息表';


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS plt_taobao_promotion;


