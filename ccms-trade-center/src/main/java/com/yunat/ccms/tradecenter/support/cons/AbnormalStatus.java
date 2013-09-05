package com.yunat.ccms.tradecenter.support.cons;



/**
 * 异常状态关键字
 * @author 李卫林
 *
 */
public enum AbnormalStatus {
    DIFFICULT_THING("疑难件", "疑难件"), NO_FLOW("无流转", "无流转"),

    SUPER_AREA("超区", "超区"), AGENCY_SIGN("代签收", "代签收"),

    REFUSE_SIGN("拒收", "拒收"), LOGISTICS_NOUPDATE_2DAY("2天物流未更新", "2天物流未更新"),

    LOGISTICS_NOUPDATE_5DAY("5天物流未更新", "5天物流未更新")
    ;

	private String status;
	private String message;

	AbnormalStatus(String status, String message){
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
	 * @param status
	 * @return
	 */
	public static boolean containsType(String status) {
		boolean ret = false;
		for (AbnormalStatus abnormalStatus : AbnormalStatus.values()){
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
	public static String getMessage(String status) {
		String message = "";
		for (AbnormalStatus abnormalStatus : AbnormalStatus.values()){
			if (abnormalStatus.getStatus().equals(status)) {
				message = abnormalStatus.getMessage();
				break;
			}
		}

		return message;
	}
}
