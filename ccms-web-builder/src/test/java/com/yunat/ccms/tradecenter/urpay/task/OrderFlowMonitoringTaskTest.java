package com.yunat.ccms.tradecenter.urpay.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomain;
import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomainPK;
import com.yunat.ccms.tradecenter.repository.OrderFlowMonitoringRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class OrderFlowMonitoringTaskTest {
	
	@Autowired
	private OrderFlowMonitoringRepository orderFlowMonitoringRepository;
	
	@Autowired
	private OrderFlowMonitoringTask orderFlowMonitoringTask;
	
	@Test
	public void orderFlowMonitoringSaveOrUpdate(){
		String dpId = "100571094";
		String orderStatus = "WAIT_SELLER_SEND_GOODS";
		
		OrderFlowMonitoringDomain orderFlowMonitoringDomain = new OrderFlowMonitoringDomain();
		OrderFlowMonitoringDomainPK orderFlowMonitoringDomainPK = new OrderFlowMonitoringDomainPK();
		orderFlowMonitoringDomainPK.setDpId(dpId);
		orderFlowMonitoringDomainPK.setOrderStatus(orderStatus);
		orderFlowMonitoringDomain.setOrderFlowMonitoringPK(orderFlowMonitoringDomainPK);
		orderFlowMonitoringDomain.setOrderCount(810);
		
		orderFlowMonitoringRepository.save(orderFlowMonitoringDomain);
		System.out.println(orderFlowMonitoringRepository.findOrderFlowMonitoring(dpId));
		System.out.println("保存或更新成功!");
	}
	
	@Test
	public void handleTest(){
		
		orderFlowMonitoringTask.handle(null);
		
		
	}

}
