package com.yunat.ccms.schedule.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.domain.LogJobData;
import com.yunat.ccms.schedule.support.MybatisBaseDao;

@Component
@Scope("singleton")
public class LogJobDataDao extends MybatisBaseDao{
	
	
	public LogJobData save(LogJobData jobdata) {
		return super.save(jobdata);
	}
	
	public List<LogJobData> getJobDataByJobAndTarget(Long jobId,Long target) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("target", target);
		return super.list(map);
	}
	
	/**
	 * 在插入数据前先清除原有数据
	 */
	public void clearJobData(Long jobId,Long source){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("source", source);
		super.delete(map);
	}
	

}
