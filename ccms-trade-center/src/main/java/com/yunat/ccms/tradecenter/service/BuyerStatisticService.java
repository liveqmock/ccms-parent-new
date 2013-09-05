package com.yunat.ccms.tradecenter.service;

import java.util.Date;
import java.util.List;

public interface BuyerStatisticService {

	/**
	 * 增量统计用户店铺交互信息
	 * 订单总数、已关闭订单总数、催付次数....等用户与店铺的交互属性统计
	 * @param days TODO
	 * @param shopIds TODO
	 */
	void staticBuyerInteractionIncre(List<String> dpIds, int days);

	 /**
     * 全量统计用户店铺交互信息
     * 订单总数、已关闭订单总数、催付次数....等用户与店铺的交互属性统计
     * @param days TODO
     * @param shopIds TODO
     */
    void staticBuyerInteraction(String dpId, int days, Date date);

    /**
     * 统计已知用户店铺交互信息
     * 订单总数、已关闭订单总数、催付次数....等用户与店铺的交互属性统计
     * @param dpId
     * @param days
     * @param date
     * @param customernos
     */
	void staticBuyerInteractionByCus(String dpId, int days, Date date, List<String> customernos);
}
