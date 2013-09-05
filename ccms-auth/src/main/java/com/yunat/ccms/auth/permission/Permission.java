package com.yunat.ccms.auth.permission;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tds_permission")
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1750155841303910272L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "memo")
	private String memo;

	@Column(name = "permission_key")
	private String permissionKey;

	public Long getId() {
		return id;
	}

	public void setId(final Long permissionId) {
		id = permissionId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String permissionName) {
		name = permissionName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(final String memo) {
		this.memo = memo;
	}

	public String getPermissionKey() {
		return permissionKey;
	}

	public void setPermissionKey(final String permissionKey) {
		this.permissionKey = permissionKey;
	}

	@Override
	public String toString() {
		return "Permission [permissionId=" + id + ", permissionName=" + name + ", memo=" + memo + ", permissionKey="
				+ permissionKey + "]";
	}

}