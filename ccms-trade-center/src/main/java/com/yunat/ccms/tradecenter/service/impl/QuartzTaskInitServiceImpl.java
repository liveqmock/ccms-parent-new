/**
 *
 */
package com.yunat.ccms.tradecenter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.QuartzTaskInitDomain;
import com.yunat.ccms.tradecenter.repository.QuartzTaskInitRepository;
import com.yunat.ccms.tradecenter.service.QuartzTaskInitService;

/**
 *quartz任务是初始化接口实现类
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午04:50:28
 */
@Service("quartzTaskInitService")
public class QuartzTaskInitServiceImpl implements QuartzTaskInitService{

	@Autowired
	QuartzTaskInitRepository quartzTaskInitRepository;


	@Override
	public List<QuartzTaskInitDomain> queryQuartzTaskList() {
		return quartzTaskInitRepository.queryQuartzTaskList();
	}


	@Override
	public QuartzTaskInitDomain getQuartzTask(String job_name, String job_group) {
		return quartzTaskInitRepository.getQuartzTask(job_name, job_group);
	}

}
