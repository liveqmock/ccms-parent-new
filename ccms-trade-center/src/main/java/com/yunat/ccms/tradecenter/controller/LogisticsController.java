package com.yunat.ccms.tradecenter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.LogisticsRequest;
import com.yunat.ccms.tradecenter.controller.vo.LogisticsVO;
import com.yunat.ccms.tradecenter.service.LogisticsService;
import com.yunat.ccms.tradecenter.service.OrderInteractionService;
import com.yunat.ccms.tradecenter.service.queryobject.LogisticsQuery;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 * 客服中心物流事务
 *
 * @author ming.peng
 * @date 2013-7-9
 * @since 4.2.0
 */
@Controller
@RequestMapping(value = "/customerCenter/logistics/*")
public class LogisticsController {

	protected static Logger logger = LoggerFactory.getLogger(LogisticsController.class);

	@Autowired
	private LogisticsService logisticsService;

	@Autowired
	private OrderInteractionService ordIntService;

	/**
	 * 物流 单或批量 订单关怀
	 *
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/careLogisticsOrders", method = { RequestMethod.POST, RequestMethod.GET })
	public ControlerResult careLogisticsOrders(@RequestBody CaringRequest req) {
		// 短信关怀
		Integer type = Integer.parseInt(req.getCaringType());
		if (UserInteractionType.MANUAL_LOGISTICS_SMS_CARE.getType() == type) {
			if (StringUtils.isEmpty(req.getContent())) {
				return ControlerResult.newError("短信内容不能为空!");
			}
			BaseResponse<String> res =  ordIntService.careOrders(req.getTids(), req.getContent(), req.getGatewayId(), type, req.isFilterBlacklist());
			if (!res.isSuccess()) {
				return ControlerResult.newError(res.getErrMsg());
			}
		}
		// 旺旺或电话关怀
		if (UserInteractionType.MANUAL_LOGISTICS_WW_CARE.getType() == type
				|| UserInteractionType.MANUAL_LOGISTICS_MOBILE_CARE.getType() == type) {
			ordIntService.careOrders(req.getTids(), req.getContent(), type);
		}
		return ControlerResult.newSuccess();
	}

	/**
	 * 延长收货时间
	 *
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/extensionTimes", method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public ControlerResult extensionTimes(@RequestBody LogisticsRequest req) {
		int count = logisticsService.updateTimeoutActionTimes(req);
		return count > 0 ? ControlerResult.newSuccess("订单延长成功：" + count + "笔, 失败：" + (req.getTids().length - count)
				+ "笔！") : ControlerResult.newError("订单延长收货时间失败！");
	}


	/**
	 * 物流列表
	 *
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logisticsList", method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public ControlerResult logisticsList(@RequestBody LogisticsQuery req) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<LogisticsVO> logisticsVOs = logisticsService.findWorkLogisticsList(req);
		dataMap.put("total", req.getTotalItem());
		dataMap.put("page", req.getCurrentPage());
		dataMap.put("totalPage", req.getTotalPage());
		dataMap.put("pageSize", req.getPageSize());
		dataMap.put("data", logisticsVOs);
		return ControlerResult.newSuccess(dataMap);
	}

	/**
	 * 订单的物流清单
	 *
	 * @param tid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/showLogisticsInfo", method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public Map<String, Object> getTransitStepInfo(@RequestParam("tid") String tid) {

		Map<String, Object> aMap = logisticsService.getTransitStepInfo(tid);
		System.out.println(JSONSerializer.toJSON(aMap));
		return aMap;
	}
}
