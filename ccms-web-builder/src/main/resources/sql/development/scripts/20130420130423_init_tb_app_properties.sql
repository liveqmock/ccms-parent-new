--// init tb app properties
-- Migration SQL that makes the change goes here.

#初始化应用程序配置信息
#(按照“开发环境”参数进行配置，发布到“测试”或者“生产环境”时需要重新配置)
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (10, 'CCMS', 'ccms_password', '5325ddba94c446beac479f67bc06a180', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (11, 'CCMS', 'ccms_trad_version', 'Retail', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (12, 'CCMS', 'ccms_upload_dir', 'd:/upload', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (13, 'CCMS', 'ccms_username', 'qiushi', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (14, 'CCMS', 'ccms_version', '3.0.8', 'ccms版本号');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (15, 'CCMS', 'ccms_youpaiyun_url', 'http://yunat-base.b0.upaiyun.com/', '又拍云根目录');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (16, 'CCMS', 'ccms_youpaiyun_name', 'yunat','又拍云用户名');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (17, 'CCMS', 'ccms_youpaiyun_password', 'f703c9b55d2900d63c275b68678f8540','又拍云密码');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (18, 'CCMS', 'ccms_youpaiyun_bucketname', 'yunat-base', '又拍云空间名称');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (19, 'CCMS', 'ccms_login_code_check', 'true', '是否在登陆时根据订购代码重置tb_function,默认true，为false相当于tb_function处于静止状态');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (20, 'CCMS', 'ccms_sendPageSize', '20000', '渠道分页发送每页数量');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (21, 'CCMS', 'node_retry_times', '3', '节点最多重试次数');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (100, 'CHANNEL', 'channel_Appkey', '12611940', '');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (101, 'CHANNEL', 'channel_AuthorizeURL', 'http://my.open.taobao.com/auth/authorize.htm?appkey=12611940', '');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (102, 'CHANNEL', 'channel_Secret', '4283e0d7a760229ab34bca67cc87fcf2', '');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (103, 'CHANNEL', 'channel_Url', 'http://gw.api.taobao.com/router/rest', '');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (104, 'CHANNEL', 'channel_allow_paramid_for_sms_after_normal', '1,5,21,31,41,71', '短信节点的前置节点的普通订单组时，该短信节点可以使用的变量类型id');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (105, 'CHANNEL', 'channel_allow_paramid_for_sms_after_order', '51,5', '短信节点的前置节点为匹配订单组时，该短信节点可以使用的变量类型id');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (106, 'CHANNEL', 'channel_announcement_url', 'http://10.200.187.70:18091/yunat-report/bulletin', '公告链接地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (107, 'CHANNEL', 'channel_app_id', '0', 'ccms调用渠道的应用id');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (108, 'CHANNEL', 'channel_baseapp_order_url', 'http://basesessionKey.ccms.fenxibao.com/sessions/rest/getSession', '基础app订购调用情况');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (109, 'CHANNEL', 'channel_diy_wap_url', 'http://10.200.187.70:18083/channel-wap/WapPageAction/FrameTab.jsp', '测试用导航自定义wap地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (110, 'CHANNEL', 'channel_ec_resp_wait_time', '300000', '优惠券活动发送成功的等待间隔时间 （5分钟）');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (111, 'CHANNEL', 'channel_info_url', 'http://10.200.187.70:18080/channel-info/JsonServlet', '测试用调用渠道接口地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (112, 'CHANNEL', 'channel_name', 'ccms', 'ccms调用渠道用户中心的用户名');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (113, 'CHANNEL', 'channel_password', 'rt6n3uani40yjgwpnlfosmdxoqt7pc0fn13p', 'ccms调用渠道用户中心的密码');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (114, 'CHANNEL', 'channel_recharge_url', 'http://10.200.187.70:18090/yunat-recharge/ccmsController/accountManager?', '充值链接地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (115, 'CHANNEL', 'channel_report_url', 'http://10.200.187.70:18091/yunat-report/report?', '发送报告链接地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (116, 'CHANNEL', 'channel_rmi_url', 'rmi://10.200.187.52:1199/CCMSService', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (117, 'CHANNEL', 'channel_service_url', 'http://10.200.187.70:18081/channel-service/JsonServlet', '测试用调用渠道接口服务地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (118, 'CHANNEL', 'channel_sms_serial', '', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (119, 'CHANNEL', 'channel_ump_call_frequency', '100', 'ump设置会员接口的调用频率，每秒次数');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (120, 'CHANNEL', 'channel_ump_max_call_thread', '1', 'ump设置会员接口允许的最大线程数（同时有几个节点跑，目前一个节点一个线程）');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (121, 'CHANNEL', 'channel_ump_resp_wait_time', '1000', '设置会员的等待间隔时间 （1秒）');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (122, 'CHANNEL', 'channel_ump_tool_r_d_a', '1716001', '限购，打折并且减价并且送礼并且地区包邮');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (123, 'CHANNEL', 'channel_ump_tool_x_d_a', '1724002', '满X元且限购，打折且减价且地区包邮');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (124, 'CHANNEL', 'channel_ump_tool_x_y_o', '1446001', '满X元并且满Y件，打折并且减价并且送礼并且地区包邮(订单级)');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (125, 'CHANNEL', 'channel_ump_tool_x_y_p', '1642001', '满X元并且满Y件，打折并且减价并且送礼并且地区包邮(商品级)');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (126, 'CHANNEL', 'channel_ump_tool_y_d_a', '1758001', '满Y件且限购，打折且减价且地区包邮');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (127, 'CHANNEL', 'channel_user_centre_AllItemCode_url', 'http://10.200.187.70:18084/yunat-ucenter/rest/getAllItemCode', '根据账户获取订购信息地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (128, 'CHANNEL', 'channel_user_centre_SessionKey_url', 'http://10.200.187.70:18084/yunat-ucenter/rest/getSessionKey', '调用渠道用户中心接口SessionKey获取长授权所需要的地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (129, 'CHANNEL', 'channel_user_centre_ShopAppByAccount_url', 'http://10.200.187.70:18084/yunat-ucenter/rest/getShopAppByAccount', '调用渠道用户中心接口ShopAppByAccount所需要的地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (130, 'CHANNEL', 'channel_user_centre_ShopByAccount', 'http://10.200.187.70:18084/yunat-ucenter/rest/getShopsByAccount', '调用渠道用户中心接口getShopsByAccount所需要的地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (131, 'CHANNEL', 'channel_user_centre_TsItemCode_url', 'http://10.200.187.70:18084/yunat-ucenter/rest/getTsItemCode', '调用渠道用户中心接口TsItemCode所需要的地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (132, 'CHANNEL', 'channel_user_centre_UpayShopByAccount_url', 'http://10.200.187.70:18084/yunat-ucenter/rest/getUpayShopByAccount', '获取订购了大促的店铺信息地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (133, 'CHANNEL', 'channel_wap', 'http://10.200.187.70:18083/channel-wap/JsonServlet', 'ccms调用渠道wap接口地址');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (134, 'CHANNEL', 'channel_wap_detail_url_char1', 'http://a.m.taobao.com/i', '#WAP页面中产品链接设置为手机淘宝链接，链接组成：char1 产品ID char2');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (135, 'CHANNEL', 'channel_wap_detail_url_char2', '.htm', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (136, 'CHANNEL', 'channel_wap_page_url', 'http://10.200.187.52:18089/channel-wap/JsonServlet', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (137, 'CHANNEL', 'channel_wap_pic_url_char1', '_310x310.jpg', 'WAP页面中产品图片链接设置为手机淘宝图片，图片组成：imgURL char1');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (138, 'CHANNEL', 'channel_wap_pic_url_default', 'http://img01.taobaocdn.com/imgextra/i1/903270374/T2kklvXixMXXXXXXXX_!!903270374.jpg?', '默认图片为null时地址（?必须保留）');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (139, 'CHANNEL', 'ump_all_person_all_items_activity_limit_number', '30', '全店参与（指定所有商品）的非定向活动数量最大值');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (140, 'CHANNEL', 'ump_all_person_part_items_activity_limit_number', '20', '部分参与（指定部分商品）的非定向活动数量最大值');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (141, 'CHANNEL', 'channel_check_baseapp_add_ccmssessionkey_url', 'http://10.200.187.70:18084/yunat-ucenter/rest/getStatus', '数据赢家sessionkey的问题');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (142, 'CHANNEL', 'channel_promote_url', 'http://app.fenxibao.com/ccms-ad-1212/login', '大促广告界面充值连接显示');
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (500, 'MQ', 'mq_exchange_name', 'USER_NOTICE_MQ_EXCHANGE_TEST1', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (501, 'MQ', 'mq_host', '10.200.187.73', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (502, 'MQ', 'mq_password', '$datawinner$', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (503, 'MQ', 'mq_port', '5672', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (504, 'MQ', 'mq_username', 'datawinner', NULL);
INSERT INTO tb_app_properties (prop_id, prop_group, prop_name, prop_value, prop_desc) VALUES (505, 'MQ', 'mq_virtualHost', 'datawinner', NULL);


--//@UNDO
-- SQL to undo the change goes here.
delete from tb_app_properties;

