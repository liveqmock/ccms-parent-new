package com.yunat.ccms.tradecenter.support.cons;

import java.util.HashMap;
import java.util.Map;

/**
 * 客服订单关系表 (tb_tc_customer_orders_ship) 字段名称
 * @author fanhong.meng
 *
 */
public enum ColumnEnum {

	/** 未付款跟进 (0-没隐藏  1-隐藏)*/
	IS_Hide("is_hide", "is_hide"),

	/** 物流事务隐藏 (0-没隐藏  1-隐藏)*/
	LOGISTICS_HIDE("logistics_hide", "logistics_hide"),

	/** 物流事务关怀状态（0-未关怀  1-已关怀）*/
	LOGISTICS_CARE_STATUS("logistics_care_status", "logistics_care_status"),

	/** 发货事务关怀状态（0-未关怀  1-已关怀）*/
	SENDGOODS_CARE_STATUS("sendgoods_care_status", "sendgoods_care_status"),

	/** 发货事务关怀状态（0-未关怀  1-已关怀）*/
	REFUND_CARE_STATUS("refund_care_status", "refund_care_status"),

	/** 发货事务隐藏（0-没隐藏  1-隐藏）*/
	SENDGOODS_HIDE("sendgoods_hide", "sendgoods_hide")
	;

	private String key;
	private String name;

	private ColumnEnum(String name) {
		this.name = name;
	}

	private ColumnEnum(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}


	/**
	 * 获得键对应的值
	 * @param type
	 * @return
	 */
	public static String getNameByKey(String key) {
		ColumnEnum[] types = ColumnEnum.values();
		for (ColumnEnum type : types){
			if (type.getKey().equals(key)){
				return type.getName();
			}
		}
		return "";
	}


	/**
	 * 获得描述键值
	 * @param type
	 * @return
	 */
	public static Map<String, String> getKeyNameMap() {
		ColumnEnum[] types = ColumnEnum.values();
		Map<String, String> item = new HashMap<String, String>(types.length);
		for (ColumnEnum type : types){
			item.put(type.getKey(), type.getName());
		}
		return item;
	}

}
