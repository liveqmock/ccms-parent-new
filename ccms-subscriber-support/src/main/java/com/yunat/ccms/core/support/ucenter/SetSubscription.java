package com.yunat.ccms.core.support.ucenter;

/**
 * 订购中心的SetSubscription，是用户（Subscriber）订购了的产品集合（Set）。
 * 
 * <pre>
 * 参考http://wiki.yunat.com/display/CS/Set+Subscription
 * 
 *  {
 *    "setSubscriptionId":0,
 *    "subscriberId":132,
 *    "setId":"1-PR",
 *    "platCode":"taobao",
 *    "beginTime":1344700800000,
 *    "endTime":1356841929000
 * }
 * </pre>
 * 
 * @author MaGiCalL
 * 
 */
public class SetSubscription {

	private Long setSubScriptionId;
	private Long subscriberId;
	private String setId;
	private Long beginTime;
	private Long endTime;

	public Long getSetSubScriptionId() {
		return setSubScriptionId;
	}

	public void setSetSubScriptionId(final Long setSubScriptionId) {
		this.setSubScriptionId = setSubScriptionId;
	}

	public Long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(final Long subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(final String setId) {
		this.setId = setId;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(final Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(final Long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "SetSubscription [setSubScriptionId=" + setSubScriptionId + ", subscriberId=" + subscriberId
				+ ", setId=" + setId + ", beginTime=" + beginTime + ", endTime=" + endTime + "]";
	}

}
