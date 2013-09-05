package com.yunat.ccms.auth.login.taobao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "plt_taobao_shop")
public class TaobaoShop implements Serializable {

	/***  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "shop_id", nullable = false, length = 50)
	private String shopId;

	@Column(name = "shop_name", nullable = false, length = 100)
	private String shopName;

	@Column(name = "shop_type", unique = true, nullable = false, length = 50)
	private String shopType;

	/**
	 * 该店铺最早的订单时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "order_created_earliest", nullable = true)
	private Date earliestOrder;

	/**
	 * 该店铺最近的订单时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "order_created_latest", nullable = true)
	private Date latestOrder;

	/**
	 * 该店铺最早的订单时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "acookie_visit_date", nullable = true)
	private Date latestAcookie;

	public Date getLatestAcookie() {
		return latestAcookie;
	}

	public void setLatestAcookie(Date latestAcookie) {
		this.latestAcookie = latestAcookie;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Date getEarliestOrder() {
		return earliestOrder;
	}

	public void setEarliestOrder(Date earliestOrder) {
		this.earliestOrder = earliestOrder;
	}

	public Date getLatestOrder() {
		return latestOrder;
	}

	public void setLatestOrder(Date latestOrder) {
		this.latestOrder = latestOrder;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	@Override
	public String toString() {
		return "TaobaoShop [shopId=" + shopId + ", shopName=" + shopName + ", shopType=" + shopType + "]";
	}

}
