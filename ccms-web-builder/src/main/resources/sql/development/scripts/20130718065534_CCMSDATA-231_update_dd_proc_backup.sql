--// CCMSDATA-231_update_dd_proc_backup
-- Migration SQL that makes the change goes here.

DROP PROCEDURE IF EXISTS  dop_dd_synch_data;
CREATE PROCEDURE dop_dd_synch_data()
BEGIN
	
	/*
	 * order_id,dangdang_account_id,dp_id,send_order_id 默认程序中添加前缀dd|
	 * */
-- 备份数据
call dop_dd_backup_data();
	
-- 剔除掉不合格的数据
DELETE a 
  FROM process_dd_order a, (select tid,modified,ccms_order_status from plt_taobao_order  pto inner join
  (select shop_id from plt_taobao_shop where shop_id like 'dd|%') pts on pto.dp_id=pts.shop_id) b
 WHERE a.order_id = b.tid
   AND (a.last_modify_time < b.modified
    OR b.ccms_order_status=30);

 
delete a
  FROM process_dd_order_item a
 WHERE NOT EXISTS (SELECT 1 
                 FROM process_dd_order b 
                WHERE a.order_id = b.order_id
                  AND a.job_id = b.job_id);

-- 将临时表的数据，更新到plt_taobao_order表中
REPLACE INTO plt_taobao_order(tid, dp_id, customerno, created, endtime, status, 
        pay_time, total_fee, post_fee, consign_time, ccms_order_status, modified, 
        payment, discount_fee, adjust_fee,  receiver_name, receiver_state, receiver_city,
        receiver_district, receiver_address, receiver_zip, receiver_mobile, receiver_phone)
 SELECT order_id AS tid, 
        dp_id AS dp_id, 
        IF( dangdang_account_id is null, '',dangdang_account_id)  AS customerno,
        create_time AS created,
        end_time AS endtime,
        dd_change_taobao_status(order_state) AS status,
        pay_time AS pay_time,
        (goods_money + account_balance+ gift_card_money + gift_cert_money - postage) as total_fee,
        postage AS post_fee,
        send_time AS consign_time,
        dd_change_ccms_status(order_state) AS ccms_order_status,
        last_modify_time AS modified,
        (total_bargin_price + account_balance+ gift_card_money + gift_cert_money) AS payment,
        (deduct_amount + promo_deduct_amount + gift_card_money + gift_cert_money) AS discount_fee,
        (total_bargin_price - goods_money) AS adjust_fee,
        consignee_name AS receiver_name, 
        consignee_addr_province AS receiver_state, 
        consignee_addr_city AS receiver_city,
        consignee_addr_area AS receiver_district, 
        consignee_addr AS receiver_address, 
        consignee_postcode AS receiver_zip, 
        substr(trim(consignee_mobile_tel), 1, 20) AS receiver_mobile, 
        substr(trim(consignee_tel), 1, 50) AS receiver_phone
   FROM process_dd_order
  ORDER BY last_modify_time;
  
UPDATE plt_taobao_order c, (SELECT b.order_id,
                                  SUM(b.order_count) AS num 
                             FROM (SELECT a.order_id,a.order_count 
                                     FROM process_dd_order_item a 
                                    GROUP BY a.order_id,a.item_id) b 
                                    GROUP BY b.order_id) d
   SET c.num = d.num
 WHERE c.tid = d.order_id;
   
  
-- 将临时表的数据，更新到plt_taobao_order_item中
REPLACE INTO plt_taobao_order_item(oid, tid, dp_id, total_fee, customerno,discount_fee, 
        adjust_fee, payment, num, num_iid, title, status, created, ccms_order_status)
 SELECT CONCAT(order_id, item_id) AS oid,
        order_id AS tid,
        dp_id AS dp_id,
        (order_count * unit_price) AS total_fee,
        '' AS customerno,
        NULL AS discount_fee,
        0 AS adjust_fee,
        (order_count * unit_price) AS payment,
        order_count AS num,
        CONCAT('dd|',item_id) AS num_iid,
        item_name AS title,
        '' AS status,
        now() AS created,
        0 AS ccms_order_status
   FROM process_dd_order_item;

