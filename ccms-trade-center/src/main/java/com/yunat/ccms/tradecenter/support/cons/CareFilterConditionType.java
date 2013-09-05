package com.yunat.ccms.tradecenter.support.cons;



/**
 * 关怀过滤条件
 * @author 李卫林
 *
 */
public enum CareFilterConditionType {

	TODAY_HAS_SEND("1", "排除今天发过的客户"), BLACKLIST("2", "屏蔽短信黑名单用户"), AUTO_CONFIRM("3", "自动确认收货");

	private String type;
	private String message;

	CareFilterConditionType(String type, String message){
		this.type = type;
		this.message = message;
	}

	public String getType() {
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
	public static boolean containsType(String type) {
		boolean ret = false;
		for (CareFilterConditionType urpayQueryType : CareFilterConditionType.values()){
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
	public static String getMessage(String type) {
		//无会员等级
		String message = "";
		for (CareFilterConditionType urpayQueryType : CareFilterConditionType.values()){
			if (urpayQueryType.getType().equals(type)) {
				message = urpayQueryType.getMessage();
				break;
			}
		}

		return message;
	}
}
