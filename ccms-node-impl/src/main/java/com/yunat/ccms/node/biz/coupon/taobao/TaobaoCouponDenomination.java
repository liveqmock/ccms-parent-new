package com.yunat.ccms.node.biz.coupon.taobao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plt_taobao_coupon_denomination")
public class TaobaoCouponDenomination implements Serializable {

	/***  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "denomination_value", unique = true, nullable = false, precision = 10, scale = 0)
	private Long denominationValue;

	@Column(name = "denomination_name")
	private String denominationName;

	public String getDenominationName() {
		return denominationName;
	}

	public void setDenominationName(String denominationName) {
		this.denominationName = denominationName;
	}

	public Long getDenominationValue() {
		return denominationValue;
	}

	public void setDenominationValue(Long denominationValue) {
		this.denominationValue = denominationValue;
	}

	@Override
	public String toString() {
		return "TaobaoCouponDenomination [denominationValue=" + denominationValue + ", denominationName="
				+ denominationName + "]";
	}

}