UPDATE plt_taobao_order_item a, (select * from (SELECT * FROM process_dd_order ORDER BY last_modify_time) c group by c.order_id ) b
   SET a.status = dd_change_taobao_status(b.order_state),
       a.customerno = IF(b.dangdang_account_id is null, '',b.dangdang_account_id),
       a.created = b.create_time,
       a.endtime = b.end_time,
       a.pay_time = b.pay_time,
       a.consign_time = b.send_time,
       a.ccms_order_status = dd_change_ccms_status(b.order_state),
       a.modified = b.last_modify_time
 WHERE a.tid = order_id;
 
 -- 将临时表的数据，更新到plt_taobao_customer中
 REPLACE INTO plt_taobao_customer(customerno, full_name, zip, address, city, state, 
         country, district, mobile, phone, last_sync, changed)
  SELECT dangdang_account_id AS customerno,
         consignee_name AS full_name,
         consignee_postcode AS zip,
         consignee_addr AS address,
         consignee_addr_city AS city,
         consignee_addr_province AS state,
         consignee_addr_state AS country,
         consignee_addr_area AS district, 
         substr(trim(consignee_mobile_tel), 1, 20) AS mobile,
          substr(trim(consignee_tel), 1, 50) AS phone,
         now() AS last_sync,
         now() AS changed
    FROM process_dd_order where dangdang_account_id is not null
   ORDER BY last_modify_time;
 
 -- 将临时表的数据，更新到plt_taobao_shipping中
REPLACE INTO plt_taobao_shipping(order_code, tid, dp_id, buyer_nick, out_sid, receiver_mobile, 
        receiver_name, receiver_phone, company_name, is_success, created, modified)
 SELECT order_id AS order_code,
        order_id AS tid,
        dp_id AS dp_id,
        dangdang_account_id AS buyer_nick,
        send_order_id AS out_sid,
         substr(trim(consignee_mobile_tel), 1, 20) AS receiver_mobile,
        consignee_name AS receiver_name,
         substr(trim(consignee_tel), 1, 50) AS receiver_phone,
        send_company AS company_name,
        IF(end_time,1,0) AS is_success,
        send_time AS created,
        last_modify_time AS modified
   FROM process_dd_order
  ORDER BY last_modify_time;
  
-- 将临时表的数据更新到plt_taobao_product
INSERT IGNORE INTO plt_taobao_product(num_iid, title, price, dp_id, outer_id, approve_status)
SELECT CONCAT('dd|',item_id) AS num_iid,
       item_name AS title,
       unit_price AS price,
       dp_id AS dp_id,
       outer_item_id AS outer_id,
       'onsale' AS approve_status
  FROM process_dd_order_item;

-- 更新店铺的最后更新时间
UPDATE plt_taobao_shop a, (SELECT  dp_id,
                                  MAX(create_time) max_create_time 
                             FROM process_dd_order
                            GROUP BY dp_id) b
   SET a.order_created_latest = b.max_create_time
 WHERE a.shop_id = b.dp_id
   AND a.order_created_latest < b.max_create_time;
       
 

-- 将中间数据插入plt_taobao_product中
REPLACE INTO plt_taobao_product(num_iid, title, 
        price, approve_status, dp_id, outer_id)
 SELECT CONCAT("dd|",item_id) AS num_iid,
        item_name AS title,
        maximum_price AS price,
        dd_change_approve_status(item_state) approve_status,
        dp_id AS dp_id,
        outer_item_id AS outer_id
   FROM process_dd_product;

-- 暂用 copy表测试
-- 剔除掉不合格的数据
delete a
  FROM process_dd_return_item a
 WHERE NOT EXISTS (SELECT 1 
                 FROM process_dd_return b 
                WHERE a.return_exchange_code = b.return_exchange_code
                  AND a.job_id = b.job_id);


 
-- 用临时表，更新plt_taobao_order的数据
UPDATE plt_taobao_order a, process_dd_return b
   SET a.refund_fee = IF( b.order_result = 1 , b.order_money, a.refund_fee),
       a.ccms_order_status = IF( b.order_result = 1 && a.payment<=b.order_money, 30, a.ccms_order_status),
       a.status = IF( b.order_result = 1 && a.payment<=b.order_money, 'TRADE_CLOSED', a.status),
       b.update_status = 2
 WHERE a.tid =b.order_id;

-- 用临时表，更新plt_taobao_order_item的数据
UPDATE plt_taobao_order_item a, process_dd_return_item b, process_dd_return c
   SET a.refund_fee = (b.unit_price * b.order_count),
       a.ccms_order_status = IF(c.order_result=1, 30, a.ccms_order_status),
       a.status = IF(c.order_result=1, 'TRADE_CLOSED', a.status),
       a.refund_status = dd_change_return_status(c.order_result),
       b.update_status = 2
 WHERE a.oid = CONCAT(c.order_id, b.item_id)
   AND b.return_exchange_code = c.return_exchange_code;


