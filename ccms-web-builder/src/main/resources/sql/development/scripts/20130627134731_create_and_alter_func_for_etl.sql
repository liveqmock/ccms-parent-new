--// create and alter func for etl
-- Migration SQL that makes the change goes here.
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- 定义ETL相关函数
-- ---------------------------------------------------------------------------------------------------------------------------------------

 SET GLOBAL log_bin_trust_function_creators = 1; -- Utilities

DROP FUNCTION IF exists top_trade_order_status_index;
CREATE FUNCTION top_trade_order_status_index(status varchar(50))
RETURNS int
BEGIN
  CASE status
    WHEN 'TRADE_NO_CREATE_PAY' THEN RETURN 1;
    WHEN 'WAIT_BUYER_PAY' THEN RETURN 2;
    WHEN 'WAIT_SELLER_SEND_GOODS' THEN RETURN 3;
    WHEN 'WAIT_BUYER_CONFIRM_GOODS' THEN RETURN 4;
    WHEN 'TRADE_BUYER_SIGNED' THEN RETURN 5;
    WHEN 'TRADE_FINISHED' THEN RETURN 6;
    WHEN 'TRADE_CLOSED_BY_TAOBAO' THEN RETURN 6;
    WHEN 'TRADE_CLOSED' THEN RETURN 6;
    ELSE RETURN null;
  END CASE;
END;


DROP FUNCTION IF EXISTS top_is_mobile;
CREATE FUNCTION top_is_mobile(phone varchar(50))
RETURNS boolean
BEGIN
  IF phone IS null THEN
    RETURN false;
  END IF;
  RETURN phone regexp '^(13[0-9]|14[7]|15[0-9]|18[0-9]){1}[0-9]{8}$';
END;


DROP FUNCTION IF EXISTS top_is_email;
CREATE FUNCTION top_is_email(email varchar(100))
RETURNS boolean
BEGIN
  IF email IS null THEN
    RETURN false;
  END if;
  RETURN email REGEXP '[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,4}';
END;


DROP FUNCTION IF EXISTS top_trade_ccms_order_status;
CREATE FUNCTION top_trade_ccms_order_status(status varchar(50))
RETURNS int
BEGIN
  CASE status
    WHEN 'WAIT_BUYER_PAY' THEN RETURN 10;
    WHEN 'WAIT_SELLER_SEND_GOODS' THEN RETURN 21;
    WHEN 'WAIT_BUYER_CONFIRM_GOODS' THEN RETURN 22;
    WHEN 'TRADE_FINISHED' THEN RETURN 23;
    WHEN 'TRADE_BUYER_SIGNED' THEN RETURN 22;
    WHEN 'TRADE_NO_CREATE_PAY' THEN RETURN 10;
    WHEN 'FRONT_NOPAID_FINAL_NOPAID' THEN RETURN 11;
    WHEN 'FRONT_PAID_FINAL_NOPAID' THEN RETURN 12;
    WHEN 'TRADE_CLOSED' THEN RETURN 32;
    WHEN 'TRADE_CLOSED_BY_TAOBAO' THEN RETURN 31;
    WHEN 'SELLER_CONSIGNED_PART' THEN RETURN 22;
    ELSE RETURN null;
  END CASE;
END;

-- 查找nick对应的name
DROP FUNCTION IF EXISTS top_caculate_customer_name;
CREATE FUNCTION top_caculate_customer_name (nick varchar(50)) RETURNS varchar(50)
BEGIN
  DECLARE p_name varchar(50);
  
  SELECT name 
    INTO p_name 
    FROM top_summary_user 
   WHERE buyer_nick = nick 
   ORDER BY occurance DESC, last_datetime DESC 
   LIMIT 1;
   
  RETURN p_name;
END;


-- 计算nick对应的手机号码
DROP FUNCTION IF EXISTS top_caculate_customer_mobile;
CREATE FUNCTION top_caculate_customer_mobile(nick varchar(50)) RETURNS varchar(50)
BEGIN
  DECLARE p_mobile varchar(50);
  DECLARE username varchar(50);
  
  SELECT top_caculate_customer_name(nick) INTO username;
  
  SELECT TRIM(receiver_mobile) 
    INTO p_mobile 
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick
     AND receiver_name = username 
     AND top_is_mobile(receiver_mobile)   
   ORDER BY created DESC 
   LIMIT 1;

    IF !top_is_mobile(p_mobile) THEN -- 不符合手机号码规则，尝试使用buyer_alipay_no
        SET p_mobile = null;
        SELECT TRIM(buyer_alipay_no)   INTO p_mobile   FROM top_trade_full_job_buffer 
        WHERE buyer_nick = nick   AND receiver_name = username   LIMIT 1;
     
        IF !top_is_mobile(p_mobile) THEN -- 不符合手机号码规则，尝试使用原来的手机号，如果原来的手机号不存在，则值为null
           SET p_mobile = null;
           SELECT mobile   INTO p_mobile  FROM top_tmp_customer_tobe_updated 
           WHERE customerno = nick ;
         END IF;
    END IF;
  
  RETURN p_mobile;
