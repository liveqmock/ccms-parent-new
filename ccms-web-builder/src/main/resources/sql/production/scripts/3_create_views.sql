###--------------------------------------------------------------------------------------------------------------------
###   View: vw_taobao_customer
###   淘宝客户信息统一视图(基础版)
###--------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM = MERGE  VIEW vw_taobao_customer AS
SELECT
    u.uni_id          AS uni_id,    #客户统一ID
    taobao.customerno AS customerno ,  #TODO:所有视图中的客户ID字段必须统一命名成customerno
    u.full_name       AS full_name, #客户姓名
    u.sex AS sex,  #性别
    u.job AS job,  #职业
    YEAR(CURRENT_DATE)-u.birth_year AS age,  #年龄
    u.birthday, #生日
    u.email email,
    u.mobile mobile,
    u.state state, #省份
    u.city city,     #城市
    u.district,      #区域
    u.address,     #地址
    u.zip,            #邮编
    CASE WHEN u.mobile REGEXP '^(1[3,4,5,8]){1}[[:digit:]]{9}$' THEN '1' ELSE '0' END AS is_mobile_valid, #手机号是否有效
    CASE WHEN u.email REGEXP '[A-Za-z0-9._-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,4}$' THEN '1' ELSE '0' END AS is_email_valid, #email是否有效
    taobao.vip_info,              #客户全站等级
    taobao.buyer_credit_lev, #买家信用等级
    taobao.created,              #淘宝用户注册时间
    round (CASE WHEN (taobao.buyer_credit_total_num > 0)
           THEN ((taobao.buyer_credit_good_num*1.0 / taobao.buyer_credit_total_num) * 100)
           ELSE NULL END, 1)                       AS buyer_good_ratio,  #买家好评率
    DATE_FORMAT(CURRENT_DATE, '%Y年%m月%d日')      AS ymd,               #当天年月日
    DATE_FORMAT(CURRENT_DATE, '%Y年%m月')          AS ym,                #当天年月
    crm.grade AS grade,									#会员等级
    IFNULL(label.label_name, '') AS　label_name			#客户标签
FROM  plt_taobao_customer taobao
    Left Join uni_customer_plat cp On cp.customerno = taobao.customerno And cp.plat_code = 'taobao'
    Left Join uni_customer u On u.uni_id = cp.uni_id
    Left Join plt_taobao_crm_member crm on taobao.customerno = crm.customerno
    Left Join plt_kfgzt_customer_label label on taobao.customerno = label.customerno;


###--------------------------------------------------------------------------------------------------------------------
###   View: vm_taobao_order
###   订单信息视图(基础版)
###--------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM = MERGE VIEW  vm_taobao_order AS
  SELECT
    plt_taobao_order.tid,
    plt_taobao_order.customerno,
    plt_taobao_order.receiver_name,
    plt_taobao_order.created,
    plt_taobao_order.pay_time,
    plt_taobao_order.consign_time,
    plt_taobao_shop.shop_name,
    plt_taobao_order.payment,
    plt_taobao_order.post_fee ,
    plt_taobao_order.receiver_mobile,
    plt_taobao_order.buyer_alipay_no,
    plt_taobao_order.buyer_email,
    plt_taobao_order.total_fee,
    tds_order_status.status_name as status,
    b.mobile as buyer_mobile,
    s.out_sid  as out_sid,
    s.company_name as company_name,
    s.created  as shipping_created
FROM plt_taobao_order
    join plt_taobao_shop on dp_id = shop_id left
    join tds_order_status on status_id = ccms_order_status
    join plt_taobao_customer b on plt_taobao_order.customerno = b.customerno
    left join plt_taobao_shipping s on plt_taobao_order.tid= s.tid;
