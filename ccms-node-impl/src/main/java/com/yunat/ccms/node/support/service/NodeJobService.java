package com.yunat.ccms.node.support.service;

public interface NodeJobService {

	/**
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public Long getSubjobId(Long jobId, Long nodeId);

	/**
	 * @param jobId
	 * @return
	 */
	public Long getCampIdByJobId(Long jobId);

	/**
	 * @param subjobId
	 * @return
	 */
	public Long getCampIdBySubjobId(Long subjobId);

}
