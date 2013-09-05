package com.yunat.ccms.core.support.ucenter;

/**
 * <pre>
 * 一个dto,表示订购中心的Account.
 * 参考http://wiki.yunat.com/display/CS/Account
 * </pre>
 * 
 * @author wenjian.liang
 * 
 */
public class Account {

	private String accountName;
	private String accountPass;
	private String accountPassPlain;
	private String status;
	private Long createTime;
	private Long updateTime;
	private String createdBy;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPass() {
		return accountPass;
	}

	public void setAccountPass(final String accountPass) {
		this.accountPass = accountPass;
	}

	public String getAccountPassPlain() {
		return accountPassPlain;
	}

	public void setAccountPassPlain(final String accountPassPlain) {
		this.accountPassPlain = accountPassPlain;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "Account [accountName=" + accountName + ", accountPass=" + accountPass + ", accountPassPlain="
				+ accountPassPlain + ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", createdBy=" + createdBy + "]";
	}
}
