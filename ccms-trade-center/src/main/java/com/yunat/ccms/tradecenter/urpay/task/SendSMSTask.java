/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.SmsQueueService;

/**
 *短信发送任务
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-6 下午12:59:20
 */
public class SendSMSTask extends BaseJob{

	@Autowired
	private SmsQueueService smsQueueService;

	@Autowired
	private SendLogService sendLogService;

	@Autowired
	AppPropertiesRepository appPropertiesRepository;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("订单中心发送短信任务开始");

		// quartz每分钟执行都是整分执行，打乱整分执行的规则
		Long temp = Math.round(Math.random()*30000-1);
		try {
			logger.info("发送短信任务开始之前避免并发请求渠道，休眠【"+temp+"】毫秒，再执行！");
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			logger.error("发送短信任务休眠异常");
			e.printStackTrace();
		}

		String ccmsUser = appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_group(), CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_name());
	    String passWord = appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_group(), CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_name());
		Map<Long,List<SmsQueueDomain>> map = new HashMap<Long, List<SmsQueueDomain>>();

		String thread = DateUtils.getFullStringDate(new Date());

		//首先更新需要发送的短信数据
		smsQueueService.updateQueue(thread);

		//将数据查询出来,分组发送
		List<SmsQueueDomain> listT = smsQueueService.querySmsQueueByThread(thread);
		for(SmsQueueDomain sms : listT){
			if(map.get(sms.getGatewayId())==null){
				List<SmsQueueDomain> list = new ArrayList<SmsQueueDomain>();
				list.add(sms);
				map.put(sms.getGatewayId(), list);
			}else{
				map.get(sms.getGatewayId()).add(sms);
			}
		}
		Set<Long> set = map.keySet();
		for(Long gateway_id : set){
			List<SmsQueueDomain> list = map.get(gateway_id);
			try{
			    sendLogService.sendSMS(list,gateway_id,ccmsUser,passWord);
			}catch(Exception ex){
			    logger.error("短信发送异常",ex);
			}
		}
		//删除发送过的队列
		try{
		    smsQueueService.deleteSmsByThread(thread);
		}catch(Exception ex){
		    logger.error("批次:" + thread + " 队列数据删除失败",ex);
		}
		logger.info("订单中心发送短信任务结束");
	}

}
