package com.yunat.ccms.core.support.external;

import com.yunat.base.enums.app.PlatEnum;

public class ChannelParamBuilder {

	/**
	 * 渠道唯一标识一个店铺的字符串
	 * 
	 * @param plat
	 * @param shopId
	 * @return
	 */
	public static String shopKey(PlatEnum plat, String shopId) {
		return plat.toString().concat("_").concat(shopId);
	}

	/**
	 * 与渠道约定的TaskId
	 * 
	 * @param tenantId
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public static String taskId(String tenantId, Long jobId, Long nodeId) {
		return tenantId + "_" + jobId + "_" + nodeId;
	}
}
