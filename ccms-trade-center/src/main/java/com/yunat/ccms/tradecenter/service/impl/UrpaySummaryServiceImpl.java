/**
 *
 */
package com.yunat.ccms.tradecenter.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.UrpaySummaryDomain;
import com.yunat.ccms.tradecenter.repository.UrpaySummaryRepository;
import com.yunat.ccms.tradecenter.service.UrpaySummaryService;

/**
 *催付统计接口实现类
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-3 上午10:30:35
 */
@Service("urpaySummaryService")
public class UrpaySummaryServiceImpl implements UrpaySummaryService{

	@Autowired
	UrpaySummaryRepository urpaySummaryRepository;


	@Override
	public List<UrpaySummaryDomain> queryUrpaySummaryList(Integer urpayType,String dpId) {
		Date date30 = DateUtils.addDay(new Date(), -29);
		String orderDate =DateUtils.getString(date30);
		return urpaySummaryRepository.queryUrpaySummaryList(urpayType, dpId ,orderDate);
	}

	@Override
	public void saveUrpaySummaryDomain(UrpaySummaryDomain summary) {
		urpaySummaryRepository.saveAndFlush(summary);
	}


	@Override
	public UrpaySummaryDomain queryUrpaySummaryDomain(Integer urpayType,String dpId,String urpayDate) {
		List<UrpaySummaryDomain> list =  urpaySummaryRepository.queryUrpaySummaryDomain(urpayType, dpId, urpayDate);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}



}
