package com.yunat.ccms.tradecenter.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.ConfigLogDomain;
import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.ConfigLogRepository;
import com.yunat.ccms.tradecenter.repository.DictRepository;
import com.yunat.ccms.tradecenter.service.BaseService;
import com.yunat.ccms.tradecenter.service.CareConfigService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.channel.util.DateUtil;

/**
 * 关怀配置实现
 *
 * @author teng.zeng date 2013-6-13 下午05:33:57
 */
@Service("careConfigService")
public class CareConfigServiceImpl extends BaseService implements CareConfigService {

	@Autowired
	private CareConfigRepository careConfigRepository;

	@Autowired
	DictRepository dictRepository;

	@Autowired
	ConfigLogRepository configLogRepository;

	@Override
	public CareConfigDomain getByCareTypeAndDpId(Integer careType, String dpId) {
		CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpId(careType, dpId);
		if (null == careConfigDomain) {
			careConfigDomain = new CareConfigDomain();
			careConfigDomain.setDpId(dpId);
			careConfigDomain.setCareType(careType);
			// 第一次赋初始值
			careConfigDomain.setMemberGrade("-1");
			careConfigDomain.setOrderMaxAcount(-1.0);
			careConfigDomain.setOrderMinAcount(-1.0);
		}
		setFieldLsit(careConfigDomain);
		return careConfigDomain;
	}

	@Override
	public CareConfigDomain saveCareConfigDomain(CareConfigDomain careConfigDomain) {

		if(null == careConfigDomain.getPkid() || careConfigDomain.getPkid() == 0){
			CareConfigDomain careConfig = careConfigRepository.getByCareTypeAndDpId(careConfigDomain.getCareType(), careConfigDomain.getDpId());
			if(null != careConfig && null != careConfig.getPkid()){
				careConfigDomain.setPkid(careConfig.getPkid());
			}
		}

		// 设置数据创建时间和数据更新时间
		if (null == careConfigDomain.getCreated()) {
			careConfigDomain.setCreated(new Date());
		}
		careConfigDomain.setUpdated(new Date());

		// 判断订单范围是否是定义（1：自定义，0非自定义），反推算开始结束时间
		if (null == careConfigDomain.getDateType()) {
			careConfigDomain.setDateType(0);
		}

		// 处理非自定义反推算，由持续N天转换成具体日期
		if (careConfigDomain.getDateType() == 0) {
			int datenumber = careConfigDomain.getDateNumber();
			String dateStr = DateUtil.getDate("yyyy-MM-dd");
			careConfigDomain.setStartDate(DateUtils.getDate(dateStr));
			if (datenumber > 0) {
				Date DateCur = DateUtils.dateEnd(DateUtils.getDate(dateStr));
				if (datenumber == 1) {
					careConfigDomain.setEndDate(DateCur);
				} else {
					careConfigDomain.setEndDate(DateUtils.addDay(DateCur, datenumber - 1));
				}
			}
		}

		// 处理用户
		careConfigDomain.setUserName(getTenantId());
		careConfigDomain.setPlatName(PlatEnum.taobao.toString());
		careConfigDomain.setOpUser(getLoginName());

		// 后台状态控制赋初始值 1:开
		if (null == careConfigDomain.getIsSwitch()) {
			careConfigDomain.setIsSwitch(1);
		}

		// 是否包含聚划算设置默认值 0:不包含，1包含
		if (null == careConfigDomain.getIncludeCheap()) {
			// 默认不限
			careConfigDomain.setIncludeCheap(-1);
		}

		CareConfigDomain newCareConfigDomain = careConfigRepository.saveAndFlush(careConfigDomain);

		// 如果保存成功记录操作日志
		if (null != newCareConfigDomain) {
			saveConfigLog(newCareConfigDomain);
		}
		setFieldLsit(newCareConfigDomain);
		return newCareConfigDomain;
	}

	/**
	 * 设置实体中集合数据
	 */
	private void setFieldLsit(CareConfigDomain careConfigDomain) {

		Integer fileType = ConstantTC.CARE_FILTE_TYPE;
		// 确认收货关怀增加过滤条件
		if(careConfigDomain.getCareType() == 12){
			fileType = ConstantTC.CARE_CONFIRM_TYPE;
		}
		// 设置界面加载选项条件
		List<DictDomain> filterList = dictRepository.getByType(fileType);
		for (DictDomain dictDomain : filterList) {
			if(dictDomain.getRemark().contains("{carename}")){
				String newRemark = dictDomain.getRemark().replace("{carename}", UserInteractionType.getMessage(careConfigDomain.getCareType()));
				dictDomain.setRemark(newRemark);
			}
		}
		careConfigDomain.setFilterConditionList(filterList);
		List<DictDomain> memberList = dictRepository.getByType(ConstantTC.MEMBER_TYPE);
		careConfigDomain.setMemberGradeList(memberList);
		// 设置通道信息
//		List<GatewayInfoResponse> gatewayList = gatewayService.retrievalAllGateways(getTenantId(), getTenantPassword(),
//				ChannelType.SMS, PlatEnum.taobao);
//		careConfigDomain.setGatewayList(gatewayList);
	}

	private void saveConfigLog(CareConfigDomain careConfigDomain) {
		ConfigLogDomain configLogDomain = new ConfigLogDomain();
		configLogDomain.setCreated(new Date());
		configLogDomain.setDpId(careConfigDomain.getDpId());
		configLogDomain.setOpUser(careConfigDomain.getOpUser());
		configLogDomain.setType(careConfigDomain.getCareType());
		configLogDomain.setContent(JackSonMapper.toCJsonString(careConfigDomain));
		configLogRepository.saveAndFlush(configLogDomain);
	}

    @Override
    public List<CareConfigDomain> getByCareType(Integer careType) {
        return careConfigRepository.getCareConfigByType(careType);
    }

}
