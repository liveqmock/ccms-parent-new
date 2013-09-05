package com.yunat.ccms.dashboard.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dashboard_module")
public class DashboardModule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1577697453060606781L;

	private Long dashboardModuleId;
	
	private String dashboardModuleName;
	
	private String dashboardModuleUrl;
	
	private String dashboardModuleCreatetime;
	
	//是否为默认加载模块
	private Boolean  dashboardDefaultModule;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dashboard_module_id",precision = 20, scale = 0)
	public Long getDashboardModuleId() {
		return dashboardModuleId;
	}

	public void setDashboardModuleId(Long dashboardModuleId) {
		this.dashboardModuleId = dashboardModuleId;
	}

	
	@Column(name = "dashboard_module_name")
	public String getDashboardModuleName() {
		return dashboardModuleName;
	}

	public void setDashboardModuleName(String dashboardModuleName) {
		this.dashboardModuleName = dashboardModuleName;
	}

	@Column(name = "dashboard_module_url")
	public String getDashboardModuleUrl() {
		return dashboardModuleUrl;
	}

	public void setDashboardModuleUrl(String dashboardModuleUrl) {
		this.dashboardModuleUrl = dashboardModuleUrl;
	}

	
	@Column(name = "dashboard_module_createtime")
	public String getDashboardModuleCreatetime() {
		return dashboardModuleCreatetime;
	}

	public void setDashboardModuleCreatetime(String dashboardModuleCreatetime) {
		this.dashboardModuleCreatetime = dashboardModuleCreatetime;
	}

	
	@Column(name = "dashboard_default_module")
	public Boolean getDashboardDefaultModule() {
		return dashboardDefaultModule;
	}

	public void setDashboardDefaultModule(Boolean dashboardDefaultModule) {
		this.dashboardDefaultModule = dashboardDefaultModule;
	}
	
}