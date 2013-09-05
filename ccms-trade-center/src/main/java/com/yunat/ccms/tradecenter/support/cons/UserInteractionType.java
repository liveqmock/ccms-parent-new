package com.yunat.ccms.tradecenter.support.cons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 用户交互类型
 * @author 李卫林
 *
 */
public enum UserInteractionType {

    /** 催付  */
	AUTO_URPAY(1, "自动催付", SysType.ORDERCENTER),
    PRE_CLOSE_URPAY(2, "预关闭催付", SysType.ORDERCENTER),
    CHEAP_URPAY(3, "聚划算催付", SysType.ORDERCENTER),
    MANUAL_URPAY(4, "手动短信催付", SysType.CUSTOMERCETNER),
    MANUAL_WANGWANG_URPAY(5, "手动旺旺催付", SysType.CUSTOMERCETNER),
	/** 关怀  */
	ORDER_CARE(6,"下单关怀", SysType.ORDERCENTER),
	SHIPMENT_CARE(7,"发货关怀", SysType.ORDERCENTER),
	ARRIVED_CARE(8,"同城关怀", SysType.ORDERCENTER),
	DELIVERY_CARE(9,"派件关怀", SysType.ORDERCENTER),
	SIGNED_CARE(10,"签收关怀", SysType.ORDERCENTER),
	REFUND_CARE(11,"退款关怀", SysType.ORDERCENTER),
	CONFIRM_CARE(12,"确认收货关怀", SysType.ORDERCENTER),
	ASSESS_CARE(13,"评价关怀", SysType.ORDERCENTER),

	MANUAL_SENDGOODS_SMS_CARE(18, "发货事务短信关怀", SysType.CUSTOMERCETNER, ColumnEnum.SENDGOODS_CARE_STATUS),
	MANUAL_SENDGOODS_WANGWANG_CARE(19, "发货事务旺旺关怀", SysType.CUSTOMERCETNER, ColumnEnum.SENDGOODS_CARE_STATUS),
	MANUAL_SENDGOODS_MOBILE_CARE(20, "发货事务手机关怀", SysType.CUSTOMERCETNER, ColumnEnum.SENDGOODS_CARE_STATUS),

	MANUAL_LOGISTICS_SMS_CARE(21, "物流事务短信关怀", SysType.CUSTOMERCETNER, ColumnEnum.LOGISTICS_CARE_STATUS),
	MANUAL_LOGISTICS_WW_CARE(22, "物流事务旺旺关怀", SysType.CUSTOMERCETNER, ColumnEnum.LOGISTICS_CARE_STATUS),
	MANUAL_LOGISTICS_MOBILE_CARE(23, "物流事务手机关怀", SysType.CUSTOMERCETNER, ColumnEnum.LOGISTICS_CARE_STATUS),

	MANUAL_REFUND_SMS_CARE(24, "退款事务短信关怀", SysType.CUSTOMERCETNER),
	MANUAL_REFUND_WW_CARE(25, "退款事务旺旺关怀", SysType.CUSTOMERCETNER),
	MANUAL_REFUND_MOBILE_CARE(26, "退款事务手机关怀", SysType.CUSTOMERCETNER),

	/** 评价事务短信关怀 */
	MANUAL_TRADERATE_SMS_CARE(27, "评价事务短信关怀", SysType.CUSTOMERCETNER),
	/** 评价事务旺旺关怀 */
	MANUAL_TRADERATE_WANGWANG_CARE(28, "评价事务旺旺关怀", SysType.CUSTOMERCETNER),
	MANUAL_TRADERATE_MOBILE_CARE(29, "评价事务手机关怀", SysType.CUSTOMERCETNER),

	NOT_GOOD_TRADERATE_WARN(30, "中差评告警", SysType.ORDERCENTER),
	REFUND_WARN(31, "退款告警", SysType.ORDERCENTER)

	;

	private Integer type;
	private String message;
	private boolean isShow = true; // 是否在界面上展示, 默认true，展示
	private SysType sysType; // 系统类型， 是客服、订单
	private ColumnEnum shipColumn;

