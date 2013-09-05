package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.request.TraderateExplainAddRequest;
import com.taobao.api.response.TraderateExplainAddResponse;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.support.repository.MobileBlackListRepository;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.domain.CaringDetailDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.TraderateDomain;
import com.yunat.ccms.tradecenter.domain.TraderateDomainPK;
import com.yunat.ccms.tradecenter.repository.CaringDetailRepository;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.TraderateCustomerOperateRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.TaobaoService;
import com.yunat.ccms.tradecenter.service.TraderateCustomerOperate;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

@Service
public class TraderateCustomerOperateImpl extends BaseService implements TraderateCustomerOperate {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private SendLogService sendLogService;

	@Autowired
	MobileBlackListRepository mobileBlackListRepository;

	@Autowired
	private CaringDetailRepository caringDetailRepository;

	@Autowired
	private TraderateCustomerOperateRepository traderateCustomerOperateRepository;

	@Autowired
	private TaobaoService taobaoService;

	@Override
	public void customerExplain(TraderateDomainPK traderateDomainPK, String reply, String shopId) throws Exception {

		TraderateExplainAddRequest req = new TraderateExplainAddRequest();
		req.setOid(Long.valueOf(traderateDomainPK.getOid()));
		req.setReply(reply);
		TraderateExplainAddResponse response = taobaoService.execTaobao(shopId, req);
		// 设置成功
		if (response.isSuccess()) {
			TraderateDomain tra = traderateCustomerOperateRepository.findOne(traderateDomainPK);
			tra.setReply(reply);
			traderateCustomerOperateRepository.save(tra);
		}
		// 设置失败
		else {
			throw new Exception(response.getMsg() + ":" + response.getSubMsg());
		}

	}

	@Override
	public BaseResponse<String> customerRegard(CaringRequest caringRequest) {

		BaseResponse<String> response = new BaseResponse<String>();
		if (caringRequest.getCaringType().equals(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType().toString())) {
			List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();

			boolean flag = genSmsSendQueue(caringRequest, sendQueueDomains);

			if (!flag) {
				return new BaseResponse<String>();
			}

			response = sendLogService.sendSMS(sendQueueDomains, caringRequest.getGatewayId(), getTenantId(),
					getTenantPassword());

			if (response.isSuccess()) {
				CaringDetailDomain cdd = CaringreqToDomain(caringRequest);

				saveCaringReq(caringRequest, cdd);
			}
		} else {
			CaringDetailDomain cdd = CaringreqToDomain(caringRequest);
			saveCaringReq(caringRequest, cdd);
		}

		return response;

	}

	private void saveCaringReq(CaringRequest caringRequest, CaringDetailDomain cdd) {
		CaringDetailDomain cd = caringDetailRepository.getByTidAndOidAndCaringperson(cdd.getTid(), cdd.getOid(),
				cdd.getCaringperson());
		if (cd != null) {
			cd.setUpdated(new Date());
			cd.setContent(caringRequest.getContent());
			caringDetailRepository.saveAndFlush(cd);
		} else {
			caringDetailRepository.saveAndFlush(cdd);
		}
	}

	private CaringDetailDomain CaringreqToDomain(CaringRequest caringRequest) {
		CaringDetailDomain cdd = new CaringDetailDomain();
		cdd.setCaringperson(caringRequest.getCaringperson());
		cdd.setCaringType(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType());
		cdd.setContent(caringRequest.getContent());
		cdd.setCreated(new Date());
		cdd.setCustomerno(caringRequest.getCustomerno());
		cdd.setDpId(caringRequest.getDpId());
		cdd.setGatewayId(caringRequest.getGatewayId());
		cdd.setOid(caringRequest.getOid());
		cdd.setTid(caringRequest.getTid());
		return cdd;
	}

	/**
	 * 由于参数要求过多，所以此方法用时，前端要传够足够的属性，此部分会将被过滤的关怀去掉
	 * 
	 * @param caringRequest
	 * @param oids
	 * @return
	 */
	private List<CaringDetailDomain> CaringreqToDomainList(CaringRequest caringRequest, List<String> oids) {
		List<CaringDetailDomain> l = new ArrayList<CaringDetailDomain>();
		for (String oid : caringRequest.getOids()) {
			if (!oids.contains(oid)) {
				continue;
			}
			CaringDetailDomain cdd = new CaringDetailDomain();
			cdd.setCaringperson(caringRequest.getCaringperson());
			cdd.setCaringType(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType());
			cdd.setContent(caringRequest.getContent());
			cdd.setCreated(new Date());
			cdd.setCustomerno(caringRequest.getCustomerno());
			cdd.setDpId(caringRequest.getDpId());
			cdd.setGatewayId(caringRequest.getGatewayId());
			cdd.setOid(caringRequest.getOid());
			cdd.setTid(caringRequest.getTid());
			l.add(cdd);
		}
		return l;
	}

	/**
	 * 生成短信发送队列，其中包括是否黑字典校验,true表示ok，false表示被过滤了
	 * 
	 * @param caringRequest
	 * @param sendQueueDomains
	 */
	private boolean genSmsSendQueue(CaringRequest caringRequest, List<SmsQueueDomain> sendQueueDomains) {
		// 获取订单信息
		OrderDomain orderDomain = orderRepository.findOne(caringRequest.getTid());

		// 是短信黑名单用户
		if (caringRequest.isFilterBlacklist()
				&& mobileBlackListRepository.findOne(orderDomain.getReceiverMobile()) != null) {
			return false;
		}

		caringRequest.setCustomerno(orderDomain.getCustomerno());

		SmsQueueDomain sendLogDomain = new SmsQueueDomain();
		sendLogDomain.setBuyer_nick(orderDomain.getCustomerno());
		sendLogDomain.setCreated(new Date());
		sendLogDomain.setUpdated(new Date());
		sendLogDomain.setDpId(orderDomain.getDpId());
		sendLogDomain.setGatewayId(caringRequest.getGatewayId());
		sendLogDomain.setMobile(orderDomain.getReceiverMobile());

		sendLogDomain.setSend_user(caringRequest.getCaringperson());
		sendLogDomain.setSms_content(caringRequest.getContent());

		sendLogDomain.setTid(orderDomain.getTid());
		sendLogDomain.setTrade_created(orderDomain.getCreated());
		sendLogDomain.setType(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType());

		sendQueueDomains.add(sendLogDomain);
		return true;
	}

	@Override
	public BaseResponse<String> batchCustomerRegard(CaringRequest caringRequest) {
		BaseResponse<String> response = new BaseResponse<String>();
		if (caringRequest.getCaringType().equals(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType().toString())) {
			List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();

			List<String> oids = new ArrayList<String>();
			for (String oid : caringRequest.getOids()) {
				boolean flag = genSmsSendQueue(caringRequest, sendQueueDomains);
				if (flag) {
					oids.add(oid);
				}
			}

			if (sendQueueDomains.isEmpty()) {
				return new BaseResponse<String>();
			}

			response = sendLogService.sendSMS(sendQueueDomains, caringRequest.getGatewayId(), getTenantId(),
					getTenantPassword());

			if (response.isSuccess()) {
				List<CaringDetailDomain> cdds = CaringreqToDomainList(caringRequest, oids);
				for (CaringDetailDomain cdd : cdds) {
					saveCaringReq(caringRequest, cdd);
				}
			}
		} else {
			CaringDetailDomain cdd = CaringreqToDomain(caringRequest);
			saveCaringReq(caringRequest, cdd);
		}

		return response;

	}

}