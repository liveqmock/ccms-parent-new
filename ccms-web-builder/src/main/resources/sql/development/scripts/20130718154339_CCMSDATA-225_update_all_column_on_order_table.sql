--// CCMSDATA-225 update all column on order table
-- Migration SQL that makes the change goes here.
-- ETL order, order_item, trade order_item表增加字段----------------------------
DROP PROCEDURE IF EXISTS  top_trade_transform;
CREATE PROCEDURE top_trade_transform(IN p_job_id bigint(20))
BEGIN
    -- START TRANSACTION;-- 开启事务

    -- 1. 合并按created和modified取数的订单记录, 只取最新的订单记录，并保存到 top_jobmid_trade
    DELETE FROM top_jobmid_trade;
    INSERT IGNORE INTO top_jobmid_trade  -- modified增量总订单优先
                  (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                  buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
                  type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
                  receiver_name,receiver_state,receiver_city,receiver_district,
                  receiver_address,receiver_zip,receiver_mobile,receiver_phone,
                  consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
                  has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
                  payment,discount_fee,point_fee,real_point_fee,shipping_type,adjust_fee,
                  buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag)
      SELECT dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
             buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
             type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
             receiver_name,receiver_state,receiver_city,receiver_district,
             receiver_address,receiver_zip,receiver_mobile,receiver_phone,
             consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
             has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
             payment,discount_fee,point_fee,real_point_fee,shipping_type,adjust_fee,
             buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag
      FROM top_trade_incr_job_buffer WHERE job_status = 'DOING'
      ORDER BY modified DESC; --  从较晚更新的订单开始处理
    INSERT IGNORE INTO top_jobmid_trade  -- created增量总订单置后
                  (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                  buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
                  type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
                  receiver_name,receiver_state,receiver_city,receiver_district,
                  receiver_address,receiver_zip,receiver_mobile,receiver_phone,
                  consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
                  has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
                  payment,discount_fee,point_fee,real_point_fee,shipping_type,
                  adjust_fee,buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag)
      SELECT dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
             buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
             type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
             receiver_name,receiver_state,receiver_city,receiver_district,
             receiver_address,receiver_zip,receiver_mobile,receiver_phone,
             consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
             has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
             payment,discount_fee,point_fee,real_point_fee,shipping_type,
             adjust_fee,buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag
      FROM top_trade_job_buffer WHERE job_status = 'DOING'
      ORDER BY modified DESC; --  从较晚更新的订单开始处理

    -- 2. 合并按created和modified取数的子订单记录, 只取最新的订单记录，并保存到top_jobmid_trade_order
    DELETE FROM top_jobmid_trade_order;
    INSERT IGNORE INTO top_jobmid_trade_order  -- modified增量子订单优先
                    (dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
                    item_meal_id,status,refund_id,sku_id,sku_properties_name,
                    item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
                    refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,trade_modified)
      SELECT o.dp_id,o.oid,o.tid,o.total_fee,o.discount_fee,o.adjust_fee,o.payment,
             o.item_meal_id,o.status,o.refund_id,o.sku_id,o.sku_properties_name,
             o.item_meal_name,o.num,o.title,o.price,o.pic_path,o.seller_nick,
             o.buyer_nick,o.refund_status,o.outer_iid,o.outer_sku_id,o.num_iid,o.cid,o.is_oversold,o.trade_modified
      FROM top_trade_order_incr_job_buffer o
           LEFT JOIN top_trade_incr_job_buffer t 
                  ON o.job_execution_id = t.job_execution_id 
                  AND o.tid = t.tid 
                  AND t.job_status = 'DOING'
      WHERE o.job_status = 'DOING'
      ORDER BY t.modified DESC, o.trade_modified DESC; --  从较晚更新的订单开始处理
    INSERT IGNORE INTO top_jobmid_trade_order  -- created增量子订单置后
                    (dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
                    item_meal_id,status,refund_id,sku_id,sku_properties_name,
                    item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
                    refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,trade_modified)
      SELECT o.dp_id,o.oid,o.tid,o.total_fee,o.discount_fee,o.adjust_fee,o.payment,
             o.item_meal_id,o.status,o.refund_id,o.sku_id,o.sku_properties_name,
             o.item_meal_name,o.num,o.title,o.price,o.pic_path,o.seller_nick,o.buyer_nick,
             o.refund_status,o.outer_iid,o.outer_sku_id,o.num_iid,o.cid,o.is_oversold,o.trade_modified
      FROM top_trade_order_job_buffer o
           LEFT JOIN top_trade_job_buffer t 
                  ON o.job_execution_id = t.job_execution_id 
                  AND o.tid = t.tid 
                  AND t.job_status = 'DOING'
      WHERE o.job_status = 'DOING'
      ORDER BY t.modified DESC, o.trade_modified DESC; --  从较晚更新的订单开始处理

    -- 3. 合并退款
    DELETE FROM top_jobmid_refund;
    INSERT IGNORE INTO top_jobmid_refund
                  (dp_id,refund_id,tid,oid,modified,status,refund_fee,price,num,
                  num_iid,alipay_no,total_fee,buyer_nick,seller_nick,created,
                  order_status,good_status,has_good_return,payment,reason,iid,
                  title,good_return_time,company_name,sid,address,shipping_type,
                  rto_remind_type,rto_exists_timeout,rto_timeout)
    SELECT dp_id,refund_id,tid,oid,modified,status,refund_fee,price,num,
           num_iid,alipay_no,total_fee,buyer_nick,seller_nick,created,
           order_status,good_status,has_good_return,payment,reason,iid,
           title,good_return_time,company_name,sid,address,shipping_type,
           rto_remind_type,rto_exists_timeout,rto_timeout
    FROM top_refund_job_buffer 
    WHERE job_status = 'DOING' 
    ORDER BY modified DESC; --  从较晚更新的退款单开始处理

    -- 5. 更新入plt_taobao_order_item, 先插入新子订单，再更新旧的子订单
    INSERT IGNORE INTO plt_taobao_order_item
                  (oid,tid,dp_id,customerno,total_fee,discount_fee,adjust_fee,
                  payment,status,ccms_order_status,num,num_iid,created,endtime,
                  trade_from ,type,pay_time,consign_time,title,refund_fee,modified,
                  price,refund_id,refund_status,item_meal_id,item_meal_name,sku_id,
                  sku_properties_name,outer_iid,cid,pic_path)
    SELECT o.oid,
           o.tid,
           o.dp_id,
           t.buyer_nick AS customerno,
           o.total_fee,
           o.discount_fee,
           o.adjust_fee,
           o.payment,
           o.status,
           top_trade_ccms_order_status(o.status) AS ccms_order_status,
           o.num,
           o.num_iid,
           t.created,
           t.endtime,
           (select trade_from from top_trade_full_job_buffer b where b.tid = o.tid limit 1),
           t.type,
           t.pay_time,
           t.consign_time,
           o.title,
           0 AS refund_fee,
           t.modified,
           o.price,
           o.refund_id,
           o.refund_status,
           o.item_meal_id,
           o.item_meal_name,
           o.sku_id,
           o.sku_properties_name,
           o.outer_iid,
           o.cid,
           o.pic_path
    FROM top_jobmid_trade_order o
    LEFT JOIN top_jobmid_trade t ON o.tid = t.tid;

    UPDATE  plt_taobao_order_item i,    -- 从中间子订单与中间总订单的join集合更新变化字段到最终子订单
            (SELECT t.tid,o.oid,o.total_fee,o.discount_fee,o.adjust_fee,o.payment,
                    o.status,t.endtime,t.pay_time,t.consign_time,t.step_trade_status,t.modified,
                    o.price,o.refund_id,o.refund_status,o.item_meal_id,o.item_meal_name,
                    o.sku_id,o.sku_properties_name,o.outer_iid,o.cid,o.pic_path
             FROM top_jobmid_trade_order o LEFT JOIN top_jobmid_trade t ON o.tid = t.tid )  b
     SET i.total_fee = b.total_fee,
         i.discount_fee = b.discount_fee,
         i.adjust_fee = b.adjust_fee,
         i.payment = b.payment,
         i.status = b.status,
         i.ccms_order_status = IF(b.status IS NULL, top_trade_ccms_order_status(b.step_trade_status), top_trade_ccms_order_status(b.status)),
         i.endtime = b.endtime,
         i.pay_time = b.pay_time,
         i.consign_time = b.consign_time,
         i.modified = b.modified,
         i.price = b.price,
         i.refund_id = b.refund_id,
         i.refund_status = b.refund_status,
         i.item_meal_id = b.item_meal_id,
         i.item_meal_name = b.item_meal_name,
         i.sku_id = b.sku_id,
         i.sku_properties_name = b.sku_properties_name,
         i.outer_iid = b.outer_iid,
         i.cid = b.cid,
         i.pic_path = b.pic_path,
         i.step_trade_status = b.step_trade_status
    WHERE i.oid = b.oid
    AND i.modified <= b.modified; -- 状态较旧的订单不能覆盖状态较新的订单（防止job乱序执行带来的旧订单替换新订单的隐患）

    UPDATE plt_taobao_order_item AS d   -- 更新退款相关字段
           INNER JOIN (SELECT * FROM top_jobmid_refund GROUP BY oid) b
    SET d.refund_status = b.STATUS,
        d.refund_fee = b.refund_fee
    WHERE d.oid = b.oid;

    INSERT INTO top_load_log(table_name,updated,etl_job_id,message) select
    'plt_taobao_order_item',
    now(),
    p_job_id,
    concat('本批次任务数据中，订单唯一tid数量：' , (select count(distinct tid) from top_jobmid_trade),
        '，子订单唯一tid数量', (select count(distinct tid) from top_jobmid_trade_order),
        '，TradeFull唯一tid数量', (select count(distinct tid) from top_trade_full_job_buffer where job_status = 'DOING')
        );

    -- 6. 正式导入plt_taobao_order, 先导入全新的总订单，再更新已有的旧总订单
    INSERT IGNORE INTO plt_taobao_order
                  (tid,dp_id,customerno,created,modified,ENDtime,
                  status,step_trade_status,ccms_order_status,trade_from,
                  type,pay_time,total_fee,post_fee,consign_time,alipay_no,
                  payment,discount_fee,point_fee,real_point_fee,shipping_type,
                  buyer_cod_fee,seller_cod_fee,express_agency_fee,
                  adjust_fee,buyer_obtain_point_fee ,cod_fee,cod_status,
                  buyer_alipay_no,receiver_name,receiver_state,receiver_city,
                  receiver_district,receiver_address,receiver_zip,
                  receiver_mobile,receiver_phone,buyer_email,commission_fee,
                  received_payment,num,refund_fee,buyer_memo,seller_memo,seller_flag)
    SELECT t.tid,
           t.dp_id,
           t.buyer_nick AS customerno,
           t.created,
           t.modified,
           t.endtime,
           t.status,
           t.step_trade_status,
           top_trade_ccms_order_status(t.status) AS ccms_order_status,
           (SELECT a.trade_from FROM top_trade_full_job_buffer a WHERE a.job_status = 'DOING' and a.tid = t.tid limit 1) AS trade_from,
           t.type,
           t.pay_time,
           t.total_fee,
           t.post_fee,
           t.consign_time,
           t.alipay_no,
           t.payment,
           t.discount_fee,
           t.point_fee,
           t.real_point_fee,
           t.shipping_type,
           t.buyer_cod_fee,
           t.seller_cod_fee,
           t.express_agency_fee,
           t.adjust_fee,
           t.buyer_obtain_point_fee,
           t.cod_fee,
           t.cod_status,
           substr(trim(t.buyer_alipay_no), 1, 100),
           substr(trim(t.receiver_name), 1, 50),
           substr(trim(t.receiver_state), 1, 50),
           substr(trim(t.receiver_city), 1, 50),
           substr(trim(t.receiver_district), 1, 50),
           substr(trim(t.receiver_address), 1, 255),
           substr(trim(t.receiver_zip), 1, 10),
           substr(trim(t.receiver_mobile), 1, 20),
           substr(trim(t.receiver_phone), 1, 50),
           substr(trim(t.buyer_email), 1, 100),
           t.commission_fee,
           t.received_payment,
           (SELECT sum(b.num) AS num FROM top_jobmid_trade_order b where t.tid = b.tid group by b.tid) AS num,
           0 AS refund_fee,
           t.buyer_memo,
           t.seller_memo,
           t.seller_flag
    FROM top_jobmid_trade t;

    UPDATE  plt_taobao_order o, top_jobmid_trade t    -- 从中间总订单更新变化字段到最终总订单
    SET o.total_fee = t.total_fee,
        o.discount_fee = t.discount_fee,
        o.adjust_fee = t.adjust_fee,
        o.payment = t.payment,
        o.status = t.status,
        o.step_trade_status = t.step_trade_status,
        o.ccms_order_status = IF(t.status IS NULL, top_trade_ccms_order_status(t.step_trade_status), top_trade_ccms_order_status(t.status)),
        o.endtime = t.endtime,
        o.pay_time = t.pay_time,
        o.consign_time = t.consign_time,
        o.modified = t.modified,
        o.buyer_memo = IF(t.buyer_memo IS NULL OR t.buyer_memo='', o.buyer_memo, t.buyer_memo),
        o.seller_memo = IF(t.seller_memo IS NULL OR t.seller_memo='', o.seller_memo, t.seller_memo),
        o.seller_flag = IF(t.seller_flag IS NULL OR t.seller_flag='', o.seller_flag, t.seller_flag),
        o.type = t.type,
        o.post_fee = t.post_fee,
        o.alipay_no = t.alipay_no,
        o.point_fee = t.point_fee,
        o.real_point_fee = t.real_point_fee,
        o.shipping_type = t.shipping_type,
        o.buyer_cod_fee = t.buyer_cod_fee,
        o.seller_cod_fee = t.seller_cod_fee,
        o.express_agency_fee = t.express_agency_fee,
        o.buyer_obtain_point_fee = t.buyer_obtain_point_fee,
        o.cod_fee = t.cod_fee,
        o.cod_status = t.cod_status,
        o.buyer_alipay_no = substr(trim(t.buyer_alipay_no), 1, 100),
        o.receiver_name = substr(trim(t.receiver_name), 1, 50),
        o.receiver_state = substr(trim(t.receiver_state), 1, 50),
        o.receiver_city = substr(trim(t.receiver_city), 1, 50),
        o.receiver_district = substr(trim(t.receiver_district), 1, 50),
        o.receiver_address = substr(trim(t.receiver_address), 1, 50),
        o.receiver_zip = substr(trim(t.receiver_zip), 1, 50),
        o.receiver_mobile = substr(trim(t.receiver_mobile), 1, 50),
        o.receiver_phone = substr(trim(t.receiver_phone), 1, 50),
        o.commission_fee = t.commission_fee,
        o.received_payment = t.received_payment,
        o.num = (SELECT sum(b.num) AS num FROM top_jobmid_trade_order b where t.tid = b.tid group by b.tid)
    WHERE o.tid = t.tid
    AND o.modified <= t.modified; -- 状态较旧的订单不能覆盖状态较新的订单（防止job乱序执行带来的旧订单替换新订单的隐患）

    UPDATE plt_taobao_order AS t      -- 从中间退款表汇总退款信息到最终总订单
           INNER JOIN (SELECT tid, SUM(IF(STATUS='SUCCESS', refund_fee, 0)) AS fee
                       FROM top_jobmid_refund
                       GROUP BY tid) b
    SET t.refund_fee = b.fee
    WHERE b.tid = t.tid;

    -- 7. 特殊处理trade_from, buyer_email, buyer_alipay_no
    UPDATE plt_taobao_order i , top_trade_job_buffer t  -- 用trade总订单补一次总订单trade_from
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    UPDATE plt_taobao_order_item i , top_trade_job_buffer t -- 用trade总订单补一次子订单的trade_from
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    UPDATE plt_taobao_order i , top_trade_full_job_buffer t  -- 用trade_full总订单补一次总订单trade_from, buyer_email, buyer_alipay_no
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from),
        i.buyer_email = IF(t.buyer_email IS NULL, i.buyer_email, t.buyer_email),
        i.buyer_alipay_no = IF(t.buyer_alipay_no IS NULL, i.buyer_alipay_no, t.buyer_alipay_no),
        i.buyer_memo = IF(t.buyer_memo IS NULL OR t.buyer_memo='', i.buyer_memo, t.buyer_memo),
        i.seller_memo = IF(t.seller_memo IS NULL OR t.seller_memo='', i.seller_memo, t.seller_memo),
        i.seller_flag = IF(t.seller_flag IS NULL OR t.seller_flag='', i.seller_flag, t.seller_flag),
        i.type = t.type,
        i.post_fee = t.post_fee,
        i.alipay_no = t.alipay_no,
        i.point_fee = t.point_fee,
        i.real_point_fee = t.real_point_fee,
        i.shipping_type = t.shipping_type,
        i.buyer_cod_fee = t.buyer_cod_fee,
        i.seller_cod_fee = t.seller_cod_fee,
        i.express_agency_fee = t.express_agency_fee,
        i.buyer_obtain_point_fee = t.buyer_obtain_point_fee,
        i.cod_fee = t.cod_fee,
        i.cod_status = t.cod_status,
        i.buyer_alipay_no = substr(trim(t.buyer_alipay_no), 1, 100),
        i.receiver_name = substr(trim(t.receiver_name), 1, 50),
        i.receiver_state = substr(trim(t.receiver_state), 1, 50),
        i.receiver_city = substr(trim(t.receiver_city), 1, 50),
        i.receiver_district = substr(trim(t.receiver_district), 1, 50),
        i.receiver_address = substr(trim(t.receiver_address), 1, 50),
        i.receiver_zip = substr(trim(t.receiver_zip), 1, 50),
        i.receiver_mobile = substr(trim(t.receiver_mobile), 1, 50),
        i.receiver_phone = substr(trim(t.receiver_phone), 1, 50),
        i.commission_fee = t.commission_fee,
        i.received_payment = t.received_payment,
        i.num = (SELECT sum(b.num) AS num FROM top_jobmid_trade_order b where t.tid = b.tid group by b.tid)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    UPDATE plt_taobao_order_item i , top_trade_full_job_buffer t -- 用trade_full总订单补一次子订单的trade_from
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    INSERT INTO top_load_log(table_name,updated,etl_job_id) values ('plt_taobao_order',now(),p_job_id);

    -- 8. 更新订单评价
    REPLACE INTO plt_taobao_traderate( tid, oid, dp_id, valid_score, role, nick, result,  created, rated_nick, item_title, item_price, content, reply)
    SELECT tid,
           oid,
           dp_id,
           valid_score,
           role,
           nick ,
           result,
           str_to_date(created,'%Y%m%d%H%i%s') AS created,
           rated_nick,
           item_title,
           item_price,
           content,
           reply
    FROM top_trade_rate_job_buffer WHERE job_status = 'DOING';

    -- 9. 更新订单优惠信息
    REPLACE INTO plt_taobao_promotion(tid, oid, promotion_name, discount_fee, gift_item_name, gift_item_id, gift_item_num, promotion_desc, promotion_id)
    SELECT tid,
           toid,
           promotion_name,
           discount_fee,
           gift_item_name,
           gift_item_id,
           gift_item_num,
           promotion_desc,
           promotion_id
    FROM top_trade_promotion_job_buffer WHERE job_status = 'DOING';

    -- 10. 更新店铺的最新订单时间
    UPDATE plt_taobao_shop a,
           (SELECT dp_id,
                   MAX(created) order_created_latest
              FROM top_jobmid_trade
             GROUP BY dp_id) b
       SET a.order_created_latest = b.order_created_latest
     WHERE a.shop_id = b.dp_id
       AND (a.order_created_latest < b.order_created_latest OR a.order_created_latest IS null);

    -- COMMIT;-- 提交事务
