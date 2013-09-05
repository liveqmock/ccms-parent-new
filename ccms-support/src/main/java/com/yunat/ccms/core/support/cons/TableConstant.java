package com.yunat.ccms.core.support.cons;


public interface TableConstant {
	
	/** 统一客户信息表（和平台无关） */
	public static final String UNI_CUSTOMER = "uni_customer";

	/** 客户来源平台表 （维表）*/
	public static final String UNI_PLAT = "uni_plat";

	/** 统一客户信息与客户来源平台的关系表 */
	public static final String UNI_CUSTOMER_PLAT = "uni_customer_plat";

	/** 外部导入客户信息表（非接驳平台）*/
	public static final String PLT_EXT_CUSTOMER= "plt_ext_customer";

	/** 外部数据导入批次信息表*/
	public static final String PLT_EXT_IMPORT_BATCH = "plt_ext_import_batch";

	/** 手工修改客户信息表*/
	public static final String PLT_MODIFY_CUSTOMER= "plt_modify_customer";

	/** 外部导入“淘宝”平台客户信息表（已接驳平台）*/
	public static final String PLT_EXTAOBAO_CUSTOMER= "plt_extaobao_customer";

	/** “淘宝”平台客户信息统一视图 */
	public static final String VW_TAOBAO_CUSTOMER = "vw_taobao_customer";

	/** 查询节点－客户属性查询条件设置表 */
	public static final String TWF_QUERY_PROPERTY = "twf_query_property";

	/** The Constant TD_CONDITION. */
	public static final String TD_CONDITION = "td_condition";

	/** The Constant TWF_IMPORT_USER. */
	public static final String TWF_IMPORT_USER = "twf_import_user";

	public static final String TWF_LOG_CHANNEL_USER = "twf_log_channel_user";
	public static final String TWF_LOG_CHANNEL = "twf_log_channel";

	/** 短信发送统计日志 */
	public static final String TWF_NODE_SMS_SEND_LOG =  "twf_node_sms_send_log";

	/** WAP短链生成日志 */
	public static final String TWF_NODE_WAP_LINK_LOG = "twf_node_wap_link_log";

	/** EDM发送统计日志 */
	public static final String TWF_LOG_EDM_SEND = "twf_log_edm_send";

	/** “淘宝”平台业务表 */
	public static final String PLT_TAOBAO_ORDER_ITEM = "plt_taobao_order_item";
	public static final String PLT_TAOBAO_ORDER = "plt_taobao_order";
	public static final String PLT_TAOBAO_PRODUCT = "plt_taobao_product";
	public static final String PLT_TAOBAO_CUSTOMER = "plt_taobao_customer";
	public static final String PLT_TAOBAO_SHOP = "plt_taobao_shop";
	public static final String PLT_TAOBAO_ITEM_SELLER_CAT = "plt_taobao_item_seller_cat";
	public static final String PLT_TAOBAO_SHIPPING = "plt_taobao_shipping";

	/** “淘宝”平台Acookie查询相关表 */
	public static final String ACOOKIE_ITEM_PV = "plt_taobao_acookie_item_pv";
	public static final String ACOOKIE_KEYWORD_CNT_T100 = "plt_taobao_acookie_keyword_cnt_t100";
	public static final String ACOOKIE_SCID_CNT_T100 = "plt_taobao_acookie_scid_cnt_t100";
	public static final String ACOOKIE_SEARCH_LOG = "plt_taobao_acookie_search_log";
	public static final String ACOOKIE_SESSION_SOURCE = "plt_taobao_acookie_session_source";
	public static final String ACOOKIE_SOURCE_TYPE = "plt_taobao_acookie_source_type";
	public static final String ACOOKIE_TID_SOURCE = "plt_taobao_acookie_tid_source";
	public static final String ACOOKIE_VISITOR_TID = "plt_taobao_acookie_visitor_tid";
	public static final String ACOOKIE_VIEWITEM_LOG = "plt_taobao_acookie_viewitem_log";


	public static final String ORDER_STATUS = "tds_order_status";

	/**
	 * 节点使用的统一客户ID基表，节点流中以此表记录为准，
	 * 实际为uni_customer中的所有用户ID
	 */
	public static final String NODE_BASE_USER = "vw_node_uni_id_all";

