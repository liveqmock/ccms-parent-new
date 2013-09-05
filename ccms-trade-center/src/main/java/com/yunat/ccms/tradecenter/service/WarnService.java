package com.yunat.ccms.tradecenter.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.tradecenter.controller.vo.WarnConfigVO;
import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;

/**
 * 告警业务接口
 * 
 * @author tim.yin
 * 
 */
public interface WarnService {

	/**
	 * 中差评投诉告警配置
	 */
	public abstract void saveOrUpdateWarnConfig(final WarnConfigVO warnConfigVO) throws Exception;

	/**
	 * 查询告警配置
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract WarnConfigDomain searchWarnConfig(String dpId, Integer warnType) throws Exception;

	/**
	 * 根据告警类型,获取当前时间需要处理的警告配置
	 * 
	 * @return
	 */
	List<WarnConfigDomain> getRefundWarnConfigListAtPresent(String warnType);

	/**
	 * 获取当前时间需要处理的中差评警告配置
	 * 
	 * @return
	 */
	List<WarnConfigDomain> getNotGoodWarnConfigListAtPresent(String warnType);

	/**
	 * 按照店铺id，获取最后处理的中差评告警的时间
	 * 
	 * @return
	 */
	Date getLastDealNotGoodTime(String dpId);

	BaseResponse<String> sendNotGoodWarnSms(String dpNick, List<Map<String, Object>> rds, Long gatewayId, String moblies);

	public abstract void record(List<String> oids, String thread);

}
