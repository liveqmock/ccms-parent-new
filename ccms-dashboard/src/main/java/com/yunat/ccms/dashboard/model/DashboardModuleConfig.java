package com.yunat.ccms.dashboard.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 首页 用户和模块对应关系表
 * 
 * @author yinwei
 * @data  2013-02-25
 * 
 */


@Entity
@Table(name="dashboard_module_config")
public class DashboardModuleConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    public DashboardModuleConfig() {
        super();
    }

    public DashboardModuleConfig(Long userId,Long moduleId,  Boolean disabled) {
        this.id =new DashboardModuleConfigPK(userId,moduleId);
        this.disabled = disabled;
    }
	
	
	
	
	@EmbeddedId
    private DashboardModuleConfigPK id;
	
	@Column(name = "disabled")
	private Boolean disabled;

	
		
	public DashboardModuleConfigPK getId() {
		return id;
	}

	public void setId(DashboardModuleConfigPK id) {
		this.id = id;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
}
