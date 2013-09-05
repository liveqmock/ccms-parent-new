package com.yunat.ccms.tradecenter.support.cons;

/**
 * 来源标识
 * @author 李卫林
 *
 */
public enum TradeFromType {

	JHS("JHS", "聚划算"), TAOBAO("TAOBAO", "普通淘宝"), TOP("TOP", "TOP平台"), HITAO("HITAO", "嗨淘"), WAP("WAP", "手机");

	private String type;
	private String message;


	private TradeFromType(String type, String message) {
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
		for (TradeFromType tradeFromType : TradeFromType.values()){
			if (tradeFromType.getType().equals(type)) {
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
		for (TradeFromType tradeFromType : TradeFromType.values()){
			if (tradeFromType.getType().equals(type)) {
				message = tradeFromType.getMessage();
				break;
			}
		}

		return message;
	}
}
