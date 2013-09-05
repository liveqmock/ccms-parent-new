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
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.service.UrpayConfigService;

/**
 * 催付配置信息数据交互
 *
 * @author teng.zeng date 2013-5-31 上午11:48:52
 */
@Controller
@RequestMapping(value = "/urpay")
public class UrpayConfigController {

	@Autowired
	private UrpayConfigService urpayConfigService;

	private static Logger logger = LoggerFactory.getLogger(UrpayConfigController.class);

	@ResponseBody
	@RequestMapping(value = "/urpayConfig", method = RequestMethod.GET)
	public ControlerResult find(@RequestParam String dpId, @RequestParam Integer urpayType) {
		logger.info("请求催付界面信息：店铺id["+dpId+"];类型["+urpayType+"]");
		UrpayConfigDomain urpayConfigDomain = urpayConfigService.getByUrpayTypeAndDpId(urpayType, dpId);
		//ConfigLogDomain log = configLogService.getPreOpConfig(dpId, urpayType);
		//urpayConfigDomain.setLog(log);
		return ControlerResult.newSuccess(urpayConfigDomain);
	}

	@ResponseBody
	@RequestMapping(value = "/urpayConfig", method = RequestMethod.POST)
	public ControlerResult save(@RequestBody final UrpayConfigDomain urpayConfigDomain) {
		logger.info("催付任务配置保存！传入参数信息：" + JackSonMapper.toCJsonString(urpayConfigDomain));
		UrpayConfigDomain newUrpayConfigDomain = urpayConfigService.saveUrpayConfigDomain(urpayConfigDomain);
		if (null != newUrpayConfigDomain) {
			logger.info("催付任务配置保存成功！任务信息：" + JackSonMapper.toCJsonString(newUrpayConfigDomain));
		}
		return ControlerResult.newSuccess(newUrpayConfigDomain);
	}
}