END;

-- 计算用户的email
DROP FUNCTION IF EXISTS top_caculate_customer_email;
CREATE FUNCTION top_caculate_customer_email(nick varchar(50)) RETURNS varchar(100)
BEGIN
  DECLARE p_email varchar(100);
  DECLARE username varchar(50);

  SELECT top_caculate_customer_name(nick) INTO username;
   
  SELECT TRIM(buyer_email) 
    INTO p_email 
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick 
     AND receiver_name = username
     AND top_is_email(buyer_email)
   ORDER BY created DESC 
   LIMIT 1;

  IF !top_is_email(p_email) THEN -- 不符合邮箱规则，尝试使用buyer_alipay_no
    SET p_email = null;
    SELECT TRIM(buyer_alipay_no) 
      INTO p_email 
      FROM top_trade_full_job_buffer 
     WHERE buyer_nick = nick 
       AND receiver_name = username 
     LIMIT 1;
     
    IF !top_is_email(p_email) THEN -- 不符合邮箱规则，尝试使用原有的email
      SET p_email = null;
      SELECT email 
        INTO p_email 
        FROM top_tmp_customer_tobe_updated
       WHERE customerno = nick;
    END if;
  END if;
  
  RETURN p_email;
END;

-- 计算用户的地址
DROP FUNCTION IF EXISTS top_caculate_customer_address;
CREATE FUNCTION top_caculate_customer_address(nick varchar(50)) RETURNS varchar(255)
BEGIN
  DECLARE p_address varchar(255);
  
  SELECT receiver_address 
    INTO p_address
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick 
     AND receiver_name = top_caculate_customer_name(nick) 
   ORDER BY created DESC 
   LIMIT 1;

  IF p_address IS null THEN
    SELECT address 
      INTO p_address 
      FROM top_tmp_customer_tobe_updated 
     WHERE customerno = nick;
  END IF;
  RETURN p_address;
END;

-- 计算用户的邮编
DROP FUNCTION IF EXISTS top_caculate_customer_zip;
CREATE FUNCTION top_caculate_customer_zip(nick varchar(50)) RETURNS varchar(20)
BEGIN
  DECLARE p_zip varchar(20);
   
  SELECT receiver_zip 
    INTO p_zip
    FROM top_trade_full_job_buffer  
   WHERE buyer_nick= nick 
     AND receiver_name = top_caculate_customer_name(nick) 
   ORDER BY created desc LIMIT 1;

  IF p_zip IS null THEN
    SELECT zip INTO p_zip 
      FROM top_tmp_customer_tobe_updated 
     WHERE customerno = nick;
  END if;
  
  RETURN p_zip;
END;

-- 计算用户的城市
DROP FUNCTION IF EXISTS top_caculate_customer_city;
CREATE FUNCTION top_caculate_customer_city(nick varchar(50)) RETURNS varchar(50)
BEGIN
  DECLARE p_city varchar(50);

  SELECT receiver_city 
    INTO p_city
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick 
     AND receiver_name = top_caculate_customer_name(nick) 
   ORDER BY created DESC 
   LIMIT 1;

  IF p_city IS null THEN
    SELECT city 
      INTO p_city 
      FROM top_tmp_customer_tobe_updated
     WHERE customerno = nick;
  END if;
  
  RETURN p_city;
END;

-- 计算用户所在的省份
DROP FUNCTION IF EXISTS top_caculate_customer_state;
CREATE FUNCTION top_caculate_customer_state(nick varchar(50)) RETURNS varchar(50)
BEGIN
  DECLARE p_state varchar(50);
  
  SELECT receiver_state 
    INTO p_state
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick 
     AND receiver_name = top_caculate_customer_name(nick) 
   ORDER BY created DESC 
   LIMIT 1;

  IF p_state IS null THEN
    SELECT state 
      INTO p_state
      FROM top_tmp_customer_tobe_updated
     WHERE customerno = nick;
  END IF;
  
  RETURN p_state;
END;

