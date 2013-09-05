package com.yunat.ccms.biz.core.repository.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.biz.core.test.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.repository.CampaignRepository;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;

@Component
public class CampaignRepositoryExtendTest extends
		AbstractJunit4SpringContextBaseTests {
	@Autowired
	private CampaignRepository campaignRepository;
	
	@Test
	public void testFindByCampStateIn() {
		Collection<CampaignStatus> colls = new ArrayList<CampaignStatus>();
		CampaignStatus cs = new CampaignStatus();
		cs.setStatusId(CampaignState.TESTING_ON_DESIGN.getCode());
		colls.add(cs);
		campaignRepository.findByCampStateIn(colls);
	}
}
