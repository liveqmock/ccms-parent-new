package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.taobao.api.request.TraderateAddRequest;
import com.taobao.api.response.TraderateAddResponse;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.support.repository.MobileBlackListRepository;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.TraderateAutoSetRequest;
import com.yunat.ccms.tradecenter.controller.vo.TraderateVO;
import com.yunat.ccms.tradecenter.domain.CaringDetailDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.TraderateAutoSetDomain;
import com.yunat.ccms.tradecenter.domain.TraderateDomainPK;
import com.yunat.ccms.tradecenter.repository.CaringDetailRepository;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.TraderateAutoSetRepository;
import com.yunat.ccms.tradecenter.repository.TraderateRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.OrderInteractionService;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.TaobaoService;
import com.yunat.ccms.tradecenter.service.TraderateCustomerOperate;
import com.yunat.ccms.tradecenter.service.TraderateService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

@Service
public class TraderateServiceImpl extends BaseService implements TraderateService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TraderateCustomerOperate traderateCustomerOperate;

	@Autowired
	private TraderateRepository traderateRepository;

	@Autowired
	private TraderateAutoSetRepository traderateAutoSetRepository;

	@Autowired
	private TaobaoService taobaoService;

	@Autowired
	private SendLogService sendLogService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	MobileBlackListRepository mobileBlackListRepository;

	@Autowired
	private CaringDetailRepository caringDetailRepository;

	@Autowired
	private OrderInteractionService ordIntService;

	@Override
	public Page<Map<String, Object>> findTraderateByCondition(TraderateVO traderateVO, Pageable page) {
		return traderateRepository.queryTraderateByCondition(traderateVO, page);
	}

	@Override
	public void traderateCustomerExplain(TraderateDomainPK traderateDomainPK, String reply, String shopId)
			throws Exception {
		traderateCustomerOperate.customerExplain(traderateDomainPK, reply, shopId);
	}

	@Override
	public void traderateBatchCustomerExplain(List<LinkedHashMap<String, Object>> values) throws Exception {
		if (values.size() > 1) {
			LinkedHashMap<String, Object> obj = values.get(values.size() - 1);
			String reply = (String) obj.get("reply");

			for (int i = 0; i < values.size() - 1; i++) {
				LinkedHashMap<String, Object> Map = values.get(i);
				String tid = (String) Map.get("tid");
				String oid = (String) Map.get("oid");
				String shopId = (String) Map.get("shopId");
				traderateCustomerOperate.customerExplain(new TraderateDomainPK(tid, oid), reply, shopId);
			}
		}
	}

	@Override
	public BaseResponse<String> traderateCustomerRegard(CaringRequest caringRequest) {

		BaseResponse<String> response = new BaseResponse<String>();
		List<SmsQueueDomain> sendQueueDomains = new ArrayList<SmsQueueDomain>();

		if(caringRequest.getCaringType().equals(UserInteractionType.MANUAL_TRADERATE_SMS_CARE.getType().toString())){
			logger.info("--------评价事务短信关怀开始--------");
			boolean flag = genSendQueue(caringRequest, sendQueueDomains, caringRequest.isFilterBlacklist());

			if (!flag) {
				logger.info("本次评价关怀条数： "+ sendQueueDomains.size()+ "(黑名单原因)， 直接结束!");
				return new BaseResponse<String>();
			}
			logger.info("本次评价关怀条数： "+ sendQueueDomains.size());
			response = sendLogService.sendSMS(sendQueueDomains, caringRequest.getGatewayId(), getTenantId(), getTenantPassword());

			if (response.isSuccess()) {
				List<CaringDetailDomain> cdds = CaringreqToDomain( sendQueueDomains);
				try {
					saveCaringReq(cdds);
				} catch (Exception e) {
					logger.error("评价短信关怀保存关怀信息异常： ", e);
				}
			}
			logger.info("--------评价事务短信关怀结束--------");
		}
		else{
			logger.info("--------评价事务其他关怀开始--------");
			boolean flag = genSendQueue(caringRequest, sendQueueDomains, false);
			if(flag){
				caringRequest.setDpId(sendQueueDomains.get(0).getDpId());
				ordIntService.refundOrdersCare(caringRequest, Integer.parseInt(caringRequest.getCaringType()));
				List<CaringDetailDomain> cdds = CaringreqToDomain( sendQueueDomains);
				try {
					saveCaringReq(cdds);
				} catch (Exception e) {
					logger.error("评价其他关怀保存关怀信息异常： ", e);
				}
			}
			logger.info("--------评价事务其他关怀结束--------");
		}

		return response;
	}

	@Override
	public BaseResponse<String> traderateBatchCustomerRegard(CaringRequest caringRequest) {
		return traderateCustomerRegard(caringRequest);
	}

	@Override
	public void traderateAutoSet(TraderateAutoSetRequest traderateAutoSetRequest) {
		TraderateAutoSetDomain traderateAutoSetDomain = new TraderateAutoSetDomain();
		traderateAutoSetDomain.setDp_id(traderateAutoSetRequest.getDpId());
		traderateAutoSetDomain.setType(traderateAutoSetRequest.getType());
		traderateAutoSetDomain.setContent(traderateAutoSetRequest.getContent());
		traderateAutoSetDomain.setStatus(traderateAutoSetRequest.getStatus());
		traderateAutoSetDomain.setLatest_time(DateUtils.getStringDate(new Date()));
		traderateAutoSetRepository.save(traderateAutoSetDomain);
	}

	@Override
	public TraderateAutoSetDomain getTraderateAutoSet(String dpId) {
		return traderateAutoSetRepository.getTraderateAutoSet(dpId);
	}

	@Override
	public TraderateAddResponse traderateAutoTaoBao(String shopId, TraderateAddRequest req) {
		TraderateAddResponse response = taobaoService.execTaobao(shopId, req);
		return response;
	}

	private List<CaringDetailDomain> CaringreqToDomain( List<SmsQueueDomain> sqds) {
		List<CaringDetailDomain> cdds = new ArrayList<CaringDetailDomain>();
		for(SmsQueueDomain sqd : sqds){
			CaringDetailDomain cdd = new CaringDetailDomain();
			cdd.setCaringperson(sqd.getSend_user());
			cdd.setCaringType(sqd.getType());
			cdd.setContent(sqd.getSms_content());
			cdd.setCreated(new Date());
			cdd.setUpdated(new Date());
			cdd.setCustomerno(sqd.getBuyer_nick());
			cdd.setDpId(sqd.getDpId());
			cdd.setGatewayId(sqd.getGatewayId());
			cdd.setOid(sqd.getOid());
			cdd.setTid(sqd.getTid());

			cdds.add(cdd);
		}
		return cdds;
	}

	/**
	 * 生成发送队列，若发送队列sendQueueDomains返回为空，则返回false
	 *
	 * @param caringRequest
	 * @param sendQueueDomains
	 */
	private boolean genSendQueue(CaringRequest caringRequest, List<SmsQueueDomain> sendQueueDomains , boolean isFilterBlacklist) {

		int flag = 0;
		//解析批量保存记录
		for (String tid : caringRequest.getTids()) {
			//获取订单信息
			OrderDomain orderDomain = orderRepository.findOne(tid);

			//是短信黑名单用户
			if (isFilterBlacklist && mobileBlackListRepository.findOne(orderDomain.getReceiverMobile()) != null) {
				continue;
			}

			SmsQueueDomain smsDomain = new SmsQueueDomain();
			smsDomain.setBuyer_nick(orderDomain.getCustomerno());
			//取对应的oid
			smsDomain.setOid(caringRequest.getOids()[flag]);
			smsDomain.setCreated(new Date());
			smsDomain.setUpdated(new Date());
			smsDomain.setDpId(orderDomain.getDpId());
			smsDomain.setGatewayId(caringRequest.getGatewayId());
			smsDomain.setMobile(orderDomain.getReceiverMobile());
			smsDomain.setSend_user(getLoginName());
			smsDomain.setSms_content(caringRequest.getContent());
			smsDomain.setTid(tid);
			smsDomain.setTrade_created(orderDomain.getCreated());
			smsDomain.setType(Integer.parseInt(caringRequest.getCaringType()));

			flag++;
			sendQueueDomains.add(smsDomain);
		}
		if(sendQueueDomains.isEmpty()){
			return false;
		}
		return true;

	}

	private synchronized void saveCaringReq(List<CaringDetailDomain> cdds) {
		for(CaringDetailDomain cdd : cdds){
			CaringDetailDomain cd = caringDetailRepository.getByTidAndOidAndCaringperson(cdd.getTid(), cdd.getOid(), cdd.getCaringperson());
			if(cd != null){
				cd.setCaringType(cdd.getCaringType());
				cd.setUpdated(new Date());
				cd.setContent(cdd.getContent());
				caringDetailRepository.save(cd);
			}else{
				caringDetailRepository.save(cdd);
			}
		}
	}
}
