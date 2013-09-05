package com.yunat.ccms.tradecenter.support.taobaoapi;

import com.taobao.api.response.TradeFullinfoGetResponse;

public interface TaobaoTradeMemoManager {

	/**
	 * 对一笔交易添加/修改备注
	 * 淘宝api参见：http://api.taobao.com/apidoc/api.htm?path=cid:5-apiId:49
	 * <pre>特别注意：此接口支持对备注置为空，所以调用者可以自己对memo是否为空做校验</pre>
	 * @param tid
	 * @param memo 备注内容
	 * @param flag
	 * <pre>flag值：交易备注旗帜，可选值为：0(灰色), 1(红色), 2(黄色), 3(绿色), 4(蓝色), 5(粉红色)，默认值为0</pre>
	 * @return
	 */
	public boolean addOrUpdateTradeMemo(Long tid, String memo,Long flag, String shopId);

	/**
	 *	支持获取交易中卖家备注(seller_memo),买家留言(buyer_memo),交易备注旗帜(flag)
	 * 交易备注旗帜，可选值为：0(灰色), 1(红色), 2(黄色), 3(绿色), 4(蓝色), 5(粉红色)，默认值为0
	 *<pre>调用示例：
	 *	TradeFullinfoGetResponse tradeRes = taobaoTradeMemoManager.getTradeMemo(tid, shopId);
    	if(tradeRes.isSuccess()){
    		tradeRes.getTrade().getBuyerMemo();
    		tradeRes.getTrade().getSellerMemo();
    		tradeRes.getTrade().getSellerFlag();
    	}else{
    		//TODO 处理错误异常
    	}</pre>
	 * @param tid
	 * @param shopId
	 * @return 可能为null，其中trade也可能为null。
	 */
	public TradeFullinfoGetResponse getTradeMemo(Long tid, String shopId);


	/**
	 *获取留言
	 *
	 * @param sessionKey
	 * @param tid
	 * @return
	 */
	public TradeFullinfoGetResponse getTradeMemo(String sessionKey,Long tid);

}
