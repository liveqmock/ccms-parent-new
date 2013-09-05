package com.yunat.ccms.schedule.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.support.MybatisBaseDao;

@Component
@Scope("singleton")
public class LogJobDao extends MybatisBaseDao {

	public LogJob save(LogJob job) {
		return super.save(job);
	}

	public int update(LogJob job) {
		return super.update(job);
	}

	/*
	 * public void delete(Long id) {
	 * super.delete(id);
	 * }
	 */

	public LogJob get(Long jobId) {
		return super.get(jobId);
	}

	public List<LogJob> getJobsByCampId(Long campId) {
		return super.list(campId);
	}

	public List<LogJob> getJobsByCampIdAndIsTest(Long campId, boolean isTest) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("campId", campId);
		map.put("isTest", isTest);
		return super.list(map);
	}

	public List<LogJob> getJobsInStatesByCampId(Long campId, List<Long> status, boolean isTest) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("campId", campId);
		map.put("status", status);
		map.put("isTest", isTest);
		return super.list(map);
	}
}
