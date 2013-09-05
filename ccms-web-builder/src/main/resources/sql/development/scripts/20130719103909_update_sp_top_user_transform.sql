--// update sp top_user_transform
-- Migration SQL that makes the change goes here.
-- ETL USER --------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_user_transform;
CREATE PROCEDURE top_user_transform(IN p_job_id bigint(20))
BEGIN

    -- 1. 合并所有客人
    DELETE FROM top_jobmid_user;
    INSERT IGNORE INTO top_jobmid_user
                (dp_id,user_id,nick,sex,buyer_credit_lev,buyer_credit_score,
                buyer_credit_good_num,buyer_credit_total_num,seller_credit_lev,
                seller_credit_score,seller_credit_good_num,seller_credit_total_num,
                zip,address,city,state,country,district,created,last_visit,birthday,
                type,alipay_account,alipay_no,has_shop,vip_info,email)
    SELECT dp_id,user_id,nick,sex,buyer_credit_lev,buyer_credit_score,
           buyer_credit_good_num,buyer_credit_total_num,seller_credit_lev,
           seller_credit_score,seller_credit_good_num,seller_credit_total_num,
           zip,address,city,state,country,district,created,last_visit,birthday,
           type,alipay_account,alipay_no,has_shop,vip_info,email
    FROM top_user_job_buffer WHERE job_status = 'DOING'; -- 新先入则旧不入

    -- 3. 根据取到的用户数据，更新plt_taobao_customer
    INSERT IGNORE INTO plt_taobao_customer
                       (customerno,full_name,sex,buyer_credit_lev,buyer_credit_score,
                       buyer_credit_good_num,buyer_credit_total_num,zip,address,city,
                       state,country,district,created,last_visit,vip_info,email,
                       mobile,phone,last_sync,changed)
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
    SET c.sex = IF(u.sex is null, c.sex, u.sex),
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
    INSERT IGNORE INTO plt_taobao_customer
                       (customerno,full_name,zip,address,city,state,district,email,mobile,phone,changed)
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
    SET c.full_name = IF(t.receiver_name is null, c.full_name, substr(trim(t.receiver_name), 1, 50)),
        c.zip = IF(t.receiver_zip is null, c.zip, substr(trim(t.receiver_zip), 1, 20)),
        c.address = IF(t.receiver_address is null, c.address, substr(trim(t.receiver_address), 1, 255)),
        c.city = IF(t.receiver_city is null, c.city, substr(trim(t.receiver_city), 1, 50)),
        c.state = IF(t.receiver_state is null, c.state, substr(trim(t.receiver_state), 1, 50)),
        c.district = IF(t.receiver_district is null, c.district, substr(trim(t.receiver_district), 1, 100)),
        c.last_sync = now(),
        c.changed = now()
    WHERE c.customerno = t.buyer_nick;

    -- 根据订单里的数据，补plt_taobao_customer中没有的部分用户
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
    FROM top_trade_full_job_buffer a;

    -- 根据trade中间表的收货人地址数据补customer信息
    UPDATE plt_taobao_customer c, top_trade_full_job_buffer t
    SET c.full_name = IF(t.receiver_name is null, c.full_name, substr(trim(t.receiver_name), 1, 50)),
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
    SET c.email = IF(t.buyer_email is null,
                    IF(top_is_email(t.buyer_alipay_no),
                      t.buyer_alipay_no,
                      c.email),
                    t.buyer_email),
        c.mobile = IF(t.receiver_mobile is null or t.receiver_mobile='',
                     IF(top_is_mobile(t.buyer_alipay_no),
                       t.buyer_alipay_no,
                       c.mobile),
                     t.receiver_mobile)
     WHERE c.customerno = t.buyer_nick
     AND t.job_status = 'DOING';

    INSERT INTO top_load_log (table_name, updated, etl_job_id) VALUES ('plt_taobao_customer', now(), p_job_id);

END;


--//@UNDO
-- SQL to undo the change goes here.
-- ETL USER --------------------------------------------------------------------
DROP PROCEDURE IF EXISTS top_user_transform;
CREATE PROCEDURE top_user_transform(IN p_job_id bigint(20))
BEGIN

    -- 1. 合并所有客人
    DELETE FROM top_jobmid_user;
    INSERT IGNORE INTO top_jobmid_user
                (dp_id,user_id,nick,sex,buyer_credit_lev,buyer_credit_score,
                buyer_credit_good_num,buyer_credit_total_num,seller_credit_lev,
                seller_credit_score,seller_credit_good_num,seller_credit_total_num,
                zip,address,city,state,country,district,created,last_visit,birthday,
                type,alipay_account,alipay_no,has_shop,vip_info,email)
    SELECT dp_id,user_id,nick,sex,buyer_credit_lev,buyer_credit_score,
           buyer_credit_good_num,buyer_credit_total_num,seller_credit_lev,
           seller_credit_score,seller_credit_good_num,seller_credit_total_num,
           zip,address,city,state,country,district,created,last_visit,birthday,
           type,alipay_account,alipay_no,has_shop,vip_info,email
    FROM top_user_job_buffer WHERE job_status = 'DOING'; -- 新先入则旧不入

    -- 3. 根据取到的用户数据，更新plt_taobao_customer
    INSERT IGNORE INTO plt_taobao_customer
                       (customerno,full_name,sex,buyer_credit_lev,buyer_credit_score,
                       buyer_credit_good_num,buyer_credit_total_num,zip,address,city,
                       state,country,district,created,last_visit,vip_info,email,
                       mobile,phone,last_sync,changed)
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
    SET c.sex = IF(u.sex is null, c.sex, u.sex),
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
    INSERT IGNORE INTO plt_taobao_customer
                       (customerno,full_name,zip,address,city,state,district,email,mobile,phone,changed)
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
    SET c.full_name = IF(t.receiver_name is null, c.full_name, substr(trim(t.receiver_name), 1, 50)),
        c.zip = IF(t.receiver_zip is null, c.zip, substr(trim(t.receiver_zip), 1, 20)),
        c.address = IF(t.receiver_address is null, c.address, substr(trim(t.receiver_address), 1, 255)),
        c.city = IF(t.receiver_city is null, c.city, substr(trim(t.receiver_city), 1, 50)),
        c.state = IF(t.receiver_state is null, c.state, substr(trim(t.receiver_state), 1, 50)),
        c.district = IF(t.receiver_district is null, c.district, substr(trim(t.receiver_district), 1, 100)),
        c.last_sync = now(),
        c.changed = now()
    WHERE c.customerno = t.buyer_nick;

    -- 根据订单里的数据，补plt_taobao_customer中没有的部分用户
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
    FROM top_trade_full_job_buffer a;

    -- 根据trade中间表的收货人地址数据补customer信息
    UPDATE plt_taobao_customer c, top_trade_full_job_buffer t
    SET c.full_name = IF(t.receiver_name is null, c.full_name, substr(trim(t.receiver_name), 1, 50)),
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
    SET c.email = IF(t.buyer_email is null,
                    IF(top_is_email(t.buyer_alipay_no),
                      t.buyer_alipay_no,
                      c.email),
                    t.buyer_email),
        c.mobile = IF(t.receiver_mobile is null or t.receiver_mobile='',
                     IF(top_is_mobile(t.buyer_alipay_no),
                       t.buyer_alipay_no,
                       c.mobile),
                     t.receiver_mobile)
     WHERE c.customerno = t.buyer_nick
     AND t.job_status = 'DOING';

    INSERT INTO top_load_log (table_name, updated, etl_job_id) VALUES ('plt_taobao_customer', now(), p_job_id);

END;

