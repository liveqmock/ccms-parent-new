package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.support.repository.MobileBlackListRepository;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.RefundRepository;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.repository.UrpayStatusRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.CustomerOrdersService;
import com.yunat.ccms.tradecenter.service.OrderInteractionService;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

@Component("orderInteractionServiceImpl")
public class OrderInteractionServiceImpl extends BaseService implements OrderInteractionService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private SendLogRepository sendLogRepository;

	@Autowired
	private SendLogService sendLogService;

	@Autowired
	private UrpayStatusRepository urpayStatusRepository;

	@Autowired
	MobileBlackListRepository mobileBlackListRepository;

	@Autowired
	private CustomerOrdersService customerOrdersService;

	@Autowired
	private RefundRepository refundRepository;

	@SuppressWarnings("unchecked")
	@Transactional
	public void refundOrdersCare(CaringRequest req, Integer type) {
		List<SendLogDomain> sendLogDomains = new ArrayList<SendLogDomain>();
		Map<String, ?>[] array = new Map[req.getTids().length];
		//解析批量保存记录
		for (int i = 0; i < req.getTids().length; i++) {
			String tid = req.getTids()[i], oid = req.getOids()[i];
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			SendLogDomain sendLogDomain = new SendLogDomain();
			sendLogDomain.setBuyerNick(orderDomain.getCustomerno());
			sendLogDomain.setCreated(new Date());
			sendLogDomain.setUpdated(new Date());
			sendLogDomain.setDpId(orderDomain.getDpId());
			sendLogDomain.setMobile(orderDomain.getReceiverMobile());
			sendLogDomain.setSendUser(getLoginName());
			sendLogDomain.setSmsContent(req.getContent());
			sendLogDomain.setTid(tid);
			sendLogDomain.setOid(oid);// 子订单号
			sendLogDomain.setSendStatus(1);
			sendLogDomain.setTradeCreated(orderDomain.getCreated());
			sendLogDomain.setType(type);
			sendLogDomains.add(sendLogDomain);
			// 关怀状态
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("tid", tid);
			map.put("oid", oid);
			map.put("dpId", req.getDpId());
			map.put("care", true);
			array[i] = map;
		}
		// 批量保存记录
		sendLogRepository.save(sendLogDomains);
		// 批量更新关怀状态
		refundRepository.refundOrdersCare(array);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public BaseResponse<String> refundOrdersSMSCare(CaringRequest req, Integer type) {
		Map<String, ?>[] array = new Map[req.getTids().length];
		BaseResponse<String> response = new BaseResponse<String>();
		List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();
		//解析批量保存记录
		for (int i = 0; i < req.getTids().length; i++) {
			String tid = req.getTids()[i], oid = req.getOids()[i];
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			//是短信黑名单用户
			if (req.isFilterBlacklist() && mobileBlackListRepository.findOne(orderDomain.getReceiverMobile()) != null) {
				continue;
			}

			SmsQueueDomain smsDomain = new SmsQueueDomain();
			smsDomain.setBuyer_nick(orderDomain.getCustomerno());
			smsDomain.setCreated(new Date());
			smsDomain.setUpdated(new Date());
			smsDomain.setDpId(orderDomain.getDpId());
			smsDomain.setGatewayId(req.getGatewayId());
			smsDomain.setMobile(orderDomain.getReceiverMobile());
			smsDomain.setSend_user(getLoginName());
			smsDomain.setSms_content(req.getContent());
			smsDomain.setTid(tid);
			smsDomain.setOid(oid);// 子订单号
			smsDomain.setTrade_created(orderDomain.getCreated());
			smsDomain.setType(type);
			sendQueueDomains.add(smsDomain);

			// 关怀状态
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("tid", tid);
			map.put("oid", oid);
			map.put("dpId", req.getDpId());
			map.put("care", true);
			array[i] = map;
		}

		if (CollectionUtils.isNotEmpty(sendQueueDomains)){
			// 发送短信并批量保存记录
			response = sendLogService.sendSMS(sendQueueDomains, req.getGatewayId(), getTenantId(), getTenantPassword());
			// 批量更新关怀状态
			if (response.isSuccess()) {
				refundRepository.refundOrdersCare(array);
			}
		}
		return response;
	}

	@Override
	@Transactional
	public BaseResponse<String> urpayOrder(String tid, String smsContent, Long gatewayId, Boolean filterBlacklist) {

		BaseResponse<String> response = new BaseResponse<String>();

		//获取订单信息
		OrderDomain orderDomain = orderRepository.findOne(tid);

		//是短信黑名单用户
		if (filterBlacklist && mobileBlackListRepository.findOne(orderDomain.getReceiverMobile()) != null) {
			return response;
		}

		//如果订单状态是下单未付款
		if (OrderStatus.WAIT_BUYER_PAY.getStatus().equals(orderDomain.getStatus())) {

			//调用发送接口
			SmsQueueDomain sendLogDomain = new SmsQueueDomain();
			sendLogDomain.setBuyer_nick(orderDomain.getCustomerno());
			sendLogDomain.setCreated(new Date());
			sendLogDomain.setUpdated(new Date());
			sendLogDomain.setDpId(orderDomain.getDpId());
			sendLogDomain.setGatewayId(gatewayId);
			sendLogDomain.setMobile(orderDomain.getReceiverMobile());

			sendLogDomain.setSend_user(getLoginName());
			sendLogDomain.setSms_content(smsContent);

			sendLogDomain.setTid(tid);
			sendLogDomain.setTrade_created(orderDomain.getCreated());
			sendLogDomain.setType(UserInteractionType.MANUAL_URPAY.getType());

			List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();
			sendQueueDomains.add(sendLogDomain);

			response = sendLogService.sendSMS(sendQueueDomains, gatewayId, getTenantId(), getTenantPassword());

			if (response.isSuccess()) {
				//更新催付状态表
				urpayStatusRepository.saveOrUpdateManualStatus(1, tid);
			}

		}

		return response;
	}

	@Override
	@Transactional
	public BaseResponse<String> urpayOrders(String[] tids, String smsContent, Long gatewayId, Boolean filterBlacklist) {
		BaseResponse<String> response = new BaseResponse<String>();

		List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();

		//解析批量保存记录
		for (String tid : tids) {
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			//是短信黑名单用户
			if (filterBlacklist && mobileBlackListRepository.findOne(orderDomain.getReceiverMobile()) != null) {
				continue;
			}

			//如果订单状态是下单未付款
			if (OrderStatus.WAIT_BUYER_PAY.getStatus().equals(orderDomain.getStatus())) {

				SmsQueueDomain sendLogDomain = new SmsQueueDomain();
				sendLogDomain.setBuyer_nick(orderDomain.getCustomerno());
				sendLogDomain.setCreated(new Date());
				sendLogDomain.setUpdated(new Date());
				sendLogDomain.setDpId(orderDomain.getDpId());
				sendLogDomain.setGatewayId(gatewayId);
				sendLogDomain.setMobile(orderDomain.getReceiverMobile());
				sendLogDomain.setSend_user(getLoginName());
				sendLogDomain.setSms_content(smsContent);

				sendLogDomain.setTid(tid);
				sendLogDomain.setTrade_created(orderDomain.getCreated());
				sendLogDomain.setType(UserInteractionType.MANUAL_URPAY.getType());

				sendQueueDomains.add(sendLogDomain);
			}
		}
		if (CollectionUtils.isNotEmpty(sendQueueDomains)){
			//批量保存记录
			response = sendLogService.sendSMS(sendQueueDomains, gatewayId, getTenantId(), getTenantPassword());
			//更新催付状态表
			if (response.isSuccess()) {
				for (SmsQueueDomain sendLogDomain : sendQueueDomains) {
					urpayStatusRepository.saveOrUpdateManualStatus(1, sendLogDomain.getTid());
				}
			}
		}
		return response;
	}

	@Override
	public void wwUrpayOrder(String tid, String note) {
		//获取订单信息
		OrderDomain orderDomain = orderRepository.findOne(tid);

		//保存记录
		SendLogDomain sendLogDomain = new SendLogDomain();
		sendLogDomain.setBuyerNick(orderDomain.getCustomerno());
		sendLogDomain.setCreated(new Date());
		sendLogDomain.setUpdated(new Date());
		sendLogDomain.setDpId(orderDomain.getDpId());
		sendLogDomain.setMobile(orderDomain.getReceiverMobile());
		sendLogDomain.setSendUser(getLoginName());
		sendLogDomain.setTid(tid);
		sendLogDomain.setSmsContent(note);
		sendLogDomain.setTradeCreated(orderDomain.getCreated());
		sendLogDomain.setSendStatus(1);
		sendLogDomain.setType(UserInteractionType.MANUAL_WANGWANG_URPAY.getType());

		sendLogRepository.save(sendLogDomain);
		urpayStatusRepository.saveOrUpdateManualStatus(1, tid);
	}

	@Override
	public void wwUrpayOrders(String[] tids, String note) {
		List<SendLogDomain> sendLogDomains = new ArrayList<SendLogDomain>();

		//解析批量保存记录
		for (String tid : tids) {
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			SendLogDomain sendLogDomain = new SendLogDomain();
			sendLogDomain.setBuyerNick(orderDomain.getCustomerno());
			sendLogDomain.setCreated(new Date());
			sendLogDomain.setUpdated(new Date());
			sendLogDomain.setDpId(orderDomain.getDpId());
			sendLogDomain.setMobile(orderDomain.getReceiverMobile());
			sendLogDomain.setSendUser(getLoginName());
			sendLogDomain.setSmsContent(note);
			sendLogDomain.setTid(tid);
			sendLogDomain.setSendStatus(1);
			sendLogDomain.setTradeCreated(orderDomain.getCreated());
			sendLogDomain.setType(UserInteractionType.MANUAL_WANGWANG_URPAY.getType());

			sendLogDomains.add(sendLogDomain);
		}

		//批量保存记录
		sendLogRepository.save(sendLogDomains);
		for (String tid : tids) {
			urpayStatusRepository.saveOrUpdateManualStatus(1, tid);
		}
	}

	@Override
	@Transactional
	public BaseResponse<String> careOrders(String[] tids, String smsContent, Long gatewayId, Integer type, Boolean filterBlacklist) {
		List<String> tidsValidate = new ArrayList<String>();
		BaseResponse<String> response = new BaseResponse<String>();
		List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();
		//解析批量保存记录
		for (String tid : tids) {
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			//是短信黑名单用户
			if (filterBlacklist && mobileBlackListRepository.findOne(orderDomain.getReceiverMobile()) != null) {
				continue;
			}

			SmsQueueDomain smsDomain = new SmsQueueDomain();
			smsDomain.setBuyer_nick(orderDomain.getCustomerno());
			smsDomain.setCreated(new Date());
			smsDomain.setUpdated(new Date());
			smsDomain.setDpId(orderDomain.getDpId());
			smsDomain.setGatewayId(gatewayId);
			smsDomain.setMobile(orderDomain.getReceiverMobile());
			smsDomain.setSend_user(getLoginName());
			smsDomain.setSms_content(smsContent);
			smsDomain.setTid(tid);
			smsDomain.setTrade_created(orderDomain.getCreated());
			smsDomain.setType(type);

			tidsValidate.add(tid);
			sendQueueDomains.add(smsDomain);
		}
		if (CollectionUtils.isNotEmpty(sendQueueDomains)){
			// 发送短信并批量保存记录
			response = sendLogService.sendSMS(sendQueueDomains, gatewayId, getTenantId(), getTenantPassword());
			// 批量更新关怀状态
			if (response.isSuccess()) {
				customerOrdersService.updateCusOrdsShipHide(true, UserInteractionType.get(type).getShipColumn().getName(), tidsValidate.toArray(new String[0]));
			}
		}
		return response;
	}

	@Override
	@Transactional
	public void careOrders(String[] tids, String content, Integer type) {
		List<SendLogDomain> sendLogDomains = new ArrayList<SendLogDomain>();
		//解析批量保存记录
		for (String tid : tids) {
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			SendLogDomain sendLogDomain = new SendLogDomain();
			sendLogDomain.setBuyerNick(orderDomain.getCustomerno());
			sendLogDomain.setCreated(new Date());
			sendLogDomain.setUpdated(new Date());
			sendLogDomain.setDpId(orderDomain.getDpId());
			sendLogDomain.setMobile(orderDomain.getReceiverMobile());
			sendLogDomain.setSendUser(getLoginName());
			sendLogDomain.setSmsContent(content);
			sendLogDomain.setTid(tid);
			sendLogDomain.setSendStatus(1);
			sendLogDomain.setTradeCreated(orderDomain.getCreated());
			sendLogDomain.setType(type);

			sendLogDomains.add(sendLogDomain);
		}
		// 批量保存记录
		sendLogRepository.save(sendLogDomains);
		// 批量更新关怀状态
		customerOrdersService.updateCusOrdsShipHide(true, UserInteractionType.get(type).getShipColumn().getName(), tids);
	}

}
