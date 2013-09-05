package com.yunat.ccms.dashboard.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.dashboard.repository.CampaignRepository;

@Service
public class CampaignService {

	@Autowired
	private CampaignRepository campaignRepository;

	private static final Object[] errorSubStatusCode = new Object[] { 12, 13, 22, 23 };

	private Date beforeDate;

	private Date afterDate;

	@Transactional(readOnly = true)
	public List<Map<String, Object>> nodeInfoByComposite(String[] campStatusArray, String[] subjobStatusArray) {
		return campaignRepository.nodeInfoByComposite(campStatusArray, subjobStatusArray);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> campInfoByStatus(String campaignStatus) {
		return campaignRepository.queryCampaignByStatus(campaignStatus);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> campInfoByErrorStatus() {
		initDateRange();
		return campaignRepository.queryCampaignByErrorStatus(StringUtils.join(errorSubStatusCode, ","), beforeDate,
				afterDate);
	}

	private void initDateRange() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);

		afterDate = today.getTime();

		today.add(Calendar.DATE, -3);
		today.set(Calendar.HOUR_OF_DAY, 00);
		today.set(Calendar.MINUTE, 00);
		today.set(Calendar.SECOND, 00);

		beforeDate = today.getTime();
	}

}
