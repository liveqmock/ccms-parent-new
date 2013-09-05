/**
 *
 */
package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *quartz任务初始化
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午04:28:09
 */
@Entity
@Table(name = "tb_tc_quartz_task_init")
public class QuartzTaskInitDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 * */
	private Long pkid;

	/**
	 * 任务名
	 * */
	private String job_name;

	/**
	 * 任务组名
	 * */
	private String job_group;

	/**
	 * 任务类全名
	 * */
	private String job_class_name;

	/**
	 * 任务调度的表达式
	 * */
	private String cron_expression;

	/**
	 * 是否有效；1：有效，0：无效
	 * */
	private Integer is_valid;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkid", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getJob_group() {
		return job_group;
	}

	public void setJob_group(String job_group) {
		this.job_group = job_group;
	}

	public String getJob_class_name() {
		return job_class_name;
	}

	public void setJob_class_name(String job_class_name) {
		this.job_class_name = job_class_name;
	}

	public String getCron_expression() {
		return cron_expression;
	}

	public void setCron_expression(String cron_expression) {
		this.cron_expression = cron_expression;
	}

	public Integer getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}

}
