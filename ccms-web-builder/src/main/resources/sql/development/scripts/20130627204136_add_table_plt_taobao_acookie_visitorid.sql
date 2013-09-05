--// add table plt_taobao_acookie_visitorid
-- Migration SQL that makes the change goes here.
CREATE TABLE IF NOT EXISTS PLT_TAOBAO_ACOOKIE_VISITORID (
dp_id  varchar(50) NOT NULL COMMENT '店铺id' ,
visitor_id  varchar(50) NOT NULL COMMENT '访客ID，与Acookie数据中的visitorid可以匹配' ,
buyer_nick  varchar(50) NOT NULL COMMENT '访客ID对应的淘宝昵称' ,
date_min  datetime  COMMENT '最早一笔订单的下单时间，无业务意义，只是标示数据作用',
date_max  datetime  COMMENT '最后一笔订单的下单时间，无业务意义，只是标示数据作用' ,
PRIMARY KEY (dp_id,visitor_id,buyer_nick)
)
COMMENT = '基于Acookie订单建立的visitorid与Buyer_nick的映射关系表' ENGINE=InnoDB;


--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS PLT_TAOBAO_ACOOKIE_VISITORID;

