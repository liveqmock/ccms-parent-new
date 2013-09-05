package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_tc_traderate_auto_set")
public class TraderateAutoSetDomain extends BaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4638658480222840322L;
	
	/**
	 * 店铺ＩＤ
	 */
	private String dp_id;
	
	/**
	 * 评价方式
	 */
	private String type;
	
	/**
	 * 评价内容
	 */
	private String content;
	
	/**
	 * 自动评价设置是否开启（0-已关闭  1-已开启）
	 */
	private Integer status;
	
	/**
	 * 最新一次订单自动回评时间
	 */
	private String latest_time;
	
	@Id
	@Column(name = "dp_id", unique = true, length = 50)
	public String getDp_id() {
		return dp_id;
	}

	public void setDp_id(String dp_id) {
		this.dp_id = dp_id;
	}

	@Column(name = "type", length = 50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "content", length = 500)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "latest_time")
	public String getLatest_time() {
		return latest_time;
	}

	public void setLatest_time(String latest_time) {
		this.latest_time = latest_time;
	}
	
	
	

}
