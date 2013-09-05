package com.yunat.ccms.core.support.ucenter;

/**
 * UCenter的Set的一个属性
 * 
 * <pre>
 * 参考:http://wiki.yunat.com/display/CS/Module 
 * { "modId":"M0-MM",
 *  "appId":"0",
 *  "modName":"会员管理",
 *  "createTime":1333912329000, 
 *  "updateTime":1358587167000,
 *  "valid":false,
 *  "comments":null }
 * </pre>
 * 
 * @author MaGiCalL
 * 
 */
public class ProductModule {

	private String modId;
	private String appId;
	private String modName;
	private Long createTime;
	private Long updateTime;
	private Boolean valid;
	private String comments;

	public String getModId() {
		return modId;
	}

	public void setModId(final String modId) {
		this.modId = modId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(final String appId) {
		this.appId = appId;
	}

	public String getModName() {
		return modName;
	}

	public void setModName(final String modName) {
		this.modName = modName;
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

	public Boolean getValid() {
		return valid;
	}

	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "ProductModule [modId=" + modId + ", appId=" + appId + ", modName=" + modName + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", valid=" + valid + ", comments=" + comments + "]";
	}

}
