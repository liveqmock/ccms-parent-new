package com.yunat.ccms.tradecenter.support.cons;



/**
 * 催付查询类型
 * @author 李卫林
 *
 */
public enum OrderStatus {

	TRADE_NO_CREATE_PAY("TRADE_NO_CREATE_PAY", "没有创建支付宝交易"),
	WAIT_BUYER_PAY("WAIT_BUYER_PAY", "等待买家付款"),
	SELLER_CONSIGNED_PART("SELLER_CONSIGNED_PART", "卖家部分发货"),
	WAIT_SELLER_SEND_GOODS("WAIT_SELLER_SEND_GOODS", "等待卖家发货"),
	WAIT_BUYER_CONFIRM_GOODS("WAIT_BUYER_CONFIRM_GOODS", "等待买家确认收货")
	,TRADE_BUYER_SIGNED("TRADE_BUYER_SIGNED", "买家已签收,货到付款专用"),
	TRADE_FINISHED("TRADE_FINISHED", "交易成功"),
	TRADE_CLOSED("TRADE_CLOSED", "付款以后用户退款成功")
	,TRADE_CLOSED_BY_TAOBAO("TRADE_CLOSED_BY_TAOBAO", "付款以前，卖家或买家主动关闭交易");

	private String status;
	private String message;

	OrderStatus(String status, String message){
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
		for (OrderStatus urpayQueryType : OrderStatus.values()){
			if (urpayQueryType.getStatus().equals(type)) {
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
	public static String getMessage(String status) {
		String message = "";
		for (OrderStatus orderStatus : OrderStatus.values()){
			if (orderStatus.getStatus().equals(status)) {
				message = orderStatus.getMessage();
				break;
			}
		}

		return message;
	}
}
