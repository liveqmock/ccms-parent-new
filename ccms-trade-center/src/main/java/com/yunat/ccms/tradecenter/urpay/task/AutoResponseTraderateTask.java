package com.yunat.ccms.tradecenter.urpay.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.taobao.api.request.TraderateAddRequest;
import com.taobao.api.response.TraderateAddResponse;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.OrderItemDomain;
import com.yunat.ccms.tradecenter.domain.TraderateAutoLogDomain;
import com.yunat.ccms.tradecenter.domain.TraderateAutoSetDomain;
import com.yunat.ccms.tradecenter.repository.OrderItemRepository;
import com.yunat.ccms.tradecenter.repository.TraderateAutoLogRepository;
import com.yunat.ccms.tradecenter.repository.TraderateAutoSetRepository;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.service.TraderateService;
import com.yunat.ccms.tradecenter.support.cons.AutoSetTraderateTypeEnum;
import com.yunat.ccms.tradecenter.support.cons.PropertiesNameEnum;

/**
 * 
 * @Description: 评价事务-自动回复评价
 * @author fanhong.meng
 * @date 2013-7-15 上午10:17:12
 * 
 */
public class AutoResponseTraderateTask extends BaseJob {

	@Autowired
	private TraderateService traderateService;

	@Autowired
	private TraderateAutoSetRepository traderateAutoSetRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private TraderateAutoLogRepository traderateAutoLogRepository;

	@Autowired
	private PropertiesConfigManager propertiesConfigManager;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("系统 评价事务-自动评价设置，开始!");
		long s0 = System.currentTimeMillis();
		// 获取 开启状态的自动评价回复设置
		List<TraderateAutoSetDomain> traderateAutoSetList = traderateAutoSetRepository.findTraderateAutoSet(1);
		if (0 >= traderateAutoSetList.size()) {
			logger.info("系统 评价事务-自动评价设置关闭，不进行设置!");
			return;
		}

		// 获取自动回评单次最大统计子订单数量
		Integer maxCount = propertiesConfigManager.getInt(null,
				PropertiesNameEnum.AUTO_TRADE_RATE_SET_MAX_ITEM_COUNT.getName());
		String nowDate = DateUtils.getStringDate(new Date());

