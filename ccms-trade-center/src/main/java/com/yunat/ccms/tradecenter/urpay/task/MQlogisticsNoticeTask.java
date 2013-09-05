/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.quartz.JobExecutionContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.repository.MQlogisticsRepository;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-13 下午06:12:58
 */
public class MQlogisticsNoticeTask extends BaseJob{

	@Autowired
	private MQlogisticsRepository mqlogisticsRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("==开始执行发送获取物流数据的MQ消息任务==");
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateStr = format.format(new Date());
		Map<String,List<Long>> tidListMap = new HashMap<String,List<Long>>();
		List<Map<String, Object>> list = mqlogisticsRepository.queryOrderMQ();
		logger.info("获取发货的订单数据总共【"+list.size()+"】条数据");
		for(Map<String, Object> map : list){
			if(map.get("tid")!=null&&map.get("dpId")!=null){
				String key = map.get("dpId").toString();
				if(tidListMap.get(key)!=null){
					tidListMap.get(key).add(new Long(map.get("tid").toString()));
				}else{
					List<Long> tidList = new ArrayList<Long>();
					tidList.add(new Long(map.get("tid").toString()));
					tidListMap.put(key, tidList);
				}
			}
		}

		Set<String> shopIdList = tidListMap.keySet();
		logger.info("获取发货的订单数据分组后，总共【"+shopIdList.size()+"】店铺");
		for(String shop_id:shopIdList){
			List<Long> tidList = tidListMap.get(shop_id);
			logger.info("获取发货的订单数据，店铺【"+shop_id+"】，共【"+tidList.size()+"】个订单");
			if(tidList!=null&&tidList.size()>0){
				 int updatedNum = 1000;
	             int num = tidList.size() / updatedNum + 1;
	             for(int j=0;j<num;j++){
	            	 List<Long> listT = tidList.subList(j * updatedNum, Math.min(j * updatedNum + updatedNum, tidList.size()));
	            	 if(listT!=null&&listT.size()>0){
	            		 logger.info("发送获取物流数据的MQ消息，shop_id:" + shop_id+",数量：【"+listT.size() +"】,第【"+(j+1)+"】个消息");
	            		 JSONObject json = new JSONObject();
	            		 Map<String,Object> map = new HashMap<String, Object>();
	            		 map.put("shop_id", shop_id);
	            		 map.put("task_id", "logistics_"+dateStr+"_"+shop_id+"_"+(j+1));
	            		 map.put("tids", listT);
	            		 json.put("message", map);
	            		 rabbitTemplate.convertAndSend(json.toString());
	            	 }
	             }
			}

		}

	}

}