delete from process_dd_order;
delete from process_dd_order_item;
delete from process_dd_product;
delete from process_dd_return;
delete from process_dd_return_item;
delete from process_dd_specila_item;
delete from process_dd_suborder;

END;

DROP PROCEDURE IF EXISTS  dop_dd_backup_data;
CREATE PROCEDURE dop_dd_backup_data()
BEGIN
 INSERT INTO process_dd_order_backups (order_id, label, account_balance, buyer_pay_mode, deduct_amount, gift_card_money, gift_cert_money, goods_money, postage, promo_deduct_amount, total_bargin_price, last_modify_time, message, order_state, outer_order_id, parent_order_id, receipt_details, receipt_money, receipt_name, remark, consignee_addr, consignee_addr_area, consignee_addr_city, consignee_addr_province, consignee_addr_state, consignee_mobile_tel, consignee_name, consignee_postcode, consignee_tel, dangdang_account_id, send_company, send_goods_mode, send_order_id, create_time, pay_time, send_time, end_time, dp_id, job_id, update_status)
 select order_id, label, account_balance, buyer_pay_mode, deduct_amount, gift_card_money, gift_cert_money, goods_money, postage, promo_deduct_amount, total_bargin_price, last_modify_time, message, order_state, outer_order_id, parent_order_id, receipt_details, receipt_money, receipt_name, remark, consignee_addr, consignee_addr_area, consignee_addr_city, consignee_addr_province, consignee_addr_state, consignee_mobile_tel, consignee_name, consignee_postcode, consignee_tel, dangdang_account_id, send_company, send_goods_mode, send_order_id, create_time, pay_time, send_time, end_time, dp_id, job_id, update_status from process_dd_order;

 INSERT INTO process_dd_order_item_backups ( order_id, item_id, outer_item_id, item_name, item_type, special_attribute, market_price, unit_price, order_count, send_goods_count, dp_id, job_id, update_status) select order_id, item_id, outer_item_id, item_name, item_type, special_attribute, market_price, unit_price, order_count, send_goods_count, dp_id, job_id, update_status from process_dd_order_item;
 INSERT INTO process_dd_product_backups (item_id, item_name, item_subhead, brand, model, maximum_price, floor_price, vip_price_type, item_state, outer_item_id, stock_count, dp_id, job_id, update_status) select item_id, item_name, item_subhead, brand, model, maximum_price, floor_price, vip_price_type, item_state, outer_item_id, stock_count, dp_id, job_id, update_status from process_dd_product;
 INSERT INTO process_dd_return_backups (order_id, order_money, order_result, order_status, order_time, return_exchange_code, return_exchange_orders_appr_status, return_exchange_status, dp_id, job_id, update_status) SELECT order_id, order_money, order_result, order_status, order_time, return_exchange_code, return_exchange_orders_appr_status, return_exchange_status, dp_id, job_id, update_status from process_dd_return;
 INSERT INTO process_dd_return_item_backups (item_id, item_name, return_exchange_code, unit_price, order_count, dp_id, job_id, update_status) select item_id, item_name, return_exchange_code, unit_price, order_count, dp_id, job_id, update_status from process_dd_return_item;
 INSERT INTO process_dd_specila_item_backups (outer_item_id, item_id, special_attribute, special_attribute_class, special_attribute_seq, stock_count, subitem_id, unit_price, dp_id, job_id, update_status) select outer_item_id, item_id, special_attribute, special_attribute_class, special_attribute_seq, stock_count, subitem_id, unit_price, dp_id, job_id, update_status from process_dd_specila_item;
 INSERT INTO process_dd_suborder_backups (order_id, suborder_id, outer_suborder_id, dp_id, job_id, update_status) select order_id, suborder_id, outer_suborder_id, dp_id, job_id, update_status from process_dd_suborder;
END;


--//@UNDO
-- SQL to undo the change goes here.

DROP PROCEDURE IF EXISTS  dop_dd_synch_data;
CREATE PROCEDURE dop_dd_synch_data()
BEGIN
	
	/*
	 * order_id,dangdang_account_id,dp_id,send_order_id 默认程序中添加前缀dd|
	 * */
-- 备份数据
insert into process_dd_order_backups  select * from process_dd_order;
insert into process_dd_order_item_backups  select * from process_dd_order_item;
insert into process_dd_product_backups  select * from process_dd_product;
insert into process_dd_return_backups  select * from process_dd_return;
insert into process_dd_return_item_backups  select * from process_dd_return_item;
insert into process_dd_specila_item_backups  select * from process_dd_specila_item;
insert into process_dd_suborder_backups  select * from process_dd_suborder;	
	
