package com.yunat.ccms.tradecenter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.taobao.api.request.TraderateAddRequest;
import com.taobao.api.response.TraderateAddResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class TraderateServiceTest {
	
	@Autowired
	private TraderateService traderateService;
	
	@Test
	public void traderateAutoTaoBaoTest(){
		String shopId = "65927470";
		TraderateAddRequest req = new TraderateAddRequest();
		req.setTid(Long.parseLong("285371590550875"));
		req.setOid(Long.parseLong("285371590550875"));
		req.setResult("good"); // good(好评,默认),neutral(中评),bad(差评)
		req.setRole("seller"); // seller(卖家),buyer(买家)
		req.setContent("测试用评价！");
		req.setAnony(false); // 卖家评不能匿名
		TraderateAddResponse rspon = traderateService.traderateAutoTaoBao(shopId, req);
		System.out.println(rspon.isSuccess());
		
	}

}
