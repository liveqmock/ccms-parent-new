package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_tc_buyer_interaction_statistic")
public class BuyerInteractionStatisticDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 617043080864149270L;

	/**
	 * 主键
	 */
	private Long pkid;

	/**
	 * 用户昵称
	 */
	private String customerno;

	/**
	 * 店铺id
	 */
	private String dpId;

	/**
	 * 交易总数
	 */
	private Integer tradeCount;

	/**
	 * 未付款交易数
	 */
	private Integer tradeNoPayedCount;

	/**
	 * 已付款交易数
	 */
	private Integer tradePayedCount;

	/**
	 * 已关闭交易数
	 */
	private Integer tradeCloseCount;

	/**
	 * 催付次数
	 */
	private Integer urpayCount;

	/**
	 * 处理日期
	 */
	private Date dealDate;


	private Date created;
	private Date updated;

	@Transient
	public void addTradeCount(int increment) {
		tradeCount += increment;
	}

	@Transient
	public void addTradeNoPayedCount(int increment) {
		tradeNoPayedCount += increment;
	}

	@Transient
	public void addTradePayedCount(int increment) {
		tradePayedCount += increment;
	}

	@Transient
	public void addTradeCloseCount(int increment) {
		tradeCloseCount += increment;
	}

	@Transient
	public void addUrpayCount(int increment) {
		urpayCount += increment;
	}

	@Id
	@GeneratedValue
	public Long getPkid() {
		return pkid;
	}
	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getCustomerno() {
		return customerno;
	}
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	@Column(name = "dp_id")
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	@Column(name = "trade_count")
	public Integer getTradeCount() {
		return tradeCount;
	}
	public void setTradeCount(Integer tradeCount) {
		this.tradeCount = tradeCount;
	}

	@Column(name = "trade_no_payed_count")
	public Integer getTradeNoPayedCount() {
		return tradeNoPayedCount;
	}
	public void setTradeNoPayedCount(Integer tradeNoPayedCount) {
		this.tradeNoPayedCount = tradeNoPayedCount;
	}

	@Column(name = "trade_payed_count")
	public Integer getTradePayedCount() {
		return tradePayedCount;
	}
	public void setTradePayedCount(Integer tradePayedCount) {
		this.tradePayedCount = tradePayedCount;
	}

	@Column(name = "trade_close_count")
	public Integer getTradeCloseCount() {
		return tradeCloseCount;
	}
	public void setTradeCloseCount(Integer tradeCloseCount) {
		this.tradeCloseCount = tradeCloseCount;
	}

	@Column(name = "urpay_count")
	public Integer getUrpayCount() {
		return urpayCount;
	}
	public void setUrpayCount(Integer urpayCount) {
		this.urpayCount = urpayCount;
	}

	@Column(name = "deal_date")
	public Date getDealDate() {
		return dealDate;
	}
	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "(" + customerno + ")";
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof BuyerInteractionStatisticDomain) {
			BuyerInteractionStatisticDomain compareTo = (BuyerInteractionStatisticDomain)obj;

			if (customerno.equals(compareTo.getCustomerno()) && dpId.equals(compareTo.getDpId())
					&& tradeCount.equals(compareTo.getTradeCount()) && tradeCloseCount.equals(compareTo.getTradeCloseCount()) && tradeNoPayedCount.equals(compareTo.getTradeNoPayedCount())
					&& tradePayedCount.equals(compareTo.getTradePayedCount()) && urpayCount.equals(compareTo.getUrpayCount())) {
				return true;
			}
		}

		return false;
	}
}
