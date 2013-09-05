package com.yunat.ccms.core.support.cons;

/**
 * CCMS系统常用数据库字段常量
 * 
 * @author Arlight
 */

public interface ColumnConstant {

	/**
	 * 统一客户ID字段名。 统一客户ID是客人在CCMS中唯一的全局标识，是用于CCMS内部流转的客人标识，与平台（如淘宝、京东、拍拍等）无关 。
	 * CCMS流程中各节点输入输出的“统一客户ID”字段需指定为该常量。
	 */
	 String UNI_ID = "uni_id";

	/**
	 * 平台内客户ID字段名。 各个与CCMS连接的平台（如淘宝、京东、拍拍等），其客户在各自平台内的客户唯一标识字段的名字统称为：customerno
	 * 。 在访问平台相关的客人ID时，需指定该常量。
	 */
	 String CUSTOMER_NO = "customerno";

	/**
	 * 平台来源代码(如taobao、extaobao、paipai等)
	 * 
	 */
	 String PLAT_CODE = "plat_code";

	/**
	 * 平台内客户ID字段的数据库类型常量
	 */
	 String DB_TYPE_CUSTOMER_NO = "varchar(50)";

	 String DB_TYPE_USER_ID = "varchar(64)";

	/**
	 * “淘宝”平台订单ID字段常量
	 */
	 String TID = "tid";
	/** 淘宝平台订单 "实付金额"字段常量 */
	 String TB_ORDER_PAYMENT = "payment";
	/** 淘宝平台订单 "商品金额"字段常量 */
	 String TB_ORDER_TOTAL_FEE = "total_fee";

	/**
	 * 控制/发送组类型字段名常量
	 */
	 String CONTROL_GROUP_TYPE = "control_group_type";

	/**
	 * 控制/发送组字段的数据库类型常量
	 */
	 String DB_TYPE_CONTROL_GROUP_TYPE = "tinyint";

	/**
	 * 聚类分组号字段
	 */
	 String CLUSTERING_ID = "clustering_id";

	/**
	 * 手机号字段
	 */
	 String MOBILE = "mobile";

	/**
	 * Email字段
	 */
	 String EMAIL = "email";

	/**
	 * 手机发送内容
	 */
	 String MESSAGE = "message";
	 String ENCRYPT = "encrypt";

	/**
	 * 年月字段
	 */
	 String YEAR_MONTH = "year_month";

	/** 渠道节点常用变量 */

	 String SUBJOB_ID = "subjob_id";

	 String NODE_ID = "node_id";

	 String CAMP_ID = "camp_id";

	 String ORDER_ID = "order_id";

	 String STATUS = "status";

	 String CAMP_TYPE = "camp_type";

	 String CHANNEL_TYPE = "channel_type";

	 String PROG_ID = "prog_id";

	 String NICK = "nick";

	 String ID = "id";

	 String CONTENT = "content";

	 String PLAN_STARTTIME = "plan_starttime";

	 String VISITOR_ID = "visitor_id";
	 String DP_ID = "dp_id";
	 String BUYER_NICK = "buyer_nick";

}
