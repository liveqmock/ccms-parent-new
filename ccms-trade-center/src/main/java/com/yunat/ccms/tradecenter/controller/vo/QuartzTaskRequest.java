/**
 *
 */
package com.yunat.ccms.tradecenter.controller.vo;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午05:57:31
 */
public class QuartzTaskRequest {

	private String jobName;
	private String jobGroup;
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}


}