	/** 节点临时数据表(每个subjob一张表或视图). */
	public static final String TEMP_NODE_TABLE_PREFIX = "tmp_log_node";

	public static final String TMP_SPLIT_SORTFLDS = "tmp_split_sortflds";

	/** 节点临时数据视图(每个subjob一张表或视图). */
	public static final String TEMP_NODE_VIEW_PREFIX =  "vtmp_log_node";

	public static final String TWF_LOG_GROUP = "twf_log_group";
	public static final String TWF_LOG_GROUP_USER = "twf_log_group_user";

	/** 其它节点临时中间数据表. oracle 临时表 */
	public static final String TMP_ORDER_TARGET_GROUP = "tmp_order_target_group_";

	/** 其它节点临时中间数据表. postgresql建表语句 */
	public static final String MID_TEMP_NODE_CREATE1 = "CREATE TABLE ";
	public static final String MID_TEMP_NODE_CREATE2 = " (user_id varchar(64) primary key,control_group_type tinyint(1))";

	/** 屏蔽节点客户黑名单表. */
	public static final String SCREEN_CUSTOMER_BLACK = "tw_screen_customer_black";

	/** 屏蔽节点客户红名单表. */
	public static final String SCREEN_CUSTOMER_RED = "tw_screen_customer_red";

	/** 屏蔽节点客户内部员工表. */
	public static final String SCREEN_CUSTOMER_INSIDE = "tw_screen_customer_inside";

	/** 屏蔽节点手机状态黑名单表. */
	public static final String SCREEN_PHONE_BLACK = "tw_screen_phone_black";

	public static final String TEMP_COMM_TABLE = "temp_comm_table";

	/** 拆分节点用户ID临时排序中间表（Oracle临时表）. */
	public static final String TEMP_SPLIT_NODE_UID_SORT = "twf_splitnode_uid_sort";

	/** 拆分节点用户ID临时排序中间表 */
	public static final String TMP_SPLITNODE_UID_SORT_TEMPLATE = "tmp_splitnode_uid_sort";
	public static final String TMP_SPLITNODE_UID_SORT_CREATE = " (user_id varchar(64),no int(10) primary key auto_increment) ";

	/** 分析节点连续型字段ETL临时表（Oracle临时表）. */
	public static final String TEMP_ANA_NODE_FLDVAL_ETL = "twf_analysis_valfld_etl";

	public static final String CREATE_TABLE_PREFIX = "create table ";
	public static final String TEMP_CREATE_TABLE_PREFIX = "create temp table ";
	public static final String UNLOGGED_CREATE_TABLE_PREFIX = "create table ";

	public static final String TEMP_ANA_NODE_FLDVAL_ETL_CREATE = UNLOGGED_CREATE_TABLE_PREFIX
	+ TEMP_ANA_NODE_FLDVAL_ETL + " (\"uni_id\" varchar(64),\"table_id\" int8,\"column_id\" int8,\"fld_val\" numeric(12,2),\"ord_no\" int4 )";

	/** 流程执行中间表或视图列表 . */
	public static final String TWF_LOG_NODE_MIDS = "twf_log_node_mids";

	/** 流程运行日志表 */
	public static final String TWF_LOG_JOB = "twf_log_job";

	/** 流程节点运行日志表 */
	public static final String TWF_LOG_SUBJOB = "twf_log_subjob";

	/** 流程节点通用信息表 */
	public static final String TWF_NODE = "twf_node";

	/** 短信接口表（短信发送详单CDR） */
	public static final String TI_SMS = "ti_sms";
	/** WAP接口表（WAP短信发送详单CDR） */
	public static final String TI_WAP = "ti_wap";
	/** EDM直邮接口表（Email发送详单CDR） */
	public static final String TI_EDM = "ti_edm";
	/** EC淘宝优惠卷接口表（EC发送详单CDR） */
	public static final String TI_EC = "ti_ec";

	public static final String TB_CAMPAIGN = "tb_campaign";
	public static final String TB_OFFER = "tb_offer";

	// 元数据表
	public static final String TM_DIC_TYPE_VALUE = "tm_dic_type_value";
	public static final String TM_DB_TABLE = "tm_db_table";
	public static final String TM_DB_COLUMN = "tm_db_column";
	public static final String TM_VIEW_DIR = "tm_view_dir";

