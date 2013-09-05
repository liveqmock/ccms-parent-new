package com.yunat.ccms.tradecenter.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taobao.api.request.TraderateAddRequest;
import com.taobao.api.response.TraderateAddResponse;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.TraderateAutoSetRequest;
import com.yunat.ccms.tradecenter.controller.vo.TraderateVO;
import com.yunat.ccms.tradecenter.domain.TraderateAutoSetDomain;
import com.yunat.ccms.tradecenter.domain.TraderateDomainPK;

/**
 * 评价事务业务接口
 * 
 * @author tim.yin
 * 
 */
public interface TraderateService {

	public abstract Page<Map<String, Object>> findTraderateByCondition(TraderateVO traderateVO, Pageable page);

	public abstract void traderateCustomerExplain(TraderateDomainPK traderateDomainPK, String reply, String shopId)
			throws Exception;

	public abstract void traderateBatchCustomerExplain(List<LinkedHashMap<String, Object>> values) throws Exception;

	/**
	 * 评价事务关怀
	 * 
	 * @param caringDetailDomain
	 * @return
	 */
	public abstract BaseResponse<String> traderateCustomerRegard(CaringRequest caringDetailDomain);

	/**
	 * 评价事务批量关怀
	 * 
	 * @param caringDetailDomain
	 * @return
	 */
	public abstract BaseResponse<String> traderateBatchCustomerRegard(CaringRequest caringRequest);

	/**
	 * 自动评价设置（开始或关闭）
	 * 
	 * @param traderateAutoSetRequest
	 * @return
	 */
	public void traderateAutoSet(TraderateAutoSetRequest traderateAutoSetRequest);

	/**
	 * 根据店铺ID，返回自动评价设置
	 * 
	 * @param dpId
	 *            店铺ID
	 * @return
	 */
	public TraderateAutoSetDomain getTraderateAutoSet(String dpId);

	/**
	 * 商城评价解释接口-调用淘宝接口
	 * 
	 * @param oid
	 *            子订单ID
	 * @param reply
	 *            解释
	 */
	public TraderateAddResponse traderateAutoTaoBao(String shopId, TraderateAddRequest req);

}
