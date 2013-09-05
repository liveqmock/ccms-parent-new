package com.yunat.ccms.tradecenter.support.taobaoapi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taobao.api.request.RefundMessagesGetRequest;
import com.taobao.api.response.RefundMessagesGetResponse;
import com.yunat.ccms.tradecenter.support.taobaoapi.BaseApiManager;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoRefundManager;

@Component
public class TaobaoRefundManagerImpl extends BaseApiManager implements TaobaoRefundManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public RefundMessagesGetResponse getRefundMessages(Long refund_id, Long page_no, String shopId) {
		RefundMessagesGetRequest req=new RefundMessagesGetRequest();
		req.setFields("owner_role,owner_nick,created,content,pic_urls,message_type");
		req.setRefundId(refund_id);
		if(page_no == null){
			page_no = 1L;
		}
		req.setPageNo(page_no);
		req.setPageSize(40L);

		RefundMessagesGetResponse tr = execTaobao(shopId, req);

		if(tr == null){
    		logger.error("退款留言/凭证查询 查询淘宝失败 refund_id："+refund_id+"---page_no--"+page_no+"---shopId--"+shopId);
    		return null;
    	}
    	if(tr.isSuccess()){
    		return tr;
    	}else{
    		logger.error(refund_id+ "退款留言/凭证查询 查询淘宝异常："+tr.getSubCode()+"---"+tr.getSubMsg());
    		return tr;
    	}
	}

}