	// 数据字典表
	public static final String TDS_JOB_STATUS = "tds_job_status";

	/** 活动评估源客户组统计表 */
	public static final String TC_CAMP_SRC_GROUP = "tc_camp_src_group";

	/** 活动评估响应组统计表 */
	public static final String TC_CAMP_RESP_GROUP = "tc_camp_resp_group";

	/** RFM节点用户汇总指标结果表前缀（后面跟节点ID和指标分组号，如: twf_etl_customer_91831_1） */
	public static final String TWF_ETL_CUSTOMER_PREFIX = "twf_etl_customer";
	public static final String TMP_ETL_NODE_FLD = "tmp_etlfld";

	/** 屏蔽名单中黑名单和内部名单导入的临时表表名前缀 */
	public static final String TEMP_CUSTOMER_MANAGER_IMPORT = "tmp_twf_cus_import";


	/** 黑名单管理邮件黑名单表. */
	public static final String EDM_BLACKLIST = "tw_edm_blacklist";


	/** 黑名单管理手机黑名单表. */
	public static final String MOBILE_BLACKLIST = "tw_mobile_blacklist";



	/** 红名单管理手机红名单表. */
	public static final String MOBILE_REDLIST = "tw_mobile_redlist";

	/**
	 * 黑名单管理会员黑名单
	 */
	public static final String MEMBER_BLACKLIST = "tw_member_blacklist";


	/** 红名单管理邮件红名单表. */
	public static final String EDM_REDLIST = "tw_edm_redlist";

	/** 邮件黑名单和内部名单导入的临时表表名前缀 */
	public static final String TMP_EDM_MANAGER_IMPORT = "tmp_edm_import";

	/** 短信黑名单和内部名单导入的临时表表名前缀 */
	public static final String TMP_MOBILE_MANAGER_IMPORT = "tmp_mobile_import";

	/** 会员黑名单和内部名单导入的临时表表名前缀 */
	public static final String TMP_MEMBER_MANAGER_IMPORT = "tmp_member_import";


	/** 邮件红名单和内部名单导入的临时表表名前缀 */
	public static final String TMP_RED_EDM_MANAGER_IMPORT = "tmp_edm_redlist_import";

	/** 短信红名单和内部名单导入的临时表表名前缀 */
	public static final String TMP_RED_SMS_MANAGER_IMPORT = "tmp_mobile_redlist_import";

	/** 导入节点文件名前缀 */
	public static final String IMPORT_FILE_TABLENAME = "twf_import_user";

	public static final String LOG_CHANNEL_EC_USER_RESPONSE = "twf_log_channel_user_response_ec";

	public static final String VIP_GRADE_TABLE = "twb_vip_customer";

	/**
	 * VIP会员等级设置的临时表的前缀
	 */
	public static final String TEMP_VIP_GRADE = "tmp_customer_vipinfo_";

	/**
	 * vip 会员的等级设置存储其他店铺临时表前缀
	 */
	public static final String TMP_CUSTOMER_VIPINFO_OTHER = "tmp_customer_vipinfo_other_";

	/** 自定义标签临时表前缀 */
	public static final String TEMP_CUSTOMIZE_IDX = "tmp_customize_idx_";

	/** 自定义标签固定表前缀 */
	public static final String TWB_CUSTOMIZE_IDX = "twb_customize_idx_";

	/** 匹配订单节点后生成的订单-用户关系表  */
	public static final String TWF_CUSTOMER_MAPING_ORDER = "twf_customer_maping_order";

	/** 订单目标组用户数相关表 */
	public static final String VM_TAOBAO_ORDER = "vm_taobao_order";

	/** 短信节点抽样数据临时存储数据表*/
	public static final String TWF_NODE_SMS_SAMPLE_USER = "twf_node_sms_sample_user";

	/** 短信 节点抽样消息结果统计表*/
	public static final String TWF_NODE_SMS_SAMPLE_MSG = "twf_node_sms_sample_msg";

	public static final String TB_PROGRAM = "tb_program";

	public static final String TB_SYSUSER = "tb_sysuser";

}