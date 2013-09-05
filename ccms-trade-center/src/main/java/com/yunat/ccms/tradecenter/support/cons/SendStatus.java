package com.yunat.ccms.tradecenter.support.cons;



/**
 * 发送状态
 * @author 李卫林
 *
 */
public enum SendStatus {

	NOT_SENT(0, "未发送"), SEND(1, "成功发送"), NEXT_DAY(2, "第二天"), DONT_SEND(3, "不发送"),  MISS_LOGISTICS(4, "物流缺失");

	private Integer status;
	private String message;

	SendStatus(Integer status, String message){
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
	public static boolean containsType(Integer status) {
		boolean ret = false;
		for (SendStatus sendStatus : SendStatus.values()) {
			if (sendStatus.getStatus().equals(status)) {
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
		for (SendStatus sendStatus : SendStatus.values()) {
			if (sendStatus.getStatus().equals(status)) {
				message = sendStatus.getMessage();
				break;
			}
		}

		return message;
	}
}
