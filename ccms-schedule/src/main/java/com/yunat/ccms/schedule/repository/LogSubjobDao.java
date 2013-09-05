package com.yunat.ccms.schedule.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.support.MybatisBaseDao;

@Component
@Scope("singleton")
public class LogSubjobDao extends MybatisBaseDao {

	public LogSubjob save(LogSubjob subjob) {
		return super.save(subjob);
	}

	public int update(LogSubjob subjob) {
		return super.update(subjob);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public LogSubjob get(Long subjobId) {
		return super.get(subjobId);
	}

	@Deprecated
	public int creatSubjob(Long jobId, Long nodeId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("nodeId", nodeId);
		return super.update(map);
	}

	/**
	 * 按jobId和nodeId查询唯一的subjob
	 * 
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public LogSubjob getSubjobByJobidAndNodeId(Long jobId, Long nodeId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("nodeId", nodeId);
		return super.get(map);
	}

	/**
	 * 查找一个job下面的所有subjob
	 * 
	 * @param jobId
	 * @return
	 */
	public List<LogSubjob> getSubjobByJobid(Long jobId) {
		return super.list(jobId);
	}

	/**
	 * 获取某节点的前置节点的subjob
	 * 
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public List<LogSubjob> getPreSubjobByJobidAndNodeId(Long jobId, Long nodeId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("nodeId", nodeId);
		return super.list(map);
	}

	/**
	 * 按jobid 和 nodeId列表查找相应的subjob
	 * 
	 * @param jobId
	 * @param nodeIds
	 * @return
	 */
	public List<LogSubjob> getSubjobByJobidAndNodeIds(Long jobId, List<Long> nodeIds) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("nodeIds", nodeIds);
		return super.list(map);
	}

	public void deleteSubjobByJobAndNodeIds(Long jobId, List<Long> nodeIds) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("nodeIds", nodeIds);
		super.delete(map);
	}

}
