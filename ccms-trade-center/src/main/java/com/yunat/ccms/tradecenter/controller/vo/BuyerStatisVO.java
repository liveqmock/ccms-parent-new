package com.yunat.ccms.tradecenter.controller.vo;

import com.yunat.ccms.tradecenter.controller.BaseVO;

public class BuyerStatisVO extends BaseVO {
	private Integer advicesStatus;
	private Integer tradeCount;
	private Integer noPayedCount;
	private Integer payedCount;
	private Integer closeCount;
	private String msg;
	public Integer getAdvicesStatus() {
		return advicesStatus;
	}
	public void setAdvicesStatus(Integer advicesStatus) {
		this.advicesStatus = advicesStatus;
	}
	public Integer getTradeCount() {
		return tradeCount;
	}
	public void setTradeCount(Integer tradeCount) {
		this.tradeCount = tradeCount;
	}
	public Integer getNoPayedCount() {
		return noPayedCount;
	}
	public void setNoPayedCount(Integer noPayedCount) {
		this.noPayedCount = noPayedCount;
	}
	public Integer getPayedCount() {
		return payedCount;
	}
	public void setPayedCount(Integer payedCount) {
		this.payedCount = payedCount;
	}
	public Integer getCloseCount() {
		return closeCount;
	}
	public void setCloseCount(Integer closeCount) {
		this.closeCount = closeCount;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}


}
