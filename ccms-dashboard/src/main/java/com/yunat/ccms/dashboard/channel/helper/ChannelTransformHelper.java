package com.yunat.ccms.dashboard.channel.helper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.dashboard.service.DashboardChannelService;
import com.yunat.ccms.node.biz.wait.NodeWait;

@Component
public class ChannelTransformHelper {
	@Autowired
	DashboardChannelService channelService;
	

	public List<Map<String, Object>> channelSendInfo(String channelType){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = channelService.channelSendInfo(channelType);
		return commonChannel(resultList);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, Object>> commonChannel(
			List<Map<String, Object>> resultList) {
		List resultListTarget = new ArrayList();
		for (Map<String, Object> map : resultList) {
			Map targetMap = new HashMap();
			// 开始时间
			String plantime = map.get("plantime").toString();
			// 经过算法计算后的预计发送时间
			Date calculateTime = null;
			try {
				calculateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(plantime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 短信节点
			Long nodeId = Long.valueOf(map.get("nodeId").toString());
			Set<Long> preWaitNode = channelService.search(nodeId,
					"tflowwait");
			for (Long id : preWaitNode) {
				NodeWait nodeWait = channelService.getWaitNode(id);
				if (nodeWait.getIsDate() == 0) {

				} else if (nodeWait.getIsDate() == 1) {

				} else if (nodeWait.getIsDate() == 2) {
					int wait_hour = nodeWait.getWaithour();
					int wait_minute = nodeWait.getWaitminute();
					Calendar cal = Calendar.getInstance();
					cal.setTime(calculateTime);
					cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)
							+ wait_hour);
					cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)
							+ wait_minute);
					calculateTime = cal.getTime();
				}
			}
			targetMap.put("campId",
					map.get("campId") != null ? map.get("campId").toString()
							: "");
			targetMap.put("campName",
					map.get("campName") != null ? map.get("campName")
							.toString() : "");
			targetMap.put("nodeId",
					map.get("nodeId") != null ? map.get("nodeId").toString()
							: "");
			targetMap.put("sendTime", new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(calculateTime));
			targetMap.put("sendNum",
					map.get("sendNum") != null ? map.get("sendNum").toString()
							: "N/A");
			targetMap.put("sendStatus", map.get("sendStatus") != null ? map
					.get("sendStatus").toString() : "");
			resultListTarget.add(targetMap);
		}
		return resultListTarget;
	}

}
