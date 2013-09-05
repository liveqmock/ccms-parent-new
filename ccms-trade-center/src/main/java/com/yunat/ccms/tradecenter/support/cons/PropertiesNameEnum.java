package com.yunat.ccms.tradecenter.support.cons;

/**
 * 配置项
 * @author 李卫林
 *
 */
public enum PropertiesNameEnum {
    COURIER_COMPANY("courierCompany","快递公司"),
    ABNORMAL_DAYS("abnormalDays", "异常物流超时更新天数"),
    AVERAGE_DELAY("averageDelay","普通延迟"),
    SERIOUS_DELAY("seriousDelay","严重延迟") ,
    ADDRESS_BLACKLIST("addressBlacklist", "收货地址黑名单") ,
    ADDRESS_BLACKLIST_EXCLUSIVE("addressBlacklistExclusive", "收货地址黑名单例外"),

    AUTO_TRADE_RATE_SET_MAX_ITEM_COUNT("autoTraderateSetMaxItemCount", "评价事务-自动回评统计子订单单次查询最大数量"),
    
    NEARLY_30_DAYS_ORDER_FLOW_MONITORING("nearly30DaysOrderFlowMonitoring", "订单监控-近30条订单流转监控"),

    REFUND_RESON_RECENT_STATISTICS_TIME("refundResonRecentStatisticsTime", "退款原因最近统计时间")
    ;


	private String name;
	private String message;

	PropertiesNameEnum(String type, String message){
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
	 * @param name
	 * @return
	 */
	public static boolean containsType(Integer name) {
		boolean ret = false;
		for (PropertiesNameEnum urpayQueryType : PropertiesNameEnum.values()){
			if (urpayQueryType.getName().equals(name)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
