package com.yunat.ccms.dashboard.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable  //embeddable: 可嵌入的
public class DashboardModuleConfigPK  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public DashboardModuleConfigPK() {
        super();
    }
    
    public DashboardModuleConfigPK(Long userId, Long moduleId) {
        super();
        this.userId = userId;
        this.moduleId = moduleId;
    }
	
	
	
	
    @Column(name = "user_id" ,length = 20)
    private Long userId;// 用户ID
	
    @Column(name = "module_id" ,length = 20)
    private Long moduleId;// 模块ID

    

	
    
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
    
    
    
	
	 @Override
	    public int hashCode() {
	        final int PRIME = 31;
	        int result = 1;
	        result = PRIME * result + ((userId == null) ? 0 : userId.hashCode());
	        result = PRIME * result
	                + ((moduleId == null) ? 0 : moduleId.hashCode());
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        final DashboardModuleConfigPK other = (DashboardModuleConfigPK) obj;
	        if (userId == null) {
	            if (other.userId != null)
	                return false;
	        } else if (!userId.equals(other.userId))
	            return false;
	        if (moduleId == null) {
	            if (other.moduleId != null)
	                return false;
	        } else if (!moduleId.equals(other.moduleId))
	            return false;
	        return true;
	    }
	
}
