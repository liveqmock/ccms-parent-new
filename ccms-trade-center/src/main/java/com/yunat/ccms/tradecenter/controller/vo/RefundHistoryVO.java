package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;
import java.util.Map;

public class RefundHistoryVO {

	private boolean isAll;

	private Long pageNo;

	private List<Map<String, Object>> refundMessages;

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}

	public List<Map<String, Object>> getRefundMessages() {
		return refundMessages;
	}

	/**
	 * map中需要包含 created,owner_role(留言者身份),owner_nick,content,pic_urls(PicUrl []),message_type
	 * @param refundMessages
	 */
	public void setRefundMessages(List<Map<String, Object>> refundMessages) {
		this.refundMessages = refundMessages;
	}

	public Long getPageNo() {
		return pageNo;
	}

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}
}
