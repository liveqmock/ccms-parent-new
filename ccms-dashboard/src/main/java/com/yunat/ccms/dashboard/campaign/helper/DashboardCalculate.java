package com.yunat.ccms.dashboard.campaign.helper;

import java.util.List;
import java.util.Map;

/**
 * ©Copyright：yunat Project：CCMS Module ID： Comments： 首页获取活动信息和节点信息的接口 JDK
 * version used：<JDK1.6> Author：yinwei Create Date： 2012-12-21 Version：1.0
 * Modified By： Modified Date： Why & What is modified： Version：
 */
public interface DashboardCalculate {
	// 根据不同活动状态和节点的状态得到节点信息
	List<Map<String, Object>> nodeInfoByComposite(String[] campStatusArray, String[] subjobStatusArray);

	// 根据不同活动状态得到活动信息
	List<Map<String, Object>> campInfoByStatus(String type);
}
