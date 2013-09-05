package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.service.GatewayService;
import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.RefundDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.WarnStatusDomain;
import com.yunat.ccms.tradecenter.repository.RefundRepository;
import com.yunat.ccms.tradecenter.repository.WarnStatusRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.RefundWarnService;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

@Service
public class RefundWarnServiceImpl extends BaseService implements RefundWarnService {

	@Autowired
	AppPropertiesRepository appPropertiesRepository;

	@Autowired
	RefundRepository refundRepository;

	@Autowired
	WarnStatusRepository warnStatusRepository;

	@Autowired
	protected SendLogService sendLogService;

	@Autowired
	protected GatewayService gatewayService;

	@Override
	public List<RefundDomain> getRefundListByDpId(String dpId, Date lastDealTime, Date nowTime) {

		return refundRepository.getRefundWarnListByDpId(dpId, lastDealTime, nowTime);
	}

	@Override
	public BaseResponse<String> sendRefundWarnSms(String moblies, Long gatewayId, String dpId, String content, String dpNick) {
		if(gatewayId == null){
			String ccmsUser = appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_group(), CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_name());
		    String passWord = appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_group(), CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_name());
			List<GatewayInfoResponse> l = gatewayService.retrievalAllGateways(ccmsUser, passWord, ChannelType.SMS, PlatEnum.taobao);
			if(l == null || l.isEmpty()){
				BaseResponse<String> br = new BaseResponse<String>();
				br.setSuccess(false);
				br.setErrDesc("渠道没有通道用来发送短信");
				return br;
			}
			gatewayId = l.get(0).getGatewayId();
		}
		List<SmsQueueDomain> smsQueueDomains = new ArrayList<SmsQueueDomain>();
		for(String mobile : moblies.split(",")){
			SmsQueueDomain smsDomain = new SmsQueueDomain();
			smsDomain.setBuyer_nick(dpNick);
			smsDomain.setCreated(new Date());
			smsDomain.setUpdated(new Date());
			smsDomain.setDpId(dpId);
			smsDomain.setGatewayId(gatewayId);
			smsDomain.setMobile(mobile);
			smsDomain.setSms_content(content);
			smsDomain.setType(UserInteractionType.REFUND_WARN.getType());

			smsQueueDomains.add(smsDomain);
		}
		return sendLogService.sendSMS(smsQueueDomains, gatewayId, getTenantId(), getTenantPassword());
	}

	@Override
	public void record(List<String> oids, String thread) {
		for(String oid : oids){
			WarnStatusDomain wsd = warnStatusRepository.getByOid(oid);
			if(wsd != null){
				wsd.setRefundWarnStatus(1);
				wsd.setRefundWarnTime(DateUtils.getDateTime(thread));
				wsd.setUpdated(new Date());
				warnStatusRepository.saveAndFlush(wsd);
			}else{
				wsd = new WarnStatusDomain();
				wsd.setOid(oid);
				wsd.setCreated(DateUtils.getDateTime(thread));
				wsd.setRefundWarnStatus(1);
				wsd.setRefundWarnTime(DateUtils.getDateTime(thread));
				warnStatusRepository.saveAndFlush(wsd);
			}
		}
	}


	@Override
	public List<String> findNotGoodUnDealEarly(List<String> oids) {
		List<String> rtnoids = new ArrayList<String>();
		for(String oid : oids){
			WarnStatusDomain wsd = warnStatusRepository.getByOid(oid);
			if(wsd == null){
				rtnoids.add(oid);
			}
		}
		return rtnoids;
	}

}