-- 剔除掉不合格的数据
DELETE a 
  FROM process_dd_order a, (select tid,modified,ccms_order_status from plt_taobao_order  pto inner join
  (select shop_id from plt_taobao_shop where shop_id like 'dd|%') pts on pto.dp_id=pts.shop_id) b
 WHERE a.order_id = b.tid
   AND (a.last_modify_time < b.modified
    OR b.ccms_order_status=30);

 
delete a
  FROM process_dd_order_item a
 WHERE NOT EXISTS (SELECT 1 
                 FROM process_dd_order b 
                WHERE a.order_id = b.order_id
                  AND a.job_id = b.job_id);

-- 将临时表的数据，更新到plt_taobao_order表中
REPLACE INTO plt_taobao_order(tid, dp_id, customerno, created, endtime, status, 
        pay_time, total_fee, post_fee, consign_time, ccms_order_status, modified, 
        payment, discount_fee, adjust_fee,  receiver_name, receiver_state, receiver_city,
        receiver_district, receiver_address, receiver_zip, receiver_mobile, receiver_phone)
 SELECT order_id AS tid, 
        dp_id AS dp_id, 
        IF( dangdang_account_id is null, '',dangdang_account_id)  AS customerno,
        create_time AS created,
        end_time AS endtime,
        dd_change_taobao_status(order_state) AS status,
        pay_time AS pay_time,
        (goods_money + account_balance+ gift_card_money + gift_cert_money - postage) as total_fee,
        postage AS post_fee,
        send_time AS consign_time,
        dd_change_ccms_status(order_state) AS ccms_order_status,
        last_modify_time AS modified,
        (total_bargin_price + account_balance+ gift_card_money + gift_cert_money) AS payment,
        (deduct_amount + promo_deduct_amount + gift_card_money + gift_cert_money) AS discount_fee,
        (total_bargin_price - goods_money) AS adjust_fee,
        consignee_name AS receiver_name, 
        consignee_addr_province AS receiver_state, 
        consignee_addr_city AS receiver_city,
        consignee_addr_area AS receiver_district, 
        consignee_addr AS receiver_address, 
        consignee_postcode AS receiver_zip, 
        substr(trim(consignee_mobile_tel), 1, 20) AS receiver_mobile, 
        substr(trim(consignee_tel), 1, 50) AS receiver_phone
   FROM process_dd_order
  ORDER BY last_modify_time;
  
UPDATE plt_taobao_order c, (SELECT b.order_id,
                                  SUM(b.order_count) AS num 
                             FROM (SELECT a.order_id,a.order_count 
                                     FROM process_dd_order_item a 
                                    GROUP BY a.order_id,a.item_id) b 
                                    GROUP BY b.order_id) d
   SET c.num = d.num
 WHERE c.tid = d.order_id;
   
  
-- 将临时表的数据，更新到plt_taobao_order_item中
REPLACE INTO plt_taobao_order_item(oid, tid, dp_id, total_fee, customerno,discount_fee, 
        adjust_fee, payment, num, num_iid, title, status, created, ccms_order_status)
 SELECT CONCAT(order_id, item_id) AS oid,
        order_id AS tid,
        dp_id AS dp_id,
        (order_count * unit_price) AS total_fee,
        '' AS customerno,
        NULL AS discount_fee,
        0 AS adjust_fee,
        (order_count * unit_price) AS payment,
        order_count AS num,
        CONCAT('dd|',item_id) AS num_iid,
        item_name AS title,
        '' AS status,
        now() AS created,
        0 AS ccms_order_status
   FROM process_dd_order_item;

