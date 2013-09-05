package com.yunat.ccms.tradecenter.support.cons;

/**
 * 配置组
 * @author 李卫林
 *
 */
public enum PropertiesGroupEnum {

    /** 催付  */
    ABNORMAL_LOGISTICS("abnormalLogistics", "疑难件"),
    SENDGOODS("sendGoods", "等待发货"),
    ADDRESS_BLACKLIST("addressBlacklist", "收货地址黑名单"),
    
    TRADE_RATE("tradeRate", "评价事务"),
    
    ORDER_FLOW_MONITORING("orderFlowMonitoring","订单监控-订单流转监控"),
	;

	private String name;
	private String message;

	PropertiesGroupEnum(String type, String message){
		this.name = type;
		this.message = message;
	}

	public String getName() {
		return name;
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
		for (PropertiesGroupEnum urpayQueryType : PropertiesGroupEnum.values()){
			if (urpayQueryType.getName().equals(type)) {
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
		for (PropertiesGroupEnum urpayQueryType : PropertiesGroupEnum.values()){
			if (urpayQueryType.getName().equals(type)) {
				message = urpayQueryType.getMessage();
				break;
			}
		}
		return message;
	}
}
