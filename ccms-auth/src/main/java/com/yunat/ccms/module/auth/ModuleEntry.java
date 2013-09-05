package com.yunat.ccms.module.auth;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.module.Module;

/**
 * 模块准入规则(module entry rule)。对应数据库的“模块-准入表”。
 * 类似于acl_entry
 * 
 * @author MaGiCalL
 */
@Entity
@Table(name = "module_entry")
public class ModuleEntry {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 模块id
	 */
	@Column(name = "module_id")
	private Long moduleId;
	/**
	 * 授权结果.类型为int表示非空
	 */
	@Column(name = "support_ops_mask")
	private int supportOpsMask;
	/**
	 * 备注
	 */
	@Column
	private String memo;

	/**
	 * 权限id
	 */
	@Column(name = "permission_id")
	private Long permissionId;
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 角色id
	 */
	@Column(name = "role_id")
	private Long roleId;

	/**
	 * 对应于moduleId的Module对象
	 */
	private transient Module module;
	/**
	 * 对应于permissionId的Permission对象
	 */
	private transient Permission permission;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(final Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(final String note) {
		memo = note;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(final Long permissionId) {
		this.permissionId = permissionId;
	}

	public int getSupportOpsMask() {
		return supportOpsMask;
	}

	public void setSupportOpsMask(final int supportOpMask) {
		supportOpsMask = supportOpMask;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(final Module module) {
		this.module = module;
	}

	public Collection<SupportOp> getSupportOps() {
		return SupportOp.supportOps(getSupportOpsMask());
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(final Permission permission) {
		this.permission = permission;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(final Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "ModuleEntry [id=" + id + ", moduleId=" + moduleId + ", supportOpsMask=" + supportOpsMask + ", memo="
				+ memo + ", permissionId=" + permissionId + ", userId=" + userId + ", roleId=" + roleId + "]";
	}
}
