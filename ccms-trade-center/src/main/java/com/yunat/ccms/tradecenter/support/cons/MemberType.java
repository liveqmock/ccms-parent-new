package com.yunat.ccms.tradecenter.support.cons;



/**
 * 会员级别
 * @author 李卫林
 *
 */
public enum MemberType {

	ORDINARY("1", "普通会员"), SENIOR("2", "高级会员"), VIP("3", "VIP会员"), SUPRE_VIP("4", "至尊VIP");

	private String type;
	private String message;

	MemberType(String type, String message){
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
		for (MemberType urpayQueryType : MemberType.values()){
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
		for (MemberType urpayQueryType : MemberType.values()){
			if (urpayQueryType.getType().equals(type)) {
				message = urpayQueryType.getMessage();
				break;
			}
		}

		return message;
	}
}
