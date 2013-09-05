package com.yunat.ccms.acceptance;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.SpringConcordionRunner;
import com.yunat.ccms.tradecenter.controller.vo.OrderVO;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;
import com.yunat.ccms.tradecenter.support.util.ListUtil;


@RunWith(SpringConcordionRunner.class)
public class promptUrpaySpecTest extends AbstractJunit4SpringContextBaseTests{

	public void aa(){

	}

	@Autowired
	private OrderService orderService;

	private List<OrderVO> getOrderVO() {
		//OrderService orderService = new OrderServiceImpl();

		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setCreatedStartTime("2013-06-05 00:00:00");
		orderQuery.setCreatedEndTime("2013-06-07 00:00:00");
		orderQuery.setDpId("144939");

		List<OrderVO> orderVOs = orderService.findWorkOrder(orderQuery);

		return orderVOs;
	}
	public Iterable<String> getTid() {
		SortedSet<String> matches = new TreeSet<String>();
		//matches.add("1000001");

		List<OrderVO> orderVOs = getOrderVO();
		List<String> ids = ListUtil.getPropertiesFromList(orderVOs, "tid");

		matches.addAll(ids);
		return matches;

	}
	public String getUrpayAdvice(String tid)
	{
		String urpay = "";
		//TODO 根据订单号，把该订单当前的建议催付情况查询出来
		List<OrderVO> orderVOs = getOrderVO();

		OrderVO orderVO = ListUtil.getObjectFromList(orderVOs,"tid", tid);

		if (orderVO != null) {
			if (orderVO.getUrpayStatus() == 1) {
				 urpay = "已催付";
			} else {
				if (orderVO.getUrpayStatus0().getAdvicesStatus() == 0) {
					urpay = "建议不催付";
				} else {
					urpay = "建议催付";
				}

			}
		}

		return urpay;
	}
	public int getOrdersCount(String tid)
	{
		int orderTotal = 0;
		//TODO 根据订单号，把该订单的用户在3天以内的订单数量进行统计；
		List<OrderVO> orderVOs = getOrderVO();

		OrderVO orderVO = ListUtil.getObjectFromList(orderVOs,"tid", tid);

		if (orderVO != null) {
			if (orderVO.getUrpayStatus0() != null) {
				orderTotal = orderVO.getUrpayStatus0().getTradeCount();
			}
		}

		return orderTotal;
	}

	public int getOrdersUnpay(String tid)
	{
		int orderTotal = 0;
		//TODO 根据订单号，把该订单的用户在3天以内的订单数量进行统计；
		List<OrderVO> orderVOs = getOrderVO();
		OrderVO orderVO = ListUtil.getObjectFromList(orderVOs,"tid", tid);
		if (orderVO != null && orderVO.getUrpayStatus0() != null) {
			orderTotal = orderVO.getUrpayStatus0().getNoPayedCount();
		}

		return orderTotal;
	}

	public int getOrdersPaid(String tid)
	{
		int orderTotal = 0;
		//TODO 根据订单号，把该订单的用户在3天以内的订单数量进行统计；
		List<OrderVO> orderVOs = getOrderVO();
		OrderVO orderVO = ListUtil.getObjectFromList(orderVOs,"tid", tid);
		if (orderVO != null && orderVO.getUrpayStatus0() != null) {
			orderTotal = orderVO.getUrpayStatus0().getPayedCount();
		}

		return orderTotal;
	}
	public int getOrdersClosed(String tid)
	{
		int orderTotal = 0;
		//TODO 根据订单号，把该订单的用户在3天以内的订单数量进行统计；
		List<OrderVO> orderVOs = getOrderVO();
		OrderVO orderVO = ListUtil.getObjectFromList(orderVOs,"tid", tid);
		if (orderVO != null && orderVO.getUrpayStatus0() != null) {
			orderTotal = orderVO.getUrpayStatus0().getCloseCount();
		}

		return orderTotal;
	}
	public String getOrdersStatus(String tid)
	{
		String msg = "";
		//TODO 根据订单号，把该用户的所有订单，在3天内是否进行过催付统计出来；
		List<OrderVO> orderVOs = getOrderVO();
		OrderVO orderVO = ListUtil.getObjectFromList(orderVOs,"tid", tid);
		if (orderVO != null && orderVO.getUrpayStatus0() != null) {
			msg = orderVO.getUrpayStatus0().getMsg();
		}

		return msg;
	}


}