END;


--//@UNDO
-- SQL to undo the change goes here.
-- ETL order, order_item, trade order_item表增加字段----------------------------
DROP PROCEDURE IF EXISTS  top_trade_transform;
CREATE PROCEDURE top_trade_transform(IN p_job_id bigint(20))
BEGIN
    -- START TRANSACTION;-- 开启事务

    -- 1. 合并按created和modified取数的订单记录, 只取最新的订单记录，并保存到 top_jobmid_trade
    DELETE FROM top_jobmid_trade;
    INSERT IGNORE INTO top_jobmid_trade  -- modified增量总订单优先
                  (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                  buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
                  type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
                  receiver_name,receiver_state,receiver_city,receiver_district,
                  receiver_address,receiver_zip,receiver_mobile,receiver_phone,
                  consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
                  has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
                  payment,discount_fee,point_fee,real_point_fee,shipping_type,adjust_fee,
                  buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag)
      SELECT dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
             buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
             type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
             receiver_name,receiver_state,receiver_city,receiver_district,
             receiver_address,receiver_zip,receiver_mobile,receiver_phone,
             consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
             has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
             payment,discount_fee,point_fee,real_point_fee,shipping_type,adjust_fee,
             buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag
      FROM top_trade_incr_job_buffer WHERE job_status = 'DOING'
      ORDER BY modified DESC; --  从较晚更新的订单开始处理
    INSERT IGNORE INTO top_jobmid_trade  -- created增量总订单置后
                  (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                  buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
                  type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
                  receiver_name,receiver_state,receiver_city,receiver_district,
                  receiver_address,receiver_zip,receiver_mobile,receiver_phone,
                  consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
                  has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
                  payment,discount_fee,point_fee,real_point_fee,shipping_type,
                  adjust_fee,buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag)
      SELECT dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
             buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
             type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
             receiver_name,receiver_state,receiver_city,receiver_district,
             receiver_address,receiver_zip,receiver_mobile,receiver_phone,
             consign_time,buyer_email,step_trade_status,step_paid_fee,commission_fee,
             has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
             payment,discount_fee,point_fee,real_point_fee,shipping_type,
             adjust_fee,buyer_obtain_point_fee,buyer_memo,seller_memo,seller_flag
      FROM top_trade_job_buffer WHERE job_status = 'DOING'
      ORDER BY modified DESC; --  从较晚更新的订单开始处理

    -- 2. 合并按created和modified取数的子订单记录, 只取最新的订单记录，并保存到top_jobmid_trade_order
    DELETE FROM top_jobmid_trade_order;
    INSERT IGNORE INTO top_jobmid_trade_order  -- modified增量子订单优先
                    (dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
                    item_meal_id,status,refund_id,sku_id,sku_properties_name,
                    item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
                    refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,trade_modified)
      SELECT o.dp_id,o.oid,o.tid,o.total_fee,o.discount_fee,o.adjust_fee,o.payment,
             o.item_meal_id,o.status,o.refund_id,o.sku_id,o.sku_properties_name,
             o.item_meal_name,o.num,o.title,o.price,o.pic_path,o.seller_nick,
             o.buyer_nick,o.refund_status,o.outer_iid,o.outer_sku_id,o.num_iid,o.cid,o.is_oversold,o.trade_modified
      FROM top_trade_order_incr_job_buffer o
           LEFT JOIN top_trade_incr_job_buffer t 
                  ON o.job_execution_id = t.job_execution_id 
                  AND o.tid = t.tid 
                  AND t.job_status = 'DOING'
      WHERE o.job_status = 'DOING'
      ORDER BY t.modified DESC, o.trade_modified DESC; --  从较晚更新的订单开始处理
    INSERT IGNORE INTO top_jobmid_trade_order  -- created增量子订单置后
                    (dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
                    item_meal_id,status,refund_id,sku_id,sku_properties_name,
                    item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
                    refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,trade_modified)
      SELECT o.dp_id,o.oid,o.tid,o.total_fee,o.discount_fee,o.adjust_fee,o.payment,
             o.item_meal_id,o.status,o.refund_id,o.sku_id,o.sku_properties_name,
             o.item_meal_name,o.num,o.title,o.price,o.pic_path,o.seller_nick,o.buyer_nick,
             o.refund_status,o.outer_iid,o.outer_sku_id,o.num_iid,o.cid,o.is_oversold,o.trade_modified
      FROM top_trade_order_job_buffer o
           LEFT JOIN top_trade_job_buffer t 
                  ON o.job_execution_id = t.job_execution_id 
                  AND o.tid = t.tid 
                  AND t.job_status = 'DOING'
      WHERE o.job_status = 'DOING'
      ORDER BY t.modified DESC, o.trade_modified DESC; --  从较晚更新的订单开始处理

    -- 3. 合并退款
    DELETE FROM top_jobmid_refund;
    INSERT IGNORE INTO top_jobmid_refund
                  (dp_id,refund_id,tid,oid,modified,status,refund_fee,price,num,
                  num_iid,alipay_no,total_fee,buyer_nick,seller_nick,created,
                  order_status,good_status,has_good_return,payment,reason,iid,
                  title,good_return_time,company_name,sid,address,shipping_type,
                  rto_remind_type,rto_exists_timeout,rto_timeout)
    SELECT dp_id,refund_id,tid,oid,modified,status,refund_fee,price,num,
           num_iid,alipay_no,total_fee,buyer_nick,seller_nick,created,
           order_status,good_status,has_good_return,payment,reason,iid,
           title,good_return_time,company_name,sid,address,shipping_type,
           rto_remind_type,rto_exists_timeout,rto_timeout
    FROM top_refund_job_buffer 
    WHERE job_status = 'DOING' 
    ORDER BY modified DESC; --  从较晚更新的退款单开始处理

    -- 5. 更新入plt_taobao_order_item, 先插入新子订单，再更新旧的子订单
    INSERT IGNORE INTO plt_taobao_order_item
                  (oid,tid,dp_id,customerno,total_fee,discount_fee,adjust_fee,
                  payment,status,ccms_order_status,num,num_iid,created,endtime,
                  trade_from ,type,pay_time,consign_time,title,refund_fee,modified,
                  price,refund_id,refund_status,item_meal_id,item_meal_name,sku_id,
                  sku_properties_name,outer_iid,cid,pic_path)
    SELECT o.oid,
           o.tid,
           o.dp_id,
           t.buyer_nick AS customerno,
           o.total_fee,
           o.discount_fee,
           o.adjust_fee,
           o.payment,
           o.status,
           top_trade_ccms_order_status(o.status) AS ccms_order_status,
           o.num,
           o.num_iid,
           t.created,
           t.endtime,
           (select trade_from from top_trade_full_job_buffer b where b.tid = o.tid limit 1),
           t.type,
           t.pay_time,
           t.consign_time,
           o.title,
           0 AS refund_fee,
           t.modified,
           o.price,
           o.refund_id,
           o.refund_status,
           o.item_meal_id,
           o.item_meal_name,
           o.sku_id,
           o.sku_properties_name,
           o.outer_iid,
           o.cid,
           o.pic_path
    FROM top_jobmid_trade_order o
    LEFT JOIN top_jobmid_trade t ON o.tid = t.tid;

    UPDATE  plt_taobao_order_item i,    -- 从中间子订单与中间总订单的join集合更新变化字段到最终子订单
            (SELECT t.tid,o.oid,o.total_fee,o.discount_fee,o.adjust_fee,o.payment,
                    o.status,t.endtime,t.pay_time,t.consign_time,t.step_trade_status,t.modified,
                    o.price,o.refund_id,o.refund_status,o.item_meal_id,o.item_meal_name,
                    o.sku_id,o.sku_properties_name,o.outer_iid,o.cid,o.pic_path
             FROM top_jobmid_trade_order o LEFT JOIN top_jobmid_trade t ON o.tid = t.tid )  b
     SET i.total_fee = b.total_fee,
         i.discount_fee = b.discount_fee,
         i.adjust_fee = b.adjust_fee,
         i.payment = b.payment,
         i.status = b.status,
         i.ccms_order_status = IF(b.status IS NULL, top_trade_ccms_order_status(b.step_trade_status), top_trade_ccms_order_status(b.status)),
         i.endtime = b.endtime,
         i.pay_time = b.pay_time,
         i.consign_time = b.consign_time,
         i.modified = b.modified,
         i.price = b.price,
         i.refund_id = b.refund_id,
         i.refund_status = b.refund_status,
         i.item_meal_id = b.item_meal_id,
         i.item_meal_name = b.item_meal_name,
         i.sku_id = b.sku_id,
         i.sku_properties_name = b.sku_properties_name,
         i.outer_iid = b.outer_iid,
         i.cid = b.cid,
         i.pic_path = b.pic_path,
         i.step_trade_status = b.step_trade_status
    WHERE i.oid = b.oid
    AND i.modified <= b.modified; -- 状态较旧的订单不能覆盖状态较新的订单（防止job乱序执行带来的旧订单替换新订单的隐患）

    UPDATE plt_taobao_order_item AS d   -- 更新退款相关字段
           INNER JOIN (SELECT * FROM top_jobmid_refund GROUP BY oid) b
    SET d.refund_status = b.STATUS,
        d.refund_fee = b.refund_fee
    WHERE d.oid = b.oid;

    INSERT INTO top_load_log(table_name,updated,etl_job_id,message) select
    'plt_taobao_order_item',
    now(),
    p_job_id,
    concat('本批次任务数据中，订单唯一tid数量：' , (select count(distinct tid) from top_jobmid_trade),
        '，子订单唯一tid数量', (select count(distinct tid) from top_jobmid_trade_order),
        '，TradeFull唯一tid数量', (select count(distinct tid) from top_trade_full_job_buffer where job_status = 'DOING')
        );

    -- 6. 正式导入plt_taobao_order, 先导入全新的总订单，再更新已有的旧总订单
    INSERT IGNORE INTO plt_taobao_order
                  (tid,dp_id,customerno,created,modified,ENDtime,
                  status,step_trade_status,ccms_order_status,trade_from,
                  type,pay_time,total_fee,post_fee,consign_time,alipay_no,
                  payment,discount_fee,point_fee,real_point_fee,shipping_type,
                  buyer_cod_fee,seller_cod_fee,express_agency_fee,
                  adjust_fee,buyer_obtain_point_fee ,cod_fee,cod_status,
                  buyer_alipay_no,receiver_name,receiver_state,receiver_city,
                  receiver_district,receiver_address,receiver_zip,
                  receiver_mobile,receiver_phone,buyer_email,commission_fee,
                  received_payment,num,refund_fee,buyer_memo,seller_memo,seller_flag)
    SELECT t.tid,
           t.dp_id,
           t.buyer_nick AS customerno,
           t.created,
           t.modified,
           t.endtime,
           t.status,
           t.step_trade_status,
           top_trade_ccms_order_status(t.status) AS ccms_order_status,
           (SELECT a.trade_from FROM top_trade_full_job_buffer a WHERE a.job_status = 'DOING' and a.tid = t.tid limit 1) AS trade_from,
           t.type,
           t.pay_time,
           t.total_fee,
           t.post_fee,
           t.consign_time,
           t.alipay_no,
           t.payment,
           t.discount_fee,
           t.point_fee,
           t.real_point_fee,
           t.shipping_type,
           t.buyer_cod_fee,
           t.seller_cod_fee,
           t.express_agency_fee,
           t.adjust_fee,
           t.buyer_obtain_point_fee,
           t.cod_fee,
           t.cod_status,
           substr(trim(t.buyer_alipay_no), 1, 100),
           substr(trim(t.receiver_name), 1, 50),
           substr(trim(t.receiver_state), 1, 50),
           substr(trim(t.receiver_city), 1, 50),
           substr(trim(t.receiver_district), 1, 50),
           substr(trim(t.receiver_address), 1, 255),
           substr(trim(t.receiver_zip), 1, 10),
           substr(trim(t.receiver_mobile), 1, 20),
           substr(trim(t.receiver_phone), 1, 50),
           substr(trim(t.buyer_email), 1, 100),
           t.commission_fee,
           t.received_payment,
           (SELECT sum(b.num) AS num FROM top_jobmid_trade_order b where t.tid = b.tid group by b.tid) AS num,
           0 AS refund_fee,
           t.buyer_memo,
           t.seller_memo,
           t.seller_flag
    FROM top_jobmid_trade t;

    UPDATE  plt_taobao_order o, top_jobmid_trade t    -- 从中间总订单更新变化字段到最终总订单
    SET o.total_fee = t.total_fee,
        o.discount_fee = t.discount_fee,
        o.adjust_fee = t.adjust_fee,
        o.payment = t.payment,
        o.status = t.status,
        o.step_trade_status = t.step_trade_status,
        o.ccms_order_status = IF(t.status IS NULL, top_trade_ccms_order_status(t.step_trade_status), top_trade_ccms_order_status(t.status)),
        o.endtime = t.endtime,
        o.pay_time = t.pay_time,
        o.consign_time = t.consign_time,
        o.modified = t.modified,
        o.buyer_memo = IF(t.buyer_memo IS NULL OR t.buyer_memo='', o.buyer_memo, t.buyer_memo),
        o.seller_memo = IF(t.seller_memo IS NULL OR t.seller_memo='', o.seller_memo, t.seller_memo),
        o.seller_flag = IF(t.seller_flag IS NULL OR t.seller_flag='', o.seller_flag, t.seller_flag)
    WHERE o.tid = t.tid
    AND o.modified <= t.modified; -- 状态较旧的订单不能覆盖状态较新的订单（防止job乱序执行带来的旧订单替换新订单的隐患）

    UPDATE plt_taobao_order AS t      -- 从中间退款表汇总退款信息到最终总订单
           INNER JOIN (SELECT tid, SUM(IF(STATUS='SUCCESS', refund_fee, 0)) AS fee
                       FROM top_jobmid_refund
                       GROUP BY tid) b
    SET t.refund_fee = b.fee
    WHERE b.tid = t.tid;

    -- 7. 特殊处理trade_from, buyer_email, buyer_alipay_no
    UPDATE plt_taobao_order i , top_trade_job_buffer t  -- 用trade总订单补一次总订单trade_from
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    UPDATE plt_taobao_order_item i , top_trade_job_buffer t -- 用trade总订单补一次子订单的trade_from
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    UPDATE plt_taobao_order i , top_trade_full_job_buffer t  -- 用trade_full总订单补一次总订单trade_from, buyer_email, buyer_alipay_no
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from),
        i.buyer_email = IF(t.buyer_email IS NULL, i.buyer_email, t.buyer_email),
        i.buyer_alipay_no = IF(t.buyer_alipay_no IS NULL, i.buyer_alipay_no, t.buyer_alipay_no),
        i.buyer_memo = IF(t.buyer_memo IS NULL OR t.buyer_memo='', i.buyer_memo, t.buyer_memo),
        i.seller_memo = IF(t.seller_memo IS NULL OR t.seller_memo='', i.seller_memo, t.seller_memo),
        i.seller_flag = IF(t.seller_flag IS NULL OR t.seller_flag='', i.seller_flag, t.seller_flag)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    UPDATE plt_taobao_order_item i , top_trade_full_job_buffer t -- 用trade_full总订单补一次子订单的trade_from
    SET i.trade_from = IF(t.trade_from IS NULL, i.trade_from, t.trade_from)
    WHERE i.tid = t.tid
    AND t.job_status = 'DOING';

    INSERT INTO top_load_log(table_name,updated,etl_job_id) values ('plt_taobao_order',now(),p_job_id);

    -- 8. 更新订单评价
    REPLACE INTO plt_taobao_traderate( tid, oid, dp_id, valid_score, role, nick, result,  created, rated_nick, item_title, item_price, content, reply)
    SELECT tid,
           oid,
           dp_id,
           valid_score,
           role,
           nick ,
           result,
           str_to_date(created,'%Y%m%d%H%i%s') AS created,
           rated_nick,
           item_title,
           item_price,
           content,
           reply
    FROM top_trade_rate_job_buffer WHERE job_status = 'DOING';

    -- 9. 更新订单优惠信息
    REPLACE INTO plt_taobao_promotion(tid, oid, promotion_name, discount_fee, gift_item_name, gift_item_id, gift_item_num, promotion_desc, promotion_id)
    SELECT tid,
           toid,
           promotion_name,
           discount_fee,
           gift_item_name,
           gift_item_id,
           gift_item_num,
           promotion_desc,
           promotion_id
    FROM top_trade_promotion_job_buffer WHERE job_status = 'DOING';

    -- 10. 更新店铺的最新订单时间
    UPDATE plt_taobao_shop a,
           (SELECT dp_id,
                   MAX(created) order_created_latest
              FROM top_jobmid_trade
             GROUP BY dp_id) b
       SET a.order_created_latest = b.order_created_latest
     WHERE a.shop_id = b.dp_id
       AND (a.order_created_latest < b.order_created_latest OR a.order_created_latest IS null);

    -- COMMIT;-- 提交事务
END;

