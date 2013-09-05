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

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.WarnConfigVO;
import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;
import com.yunat.ccms.tradecenter.service.WarnService;

/**
 * 订单中心-异常告警-controller
 * 
 * @notify 包括 中差评告警 和 退款告警
 * 
 * @author tim.yin
 * 
 */

@Controller
public class WarnController {

	private static Logger logger = LoggerFactory.getLogger(WarnController.class);

	@Autowired
	private WarnService warnService;

	/**
	 * 查找
	 */

	@RequestMapping(value = "/warn/load", method = RequestMethod.GET)
	@ResponseBody
	public ControlerResult loadWarnConfig(@RequestParam String dpId, @RequestParam Integer warnType) throws Exception {
		WarnConfigDomain warnConfigDomain;

		try {
			warnConfigDomain = warnService.searchWarnConfig(dpId, warnType);
		} catch (Exception e) {
			logger.info("加载告警配置失败 : [exception:]", e);
			return ControlerResult.newError("", "加载告警配置失败！");
		}

		return ControlerResult.newSuccess(warnConfigDomain);
	}

	/**
	 * 告警配置
	 * 
	 * @param traderateVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/warn/add", method = RequestMethod.POST)
	@ResponseBody
	public ControlerResult saveOrUpdateWarnConfig(@RequestBody WarnConfigVO warnConfigVO) throws Exception {

		try {
			warnService.saveOrUpdateWarnConfig(warnConfigVO);
		} catch (Exception e) {
			logger.info("告警保存配置失败 : [exception:]", e);
			return ControlerResult.newError("", "告警保存配置失败！");
		}

		return ControlerResult.newSuccess();
	}

}
