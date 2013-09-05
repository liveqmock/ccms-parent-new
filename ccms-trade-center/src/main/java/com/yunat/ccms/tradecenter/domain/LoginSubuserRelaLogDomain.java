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
@Table(name = "tb_tc_login_subuser_relation_log")
public class LoginSubuserRelaLogDomain extends BaseDomain {


	/**
	 *
	 */
	private static final long serialVersionUID = 4115578444404153541L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="login_name")
	private String loginName;

	@Column(name="dp_id")
	private String dpId;

	@Column(name="last_taobao_subuser")
	private String lastTaobaoSubuser;

	@Column(name="next_taobao_subuser")
	private String nextTaobaoSubuser;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	private Date created;

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

	public String getLastTaobaoSubuser() {
		return lastTaobaoSubuser;
	}

	public void setLastTaobaoSubuser(String lastTaobaoSubuser) {
		this.lastTaobaoSubuser = lastTaobaoSubuser;
	}

	public String getNextTaobaoSubuser() {
		return nextTaobaoSubuser;
	}

	public void setNextTaobaoSubuser(String nextTaobaoSubuser) {
		this.nextTaobaoSubuser = nextTaobaoSubuser;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


}
