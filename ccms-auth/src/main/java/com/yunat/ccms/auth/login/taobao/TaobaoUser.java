package com.yunat.ccms.auth.login.taobao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.yunat.ccms.auth.user.User;

/**
 * 淘宝用户
 * 
 * @author wenjian.liang
 */
@Entity
@Table(name = "tb_sys_taobao_user")
public class TaobaoUser implements Serializable {

	private static final long serialVersionUID = -6593468098628104238L;

	@Id
	@GeneratedValue(generator = "persistenceGenerator")
	@GenericGenerator(name = "persistenceGenerator", strategy = "foreign",//
			parameters = { @Parameter(name = "property", value = "user") })
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	@AccessType(value = "property")
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id", referencedColumnName = "id", unique = true, nullable = false, updatable = false)
	private User user;

	@Column(name = "plat_user_id", nullable = false, unique = true, length = 20)
	private String platUserId;

	@Column(name = "plat_user_name", nullable = false, length = 100)
	private String platUserName;

	@Column(name = "is_subuser", nullable = true, length = 2)
	private boolean isSubuser;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plat_shop_id", referencedColumnName = "shop_id", unique = false, nullable = false,
			updatable = true)
	private TaobaoShop platShop;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the sysuser
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param sysuser
	 *            the sysuser to set
	 */
	public void setUser(final User sysuser) {
		user = sysuser;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (platUserId == null ? 0 : platUserId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TaobaoUser other = (TaobaoUser) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (platUserId == null) {
			if (other.platUserId != null) {
				return false;
			}
		} else if (!platUserId.equals(other.platUserId)) {
			return false;
		}
		return true;
	}

	public String getPlatUserId() {
		return platUserId;
	}

	public void setPlatUserId(final String platUserId) {
		this.platUserId = platUserId;
	}

	public String getPlatUserName() {
		return platUserName;
	}

	public void setPlatUserName(final String platUserName) {
		this.platUserName = platUserName;
	}

	public boolean getIsSubuser() {
		return isSubuser;
	}

	public void setIsSubuser(final boolean isSubuser) {
		this.isSubuser = isSubuser;
	}

	public TaobaoShop getPlatShop() {
		return platShop;
	}

	public void setPlatShop(final TaobaoShop platShop) {
		this.platShop = platShop;
	}

	public void setSubuser(final boolean isSubuser) {
		this.isSubuser = isSubuser;
	}

}