-- 计算用户的district
DROP FUNCTION IF EXISTS top_caculate_customer_district;
CREATE FUNCTION top_caculate_customer_district(nick varchar(50)) RETURNS varchar(100)
BEGIN
  DECLARE p_district varchar(100);

  SELECT receiver_district 
    INTO p_district
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick 
     AND receiver_name = top_caculate_customer_name(nick)
   ORDER BY created DESC 
   LIMIT 1;

  IF p_district IS null THEN
    SELECT district 
      INTO p_district 
      FROM top_tmp_customer_tobe_updated 
     WHERE customerno = nick;
  END if;
  
  RETURN p_district;
END;

-- 计算用户的phone
DROP FUNCTION IF EXISTS top_caculate_customer_phone;
CREATE FUNCTION top_caculate_customer_phone(nick varchar(50)) RETURNS varchar(50)
BEGIN
  DECLARE p_phone varchar(50);

  SELECT receiver_phone 
    INTO p_phone 
    FROM top_trade_full_job_buffer 
   WHERE buyer_nick = nick
     AND receiver_name = top_caculate_customer_name(nick)
   ORDER BY created DESC 
   LIMIT 1;
  
  IF p_phone IS null THEN
    SELECT receiver_phone 
      INTO p_phone 
      FROM top_trade_full_job_buffer 
     WHERE buyer_nick = nick
     ORDER BY created DESC 
     LIMIT 1;
    IF p_phone IS null THEN
      SELECT phone 
        INTO p_phone 
        FROM top_tmp_customer_tobe_updated 
       WHERE customerno = nick;
    END if ;
  END if;
  
  RETURN p_phone;
END;

-- 计算用户的真实姓名
DROP FUNCTION IF EXISTS top_caculate_customer_full_name;
CREATE FUNCTION top_caculate_customer_full_name(nick varchar(50)) RETURNS varchar(50)
BEGIN
  DECLARE p_full_name varchar(50);

  SELECT top_caculate_customer_name(nick) INTO p_full_name;
  IF p_full_name IS null THEN
    SELECT full_name 
      INTO p_full_name 
      FROM top_tmp_customer_tobe_updated 
     WHERE customerno = nick;
  END if;
  
  RETURN p_full_name;
END;

-- 截取手机号的函数
DROP FUNCTION IF EXISTS substr_num;
CREATE FUNCTION substr_num(str VARCHAR(50))
    RETURNS VARCHAR(50)
BEGIN
  DECLARE reg VARCHAR(50);
  DECLARE flag boolean;
  DECLARE str_len int;
  DECLARE res VARCHAR(50);
  DECLARE tmp_str VARCHAR(50);
  DECLARE i int;
  SET reg = '^[0-9]+$';
  SET flag = false;
  SET str_len = length(str);
  SET res = '';
  SET i = 1;
  WHILE i <= str_len do
    SET tmp_str = SUBSTRING(str, i, 1);
    IF tmp_str regexp reg THEN
      SET res = CONCAT(res,tmp_str);
      SET flag = true;
    ELSEIF flag THEN
      RETURN res;
    END IF;
    SET i = i+1;
  END WHILE;
  RETURN res;
END;


--//@UNDO
-- SQL to undo the change goes here.
-- ---------------------------------------------------------------------------------------------------------------------------------------
-- 定义ETL相关函数
-- ---------------------------------------------------------------------------------------------------------------------------------------

-- SET GLOBAL log_bin_trust_function_creators = 1; -- Utilities

DROP FUNCTION IF exists top_trade_order_status_index;



DROP FUNCTION IF EXISTS top_is_mobile;



DROP FUNCTION IF EXISTS top_trade_ccms_order_status;


-- 查找nick对应的name
DROP FUNCTION IF EXISTS top_caculate_customer_name;



-- 计算nick对应的手机号码
DROP FUNCTION IF EXISTS top_caculate_customer_mobile;


-- 计算用户的email
DROP FUNCTION IF EXISTS top_caculate_customer_email;


-- 计算用户的地址
DROP FUNCTION IF EXISTS top_caculate_customer_address;


-- 计算用户的邮编
DROP FUNCTION IF EXISTS top_caculate_customer_zip;


-- 计算用户的城市
DROP FUNCTION IF EXISTS top_caculate_customer_city;


-- 计算用户所在的省份
DROP FUNCTION IF EXISTS top_caculate_customer_state;


-- 计算用户的district
DROP FUNCTION IF EXISTS top_caculate_customer_district;


-- 计算用户的phone
DROP FUNCTION IF EXISTS top_caculate_customer_phone;


-- 计算用户的真实姓名
DROP FUNCTION IF EXISTS top_caculate_customer_full_name;


-- 截取手机号的函数
DROP FUNCTION IF EXISTS substr_num;


