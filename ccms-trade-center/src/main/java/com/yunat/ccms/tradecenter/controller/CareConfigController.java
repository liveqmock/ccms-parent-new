package com.yunat.ccms.tradecenter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.service.CareConfigService;

/**
 * 关怀配置信息数据前端交互入口
 *
 * @author teng.zeng
 * date 2013-6-14 上午11:18:25
 */
@Controller
@RequestMapping(value = "/care")
public class CareConfigController {

	private static Logger logger = LoggerFactory.getLogger(CareConfigController.class);

	@Autowired
	private CareConfigService careConfigService;

	@ResponseBody
	@RequestMapping(value = "/careConfig", method = RequestMethod.GET)
	public ControlerResult find(@RequestParam String dpId, @RequestParam Integer careType) {
		logger.info("请求关怀界面信息：店铺id["+dpId+"];类型["+careType+"]");
		CareConfigDomain careConfigDomain = careConfigService.getByCareTypeAndDpId(careType, dpId);
		//ConfigLogDomain log = configLogService.getPreOpConfig(dpId, careType);
		//careConfigDomain.setLog(log);
		return ControlerResult.newSuccess(careConfigDomain);
	}

	@ResponseBody
	@RequestMapping(value = "/careConfig", method = RequestMethod.POST)
	public ControlerResult save(@RequestBody final CareConfigDomain careConfigDomain) {
		logger.info("关怀任务配置保存！传入参数信息：" + JackSonMapper.toCJsonString(careConfigDomain));
		CareConfigDomain newCareConfigDomain = careConfigService.saveCareConfigDomain(careConfigDomain);
		if (null != newCareConfigDomain) {
			logger.info("关怀任务配置保存成功！任务信息：" + JackSonMapper.toCJsonString(newCareConfigDomain));
		}
		return ControlerResult.newSuccess(newCareConfigDomain);
	}

}
