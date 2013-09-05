package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserialize;
import com.yunat.ccms.core.support.json.JsonDateSerializer;

@Entity
@Table(name = "tb_tc_login_subuser_relation")
public class LoginSubuserRelaDomain extends BaseDomain {


	/**
	 *
	 */
	private static final long serialVersionUID = -8943080596981366294L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="login_name")
	private String loginName;

	@Column(name="dp_id")
	private String dpId;

	@Column(name="taobao_subuser")
	private String taobaoSubuser;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	private Date created;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	private Date updated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getTaobaoSubuser() {
		return taobaoSubuser;
	}

	public void setTaobaoSubuser(String taobaoSubuser) {
		this.taobaoSubuser = taobaoSubuser;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
