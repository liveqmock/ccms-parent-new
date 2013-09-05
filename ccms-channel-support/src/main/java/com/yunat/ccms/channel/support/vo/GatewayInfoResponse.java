package com.yunat.ccms.channel.support.vo;

public class GatewayInfoResponse {
	private Long gatewayId;
	private String gatewayName;
	private Double gatewayPrice;
	private Double gatewayBalance;
	private Integer gatewayType;
	private Integer wordsLimit;
	private String unsubscribeMessage;
	private Integer markLength;
	private String sign;
	private String notice;

	public Long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getGatewayName() {
		return gatewayName;
	}

	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}

	public Double getGatewayPrice() {
		return gatewayPrice;
	}

	public void setGatewayPrice(Double gatewayPrice) {
		this.gatewayPrice = gatewayPrice;
	}

	public Double getGatewayBalance() {
		return gatewayBalance;
	}

	public void setGatewayBalance(Double gatewayBalance) {
		this.gatewayBalance = gatewayBalance;
	}

	public Integer getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(Integer gatewayType) {
		this.gatewayType = gatewayType;
	}

	public Integer getWordsLimit() {
		return wordsLimit;
	}

	public void setWordsLimit(Integer wordsLimit) {
		this.wordsLimit = wordsLimit;
	}

	public String getUnsubscribeMessage() {
		return unsubscribeMessage;
	}

	public void setUnsubscribeMessage(String unsubscribeMessage) {
		this.unsubscribeMessage = unsubscribeMessage;
	}

	public Integer getMarkLength() {
		return markLength;
	}

	public void setMarkLength(Integer markLength) {
		this.markLength = markLength;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}