	UserInteractionType(Integer type, String message){
		this.type = type;
		this.message = message;
	}

	UserInteractionType(Integer type, String message, SysType sysType){
		this.type = type;
		this.message = message;
		this.sysType = sysType;
	}

	UserInteractionType(Integer type, String message, SysType sysType, boolean isShow){
		this(type, message, sysType);
		this.isShow = isShow;
	}

	UserInteractionType(Integer type, String message, SysType sysType, ColumnEnum shipColumn){
		this(type, message, sysType);
		this.shipColumn = shipColumn;
	}

	UserInteractionType(Integer type, String message, SysType sysType, boolean isShow, ColumnEnum shipColumn){
		this(type, message, sysType, isShow);
		this.shipColumn = shipColumn;
	}

	public ColumnEnum getShipColumn() {
		return shipColumn;
	}

	public boolean isShow() {
		return isShow;
	}

	public SysType getSysType() {
		return sysType;
	}

	public Integer getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 检查是否包含
	 * @param type
	 * @return
	 */
	public static boolean containsType(Integer type) {
		boolean ret = false;
		for (UserInteractionType urpayQueryType : UserInteractionType.values()){
			if (urpayQueryType.getType().equals(type)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * 获得描述
	 * @param type
	 * @return
	 */
	public static String getMessage(Integer type) {
		String message = "";
		for (UserInteractionType urpayQueryType : UserInteractionType.values()){
			if (urpayQueryType.getType().equals(type)) {
				message = urpayQueryType.getMessage();
				break;
			}
		}
		return message;
	}



	/**
	 * 根据类型及是否展示获得描述列表键值
	 * @param type
	 * @return
	 */
	public static List<Map<String, Object>> getTypeMsgListBySysTypeShow(SysType type, boolean isShow) {
		UserInteractionType[] types = UserInteractionType.values();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (UserInteractionType urpayQueryType : types){
			if (urpayQueryType.getSysType().equals(type) && urpayQueryType.isShow() == isShow){
				Map<String, Object> item = new HashMap<String, Object>(2);
				item.put("type", urpayQueryType.getType());
				item.put("value", urpayQueryType.getMessage());
				list.add(item);
			}
		}
		return list;
	}


	/**
	 * 根据类型及是否展示获得描述列表键值
	 * @param type
	 * @return
	 */
	public static List<Integer> getTypeListBySysTypeShow(SysType type, boolean isShow) {
		UserInteractionType[] types = UserInteractionType.values();
		List<Integer> list = new ArrayList<Integer>();
		for (UserInteractionType urpayQueryType : types){
			if (urpayQueryType.getSysType().equals(type) && urpayQueryType.isShow() == isShow){
				list.add(urpayQueryType.getType());
			}
		}
		return list;
	}

	/**
	 * 获得描述列表键值
	 * @param type
	 * @return
	 */
	public static List<Map<String, Object>> getTypeMsgListAll() {
		UserInteractionType[] types = UserInteractionType.values();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (UserInteractionType urpayQueryType : types){
			Map<String, Object> item = new HashMap<String, Object>(2);
			item.put("type", urpayQueryType.getType());
			item.put("value", urpayQueryType.getMessage());
			list.add(item);
		}
		return list;
	}

	/**
	 * 获得描述键值
	 * @param type
	 * @return
	 */
	public static Map<Integer, String> getTypeMsgMap() {
		UserInteractionType[] types = UserInteractionType.values();
		Map<Integer, String> item = new HashMap<Integer, String>(types.length);
		for (UserInteractionType urpayQueryType : types){
			item.put(urpayQueryType.getType(), urpayQueryType.getMessage());
		}
		return item;
	}

	public static UserInteractionType get(Integer type) {
		if (type == null) {
			return null;
		}
		UserInteractionType userInteractionType = null;
		for (UserInteractionType urpayQueryType : UserInteractionType.values()){
			if (urpayQueryType.getType().equals(type)) {
				userInteractionType = urpayQueryType;
				break;
			}
		}
		return userInteractionType;
	}
}
