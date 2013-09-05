package com.yunat.ccms.auth.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.yunat.ccms.auth.role.Role;

@Entity
@Table(name = "tb_sysuser")
public class User implements Serializable {

	private static final long serialVersionUID = -6694774002287986318L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	private Long id;
	/**
	 * 用户的登录名
	 */
	@Column(name = "login_name")
	private String loginName;

	@Column(name = "password")
	private String password = "";
	/**
	 * 用户的真实姓名
	 */
	@Column(name = "real_name")
	private String realName;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "disabled", columnDefinition = "TINYINT(1)")
	private Boolean disabled = false;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "user_type")
	private String userType;

	//XXX:不eager不行.否则取用户拿不到角色.eager的坑(多对多时数量不对)在这里没事
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"),//
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * 获取用户的登录名
	 * 
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(final String username) {
		loginName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * 获取用户的真实姓名
	 * 
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	public void setRealName(final String name) {
		realName = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(final Boolean disabled) {
		this.disabled = disabled;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(final String userType) {
		this.userType = userType;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Set<Role> uroles) {
		roles = uroles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", loginName=" + loginName + ", password=" + password + ", realName=" + realName
				+ ", mobile=" + mobile + ", email=" + email + ", disabled=" + disabled + ", userType=" + userType
				+ ", uroles=" + roles + "]";
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

}