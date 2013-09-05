package com.yunat.ccms.tradecenter.support.cons;



/**
 * 物流详细状态
 * @author 李卫林
 *
 */
public enum ShippingStatus {
    not_city(4, "未同城"), city(1, "已同城"),

    delivery(2, "派件中"), signed(3, "已签收"),
    ;

	private Integer status;
	private String message;

	ShippingStatus(Integer status, String message){
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 检查是否包含
	 * @param status
	 * @return
	 */
	public static boolean containsType(String status) {
		boolean ret = false;
		for (ShippingStatus abnormalStatus : ShippingStatus.values()){
			if (abnormalStatus.getStatus().equals(status)) {
				ret = true;
				break;
			}
		}

		return ret;
	}

	/**
	 * 获得描述
	 * @param status
	 * @return
	 */
	public static String getMessage(Integer status) {
		String message = "";
		for (ShippingStatus abnormalStatus : ShippingStatus.values()){
			if (abnormalStatus.getStatus().equals(status)) {
				message = abnormalStatus.getMessage();
				break;
			}
		}

		return message;
	}
}
