package com.yunat.ccms.tradecenter.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.base.response.BaseResponse;
import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.OrderVO;
import com.yunat.ccms.tradecenter.controller.vo.PageVO;
import com.yunat.ccms.tradecenter.controller.vo.UrPayOrdersLogVo;
import com.yunat.ccms.tradecenter.controller.vo.UrpayOrderRequest;
import com.yunat.ccms.tradecenter.service.CustomerOrdersService;
import com.yunat.ccms.tradecenter.service.OrderInteractionService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;
import com.yunat.ccms.tradecenter.support.cons.ColumnEnum;

/**
 * 客服中心下单事物
 *
 * @author ming.peng
 * @date 2013-6-3
 * @since 4.1.0
 */
@Controller
@RequestMapping(value = "/customerCenter/orders/*")
public class CustomerOrdersController {

	private static Logger logger = LoggerFactory.getLogger(CustomerOrdersController.class);

	@Autowired
	private CustomerOrdersService cusOrdsService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderInteractionService orderInteractionService;

	/**
	 * 催付成功订单记录列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/urPayOrdersLogList", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult urPayOrdersLogList(@RequestBody UrpayOrderRequest urpayOrderRequest){
		// 得到分页数据
		PageRequest page = new PageRequest(urpayOrderRequest.getCurrPage() - 1, urpayOrderRequest.getPageSize());
		UrPayOrdersLogVo data = cusOrdsService.urPayOrdersLogList(page, urpayOrderRequest.getDpId());
		return ControlerResult.newSuccess(data);
	}

	/**
	 * 店铺的下单接待旺旺客服列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderReceptionWw", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult orderReceptionWw(@RequestBody UrpayOrderRequest urpayOrderRequest){
		List<Map<String, Object>> wwList = cusOrdsService.getOrderReceptionWwListByDpId(urpayOrderRequest.getDpId());
		return ControlerResult.newSuccess(wwList);
	}

	/**
	 * 隐藏/取消隐藏订单
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/hiddenOrder", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult hiddenOrder(@RequestBody UrpayOrderRequest req) {
		int updateBatch = cusOrdsService.updateCusOrdsShipHide(req.getTid(), ColumnEnum.getNameByKey(req.getHideColumnName()));
		if (0 >= updateBatch){
			final Map<String, Object> result = Maps.newHashMap();
			result.put("errordesc", "修改订单是否隐藏失败！");
			return ControlerResult.newError();
		}
		return ControlerResult.newSuccess();
	}

	/**
	 * 批量隐藏/取消隐藏订单
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/hiddenOrders", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult hiddenOrders(@RequestBody UrpayOrderRequest req) {
		int[] updateBatch = cusOrdsService.updateCusOrdsShipHide(req.isHide(), ColumnEnum.getNameByKey(req.getHideColumnName()), req.getTids());
		if (0 >= updateBatch.length){
			final Map<String, Object> result = Maps.newHashMap();
			result.put("errordesc", "修改订单是否隐藏失败！");
			return ControlerResult.newError();
		}
		return ControlerResult.newSuccess();
	}

	/**
	 * 测试，需要删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/customerOrdersShip", method = {RequestMethod.POST, RequestMethod.GET} , produces = "application/json; charset=utf-8")
	public ControlerResult customerOrdersShip(){
		final Map<String, Object> result = Maps.newHashMap();
		int[] insertBatch = cusOrdsService.saveCustomerOrdersShipData();
		String msg = JackSonMapper.toJsonString(insertBatch);
		logger.info(msg);
		result.clear();
		result.put("msg", msg);
		result.put("rows", insertBatch.length);
//		result.put("errordesc", "显示下单数据失败!");
		return ControlerResult.newSuccess(result);
	}


	@ResponseBody
	@RequestMapping(value = "/ordersList", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult ordersList(@RequestBody OrderQuery orderQuery) {
		Map<String, Object> result = Maps.newHashMap();

		//解析order
		if (orderQuery.getOrder() != null) {
			 String[] orderDes = orderQuery.getOrder().split("_");
			 String firstOrder = orderDes[0];
			 String firstOrderSort = orderDes[1];

			 orderQuery.setFirstOrder(firstOrder);
			 orderQuery.setFirstOrderSort(firstOrderSort);
		}

		List<OrderVO> orderVOs = orderService.findWorkOrder(orderQuery);

		result.put("total", orderQuery.getTotalItem());
		result.put("page", orderQuery.getCurrentPage());
        result.put("pageSize", orderQuery.getPageSize());
        result.put("totalPage",orderQuery.getTotalPage());
		result.put("data", orderVOs);
		return ControlerResult.newSuccess(result);
	}

	@ResponseBody
	@RequestMapping(value = "/ordersCount", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult ordersCount(@RequestBody OrderQuery orderQuery) {

		//解析order
		if (orderQuery.getOrder() != null) {
			 String[] orderDes = orderQuery.getOrder().split("_");
			 String firstOrder = orderDes[0];
			 String firstOrderSort = orderDes[1];

			 orderQuery.setFirstOrder(firstOrder);
			 orderQuery.setFirstOrderSort(firstOrderSort);
		}

		PageVO pageVO = orderService.findWorkOrderCount(orderQuery);


		return ControlerResult.newSuccess(pageVO);
	}

	@ResponseBody
	@RequestMapping(value = "/urpayOrder", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult urpayOrder(@RequestBody UrpayOrderRequest urpayOrderRequest) {
		if (StringUtils.isEmpty(urpayOrderRequest.getSmsContent())) {
			return ControlerResult.newError("短信不能为空");
		}

		if (urpayOrderRequest.getTid() != null) {
			BaseResponse<String> response = orderInteractionService.urpayOrder(urpayOrderRequest.getTid(), urpayOrderRequest.getSmsContent(), urpayOrderRequest.getGatewayId(), urpayOrderRequest.isFilterBlacklist());

			if (!response.isSuccess()) {
				return ControlerResult.newError(response.getErrMsg());
			}
		}


		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/urpayOrders", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult urpayOrders(@RequestBody UrpayOrderRequest urpayOrderRequest) {
		if (StringUtils.isEmpty(urpayOrderRequest.getSmsContent())) {
			return ControlerResult.newError("短信不能为空");
		}

		if (urpayOrderRequest.getTids() != null) {
			BaseResponse<String> response = orderInteractionService.urpayOrders(urpayOrderRequest.getTids(), urpayOrderRequest.getSmsContent(), urpayOrderRequest.getGatewayId(), urpayOrderRequest.isFilterBlacklist());

			if (!response.isSuccess()) {
				return ControlerResult.newError(response.getErrMsg());
			}
		}

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/wwUrpayOrder", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult wwUrpayOrder(@RequestBody UrpayOrderRequest urpayOrderRequest) {
		if (urpayOrderRequest.getTid() != null) {
			orderInteractionService.wwUrpayOrder(urpayOrderRequest.getTid(), urpayOrderRequest.getNote());
		}

		return ControlerResult.newSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/wwUrpayOrders", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult wwUrpayOrders(@RequestBody UrpayOrderRequest urpayOrderRequest) {
		if (urpayOrderRequest.getTids() != null) {
			orderInteractionService.wwUrpayOrders(urpayOrderRequest.getTids(), urpayOrderRequest.getNote());
		}

		return ControlerResult.newSuccess();
	}

}
