package com.yunat.ccms.channel.support;

/**
 * @author yangtao
 *         </br><code>SMS 短信类渠道</code> </br><code>OTHER 线下其他渠道</code> </br>
 *         <code>EDM 邮件服务渠道</code> </br><code>DM 直邮类渠道</code> </br>
 *         <code>MMS 彩信类渠道</code> </br><code>EC 电子优惠券</code> </br>
 *         <code>UMP 定向优惠</code>
 */
public enum ChannelType {
	SMS(1L), OTHER(2L), EDM(3L), DM(4L), MMS(5L), EC(6L), UMP(8L);

	private Long code;

	private ChannelType(Long _code) {
		this.code = _code;
	}

	public Long getCode() {
		return code;
	}

}