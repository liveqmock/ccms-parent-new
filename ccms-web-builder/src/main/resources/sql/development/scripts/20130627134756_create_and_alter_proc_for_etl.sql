--// create and alter proc for etl
-- Migration SQL that makes the change goes here.
-- ---------------------------------------------------------------------------------------------------------------------------------------
--   定义ETL相关全局变量和存储过程
-- --------------------------------------------------------------------------------------------------------------------------------------

-- SET GLOBAL log_bin_trust_function_creators = 1; -- Utilities
DROP PROCEDURE IF EXISTS  top_job_buffer_reset_job_status;
CREATE PROCEDURE top_job_buffer_reset_job_status()
BEGIN
  UPDATE top_increment_member_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_item_cat_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_item_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_item_onsale_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_item_sku_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_product_prop_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_product_seller_cat_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_refund_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_shipping_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_shop_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_full_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_incr_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_order_incr_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_order_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_rate_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_trade_promotion_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_user_job_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
  UPDATE top_acookie_visitorid_buffer a SET a.job_status = 'NEW' WHERE a.job_status = 'DOING';
END ;

DROP PROCEDURE IF EXISTS top_job_buffer_release_space;
CREATE PROCEDURE top_job_buffer_release_space()
BEGIN
  DECLARE v_buff_rec_count int(11) DEFAULT 0;
  SELECT count(1) INTO v_buff_rec_count FROM top_increment_member_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_increment_member_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_item_cat_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_item_cat_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_item_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_item_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_item_onsale_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_item_onsale_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_item_sku_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_item_sku_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_product_prop_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_product_prop_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_product_seller_cat_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_product_seller_cat_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_refund_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_refund_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_shipping_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_shipping_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_shop_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_shop_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_trade_full_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_trade_full_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_trade_incr_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_trade_incr_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_trade_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_trade_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_trade_order_incr_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_trade_order_incr_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_trade_order_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_trade_order_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_trade_rate_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_trade_rate_job_buffer; END IF;
  SELECT count(1) INTO v_buff_rec_count FROM top_user_job_buffer;
  IF v_buff_rec_count = 0 THEN TRUNCATE TABLE top_user_job_buffer; END IF;
END;

-- 对JOB将要处理的所有记录的状态（job_status）置为正在处理：DOING，相当于圈定本周期JOB将要处理的所有记录
DROP PROCEDURE IF EXISTS  top_job_buffer_prepare;
CREATE PROCEDURE top_job_buffer_prepare()
BEGIN
  DECLARE v_buffer_trade_count int(11) DEFAULT 0;
  DECLARE v_batch_max_count int(11) DEFAULT 1000; -- 每个批次默认处理的数量（订单数或记录数）

  -- 动态设定每个批次处理的数量
  SELECT count(*) INTO v_buffer_trade_count FROM top_trade_job_buffer;
  SELECT case when v_buffer_trade_count < 5000 then 1000
              when v_buffer_trade_count < 10000 then 2000
              when v_buffer_trade_count >= 10000 and v_buffer_trade_count < 100000 then 3000
              when v_buffer_trade_count >= 100000 then 10000
          end
  INTO v_batch_max_count;


  -- 1. 重置因上次JOB失败时所设置的处理状态
  call top_job_buffer_reset_job_status();

  -- 2. 圈定要处理的订单。ETL订单处理流程是由订单驱动，订单对象的处理都围绕被圈定的订单。
  DELETE FROM top_jobmid_trade_doing;
  INSERT INTO top_jobmid_trade_doing  SELECT DISTINCT t.tid, t.buyer_nick FROM
    (SELECT tid, buyer_nick, modified FROM top_trade_job_buffer WHERE job_status = 'NEW'  UNION ALL
     SELECT tid, buyer_nick, modified FROM top_trade_incr_job_buffer WHERE job_status = 'NEW') t
    ORDER BY t.modified ASC -- 从较早的订单开始处理
    Limit v_batch_max_count;

  -- 3. 在各个订单直接相关的job_buffer表圈定要处理的订单记录，将处理状态置为DOING。
  UPDATE top_trade_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND EXISTS (SELECT 1 FROM top_jobmid_trade_doing b WHERE a.tid = b.tid );
  UPDATE top_trade_order_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND EXISTS (SELECT 1 FROM top_jobmid_trade_doing b WHERE a.tid = b.tid );
  UPDATE top_trade_incr_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND EXISTS (SELECT 1 FROM top_jobmid_trade_doing b WHERE a.tid = b.tid );
  UPDATE top_trade_order_incr_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND EXISTS (SELECT 1 FROM top_jobmid_trade_doing b WHERE a.tid = b.tid );
  UPDATE top_trade_full_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND EXISTS (SELECT 1 FROM top_jobmid_trade_doing b WHERE a.tid = b.tid );

  -- 4. 在各个客人相关的job_buffer表圈定要处理的记录，将处理状态置为DOING。

  -- 5. 无依赖关系大数据量对象, 需分批次处理，将每批次处理状态置为DOING。
  UPDATE top_user_job_buffer a, (SELECT DISTINCT nick FROM top_user_job_buffer LIMIT v_batch_max_count) b SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND a.nick = b.nick;
  UPDATE top_trade_rate_job_buffer a, (SELECT DISTINCT tid FROM top_trade_rate_job_buffer LIMIT v_batch_max_count) b SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND a.tid = b.tid;
  UPDATE top_refund_job_buffer a, (SELECT DISTINCT tid FROM top_refund_job_buffer LIMIT v_batch_max_count) b SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND a.tid = b.tid;
  UPDATE top_shipping_job_buffer a, (SELECT DISTINCT tid FROM top_shipping_job_buffer LIMIT v_batch_max_count) b SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND a.tid = b.tid;
  UPDATE top_increment_member_job_buffer a, (SELECT DISTINCT buyer_nick FROM top_increment_member_job_buffer ORDER BY row_id ASC LIMIT v_batch_max_count) b
    SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' AND a.buyer_nick = b.buyer_nick; -- 只处理前 v_batch_max_count 的会员信息

  -- 6. 无依赖关系全量处理相关的job_buffer表圈定要处理的订单记录，将处理状态置为DOING。
  UPDATE top_item_cat_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_item_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_item_sku_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_product_prop_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_product_seller_cat_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_shop_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_trade_promotion_job_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW';
  UPDATE top_acookie_visitorid_buffer a SET a.job_status = 'DOING' WHERE a.job_status = 'NEW' ORDER BY trade_created DESC LIMIT v_batch_max_count;

