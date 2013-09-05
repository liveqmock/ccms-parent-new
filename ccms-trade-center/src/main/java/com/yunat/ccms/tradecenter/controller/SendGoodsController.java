package com.yunat.ccms.tradecenter.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.service.OrderInteractionService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 *
 * 发货事务控制器
 *
 * @author shaohui.li
 * @version $Id: SendGoodsController.java, v 0.1 2013-7-8 下午04:09:12 shaohui.li
 *          Exp $
 */
@Controller
@RequestMapping(value = "/sendGoods/orders/*")
public class SendGoodsController {

    private static Logger logger = LoggerFactory.getLogger(SendGoodsController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderInteractionService orderInteractionService;

	/**
	 *
	 * 发货查询
	 *
	 * @param sendGoodsQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ordersList", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	public ControlerResult ordersList(@RequestBody SendGoodsQueryRequest sendGoodsQuery) {
		Map<String, Object> result = Maps.newHashMap();
		try{
		    result.put("data", orderService.querySendGoodsOrders(sendGoodsQuery));
	        sendGoodsQuery.setTotalItem((int)orderService.querySendGoodsOrdersCount(sendGoodsQuery));
	        result.put("page", sendGoodsQuery.getCurrentPage());
	        result.put("pageSize", sendGoodsQuery.getPageSize());
	        result.put("total", sendGoodsQuery.getTotalItem());
	        result.put("totalPages", sendGoodsQuery.getTotalPage());
	        result.put("serverTime", DateUtils.getStringDate(new Date()));
	        return ControlerResult.newSuccess(result);
		}catch(Exception ex){
		    logger.error("发货查询出错",ex);
		    return ControlerResult.newError("出错:" + ex.getMessage());
		}
	}

	/**
	 * 发货 单或批量 订单关怀
	 *
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "careOrders", method = RequestMethod.POST)
	public ControlerResult careOrders(@RequestBody CaringRequest req) {
		// 短信关怀
		Integer type = Integer.parseInt(req.getCaringType());
		if (UserInteractionType.MANUAL_SENDGOODS_SMS_CARE.getType() == type) {
			if (StringUtils.isEmpty(req.getContent())) {
				return ControlerResult.newError("短信内容不能为空!");
			}
			BaseResponse<String> response = orderInteractionService.careOrders(req.getTids(), req.getContent(), req.getGatewayId(), type, req.isFilterBlacklist());
			if (!response.isSuccess()) {
				return ControlerResult.newError(response.getErrMsg());
			}
		}
		// 旺旺或电话关怀
		if (UserInteractionType.MANUAL_SENDGOODS_WANGWANG_CARE.getType() == type || UserInteractionType.MANUAL_SENDGOODS_MOBILE_CARE.getType() == type) {
			orderInteractionService.careOrders(req.getTids(), req.getContent(), type);
		}

		return ControlerResult.newSuccess();
	}


}
