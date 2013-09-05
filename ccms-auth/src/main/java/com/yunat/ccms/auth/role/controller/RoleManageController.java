package com.yunat.ccms.auth.role.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;
import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.role.RoleRepository;

/**
 * 系统管理-角色管理
 * 
 * @author yinwei
 * @version 1.0
 * @date 2013-04-02
 */

@Controller
@RequestMapping(value = "/role-manager/*")
public class RoleManageController {

	@Autowired
	RoleRepository roleRepository;

	@RequestMapping(value = "/role", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Role findRoleByOne(@RequestParam final Long roleId) throws Exception {
		Role roleEntity;
		try {
			roleEntity = roleRepository.findOne(roleId);
		} catch (final Exception e) {
			throw new Exception("获取角色失败！");
		}
		return roleEntity;
	}

	@RequestMapping(value = "/role", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public void saveRole(final Role role, final Long[] permisssionIds) throws Exception {
		try {
			//设置该角色的权限
			role.setPermisssions(constructCheckPermisssionSet(permisssionIds));
			roleRepository.save(role);
		} catch (final Exception e) {
			throw new Exception("添加角色失败！");
		}
	}

	private Set<Permission> constructCheckPermisssionSet(final Long[] permissionIds) {
		final Set<Permission> permissionCheckSet = Sets.newHashSet();
		for (final Long permissionId : permissionIds) {
			final Permission permission = new Permission();
			permission.setId(permissionId);
			permissionCheckSet.add(permission);
		}
		return permissionCheckSet;
	}

}