END;


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

-- ETL USER -----------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_user_transform;
CREATE PROCEDURE top_user_transform(IN p_job_id bigint(20))
BEGIN
  -- START TRANSACTION;-- 开启事务

  -- 1. 合并所有客人
  DELETE FROM top_jobmid_user;
  INSERT IGNORE INTO top_jobmid_user
                (dp_id, user_id, nick, sex, buyer_credit_lev, buyer_credit_score, buyer_credit_good_num, buyer_credit_total_num, seller_credit_lev, seller_credit_score, seller_credit_good_num, seller_credit_total_num, zip, address, city, state, country, district, created, last_visit, birthday, type, alipay_account, alipay_no, has_shop, vip_info, email)
    SELECT dp_id, user_id, nick, sex, buyer_credit_lev, buyer_credit_score, buyer_credit_good_num, buyer_credit_total_num, seller_credit_lev, seller_credit_score, seller_credit_good_num, seller_credit_total_num, zip, address, city, state, country, district, created, last_visit, birthday, type, alipay_account, alipay_no, has_shop, vip_info, email
    FROM top_user_job_buffer WHERE job_status = 'DOING'; -- 新先入则旧不入

  -- 2. 创建一个存储需要被etl的用户，在plt_taobao_customer中各项数据的原有值的临时表
--  DROP TABLE IF EXISTS top_tmp_customer_tobe_updated;
--  CREATE TABLE top_tmp_customer_tobe_updated AS
--  SELECT cust.*
--    FROM plt_taobao_customer cust
--   INNER JOIN top_jobmid_user user
--      ON cust.customerno = user.nick ;

--  CREATE INDEX idx_top_tmp_customer_tobe_updated_nick on top_tmp_customer_tobe_updated(customerno);

  -- 3. 根据取到的用户数据，更新plt_taobao_customer
  INSERT IGNORE INTO plt_taobao_customer (customerno, full_name, sex, buyer_credit_lev, buyer_credit_score,
                                    buyer_credit_good_num, buyer_credit_total_num, zip, address, city,
                                    state, country, district, created, last_visit, vip_info, email,
                                    mobile, phone, last_sync, changed)
  SELECT nick AS customerno,
         null, -- top_caculate_customer_full_name(nick) AS full_name,
         sex,
         buyer_credit_lev,
         buyer_credit_score,
         buyer_credit_good_num,
         buyer_credit_total_num,
         zip, -- top_caculate_customer_zip(nick) AS zip,
         address, -- top_caculate_customer_address(nick) AS address,
         city, -- top_caculate_customer_city(nick) AS city,
         state, -- top_cacul ate_customer_state(nick) AS state,
         country,
         district, -- top_caculate_customer_district(nick) AS district,
         created,
         last_visit,
         vip_info,
         email, -- top_caculate_customer_email(nick) AS email,
         null, -- top_caculate_customer_mobile(nick) AS mobile,
         null, -- top_caculate_customer_phone(nick) AS phone,
         now() AS last_sync,
         now() AS changed
    FROM top_jobmid_user;

  UPDATE plt_taobao_customer c,top_jobmid_user u
        SET
            c.sex = IF(u.sex is null, c.sex, u.sex),
            c.buyer_credit_lev = IF(u.buyer_credit_lev is null, c.buyer_credit_lev, u.buyer_credit_lev),
            c.buyer_credit_score = IF(u.buyer_credit_score is null, c.buyer_credit_score, u.buyer_credit_score),
            c.buyer_credit_good_num = IF(u.buyer_credit_good_num is null, c.buyer_credit_good_num, u.buyer_credit_good_num),
            c.buyer_credit_total_num = IF(u.buyer_credit_total_num is null, c.buyer_credit_total_num, u.buyer_credit_total_num),
            c.zip = IF(u.zip is null, c.zip, u.zip),
            c.address = IF(u.address is null, c.address, u.address),
            c.city = IF(u.city is null, c.city, u.city),
            c.state = IF(u.state is null, c.state, u.state),
            c.country = IF(u.country is null, c.country, u.country),
            c.district = IF(u.district is null, c.district, u.district),
            c.created = IF(u.created is null, c.created, u.created),
            c.last_visit = IF(u.last_visit is null, c.last_visit, u.last_visit),
            c.vip_info = IF(u.vip_info is null, c.vip_info, u.vip_info),
            c.email = IF(u.email is null, c.email, u.email),
            c.last_sync = now(),
            c.changed = now()
  WHERE c.customerno = u.nick;
  
  -- 4. 根据订单里的数据，补plt_taobao_customer中没有的部分用户
  INSERT IGNORE INTO plt_taobao_customer(customerno, full_name, zip,address, city, state, district, email, mobile, phone, changed)
    SELECT a.buyer_nick AS customerno,
         substr(trim(a.receiver_name), 1, 50) AS full_name,
         substr(trim(a.receiver_zip), 1, 20) AS zip,
         substr(trim(a.receiver_address), 1, 255) AS address,
         substr(trim(a.receiver_city), 1, 50) AS city,
         substr(trim(a.receiver_state), 1, 50) AS state,
         substr(trim(a.receiver_district), 1, 100) AS district,
         substr(trim(a.buyer_email), 1, 100) AS email,
         substr(trim(a.receiver_mobile), 1, 20) AS mobile,
         substr(trim(a.receiver_phone), 1, 50) AS phone,
         a.created AS changed
    FROM top_jobmid_trade a;

  -- 5. 根据trade中间表的收货人地址数据补customer信息
    UPDATE plt_taobao_customer c, top_jobmid_trade t
        SET
            c.full_name = IF(t.receiver_name is null, c.full_name, substr(trim(t.receiver_name), 1, 50)),
            c.zip = IF(t.receiver_zip is null, c.zip, substr(trim(t.receiver_zip), 1, 20)),
            c.address = IF(t.receiver_address is null, c.address, substr(trim(t.receiver_address), 1, 255)),
            c.city = IF(t.receiver_city is null, c.city, substr(trim(t.receiver_city), 1, 50)),
            c.state = IF(t.receiver_state is null, c.state, substr(trim(t.receiver_state), 1, 50)),
            c.district = IF(t.receiver_district is null, c.district, substr(trim(t.receiver_district), 1, 100)),
            c.last_sync = now(),
            c.changed = now()
        WHERE c.customerno = t.buyer_nick;

    -- 6. 从trade_full Buffer表的数据补全email或手机号
    UPDATE plt_taobao_customer c, top_trade_full_job_buffer t  -- 用trade_full总订单补一次总订单trade_from
        SET
            c.email = IF(t.buyer_email is null,
                         IF(top_is_email(t.buyer_alipay_no),
                            t.buyer_alipay_no,
                            c.email) ,
                          t.buyer_email),
            c.mobile = IF(t.receiver_mobile is null or t.receiver_mobile<>'',
                          IF(top_is_mobile(t.buyer_alipay_no),
                            t.buyer_alipay_no,
                            c.mobile) ,
                          t.receiver_mobile)
     WHERE c.customerno = t.buyer_nick
     AND t.job_status = 'DOING';

