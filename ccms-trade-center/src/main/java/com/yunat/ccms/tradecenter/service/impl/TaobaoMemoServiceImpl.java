package com.yunat.ccms.tradecenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.domain.Trade;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.tradecenter.controller.vo.TradeMemoVO;
import com.yunat.ccms.tradecenter.service.TaobaoMemoService;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeMemoManager;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

/**
 *
 *TaobaoMemoService 接口实现
 * @author shaohui.li
 * @version $Id: TaobaoMemoServiceImpl.java, v 0.1 2013-8-26 下午03:07:11 shaohui.li Exp $
 */
@Service("taobaoMemoService")
public class TaobaoMemoServiceImpl implements TaobaoMemoService {

    /** 淘宝备注 **/
    @Autowired
    private TaobaoTradeMemoManager taobaoTradeMemoManager;

    @Autowired
    private AccessTokenService     accessTokenService;

    /** 日志 对象 **/
    protected Logger               logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, TradeMemoVO> getTaobaoMemo(List<String> tids, String shopId) {
        Map<String, TradeMemoVO> map = new HashMap<String, TradeMemoVO>();
        for (String tid : tids) {
            TradeMemoVO v = new TradeMemoVO();
            v.setBuyerMemo("");
            v.setFlag(0l);
            v.setSellerMemo("");
            map.put(tid, v);
        }
        String sessionKey = getSessionKey(shopId);
        //如果获取sessionKey失败
        if (StringUtils.isBlank(sessionKey)) {
            for (Map.Entry<String, TradeMemoVO> entry : map.entrySet()) {
                entry.getValue().setBuyerMemo("获取订单备注失败,请重试!");
                entry.getValue().setSellerMemo("获取订单备注失败,请重试!");
            }
        } else {
            for (Map.Entry<String, TradeMemoVO> entry : map.entrySet()) {
                String tid = entry.getKey();
                TradeMemoVO v = getVo(sessionKey, tid);
                entry.setValue(v);
            }
        }
        return map;
    }

    /**
     * 调用淘宝接口获取买家留言
     *
     * @param sessionKey
     * @param tid
     * @return
     */
    private TradeMemoVO getVo(String sessionKey, String tid) {
        TradeFullinfoGetResponse res = null;
        TradeMemoVO v = new TradeMemoVO();
        try {
            res = taobaoTradeMemoManager.getTradeMemo(sessionKey, Long.parseLong(tid));
        } catch (Exception ex) {
            logger.error("获取订单备注失败:" + ex.getMessage(), ex);
            v.setBuyerMemo("获取订单备注失败,请重试!");
            v.setSellerMemo("获取订单备注失败,请重试!");
            return v;
        }
        if (res != null) {
            if (res.isSuccess()) {
                Trade t = res.getTrade();
                if (t != null) {
                    v.setBuyerMemo(t.getBuyerMessage());
                    v.setSellerMemo(t.getSellerMemo());
                    v.setFlag(t.getSellerFlag());
                    return v;
                }
            } else {
                v.setBuyerMemo("获取订单备注失败,请重试!");
                v.setSellerMemo("获取订单备注失败,请重试!");
            }
        }
        return v;
    }

    /**
     * 获取sessionKey
     *
     * @param dpId
     * @return
     */
    private String getSessionKey(String shopId) {
        String sessionKey = "";
        try {
            AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
            if (accessToken != null) {
                return accessToken.getAccessToken();
            }
        } catch (Exception ex) {
            logger.error("店铺[" + shopId + "] 获取sessionKey失败", ex);
        }
        return sessionKey;
    }
}
