--// tb channel 
-- Migration SQL that makes the change goes here.

CREATE TABLE tb_channel (
channel_id  bigint(20) NOT NULL AUTO_INCREMENT,
channel_name  varchar(255) NOT NULL COMMENT '渠道名称' ,
channel_type  int(4) NOT NULL COMMENT '渠道类型' ,
channel_price  decimal(15,2) DEFAULT NULL COMMENT '渠道单价' ,
channel_desc  varchar(512) COMMENT '渠道描述' , 
PRIMARY KEY (channel_id)
)
COMMENT '渠道表' ;


--//@UNDO
-- SQL to undo the change goes here.

drop table tb_channel;