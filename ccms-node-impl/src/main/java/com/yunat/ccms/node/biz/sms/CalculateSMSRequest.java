package com.yunat.ccms.node.biz.sms;

public class CalculateSMSRequest {
	private String content;
	private int markLength;
	private int wordsLimit;
	private int gatewayType;
	private int isOrderBack;
	private String backOrderInfo;
	private String sign;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMarkLength() {
		return markLength;
	}

	public void setMarkLength(int markLength) {
		this.markLength = markLength;
	}

	public int getWordsLimit() {
		return wordsLimit;
	}

	public void setWordsLimit(int wordsLimit) {
		this.wordsLimit = wordsLimit;
	}

	public int getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(int gatewayType) {
		this.gatewayType = gatewayType;
	}

	public int getIsOrderBack() {
		return isOrderBack;
	}

	public void setIsOrderBack(int isOrderBack) {
		this.isOrderBack = isOrderBack;
	}

	public String getBackOrderInfo() {
		return backOrderInfo;
	}

	public void setBackOrderInfo(String backOrderInfo) {
		this.backOrderInfo = backOrderInfo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
