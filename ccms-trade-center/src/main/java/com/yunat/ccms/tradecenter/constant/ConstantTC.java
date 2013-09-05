/**
 *
 */
package com.yunat.ccms.tradecenter.constant;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-8 下午07:34:04
 */
public interface ConstantTC {

	 /** 获取变量替换的CODE **/
    public static final Integer VARIABLE_REPLACE = 4;

    /** 过滤条件类型 */
	public static final Integer FILTE_TYPE = 1;

	/** 会员等级类型 */
	public static final Integer MEMBER_TYPE = 2;

	/** 关怀过滤条件  */
	public static final Integer CARE_FILTE_TYPE = 8;

	/** ETL-延迟失效类型  */
    public static final Integer ETL_TIMEOUT_TYPE = 6;

    /** 物流流转过滤关键字  */
	public static final Integer LOGISTICS_KEY_WORDS = 7;

	/** 关怀过滤条件  */
	public static final Integer CARE_CONFIRM_TYPE = 5;

	String NEWLINE = "\n";

	String TABLE = "\t";

	String SPLIT_CHAR = ",";

	String TABLE_COMMA = "\t,";

	 /**  发货严重延迟默认小时数  */
    public static final Integer SERIOUS_DELAY = 120;

    /**   发货轻微延迟默认小时数 */
    public static final Integer COMMON_DELAY = 48;

}
