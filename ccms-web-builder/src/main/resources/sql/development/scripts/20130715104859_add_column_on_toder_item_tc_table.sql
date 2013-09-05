--// add column on toder_item_tc table
-- Migration SQL that makes the change goes here.
-- 子订单
alter table plt_taobao_order_item_tc 
    add column timeout_action_time datetime DEFAULT NULL COMMENT '订单超时到期时间',
    add column buyer_rate varchar(20) DEFAULT NULL COMMENT '买家是否已评价。可选值：true(已评价)，false(未评价)',
    add column seller_rate varchar(20) DEFAULT NULL COMMENT '卖家是否已评价。可选值：true(已评价)，false(未评价)';

-- 退款
alter table plt_taobao_refund 
    add column cs_status integer DEFAULT NULL COMMENT '不需客服介入1; 需要客服介入2; 客服已经介入3; 客服初审完成 4; 客服主管复审失败5; 客服处理完成6',
    add column price decimal(12,2) DEFAULT NULL COMMENT '商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    add column good_return_time datetime DEFAULT NULL COMMENT '退货时间。格式:yyyy-MM-dd HH:mm:ss',
    add column num_iid integer DEFAULT NULL COMMENT '申请退款的商品数字编号',
    add column remind_type integer DEFAULT NULL COMMENT '提醒的类型（退款详情中提示信息的类型）',
    add column exist_timeout varchar(20) DEFAULT NULL COMMENT '是否存在超时。可选值:true(是),false(否)',
    add column timeout datetime DEFAULT NULL COMMENT '超时时间';

--//@UNDO
-- SQL to undo the change goes here.
-- 子订单
alter table plt_taobao_order_item_tc 
    drop column timeout_action_time,
    drop column buyer_rate,
    drop column seller_rate;

alter table plt_taobao_refund 
    drop column cs_status,
    drop column price,
    drop column good_return_time,
    drop column num_iid,
    drop column remind_type,
    drop column exist_timeout,
    drop column timeout;
