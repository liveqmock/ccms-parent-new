package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.tradecenter.controller.vo.OrderVO;
import com.yunat.ccms.tradecenter.controller.vo.PageVO;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsResultVO;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 *
 * @author shaohui.li
 * @version $Id: OrderService.java, v 0.1 2013-6-4 下午08:21:58 shaohui.li Exp $
 */
public interface ShopReasonService {

    /**
     * 根据店铺id获得退款原因列表
     *
     * @param shopId
     * @return
     */
	List<String> getRefundReasons(String shopId);
}
