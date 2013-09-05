package com.yunat.ccms.tradecenter.support.cons;



/**
 * 会员级别
 * @author 李卫林
 *
 */
public enum ShippingType {

	EMS("ems", "ems"), EXPRESS("express", "快递"), POST("post", "平邮"), FREE("free", "无物流"), VIRTUAL("virtual", "虚拟");

	private String type;
	private String message;

	ShippingType(String type, String message){
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
		for (ShippingType urpayQueryType : ShippingType.values()){
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
		for (ShippingType urpayQueryType : ShippingType.values()){
			if (urpayQueryType.getType().equals(type)) {
				message = urpayQueryType.getMessage();
				break;
			}
		}

		return message;
	}
}