UPDATE plt_taobao_order_item a, (select * from (SELECT * FROM process_dd_order ORDER BY last_modify_time) c group by c.order_id ) b
   SET a.status = dd_change_taobao_status(b.order_state),
       a.customerno = IF(b.dangdang_account_id is null, '',b.dangdang_account_id),
       a.created = b.create_time,
       a.endtime = b.end_time,
       a.pay_time = b.pay_time,
       a.consign_time = b.send_time,
       a.ccms_order_status = dd_change_ccms_status(b.order_state),
       a.modified = b.last_modify_time
 WHERE a.tid = order_id;
 
 -- 将临时表的数据，更新到plt_taobao_customer中
 REPLACE INTO plt_taobao_customer(customerno, full_name, zip, address, city, state, 
         country, district, mobile, phone, last_sync, changed)
  SELECT dangdang_account_id AS customerno,
         consignee_name AS full_name,
         consignee_postcode AS zip,
         consignee_addr AS address,
         consignee_addr_city AS city,
         consignee_addr_province AS state,
         consignee_addr_state AS country,
         consignee_addr_area AS district, 
         substr(trim(consignee_mobile_tel), 1, 20) AS mobile,
          substr(trim(consignee_tel), 1, 50) AS phone,
         now() AS last_sync,
         now() AS changed
    FROM process_dd_order where dangdang_account_id is not null
   ORDER BY last_modify_time;
 
 -- 将临时表的数据，更新到plt_taobao_shipping中
REPLACE INTO plt_taobao_shipping(order_code, tid, dp_id, buyer_nick, out_sid, receiver_mobile, 
        receiver_name, receiver_phone, company_name, is_success, created, modified)
 SELECT order_id AS order_code,
        order_id AS tid,
        dp_id AS dp_id,
        dangdang_account_id AS buyer_nick,
        send_order_id AS out_sid,
         substr(trim(consignee_mobile_tel), 1, 20) AS receiver_mobile,
        consignee_name AS receiver_name,
         substr(trim(consignee_tel), 1, 50) AS receiver_phone,
        send_company AS company_name,
        IF(end_time,1,0) AS is_success,
        send_time AS created,
        last_modify_time AS modified
   FROM process_dd_order
  ORDER BY last_modify_time;
  
-- 将临时表的数据更新到plt_taobao_product
INSERT IGNORE INTO plt_taobao_product(num_iid, title, price, dp_id, outer_id, approve_status)
SELECT CONCAT('dd|',item_id) AS num_iid,
       item_name AS title,
       unit_price AS price,
       dp_id AS dp_id,
       outer_item_id AS outer_id,
       'onsale' AS approve_status
  FROM process_dd_order_item;

-- 更新店铺的最后更新时间
UPDATE plt_taobao_shop a, (SELECT  dp_id,
                                  MAX(create_time) max_create_time 
                             FROM process_dd_order
                            GROUP BY dp_id) b
   SET a.order_created_latest = b.max_create_time
 WHERE a.shop_id = b.dp_id
   AND a.order_created_latest < b.max_create_time;
       
 

-- 将中间数据插入plt_taobao_product中
REPLACE INTO plt_taobao_product(num_iid, title, 
        price, approve_status, dp_id, outer_id)
 SELECT CONCAT("dd|",item_id) AS num_iid,
        item_name AS title,
        maximum_price AS price,
        dd_change_approve_status(item_state) approve_status,
        dp_id AS dp_id,
        outer_item_id AS outer_id
   FROM process_dd_product;

-- 暂用 copy表测试
-- 剔除掉不合格的数据
delete a
  FROM process_dd_return_item a
 WHERE NOT EXISTS (SELECT 1 
                 FROM process_dd_return b 
                WHERE a.return_exchange_code = b.return_exchange_code
                  AND a.job_id = b.job_id);


 
-- 用临时表，更新plt_taobao_order的数据
UPDATE plt_taobao_order a, process_dd_return b
   SET a.refund_fee = IF( b.order_result = 1 , b.order_money, a.refund_fee),
       a.ccms_order_status = IF( b.order_result = 1 && a.payment<=b.order_money, 30, a.ccms_order_status),
       a.status = IF( b.order_result = 1 && a.payment<=b.order_money, 'TRADE_CLOSED', a.status),
       b.update_status = 2
 WHERE a.tid =b.order_id;

-- 用临时表，更新plt_taobao_order_item的数据
UPDATE plt_taobao_order_item a, process_dd_return_item b, process_dd_return c
   SET a.refund_fee = (b.unit_price * b.order_count),
       a.ccms_order_status = IF(c.order_result=1, 30, a.ccms_order_status),
       a.status = IF(c.order_result=1, 'TRADE_CLOSED', a.status),
       a.refund_status = dd_change_return_status(c.order_result),
       b.update_status = 2
 WHERE a.oid = CONCAT(c.order_id, b.item_id)
   AND b.return_exchange_code = c.return_exchange_code;


delete from process_dd_order;
delete from process_dd_order_item;
delete from process_dd_product;
delete from process_dd_return;
delete from process_dd_return_item;
delete from process_dd_specila_item;
delete from process_dd_suborder;

END;
DROP PROCEDURE IF EXISTS  dop_dd_backup_data;