--  DROP TABLE top_tmp_customer_tobe_updated;

  INSERT INTO top_load_log (table_name, updated, etl_job_id) VALUES ('plt_taobao_customer', now(), p_job_id);

  -- COMMIT;
END;

DROP PROCEDURE IF EXISTS top_refund_transform;
CREATE PROCEDURE top_refund_transform()
BEGIN

    INSERT IGNORE INTO plt_taobao_refund
                 (refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
                 order_status,status,good_status,has_good_return,refund_fee,payment,
                 reason,refund_desc,title,num,company_name,sid)
    SELECT refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
           order_status,status,good_status,has_good_return,refund_fee,payment,
           reason,refund_desc,title,num,company_name,sid
    FROM top_refund_job_buffer
    WHERE job_status = 'DOING' ORDER BY modified;

    REPLACE INTO plt_taobao_refund
                 (refund_id,tid,oid,dp_id,total_fee,buyer_nick,seller_nick,created,modified,
                 order_status,status,good_status,has_good_return,refund_fee,payment,
                 reason,refund_desc,title,num,company_name,sid)
    SELECT o.refund_id,o.tid,o.oid,o.dp_id,o.total_fee,o.buyer_nick,o.seller_nick,o.created,o.modified,
           o.order_status,o.status,o.good_status,o.has_good_return,o.refund_fee,o.payment,
           o.reason,o.refund_desc,o.title,o.num,o.company_name,o.sid
    FROM top_refund_job_buffer o, plt_taobao_refund p
    WHERE o.job_status = 'DOING' AND o.refund_id = p.refund_id AND o.modified > p.modified
    ORDER BY modified;

END;

-- ETL shipping -------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_shipping_transform;
CREATE PROCEDURE top_shipping_transform(IN p_job_id bigint(20))
BEGIN
  -- START TRANSACTION;-- 开启事务

  REPLACE INTO plt_taobao_shipping (
               dp_id, buyer_nick, company_name, created, delivery_END,
               delivery_start,freight_payer, is_success, item_title,
               modified, order_code, out_sid,receiver_mobile, receiver_name,
               receiver_phone, seller_confirm, seller_nick, status, tid, type)
   SELECT dp_id,
          buyer_nick,
          company_name,
          created,
          delivery_END,
          delivery_start,
          freight_payer,
          IF(is_success = 'true',1,0) AS is_success,
          item_title,
          modified,
          order_code,
          out_sid,
          receiver_mobile,
          receiver_name,
          receiver_phone,
          IF(seller_confirm = 'yes', 1, 0) AS seller_confirm,
          seller_nick,
          status,
          tid,
          type
     FROM top_shipping_job_buffer WHERE job_status = 'DOING';

  -- COMMIT;
END ;

-- etl top_increment_member ----------------------------------------------------
DROP PROCEDURE IF EXISTS top_increment_member_transform;
CREATE PROCEDURE top_increment_member_transform(IN p_job_id bigint(20))
BEGIN
    -- START TRANSACTION;-- 开启事务

    INSERT IGNORE INTO plt_taobao_crm_member
           (dp_id,customerno,status,grade,trade_count,trade_amount,last_trade_time,last_sync)
    SELECT dp_id,buyer_nick,status,grade,trade_count,trade_amount,last_trade_time,last_sync
    FROM top_increment_member_job_buffer WHERE job_status = 'DOING';

    UPDATE plt_taobao_crm_member m, (select * from top_increment_member_job_buffer order by last_sync desc) tm
    SET m.status = tm.status,
        m.grade = tm.grade,
        m.trade_count = tm.trade_count,
        m.trade_amount = tm.trade_amount,
        m.last_trade_time = tm.last_trade_time,
        m.last_sync = tm.last_sync
    WHERE m.dp_id = tm.dp_id AND m.customerno = tm.buyer_nick AND (m.last_sync is null or m.last_sync < tm.last_sync)
    AND tm.job_status = 'DOING';

    INSERT IGNORE INTO plt_taobao_customer(customerno)
    SELECT buyer_nick AS customer FROM top_increment_member_job_buffer;
    -- COMMIT;
END ;

