package com.yunat.ccms.tradecenter.support.cons;



/**
 * 催付查询类型
 * @author 李卫林
 *
 */
public enum UrpayQueryType {

	NO_URPAY(0, "未催付"), HAS_URPAY(1, "已催付"), ADVICE_URPAY(2, "建议催付款"), DEPRECATED_URPAY(3, "建议不催付");

	private Integer type;
	private String message;

	UrpayQueryType(Integer code, String message){
		this.type = code;
		this.message = message;
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
		for (UrpayQueryType urpayQueryType : UrpayQueryType.values()){
			if (urpayQueryType.getType().equals(type)) {
				ret = true;
				break;
			}
		}

		return ret;
	}
}
