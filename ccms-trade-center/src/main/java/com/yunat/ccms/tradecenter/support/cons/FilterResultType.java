package com.yunat.ccms.tradecenter.support.cons;



/**
 * 用户交互类型
 * @author 李卫林
 *
 */
public enum FilterResultType {
    NEXT_DAY("1", "需要次日发送"), REPEAT("3", "去重订单"),NOT_SEND("2","不需要发送的订单");

	private String type;
	private String message;

	private FilterResultType(String type, String message) {
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
		for (FilterResultType filterResultType : FilterResultType.values()) {
			if (filterResultType.getType().equals(type)) {
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
		String message = "";
		for (FilterResultType filterResultType : FilterResultType.values()) {
			if (filterResultType.getType().equals(type)) {
				message = filterResultType.getMessage();
				break;
			}
		}

		return message;
	}


}
