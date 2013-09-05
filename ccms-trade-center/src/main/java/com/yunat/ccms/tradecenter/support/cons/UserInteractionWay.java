package com.yunat.ccms.tradecenter.support.cons;




/**
 * 用户交互方式
 * @author 李卫林
 *
 */
public enum UserInteractionWay {

	SMS(1, "短信"),
    WANGWANG(2, "旺旺"),
    PHONE(3, "电话")
    ;

	private Integer type;
	private String message;

	UserInteractionWay(Integer type, String message){
		this.type = type;
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
		for (UserInteractionWay urpayQueryType : UserInteractionWay.values()){
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
	public static String getMessage(Integer type) {
		String message = "";
		for (UserInteractionWay urpayQueryType : UserInteractionWay.values()){
			if (urpayQueryType.getType().equals(type)) {
				message = urpayQueryType.getMessage();
				break;
			}
		}
		return message;
	}
}
