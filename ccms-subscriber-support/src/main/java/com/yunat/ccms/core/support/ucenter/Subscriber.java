package com.yunat.ccms.core.support.ucenter;

import java.util.List;

/**
 * <pre>
 * 一个dto,表示订购中心的Subscriber(客户)
 * 参考http://wiki.yunat.com/display/CS/Subscriber
 * </pre>
 * 
 * @author wenjian.liang
 * 
 */
public class Subscriber {
	private Long subscriberId;
	private String appId;
	private String subscriberName;
	private Boolean etlEnabled;
	private Long createTime;
	private Long updateTime;
	private List<Shop> shops;
	private Account account;

	public String getAppId() {
		return appId;
	}

	public void setAppId(final String appId) {
		this.appId = appId;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(final String subscriberName) {
		this.subscriberName = subscriberName;
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

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(final List<Shop> shops) {
		this.shops = shops;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(final Account account) {
		this.account = account;
	}

	public Long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(final Long subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Boolean getEtlEnabled() {
		return etlEnabled;
	}

	public void setEtlEnabled(final Boolean etlEnabled) {
		this.etlEnabled = etlEnabled;
	}

	@Override
	public String toString() {
		return "Subscriber [subscriberId=" + subscriberId + ", appId=" + appId + ", subscriberName=" + subscriberName
				+ ", etlEnabled=" + etlEnabled + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", shops=" + shops + ", account=" + account + "]";
	}
}