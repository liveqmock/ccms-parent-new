package com.yunat.ccms.tradecenter.urpay.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;
import com.yunat.ccms.tradecenter.repository.TraderateRepository;
import com.yunat.ccms.tradecenter.service.RefundWarnService;
import com.yunat.ccms.tradecenter.service.WarnService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 * 任务启动由quartz调度，内部所以简化逻辑，不记录前次执行的任何信息
 *
 * @author Administrator
 *
 */
public class NotGoodWarnTask extends BaseJob {

	/** 日志对象 **/
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected WarnService warnService;

	@Autowired
	protected TraderateRepository traderateRepository;

	@Autowired
	protected RefundWarnService refundWarnService;

	@Autowired
	protected TaobaoShopService taobaoShopService;

	/** 当前执行线程 **/
	private String thread;

	/** TODO
	 * shop_nick23点23分收到一个差/中评，昵称：XXXXXX 详情：XXXXXXXXXXXXXXXXXX【数据赢家】
	 */

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("----------------------中差评警告任务启动了------------------------");
		thread = DateUtils.getStringDate(new Date());
		List<WarnConfigDomain> configList = getWarnConfig(UserInteractionType.NOT_GOOD_TRADERATE_WARN.getType().toString());
		if (configList != null) {
			logger.info("中差评配置店铺 ："+ configList.size()+ " 现在时间 ： "+ thread);
			for (WarnConfigDomain config : configList) {

//			 短信通知里的开始时间,也是最后一次退款警告的有效执行时间
//				Date lastDealTime = warnService.getLastDealNotGoodTime(config.getDpId());
//				//若warn_status中最后的发送时间早于 warn_config表中的updated，则用config表的时间
//				if(lastDealTime != null){
//					if(lastDealTime.before(config.getUpdated())){
//						lastDealTime = config.getUpdated();
//					}
//				}
				Date lastDealTime = config.getUpdated();
				if(lastDealTime == null || lastDealTime.before(org.apache.commons.lang.time.DateUtils.addDays(new Date(), -1))){
					//最早从早一天开始找中差评
					lastDealTime = org.apache.commons.lang.time.DateUtils.addDays(new Date(), -1);
				}

				String dpNick = taobaoShopService.get(config.getDpId()).getShopName();
				String dpId = config.getDpId();
				logger.info("dpId: "+ dpId + "   查询中差评订单按lastDealTime： "+ lastDealTime);

				List<Map<String, Object>> rds = traderateRepository.getNotGoodListByDpId(dpId, lastDealTime);
				logger.info("从 "+ lastDealTime+ " 开始，待处理中差评单数: " + rds.size());

				if (rds.size() != 0) {

					List<String> oids = new ArrayList<String>();
					List<Map<String, Object>> dealList = new ArrayList<Map<String, Object>>();
					int notGoodUnDealNum = 0;

					for (Map<String, Object> rd : rds) {
						if (isUndeal(rd)) {
							notGoodUnDealNum++;
							oids.add(rd.get("oid").toString());
							dealList.add(rd);
						}
					}
					BaseResponse<String> br = warnService.sendNotGoodWarnSms(dpNick, dealList, null, config.getWarnMobiles());

					if (br.isSuccess()) {
						warnService.record(oids, thread);
						logger.info("店铺 " + dpId + " 中差评告警成功....");
						logger.info("[接受者]:" + config.getWarnMobiles());
					} else {
						logger.error("店铺 " + dpId + " 中差评告警失败： [ErrCode]-" + br.getErrCode() + "-[ErrDesc]-"
								+ br.getErrDesc() + "-[ErrMsg]-" + br.getErrMsg());
						logger.error("[接受者]:" + config.getWarnMobiles());
					}
				} else {
					logger.info("店铺 " + dpId + " 中差评告警成功  无中差评订单....");
				}
			}
		}
		logger.info("----------------------中差评警告任务结束了------------------------");
	}

	/**
	 * 判断是否处理过
	 * @param rd
	 * @return
	 */
	private boolean isUndeal(Map<String, Object> rd) {
		String oid = rd.get("oid").toString();
		return traderateRepository.findIfDealWithNotGood(oid);
	}

	/**
	 * 根据告警类型，获取警告配置表的有效信息
	 *
	 * @return
	 */
	private List<WarnConfigDomain> getWarnConfig(String warnType) {
		return warnService.getNotGoodWarnConfigListAtPresent(warnType);
	}

}
