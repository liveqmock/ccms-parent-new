package com.yunat.ccms.rule.center.runtime.fact;

import com.yunat.ccms.rule.center.engine.Fact;

public class Customer extends Fact<Long> {

	private static final long serialVersionUID = -7174796340631911535L;

	/**
	 * 客户类型(回头客/首次客)
	 */
	private String customerType;
	/**
	 * 会员等级
	 */
	private String grade;
	/**
	 * 信用等级
	 */
	private String buyerGoodRatio;
	/**
	 * 全站等级
	 */
	private String vipInfo;
	/**
	 * 交易成功笔数
	 */
	private Long tradeCount;
	/**
	 * 交易成功的金额
	 */
	private Double tradeAmount;
	/**
	 * 最后交易间隔(天)
	 */
	private Long lastTradeDateDiff;

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(final String customerType) {
		this.customerType = customerType;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(final String grade) {
		this.grade = grade;
	}

	public String getBuyerGoodRatio() {
		return buyerGoodRatio;
	}

	public void setBuyerGoodRatio(final String buyerGoodRatio) {
		this.buyerGoodRatio = buyerGoodRatio;
	}

	public String getVipInfo() {
		return vipInfo;
	}

	public void setVipInfo(final String vipInfo) {
		this.vipInfo = vipInfo;
	}

	public Long getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(final Long tradeCount) {
		this.tradeCount = tradeCount;
	}

	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(final Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Long getLastTradeDateDiff() {
		return lastTradeDateDiff;
	}

	public void setLastTradeDateDiff(final Long lastTradeDateDiff) {
		this.lastTradeDateDiff = lastTradeDateDiff;
	}

	@Override
	public String toString() {
		return "Customer [customerType=" + customerType + ", grade=" + grade + ", buyerGoodRatio=" + buyerGoodRatio
				+ ", vipInfo=" + vipInfo + ", tradeCount=" + tradeCount + ", tradeAmount=" + tradeAmount
				+ ", lastTradeDateDiff=" + lastTradeDateDiff + "]";
	}

}
