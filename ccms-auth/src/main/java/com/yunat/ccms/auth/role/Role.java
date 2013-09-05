package com.yunat.ccms.auth.role;

import java.io.Serializable;
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

import com.yunat.ccms.auth.permission.Permission;

@Entity
@Table(name = "tb_role")
public class Role implements Serializable {

	private static final long serialVersionUID = -1767364792150270155L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "memo")
	private String memo;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "tb_role_permission", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),//
			inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
	private Set<Permission> permisssions;

	public Long getId() {
		return id;
	}

	public void setId(final Long roleId) {
		id = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String roleName) {
		name = roleName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(final String memo) {
		this.memo = memo;
	}

	public Set<Permission> getPermisssions() {
		return permisssions;
	}

	public void setPermisssions(final Set<Permission> permisssions) {
		this.permisssions = permisssions;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + id + ", roleName=" + name + ", memo=" + memo + ", permisssions=" + permisssions + "]";
	}

}