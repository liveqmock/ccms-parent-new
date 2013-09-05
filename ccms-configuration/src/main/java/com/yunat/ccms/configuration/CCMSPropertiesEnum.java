package com.yunat.ccms.configuration;

/**
 * CCMS系统配置项枚举
 *
 * @author tao.yan
 * @author xiaojing.qu
 *
 */
public enum CCMSPropertiesEnum {

	/*** 版本号 */
	CCMS_VERSION("CCMS", "ccms_version"),
	/*** CCMS租户ID */
	CCMS_TENANT_ID("CCMS", "ccms_tenant_id"),
	/*** CCMS租户密钥 */
	CCMS_TENANT_PASSWORD("CCMS", "ccms_tenant_password"),
	/*** CCMS文件上传地址 */
	CCMS_UPLOAD_DIR("CCMS", "ccms_upload_dir"),
	/** 节点异常重试最大次数 */
	CCMS_NODE_RETRY_TIMES("CCMS", "ccms_node_retry_times"),
	/** 客服系统默认权限 */
	CCMS_DEFAULT_YUNAT_USER_ROLE("CCMS", "default_yunat_user_role"),

	/*** CCMS调用渠道用户中心的用户名 */
	CHANNEL_CLIENT_NAME("CHANNEL", "channel_client_name"),
	/*** CCMS调用渠道用户中心的密码 */
	CHANNEL_CLIENT_PASSWORD("CHANNEL", "channel_client_password"),
	/*** CCMS调用渠道的应用id */
	CHANNEL_CLIENT_APP_ID("CHANNEL", "channel_client_app_id"),
	/*** CCMS调用渠道分页发送每页数量 */
	CHANNEL_CLIENT_SEND_BATCH_SIZE("CHANNEL", "channel_client_send_batch_size"),
	/*** CCMS调用渠道获取发送结果等待时间 */
	CHANNEL_CLIENT_GET_RESP_WAIT_TIME("CHANNEL", "channel_client_get_resp_wait_time"),
	/*** 渠道查询服务接口地址 */
	CHANNEL_SERVICE_QUERY_URL("CHANNEL", "channel_service_query_url"),
	/*** 渠道命令服务接口地址 */
	CHANNEL_SERVICE_COMMAND_URL("CHANNEL", "channel_service_command_url"),
	/*** 发送报告链接地址 */
	CHANNEL_REPORT_SEND_RESULT_URL("CHANNEL", "channel_report_send_result_url"),
	/*** 公告链接地址 */
	CHANNEL_REPORT_ANNOUNCEMENT_URL("CHANNEL", "channel_report_announcement_url"),
	/*** 店铺诊断 ***/
	CHANNEL_SHOP_DIAGNOSIS_URL("CHANNEL", "channel_shop_diagnosis_url"),
	/*** 店铺监控 ***/
	CHANNEL_SHOP_MONITOR_URL("CHANNEL", "channel_shop_monitor_url"),
	/*** 订单中心 ***/
	CHANNEL_ORDER_CENTER_URL("CHANNEL", "channel_order_center_url"),
	/*** 店铺监控和订单中心的SECRET_KEY ***/
	CHANNEL_ORDERCENTER_SECRET_KEY("CHANNEL", "channel_ordercenter_secret_key"),
	/*** CCMS租户渠道账号充值 ***/
	CHANNEL_TENANT_RECHARGE_URL("CHANNEL", "channel_tenant_recharge_url"),

	/*** 订购中心REST接口地址 */
	UCENTER_SERVICE_REST_URL("UCENTER", "ucenter_service_rest_url"),

	/*** 淘宝开放平台rest地址 */
	TOP_SERVICE_REST_URL("TOP", "top_service_rest_url"),
	/*** CCMS在淘宝开放平台的Appkey */
	TOP_CCMS_APPKEY("TOP", "top_ccms_appkey"),
	/*** CCMS在淘宝开放平台的Appsecret */
	TOP_CCMS_APPSECRET("TOP", "top_ccms_appsecret"),
	/** 淘宝开放平台授权地址 */
	TOP_CCMS_GRANT_URL("TOP", "top_ccms_grant_url"),

	/** MQ配置 */
	MQ_EXCHANGE_NAME("MQ", "mq_exchange_name"),
	/** MQ配置 */
	MQ_HOST("MQ", "mq_host"),
	/** MQ配置 */
	MQ_PORT("MQ", "mq_port"),

	/** MQ CCMS 配置 */
	MQ_CCMS_VIRTUALHOST("MQ", "mq_ccms_virtualHost"),
	/** MQ CCMS 配置 */
	MQ_CCMS_USERNAME("MQ", "mq_ccms_username"),
	/** MQ CCMS 配置 */
	MQ_CCMS_PASSWORD("MQ", "mq_ccms_password"),

	/** MQ ETL 配置 */
	MQ_ETL_VIRTUALHOST("MQ", "mq_etl_virtualHost"),
	/** MQ ETL 配置 */
	MQ_ETL_USERNAME("MQ", "mq_etl_username"),
	/** MQ ETL 配置 */
	MQ_ETL_PASSWORD("MQ", "mq_etl_password"),
	/** MQ ETL 配置 */
	MQ_ETL_QUEUE("MQ", "mq_etl_queue"),

	/** 客服中心上传常用凭证保存地址 */
	KFZX_UPLOAD_PATH("KFZX", "kfzx_uploadimg"),

	;// End of define Eunm

	private String prop_group;
	private String prop_name;

	private CCMSPropertiesEnum(String prop_group, String prop_name) {
		this.prop_group = prop_group;
		this.prop_name = prop_name;
	}

	public String getProp_group() {
		return prop_group;
	}

	public void setProp_group(String prop_group) {
		this.prop_group = prop_group;
	}

	public String getProp_name() {
		return prop_name;
	}

	public void setProp_name(String prop_name) {
		this.prop_name = prop_name;
	}
}