-- etl product 增加字段---------------------------------------------------------
DROP PROCEDURE IF EXISTS top_item_transform;
CREATE PROCEDURE top_item_transform(IN p_job_id bigint(20))
BEGIN

    -- 1. 合并商品
    DELETE FROM top_jobmid_item;
    INSERT IGNORE INTO top_jobmid_item
                (dp_id,num_iid,nick,price,detail_url,title,pic_url,
                list_time,type,props_name,created,promoted_service,cid,
                seller_cids,props,input_pids,input_str,num,valid_thru,
                delist_time,stuff_status,loc_zip,loc_address,loc_city,
                loc_state,loc_country,loc_district,post_fee,express_fee,
                ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
                has_showcase,modified,increment,approve_status,postage_id,
                product_id,auction_point,property_alias,outer_id,is_virtual,
                is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
                second_kill,auto_fill,violation,is_prepay,ww_status,
                wap_detail_url,after_sale_id,cod_postage_id,sell_promise)
    SELECT dp_id,num_iid,nick,price,detail_url,title,pic_url,
           list_time,type,props_name,created,promoted_service,cid,
           seller_cids,props,input_pids,input_str,num,valid_thru,
           delist_time,stuff_status,loc_zip,loc_address,loc_city,
           loc_state,loc_country,loc_district,post_fee,express_fee,
           ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
           has_showcase,modified,increment,approve_status,postage_id,
           product_id,auction_point,property_alias,outer_id,is_virtual,
           is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
           second_kill,auto_fill,violation,is_prepay,ww_status,
           wap_detail_url,after_sale_id,cod_postage_id,sell_promise
    FROM top_item_job_buffer WHERE job_status = 'DOING' ORDER BY modified DESC; -- 新先入则旧不入

    -- 2. 正式导入商品表 先INSERT IGNORE
    REPLACE INTO plt_taobao_product
                (num_iid,dp_id,detail_url,title,created,cid,pic_url,
                list_time,delist_time,price,modified,approve_status,outer_id,
                props_name,props,seller_cids,input_pids,input_str)
    SELECT num_iid,i1.dp_id,i1.detail_url,i1.title,i1.created,i1.cid,i1.pic_url,
          i1.list_time,i1.delist_time,i1.price,i1.modified,i1.approve_status,i1.outer_id,
          i1.props_name,i1.props,i1.seller_cids,i1.input_pids,i1.input_str
    FROM top_jobmid_item i1;

    -- 4. 从子订单表中补充 商品表中的缺失信息
    INSERT IGNORE INTO plt_taobao_product(num_iid,dp_id,title,price,cid)
    SELECT a.num_iid as num_iid,
           a.dp_id as dp_id,
           a.title as title,
           a.price as price,
           a.cid as cid
    FROM top_jobmid_trade_order a;

    -- 5. 正式导入卖家自定义类目表
    REPLACE INTO plt_taobao_product_seller_cat(num_iid,seller_cid)
    SELECT s.num_iid,s.seller_cid
    FROM top_product_seller_cat_job_buffer  s
    WHERE s.job_status = 'DOING'
      AND NOT EXISTS (SELECT 1
                     FROM plt_taobao_product_seller_cat c
                     WHERE c.num_iid = s.num_iid
                       AND c.seller_cid = s.seller_cid);

    -- 6. 正式导入卖家商品属性表
    REPLACE INTO plt_taobao_product_prop( num_iid,pid,prop_name,vid,name,name_alias,is_input_prop )
    SELECT p1.num_iid,p1.pid,p1.prop_name,p1.vid,p1.name,p1.name_alias,p1.is_input_prop
    FROM top_product_prop_job_buffer p1
         INNER JOIN (SELECT MAX(row_id) AS row_id
                    FROM top_product_prop_job_buffer WHERE job_status = 'DOING'
                    GROUP BY num_iid,pid) p2
         ON p1.row_id = p2.row_id
    WHERE p1.job_status = 'DOING';

    INSERT INTO top_load_log(table_name,updated,etl_job_id) VALUES ('plt_taobao_product',now(), p_job_id );
END;

