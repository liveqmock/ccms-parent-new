package com.yunat.ccms.tradecenter.support.cons;

/**
 * 
* @Description: 评价事务-自动评价设置-评价方式
* @author fanhong.meng
* @date 2013-7-15 下午3:22:11 
*
 */
public enum AutoSetTraderateTypeEnum {
	
	ORDER_SUCCESS("order_success", "订单交易成功后自动给买家好评"),
	ORDER_TRADERATE("order_traderate", "买家评价后自动给买家好评");
	
	private String type;
	private String message;
	
	private AutoSetTraderateTypeEnum(String type, String message) {
		this.type = type;
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
