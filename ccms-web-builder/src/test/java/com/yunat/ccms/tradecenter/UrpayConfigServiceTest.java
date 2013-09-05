package com.yunat.ccms.tradecenter;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.service.UrpayConfigService;


public class UrpayConfigServiceTest extends AbstractJunit4SpringContextBaseTests {

    @Autowired
    private UrpayConfigService urpayConfigService;

    @Test
    public void queryUrpayConfig(){
        List<UrpayConfigDomain> list = urpayConfigService.getUrpayConfigListByType(1,0);
        if(list != null){
            logger.info(String.valueOf(list.size()));
        }
    }

    @Test
    public void saveUrpayConfig(){
    	UrpayConfigDomain urpayConfigDomain = new UrpayConfigDomain();
    	urpayConfigDomain.setCreated(new Date());
    	urpayConfigDomain.setUpdated(new Date());
    	urpayConfigDomain.setUrpayType(1);
    	urpayConfigDomain.setDateType(0);
    	urpayConfigDomain.setDateNumber(1);
    	urpayConfigDomain.setTaskType(1);
    	urpayConfigDomain.setFilterCondition("1,2");
    	urpayConfigDomain.setOffset(1);
    	urpayConfigDomain.setNotifyOption(1);
    	urpayConfigDomain.setDpId("123");
    	urpayConfigDomain.setPkid(3L);
    	UrpayConfigDomain doomain = urpayConfigService.saveUrpayConfigDomain(urpayConfigDomain);
    	if(null != doomain){
    		logger.info("pkid"+doomain.getPkid());
    	}
    }

    @Test
    public void findUrpayConfig(){
    	UrpayConfigDomain urpayConfigDomain = urpayConfigService.getByUrpayTypeAndDpId(1, "123");
    	if(null != urpayConfigDomain){
    		logger.info(urpayConfigDomain.getFilterCondition());
    		logger.info(urpayConfigDomain.getUserName());
    		List<DictDomain> memberList = urpayConfigDomain.getMemberGradeList();
    		for (DictDomain dictDomain : memberList) {
    			logger.info(dictDomain.getName());
			}
    		List<DictDomain> filterList = urpayConfigDomain.getFilterConditionList();
    		for (DictDomain dictDomain : filterList) {
    			logger.info(dictDomain.getName());
			}
    	}
    }
}