DROP PROCEDURE IF EXISTS top_item_props_transform;
CREATE PROCEDURE top_item_props_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare props_t text;
    declare _done int default 0;

    -- 查找出top_item_job_buffer表中所有的props记录, 进行遍历
    declare _cur cursor for
      select num_iid, props from top_item_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, props_t;
                call top_item_props_split(num_iid_t, props_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_props_split;
CREATE PROCEDURE top_item_props_split(IN num_iid varchar(50), props text)
BEGIN
    declare total_len int;
    declare start_pos int;
    declare end_pos int;

    -- 将props中的全角标点替换为半角标点, 最后加上半角的分号';' 方便处理
    set props = replace(props, '；', ';');
    set props = replace(props, '：', ':');
    set props = concat(props, ';');
    set total_len = length(props);

    set start_pos = 1;
    set end_pos = locate(';', props, start_pos);
    -- 如果找到了一个分号, 表示有一条props记录, 分割后递交下一步处理
    while end_pos <> 0 do
        call top_item_props_split_pid_vid(num_iid, substring(props, start_pos, end_pos - start_pos));
        set start_pos = end_pos + 1;
        set end_pos = locate(';', props, start_pos);
    end while;

END;

DROP PROCEDURE IF EXISTS top_item_props_split_pid_vid;
CREATE PROCEDURE top_item_props_split_pid_vid(IN num_iid varchar(50), procs text)
sp:BEGIN
    declare pid varchar(150);
    declare vid varchar(150);
    declare pidname varchar(200);
    declare vidname varchar(200);
    declare start_pos int;
    declare end_pos int;

    if (procs is null or procs='') then
        leave sp;
    end if;

    -- procs的处理按照正常情况处理, 即四个字段全部存在, 异常情况暂时没有处理
    set start_pos = 1;
    set end_pos = locate(':', procs, start_pos);
    set pid = substring(procs, start_pos, end_pos - start_pos);

    set start_pos = end_pos + 1;
    set end_pos = locate(':', procs, start_pos);
    set vid = substring(procs, start_pos, end_pos - start_pos);

    set start_pos = end_pos + 1;
    set end_pos = locate(':', procs, start_pos);
    set pidname = substring(procs, start_pos, end_pos - start_pos);

    set start_pos = end_pos + 1;
    set vidname = right(procs, char_length(procs) - end_pos);

    replace into plt_taobao_product_prop(num_iid, pid, vid, prop_name, name) 
    values(num_iid, pid, vid, pidname, vidname);

END;

DROP PROCEDURE IF EXISTS top_item_seller_cat_transform;
CREATE PROCEDURE top_item_seller_cat_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare seller_cids_t varchar(200);
    declare _done int default 0;

    -- 查询top_item_job_buffer表记录, 开始遍历
    declare _cur cursor for
      select num_iid, seller_cids from top_item_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, seller_cids_t;
                call top_item_seller_cid_split(num_iid_t, seller_cids_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_seller_cid_split;
CREATE PROCEDURE top_item_seller_cid_split(IN num_iid varchar(50), seller_cids varchar(200))
sp:BEGIN
    declare total_len int;
    declare start_pos int;
    declare end_pos int;
    declare str varchar(40);

    if (seller_cids is null or seller_cids='') then
        leave sp;
    end if;

    -- 替换标点
    set seller_cids = replace(seller_cids, '，', ',');
    set seller_cids = concat(seller_cids, ',');
    set total_len = length(seller_cids);

    set start_pos = 1;
    set end_pos = locate(',', seller_cids, start_pos);
    -- 两逗号','中间的是一个记录
    while end_pos <> 0 do
        set str = substring(seller_cids, start_pos, end_pos - start_pos);
        if(str is not null and str <> '') then
            insert ignore into plt_taobao_product_seller_cat(num_iid, seller_cid) values(num_iid, str);
        end if;
        set start_pos = end_pos + 1;
        set end_pos = locate(',', seller_cids, start_pos);
    end while;

END;

-- ETL sku 新增加的plt_taobao_product_sku处理sp
DROP PROCEDURE IF EXISTS top_item_sku_transform;
CREATE PROCEDURE top_item_sku_transform()
BEGIN
    -- 新增的数据类型进行处理时, 不再进行分批划分, 一次性的将buffer表中的数据全部处理
    REPLACE INTO plt_taobao_product_skus
                 (properties_name,sku_spec_id,with_hold_quantity,sku_delivery_time,
                 change_prop,sku_id,dp_id,iid,num_iid,properties,quantity,price,
                 outer_id,created,modified,status)
    SELECT properties_name,sku_spec_id,with_hold_quantity,sku_delivery_time,
           change_prop,sku_id,dp_id,iid,num_iid,properties,quantity,price,
           outer_id,created,modified,status
    FROM top_item_sku_job_buffer;

    INSERT IGNORE INTO plt_taobao_product_skus(num_iid,sku_id,properties)
    SELECT a.num_iid as num_iid,
           a.sku_id as sku_id,
           a.sku_properties_name as properties
    FROM top_jobmid_trade_order a;
END;

DROP PROCEDURE IF EXISTS top_item_skus_prop_transform;
CREATE PROCEDURE top_item_skus_prop_transform()
BEGIN
    declare num_iid_t varchar(50);
    declare sku_id_t varchar(150);
    declare props_t varchar(1000);
    declare _done int default 0;

    declare _cur cursor for
      select num_iid, sku_id, properties_name from top_item_sku_job_buffer;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;

    open _cur;
        repeat
            if not _done then
                fetch _cur into num_iid_t, sku_id_t, props_t;
                call top_item_sku_split(num_iid_t, sku_id_t, props_t);
            end if;
        until _done end repeat;
    close _cur;

END;

DROP PROCEDURE IF EXISTS top_item_sku_split;
CREATE PROCEDURE top_item_sku_split(IN num_iid varchar(50), sku_id varchar(50), procs varchar(1000))
BEGIN
    declare total_len int;
    declare start_pos int;
    declare end_pos int;

    set procs = replace(procs, '；', ';');
    set procs = replace(procs, '：', ':');
    set procs = concat(procs, ';');
    set total_len = char_length(procs);
    set start_pos = 1;
    set end_pos = locate(';', procs, start_pos);

    while end_pos <> 0 do
        call top_item_sku_split_pid_vid(num_iid, sku_id, substring(procs, start_pos, end_pos - start_pos));
        set start_pos = end_pos + 1;
        set end_pos = locate(';', procs, start_pos);
    end while;

END;

DROP PROCEDURE IF EXISTS top_item_sku_split_pid_vid;
CREATE PROCEDURE top_item_sku_split_pid_vid(IN num_iid varchar(50), sku_id varchar(50), procs text)
sp:BEGIN
    declare pid varchar(150);
    declare vid varchar(150);
    declare pidname varchar(200);
    declare vidname varchar(200);
    declare start_pos int;
    declare end_pos int;

    if (procs is null or procs='') then
        leave sp;
    end if;

    -- procs的处理按照正常情况处理, 即四个字段全部存在, 异常情况暂时没有处理
    set start_pos = 1;
    set end_pos = locate(':', procs, start_pos);
    set pid = substring(procs, start_pos, end_pos - start_pos);

    set start_pos = end_pos + 1;
    set end_pos = locate(':', procs, start_pos);
    set vid = substring(procs, start_pos, end_pos - start_pos);

    set start_pos = end_pos + 1;
    set end_pos = locate(':', procs, start_pos);
    set pidname = substring(procs, start_pos, end_pos - start_pos);

    set start_pos = end_pos + 1;
    set vidname = right(procs, char_length(procs) - end_pos);

    replace into plt_taobao_product_skus_prop
           (num_iid, sku_id, pid, vid, pidname, vidname) 
    values(num_iid, sku_id, pid, vid, pidname, vidname);
END;

-- etl acookie --------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_acookie_transform;
CREATE PROCEDURE top_acookie_transform(IN p_job_id bigint(20))
BEGIN
  -- START TRANSACTION;-- 开启事务

  -- 1. 插入acookie_user 信息
  INSERT IGNORE INTO plt_taobao_acookie_visitorid (dp_id,visitor_id,buyer_nick,date_min,date_max)
    SELECT dp_id,acook_nick,buyer_nick,trade_created,trade_created FROM top_acookie_visitorid_buffer
    WHERE job_status = 'DOING';

  UPDATE plt_taobao_acookie_visitorid a,
    (select dp_id,acook_nick,buyer_nick, min(trade_created) as mmin, max(trade_created) as mmax
      from  top_acookie_visitorid_buffer
      where job_status = 'DOING'
      group by dp_id,acook_nick,buyer_nick) b
    SET a.date_min = IF(mmin < a.date_min, mmin, a.date_min),
        a.date_max = IF(mmax > a.date_max, mmax, a.date_max)
  WHERE a.dp_id = b.dp_id AND a.visitor_id = b.acook_nick AND a.buyer_nick = b.buyer_nick;

   -- COMMIT;
END;

-- backup buffer data ----------------------------------------------------------
DROP PROCEDURE IF EXISTS top_backup_buffer_data;
CREATE PROCEDURE top_backup_buffer_data()
BEGIN

    -- bakup to top_trade_order
    insert into top_trade_order
                (dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
                item_meal_id,status,refund_id,sku_id,sku_properties_name,
                item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
                refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,
                job_execution_id,trade_modified)
    select dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
           item_meal_id,status,refund_id,sku_id,sku_properties_name,
           item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
           refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,
           job_execution_id,trade_modified
    from top_trade_order_job_buffer where job_status = 'DOING';
    delete from top_trade_order_job_buffer where job_status = 'DOING';

    -- bakup to top_trade_full
    insert into top_trade_full
                (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                buyer_cod_fee,seller_cod_fee,express_agency_fee,status,
                trade_from,type,alipay_no,pay_time,total_fee,post_fee,
                buyer_alipay_no,receiver_name,receiver_state,receiver_city,
                receiver_district,receiver_address,receiver_zip,receiver_mobile,
                receiver_phone,consign_time,buyer_email,commission_fee,
                has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
                payment,discount_fee,point_fee,real_point_fee,shipping_type,
                adjust_fee,buyer_obtain_point_fee,job_execution_id)
    select dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
           buyer_cod_fee,seller_cod_fee,express_agency_fee,status,
           trade_from,type,alipay_no,pay_time,total_fee,post_fee,
           buyer_alipay_no,receiver_name,receiver_state,receiver_city,
           receiver_district,receiver_address,receiver_zip,receiver_mobile,
           receiver_phone,consign_time,buyer_email,commission_fee,
           has_post_fee,received_payment,cod_fee,cod_status,timeout_action_time,
           payment,discount_fee,point_fee,real_point_fee,shipping_type,
           adjust_fee,buyer_obtain_point_fee,job_execution_id
    from top_trade_full_job_buffer where job_status = 'DOING';
    delete from top_trade_full_job_buffer where job_status = 'DOING';

    -- bakup to top_trade
    insert into top_trade
                (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                buyer_cod_fee,seller_cod_fee,express_agency_fee,status,
                trade_from,type,alipay_no,pay_time,total_fee,post_fee,
                buyer_alipay_no,receiver_name,receiver_state,receiver_city,
                receiver_district,receiver_address,receiver_zip,receiver_mobile,
                receiver_phone,consign_time,buyer_email,commission_fee,has_post_fee,
                received_payment,cod_fee,cod_status,timeout_action_time,payment,
                discount_fee,point_fee,real_point_fee,shipping_type,adjust_fee,
                buyer_obtain_point_fee,job_execution_id)
    select dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
           buyer_cod_fee,seller_cod_fee,express_agency_fee,status,
           trade_from,type,alipay_no,pay_time,total_fee,post_fee,
           buyer_alipay_no,receiver_name,receiver_state,receiver_city,
           receiver_district,receiver_address,receiver_zip,receiver_mobile,
           receiver_phone,consign_time,buyer_email,commission_fee,has_post_fee,
           received_payment,cod_fee,cod_status,timeout_action_time,payment,
           discount_fee,point_fee,real_point_fee,shipping_type,adjust_fee,
           buyer_obtain_point_fee,job_execution_id
    from top_trade_job_buffer where job_status = 'DOING';
    delete from top_trade_job_buffer where job_status = 'DOING';

    -- bakup to top_trade_order_incr
    insert into top_trade_order_incr
                (dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
                item_meal_id,status,refund_id,sku_id,sku_properties_name,
                item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
                refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,
                job_execution_id,trade_modified)
    select dp_id,oid,tid,total_fee,discount_fee,adjust_fee,payment,
           item_meal_id,status,refund_id,sku_id,sku_properties_name,
           item_meal_name,num,title,price,pic_path,seller_nick,buyer_nick,
           refund_status,outer_iid,outer_sku_id,num_iid,cid,is_oversold,
           job_execution_id,trade_modified
    from top_trade_order_incr_job_buffer where job_status = 'DOING';
    delete from top_trade_order_incr_job_buffer where job_status = 'DOING';

    -- bakup to top_trade_incr
    insert into top_trade_incr
                (dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
                buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
                type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
                receiver_name,receiver_state,receiver_city,receiver_district,
                receiver_address,receiver_zip,receiver_mobile,receiver_phone,
                consign_time,buyer_email,commission_fee,has_post_fee,received_payment,
                cod_fee,cod_status,timeout_action_time,payment,discount_fee,point_fee,
                real_point_fee,shipping_type,adjust_fee,buyer_obtain_point_fee,
                job_execution_id)
    select dp_id,tid,created,modified,endtime,seller_nick,buyer_nick,
           buyer_cod_fee,seller_cod_fee,express_agency_fee,status,trade_from,
           type,alipay_no,pay_time,total_fee,post_fee,buyer_alipay_no,
           receiver_name,receiver_state,receiver_city,receiver_district,
           receiver_address,receiver_zip,receiver_mobile,receiver_phone,
           consign_time,buyer_email,commission_fee,has_post_fee,received_payment,
           cod_fee,cod_status,timeout_action_time,payment,discount_fee,point_fee,
           real_point_fee,shipping_type,adjust_fee,buyer_obtain_point_fee,
           job_execution_id
    from top_trade_incr_job_buffer where job_status = 'DOING';
    delete from top_trade_incr_job_buffer where job_status = 'DOING';

    -- bakup to top_user
    insert into top_user
                (dp_id,user_id,nick,sex,buyer_credit_lev,buyer_credit_score,
                buyer_credit_good_num,buyer_credit_total_num,seller_credit_lev,
                seller_credit_score,seller_credit_good_num,seller_credit_total_num,
                zip,address,city,state,country,district,created,last_visit,birthday,
                type,alipay_account,alipay_no,has_shop,vip_info,email,job_execution_id)
    select dp_id,user_id,nick,sex,buyer_credit_lev,buyer_credit_score,
           buyer_credit_good_num,buyer_credit_total_num,seller_credit_lev,
           seller_credit_score,seller_credit_good_num,seller_credit_total_num,
           zip,address,city,state,country,district,created,last_visit,birthday,
           type,alipay_account,alipay_no,has_shop,vip_info,email,job_execution_id
    from top_user_job_buffer where job_status = 'DOING';
    delete from top_user_job_buffer where job_status = 'DOING';

    -- bakup to top_item
    insert into top_item
                (dp_id,num_iid,nick,price,detail_url,title,pic_url,list_time,type,
                props_name,created,promoted_service,cid,seller_cids,props,input_pids,
                input_str,num,valid_thru,delist_time,stuff_status,loc_zip,loc_address,
                loc_city,loc_state,loc_country,loc_district,post_fee,express_fee,
                ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
                has_showcase,modified,increment,approve_status,postage_id,product_id,
                auction_point,property_alias,outer_id,is_virtual,is_taobao,is_ex,
                is_timing,is_3d,score,volume,one_station,second_kill,auto_fill,
                violation,is_prepay,ww_status,wap_detail_url,after_sale_id,
                cod_postage_id,sell_promise,job_execution_id)
    select dp_id,num_iid,nick,price,detail_url,title,pic_url,list_time,type,
           props_name,created,promoted_service,cid,seller_cids,props,input_pids,
           input_str,num,valid_thru,delist_time,stuff_status,loc_zip,loc_address,
           loc_city,loc_state,loc_country,loc_district,post_fee,express_fee,
           ems_fee,has_discount,freight_payer,has_invoice,has_warranty,
           has_showcase,modified,increment,approve_status,postage_id,product_id,
           auction_point,property_alias,outer_id,is_virtual,is_taobao,is_ex,
           is_timing,is_3d,score,volume,one_station,second_kill,auto_fill,
           violation,is_prepay,ww_status,wap_detail_url,after_sale_id,
           cod_postage_id,sell_promise,job_execution_id
    from top_item_job_buffer where job_status = 'DOING';
    delete from top_item_job_buffer where job_status = 'DOING';

    -- bakup to top_item_cat
    insert into top_item_cat
                (cid,parent_cid,name,is_parent,job_execution_id)
    select cid,parent_cid,name,is_parent, job_execution_id
    from top_item_cat_job_buffer where job_status = 'DOING';
    delete from top_item_cat_job_buffer where job_status = 'DOING';

    -- bakup to top_shop
    insert into top_shop
                (dp_id,cid,nick,title,pic_path,created,modified,remain_count,
                all_count,used_count,shop_item_score,shop_service_score,
                shop_delivery_score,job_execution_id)
    select dp_id,cid,nick,title,pic_path,created,modified,remain_count,
           all_count,used_count,shop_item_score,shop_service_score,
           shop_delivery_score,job_execution_id
    from top_shop_job_buffer where job_status = 'DOING';
    delete from top_shop_job_buffer where job_status = 'DOING';

    -- bakup to top_item_sku
    insert into top_item_sku
                (properties_name,sku_spec_id,with_hold_quantity,sku_delivery_time,
                 change_prop,sku_id,dp_id,iid,num_iid,properties,quantity,price,
                 outer_id,created,modified,status,job_execution_id)
    select properties_name,sku_spec_id,with_hold_quantity,sku_delivery_time,
           change_prop,sku_id,dp_id,iid,num_iid,properties,quantity,price,
           outer_id,created,modified,status,job_execution_id
    from top_item_sku_job_buffer where job_status = 'DOING';
    delete from top_item_sku_job_buffer where job_status = 'DOING';

    -- bakup to top_refund
    insert into top_refund
                (dp_id,refund_id,tid,oid,modified,status,refund_fee,price,num,
                num_iid,alipay_no,total_fee,buyer_nick,seller_nick,created,
                order_status,good_status,has_good_return,payment,reason,iid,title,
                refund_desc,good_return_time,company_name,sid,address,shipping_type,
                rto_remind_type,rto_exists_timeout,rto_timeout,job_execution_id)
    select dp_id,refund_id,tid,oid,modified,status,refund_fee,price,num,
           num_iid,alipay_no,total_fee,buyer_nick,seller_nick,created,
           order_status,good_status,has_good_return,payment,reason,iid,title,
           refund_desc,good_return_time,company_name,sid,address,shipping_type,
           rto_remind_type,rto_exists_timeout,rto_timeout,job_execution_id
    from top_refund_job_buffer where job_status = 'DOING';
    delete from top_refund_job_buffer where job_status = 'DOING';

    -- bakup to top_item_onsale
    insert into top_item_onsale
                (dp_id,num_iid,nick,price,detail_url,title,pic_url,list_time,type,
                props_name,created,promoted_service,cid,seller_cids,props,
                input_pids,input_str,num,valid_thru,delist_time,stuff_status,
                loc_zip,loc_address,loc_city,loc_state,loc_country,loc_district,
                post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,
                has_warranty,has_showcase,modified,increment,approve_status,
                postage_id,product_id,auction_point,property_alias,outer_id,
                is_virtual,is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
                second_kill,auto_fill,violation,is_prepay,ww_status,wap_detail_url,
                after_sale_id,cod_postage_id,sell_promise,job_execution_id)
    select dp_id,num_iid,nick,price,detail_url,title,pic_url,list_time,type,
           props_name,created,promoted_service,cid,seller_cids,props,
           input_pids,input_str,num,valid_thru,delist_time,stuff_status,
           loc_zip,loc_address,loc_city,loc_state,loc_country,loc_district,
           post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,
           has_warranty,has_showcase,modified,increment,approve_status,
           postage_id,product_id,auction_point,property_alias,outer_id,
           is_virtual,is_taobao,is_ex,is_timing,is_3d,score,volume,one_station,
           second_kill,auto_fill,violation,is_prepay,ww_status,wap_detail_url,
           after_sale_id,cod_postage_id,sell_promise,job_execution_id
    from top_item_onsale_job_buffer where job_status = 'DOING';
    delete from top_item_onsale_job_buffer where job_status = 'DOING';

    -- bakup to top_trade_rate
    insert into top_trade_rate
                (valid_score,dp_id,tid,oid,result,role,nick,created,rated_nick,
                item_title,item_price,content,reply,job_execution_id)
    select valid_score,dp_id,tid,oid,result,role,nick,created,rated_nick,
           item_title,item_price,content,reply,job_execution_id
    from top_trade_rate_job_buffer where job_status = 'DOING';
    delete from top_trade_rate_job_buffer where job_status = 'DOING';

    -- bakup to top_product_seller_cat
    insert into top_product_seller_cat
                (num_iid,seller_cid,job_execution_id)
    select num_iid,seller_cid,job_execution_id
    from top_product_seller_cat_job_buffer where job_status = 'DOING';
    delete from top_product_seller_cat_job_buffer where job_status = 'DOING';

    -- bakup to top_product_prop
    insert into top_product_prop
                (num_iid,pid,vid,job_execution_id)
    select num_iid,pid,vid,job_execution_id
    from top_product_prop_job_buffer where job_status = 'DOING';
    delete from top_product_prop_job_buffer where job_status = 'DOING';

    -- bakup to top_shipping
    insert into top_shipping
                (dp_id,buyer_nick,company_name,created,delivery_end,delivery_start,
                freight_payer,is_success,item_title,modified,order_code,out_sid,
                receiver_mobile,receiver_name,receiver_phone,seller_confirm,seller_nick,
                status,tid,type,job_execution_id)
    select  dp_id,buyer_nick,company_name,created,delivery_end,delivery_start,
            freight_payer,is_success,item_title,modified,order_code,out_sid,
            receiver_mobile,receiver_name,receiver_phone,seller_confirm,seller_nick,
            status,tid,type,job_execution_id
    from top_shipping_job_buffer where job_status = 'DOING';
    delete from top_shipping_job_buffer where job_status = 'DOING';

    -- bakup to top_increment_member
    insert into top_increment_member
                (dp_id,buyer_nick,status,grade,trade_count,trade_amount,
                close_trade_count,close_trade_amount,item_num,biz_order_id,
                group_ids,province,city,avg_price,relation_source,last_trade_time,
                item_close_count,job_execution_id)
    select dp_id,buyer_nick,status,grade,trade_count,trade_amount,
           close_trade_count,close_trade_amount,item_num,biz_order_id,
           group_ids,province,city,avg_price,relation_source,last_trade_time,
           item_close_count,job_execution_id
    from top_increment_member_job_buffer where job_status = 'DOING';
    delete from top_increment_member_job_buffer where job_status = 'DOING';

    -- backup to top_item_seller_cat
    insert into top_item_seller_cat
                (type, cid, parent_cid, name, pic_url, sort_order, created, modified, job_execution_id)
    select type, cid, parent_cid, name, pic_url, sort_order, created, modified, job_execution_id
    from top_item_seller_cat_job_buffer;
    delete from top_item_seller_cat_job_buffer;

    -- backup to top_chat_content
    insert into top_chat_content
                (dp_id, service_staff_id, chat_time, buyer_nick, direction, chat_type,
                content, url_lists, word_lists, length, job_execution_id)
    select dp_id, service_staff_id, chat_time, buyer_nick, direction, chat_type,
           content, url_lists, word_lists, length, job_execution_id
    from top_chat_content_buffer;
    delete from top_chat_content_buffer;

    -- delete to top_trade_promotion_job_buffer
    delete from top_trade_promotion_job_buffer where job_status = 'DOING';

    delete from top_acookie_visitorid_buffer where job_status = 'DOING';

END;

-- etl realtime ----------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_etl_realtime;
CREATE PROCEDURE top_etl_realtime(IN p_job_id bigint(20))
BEGIN
    IF (p_job_id IS NULL) THEN
        SET p_job_id = UNIX_TIMESTAMP()*1000; -- DATE_FORMAT(now(),'%Y%m%d%H%i%s');
    END IF;

    call top_job_buffer_prepare();

    call top_trade_transform(p_job_id);
    call top_user_transform(p_job_id); -- user 一定要放在trade后面执行，表存在依赖关系
    call top_refund_transform();

    call top_item_transform(p_job_id);
    call top_item_props_transform();
    call top_item_seller_cat_transform();

    call top_item_sku_transform();
    call top_item_skus_prop_transform();

    call top_shipping_transform(p_job_id);
    call top_increment_member_transform(p_job_id);
    call top_acookie_transform(p_job_id);

    -- 备份并清除已处理过的数据
    call top_backup_buffer_data();

    -- 备份结束释放空间
    -- call top_job_buffer_release_space();

END;



--//@UNDO
-- SQL to undo the change goes here.
-- ---------------------------------------------------------------------------------------------------------------------------------------
--   定义ETL相关全局变量和存储过程
-- --------------------------------------------------------------------------------------------------------------------------------------

-- SET GLOBAL log_bin_trust_function_creators = 1; -- Utilities
DROP PROCEDURE IF EXISTS  top_job_buffer_reset_job_status;


DROP PROCEDURE IF EXISTS top_job_buffer_release_space;


-- 对JOB将要处理的所有记录的状态（job_status）置为正在处理：DOING，相当于圈定本周期JOB将要处理的所有记录
DROP PROCEDURE IF EXISTS  top_job_buffer_prepare;



-- ETL order, order_item, trade order_item表增加字段----------------------------
DROP PROCEDURE IF EXISTS  top_trade_transform;


-- ETL USER -----------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_user_transform;


DROP PROCEDURE IF EXISTS top_refund_transform;


-- ETL shipping -------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_shipping_transform;


-- etl top_increment_member ----------------------------------------------------
DROP PROCEDURE IF EXISTS top_increment_member_transform;


-- etl product 增加字段---------------------------------------------------------
DROP PROCEDURE IF EXISTS top_item_transform;


DROP PROCEDURE IF EXISTS top_item_props_transform;


DROP PROCEDURE IF EXISTS top_item_props_split;


DROP PROCEDURE IF EXISTS top_item_props_split_pid_vid;


DROP PROCEDURE IF EXISTS top_item_seller_cat_transform;


DROP PROCEDURE IF EXISTS top_item_seller_cid_split;


-- ETL sku 新增加的plt_taobao_product_sku处理sp
DROP PROCEDURE IF EXISTS top_item_sku_transform;


DROP PROCEDURE IF EXISTS top_item_skus_prop_transform;


DROP PROCEDURE IF EXISTS top_item_sku_split;


DROP PROCEDURE IF EXISTS top_item_sku_split_pid_vid;


-- etl acookie --------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_acookie_transform;


-- backup buffer data ----------------------------------------------------------
DROP PROCEDURE IF EXISTS top_backup_buffer_data;


-- etl realtime ----------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_etl_realtime;



