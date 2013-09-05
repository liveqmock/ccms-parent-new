/**
 *
 */
package com.yunat.ccms.tradecenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-8 下午07:17:42
 */
public class VariableReplaceServiceTest extends AbstractJunit4SpringContextBaseTests{

	@Autowired
	private VariableReplaceService variableReplaceService;

	@Test
    public void replaceTest(){
		String sms_content = "你的姓名：#RECEIVER_NAME#";
		List<Object> list =  new ArrayList<Object>();
		OrderDomain order  = new OrderDomain();
		order.setConsignTime(new Date());
		order.setCustomerno("林肯郡");
		order.setCreated(new Date());
		order.setReceiverCity("上海");
		order.setReceiverName("姓名asdj");
		order.setPayment(12.12);
		order.setPayTime(new Date());
		list.add(order);
		String sms = variableReplaceService.replaceSmsContent(sms_content, list);
		System.out.println(sms);

    }

}
