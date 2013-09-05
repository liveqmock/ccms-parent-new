package com.yunat.ccms.ucenter.rest.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 
 * http://wiki.yunat.com/display/CS/Access+Token
 * 
 * @author xiaojing.qu
 * 
 */
@XmlRootElement
public class AccessToken {

	private String shopId;// "taobao_65927470",
	private String appId;// "0",
	private String platCode;// taobao,
	private String accessToken;// 6202229c0346f2b621e6870acbaafe0dff56ace9c3339fa48291977",
	private Long createTime;// 1358493571000,
	private Long updateTime;// 1358573549000,
	private Long expireTime;// 1390029571000//Long型Java时间戳，过期时间，当前淘宝平台与r1Expire相等
	private String refreshToken;// 6202229c0346f2b621e6870acbaafe0dff56ace9c3339fa48291977",
								// //刷新Token, 淘宝特有
	// 以下时间均为Long型时间戳，有关淘宝权限请查看淘宝文档：http://open.taobao.com/doc/detail.htm?spm=0.0.0.184.LmNWRH&id=1002
	private Long w1Expire;// 1390029571000, //Long型Java时间戳
	private Long w2Expire;// 1358575349000, //Long型Java时间戳
	private Long r1Expire;// 1390029571000, //Long型Java时间戳
	private Long r2Expire;// 1358832749000, //Long型Java时间戳
	private Long reExpire;// 1358832749000, //Long型Java时间戳
	private String tokenType; // 暂时未使用

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getW1Expire() {
		return w1Expire;
	}

	public void setW1Expire(Long w1Expire) {
		this.w1Expire = w1Expire;
	}

	public Long getW2Expire() {
		return w2Expire;
	}

	public void setW2Expire(Long w2Expire) {
		this.w2Expire = w2Expire;
	}

	public Long getR1Expire() {
		return r1Expire;
	}

	public void setR1Expire(Long r1Expire) {
		this.r1Expire = r1Expire;
	}

	public Long getR2Expire() {
		return r2Expire;
	}

	public void setR2Expire(Long r2Expire) {
		this.r2Expire = r2Expire;
	}

	public Long getReExpire() {
		return reExpire;
	}

	public void setReExpire(Long reExpire) {
		this.reExpire = reExpire;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@Override
	public String toString() {
		return "AccessToken [shopId=" + shopId + ", appId=" + appId + ", platCode=" + platCode + ", accessToken="
				+ accessToken + ", createTime=" + createTime + ", updateTime=" + updateTime + ", expireTime="
				+ expireTime + ", refreshToken=" + refreshToken + ", w1Expire=" + w1Expire + ", w2Expire=" + w2Expire
				+ ", r1Expire=" + r1Expire + ", r2Expire=" + r2Expire + ", reExpire=" + reExpire + ", tokenType="
				+ tokenType + "]";
	}

}
