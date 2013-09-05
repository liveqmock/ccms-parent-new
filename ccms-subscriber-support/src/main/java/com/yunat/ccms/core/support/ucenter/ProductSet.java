package com.yunat.ccms.core.support.ucenter;

import java.util.List;

/**
 * <pre>
 * UCenter中的Set.
 * 参考http://wiki.yunat.com/display/CS/Set+Service
 *  {
 *    "setId":"1-PR",
 *    "setName":"互动营销收费版",
 *    "appId":"1",
 *    "createTime":1357701668000,
 *    "updateTime":1358586853000,
 *    "type":"MANDATERY",
 *    "valid":false,
 *    "modules": List of Module Structures
 * }
 * </pre>
 * 
 * @author MaGiCalL
 * 
 */
public class ProductSet {

	private String setId;
	private String setName;
	private String appId;
	private Long createTime;
	private Long updateTime;
	private String type;
	private Boolean valid;
	private List<ProductModule> productModules;

	public String getSetId() {
		return setId;
	}

	public void setSetId(final String setId) {
		this.setId = setId;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(final String setName) {
		this.setName = setName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(final String appId) {
		this.appId = appId;
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

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	public List<ProductModule> getProductModules() {
		return productModules;
	}

	public void setProductModules(final List<ProductModule> productModules) {
		this.productModules = productModules;
	}

	@Override
	public String toString() {
		return "ProductSet [setId=" + setId + ", setName=" + setName + ", appId=" + appId + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", type=" + type + ", valid=" + valid
				+ ", productModules=" + productModules + "]";
	}
}
