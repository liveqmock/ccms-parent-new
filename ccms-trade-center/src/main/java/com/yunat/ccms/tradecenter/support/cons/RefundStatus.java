package com.yunat.ccms.tradecenter.support.cons;



/**
 * 催付查询类型
 * @author 李卫林
 *
 */
public enum RefundStatus {

    WAIT_SELLER_AGREE("WAIT_SELLER_AGREE", "买家已经申请退款，等待卖家同意"),
    WAIT_BUYER_RETURN_GOODS("WAIT_BUYER_RETURN_GOODS", "卖家已经同意退款，等待买家退货"),
    WAIT_SELLER_CONFIRM_GOODS("WAIT_SELLER_CONFIRM_GOODS", "买家已经退货，等待卖家确认收货"),
    SELLER_REFUSE_BUYER("SELLER_REFUSE_BUYER", "卖家拒绝退款"),
    CLOSED("CLOSED", "退款关闭"),
	SUCCESS("SUCCESS", "退款成功"),
    ;

	private String status;
	private String message;

	RefundStatus(String status, String message){
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
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
		for (RefundStatus urpayQueryType : RefundStatus.values()){
			if (urpayQueryType.getStatus().equals(type)) {
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
	public static String getMessage(String status) {
		String message = "";
		for (RefundStatus orderStatus : RefundStatus.values()){
			if (orderStatus.getStatus().equals(status)) {
				message = orderStatus.getMessage();
				break;
			}
		}

		return message;
	}
}
