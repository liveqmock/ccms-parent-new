package com.yunat.ccms.tradecenter.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;
import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.repository.ConfigLogRepository;
import com.yunat.ccms.tradecenter.repository.DictRepository;
import com.yunat.ccms.tradecenter.repository.UrpayConfigRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.UrpayConfigService;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;
import com.yunat.channel.util.DateUtil;

/**
 * 催付任务配置服务
 *
 * @author teng.zeng date 2013-5-31 下午02:19:30
 */
@Service("urpayConfigService")
@Scope("prototype")
public class UrpayConfigServiceImpl extends BaseService implements UrpayConfigService {

	@Autowired
	UrpayConfigRepository urpayConfigRepository;

	@Autowired
	AppPropertiesRepository appPropertiesRepository;

	@Autowired
	DictRepository dictRepository;

	@Autowired
	ConfigLogRepository configLogRepository;

	@Override
	public List<UrpayConfigDomain> getUrpayConfigListByType(int urpayType, int taskType) {
		if (urpayType == UrpayTypeEnum.AUTO_URPAY.getTypeValue()) {
			return urpayConfigRepository.getByUrpayTypeAndTaskType(urpayType, taskType);
		} else {
			return urpayConfigRepository.getByUrpayType(urpayType);
		}
	}

	@Override
	public UrpayConfigDomain getByUrpayTypeAndDpId(Integer urpayType, String dpId) {
		UrpayConfigDomain urpayConfigDomain = urpayConfigRepository.getByUrpayTypeAndDpId(urpayType, dpId);
		if (null == urpayConfigDomain) {
			urpayConfigDomain = new UrpayConfigDomain();
			urpayConfigDomain.setDpId(dpId);
			urpayConfigDomain.setUrpayType(urpayType);
			// 第一次赋初始值
			urpayConfigDomain.setMemberGrade("-1");
			urpayConfigDomain.setOrderMaxAcount(-1.0);
			urpayConfigDomain.setOrderMinAcount(-1.0);
		}
		setFieldLsit(urpayConfigDomain);
		return urpayConfigDomain;
	}

	@Override
	public UrpayConfigDomain saveUrpayConfigDomain(UrpayConfigDomain urpayConfigDomain) {

		if(null == urpayConfigDomain.getPkid() || urpayConfigDomain.getPkid() == 0){
			UrpayConfigDomain urpayConfig = urpayConfigRepository.getByUrpayTypeAndDpId(urpayConfigDomain.getUrpayType(), urpayConfigDomain.getDpId());
			if(null != urpayConfig && null != urpayConfig.getPkid()){
				urpayConfigDomain.setPkid(urpayConfig.getPkid());
			}
		}

		// 设置数据创建时间和数据更新时间
		if (null == urpayConfigDomain.getCreated()) {
			urpayConfigDomain.setCreated(new Date());
		}
		urpayConfigDomain.setUpdated(new Date());

		// 判断订单范围是否是定义（1：自定义，0非自定义），反推算开始结束时间
		if (null == urpayConfigDomain.getDateType()) {
			urpayConfigDomain.setDateType(0);
		}
		if (urpayConfigDomain.getDateType() == 0) {
			int datenumber = urpayConfigDomain.getDateNumber();
			String dateStr = DateUtil.getDate("yyyy-MM-dd");
			urpayConfigDomain.setStartDate(DateUtils.getDate(dateStr));
			if (datenumber > 0) {
				Date DateCur = DateUtils.dateEnd(DateUtils.getDate(dateStr));
				if (datenumber == 1) {
					urpayConfigDomain.setEndDate(DateCur);
				} else {
					urpayConfigDomain.setEndDate(DateUtils.addDay(DateCur, datenumber - 1));
				}
			}
		}

		// 处理用户
		urpayConfigDomain.setUserName(getTenantId());
		urpayConfigDomain.setPlatName(PlatEnum.taobao.toString());
		urpayConfigDomain.setOpUser(getLoginName());
		// 后台状态控制赋初始值 1:开
		if (null == urpayConfigDomain.getIsSwitch()) {
			urpayConfigDomain.setIsSwitch(1);
		}

		// 是否包含聚划算设置默认值 0:不包含，1包含
		if (null == urpayConfigDomain.getIncludeCheap()) {
			// 如果是聚划算催付则默认为包含聚划算
			if (urpayConfigDomain.getUrpayType() == 3) {
				urpayConfigDomain.setIncludeCheap(1);
			} else {
				urpayConfigDomain.setIncludeCheap(0);
			}
		}
		// 预关闭催付一定不包含聚划算
		if (urpayConfigDomain.getUrpayType() == 2) {
			urpayConfigDomain.setIncludeCheap(0);
		}

		UrpayConfigDomain newUrpayConfigDomain = urpayConfigRepository.saveAndFlush(urpayConfigDomain);

		// 如果保存成功记录操作日志
		if (null != newUrpayConfigDomain) {
			saveConfigLog(newUrpayConfigDomain);
		}
		setFieldLsit(newUrpayConfigDomain);
		return newUrpayConfigDomain;
	}

	/**
	 * 设置实体中集合数据
	 */
	private void setFieldLsit(UrpayConfigDomain urpayConfigDomain) {
		// 设置界面加载选项条件
		List<DictDomain> filterList = dictRepository.getByType(ConstantTC.FILTE_TYPE);
		urpayConfigDomain.setFilterConditionList(filterList);
		List<DictDomain> memberList = dictRepository.getByType(ConstantTC.MEMBER_TYPE);
		urpayConfigDomain.setMemberGradeList(memberList);

//		Long start=System.currentTimeMillis();
		// 设置通道信息
//		List<GatewayInfoResponse> gatewayList = gatewayService.retrievalAllGateways(getTenantId(), getTenantPassword(),
//				ChannelType.SMS, PlatEnum.taobao);
//		logger.info("===========>>调用渠道接口getAllGateWayInfo，调用所消耗时间：" + (System.currentTimeMillis()-start) );
//		urpayConfigDomain.setGatewayList(gatewayList);
	}

	private void saveConfigLog(UrpayConfigDomain urpayConfigDomain) {
		ConfigLogDomain configLogDomain = new ConfigLogDomain();
		configLogDomain.setCreated(new Date());
		configLogDomain.setDpId(urpayConfigDomain.getDpId());
		configLogDomain.setOpUser(urpayConfigDomain.getOpUser());
		configLogDomain.setType(urpayConfigDomain.getUrpayType());
		configLogDomain.setContent(JackSonMapper.toCJsonString(urpayConfigDomain));
		configLogRepository.saveAndFlush(configLogDomain);
	}

}
