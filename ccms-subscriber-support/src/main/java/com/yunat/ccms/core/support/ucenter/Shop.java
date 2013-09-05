package com.yunat.ccms.core.support.ucenter;

import java.util.Map;

/**
 * <pre>
 * 一个dto,表示订购中心的Shop. 
 * 参考http://wiki.yunat.com/display/CS/Shop
 * </pre>
 * 
 * @author wenjian.liang
 * 
 */
public class Shop {

	private String shopId;
	private String platCode;
	private String platShopId;
	private String platNick;
	private Long openDate;
	private Long createTime;
	private Long updateTime;
	private Map<String, String> shopProperties;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(final String shopId) {
		this.shopId = shopId;
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(final String platCode) {
		this.platCode = platCode;
	}

	public String getPlatShopId() {
		return platShopId;
	}

	public void setPlatShopId(final String platShopId) {
		this.platShopId = platShopId;
	}

	public String getPlatNick() {
		return platNick;
	}

	public void setPlatNick(final String platNick) {
		this.platNick = platNick;
	}

	public Long getOpenDate() {
		return openDate;
	}

	public void setOpenDate(final Long openDate) {
		this.openDate = openDate;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(final Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(final Long updateTime) {
		this.updateTime = updateTime;
	}

	public Map<String, String> getShopProperties() {
		return shopProperties;
	}

	public void setShopProperties(final Map<String, String> shopProperties) {
		this.shopProperties = shopProperties;
	}

	@Override
	public String toString() {
		return "Shop [shopId=" + shopId + ", platCode=" + platCode + ", platShopId=" + platShopId + ", platNick="
				+ platNick + ", openDate=" + openDate + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", shopProperties=" + shopProperties + "]";
	}
}
