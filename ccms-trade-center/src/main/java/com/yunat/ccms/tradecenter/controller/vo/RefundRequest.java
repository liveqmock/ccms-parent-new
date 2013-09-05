package com.yunat.ccms.tradecenter.controller.vo;


/**
 * 退款事务参数请求对象.
 *
 * @author ming.peng
 * @date 2013-7-17
 * @since 4.2.0
 */
public class RefundRequest {


	/** The shopId. */
	private String dpId;
	private Long refundId;
	private String content;
	private boolean temp;
	private Long[] proofIds;
	private String[] filePath;

	/** 常用话术使用参数 **/
	private Long topId;
	private String topContent;

	/** 删除常用凭证 **/
	private Long proofId;

	public Long getProofId() {
		return proofId;
	}

	public void setProofId(Long proofId) {
		this.proofId = proofId;
	}

	public Long getTopId() {
		return topId;
	}

	public void setTopId(Long topId) {
		this.topId = topId;
	}

	public String getTopContent() {
		return topContent;
	}

	public void setTopContent(String topContent) {
		this.topContent = topContent;
	}

	public String[] getFilePath() {
		return filePath;
	}

	public void setFilePath(String[] filePath) {
		this.filePath = filePath;
	}

	public boolean getTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long[] getProofIds() {
		return proofIds;
	}

	public void setProofIds(Long[] proofIds) {
		this.proofIds = proofIds;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

}