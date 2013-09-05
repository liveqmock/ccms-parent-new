package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
* @Description: 评价事务-自动评价回复日志-Domain
* @author fanhong.meng
* @date 2013-7-15 下午3:36:27 
*
 */
@Entity
@Table(name = "tb_tc_traderate_auto_log")
public class TraderateAutoLogDomain extends BaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6446682784222932420L;
	
	
	private Long id;
	private String dpId;
	private String oid;
	private String tid;
	private String created;
	private String errorCode;
	private String errorMsg;
	private Boolean status;
	
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id 
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "dp_id", length = 50)
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	
	@Column(name = "oid", length = 50)
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@Column(name = "tid", length = 50)
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	@Column(name = "created")
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	
	@Column(name = "error_code", length = 150)
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	@Column(name = "error_msg", length = 150)
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	@Column(name = "status")
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	
	

}
