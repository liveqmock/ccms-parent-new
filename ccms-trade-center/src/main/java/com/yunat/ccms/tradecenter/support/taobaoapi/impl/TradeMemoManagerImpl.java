package com.yunat.ccms.tradecenter.support.taobaoapi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TradeGetRequest;
import com.taobao.api.request.TradeMemoUpdateRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradeGetResponse;
import com.taobao.api.response.TradeMemoUpdateResponse;
import com.yunat.ccms.tradecenter.support.taobaoapi.BaseApiManager;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeMemoManager;

/**
 * User: xin.chen
 */
@Component
public class TradeMemoManagerImpl extends BaseApiManager implements TaobaoTradeMemoManager{

	protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean addOrUpdateTradeMemo(Long tid, String memo,Long flag, String shopId) {
    	TradeMemoUpdateRequest req=new TradeMemoUpdateRequest();
    	req.setTid(tid);
    	req.setMemo(memo);
    	if(flag != null){
    		req.setFlag(flag);
    	}
    	req.setReset(false);

    	try {
    		TradeMemoUpdateResponse tr = execTaobao(shopId, req);
        	if(tr == null){
        		logger.error("淘宝API交易添加/修改备注失败： tid--"+tid+"---memo--"+memo+"---shopId--"+shopId);
        		return false;
        	}
        	if(tr.isSuccess()){
        		return tr.isSuccess();
        	}else{
        		logger.error("淘宝API交易添加/修改备注失败： "+tr.getSubCode()+"---"+tr.getSubMsg());
        		return tr.isSuccess();
        	}
		} catch (Exception e) {
			logger.error("淘宝API交易添加/修改备注异常： tid--"+tid+"---memo--"+memo+"---shopId--"+shopId, e);
			return false;
		}

    }

	@Override
	public TradeFullinfoGetResponse getTradeMemo(Long tid, String shopId) {
//		TradeGetRequest req=new TradeGetRequest();
//		req.setTid(tid);
//		req.setFields("seller_memo,buyer_memo,buyer_message");
//		TradeGetResponse tr = execTaobao(shopId, req);
//
//		if(tr == null){
//    		logger.error("淘宝API获取备注失败： tid--"+tid+"---shopId--"+shopId);
//    		return tr;
//    	}
//    	if(tr.isSuccess()){
//    		return tr;
//    	}else{
//    		logger.error("淘宝API获取备注失败: "+tr.getSubCode()+"---"+tr.getSubMsg());
//    		return tr;
//    	}

    	TradeFullinfoGetRequest req=new TradeFullinfoGetRequest();
    	req.setFields("seller_memo,seller_flag,buyer_message");
    	req.setTid(tid);
    	try {
    		TradeFullinfoGetResponse tr = execTaobao(shopId, req);

        	if(tr == null){
        		logger.error("淘宝API获取备注失败： tid--"+tid+"---shopId--"+shopId);
        		return tr;
        	}
        	if(tr.isSuccess()){
        		return tr;
        	}else{
        		logger.error("淘宝API获取备注失败: "+tr.getSubCode()+"---"+tr.getSubMsg());
        		return tr;
        	}
		} catch (Exception e) {
			logger.error("淘宝API获取备注异常:  tid--"+tid+"---shopId--"+shopId, e);
			return null;
		}

	}

    @Override
    public TradeFullinfoGetResponse getTradeMemo(String sessionKey, Long tid) {
        TradeFullinfoGetRequest req=new TradeFullinfoGetRequest();
        req.setFields("seller_memo,seller_flag,buyer_message");
        req.setTid(tid);
        try {
            TradeFullinfoGetResponse tr = callTaobao(sessionKey, req);
            if(tr == null){
                logger.error("淘宝API获取备注失败： tid--"+tid);
                return tr;
            }
            if(tr.isSuccess()){
                return tr;
            }else{
                logger.error("淘宝API获取备注失败: "+tr.getSubCode()+"---"+tr.getSubMsg());
                return tr;
            }
        } catch (Exception e) {
            logger.error("淘宝API获取备注异常:  tid--"+tid, e);
            return null;
        }
    }
}
