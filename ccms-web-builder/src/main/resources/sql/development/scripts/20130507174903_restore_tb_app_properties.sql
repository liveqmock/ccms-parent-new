--// restore tb_app_properties
-- Migration SQL that makes the change goes here.

delete from tb_app_properties;
ALTER table tb_app_properties MODIFY COLUMN prop_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数ID,仅作主键,不应该被其他表应用'; 

INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('CCMS', 'ccms_version', '4.0.0', 'ccms版本号'),
('CCMS', 'ccms_tenant_id', '0_taobao_100571094', 'ccms租户Id'),
('CCMS', 'ccms_tenant_password', '51d531kxsyofum3g6iq22sbx8mebvaquhnr5', 'ccms租户密钥'),
('CCMS', 'ccms_upload_dir', 'd:/upload', 'CCMS文件上传地址'),
('CCMS', 'ccms_node_retry_times', '3', '节点最多重试次数');

INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('CHANNEL', 'channel_client_name', 'ccms', 'ccms调用渠道用户中心的用户名'),
('CHANNEL', 'channel_client_password', 'rt6n3uani40yjgwpnlfosmdxoqt7pc0fn13p', 'ccms调用渠道用户中心的密码'),
('CHANNEL', 'channel_client_app_id', '0', 'ccms调用渠道的应用id'),
('CHANNEL', 'channel_client_send_batch_size', '20000', 'CCMS调用渠道分页发送每页数量');

INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES 
('CHANNEL', 'channel_service_query_url', 'http://10.200.187.70:18080/channel-info/JsonServlet', '渠道查询服务接口地址 '),
('CHANNEL', 'channel_service_command_url', 'http://10.200.187.70:18081/channel-service/JsonServlet', '渠道命令服务接口地址'),
('CHANNEL', 'channel_report_send_result_url', 'http://10.200.187.70:18091/yunat-report/report?', '渠道发送报告链接地址'),
('CHANNEL', 'channel_report_announcement_url', 'http://10.200.187.70:18091/yunat-report/bulletin', '公告链接地址');


INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES
('UCENTER', 'ucenter_service_rest_url', 'http://apptest.fenxibao.com/ucenter-restful-impl', '订购中心REST接口地址 ');

INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES 
('TOP', 'top_service_rest_url', 'http://gw.api.taobao.com/router/rest', '淘宝开放平台rest地址'),
('TOP', 'top_ccms_appkey', '12283535', 'CCMS在淘宝开放平台的Appkey'),
('TOP', 'top_ccms_appsecret', '4283e0d7a760229ab34bca67cc87fcf2', 'CCMS在淘宝开放平台的Appsecret'),
('TOP', 'top_ccms_grant_url', 'http://container.api.taobao.com/container?appkey=12283535&scope=promotion,item,usergrade', '淘宝开放平台授权地址');


INSERT INTO tb_app_properties ( prop_group, prop_name, prop_value, prop_desc) VALUES 
('MQ', 'mq_exchange_name', 'USER_NOTICE_MQ_EXCHANGE_TEST1', NULL),
('MQ', 'mq_host', '10.200.187.73', NULL), 
('MQ', 'mq_password', '$datawinner$', NULL),
('MQ', 'mq_port', '5672', NULL), 
('MQ', 'mq_username', 'datawinner', NULL),
('MQ', 'mq_virtualHost', 'datawinner', NULL);



--//@UNDO
-- SQL to undo the change goes here.


