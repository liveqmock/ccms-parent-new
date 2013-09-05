--// add_column_order_tc_timeout
-- Migration SQL that makes the change goes here.
ALTER TABLE plt_taobao_order_tc
ADD COLUMN timeout_action_time datetime NULL
COMMENT '超时到期时间。格式:yyyy-MM-dd HH:mm:ss。业务规则： 前提条件：只有在买家已付款，卖家已发货的情况下才有效 如果申请了退款，那么超时会落在子订单上；比如说3笔ABC，A申请了，那么返回的是BC的列表, 主定单不存在 如果没有申请过退款，那么超时会挂在主定单上；比如ABC，返回主定单，ABC的超时和主定单相同'
;



--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE plt_taobao_order_tc drop COLUMN timeout_action_time;