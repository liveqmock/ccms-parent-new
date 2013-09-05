package com.yunat.ccms.tradecenter.support.cons;

public enum OrderItemResult {

	GOOD("good", "好评"),
	NEUTRAL("neutral", "中评"),
	BAD("bad", "差评"),
    ;

	private String desc;
	private String message;

	OrderItemResult(String desc, String message){
		this.desc = desc;
		this.message = message;
	}


	public String getDesc() {
		return desc;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 获得描述
	 * @param status
	 * @return
	 */
	public static String getMessage(String status) {
		String message = "";
		for (OrderItemResult orderStatus : OrderItemResult.values()){
			if (orderStatus.getDesc().equals(status)) {
				message = orderStatus.getMessage();
				break;
			}
		}
		return message;
	}
}
