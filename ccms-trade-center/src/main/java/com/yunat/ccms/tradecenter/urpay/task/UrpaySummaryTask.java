/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.domain.UrpaySummaryDomain;
import com.yunat.ccms.tradecenter.repository.CountOrderRepository;
import com.yunat.ccms.tradecenter.service.UrpaySummaryService;



/**
 *催付统计任务
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-5-31 下午02:31:03
 */
public class UrpaySummaryTask extends BaseJob{


	@Autowired
	private CountOrderRepository countOrderRepository;

	@Autowired
	private UrpaySummaryService urpaySummaryService;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("--催付统计任务开始执行--");
		Map<String,Integer> smsNumMap = new HashMap<String, Integer>();
		logger.info("--催付统计任务，统计短信发送量开始--");
		List<Map<String, Object>> SendNumList = countOrderRepository.countAllUrpaySmsNum();
		for(Map<String, Object> map : SendNumList){
			if(map.get("type")!=null&&map.get("dpId")!=null&&map.get("urpayDate")!=null&&map.get("num")!=null){
				String key = map.get("type").toString()+"_"+map.get("dpId").toString()+"_"+map.get("urpayDate").toString();
				Integer sms_num = new Integer(map.get("num").toString());
				smsNumMap.put(key.trim(), sms_num);
			}
		}
		logger.info("--催付统计任务，统计短信发送量结束-统计到【"+SendNumList.size()+"】条-");
		for(int i=1;i<4;i++){
			List<Map<String, Object>> list = countOrderRepository.countUrpayOrderNum(i);
			logger.info("--催付统计任务，催付类型【"+i+"】，统计出数据【"+list.size()+"】条，开始更新数据库--");
			for(Map<String, Object> map : list){
				Integer urpayType = null;
				String dpId = null;
				String urpayDate = null;
				urpayType = i;
				if(map.get("dpId")!=null){
					dpId = map.get("dpId").toString();
				}
				if(map.get("urpayDate")!=null){
					urpayDate = map.get("urpayDate").toString();
				}
				UrpaySummaryDomain summary = urpaySummaryService.queryUrpaySummaryDomain(urpayType,dpId,urpayDate);
				if(summary==null){
					summary = new UrpaySummaryDomain();
					summary.setCreated(new Date());
				}
				summary.setUrpayType(urpayType);
				summary.setDpId(dpId);
				summary.setUrpayDate(urpayDate);
				if(map.get("orderNum")!=null){
					summary.setOrderNum(new Integer(map.get("orderNum").toString()));
				}
				if(map.get("responseNum")!=null){
					summary.setResponseNum(new Integer(map.get("responseNum").toString()));
				}
				if(map.get("responseAmount")!=null){
					summary.setResponseAmount(Double.valueOf(map.get("responseAmount").toString()));
				}
				String key = urpayType+"_"+dpId+"_"+urpayDate;
				Integer sms_num = smsNumMap.get(key.trim());
				if(sms_num==null){
					sms_num = 0;
				}
				summary.setSendNum(sms_num);
				summary.setUpdated(new Date());

				urpaySummaryService.saveUrpaySummaryDomain(summary);
			}
			logger.info("--催付统计任务，催付类型【"+i+"】，更新数据库完成--");
		}
		logger.info("--催付统计任务执行完毕--");
	}
}
