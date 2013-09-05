package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.TradeMemoVO;

/**
 *
 * 获取淘宝买家备注信息
 * @author shaohui.li
 * @version $Id: TaobaoMemoService.java, v 0.1 2013-8-26 下午03:00:05 shaohui.li Exp $
 */
public interface TaobaoMemoService {

    /**
     *
     * 根据店铺+交易 获取备注
     *
     * @param tid
     * @param shopId
     * @return
     */
    public Map<String,TradeMemoVO> getTaobaoMemo(List<String> tids,String shopId);

}
