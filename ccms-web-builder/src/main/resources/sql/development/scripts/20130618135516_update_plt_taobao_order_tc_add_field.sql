--// update plt_taobao_order_tc add field
-- Migration SQL that makes the change goes here.
alter table plt_taobao_order_tc add type varchar(200) DEFAULT NULL COMMENT '交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed(一口价) auction(拍卖) guarantee_trade(一口价、拍卖) auto_delivery(自动发货) independent_simple_trade(旺店入门版交易) independent_shop_trade(旺店标准版交易) ec(直冲) cod(货到付款) fenxiao(分销) game_equipment(游戏装备) shopex_trade(ShopEX交易) netcn_trade(万网交易) external_trade(统一外部交易) step (万人团)';
alter table plt_taobao_order_tc add step_trade_status varchar(50) DEFAULT NULL COMMENT '分阶段付款的订单状态（例如万人团订单等），目前有三返回状态 FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)，FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)，FRONT_PAID_FINAL_PAID(定金和尾款都付)';
alter table plt_taobao_order_tc add cod_status varchar(50) DEFAULT NULL COMMENT '货到付款物流状态。 初始状态 NEW_CREATED, 接单成功 ACCEPTED_BY_COMPANY, 接单失败 REJECTED_BY_COMPANY, 接单超时 RECIEVE_TIMEOUT, 揽收成功 TAKEN_IN_SUCCESS, 揽收失败 TAKEN_IN_FAILED, 揽收超时 TAKEN_TIMEOUT, 签收成功 SIGN_IN, 签收失败 REJECTED_BY_OTHER_SIDE, 订单等待发送给物流公司 WAITING_TO_BE_SENT, 用户取消物流订单 CANCELED';
alter table plt_taobao_order_tc add step_paid_fee decimal(12,2) DEFAULT NULL COMMENT '分阶段付款的已付金额（万人团订单已付金额）';


--//@UNDO
-- SQL to undo the change goes here.
alter table plt_taobao_order_tc drop column type;
alter table plt_taobao_order_tc drop column step_trade_status;
alter table plt_taobao_order_tc drop column cod_status;
alter table plt_taobao_order_tc drop column step_paid_fee;