		for (TraderateAutoSetDomain traderateAutoSetDomain : traderateAutoSetList) {
			String dpId = traderateAutoSetDomain.getDp_id();
			String type = traderateAutoSetDomain.getType();
			// 获取最新一次执行时间
			String modified = traderateAutoSetDomain.getLatest_time();
			List<OrderItemDomain> orderItemList = new ArrayList<OrderItemDomain>();
			List<TraderateAutoLogDomain> tradeRateLoglist = new ArrayList<TraderateAutoLogDomain>();
			Integer start = 0;
			Integer end = maxCount;
			// 统计当前执行回评订单总数，如果大于单次查询MAX，使用分页执行
			Integer itemCount = getItemCount(type, modified, dpId);
			if (itemCount > maxCount) {
				for (int i = 0; i < (itemCount/maxCount)+1; i++) {
					orderItemList = getItemList(type, modified, dpId, new PageRequest(start, end));
					executeAutoTraderate(orderItemList, traderateAutoSetDomain, dpId, tradeRateLoglist);
					start++;
					batchSaveTraderateAutoLog(tradeRateLoglist);
				}
			} else {
				orderItemList = getItemList(type, modified, dpId, new PageRequest(start, end));
				executeAutoTraderate(orderItemList, traderateAutoSetDomain, dpId, tradeRateLoglist);
				batchSaveTraderateAutoLog(tradeRateLoglist);
			}
			// 更新 最新一次执行时间为当前时间
			traderateAutoSetDomain.setLatest_time(nowDate);
			traderateAutoSetRepository.save(traderateAutoSetDomain);
			logger.info("系统 评价事务-自动评价设置,店铺：{},订单回评{}条!", dpId, itemCount);
		}
		long e0 = System.currentTimeMillis();
		logger.info("系统 评价事务-自动评价设置，结束!耗时:{}", (e0-s0));
	}

	/**
	 * 批量插入自动回评日志
	 * 
	 * @param tradeRateLoglist
	 */
	private void batchSaveTraderateAutoLog(List<TraderateAutoLogDomain> tradeRateLoglist) {
		traderateAutoLogRepository.save(tradeRateLoglist);
		tradeRateLoglist.clear();
	}

	/**
	 * 执行自动回评
	 * 
	 * @param orderItemList
	 *            回评的子订单集合
	 * @param traderateAutoSetDomain
	 * @param dpId
	 * @param tradeRateLoglist
	 *            回评日志记录集合
	 */
	private void executeAutoTraderate(List<OrderItemDomain> orderItemList,
			TraderateAutoSetDomain traderateAutoSetDomain, String dpId, List<TraderateAutoLogDomain> tradeRateLoglist) {
		for (OrderItemDomain orderItemDomain : orderItemList) {
			TraderateAddRequest req = new TraderateAddRequest();
			req.setTid(Long.parseLong(orderItemDomain.getTid()));
			req.setOid(Long.parseLong(orderItemDomain.getOid()));
			req.setResult("good"); // good(好评,默认),neutral(中评),bad(差评)
			req.setRole("seller"); // seller(卖家),buyer(买家)
			req.setContent(traderateAutoSetDomain.getContent());
			req.setAnony(false); // 卖家评不能匿名

			// 调用TaoBao接口 设置订单评价
			TraderateAddResponse response = traderateService.traderateAutoTaoBao(dpId, req);
			logger.info("系统 评价事务-自动评价设置，调用淘宝接口返回：{}!", response.isSuccess());
			// 记录子订单评价自动回评日志
			TraderateAutoLogDomain traderateAutoLogDomain = new TraderateAutoLogDomain();
			traderateAutoLogDomain.setDpId(dpId);
			if (response.isSuccess()) {
				// 设置成功
				traderateAutoLogDomain.setTid(response.getTradeRate().getTid().toString());
				traderateAutoLogDomain.setOid(response.getTradeRate().getOid().toString());
				traderateAutoLogDomain.setCreated(DateUtils.getStringDate(response.getTradeRate().getCreated()));
				traderateAutoLogDomain.setStatus(true);
			} else {
				// 设置失败
				traderateAutoLogDomain.setTid(orderItemDomain.getTid());
				traderateAutoLogDomain.setOid(orderItemDomain.getOid());
				traderateAutoLogDomain.setCreated(DateUtils.getStringDate(new Date()));
				traderateAutoLogDomain.setErrorCode(response.getErrorCode() + ":" + response.getSubCode());
				traderateAutoLogDomain.setErrorMsg(response.getMsg() + ":" + response.getSubMsg());
				traderateAutoLogDomain.setStatus(false);
			}
			tradeRateLoglist.add(traderateAutoLogDomain);
		}
		orderItemList.clear();
	}

	/**
	 * 根据自动评价方式统计子订单
	 * @param type
	 * @param modified
	 * @param dpId
	 * @return
	 */
	private Integer getItemCount(String type, String modified, String dpId) {
		// 根据自动评价方式统计子订单
		if (AutoSetTraderateTypeEnum.ORDER_SUCCESS.getType().equals(type)) {
			return Integer.parseInt(orderItemRepository.countOrderItems(modified, dpId, false).toString());
		}
		if (AutoSetTraderateTypeEnum.ORDER_TRADERATE.getType().equals(type)) {
			return Integer.parseInt(orderItemRepository.countOrderItems(modified, dpId, false, true).toString());
		}
		return 0;
	}

	/**
	 * 根据自动评价方式获取子订单
	 * @param type
	 * @param modified
	 * @param dpId
	 * @param start
	 * @param end
	 * @return
	 */
	private List<OrderItemDomain> getItemList(String type, String modified, String dpId, Pageable pageble) {
		if (AutoSetTraderateTypeEnum.ORDER_SUCCESS.getType().equals(type)) {
			return orderItemRepository.findOrderItems(modified, dpId, false, pageble);
		}
		if (AutoSetTraderateTypeEnum.ORDER_TRADERATE.getType().equals(type)) {
			return orderItemRepository.findOrderItems(modified, dpId, false, true, pageble);
		}
		return new ArrayList<OrderItemDomain>();
	}

}
