package com.yunat.ccms.tradecenter.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.service.GatewayService;
import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.controller.vo.WarnConfigVO;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;
import com.yunat.ccms.tradecenter.domain.WarnStatusDomain;
import com.yunat.ccms.tradecenter.repository.WarnConfigRepository;
import com.yunat.ccms.tradecenter.repository.WarnStatusRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.WarnService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

@Service
public class WarnServiceImpl extends BaseService implements WarnService {

	@Autowired
	AppPropertiesRepository appPropertiesRepository;

	@Autowired
	protected SendLogService sendLogService;

	@Autowired
	protected GatewayService gatewayService;

	@Autowired
	WarnConfigRepository warnConfigRepository;

	@Autowired
	WarnStatusRepository warnStatusRepository;

	// 后台开启任务标志
	private static final Integer taskStart = 1;

	@Override
	public void saveOrUpdateWarnConfig(final WarnConfigVO warnConfigVO) throws Exception {

		WarnConfigDomain queryWarnConfigDomain = warnConfigRepository.findByDpIdAndWarnType(warnConfigVO.getDpId(),
				warnConfigVO.getWarnType());

		// 更新状态
		if (queryWarnConfigDomain != null) {

			// 如果是告警配置是打开的
			if (queryWarnConfigDomain.getIsOpen() == 1) {

				queryWarnConfigDomain.setIsOpen(warnConfigVO.getIsOpen());
				queryWarnConfigDomain.setUpdated(new Date());

			} else {
				// 如果告警配置是关闭的

				queryWarnConfigDomain.setUpdated(new Date());
				queryWarnConfigDomain.setWarnStartTime(new SimpleDateFormat("HH:mm").parse(warnConfigVO
						.getWarnStartTime()));
				queryWarnConfigDomain
						.setWarnEndTime(new SimpleDateFormat("HH:mm").parse(warnConfigVO.getWarnEndTime()));
				queryWarnConfigDomain.setWarnMobiles(warnConfigVO.getMobiles());
				queryWarnConfigDomain.setIsSwitch(taskStart);
				queryWarnConfigDomain.setIsOpen(warnConfigVO.getIsOpen());

			}

			warnConfigRepository.save(queryWarnConfigDomain);

		}

		// 第一次配置，插入配置记录
		else {
			WarnConfigDomain warnConfigDomain = new WarnConfigDomain();
			warnConfigDomain.setDpId(warnConfigVO.getDpId());
			warnConfigDomain.setCreated(new Date());
			warnConfigDomain.setUpdated(new Date());
			warnConfigDomain.setWarnStartTime(new SimpleDateFormat("HH:mm").parse(warnConfigVO.getWarnStartTime()));
			warnConfigDomain.setWarnEndTime(new SimpleDateFormat("HH:mm").parse(warnConfigVO.getWarnEndTime()));
			warnConfigDomain.setContent(warnConfigVO.getContent());
			warnConfigDomain.setWarnMobiles(warnConfigVO.getMobiles());
			warnConfigDomain.setIsSwitch(taskStart);
			warnConfigDomain.setIsOpen(warnConfigVO.getIsOpen());
			warnConfigDomain.setWarnType(warnConfigVO.getWarnType());
			warnConfigRepository.save(warnConfigDomain);
		}

	}

	@Override
	public WarnConfigDomain searchWarnConfig(String dpId, Integer warnType) throws Exception {

		return warnConfigRepository.findByDpIdAndWarnType(dpId, warnType);

	}

	@Override
	public List<WarnConfigDomain> getRefundWarnConfigListAtPresent(String warnType) {
		return warnConfigRepository.getRefundWarnConfigListAtPresent(warnType);
	}

	@Override
	public List<WarnConfigDomain> getNotGoodWarnConfigListAtPresent(String warnType) {
		return warnConfigRepository.getNotGoodConfigListAtPresent(warnType);
	}

	@Override
	public Date getLastDealNotGoodTime(String dpId) {
		PageRequest p = new PageRequest(1, 1);
		List<WarnStatusDomain> wsds = warnStatusRepository.getLastNotGoodWarn(dpId, p);
		return wsds.get(0) == null ? null : wsds.get(0).getNotGoodWarnTime();
	}

	@Override
	public BaseResponse<String> sendNotGoodWarnSms(String dpNick, List<Map<String, Object>> rds, Long gatewayId,
			String moblies) {

		String rtn = "#店铺昵称##生成时间#收到一个#评论类型#，昵称：#买家昵称# 详情：#评价详情#【数据赢家】";

		if (gatewayId == null) {
			String ccmsUser = appPropertiesRepository
					.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_group(),
							CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_name());
			String passWord = appPropertiesRepository.retrieveConfiguration(
					CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_group(),
					CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_name());
			List<GatewayInfoResponse> l = gatewayService.retrievalAllGateways(ccmsUser, passWord, ChannelType.SMS,
					PlatEnum.taobao);
			if (l == null || l.isEmpty()) {
				BaseResponse<String> br = new BaseResponse<String>();
				br.setSuccess(false);
				br.setErrDesc("渠道没有通道用来发送短信");
				return br;
			}
			gatewayId = l.get(0).getGatewayId();
		}
		List<SmsQueueDomain> smsQueueDomains = new ArrayList<SmsQueueDomain>();

		for (Map<String, Object> rd : rds) {

			String smsContent = rtn.replaceAll("#店铺昵称#", dpNick)
					.replaceAll("#生成时间#", DateUtils.getStringDate((Date) rd.get("created")))
					.replaceAll("#评论类型#", getResultType(rd.get("result").toString()))
					.replaceAll("#买家昵称#", rd.get("nick").toString()).replaceAll("#评价详情#", rd.get("content").toString());

			for (String mobile : moblies.split(",")) {
				if(mobile != null && mobile.trim().length() > 0){
					SmsQueueDomain smsDomain = new SmsQueueDomain();
					smsDomain.setTid(rd.get("tid").toString());
					smsDomain.setOid(rd.get("oid").toString());
					smsDomain.setBuyer_nick(rd.get("nick").toString());
					smsDomain.setCreated(new Date());
					smsDomain.setUpdated(new Date());
					smsDomain.setDpId(rd.get("dp_id").toString());
					smsDomain.setGatewayId(gatewayId);
					smsDomain.setMobile(mobile);
					smsDomain.setSms_content(smsContent);
					smsDomain.setType(UserInteractionType.NOT_GOOD_TRADERATE_WARN.getType());

					smsQueueDomains.add(smsDomain);
				}
			}
		}

		return sendLogService.sendSMS(smsQueueDomains, gatewayId, getTenantId(), getTenantPassword());
		// return new BaseResponse<String>();
	}

	private String getResultType(String result) {
		if ("neutral".equals(result)) {
			return "中评";
		} else {
			return "差评";
		}
	}

	@Override
	public void record(List<String> oids, String thread) {
		// TODO Auto-generated method stub
		for (String oid : oids) {
			WarnStatusDomain wsd = warnStatusRepository.getByOid(oid);
			if (wsd != null) {
				wsd.setNotGoodWarnStatus(1);
				wsd.setNotGoodWarnTime(DateUtils.getDateTime(thread));
				wsd.setUpdated(new Date());
				warnStatusRepository.saveAndFlush(wsd);
			} else {
				wsd = new WarnStatusDomain();
				wsd.setOid(oid);
				wsd.setCreated(DateUtils.getDateTime(thread));
				wsd.setNotGoodWarnStatus(1);
				wsd.setNotGoodWarnTime(DateUtils.getDateTime(thread));
				warnStatusRepository.saveAndFlush(wsd);
			}
		}
	}
}
