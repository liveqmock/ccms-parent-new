package com.yunat.ccms.rule.center.conf.planGroup;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yunat.ccms.rule.center.conf.plan.Plan;

/**
 * 方案组.
 * 每个店铺有一个方案组,所以方案组的id直接使用店铺的id.注:店铺id是字符串型的
 * 
 * @author wenjian.liang
 */
@Entity
@Table(name = "rc_plan_group")
public class PlanGroup {

	@Id
	@Column(name = "shop_id")
	private String shopId;

	/**
	 * 备注的签名
	 */
	@Column(name = "sign")
	private String sign;

	/**
	 * 方案列表
	 */
	@Transient
	private List<Plan> plans;

	public String getSign() {
		return sign;
	}

	public void setSign(final String sign) {
		this.sign = sign;
	}

	public List<Plan> getPlans() {
		return plans;
	}

	public void setPlans(final List<Plan> plans) {
		this.plans = plans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (shopId == null ? 0 : shopId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlanGroup other = (PlanGroup) obj;
		if (shopId == null) {
			if (other.shopId != null) {
				return false;
			}
		} else if (!shopId.equals(other.shopId)) {
			return false;
		}
		return true;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(final String shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "PlanGroup [shopId=" + shopId + ", sign=" + sign + "]";
	}
}
