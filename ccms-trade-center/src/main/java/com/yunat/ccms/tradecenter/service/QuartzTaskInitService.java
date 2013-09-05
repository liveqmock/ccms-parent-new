/**
 *
 */
package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.QuartzTaskInitDomain;

/**
 *quartz任务初始化接口类
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午04:49:50
 */
public interface QuartzTaskInitService {

	/**
     * 查询quartz任务初始化数据
     *
     * @param urpayType
     * @return
     */
	List<QuartzTaskInitDomain> queryQuartzTaskList();

	QuartzTaskInitDomain getQuartzTask(String job_name,String job_group);

}
