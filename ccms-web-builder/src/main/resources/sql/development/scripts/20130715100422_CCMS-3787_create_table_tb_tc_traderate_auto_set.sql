--// CCMS-3787 create table tb_tc_traderate_auto_set
-- Migration SQL that makes the change goes here.
CREATE TABLE tb_tc_traderate_auto_set(
dp_id VARCHAR(50) NOT NULL COMMENT '店铺ＩＤ',
type VARCHAR(50) NOT NULL COMMENT '评价方式 （order_success-订单交易成功 order_traderate-买家评价后）',
content VARCHAR(500) COMMENT '评价内容',
status TINYINT DEFAULT 0 COMMENT '自动评价设置是否开启（0-已关闭  1-已开启）',
PRIMARY KEY (dp_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '评价事务-自动评价回复设置';


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS tb_tc_traderate_auto_set;

