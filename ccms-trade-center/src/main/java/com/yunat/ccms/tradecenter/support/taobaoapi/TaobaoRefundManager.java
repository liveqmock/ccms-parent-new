package com.yunat.ccms.tradecenter.support.taobaoapi;

import com.taobao.api.response.RefundMessagesGetResponse;

public interface TaobaoRefundManager {

	/**
	 * 退款留言/凭证查询
	 * @param refund_id
	 * @param page_no
	 * @return 可能返回为null
	 */
	RefundMessagesGetResponse getRefundMessages(Long refund_id, Long page_no, String shopId);

}
